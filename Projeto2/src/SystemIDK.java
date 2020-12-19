/**
 * @authors Ricardo e Vladyslav
 * 
 *         esta classe é o nosso sistema e irá lidar também com a distribuição
 *         de pontos e realizará uns métodos de verificação de condições
 * 
 * 
 */

public class SystemIDK {
	/** Constantes **/
	private static final int BONUS = 1000; // valor de pontos a adicionar caso o palpite esteja correto
	private static final int FAIL = 2000; // valor de pontos a subtrair caso o palpite esteja incorreto
	/** Variáveis de instância **/
	private int points, secretCount, contestantCount, round, currentC; // pontos do concorrente
	private Secret[] secrets;
	private Contestant[] contestants;
	private String name; // nome do concorrente
	private int numberOfRounds;

	/**
	 * Construtor
	 * 
	 * @param ---> segredo do jogo
	 * @pre: 1 < name.length() < 40 && name != null
	 */
	public SystemIDK(int numberOfRounds, int numberOfContestants) {
		this.numberOfRounds = numberOfRounds;
		secretCount = 0;
		contestantCount = 0;
		points = 0;
		round = 0;
		currentC = 0;
		contestants = new Contestant[numberOfContestants];
		secrets = new Secret[numberOfRounds];
		ContestantIterator c1;
		SecretIterator s1;
	}
	
	public int getMaxRounds() {
		return numberOfRounds;
	}
	
	public int getCurrentRound() {
		return round;
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
	
	public SecretIterator iteratorOfSecrets() {
		SecretIterator s1 = new SecretIterator(secrets,secretCount);
		return s1;
	}
	
	public ContestantIterator iteratorOfContestants() {
		ContestantIterator c1 = new ContestantIterator(contestants,contestantCount);
		return c1;
	}
	
	// mudança de ronda
	public void nextRound() {
		if (secrets[round].getSecret().equals(secrets[round].puzzle()) && s1.hasNext()) {
			round++;
		}
	}
	
	
	

	/**
	 * vai devolver o painel criado
	 * 
	 * @return segredo em forma de painel
	 */
	public String getThePanel() {
		return secrets[round].puzzle(); // estava so round
	}

	/**
	 * 
	 * @return ---> nome do concorrente
	 */
	public String getName() {
		return name;
	}

	/**
	 * verificação se a palavra foi adivinhada por completo
	 * 
	 * @return true ---> caso o segredo estiver totalmente adivinhado
	 */
	public boolean isCompleted() {
		return secrets[round].completed();

	}

	/**
	 * verificação se o palpite do utilizador é correto
	 * 
	 * @param guess ---> palpite do utilizador
	 * @return true ---> se o palpite for correto
	 * @pre: guess != null && 0 < guess.length() < 100
	 */

	public boolean isGuessCorrect(String guess) {
		return secrets[round].isGuessRigth(guess);
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

	public boolean isTheLetterRepeated(char letter) {
		return secrets[round].detectRepeatedLetter(letter);
	}

	/**
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> se a letra existir no segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */
	public boolean isTheLetterInTheSecret(String letter) {
		return secrets[round].belongs(letter);
	}

	/**
	 * este método irá verificar se a letra introduzida é válida e se verifica as
	 * condições para adicionar pontos
	 * 
	 * @param letter         ---> letra introduzida pelo utilizador
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: letter != null && 0 < letter.length() < 40 && numberRoulete > 0
	 */

	public void pointsAdd(String letter, int numberRoulete) {
		secrets[round].searchChar(letter);
		secrets[round].detectCharacter(letter.charAt(0));
		int value = points + (numberRoulete * secrets[round].getCounter());
		contestants[currentC].updatePoints(value);// adição dos pontos quando a pessoa acerta na letra
	}

	/**
	 * subtração dos pontos caso o utilizador não adivinhou a letra
	 * 
	 * @param numberRoulette ---> número escolhido na roleta
	 * @pre: numberRoulette > 0
	 */
	public void pointsPenalize(int numberRoulette) {
		points = points - numberRoulette;

	}

	/**
	 * devolver a pontuação do utilizador
	 * 
	 * @return points ---> devolve a pontuação do utilizador
	 */
	public int getTotalPoints() {
		return points;
	}

	// desconta pontos quando o utilizador erra no palpite

	public void fail() {
		points = points - FAIL;
	}

	// adição de pontos quando o utilizador acerta no palpite
	public void sucess() {
		points = points + BONUS;
	}

}
