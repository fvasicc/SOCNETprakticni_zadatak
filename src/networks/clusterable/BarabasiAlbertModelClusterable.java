package networks.clusterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.RandomGraph;
import model.edge.Mark;

public class BarabasiAlbertModelClusterable<V, E> implements RandomGraph<V, E> {

	private int n;
	private int m0;
	private int e0;
	private int m;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> positiveEdgeFactory;
	private Supplier<E> negativeEdgeFactory;

	private Transformer<E, Mark> markTransformer;
	
	public BarabasiAlbertModelClusterable(int n, int m0, int e0, int m, Supplier<V> nodeFactory, Supplier<E> positiveEdgeFactory, Supplier<E> negativeEdgeFactory, Transformer<E, Mark> markTransformer) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Parameter n -> Number of nodes must be positive!");
		
		if (e0 < 0) 
			throw new IllegalArgumentException("Parameter e0 -> Number of edges in Erdos Renyi network must be positive!");
		
		if (m0 > n || m0 <= 0) 
			throw new IllegalArgumentException("Parameter m0 -> Number of nodes in Erdos Renyi network must be positive and less than n!");
		
		if (m >= m0) 
			throw new IllegalArgumentException("Parameter m -> Number of new connections must be less than number of nodes in initial graph!");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("Parameter nodeFactory -> Can't be null!");
		
		if (positiveEdgeFactory == null)
			throw new IllegalArgumentException("Parameter positiveEdgeFactory -> Can't be null!");
		
		if (negativeEdgeFactory == null)
			throw new IllegalArgumentException("Parameter negativeEdgeFactory -> Can't be null!");
		
		if (markTransformer == null)
			throw new IllegalArgumentException("Parameter markTransformer -> Can't be null!");
		
		this.n = n;
		this.m0 = m0;
		this.e0 = e0;
		this.m = m;
		this.positiveEdgeFactory = positiveEdgeFactory;
		this.negativeEdgeFactory = negativeEdgeFactory;
		this.nodeFactory = nodeFactory;
		this.markTransformer = markTransformer;
	}

	@Override
	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
//		ErdosRenyiModelClus<V, E> er = new ErdosRenyiRandomModel<>(m0, e0, nodeFactory, baseGraphEdgeFactory);
		ErdosRenyModelClusterable<V, E> er = new ErdosRenyModelClusterable<>(m0, e0, nodeFactory, positiveEdgeFactory, negativeEdgeFactory, markTransformer);
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
					Clusterable<V, E> c = new Clusterable<>();
					c.addEdge(targetGraph, v, old, positiveEdgeFactory, negativeEdgeFactory, markTransformer);
					degs.add(old);
				}
			}
			
			for (int j = 0; j < this.m; j++)
				degs.add(v);
		}		
	}

}
