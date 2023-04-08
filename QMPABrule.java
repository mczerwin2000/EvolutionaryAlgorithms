package Main;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QMPABrule {
	
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final int sizeParent;		//Mju
	private float rate; 			//mutation rate
	private ArrayList<newBitString> parent;
	private Random random;
	private final float A = 2f;
	private final float B = 0.5f;
	private final float threshold = 0.05f;
	private final float upperBound = 0.5f;
	private final float lowerBound;
	
	public QMPABrule(int n, int lambda,int mju) {
		this.size = n;
		this.sizeOffspring = lambda;
		this.sizeParent = mju;
		float tmpSize = n;
		this.rate = 1/tmpSize;
		this.lowerBound = 1/tmpSize;
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
	
	private void doUAR() {
		Collections.reverse(this.parent);
		int evaluation = this.parent.get(this.sizeParent - 1).getEvaluation();
		ArrayList<newBitString> toChoose = new ArrayList<newBitString>();
		//System.out.println("PS: " + this.parent.size());
		//System.out.println("Best: " + this.parent.get(0).getEvaluation());
		//System.out.println("Worst: " + this.parent.get(this.parent.size() - 1).getEvaluation());
		for(int i = this.parent.size() - 1; i > -1; i--) {
			//System.out.println("id: "  + i);
			int currentEv = this.parent.get(i).getEvaluation();
			if(currentEv < evaluation) { 
				this.parent.remove(i);
			}
			else if(currentEv == evaluation) {
				toChoose.add(this.parent.get(i));
				this.parent.remove(i);
			}
			else {
				break;
			}
		}
		if(toChoose.size() > 1) {
			Collections.shuffle(toChoose);
			//System.out.println("CS: " + toChoose.size());
			//System.out.println("PS: " + this.parent.size());
			toChoose = new ArrayList<newBitString>(toChoose.subList(toChoose.size() - (this.sizeParent - this.parent.size()),toChoose.size()));
			//System.out.println("CS: " + toChoose.size());
			toChoose.forEach((el) -> this.parent.add(el));
		}
		else if (toChoose.size() == 1) {
			this.parent.add(toChoose.get(0));
		}
		else {
			System.out.println("Wrong UAR");
		}
		if(this.parent.size() != this.sizeParent) {
			System.out.println("Wrong parent size!!!");
		}
		//System.out.println("PS: " + this.parent.size() + "|" + "Ev: " + this.parent.get(0).getEvaluation());
		Collections.reverse(this.parent);
	}
	
	public int start() {
		int generation = 0;
		while(this.parent.get(parent.size() - 1).getEvaluation() != this.size) {
			generation++;
			ArrayList<newBitString> offspring = new ArrayList<newBitString>();
			float pass = 0f;
			for(int i = 0;i<this.sizeOffspring;i++) {
				int idParent = i % this.sizeParent;
				newBitString candidate = new newBitString(this.size,generatePatch(),this.parent.get(idParent));
				if(candidate.getEvaluation() >= this.parent.get(0).getEvaluation()) {
					candidate.applyPatch(this.parent.get(idParent).getData());
					offspring.add(candidate);
				}
				if(candidate.getEvaluation() >= this.parent.get(idParent).getEvaluation()) {
					pass++;
				}
			}
			for(newBitString el: offspring) {
				this.parent.add(el);
			}
			Collections.sort(this.parent);
			doUAR();
			//this.parent = new ArrayList<newBitString>(this.parent.subList(this.parent.size() - this.sizeParent, this.parent.size()));
//			if(generation % 1000 == 0) {
//				System.out.println("EV: " + this.parent.get(this.sizeParent - 1).getEvaluation());
//			}
			//break;
			if(pass/(float)this.sizeOffspring >= this.threshold) {
				this.rate = Math.min(this.upperBound, this.rate*this.A);
			}
			else {
				this.rate = Math.max(this.lowerBound, this.rate*this.B);
			}
		}
		//System.out.println("Solution in generation: " + generation);
		return generation;
	}
}
