package Main;
import java.util.Random;

/**
 * Class where is saved BitString
 * Every operation on BitString is here
 * @author Marcin Czerwinski
 */
public class BitString {
	private final int size;	//size of problem
	private boolean[] data;	// data about bitString
	private Random random;	//
	
	/**
	 * Constructor for initial parent
	 * @param sizeInput - size of problem
	 * @param rd - random number generator
	 */
	public BitString(int sizeInput, Random rd) {
		this.size = sizeInput;
		this.random = rd;
		this.data = generateRandomData(size);
	}
	
	/**
	 * Constructor for generating offspring
	 * @param parent - a parent which is based to generate a offspring bitString
	 * @param rate - parameter for mutation chance
	 */
	public BitString(BitString parent, float rate) {
		this.size = parent.size;
		this.random = parent.random;
		this.data = new boolean[this.size];
		for(int i = 0;i<this.size;i++) {
			this.data[i] = parent.data[i];
		}
		mutation(rate);
	}
	
	private void mutation(float rate) {
		boolean check = true; // needed for shift mutation
		for(int i = 0;i < size;i++) {
			float chance = random.nextFloat() * 100;
			if(!(chance > rate)) {
				this.data[i] ^= true;
				check = false;
			}
		}
		//Shift mutation
		if(check) {
			int maxID = this.size - 1;
			int id = random.nextInt(maxID + 1);
			this.data[id] ^= true;
		}
	}
	
	/**
	 * Generator random data for initial parent
	 */
	private boolean[] generateRandomData(int sizeInput) {
		boolean[] dataAns = new boolean[sizeInput];
		for(int i = 0;i < sizeInput;i++) {
			dataAns[i] = random.nextBoolean();
		}
		return dataAns;
	}
	
	/**
	 * Evaluate BitString, needed to compare offspring to parent
	 * @return sum of digits
	 */
	public int evaluateData() {
		int ans = 0;
		for(boolean bit : data) {
			if(bit) { ans++; }
		}
		return ans;
	}
	
	/**
	 * Function to compare offspring to parent
	 * @param diffData - a parent
	 * @return true if offspring is equal or better than parent
	 */
	public boolean betterThanDiffData(BitString diffData) {
		if(diffData.evaluateData() - this.evaluateData() <= 0) {
			return true; //If different data is worse or equal solution 
		}
		else {
			return false; //If different data is better solution
		}
	}
	
	/**
	 * Function to check that OneMax Problem is solved
	 */
	public boolean isFinalSolution() {
		for(boolean bit : this.data) {
			if(!bit) { return false; }
		}
		return true;
	}
	
	/**
	 * Function only for test purpose
	 * Return BitString data as string
	 */
	public String toString() {
		String ans = "";
		for(boolean bit : data) {
			if(bit) {
				ans += "1";
			}
			else {
				ans += "0";
			}
		}
		return ans;
	}
}
