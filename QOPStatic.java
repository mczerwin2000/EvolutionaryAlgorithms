package Main;
import java.util.ArrayList;
import java.util.Random;

/**
 * Quick One Parent evolutionary algorithm with Static mutation rate
 * @author Marcin Czerwinski
 */

public class QOPStatic {
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final float rate; 			//mutation rate
	private boolean[] parent;
	private int parentEvaluation;
	private Random random;
	
	public QOPStatic(int n, int lambda, float rateInput) {
		this.size = n;
		this.sizeOffspring = lambda;
		this.rate = rateInput;
		this.random = new Random();
		this.parentEvaluation = 0;
		this.parent = new boolean[n];
		//Generate initial random parent
		for(int i = 0;i < n;i++) {
			boolean nextBoolean = random.nextBoolean();
			parent[i] = nextBoolean;
			if(nextBoolean) { parentEvaluation++; }
		}
	}
	
	/**
	 * To calculate offspring evaluation based only on patch
	 * @param offspringPatch
	 * @param parent - this.parent
	 * @param parentFitness - this.parentEvaluation
	 * @return evaluation
	 */
	private int evaluateOffspring(ArrayList<Integer> offspringPatch,boolean[] parent, int parentFitness) {
		int evaluation = parentFitness;
		for(int id : offspringPatch) {
			if(parent[id] == true) {
				evaluation--;
			}
			else {
				evaluation++;
			}
		}
		return evaluation;
	}
	
	/**
	 * Apply patch to parent and return a new parent
	 * @param offspringPatch
	 * @param parent - this.parent
	 * @return a new parent
	 */
	private boolean[] applyPatch(ArrayList<Integer> offspringPatch,boolean[] parent) {
		for(int id : offspringPatch) {
			parent[id] ^= true;
		}
		return parent;
	}
	
	/**
	 * Generates a new offspring
	 * It gets all id which will be mutated to one ArrayList - patch
	 * @return offspring
	 */
	private ArrayList<Integer> generatePatch() {
		ArrayList<Integer> patch = new ArrayList<Integer>();
		int i = -1;
		boolean check = true; //For shift mutation
		while(true) {
			double r;
			do {
				r = random.nextFloat();
			}
			while(r == 0); //Logarithm value must be greater than 0
			int mutation = (int)(Math.log(r)/Math.log(1-this.rate));
			i += 1 + mutation;
			if(i >= this.size) {break;}
			check = false;
			patch.add(i);
		}
		//Shift mutation
		if(check) {
			int maxID = this.size - 1;
			int id = random.nextInt(maxID + 1);
			patch.add(id);
		}
		return patch;
	}
	
	/**
	 * Function which calculate the OneMax Problem with static mutation rate
	 */
	public void start() {
		int generation = 0;
		while(this.parentEvaluation != this.size) {
			generation++;
			ArrayList<ArrayList<Integer>> bestPatches = new ArrayList<ArrayList<Integer>>(); // for u.a.r
			int bestFitness = this.parentEvaluation;
			for(int i = 0;i<this.sizeOffspring;i++) {
				ArrayList<Integer> patch = generatePatch();
				int patchEvaluation =  evaluateOffspring(patch,this.parent,this.parentEvaluation);
				if(patchEvaluation == bestFitness) {
					bestPatches.add(patch);
				}
				else if(patchEvaluation > bestFitness) {
					bestPatches = new ArrayList<ArrayList<Integer>>();
					bestPatches.add(patch);
					bestFitness = patchEvaluation;
				}
			}
			if(bestPatches.size() > 0) {
				this.parentEvaluation = bestFitness;
				int maxID = bestPatches.size() - 1;
				int id = random.nextInt(maxID + 1); // u.a.r.
				this.parent = applyPatch(bestPatches.get(id),this.parent);
			}
		}
		System.out.println("Solution in generation: " + generation);
	}
}
