/**
 * @authors Ricardo e Vladyslav
 * 
 *          Esta classe é o nosso sistema e irá lidar também com a distribuição
 *          de pontos e realizará métodos de verificação de condições
 * 
 * 
 */

public class SystemCommands {
	/** Constantes **/
	private static final int WINNER_BONUS = 6000;
	private static final int BONUS = 1000; // valor de pontos a adicionar caso o palpite esteja correto
	private static final int FAIL = 2000; // valor de pontos a subtrair caso o palpite esteja incorreto
	
	/** Variáveis de instância **/
	private int secretCount, contestantCount, round; // pontos do concorrente
	private Secret[] secrets;
	private Contestant[] contestants, nameList;
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
		nameList = new Contestant[nrOfContestants];
	}

	// Criação de um iterador de segredos
	public SecretIterator iteratorOfSecrets() {
		secretIt = new SecretIterator(secrets, secretCount);
		return secretIt;
	}

	// Criação de um iterador de concorrentes
	public ContestantIterator iteratorOfContestants() {
		contestantIt = new ContestantIterator(contestants, contestantCount);
		return contestantIt;
	}

	/** 
	 * Adiona segredos
	 * @param secret
	 * @pre secret != null
	 */
	public void addSecret(String secret) {
		secrets[secretCount] = new Secret(secret);
		secrets[secretCount].createPanel(secret);
		secretCount++;
	}

	/**
	 *  Construtor de contestants
	 * @param name
	 * @pre name != null
	 */
	public void addContestant(String name) {
		contestants[contestantCount] = new Contestant(name);
		contestantCount++;
	}

	/*
	 * Função responsável pela troca de ronda
	 */
	public void nextRound() {
		if (secretIt.currentS().getSecret().equals(secretIt.currentS().returnPanel()) && secretIt.hasNext()) {
			contestantIt.currentC().updateMoney(contestantIt.currentC().returnPoints());
			contestantIt.currentC().incRoundsWon();
			resetPoints();
			round++;
			nextContestant();
			secretIt.next();
			updateNameList();
			sortContestants();
			if (round == numberOfRounds) {
				whoIsTheWinner();
			}
		}

	}

	// mudança de concorrente para o próximo
	public void nextContestant() {
		contestantIt.next();
		if (!contestantIt.hasNext()) {
			contestantIt.resetContestant();
		}
	}
	
	/**
	 * verifica se a palavra foi adivinhada por completo
	 * 
	 * @return true ---> caso o segredo estiver totalmente adivinhado
	 */
	public boolean isCompleted() {
		return secretIt.currentS().completed(); 

	}

	/**
	 * verifica se o palpite do utilizador é correto
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
	 * e se é minúscula
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso a letra pertença entre 'a' e 'z'
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean isLetter(char letter) {
		return (letter >= 'a' && letter <= 'z');
	}

	/**
	 * verifica se a letra ja foi adivinhada
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso ele detecte uma letra que já foi descoberta
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean isLetterRepeated(char letter) {
		return secretIt.currentS().repeatedLetter(letter);
	}

	/**
	 * verifica se a letra está no segredo
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> se a letra existir no segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */
	public boolean isTheLetterInTheSecret(String letter) {
		return secretIt.currentS().belongs(letter);
	}

	/**
	 * adiciona pontos
	 * @param letter         ---> letra introduzida pelo utilizador
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: letter != null && 0 < letter.length() < 40 && numberRoulete > 0
	 */

	public void pointsAdd(String letter, int numberRoulete) {
		secretIt.currentS().searchChar(letter.charAt(0));
		int value = numberRoulete * secretIt.currentS().getCounter();
		contestantIt.currentC().updatePoints(value);// adição dos pontos quando a pessoa acerta na letra
		updateNameList();
	}

	/**
	 * subtração dos pontos caso o utilizador não adivinhou a letra
	 * 
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: numberRoulette > 0
	 */
	public void pointsPenalize(int numberRoulette) {
		contestantIt.currentC().updatePoints(-numberRoulette);
		updateNameList();
	}

	// Ordenação dos concorrentes
	public void sortContestants() {
		updateNameList();
		orderAlphabetically();
		orderByPoints();
		orderByRoundsWon();
		orderByPrize();
	}

	// Devolve a variavel nameList, que tem os concorrentes ordenados
	public Contestant[] getNames() {
		return nameList; // nameList
	}

	/**
	 * vai devolver o painel criado
	 * 
	 * @return segredo em forma de painel
	 */
	public String getThePanel() {
		return secretIt.currentS().returnPanel();
	}

	// Vai devolver o numero total de rondas no jogo
	public int getMaxRounds() {
		return numberOfRounds;
	}

	// Vai devolver o ultimo segredo usado no jogo
	public String getLastSecret() {
		return secretIt.previousS().getSecret();
	}

	// Desconta pontos quando o utilizador erra no palpite
	public void fail() {
		contestantIt.currentC().updatePoints(-FAIL);
	}

	// Adição de pontos quando o utilizador acerta no palpite
	public void sucess() {
		contestantIt.currentC().updatePoints(BONUS);
	}

	/**
	 *  
	 * @return ---> Devolve o numero de concorrentes
	 * @pre numberOfContestants > 0 && numberOfContestants < 4
	 */
	public int getContestant() {
		return numberOfContestants;
	}

	/** 
	 * 
	 * @return ---> Devolve a ronda atual do jogo
	 */
	public int getCurrentRound() {
		return round;
	}

	// Adiciona a pontuação bónus a/aos concorrentes com o maior valor de euros
	private void whoIsTheWinner() {
		int maxPrize = getMaxPrize();
		if (!tiedMaxPrize(maxPrize)) {
			nameList[0].updateMoney(WINNER_BONUS);
		} else {
			int split = getTiedContestants(maxPrize);
			for (int i = 0; i < split; i++) {
				nameList[i].updateMoney(WINNER_BONUS / split);
			}
		}
	}

	// Este função reinicia todos os pontos dos concorrentes
	private void resetPoints() {
		for (int i = 0; i < numberOfContestants; i++) {
			contestants[i].resetPoints();
		}
	}

	// Aqui ordenamos os concorrentes por ordem decrescente de dinheiro
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

	// Aqui ordenamos os concorrentes por ordem decrescente de rondas ganhas
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

	// Aqui ordenamos os concorrentes por ordem decrescente de pontos ganhos
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

	// Aqui ordenamos os concorrentes por ordem alfabetica
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

	// Aqui vamos buscar o maior valor em dinheiro ganho no jogo
	public int getMaxPrize() {
		int max = 0;
		for (int i = 0; i < numberOfContestants; i++) {
			if (contestants[i].returnEuros() > max) {
				max = contestants[i].returnEuros();
			}
		}
		return max;
	}

	/** Este metodo vai devolver quantos concorrentes estao empatados com o premio maximo
	 * 
	 * @param maxPrize ---> Maior prémio do jogo
	 * @return ---> numero de concorrentes que estão empatados com o premio maximo
	 */
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

	//Atualiza a name list com as informações da variavel contestants[]
	private void updateNameList() {
		for (int i = 0; i < numberOfContestants; i++) { 
			nameList[i] = contestants[i];
		}
	}
	
	/** Verifica se existe empate com premio maximo
	 * 
	 * @param maxPrize ---> Maior prémio do jogo
	 * @return ---> verdadeiro caso se verifique empate no prémio maximo
	 */
	private boolean tiedMaxPrize(int maxPrize) {
		boolean state = false;
		for(int i = 1; i < numberOfContestants; i++) { 
			if(maxPrize == nameList[i].returnEuros()) {
				state = true;
			}
		}return state;
	}

	
}