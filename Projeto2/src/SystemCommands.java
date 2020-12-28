/**
 * @authors Ricardo e Vladyslav
 * 
 *          esta classe � o nosso sistema e ir� lidar tamb�m com a distribui��o
 *          de pontos e realizar� uns m�todos de verifica��o de condi��es
 * 
 * 
 */

public class SystemCommands {
	/** Constantes **/
	private static final int BONUS = 1000; // valor de pontos a adicionar caso o palpite esteja correto
	private static final int FAIL = 2000; // valor de pontos a subtrair caso o palpite esteja incorreto
	/** Vari�veis de inst�ncia **/
	private int secretCount, contestantCount; // pontos do concorrente
	private Secret[] secrets;
	private Contestant[] contestants, nameList;
	private int numberOfRounds, numberOfContestants, round;
	private ContestantIterator contestantIt;
	private SecretIterator secretIt;

	/**
	 * Construtor
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
		nameList = new Contestant[nrOfContestants];
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

	
	public void nextRound() {
		if (secretIt.currentS().getSecret().equals(secretIt.currentS().puzzle()) && secretIt.hasNext()) {
			contestantIt.currentC().updateMoney(contestantIt.currentC().returnPoints());
			contestantIt.currentC().incRoundsWon();
			resetPoints();
			nextContestant();
			secretIt.next();
			updateNameList();
			sortContestants();
			if (round == numberOfRounds) {
				whoIsTheWinner();
			}
		}
	}

	/*
	 */
	public void nextContestant() {
		contestantIt.next();
		if (!contestantIt.hasNext()) {
			contestantIt.resetContestant();
		}
	}


	/**
	 * verifica��o se a palavra foi adivinhada por completo
	 * 
	 * @return true ---> caso o segredo estiver totalmente adivinhado
	 */
	public boolean isCompleted() {
		return secretIt.currentS().completed();

	}

	/**
	 * verifica��o se o palpite do utilizador � correto
	 * 
	 * @param guess ---> palpite do utilizador
	 * @return true ---> se o palpite for correto
	 * @pre: guess != null && 0 < guess.length() < 100
	 */

	public boolean isGuessCorrect(String guess) {
		return secretIt.currentS().isGuessRigth(guess);
	}

	/**
	 * verifica��o se a letra introduzida pelo utilizador � uma letra m�nuscula
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso a letra perten�a entre 'a' e 'z'
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean isLetter(char letter) {
		return (letter >= 'a' && letter <= 'z');
	}

	/**
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso ele detecte uma letra que j� foi descoberta
	 * @pre: letter != null && 0 < letter.length() < 40
	 */
	
	public boolean isLetterRepeated(char letter) {
		return secretIt.currentS().RepeatedLetter(letter);
	}

	/**
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> se a letra existir no segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */
	public boolean isTheLetterInTheSecret(String letter) {
		return secretIt.currentS().belongs(letter);
	}

	/**
	 * @param letter ---> letra introduzida pelo utilizador
	 * @param numberRoulette ---> n�mero escolhido na roleta
	 * @pre: letter != null && 0 < letter.length() < 40 && numberRoulete > 0
	 */

	public void pointsAdd(String letter, int numberRoulete) {
		secretIt.currentS().searchChar(letter.charAt(0));
		int value = numberRoulete * secretIt.currentS().getCounter();
		contestantIt.currentC().updatePoints(value);
		updateNameList();
	}

	public void sortContestants() {
		updateNameList();
		orderAlphabetically();
		orderByPoints();
		orderByRoundsWon();
		orderByPrize();
	}
	
	/**
	 * subtra��o dos pontos caso o utilizador n�o adivinhou a letra
	 * 
	 * @param numberRoulette ---> n�mero escolhido na roleta
	 * @pre: numberRoulette > 0
	 */
	public void pointsPenalize(int numberRoulette) {
		contestantIt.currentC().updatePoints(-numberRoulette);
		updateNameList();
	}


	public Contestant[] getNames() {
		return nameList; // nameList
	}

	/**
	 * vai devolver o painel criado
	 * @return segredo em forma de painel
	 */
	public String getThePanel() {
		return secretIt.currentS().puzzle();
	}

	public int getMaxRounds() {
		return numberOfRounds;
	}

	public String getLastSecret() {
		return secretIt.previousS().getSecret();
	}

	// desconta pontos quando o utilizador erra no palpite

	public void fail() {
		contestantIt.currentC().updatePoints(-FAIL);
	}

	// adi��o de pontos quando o utilizador acerta no palpite
	public void sucess() {
		contestantIt.currentC().updatePoints(BONUS);
	}

	public int maxContestants() {
		return numberOfContestants;
	}

	public int getCurrentRound() {
		return round;
	}

	// esta fun��o vai devolver o maior premio existente
	private void whoIsTheWinner() {
		int maxPrize = getMaxPrize();
		if (!tiedMaxPrize(maxPrize)) {
			nameList[0].updateMoney(6000);
		} else {
			int split = getTiedContestants(maxPrize);
			for (int i = 0; i < split; i++) {
				nameList[i].updateMoney(6000 / split);
			}
		}
	}

	private void resetPoints() {
		for (int i = 0; i < numberOfContestants; i++) {
			contestants[i].resetPoints();
		}
	}

	private void orderByPrize() {
		for (int i = 1; i < numberOfContestants; i++) {
			for (int j = numberOfContestants - 1; j >= i; j--) {
				if (nameList[j - 1].returnEuros() < nameList[j].returnEuros()) {
					Contestant tmp = nameList[j - 1];
					nameList[j - 1] = nameList[j];
					nameList[j] = tmp;
				}
			}
		}
	}

	private void orderByRoundsWon() {
		for (int i = 1; i < numberOfContestants; i++) {
			for (int j = numberOfContestants - 1; j >= i; j--) {
				if (nameList[j - 1].returnRoundsWon() < nameList[j].returnRoundsWon()) {
					Contestant tmp = nameList[j - 1];
					nameList[j - 1] = nameList[j];
					nameList[j] = tmp;
				}
			}
		}
	}

	private void orderByPoints() {
		for (int i = 1; i < numberOfContestants; i++) {
			for (int j = numberOfContestants - 1; j >= i; j--) {
				if (nameList[j - 1].returnPoints() < nameList[j].returnPoints()) {
					Contestant tmp = nameList[j - 1];
					nameList[j - 1] = nameList[j];
					nameList[j] = tmp;
				}
			}
		}
	}

	private void orderAlphabetically() {
		for (int i = 1; i < numberOfContestants; i++) {
			for (int j = numberOfContestants - 1; j >= i; j--) {
				if (nameList[j - 1].compareTo(nameList[j]) > 0) {
					Contestant tmp = nameList[j - 1];
					nameList[j - 1] = nameList[j];
					nameList[j] = tmp;
				}
			}
		}
	}

	public int getMaxPrize() {
		int max = 0;
		for (int i = 0; i < numberOfContestants; i++) {
			if (contestants[i].returnEuros() > max) {
				max = contestants[i].returnEuros();
			}
		}
		return max;
	}

	private int getTiedContestants(int maxPrize) {
		int counter = 0;
		int tmp = nameList[0].returnEuros();
		for (int i = 0; i < numberOfContestants; i++) {
			if (tmp == nameList[i].returnEuros()) {
				counter++;
			}
		}
		return counter;
	}

	private void updateNameList() {
		for (int i = 0; i < numberOfContestants; i++) { // faz a mesma coisa que o clone
			nameList[i] = contestants[i];
		}
	}
	
	private boolean tiedMaxPrize(int maxPrize) {
		boolean state = false;
		for(int i = 1; i < numberOfContestants; i++) {
			if(maxPrize == nameList[i].returnEuros()) {
				state = true;
			}
		}return state;
	}

	/*
	 * if (tiedPrize()) {
			if (tiedRoundsWon()) {
				if (tiedPoints()) {
					orderAlphabetically();
				} else {
					orderAlphabetically();
					orderByPoints();
				}
			} else {
				orderAlphabetically();
				orderByPoints();
				orderByRoundsWon();
			}
		} else {
			orderAlphabetically();
			orderByPoints();
			orderByRoundsWon();
			orderByPrize();
		}
	 */
}