package it.polito.tdp.gestionale.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.gestionale.db.DidatticaDAO;

public class Model {
	
	
	private SimpleGraph<Nodo, DefaultEdge> grafo;
	private DidatticaDAO dao;
	private List<Corso> corsi;
	private List<Studente> studenti;
	private Map<Integer,Studente> idMapS;
	private Map<String,Corso> idMapC;
	private List<Arco> archi;
	private Map<Studente,Integer> nStudentiCorso;
	private Map<Integer,Integer> frequenza;
	private List<Studente> studentiFrequentanti;
	private Set<Corso> best;
	private int size;
	
	
	public Model() {
		this.dao = new DidatticaDAO();
	}
	
	
	public void creaGrafo() {
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		this.corsi = new ArrayList<>(this.dao.getCorsi());
		this.studenti = new ArrayList<>(this.dao.getStudenti());
		
		this.idMapC = new HashMap<>();
		this.idMapS = new HashMap<>();
		
		
		for(Studente s: this.studenti) {
			this.idMapS.put(s.getMatricola(), s);
		}
		
		for(Corso c: this.corsi) {
			this.idMapC.put(c.getCodins(), c);
		}
		
		
		Graphs.addAllVertices(this.grafo, this.corsi);
		Graphs.addAllVertices(this.grafo, this.studenti);
		
		this.archi = new ArrayList<>(this.dao.getArchi(this.idMapS, this.idMapC));
		
		for(Arco a: this.archi) {
			this.grafo.addEdge(a.getStudente(), a.getCorso());
		}
		
		
	}
	
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
		
		
	public int getNumeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public void calcolaFrequenza() {
		
		this.nStudentiCorso = new HashMap<>();
		this.frequenza = new TreeMap<>();
		
		for(Nodo no: this.grafo.vertexSet()) {
			if(no instanceof Studente) {
				int n = Graphs.neighborListOf(this.grafo, no).size();
				this.nStudentiCorso.put((Studente) no, n);
			}
			
		}
		
		this.calcolaStudentiCheSeguonoAlmenoUnCorso();
		
		for(Integer i: this.nStudentiCorso.values()) {
			if(this.frequenza.containsKey(i)) {
				this.frequenza.put(i, this.frequenza.get(i)+1);
			}else {
				this.frequenza.put(i, 1);
			}
		}
		
	}


	public Map<Integer, Integer> getFrequenza() {
		return frequenza;
	}
	
	public void calcolaStudentiCheSeguonoAlmenoUnCorso() {
		this.studentiFrequentanti = new ArrayList<>();
		
		for(Studente s: this.studenti) {
			if(this.nStudentiCorso.get(s)!=0) {
				this.studentiFrequentanti.add(s);
			}
		}
		
	}
	
	
	public void trovaInsieme() {
		
		
		this.best = null;
		this.size = this.corsi.size()+1;
		Set<Corso> parziale = new HashSet<>();
		
		cerca(parziale,0);
	}


	private void cerca(Set<Corso> parziale, int livello) {
		
		
		if(parziale.size()<this.size) {
			if(tuttiStudenti(parziale).containsAll(this.studentiFrequentanti)) {
				this.best = new HashSet<>(parziale);
				this.size = this.best.size();
				System.out.println("siiiii"+"   "+ this.best.size()); //
				return;
			}
		}else {
			return;
		}
		
		
		
		if(livello == this.corsi.size()) {
			return;
		}
		
		
		parziale.add(this.corsi.get(livello));
		cerca(parziale,livello+1);
		parziale.remove(this.corsi.get(livello));
		
		
		cerca(parziale, livello+1);
		
		
		
	}


	private List<Studente> tuttiStudenti(Set<Corso> parziale) {
		
		List<Studente> tutti = new ArrayList<>();
		List<Nodo> nodi = new ArrayList<>();
		
		for(Corso c: parziale) {
			nodi.addAll(Graphs.neighborListOf(this.grafo, c));
		}
		
		for(Nodo n: nodi) {
			if(n instanceof Studente) {
				tutti.add((Studente) n);
			}
		}
		
		return tutti;
	}


	public Set<Corso> getBest() {
		return best;
	}
	
	
	
	

}
