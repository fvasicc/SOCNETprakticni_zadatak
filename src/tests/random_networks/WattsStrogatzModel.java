package tests.random_networks;

import java.util.ArrayList;
import java.util.Random;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;

public class WattsStrogatzModel {
	
	private int numberOfNodes;
	private int k;
	private double p;
	
	public WattsStrogatzModel(int numberOfNodes, int k, double p) {
		
		if (numberOfNodes <= 0) 
			throw new IllegalArgumentException("");
		
		if (k <= 0 || k > numberOfNodes)
			throw new IllegalArgumentException("");
		
		if (p < 0 || p > 1)
			throw new IllegalArgumentException("");
		
		this.numberOfNodes = numberOfNodes;
		this.k = k;
		this.p = p;
	}

	public UndirectedSparseGraph<Integer, MarkedEdge> getRandomGraph(double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0 || negativeLinkProbability > 1) 
			throw new IllegalArgumentException("");
		
		UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
		Random rnd = new Random();
		
		for (int i = 0; i < this.numberOfNodes; i++) {
			graph.addVertex(i);
		} 
		
		for (int i = 0; i < this.numberOfNodes; i++) {
			for (int j = 1; j <= this.k/2; j++) {
				graph.addEdge(new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE), i, (i + j) % this.numberOfNodes);
			}
		}
		
		System.out.println(graph);
		
		for (int i = 0; i < this.numberOfNodes; i++) { 
			for (int j : new ArrayList<Integer>(graph.getNeighbors(i))) {
				if (i < j && rnd.nextDouble() < this.p && graph.getNeighborCount(i) < this.numberOfNodes - 1) {
					int dst = 0;
					boolean dstOk = false;
					while (!dstOk) {
						dst = (int) (rnd.nextDouble() * this.numberOfNodes);
						dstOk = dst != i && graph.findEdge(i, dst) == null;
					}
					graph.removeEdge(graph.findEdge(i, j));
					graph.addEdge(new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE), i, dst);
				}
			}
		}
		
		return graph;
	}
	
	public static void main(String[] args) {
		
	}
}
