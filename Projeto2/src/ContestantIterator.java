/**
 * 
 * @authors Ricardo e Vladyslav
 *
 */



public class ContestantIterator {
	// Variáveis
	private Contestant[] contestants;
	private int counter;
	private int nextContestant;
	
	/**
	 *  Construtor
	 * @param contestants ---> array de concorrentes
	 * @param counter
	 */
	public ContestantIterator(Contestant[] contestants, int counter) {
		this.contestants = contestants;
		this.counter = counter ;
		nextContestant = 0;
	}
	
	/**
	 *  Verifica se existe um próximo concorrente no iterador
	 * @return ---> verdadeiro caso existe um próximo concorrente
	 */
	public boolean hasNext() {
		return nextContestant < counter;
	} 
	
	/** 
	 * Avança para o proximo concorrente 
	 * @pre hasNext()
	 */
	public Contestant next() {
		return contestants[nextContestant++];
	}
	
	/**
	 *  
	 * @return ---> Devolve o concorrente atual
	 */
	public Contestant currentC() {
		return contestants[nextContestant];
	}
	
	/**
	 *  Reinicia o iterador de concorrentes, volta ao inicio do iterador
	 */
	public void resetContestant() {
		nextContestant = 0;
	}
}
