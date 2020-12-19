public class SecretIterator {
	private Secret[] secrets;
	private int maxSecrets;
	private int nextSecret;
	
	public SecretIterator(Secret[] secrets, int maxSecrets) {
		this.secrets = secrets;
		this.maxSecrets = maxSecrets;
		nextSecret = 0;
	}
	
	public boolean hasNext() {
		return nextSecret < maxSecrets;
	}
	
	public Secret next() {
		return secrets[nextSecret++];
	}
}

