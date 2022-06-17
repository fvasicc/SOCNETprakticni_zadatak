package networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class BarabsiAlbertRandomModel<V, E> {
	
	private int n;
	private int m0;
	private int e0;
	private int m;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> edgeFactory;
	
	private Supplier<E> baseGraphEdgeFactory;
	
	public BarabsiAlbertRandomModel(int n, int m0, int e0, int m, Supplier<V> nodeFactory, Supplier<E> edgeFactory, Supplier<E> baseGraphEdgeFactory) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("");
		
		if (e0 < 0) 
			throw new IllegalArgumentException("");
		
		if (m0 > n) 
			throw new IllegalArgumentException("");
		
		if (m >= m0) 
			throw new IllegalArgumentException("");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("");
		
		if (edgeFactory == null)
			throw new IllegalArgumentException("");
		
		if (baseGraphEdgeFactory == null)
			throw new IllegalArgumentException("");
		
		this.n = n;
		this.m0 = m0;
		this.e0 = e0;
		this.m = m;
		this.edgeFactory = edgeFactory;
		this.nodeFactory = nodeFactory;
		this.baseGraphEdgeFactory = baseGraphEdgeFactory;
	}

	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
		ErdosRenyiRandomModel<V, E> er = new ErdosRenyiRandomModel<>(m0, e0, nodeFactory, baseGraphEdgeFactory);
		er.getGraph(targetGraph);
		
		Random rnd = new Random();
		List<V> nodes = new ArrayList<>(targetGraph.getVertices());	
		List<V> degs = new ArrayList<>();
		for (int i = 0; i < this.m0; i++) {
			for (int j = 0; j < targetGraph.degree(nodes.get(i)); j++) 
				degs.add(nodes.get(i));
		}
		
		for (int i = this.m0; i < this.n; i++) {
			V v = nodeFactory.get();
			targetGraph.addVertex(v);
			nodes.add(v);
			for (int j = 0; j < this.m; j++) {
				V old;
				do {
					old = degs.get(rnd.nextInt(degs.size()));
				} while (v.equals(old));
				if (targetGraph.findEdge(v, old) == null) {
					targetGraph.addEdge(edgeFactory.get(), v, old);
					degs.add(old);
				}
			}
			
			for (int j = 0; j < this.m; j++)
				degs.add(v);
		}		
	}
}
