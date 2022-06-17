package networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class GilbertRandomModel<V, E> {

	private int n;
	private double p;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> edgeFactory;
	
	public GilbertRandomModel(int n, double p, Supplier<V> nodeFactory, Supplier<E> edgeFactory) {
		if (n <= 0) 
			throw new IllegalArgumentException("");
		
		if (p < 0 || p > 1) 
			throw new IllegalArgumentException("");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("");
		
		if (edgeFactory == null)
			throw new IllegalArgumentException("");
		
		this.n = n;
		this.p = p;
		this.edgeFactory = edgeFactory;
		this.nodeFactory = nodeFactory;
	}
	
	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
		
		for (int i = 0; i < this.n; i++) {
			targetGraph.addVertex(nodeFactory.get());
		}
		
		Random rnd = new Random();
		List<V> nodes = new ArrayList<>(targetGraph.getVertices());	
		
		for (int i = 0; i < this.n - 1; i++) {
			for (int j = i + 1; j < this.n; j++) {
				if (rnd.nextDouble() < p) {
					targetGraph.addEdge(edgeFactory.get(), nodes.get(i), nodes.get(j));
				}
			}
		}
	}
}
