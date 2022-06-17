package networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;


public class ErdosRenyiRandomModel<V, E> {

	private int n;
	private int e;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> edgeFactory;
	
	public ErdosRenyiRandomModel(int n, int e, Supplier<V> nodeFactory, Supplier<E> edgeFactory) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("");
		
		if (e < 0) 
			throw new IllegalArgumentException("");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("");
		
		if (edgeFactory == null)
			throw new IllegalArgumentException("");
		
		this.n = n;
		this.e = e;
		this.edgeFactory = edgeFactory;
		this.nodeFactory = nodeFactory;
	}
	
	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
		
		for (int i = 0; i < this.n; i++) {
			targetGraph.addVertex(nodeFactory.get());
		}
		
		double maxEdgeNumber = this.n * (this.n - 1) / 2.0;
		Random rnd = new Random();
		
		List<V> nodes = new ArrayList<>(targetGraph.getVertices());
		
		while (targetGraph.getEdgeCount() < this.e) {
			for (int i = 0; i < this.n - 1; i++) {
				for (int j = i + 1; j < this.n; j++) {
					if (rnd.nextDouble() < 1.0/maxEdgeNumber) {
						targetGraph.addEdge(edgeFactory.get(), nodes.get(i), nodes.get(j));
					}
				}
			}
		}
	}
}
