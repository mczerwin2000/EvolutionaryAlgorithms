package Main;

import java.util.ArrayList;
import java.util.Random;

public class newBitString implements Comparable<newBitString>{
	private final int size;
	private final ArrayList<Integer> patch;
	private final int evaluation;
	private boolean whichRate;
	public boolean[] data = null;
	
	public newBitString(int n, Random random) {
		this.size = n;
		this.patch = null;
		int tmpEvaluation = 0;
		boolean[] tmpParent = new boolean[n];
		for(int z = 0;z < n;z++) {
			boolean nextBoolean = random.nextBoolean();
			tmpParent[z] = nextBoolean;
			if(nextBoolean) { tmpEvaluation++; }
		}
		this.evaluation = tmpEvaluation;
		this.data = tmpParent;
	}
	
	public newBitString(int n, ArrayList<Integer> patcha,newBitString parent) {
		this.size = n;
		this.patch = new ArrayList<Integer>(patcha);
		int tmpEvaluation = parent.getEvaluation();
		boolean[] tmpData = parent.getData();
		for(int el: patcha) {
			if(tmpData[el]) {
				tmpEvaluation--;
			}
			else {
				tmpEvaluation++;
			}
		}
		this.evaluation = tmpEvaluation;
	}
	
	public newBitString(int n, ArrayList<Integer> patcha,newBitString parent, boolean whichRate) {
		this(n,patcha,parent);
		this.whichRate = whichRate;
	}
	
	public void applyPatch(boolean[] parent) {
		this.data = parent.clone();
		for(int el: this.patch) {
			data[el] ^= true;
		}
	}
	
	public boolean getRate() {
		return this.whichRate;
	}
	
	public boolean[] getData() {
		return this.data;
	}
	
	public int getEvaluation() {
		return this.evaluation;
	}
	
	@Override
	public int compareTo(newBitString diff) {
		return this.evaluation - diff.evaluation;
	}
}