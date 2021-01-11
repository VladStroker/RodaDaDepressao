/**
 * 
 * @authors Ricardo e Vladyslav
 *
 */


public class SecretIterator {
	// Variaveis
	private Secret[] secrets;
	private int maxSecrets;
	private int nextSecret;
	
	/**
	 *  Construtor
	 * @param secrets ---> array de segredos
	 * @param maxSecrets ---> numero maximo de segredos/ numero de rondas no jogo
	 * @pre maxSecrets > 0 
	 */
	public SecretIterator(Secret[] secrets, int maxSecrets) {
		this.secrets = secrets;
		this.maxSecrets = maxSecrets;
		nextSecret = 0;
	}
	
	/**
	 *  Verifica se existe um proximo segredo no iterador
	 * @return ---> verdadeira caso exista um próximo segredo
	 */
	public boolean hasNext() {
		return nextSecret < maxSecrets;
	}
	
	/**
	 *  
	 * @return ---> Devolve o segredo da atual
	 */
	public Secret currentS() {
		return secrets[nextSecret];
	}
	
	/** 
	 * @return ---> Devolve o segredo da ronda anterior
	 */
	public Secret previousS() {
		return secrets[nextSecret-1];
	}
	
	/** Avança para o proximo segredo do iterador
	 * 
	 * @pre hasNext()
	 */
	public Secret next() {
		return secrets[nextSecret++];
	}
}