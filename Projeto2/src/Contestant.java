
public class Contestant {
	
	// Variaveis
	String name;
	int points;

	// Construtor
	public Contestant(String name) {
		this.name = name;
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
	
	// Atualiza os pontos
	public void updatePoints(int score) {
		points = points + score;
	}

}
