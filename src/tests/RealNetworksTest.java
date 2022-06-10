package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.Mark;
import model.MarkedEdge;

public class RealNetworksTest {

	public static String FILES[] = { "res/soc-sign-Slashdot081106.txt" };

	public static UndirectedSparseGraph<Integer, MarkedEdge> readFromF(String file)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
			String inLine;
			while ((inLine = in.readLine()) != null) {
				String[] tokens = inLine.split("\t");
				
				Integer node1 = Integer.parseInt(tokens[0].trim());
				Integer node2 = Integer.parseInt(tokens[1].trim());
				Mark mark = Mark.getMark(Integer.parseInt(tokens[2].trim()));

				MarkedEdge checkPotentialEdge = graph.findEdge(node1, node2);

				if (checkPotentialEdge != null) {
					if (checkPotentialEdge.getMark() == Mark.POSITIVE && mark == Mark.NEGATIVE)
						checkPotentialEdge.setNewMark(Mark.NEGATIVE);
				} else {
					graph.addEdge(new MarkedEdge(mark), node1, node2);
				}

			}
			return graph;
		}
	}

	public static void main(String[] args) {
		for (String file : FILES) {
			System.out.println(file + "\n=================================================");
			ComponentClustererBFS<Integer, MarkedEdge> ccBFS = null;
			try {
				ccBFS = new ComponentClustererBFS<>(readFromF(file), MarkedEdge::getMark);
				Scanner sc = new Scanner(System.in);
				int n = sc.nextInt();
				while (n != 0) {
					if (n == 1) {
						System.err.println("Sve komponente u grafu\n=======================");
						System.out.println(ccBFS.getAllComponents());
					}
					if (n == 2) {
						System.err.println("Sve koalicije u grafu\n=======================");
						System.out.println(ccBFS.getCoalitions());
					}
					if (n == 3) {
						System.err.println("Sve komponente koje nisu koalicije u grafu\n=======================");
						System.out.println(ccBFS.getNonCoalitions());
					}
					if (n == 4) {
						System.err.println("Graf komponenti u grafu\n=======================");
						System.out.println(ccBFS.getComponentsGraph());
					}
					if (n == 5) {
						System.err.println("Najveca komponenta u grafu\n=======================");
						System.out.println(ccBFS.getGiantComponent());
					}
					if (n == 6) {
						System.err.print("Graf je klasteravilan?");
						System.out.println(ccBFS.isClusterable());
					}
					if (n == 7) {
						System.err.println(
								"\nNegativni linkovi koje treba izbrisati da graf bude klasterabilan\n=======================");
						try {
							System.out.println(ccBFS.getNegativeLinks());
							System.out.println(ccBFS.getNegativeLinks().size());
						} catch (GraphIsClusterableException e) {
							System.out.println(e.getMessage());
						}
					}
					if (n == 8) {
						System.out.println(ccBFS.getAllComponents().size());
					}
					if (n == 9) {
						System.out.println(ccBFS.getGiantComponent().getVertexCount());
					}
					if (n == 10) {
						System.out.println(ccBFS.getCoalitions().size());
					}
					n = sc.nextInt();
				}
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
