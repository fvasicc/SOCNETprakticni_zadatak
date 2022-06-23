package tests.random_networks;

import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

import clusterability.ClusteringCoefficient;
import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.edge.Mark;
import model.edge.MarkedEdge;
import networks.BarabsiAlbertRandomModel;
import smallworld.SmallWorldCoefficent;
import tests.NetworkWriter;
import tests.PrettyPrint;

public class BarabasiAlbertModel {

	private UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
	
	public BarabasiAlbertModel(int n, int m0, int e0, int m, double negativeLinkProbability, double initialGraphNegLinkP) {
		
		if (negativeLinkProbability < 0.0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("Parameter negativeLinkProbability -> Must be positive!");
		
		if (initialGraphNegLinkP < 0.0 || initialGraphNegLinkP > 1.0)
			throw new IllegalArgumentException("Parameter initialGraphNegLinkP -> Must be positive!");
		
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
		
		Supplier<MarkedEdge> edgeFactoryInitialGraph = new Supplier<MarkedEdge>() {
			@Override
			public MarkedEdge get() {
				Random rnd = new Random();
				return new MarkedEdge(rnd.nextDouble() < initialGraphNegLinkP ? Mark.NEGATIVE : Mark.POSITIVE);
			}
		};
		
		BarabsiAlbertRandomModel<Integer, MarkedEdge> ba = new BarabsiAlbertRandomModel<>(n, m0, e0, m, nodeFactory, edgeFactory, edgeFactoryInitialGraph);
		ba.getGraph(graph);
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {
		return graph;
	}
	
	public static void main(String[] args) throws GraphIsClusterableException {

		UndirectedSparseGraph<Integer, MarkedEdge> g = new BarabasiAlbertModel(250, 10, 25, 1, 0.15, 0.05).getGraph();
		
		ClusteringCoefficient<Integer, MarkedEdge> cc = new ClusteringCoefficient<>(g);
		System.out.println("Average clustering coefficient >> " + cc.averageClusteringCoeficient());
		Integer node = cc.getNodeWithMaxClusteringCoefficient();
		System.out.println("Cvor sa najvecim cc >> " + node + " --> " + cc.getClusteringCoefficientForNode(node));
		System.out.println("Diameter >> " + DistanceStatistics.diameter(g));
		SmallWorldCoefficent<Integer, MarkedEdge> swc = new SmallWorldCoefficent<>(g);
		System.out.println("Small-world coefficient >> " + swc.getSmallWorldCoeff());
		System.out.println("Network efficient" + swc.getNetworkEfficent());
		
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/BarabasiAlbert.gml");
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
