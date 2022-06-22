package tests.hardcoded;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.edge.Mark;
import model.edge.MarkedEdge;
import tests.PrettyPrint;

public class SmallGraphTests {
	
	private static String FILES_TRUE[] = {
											"res/clustered1.txt", 
											"res/clustered2.txt"
										  };
	private static String FILES_FALSE[] = {
											"res/nonclustered1.txt", 
											"res/nonclustered2.txt", 
											"res/nonclustered3.txt",
											"res/nonclustered4.txt" 
										  };
	
	public static UndirectedSparseGraph<Integer, MarkedEdge> readFromF(String file) throws IllegalArgumentException, FileNotFoundException, IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			in.readLine(); in.readLine(); in.readLine(); in.readLine();
			UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
			String inLine;
			while ((inLine = in.readLine()) != null) {
				String[] tokens = inLine.split("\t");
				graph.addEdge(
								new MarkedEdge(Mark.getMark(Integer.parseInt(tokens[2].trim()))), 
								Integer.parseInt(tokens[0].trim()), 
								Integer.parseInt(tokens[1].trim())
							);
			}
			return graph;
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println("KLASTERABILNI GRAFOVI\n=================================================");
		
		for (String file : FILES_TRUE) {
			System.out.println(file + "\n=================================================");
			ComponentClustererBFS<Integer, MarkedEdge> ccBFS = null;
			try {
				ccBFS = new ComponentClustererBFS<>(readFromF(file), MarkedEdge::getMark);
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
		
		System.out.println("NEKLASTERABILNI GRAFOVI\n=================================================");
		
		for (String file : FILES_FALSE) {
			System.out.println(file + "\n=================================================");
			ComponentClustererBFS<Integer, MarkedEdge> ccBFS = null;
			try {
				ccBFS = new ComponentClustererBFS<>(readFromF(file), MarkedEdge::getMark);
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

