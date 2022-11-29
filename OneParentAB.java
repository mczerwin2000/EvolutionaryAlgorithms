package Main;
import java.util.Random;

/**
 * A class for testing AB rule method
 * Lower bound = 1/n
 * Upper bound = 50%
 * Threshold = 5%
 * @author Marcin Czerwinski
 */
public class OneParentAB {
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final float A = 2;
	private final float B = 0.5f;
	private final float pass = 0.05f;	//threshold
	private float rate;					//mutation rate
	private BitString parent;
	private BitString[] offspring;
	
	public OneParentAB(int sizeInput, int sizeOffspringInput) {
		size = sizeInput;
		sizeOffspring = sizeOffspringInput;
		float tmpSize = sizeInput;
		rate = 1/tmpSize * 100;						//initial rate
		parent = new BitString(size, new Random());	//initial parent
		offspring = new BitString[sizeOffspring];
	}
	
	/**
	 * Generator offspring based on current parent and rate
	 * @return new offspring
	 */
	private BitString[] generateOffspring() {
		BitString[] offspringAns = new BitString[sizeOffspring];
		for(int i = 0;i < sizeOffspring;i++) {
			offspringAns[i] = new BitString(parent,rate);
		}
		return offspringAns;
	}
	
	/**
	 * Function which compare offspring to parent and gives a percentage result how many offspring is better
	 * @return a score for a threshold
	 */
	private float evaluateAllOffspringToParent() {
		float ans = 0;
		for(int i = 0;i<sizeOffspring;i++) {
			if(offspring[i].betterThanDiffData(parent)) { ans++; }
		}
		return ans/sizeOffspring;
	}
	
	/**
	 * Function which calculate the OneMax Problem with AB rule method
	 */
	public void start() {
		int generation = 0;
		do {
			generation++;
			offspring = generateOffspring();
			if(evaluateAllOffspringToParent() >= pass) {
				rate = Math.min(50, rate*A); //Check for upper bound
			}
			else {
				float tmpSize = size;
				rate = Math.max(1/tmpSize * 100, rate*B); //Check for lower bound
			}
			for(int i = 0;i < sizeOffspring;i++) {
				if(offspring[i].betterThanDiffData(parent)) {
					parent = offspring[i];
				}
			}
		}
		while(!parent.isFinalSolution());
		System.out.println("Solution in generation: " + generation);
	}
}
