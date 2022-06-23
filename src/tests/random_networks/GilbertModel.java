package tests.random_networks;

import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.function.Supplier;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import metrics.centrality.CentralityMetrics;
import metrics.clustering.ClusteringCoefficient;
import model.edge.Mark;
import model.edge.MarkedEdge;
import networks.GilbertRandomModel;
import tests.NetworkWriter;
import tests.PrettyPrint;

public class GilbertModel {
	
	private UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
	
	public GilbertModel(int n, double p, double negativeLinkProbability) {
		
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
		
		GilbertRandomModel<Integer, MarkedEdge> grm = new GilbertRandomModel<>(n, p, nodeFactory, edgeFactory);
		grm.getGraph(graph);
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {
		return graph;
	}
	
	public static void main(String[] args) {
		
		GilbertModel gm = new GilbertModel(300, 0.05, 0.30);
		UndirectedSparseGraph<Integer, MarkedEdge> g = gm.getGraph();
		
		ClusteringCoefficient<Integer, MarkedEdge> cc = new ClusteringCoefficient<>(g);
		System.out.println("Average clustering coefficient >> " + cc.averageClusteringCoeficient());
		Integer node = cc.getNodeWithMaxClusteringCoefficient();
		System.out.println("Cvor sa najvecim koeficijentom klasterisanja >> " + node + " --> " + cc.getClusteringCoefficientForNode(node));
		System.out.println("Diameter >> " + DistanceStatistics.diameter(g));
		
		CentralityMetrics<Integer, MarkedEdge> cm = new CentralityMetrics<>(g);
		System.out.println("Betweenness max >> " + cm.getNodeWithMaxBC() + " --> " + cm.getBCfor(cm.getNodeWithMaxBC()));
		System.out.println("Closeness max >> " + cm.getNodeWithMaxCC() + " --> " + cm.getCCfor(cm.getNodeWithMaxCC()));
		System.out.println("Eigenvector max >> " + cm.getNodeWithMaxEC() + " --> " + cm.getECfor(cm.getNodeWithMaxEC()));
		
		System.out.println("Pet cvorova sa najvecom betweenness >> ");
		for (Entry<Integer, Double> e : cm.getMaxNBC(5).entrySet()) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}
		System.out.println("Pet cvorova sa najvecom closeness >> ");
		for (Entry<Integer, Double> e : cm.getMaxNCC(5).entrySet()) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}
		System.out.println("Pet cvorova sa najvecom eigenvector >> ");
		for (Entry<Integer, Double> e : cm.getMaxNEC(5).entrySet()) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}
		
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/Gilbert.gml");
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
