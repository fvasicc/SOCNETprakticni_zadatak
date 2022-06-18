package tests.real_networks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.MarkedEdge;
import tests.NetworkReader;
//import tests.NetworkWriter;
import tests.PrettyPrint;

public class WikiTest {
	
	public static String FILE = "res/wiki-RfA.txt";
	
	private static int LINES_FOR_READING = 25000;
	
	public static void main(String[] args) {
		System.out.println(FILE + "\n=================================================");
		ComponentClustererBFS<String, MarkedEdge> ccBFS = null;
		try { 
			UndirectedSparseGraph<String, MarkedEdge> g = NetworkReader.readWiki(FILE, LINES_FOR_READING);
//			new NetworkWriter<String, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "wiki-RfA.gml");
			ccBFS = new ComponentClustererBFS<>(g, MarkedEdge::getMark);
			PrettyPrint<String, MarkedEdge> pp = new PrettyPrint<>();
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
