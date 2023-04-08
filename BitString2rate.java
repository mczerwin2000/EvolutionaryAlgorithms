package Main;

import java.util.ArrayList;
import java.util.Random;

public class BitString2rate extends newBitString {
	private final boolean whichGroup;
	
	public BitString2rate(int n, Random random) {
		super(n,random);
		this.whichGroup = random.nextBoolean();
	}
	
	public BitString2rate(int n, ArrayList<Integer> patcha,newBitString parent,boolean groupID) {
		super(n,patcha,parent);
		this.whichGroup = groupID;
	}

	public boolean getGroup() {
		return whichGroup;
	}
}
