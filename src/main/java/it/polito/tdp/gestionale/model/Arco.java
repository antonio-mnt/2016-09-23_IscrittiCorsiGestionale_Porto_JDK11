package it.polito.tdp.gestionale.model;

public class Arco {
	
	private Studente studente;
	private Corso corso;
	
	public Arco(Studente studente, Corso corso) {
		super();
		this.studente = studente;
		this.corso = corso;
	}

	public Studente getStudente() {
		return studente;
	}

	public void setStudente(Studente studente) {
		this.studente = studente;
	}

	public Corso getCorso() {
		return corso;
	}

	public void setCorso(Corso corso) {
		this.corso = corso;
	}

	@Override
	public String toString() {
		return "Arco [studente=" + studente + ", corso=" + corso + "]";
	}
	
	

}
