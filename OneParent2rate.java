package Main;

import java.util.Random;

/**
 * A class for testing 2-rate method
 * Lower bound = 1/n
 * Upper bound = 50%
 * @author Marcin Czerwinski
 */
public class OneParent2rate {
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private float rate = 2;				// initial mutation rate
	private BitString parent;
	private BitString[] offspring;
	
	public OneParent2rate(int sizeInput, int sizeOffspringInput) {
		size = sizeInput;
		sizeOffspring = sizeOffspringInput;
		parent = new BitString(size, new Random()); // initial parent
		offspring = new BitString[sizeOffspring];
	}
	
	/**
	 * Function which calc and return a mutation rate for a chosen group
	 * @param whichGroup - True is first group with smaller rate
	 * 					 - False is second group with greater rate
	 * @return a mutation rate for chosen group
	 */
	private float calcRate(boolean whichGroup) {
		float tmpSize = size;
		return whichGroup ? rate / (tmpSize*2) : (2*rate) / tmpSize;
	}
	
	/**
	 * Generator of offspring
	 * Divide for 2 subpopulation with 2 different mutation rate 
	 * @return new offspring
	 */
	private BitString[] generateOffspring() {
		BitString[] offspringAns = new BitString[sizeOffspring];
		int halfOffspring = sizeOffspring / 2;
		float rateGroup = calcRate(true) * 100; // mutation rate for first group
		//First group
		for(int i = 0;i < halfOffspring;i++) {
			offspringAns[i] = new BitString(parent,rateGroup);
		}
		//Second group
		rateGroup = calcRate(false) * 100;		//mutation rate for second group
		for(int i = halfOffspring;i < sizeOffspring;i++) {
			offspringAns[i] = new BitString(parent,rateGroup);
		}
		return offspringAns;
	}
	
	/**
	 * Function which calculate the OneMax Problem with 2-rate method
	 */
	public void start() {
		int generation = 0;
		do {
			generation++;
			offspring = generateOffspring();
			boolean betterOffspring = false;
			boolean whichGroup = false;
			for(int i = 0;i < sizeOffspring;i++) {
				if(offspring[i].betterThanDiffData(parent)) {
					betterOffspring = true; // check there is a better offspring
					parent = offspring[i];
					if(i < sizeOffspring/2) {
						whichGroup = true;	//if parent is from first group
					}
					else {
						whichGroup = false;	//if parent is from second group
					}
				}
			}
			float rateCandidate; //variable for new mutation rate
			if(betterOffspring) {
				rateCandidate = calcRate(whichGroup) * 100; //changing mutation rate based on new parent
			}
			//If there is no better parent - draw a random group
			// and changing of muation rate
			else {
				if((new Random().nextBoolean())) {
					rateCandidate = calcRate(true) * 100;
				}
				else {
					rateCandidate = calcRate(false) * 100;
				}
			}
			float tmpSize = size;
			rate = Math.min(Math.max(rateCandidate,2),tmpSize/4); //check for lower and upper bound
		}
		while(!parent.isFinalSolution());
		System.out.println("Solution in generation: " + generation);
	}
}
