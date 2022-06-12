package tests.real_networks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import clusterability.ComponentClustererBFS;
import exceptions.GraphIsClusterableException;
import model.MarkedEdge;
import model.PrettyPrint;
import tests.NetworkReader;

public class EpinionsAndSlashdotTest {

	public static String FILES[] = { "res/soc-sign-Slashdot081106.txt", "res/soc-sign-epinions.txt"};
	
	private static int LINES_FOR_READING = 25000;

	public static void main(String[] args) {
		for (String file : FILES) {
			System.out.println(file + "\n=================================================");
			ComponentClustererBFS<Integer, MarkedEdge> ccBFS = null;
			try { 
				ccBFS = new ComponentClustererBFS<>(NetworkReader.readEpinionsOrSlashdot(file, LINES_FOR_READING), MarkedEdge::getMark);
				PrettyPrint<Integer, MarkedEdge> pp = new PrettyPrint<>();
				pp.printMenu();
				System.out.println("Za kraj unesi 0");
				Scanner sc = new Scanner(System.in);
				System.out.print("Unesi izbor >> ");
				int in = sc.nextInt();
				while (in != 0) {
					try {
						pp.getResultByChoice(in, ccBFS);
					} catch (GraphIsClusterableException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
					System.out.print("Unesi izbor >> ");
					in = sc.nextInt();
				} ;
				
				
				sc.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
