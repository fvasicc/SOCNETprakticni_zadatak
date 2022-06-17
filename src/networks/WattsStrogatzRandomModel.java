package networks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.RandomGraph;

public class WattsStrogatzRandomModel<V, E> implements RandomGraph<V, E>{
	
	private int n;
	private int k;
	private double p;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> edgeFactory;
	
	public WattsStrogatzRandomModel(int n, int k, double p, Supplier<V> nodeFactory, Supplier<E> edgeFactory) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Parameter n -> Number of nodes must be positive!");
		
		if (k <= 0 || k > n)
			throw new IllegalArgumentException("Parameter k -> Number of neighbors in initial graph must be in interval [1, n)");
		
		if (p < 0 || p > 1)
			throw new IllegalArgumentException("Parameter p -> Probability must be in interval [0,1] !");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("Parameter nodeFactory -> Can't be null!");
		
		if (edgeFactory == null)
			throw new IllegalArgumentException("Parameter edgeFactory -> Can't be null!");
		
		this.n = n;
		this.k = k;
		this.p = p;
		
		this.edgeFactory = edgeFactory;
		this.nodeFactory = nodeFactory;
	}

	@Override
	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
		
		for (int i = 0; i < this.n; i++) {
			targetGraph.addVertex(nodeFactory.get());
		}
		
		List<V> nodes = new ArrayList<>(targetGraph.getVertices());	
		for (int i = 0; i < this.n; i++) {
			for (int j = 1; j <= this.k/2; j++) {
				targetGraph.addEdge(edgeFactory.get(), nodes.get(i), nodes.get((i + j) % this.n));
			}
		}
		Random rnd = new Random();
		
		for (int i = 0; i < this.n; i++) { 
			V v1 = nodes.get(i);
			for (V v2 : new ArrayList<V>(targetGraph.getNeighbors(v1))) {
				// graph.getNeighborCount(i) < this.numberOfNodes - 1 da izbegnemo beskonacnu petlju ako je cvor povezan sa svima
				// i u while petlji ne moze da pronadje ok cvor za povezivanje
				if (rnd.nextDouble() < this.p && targetGraph.getNeighborCount(v1) < this.n - 1) {
					int dst = 0;
					boolean dstOk = false;
					while (!dstOk) {
						dst = (int) (rnd.nextDouble() * this.n);
						dstOk = dst != i && targetGraph.findEdge(v1, nodes.get(dst)) == null;
					}
					targetGraph.removeEdge(targetGraph.findEdge(v1, v2));
					targetGraph.addEdge(edgeFactory.get(), v1, nodes.get(dst));
				}
			}
		}
		
	}
}
