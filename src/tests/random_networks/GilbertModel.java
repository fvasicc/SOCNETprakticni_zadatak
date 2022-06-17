package tests.random_networks;

import java.util.Random;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;
import tests.NetworkWriter;

public class GilbertModel {
	
	public static double NEGATIVE_LINK_PROBABILITY = 0.0;
	
	public static double NODE_CONNECTION_PROBABILITY = 0.5;
	
	private int nodesNumber;
	private double nodesConnectionProbability;
	
	public GilbertModel(int nodesNumber, double nodesConnectionProbability) {
		if (nodesNumber <= 0) 
			throw new IllegalArgumentException("");
		
		if (nodesConnectionProbability < 0 || nodesConnectionProbability > 1) 
			throw new IllegalArgumentException("");
		
		this.nodesNumber = nodesNumber;
		this.nodesConnectionProbability = nodesConnectionProbability;
	}
	
	public GilbertModel(int nodesNumber) {
		this(nodesNumber, NODE_CONNECTION_PROBABILITY);
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph(double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0 || negativeLinkProbability > 1) 
			throw new IllegalArgumentException("");
		
		UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
		
		for (int i = 0; i < this.nodesNumber; i++) {
			graph.addVertex(i);
		}
		
		Random rnd = new Random();
		
		for (int i = 0; i < this.nodesNumber - 1; i++) {
			for (int j = i + 1; j < this.nodesNumber; j++) {
				if (rnd.nextDouble() < this.nodesConnectionProbability) {
					graph.addEdge(new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE), i, j);
				}
			}
		}
		
		return graph;
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {
		return getGraph(NEGATIVE_LINK_PROBABILITY);
	}
	public static void main(String[] args) {
		GilbertModel gm = new GilbertModel(250, 0.05);
		UndirectedSparseGraph<Integer, MarkedEdge> g = gm.getGraph(0.50);
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/Gilbert.gml");
//		ComponentClustererBFS<Integer, MarkedEdge> cc = new ComponentClustererBFS<>(g, MarkedEdge::getMark);
	}
}
