package tests.random_networks;

import java.util.Random;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;
import tests.NetworkWriter;

public class ErdosRenyiModel {
	
	public static double NEGATIIVE_LINK_PROBABILITY = 0.0;
	
	private int nodesNumber;
	private int edgesNumber;

	public ErdosRenyiModel(int nodesNumber, int edgesNumber) {
		
		if (nodesNumber <= 0) 
			throw new IllegalArgumentException("");
		
		if (edgesNumber < 0) 
			throw new IllegalArgumentException("");
		
		this.edgesNumber = edgesNumber;
		this.nodesNumber = nodesNumber;
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> generateERModelWithMarkedLinks(double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0 || negativeLinkProbability > 1) 
			throw new IllegalArgumentException("");
		
		UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
		
		for (int i = 0; i < this.nodesNumber; i++) {
			graph.addVertex(i);
		}
		
		double maxEdgeNumber = this.nodesNumber * (this.nodesNumber - 1) / 2.0;
		Random rnd = new Random();
		
		
		while (graph.getEdgeCount() < this.edgesNumber) {
			for (int i = 0; i < this.nodesNumber - 1; i++) {
				for (int j = i + 1; j < this.nodesNumber; j++) {
					if (rnd.nextDouble() < 1.0/maxEdgeNumber) {
						maxEdgeNumber--;
						graph.addEdge(new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE), i, j);
					}
				}
			}
		}
				
		return graph;
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> generateERModel() {
		return generateERModelWithMarkedLinks(NEGATIIVE_LINK_PROBABILITY);
	}
	
	public static void main(String[] args) {
		ErdosRenyiModel erM = new ErdosRenyiModel(250, 500);
		UndirectedSparseGraph<Integer, MarkedEdge> g = erM.generateERModelWithMarkedLinks(0.5);
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g, "res/ErdosRenyi.gml");
		ComponentClustererBFS<Integer, MarkedEdge> cc = new ComponentClustererBFS<>(g, MarkedEdge::getMark);
		System.out.println(cc.getNegativeLinksCount());
		System.out.println(cc.getAllComponents().size());
		System.out.println(cc.isClusterable());
	}
}
