package tests.random_networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.Mark;
import model.MarkedEdge;
import tests.NetworkWriter;

public class BarabasiAlbertModel {
	
	private int nodesNumber;
	private int m0;
	private double nodesConnectionProbability;
	private int m;
	
	public BarabasiAlbertModel(int nodesNumber, int m0, double nodesConnectionProbability, int m) {
		
		if (nodesNumber <= 0) 
			throw new IllegalArgumentException("");
		
		if (nodesConnectionProbability < 0 || nodesConnectionProbability > 1) 
			throw new IllegalArgumentException("");
		
		if (m0 > nodesNumber) 
			throw new IllegalArgumentException("");
		
		if (m >= m0) 
			throw new IllegalArgumentException("");
		
		this.nodesNumber = nodesNumber;
		this.m0 = m0;
		this.nodesConnectionProbability = nodesConnectionProbability;
		this.m = m;
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getBANetwork(double negativeLinkProbability) {
		
		UndirectedSparseGraph<Integer, MarkedEdge> graph = new GilbertModel(this.m0, this.nodesConnectionProbability).getGraph(negativeLinkProbability);
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
					old = rnd.nextInt(degs.size());
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
		UndirectedSparseGraph<Integer, MarkedEdge> g = new BarabasiAlbertModel(30, 10, 0.4, 8).getBANetwork(0.05);
		System.out.println(g);
		System.out.println(new ComponentClustererBFS<>(g, MarkedEdge::getMark).getNegativeLinks());

		System.out.println(new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/BarabasiAlbert.gml"));
	}

}
