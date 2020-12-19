/**
 * @authors Ricardo e Vladyslav
 * 
 *          esta classe é o nosso sistema e irá lidar também com a distribuição
 *          de pontos e realizará uns métodos de verificação de condições
 * 
 * 
 */

public class SystemCommands {
	/** Constantes **/
	private static final int BONUS = 1000; // valor de pontos a adicionar caso o palpite esteja correto
	private static final int FAIL = 2000; // valor de pontos a subtrair caso o palpite esteja incorreto
	/** Variáveis de instância **/
	private int secretCount, contestantCount, round; // pontos do concorrente
	private Secret[] secrets;
	private Contestant[] contestants;
	private int numberOfRounds, numberOfContestants;
	private ContestantIterator contestantIt;
	private SecretIterator secretIt;

	/**
	 * Construtor
	 * 
	 * @param ---> segredo do jogo
	 * @pre: 1 < name.length() < 40 && name != null
	 */
	public SystemCommands(int nrOfRounds, int nrOfContestants) {
		this.numberOfRounds = nrOfRounds;
		this.numberOfContestants = nrOfContestants;
		secretCount = 0;
		round = 0;
		contestantCount = 0;
		contestants = new Contestant[nrOfContestants];
		secrets = new Secret[nrOfRounds];
	}

	public SecretIterator iteratorOfSecrets() {
		secretIt = new SecretIterator(secrets, secretCount);
		return secretIt;
	}

	public ContestantIterator iteratorOfContestants() {
		contestantIt = new ContestantIterator(contestants, contestantCount);
		return contestantIt;
	}

	// Construtor de segredos
	public void addSecret(String secret) {
		secrets[secretCount] = new Secret(secret);
		secrets[secretCount].createPanel(secret);
		secretCount++;
	}

	// Construtor de contestants
	public void addContestant(String name) {
		contestants[contestantCount] = new Contestant(name);
		contestantCount++;
	}

	/*
	 * mudança de ronda vai crashar quando chegar a ronda 5 de 4 por exemplo //
	 * agora talvez não porque o iterador tem de ter um próximo
	 */
	public void nextRound() {
		if (secretIt.currentS().getSecret().equals(secretIt.currentS().puzzle()) && secretIt.hasNext()) {
			contestantIt.currentC().updateMoney(contestantIt.currentC().returnPoints());
			round++;
			resetPoints();
			nextContestant();
			secretIt.next();
			// limpar os pontos e pô-los como dinheiro a serio
		}
	}

	// mudança de concorrente
	/*
	 * public void nextContestant() { if (!contestantIt.hasNext()) { contestantIt =
	 * iteratorOfContestants(); } else {
	 * 
	 * contestantIt.next(); } }
	 */

	public void nextContestant() {
		if (contestantIt.hasNext()) {
			contestantIt.next();
		} else {
			contestantIt = iteratorOfContestants();
			contestantIt.resetContestant(); // currentC = 0; } }
		}
	}

	// while(contestantIt.hasNext()) {

	/**
	 * verificação se a palavra foi adivinhada por completo
	 * 
	 * @return true ---> caso o segredo estiver totalmente adivinhado
	 */
	public boolean isCompleted() {
		return secretIt.currentS().completed();

	}

	/**
	 * verificação se o palpite do utilizador é correto
	 * 
	 * @param guess ---> palpite do utilizador
	 * @return true ---> se o palpite for correto
	 * @pre: guess != null && 0 < guess.length() < 100
	 */

	public boolean isGuessCorrect(String guess) {
		return secretIt.currentS().isGuessRigth(guess);
	}

	/**
	 * verificação se a letra introduzida pelo utilizador pertence de 'a' até 'z'
	 * minuscula
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso a letra pertença entre 'a' e 'z'
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean isLetter(char letter) {
		return (letter >= 'a' && letter <= 'z');
	}

	/**
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso ele detecte uma letra que já foi descoberta
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean isLetterRepeated(char letter) {
		return secretIt.currentS().RepeatedLetter(letter);
	}

	/**
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> se a letra existir no segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */
	public boolean isTheLetterInTheSecret(String letter) {
		return secretIt.currentS().belongs(letter);
	}

	/**
	 * @param letter         ---> letra introduzida pelo utilizador
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: letter != null && 0 < letter.length() < 40 && numberRoulete > 0
	 */

	public void pointsAdd(String letter, int numberRoulete) {
		secretIt.currentS().searchChar(letter.charAt(0));
		// secrets[round].detectCharacter(letter.charAt(0));
		int value = numberRoulete * secretIt.currentS().getCounter();
		contestantIt.currentC().updatePoints(value);// adição dos pontos quando a pessoa acerta na letra
	}

	/**
	 * subtração dos pontos caso o utilizador não adivinhou a letra
	 * 
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: numberRoulette > 0
	 */
	public void pointsPenalize(int numberRoulette) {
		contestantIt.currentC().updatePoints(-numberRoulette);

	}

	public String[] getNames() {
		String[] tmp = new String[numberOfContestants];
		for (int i = 0; i < numberOfContestants; i++) {
			tmp[i] = contestants[i].returnName();
		}
		return tmp;
	}

	/**
	 * vai devolver o painel criado
	 * 
	 * @return segredo em forma de painel
	 */
	public String getThePanel() {
		return secretIt.currentS().puzzle();
	}

	public int getMaxRounds() {
		return numberOfRounds;
	}

	// desconta pontos quando o utilizador erra no palpite

	public void fail() {
		contestantIt.currentC().updatePoints(-FAIL);
	}

	// adição de pontos quando o utilizador acerta no palpite
	public void sucess() {
		contestantIt.currentC().updatePoints(BONUS);
	}

	public int getContestant() {
		return numberOfContestants;
	}

	public int getMoney(int i) {
		return contestants[i].returnEuros();
	}

	public int getPoints(int i) {
		return contestants[i].returnPoints();
	}
	
	public int getCurrentRound() {
		return round;
	}

	private void resetPoints() {
		for (int i = 0; i < numberOfContestants; i++) {
			contestants[i].resetPoints();
		}
	}
}
