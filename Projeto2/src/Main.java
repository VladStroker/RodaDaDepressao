import java.util.Scanner;
import java.io.*;

/**
 * 
 * @authors Ricardo e Vladyslav
 *
 */
public class Main {
	//Contantes
	private static final String ROLETA = "roleta";
	private static final String PUZZLE = "puzzle";
	private static final String PAINEL = "painel";
	private static final String PONTOS = "pontos";
	private static final String SAIR = "sair";
	private static final String NAO_ACABOU = "O jogo ainda nao tinha terminado";
	private static final String VALOR_INVALIDO = "Valor invalido";
	private static final String LETRA_INVALIDA = "Letra invalida";
	private static final String JOGO_JA_TERMINOU = "O jogo terminou";
	private static final String COMANDO_INVALIDO = "Comando invalido";

	//Vai preencher a array com as linhas de segredos desejados
	private static void registerSecretLines(int[] linesOfTheFile, int numberOfRounds, Scanner input) {
		for (int i = 0; i < numberOfRounds; i++) {
			linesOfTheFile[i] = input.nextInt();

		}
	}

	//Adiciona os segredos de acordo com as linhas pedidas pelo utilizador 
	private static void addSecrets(Scanner file, SystemCommands game, int[] linesOfTheFile) {
		int line = 0;
		int a = 0;
		boolean enable = false;
		int[] registeredLines = new int[linesOfTheFile.length];
		int z = 0;
		String temp;
		String[] secrets = new String[linesOfTheFile.length];
		while (secrets.length != linesOfTheFile.length || file.hasNextLine()) {
			line++;
			enable = false;
			for (int y = 0; y < linesOfTheFile.length; y++) {
				if (linesOfTheFile[y] == line) {
					temp = file.nextLine();
					secrets[a] = temp;
					a++;
					enable = true;
					registeredLines[z] = line;
					z++;
				}
			}
			if (enable == false) {
				file.nextLine();
			}
		}
		file.close();
		
		//Vai criar uma array ordenada pela ordem de input dos segredos
		String[] copy = new String[linesOfTheFile.length];
		for (int k = 0; k < linesOfTheFile.length; k++) {
			for (int w = 0; w < linesOfTheFile.length; w++) {
				if (linesOfTheFile[k] == registeredLines[w]) {
					copy[k] = secrets[w];
				}
			}
		}
		for (int w = 0; w < linesOfTheFile.length; w++) {
			game.addSecret(copy[w]);
		}
	}

	// Adiciona um número 'n' de concorrentes
	private static void addContestants(SystemCommands game, Scanner input, int numberOfContestants) {
		for (int i = 0; i < numberOfContestants; i++) {
			game.addContestant(input.nextLine());
		}
	}

	// Verifica as possibilidades no fim do jogo
	private static void checkQuitOutcome(SystemCommands game) {
		game.sortContestants();
		if (game.getCurrentRound() != game.getMaxRounds()) {
			System.out.println(NAO_ACABOU);
		} else if (game.getCurrentRound() == game.getMaxRounds()) {
			System.out.println("Parabens! O maior premio foi " + game.getMaxPrize() + " euros");
		}
	}

	/**
	 * Verifica as possibilidades da opção puzzle
	 * 
	 * @pre: guess != null && 0 < guess.length() < 100
	 */
	private static void checkPuzzle(Scanner input, SystemCommands game) {
		String guess = input.nextLine();
		guess = guess.trim();
		if (game.getCurrentRound() == game.getMaxRounds()) {
			System.out.println("O jogo terminou");
		} else if (game.isGuessCorrect(guess)) {
			game.sucess();
			game.nextRound();
		} else {
			game.fail();
			game.nextContestant();
		}
	}

	// Verifica as possibilidades da opção roleta
	private static void roletaOutcomes(Scanner input, SystemCommands game) {
		int roulettePoints = input.nextInt();
		String letter = input.nextLine();
		letter = letter.trim();
		if (roulettePoints <= 0) {
			System.out.println(VALOR_INVALIDO);
		} else if (!game.isLetter(letter.charAt(0)) || letter.length() != 1) {
			System.out.println(LETRA_INVALIDA);
		} else if (game.getCurrentRound() == game.getMaxRounds()) {
			System.out.println(JOGO_JA_TERMINOU);
		} else {
			verification(roulettePoints, letter, game);
		}

	}

	// Verifica se a pessoa acertou na letra ou errou e adiciona ou remove pontos consequentemente
	private static void verification(int points, String letter, SystemCommands game) {
		letter = letter.trim();
		if (!game.isLetterRepeated(letter.charAt(0)) && game.isTheLetterInTheSecret(letter)) {
			game.pointsAdd(letter, points);
			lastPlay(game);
		} else {
			game.pointsPenalize(points);
			game.nextContestant();
		}
	}
	
	// Verifica se o segredo foi completo através do comando roleta
	private static void lastPlay(SystemCommands game) {
		if (game.isCompleted()) {
			game.nextRound();
		}
	}

	// Faz print aos pontos/euros dos concorrentes
	private static void printPoints(SystemCommands game) {
		game.sortContestants();
		Contestant[] nameList = game.getNames();
		for (int i = 0; i < game.getContestant(); i++) {
			System.out.println(nameList[i].returnName() + ": " + nameList[i].returnEuros() + " euros; "
					+ nameList[i].returnPoints() + " pontos");
		}

	}

	/**
	 * 
	 * @pre: rouletPoints > 0
	 * @pre: 0 < letter.length() < 40 && letter != null
	 * 
	 */

	// irá executar uma das opções escolhidas pelo utilizador
	private static void executeOption(Scanner input, String option, SystemCommands game, SecretIterator s1) {
		switch (option) {

		case ROLETA:
			roletaOutcomes(input, game);
			break;
		case PUZZLE:
			checkPuzzle(input, game);
			break;
		case PAINEL:
			if (s1.hasNext()) {
				System.out.println(game.getThePanel());
			} else {
				System.out.println(game.getLastSecret());
			}
			break;
		case PONTOS:
			printPoints(game);
			break;
		case SAIR:
			checkQuitOutcome(game);
			break;
		default:
			System.out.println(COMANDO_INVALIDO);
			input.nextLine();
			break;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {

	
		String fileName = "topSecret.txt";
		FileReader reader = new FileReader(fileName);
		Scanner file = new Scanner(reader);
		Scanner input = new Scanner(System.in);

		/**
		 * @pre --->  0 < numberOfRounds < 4 && 0 < numberOfContestants < 10
		 */
		
		int numberOfRounds = input.nextInt();
		int numberOfContestants = input.nextInt();

		SystemCommands game = new SystemCommands(numberOfRounds, numberOfContestants);

		int[] linesOfTheFile = new int[numberOfRounds];

		registerSecretLines(linesOfTheFile, numberOfRounds, input);

		addSecrets(file, game, linesOfTheFile);
		input.nextLine();

		addContestants(game, input, numberOfContestants);

		SecretIterator secretIt = game.iteratorOfSecrets();
		game.iteratorOfContestants();

		String option;
		do {
			option = input.next();
			executeOption(input, option, game, secretIt); 

		} while (!option.equals(SAIR));
		input.close();

	}
}

//https://www.youtube.com/watch?v=gJZcdGLAgaY