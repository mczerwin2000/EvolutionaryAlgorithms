package Main;

import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File to test EA
 * @author Marcin Czerwinski
 */
public class Main {
	public static void main(final String[] args) throws IOException {
		final int n = 10000; //size of problem
		String lineSeparator = System.getProperty("line.separator");
		FileOutputStream saveSolution = new FileOutputStream("ExampleFileSOL.txt",true);
		FileOutputStream saveRSD = new FileOutputStream("ExampleFileRSD.txt",true);
		final int noTest = 100;
		final int[] lambdaArray = {1,5,10,50,100,200,400,800,1600,3200};
		final int[] mjuArray = {1,5,10,50,100};
		float mR = 1f / 10000f;
		for(int y = 0;y< mjuArray.length;y++) {
			System.out.println("TEST MJU = " + mjuArray[y]);
			String sol = mjuArray[y] + ";";
			String rsd = mjuArray[y] + ";";
			saveRSD.write(10);
			saveSolution.write(10);
			for(int i = 0;i<lambdaArray.length;i++) {
				System.out.println("Test Lambda = " + lambdaArray[i]);
				if(lambdaArray[i] >= mjuArray[y]) {
					int[] ans1 = new int[100];
					for(int z = 0;z<noTest;z++) {
						QMPABrule test = new QMPABrule(10000,lambdaArray[i],mjuArray[y]);
						//QMP2rate test = new QMP2rate(10000,lambdaArray[i],mjuArray[y]);
						//QMPStatic test = new QMPStatic(10000,lambdaArray[i],mjuArray[y],mR);
						//System.out.println("Tz: " + z);
						ans1[z] = test.start();
					}
					double av1 = Arrays.stream(ans1).sum()/100;
					double rsd1 = rsd(ans1,av1);
					sol += av1 + ";";
					rsd += rsd1 + ";";
				}
				else {
					sol += "---;";
					rsd += "---;";
				}
			}
			byte[] byteSol = sol.getBytes();
			byte[] byteRSD = rsd.getBytes();
			saveSolution.write(byteSol);
			saveRSD.write(byteRSD);
			System.out.println("END");
		}
		System.out.println("END END");
	}
	
	private static double rsd(int[] data,double average) {
		double ans = 0;
		double sd = 0;
		for(int el: data) {
			double tmpEl = el;
			sd += (tmpEl - average)*(tmpEl - average);
		}
		sd = Math.sqrt(sd/(data.length - 1));
		ans = (sd/average)*100;
		return ans;
	}
}
