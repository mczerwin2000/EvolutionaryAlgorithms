package Main;

/**
 * File to test EA
 * @author Marcin Czerwinski
 */
public class Main {
	public static void main(final String[] args) {
		final int n = 10000; //size of problem
		final int lambda = 10; //size of offspring population
		System.out.println("START");
		for(int i = 0;i<100;i++) {
//			OneParent2rate simulationTwo = new OneParent2rate(n,lambda);
//			simulationTwo.start();
			OneParentAB simulationAB = new OneParentAB(n,lambda);
			simulationAB.start();
//			float tmpSize = n;
//			OneParentStatic simulationStatic = new OneParentStatic(n,lambda,100/tmpSize);
//			simulationStatic.start();
		}
		System.out.println("END");
	}
}
