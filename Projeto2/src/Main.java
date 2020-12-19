import java.util.Scanner;
import java.io.*;

/**
 * 
 * @authors Ricardo e Vladyslav
 *
 */
public class Main {

	private static final String ROLETA = "roleta";
	private static final String PUZZLE = "puzzle";
	private static final String PAINEL = "painel";
	private static final String PONTOS = "pontos";
	private static final String SAIR = "sair";
	private static final String NAO_ACABOU = "O jogo ainda nao tinha terminado";
	private static final String PARABENS = "Parabens!";
	private static final String MOEDA = "euros";
	private static final String GANHOU = "Ganhou";
	private static final String PERDEU = "Infelizmente nao ganhou dinheiro";
	private static final String VALOR_INVALIDO = "Valor invalido";
	private static final String LETRA_INVALIDA = "Letra invalida";
	private static final String JOGO_JA_TERMINOU = "O jogo terminou";
	private static final String COMANDO_INVALIDO = "Comando invalido";

	private static void getSecretLines(int[] linesOfTheFile, int numberOfRounds, Scanner input) {
		for (int i = 0; i < numberOfRounds; i++) {
			linesOfTheFile[i] = input.nextInt();

		}
	}

	private static void addSecrets(Scanner file, SystemCommands game, int[] linesOfTheFile) {
		int nrOfSecrets = 0;
		int counter = 0;
		int i = 0;
		int a = 0;
		String[] copy = new String[50];
		while (file.hasNextLine()) {
			copy[i] = file.nextLine();
			i++;
		}
		while (nrOfSecrets != linesOfTheFile.length) {

			if (linesOfTheFile[a] == counter) {
				game.addSecret(copy[counter - 1]);
				counter = 0;
				nrOfSecrets++;
				a++;
			} else {
				counter++;
			}
		}
		file.close();
	}

	// Adiciona um número 'n' de concorrentes
	private static void addContestants(SystemCommands game, Scanner input, int numberOfContestants) {
		for (int i = 0; i < numberOfContestants; i++) {
			game.addContestant(input.nextLine());
		}
	}

	// verifica as possibilidades no fim do jogo
	private static void checkQuitOutcome(SystemCommands game) {
		if (!game.isCompleted()) {
			System.out.println(NAO_ACABOU);
		} else if (game.isCompleted() && game.getTotalPoints() > 0) {
			System.out.println(PARABENS + " " + GANHOU + " " + game.getTotalPoints() + " " + MOEDA);
		} else if (game.isCompleted() && game.getTotalPoints() <= 0) {
			System.out.println(PARABENS + " " + PERDEU);
		}
	}

	/**
	 * verifica as possibilidades da opção puzzle
	 * 
	 * @pre: guess != null && 0 < guess.length() < 100
	 */

	private static void checkPuzzle(Scanner input, SystemCommands game) {
		String guess = input.nextLine();
		guess = guess.trim();
		if (game.isCompleted() && game.getCurrentRound() == game.getMaxRounds()) { // neste caso se o segredo já estiver
																					// revelado
			System.out.println("O jogo terminou");
		} else if (game.isGuessCorrect(guess)) {
			game.sucess();
			game.nextRound();
		} else {
			game.fail();
			game.nextContestant();
		}
	} 

	private static void roletaOutcomes(Scanner input, SystemCommands game) {
		int roulettePoints = input.nextInt();
		String letter = input.nextLine();
		letter = letter.trim();
		if (roulettePoints <= 0) {
			System.out.println(VALOR_INVALIDO);
		} else if (!game.isLetter(letter.charAt(0)) || letter.length() != 1) {
			System.out.println(LETRA_INVALIDA);
		} else if (game.getCurrentRound() == game.getMaxRounds()) { //game.isCompleted() && game.isLetter(letter.charAt(0)) || 
			System.out.println(JOGO_JA_TERMINOU);
		} else {
			verification(roulettePoints, letter, game);
		}

	}

	// verifica se a pessoa acertou na letra ou errou e adiciona ou remove pontos
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
	
	
	private static void lastPlay(SystemCommands game) {
		if (game.isCompleted()) {
			game.nextRound();
		}
	}
	
	private static void printPoints(SystemCommands game) {
		String[] names = game.getNames();
		for(int i = 0 ; i < game.getContestant(); i++) {
			System.out.println(names[i] + ": " + game.getMoney(i) + " euros; " + game.getPoints(i) + " pontos");
		}
	}
	
	/**
	 * 
	 * @pre: rouletPoints > 0
	 * @pre: 0 < letter.length() < 40 && letter != null
	 * 
	 */

	// irá executar uma das opções escolhidas pelo utilizador
	private static void executeOption(Scanner input, String option, SystemCommands game) {
		switch (option) {

		case ROLETA:
			roletaOutcomes(input, game);
			break;
		case PUZZLE:
			checkPuzzle(input, game);
			break;
		case PAINEL:
			System.out.println(game.getThePanel());
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
		
		String fileName = "\\Users\\vlady\\Documents\\topSecret.txt";
		FileReader reader = new FileReader(fileName);
		Scanner file = new Scanner(reader);
		Scanner input = new Scanner(System.in);

		int numberOfRounds = input.nextInt();
		int numberOfContestants = input.nextInt();
		
		SystemCommands game = new SystemCommands(numberOfRounds, numberOfContestants);

		int[] linesOfTheFile = new int[numberOfRounds];

		getSecretLines(linesOfTheFile, numberOfRounds, input);

		addSecrets(file, game, linesOfTheFile);
		input.nextLine();

		addContestants(game, input, numberOfContestants);

		SecretIterator secretIt = game.iteratorOfSecrets();
		ContestantIterator contestantIt = game.iteratorOfContestants();
		
		String option;
		do {
			option = input.next();
			executeOption(input, option, game); // tinhamos adicionado aquilo algo no argumento e passado para as
														// outras coisas

		} while (!option.equals(SAIR));
		input.close();
	}

}

//Feito
//Rondas, Contestants em arrays. Trocamos de rondas e de contestants.
//Temos iteradores para o registo de concorrentes e segredos. E para trocar de segredo e de concorrente

//Falta
//Função que inicializa um nova ronda (quando painel.equals(segredo)) : Criar um painel com o novo segredo. Coloca os pontos de toda a gente a 0. Guarda o dinheiro do vencedor.
//E passa para o proximo concorrente