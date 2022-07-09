package networks.clusterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.RandomGraph;
import model.edge.Mark;

public class GilbertModelClusterable<V, E> implements RandomGraph<V, E>{

	private int n;
	private double p;
	
	private Supplier<V> nodeFactory;
	private Supplier<E> positiveEdgeFactory;
	private Supplier<E> negativeEdgeFactory;
	
	private Transformer<E, Mark> markTransformer;
	
	public GilbertModelClusterable(int n, double p, Supplier<V> nodeFactory, Supplier<E> positiveEdgeFactory, Supplier<E> negativeEdgeFactory, Transformer<E, Mark> markTransformer) {
		
		if (n <= 0) 
			throw new IllegalArgumentException("Parameter n -> Number of nodes must be postive!");
		
		if (p < 0 || p > 1) 
			throw new IllegalArgumentException("Parameter p -> Probability must be in interval [0,1]");
		
		if (nodeFactory == null)
			throw new IllegalArgumentException("Parameter nodeFactory -> Can't be null!");
		
		if (positiveEdgeFactory == null)
			throw new IllegalArgumentException("Parameter positiveEdgeFactory -> Can't be null!");
		
		if (negativeEdgeFactory == null)
			throw new IllegalArgumentException("Parameter negativeEdgeFactory -> Can't be null!");
		
		if (markTransformer == null)
			throw new IllegalArgumentException("Parameter markTransformer -> Can't be null!");
		
		this.n = n;
		this.p = p;
		this.positiveEdgeFactory = positiveEdgeFactory;
		this.negativeEdgeFactory = negativeEdgeFactory;
		this.nodeFactory = nodeFactory;
		this.markTransformer = markTransformer;
		this.nodeFactory = nodeFactory;
	}
	
	@Override
	public void getGraph(UndirectedSparseGraph<V, E> targetGraph) {
		
		for (int i = 0; i < this.n; i++) {
			targetGraph.addVertex(nodeFactory.get());
		}
		
		Random rnd = new Random();
		List<V> nodes = new ArrayList<>(targetGraph.getVertices());	
		
		for (int i = 0; i < this.n - 1; i++) {
			for (int j = i + 1; j < this.n; j++) {
				if (rnd.nextDouble() < p) {
					Clusterable<V, E> c = new Clusterable<>();
					c.addEdge(targetGraph, nodes.get(i), nodes.get(j), positiveEdgeFactory, negativeEdgeFactory, markTransformer);
				}
			}
		}
	}			
}
