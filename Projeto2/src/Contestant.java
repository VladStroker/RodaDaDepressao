
public class Contestant {
	
	// Variaveis
	String name;
	int points, euros;

	// Construtor
	public Contestant(String a) {
		name = a;
		points = 0;
	}

	// Devolve o nome
	public String returnName() {
		return name;
	}

	// Devolve os pontos
	public int returnPoints() {
		return points;
	}
	
	public void resetPoints() {
		points = 0;
	}
	
	// Atualiza os pontos
	public void updatePoints(int a) {
		points = points + a;
	}

	public void updateMoney(int a) {
		euros = euros + a;
	}
}
