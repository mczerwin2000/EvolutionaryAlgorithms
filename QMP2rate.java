package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QMP2rate {
	private final int size;				//n
	private final int sizeOffspring;	//lambda
	private final int sizeParent;		//Mju
	private float rate; 			//mutation rate
	private ArrayList<newBitString> parent;
	private Random random;
	private final float upperBound = 0.25f;
	private final float lowerBound;
	
	private final int offspringForParent;
	
	public QMP2rate(int n, int lambda,int mju) {
		this.size = n;
		this.sizeOffspring = lambda;
		this.sizeParent = mju;
		float tmpSize = n;
		this.rate = 2/tmpSize;
		//this.lowerBound = 2/tmpSize;
		this.lowerBound = 2/(tmpSize*tmpSize);
		this.random = new Random();
		this.parent = new ArrayList<newBitString>();
		for(int i = 0;i<mju;i++) {
			this.parent.add(new newBitString(n,this.random));
		}
		this.offspringForParent = this.sizeOffspring / this.sizeParent;
		Collections.sort(parent);
	}
	
	public void testConstructor() {
		System.out.println("Size: " + this.parent.size());

		for(newBitString el: this.parent) {
			System.out.println(el.getEvaluation());
		}
	}
	
	private ArrayList<Integer> generatePatch(float rate) {
		ArrayList<Integer> patch = new ArrayList<Integer>();
		int i = -1;
		boolean check = true; //For shift mutation
		while(true) {
			double r;
			do {
				r = random.nextFloat();
			}
			while(r == 0); //Logarithm value must be greater than 0
			double forLog = 1d-(double)rate;
			int mutation = (int)(Math.log(r)/Math.log(forLog));
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
	
	private int returnParent(int idOffspring) {
		int parentID = -1;
		for(int i = 0;i < this.sizeParent;i++) {
			if(idOffspring < this.offspringForParent * (i+1)) {
				parentID = i;
				break;
			}
		}
		return parentID;
	}
	
	public int start() {
		int generation = 0;
		int mutationChange = this.offspringForParent/2;
		if(mutationChange == 0) mutationChange++;
		while(this.parent.get(parent.size() - 1).getEvaluation() != this.size) {
			generation++;
			ArrayList<newBitString> offspring = new ArrayList<newBitString>();
			boolean bestChildRate = false;
			int bestChildEvaluation = 0;
			boolean rateType = false;
			for(int i = 0;i<this.sizeOffspring;i++) {
				if(i % mutationChange == 0)
					rateType ^= true;
				float tmpRate = rateType == true ? this.rate/2 : this.rate*2; 
				int idParent = returnParent(i);
//				System.out.println("Generation: " + generation);
//				System.out.println("WhichParent: " + idParent);
//				System.out.println("RateType: " + rateType);
				newBitString candidate = new newBitString(this.size,generatePatch(tmpRate),this.parent.get(idParent),rateType);
				if(candidate.getEvaluation() >= this.parent.get(0).getEvaluation()) {
					candidate.applyPatch(this.parent.get(idParent).getData());
					offspring.add(candidate);
				}
				if(candidate.getEvaluation() >= bestChildEvaluation) {
					bestChildEvaluation = candidate.getEvaluation();
					bestChildRate = rateType;
				}
			}
			for(newBitString el: offspring) {
				this.parent.add(el);
			}
			Collections.sort(this.parent);
			doUAR();
			float newRate = this.rate;
			if(random.nextBoolean()) {
				if(bestChildRate)
					newRate /= 2;
				else
					newRate *= 2;
			}
			else {
				if(random.nextBoolean())
					newRate /= 2;
				else
					newRate *= 2;
			}
			this.rate = Math.min(Math.max(newRate, this.lowerBound),this.upperBound);
//			if(generation % 10000 == 0) {
//				System.out.println("EV: " + this.parent.get(this.sizeParent - 1).getEvaluation());
//				System.out.println("MR: " + this.rate);
//			}
//			if(generation % 1 == 0) {
//				break;
//				//System.out.println("EV: " + this.parent.get(this.sizeParent - 1).getEvaluation());
//			}
		}
		//System.out.println("Solution in generation: " + generation);
		return generation;
	}
}
