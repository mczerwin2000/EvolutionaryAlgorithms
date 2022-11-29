package Main;
import java.util.Random;

/**
 * A class for testing static mutation rate
 * @author Marcin Czerwinski
 */
public class OneParentStatic {
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final float rate; 			//mutation rate
	private BitString parent;			
	private BitString[] offspring;
	
	public OneParentStatic(int sizeInput, int sizeOffspringInput,float rateInput) {
		size = sizeInput;
		sizeOffspring = sizeOffspringInput;
		rate = rateInput;
		parent = new BitString(size, new Random()); // initial parent
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
	 * Function which calculate the OneMax Problem with static mutation rate
	 */
	public void start() {
		int generation = 0;
		do {
			generation++;
			offspring = generateOffspring();
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
