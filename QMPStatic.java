package Main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QMPStatic {
	
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final int sizeParent;		//Mju
	private final float rate; 			//mutation rate
	private ArrayList<newBitString> parent;
	private Random random;
	
	public QMPStatic(int n, int lambda,int mju, float rateInput) {
		this.size = n;
		this.sizeOffspring = lambda;
		this.sizeParent = mju;
		this.rate = rateInput;
		this.random = new Random();
		this.parent = new ArrayList<newBitString>();
		for(int i = 0;i<mju;i++) {
			this.parent.add(new newBitString(n,this.random));
		}
		Collections.sort(parent);
	}
	
	public void testConstructor() {
		System.out.println("Size: " + this.parent.size());

		for(newBitString el: this.parent) {
			System.out.println(el.getEvaluation());
		}
	}
	
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
	
	public void start() {
		int generation = 0;
		while(this.parent.get(parent.size() - 1).getEvaluation() != this.size) {
			generation++;
			ArrayList<newBitString> offspring = new ArrayList<newBitString>();
			for(int i = 0;i<this.sizeOffspring;i++) {
				int idParent = i % this.sizeParent;
				newBitString candidate = new newBitString(this.size,generatePatch(),this.parent.get(idParent));
				if(candidate.getEvaluation() >= this.parent.get(0).getEvaluation()) {
					candidate.applyPatch(this.parent.get(idParent).getData());
					offspring.add(candidate);
				}
			}
			for(newBitString el: offspring) {
				this.parent.add(el);
			}
			Collections.sort(this.parent);
			this.parent = new ArrayList<newBitString>(this.parent.subList(this.parent.size() - this.sizeParent, this.parent.size()));
			if(generation % 1000 == 0) {
				System.out.println("EV: " + this.parent.get(this.sizeParent - 1).getEvaluation());
			}
		}
		System.out.println("Solution in generation: " + generation);
	}
}
