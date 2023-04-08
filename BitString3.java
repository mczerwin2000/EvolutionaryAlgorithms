package Main;
import java.util.Random;

public class BitString3 {
	private final int size;
	private boolean[] data;
	private int evaluation;
	
	//Constructor without previous data
	public BitString3(int sizeInput) {
		this.size = sizeInput;
		this.data = generateRandomData(size);
		this.evaluation = 0;
		for(boolean bit : this.data) {
			if(bit) { this.evaluation++; }
		}
	}
	
	public BitString3(BitString3 parent) {
		this.size = parent.size;
		this.data = new boolean[this.size];
		for(int i = 0;i<this.size;i++) {
			this.data[i] = parent.data[i];
		}
		this.evaluation = parent.evaluation;
	}	
	
	//Constructor with previous data
	public BitString3(BitString3 parent, double rate) {
		this.size = parent.size;
		this.data = new boolean[this.size];
		for(int i = 0;i<this.size;i++) {
			this.data[i] = parent.data[i];
		}
		this.evaluation = parent.evaluation;
		mutation(rate);
	}
	
	private void mutation(double rate) {
		Random random = new Random();
		float min = 0;
		float max = 100;
		boolean check = true;
		for(int i = 0;i < size;i++) {
			float chance = min + random.nextFloat() * (max - min);
			if(!(chance > rate)) {
				this.data[i] ^= true;
				check = false;
				if(this.data[i]) {
					this.evaluation++;
				}
				else {
					this.evaluation--;
				}
			}
		}
		if(check) {
			int minID = 0;
			int maxID = this.size - 1;
			int id = minID + random.nextInt((maxID - minID) + 1);
			this.data[id] ^= true;
			if(this.data[id]) {
				this.evaluation++;
			}
			else {
				this.evaluation--;
			}
		}
	}
	
	private boolean[] generateRandomData(int sizeInput) {
		boolean[] dataAns = new boolean[sizeInput];
		Random random = new Random();
		for(int i = 0;i < sizeInput;i++) {
			dataAns[i] = random.nextBoolean();
		}
		return dataAns;
	}
	
	public int evaluateData() {
		int ans = 0;
		for(boolean bit : data) {
			if(bit) { ans++; }
		}
		return ans;
	}
	
	public boolean betterThanDiffData(BitString3 diffData) {
		if(diffData.evaluation - this.evaluation <= 0) {
			return true; //If different data is worse or equal solution 
		}
		else {
			return false; //If different data is better solution
		}
	}
	
	public boolean isFinalSolution() {
		return this.evaluation == this.size ? true : false;
	}
	
	//Test functions
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
	
	public int getSize() {
		return this.size;
	}
	
	public boolean[] getData() {
		return this.data;
	}
	
	public int getEvaluation() {
		return this.evaluation;
	}
}
