/**
 * @authors Ricardo e Vladyslav
 * 
 *          esta classe irá lidar com as operações relacionadas ao painel
 *          criação do mesmo, verificação de letras , substituição etc...
 * 
 */
public class Secret {
	// ** Variáveis de instância **/
	private String panel; // esta variável representa o painel
	private String secret; // esta variável é o segredo
	private int counter; // servirá para contar números de letras presentes na palavra

	/**
	 * Construtor
	 * 
	 * @param ---> segredo do jogo
	 * @pre: secret != null && 1 < secret.length() < 100
	 */

	public Secret(String secret) {
		this.secret = secret;
		counter = 0;
	}

	/**
	 * criação do painel da seguinte forma: ------
	 * 
	 * @param word ---> é o segredo
	 * @return ---> criação do painel para o jogo
	 * @pre: secret != null && 1 < secret.length() < 100
	 */

	public String createPanel(String secret) {
		char[] temppanel = secret.toCharArray();
		for (int i = 0; i < secret.length(); i++) {
			if (temppanel[i] >= 'a' && temppanel[i] <= 'z') {
				temppanel[i] = '-';
			}
			temppanel.toString();
			panel = String.valueOf(temppanel);
		}
		return panel;
	}


	/**
	 * procura da letra introduzida pelo utilizador e substituir no painel pela
	 * letra, caso ela exista no segredo
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return painel ---> devolve o painel modificado caso a letra esteja presente
	 *         no segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public void searchChar(char letter) {
		counter = 0;
		char[] temppanel = panel.toCharArray();
		for (int j = 0; j < secret.length(); j++) {
			if (secret.charAt(j) == letter) {
				temppanel[j] = letter;
				counter++;
			}
			temppanel.toString();
			panel = String.valueOf(temppanel);
		}
		
	}

	/**
	 * detecta se uma letra ja foi introduzida e registada no painel
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> quando se detecta uma repetição o contar não fica nulo e
	 *         serve de alerta para saber quando há repetição
	 * @pre: letter != null && 0 < letter.length() < 40
	 * 
	 */

	public boolean repeatedLetter(char letter) {
		char[] temppanel = panel.toCharArray();
		int count = 0;
		for (int u = 0; u < panel.length(); u++) {
			if (temppanel[u] == letter) {
				count++;

			}
		}
		return (count > 0);
	}
	/**
	 * verifica se a letra introduzida pertence ao segredo
	 * 
	 * @param letter ---> letra introduzida pelo utilizador
	 * @return true ---> caso a letra pertença ao segredo
	 * @pre: letter != null && 0 < letter.length() < 40
	 */

	public boolean belongs(String letter) {
		return secret.contains(letter);
	}

	/**
	 * verifica se o palpite foi correto
	 * 
	 * se for, atualiza o painel para ser igual ao segredo
	 * 
	 * @param guess ---> palpite introduzida pelo utilizador para adivinhar o
	 *              segredo por completo
	 * @return true ---> se o palpite foi correto
	 * @pre: guess != null && 0 < guess.length() < 100
	 */
	public boolean isGuessRigth(String guess) {
		boolean state = false;
		if(guess.equals(secret)) {
			state = true;
			panel = secret;
		} else {
			state = false;
		}
		return (state);
	}

	/**
	 * verifica se o segredo já foi descoberto por completo
	 * 
	 * @return true ---> caso o segredo foi totalmente adivinhado
	 */
	public boolean completed() {
		return (panel.equals(secret));
	}

	/**
	 * devolve a variável counter
	 * 
	 * @return counter ---> número de vezes que a letra é repetida no segredo
	 */
	public int getCounter() {
		return counter;
	}

	// devolve o segredo original
	public String getSecret() {
		return secret;
	}

	/**
	 * @return panel ---> Devolve o painel
	 */
	public String returnPanel() {
		return panel;
	}
	
}