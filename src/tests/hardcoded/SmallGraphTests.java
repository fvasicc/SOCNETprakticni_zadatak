package tests.hardcoded;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.Mark;
import model.MarkedEdge;

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
				System.err.println("Sve komponente u grafu\n=======================");
				System.out.println(ccBFS.getAllComponents());
				System.err.println("Sve koalicije u grafu\n=======================");
				System.out.println(ccBFS.getCoalitions());
				System.err.println("Sve komponente koje nisu koalicije u grafu\n=======================");
				System.out.println(ccBFS.getNonCoalitions());
				System.err.println("Graf komponenti u grafu\n=======================");
				System.out.println(ccBFS.getComponentsGraph());
				System.err.println("Najveca komponenta u grafu\n=======================");
				System.out.println(ccBFS.getGiantComponent());
				System.err.print("Graf je klasteravilan?");
				System.out.println(ccBFS.isClusterable());
				System.err.println("\nNegativni linkovi koje treba izbrisati da graf bude klasterabilan\n=======================");
				try {
					System.out.println(ccBFS.getNegativeLinks());
				} catch (GraphIsClusterableException e) {
					System.out.println(e.getMessage());
				}
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
				System.err.println("Sve komponente u grafu\n=======================");
				System.out.println(ccBFS.getAllComponents());
				System.err.println("Sve koalicije u grafu\n=======================");
				System.out.println(ccBFS.getCoalitions());
				System.err.println("Sve komponente koje nisu koalicije u grafu\n=======================");
				System.out.println(ccBFS.getNonCoalitions());
				System.err.println("Graf komponenti u grafu\n=======================");
				System.out.println(ccBFS.getComponentsGraph());
				System.err.println("Najveca komponenta u grafu\n=======================");
				System.out.println(ccBFS.getGiantComponent());
				System.err.print("Graf je klasteravilan?");
				System.out.println(ccBFS.isClusterable());
				System.err.println("\nNegativni linkovi koje treba izbrisati da graf bude klasterabilan\n=======================");
				try {
					System.out.println(ccBFS.getNegativeLinks());
				} catch (GraphIsClusterableException e) {
					System.out.println(e.getMessage());
				}
				
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

//UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 0, 1);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 2);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 3);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 1, 5);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 4, 5);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 6);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 7);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 8);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 6, 7);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 6, 8);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 7, 8);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 7, 11);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 7, 13);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 9);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 10);
//graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 11);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 9, 10);
//graph.addEdge(new MarkedEdge(Mark.POSITIVE), 11, 12);
//
////graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 2, 3);
//
//UndirectedSparseGraph<Integer, MarkedEdge> graph1 = new UndirectedSparseGraph<>();
//
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 2);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 3);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 4);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 3, 4);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 4, 7);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 6);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 8, 14);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 9, 13);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 9, 15);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 10, 11);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 10, 12);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 11, 12);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 0, 14);
//graph1.addEdge(new MarkedEdge(Mark.POSITIVE), 0, 15);
//
//graph1.addEdge(new MarkedEdge(Mark.NEGATIVE), 0, 3);
//graph1.addEdge(new MarkedEdge(Mark.NEGATIVE), 2, 6);
//graph1.addEdge(new MarkedEdge(Mark.NEGATIVE), 5, 8);
//graph1.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 10);
//
//UndirectedSparseGraph<Integer, MarkedEdge> graph2 = new UndirectedSparseGraph<>();
//
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 13);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 2, 5);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 2, 12);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 3, 4);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 3, 12);
////graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 4, 12); //
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 12);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 6, 7);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 7, 8);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 10, 13);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 11, 16);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 12, 15);
////graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 13, 14); //
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 13, 17);
//
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 9, 10);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 10, 14);
//graph2.addEdge(new MarkedEdge(Mark.POSITIVE), 14, 15);
////graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 10, 15); //
//
////graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 1, 12); //
//graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 0, 6);
//graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 0, 5);
//graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 13);
////graph2.addEdge(new MarkedEdge(Mark.NEGATIVE), 9, 13); //
