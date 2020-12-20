public class Contestant {
	
	// Variaveis
	String name;
	int points, euros, roundsWon;

	// Construtor
	public Contestant(String a) {
		name = a;
		points = 0;
	}

	// Devolve o nome
	public String returnName() {
		return name;
	}
	public int compareTo(Contestant other) {
		return returnName().compareTo(other.returnName());
	}

	// Devolve os pontos
	public int returnPoints() {
		return points;
	}
	
	public void resetPoints() {
		points = 0;
	}
	
	public int returnEuros() {
		return euros;
	}
	
	// Atualiza os pontos
	public void updatePoints(int a) {
		points = points + a;
	}

	public void updateMoney(int a) {
		if(a >= 0 ) {
		euros = euros + a;
		}
	}
	
	public int returnRoundsWon() {
		return roundsWon;
	}
	
	public void incRoundsWon() {
		roundsWon = roundsWon + 1;
	}
}