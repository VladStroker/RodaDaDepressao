/**
 * 
 * @authors Ricardo e Vladyslav
 * 
 * 
 * Esta classe gere todas as operaçoes associadas ao concorrente
 *
 *
 */

public class Contestant {
	
	// Variaveis
	private static final int INC_ROUNDS_WON = 1;
	private String name;
	private int points, money, roundsWon;

	/**
	 * Construtor
	 * @param name ---> nome do concorrentes
	 * @pre ---> name != null
	 */
	public Contestant(String name) {
		this.name = name;
		points = 0;
		roundsWon = 0;
		money = 0;
	}

	/**
	 * Devolver o nome do concorrente
	 * @return ---> nome do concorrente
	 */
	public String returnName() {
		return name;
	}
	
	/** Compara lexicograficamente os nomes dos concorrentes
	 * 
	 * @param other ---> outro concorrente para comparar os nomes 
	 * @return ---> vai devolver um inteiro 1 caso seja verificada a condição do compareTo
	 * @pre ---> other != null
	 */
	public int compareTo(Contestant other) {
		return returnName().compareTo(other.returnName());
	}

	/**
	 *  
	 * @return ---> Devolve os pontos do concorrente
	 */
	public int returnPoints() {
		return points;
	}
	
	// Reinicia os pontos do concorrente
	public void resetPoints() {
		points = 0;
	}
	
	/**	 
	 * @return ---> Devolve o dinheiro que o concorrente tem
	 */
	public int returnEuros() {
		return money;
	}
	
	/**
	 *  Atualiza os pontos
	 * @param a ---> valor para atualizar os pontos
	 */
	public void updatePoints(int a) {
		points = points + a;
	}
	
	/** Atualiza o dinheiro
	 * 
	 * @param a ---> valor para atualizar o dinheiro
	 */
	public void updateMoney(int a) {
		if(a >= 0 ) {
		money = money + a;
		}
	}
	
	/**
	 * 
	 * @return ---> Número de rondas ganhas pelo concorrente
	 */
	public int returnRoundsWon() {
		return roundsWon;
	}

	 // Adiciona um ronda às rondas ganhas
	public void incRoundsWon() {
		roundsWon = roundsWon + INC_ROUNDS_WON;
	}
}