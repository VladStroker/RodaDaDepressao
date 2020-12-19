
public class ContestantIterator {
	private Contestant[] contestants;
	private int counter;
	private int nextContestant;
	
	public ContestantIterator(Contestant[] contestants, int counter) {
		this.contestants = contestants;
		this.counter = counter;
		nextContestant = 0;
	}
	
	public boolean hasNext() {
		return nextContestant < counter - 1;
	} 
	
	public Contestant next() {
		return contestants[nextContestant++];
	}
	
	public Contestant currentC() {
		return contestants[nextContestant];
	}
	
	public void resetContestant() {
		nextContestant = 0;
	}
}

