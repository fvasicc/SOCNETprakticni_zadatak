package tests.random_networks;

import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.Mark;
import model.MarkedEdge;
import model.PrettyPrint;
import networks.ErdosRenyiRandomModel;
import tests.NetworkWriter;

public class ErdosRenyiModel {
	
	private UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
	
	public ErdosRenyiModel(int nodes, int edges, double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0.0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("");
		
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
		
		ErdosRenyiRandomModel<Integer, MarkedEdge> er = new ErdosRenyiRandomModel<>(nodes, edges, nodeFactory, edgeFactory);
		er.getGraph(graph);
		
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {		
		return graph;
	}
	
	public static void main(String[] args) {
		ErdosRenyiModel er = new ErdosRenyiModel(250, 500, 0.5);
		UndirectedSparseGraph<Integer, MarkedEdge> g = er.getGraph();
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/ErdosRenyi.gml");
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
