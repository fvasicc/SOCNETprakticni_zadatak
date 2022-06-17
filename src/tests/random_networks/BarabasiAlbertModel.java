package tests.random_networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.Mark;
import model.MarkedEdge;
import model.PrettyPrint;
import networks.BarabsiAlbertRandomModel;
import tests.NetworkWriter;

public class BarabasiAlbertModel {
	
	private int nodesNumber;
	private int m0;
	private int m;
	
	private UndirectedSparseGraph<Integer, MarkedEdge> graph;
	
	public BarabasiAlbertModel(int n, int m0, int e0, int m, double negativeLinkProbability) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("");
		
		if (e0 < 0) 
			throw new IllegalArgumentException("");
		
		if (m0 > n) 
			throw new IllegalArgumentException("");
		
		if (m >= m0) 
			throw new IllegalArgumentException("");
		
		if (negativeLinkProbability < 0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("");
		
		this.nodesNumber = n;
		this.m0 = m0;
		this.m = m;
		
		this.graph = new ErdosRenyiModel(this.m0, e0).generateERModelWithMarkedLinks(negativeLinkProbability);
	}
	
	public BarabasiAlbertModel(int n,int m0, double p, int m, double negativeLinkProbability) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("");
		
		if (m0 > n) 
			throw new IllegalArgumentException("");
		
		if (p < 0 || p > 1.0)
			throw new IllegalArgumentException("");
		
		if (m >= m0) 
			throw new IllegalArgumentException("");
		
		if (negativeLinkProbability < 0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("");
		
		this.nodesNumber = n;
		this.m0 = m0;
		this.m = m;
		
		this.graph = new GilbertModel(m0, p).getGraph(negativeLinkProbability);
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getBANetwork(double negativeLinkProbability) {
	
		Random rnd = new Random();
		
		List<Integer> degs = new ArrayList<>();
		for (int i = 0; i < this.m0; i++) {
			for (int j = 0; j < graph.degree(i); j++) 
				degs.add(i);
		}
		
		for (int i = this.m0; i < this.nodesNumber; i++) {
			graph.addVertex(i);
			for (int j = 0; j < this.m; j++) {
				int old;
				do {
					old = degs.get(rnd.nextInt(degs.size()));
				} while (old == i);
				if (graph.findEdge(i, old) == null) {
					graph.addEdge(new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE), i, old);
					degs.add(old);
				}
			}
			
			for (int j = 0; j < this.m; j++)
				degs.add(i);
		}
		
		return graph;
	}
	
	public static void main(String[] args) throws GraphIsClusterableException {
		
		Supplier<Integer> nodeFactory = new Supplier<Integer>() {
			private int i = 0;
			@Override
			public Integer get() {
				return i++;
			}
		};
		Supplier<MarkedEdge> edgeFactory = new Supplier<MarkedEdge>() {
			private static double P = 0.15;
			@Override
			public MarkedEdge get() {
				Random rnd = new Random();
				return new MarkedEdge(rnd.nextDouble() < P ? Mark.NEGATIVE : Mark.POSITIVE);
			}
		};
		BarabsiAlbertRandomModel<Integer, MarkedEdge> erMR = new BarabsiAlbertRandomModel<>(250, 10, 25, 1, nodeFactory, edgeFactory, edgeFactory);
		UndirectedSparseGraph<Integer, MarkedEdge> g1 = new UndirectedSparseGraph<Integer, MarkedEdge>();
		erMR.getGraph(g1);
		
		UndirectedSparseGraph<Integer, MarkedEdge> g = new BarabasiAlbertModel(250, 10, 25, 1, 0.15).getBANetwork(0.20);
		System.out.println(new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g1, "res/BarabasiAlbert.gml"));
		ComponentClustererBFS<Integer, MarkedEdge >ccBFS = new ComponentClustererBFS<>(g1, MarkedEdge::getMark);
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
