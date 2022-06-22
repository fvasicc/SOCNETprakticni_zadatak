package tests.random_networks;

import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.edge.Mark;
import model.edge.MarkedEdge;
import networks.WattsStrogatzRandomModel;
import tests.NetworkWriter;
import tests.PrettyPrint;

public class WattsStrogatzModel {
	
	private UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
	
	public WattsStrogatzModel(int n, int k, double p, double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0.0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("Parameter negativeLinkProbability -> Must be positive!");
		
		Supplier<Integer> nodeFactory = new Supplier<Integer>() {
			private int i = 0;
			@Override
			public Integer get() {
				return i++;
			}
		};
		
		Supplier<MarkedEdge> edgeFactory = new Supplier<MarkedEdge>() {
			@Override
			public MarkedEdge get() {
				Random rnd = new Random();
				return new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE);
			}
		};
		
		WattsStrogatzRandomModel<Integer, MarkedEdge> ws = new WattsStrogatzRandomModel<>(n, k, p, nodeFactory, edgeFactory);
		ws.getGraph(graph);
	}

	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {	
		return graph;
	}
	
	public static void main(String[] args) {
		UndirectedSparseGraph<Integer, MarkedEdge> g = new WattsStrogatzModel(250, 4, 0.15, 0.5).getGraph();
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/WattsStrogatz.gml");
		ComponentClustererBFS<Integer, MarkedEdge >ccBFS = new ComponentClustererBFS<>(g, MarkedEdge::getMark);
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
	}
}
