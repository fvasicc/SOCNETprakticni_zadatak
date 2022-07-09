package networks.clusterable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.edge.Mark;

public class Clusterable<V, E> {
	
	private Set<V> visited(UndirectedSparseGraph<V, E> targetGraph, V src, V dst, Supplier<E> positiveEdgeFactory,
			Supplier<E> negativeEdgeFactory, Transformer<E, Mark> markTransformer){
		Queue<V> queue = new LinkedList<>();
		Set<V> visited = new HashSet<>();
		queue.add(src);
		visited.add(src);
		while (!queue.isEmpty()) {
			V curr = queue.poll();
			for (V v : targetGraph.getNeighbors(curr)) {
				if (!visited.contains(v)) {
					if (markTransformer.transform(targetGraph.findEdge(curr, v)) == Mark.POSITIVE) {
						visited.add(v);
						queue.add(v);
						if (v.equals(dst)) {
							targetGraph.addEdge(positiveEdgeFactory.get(), src, dst);
							return null;
						}
					} else {
						if (v.equals(dst)) {
							targetGraph.addEdge(negativeEdgeFactory.get(), src, dst);
							return null;
						}
					}
				}
			}
		}
		return visited;
	}

	public void addEdge(UndirectedSparseGraph<V, E> targetGraph, V src, V dst, Supplier<E> positiveEdgeFactory,
			Supplier<E> negativeEdgeFactory, Transformer<E, Mark> markTransformer) {

		Random rnd = new Random();
		
		if (targetGraph.getNeighborCount(src) == 0) {
			targetGraph.addEdge(rnd.nextDouble() < 0.8 ? positiveEdgeFactory.get() : negativeEdgeFactory.get(), src, dst);
		} else {
			
			Set<V> visitedSrc = visited(targetGraph, src, dst, positiveEdgeFactory, negativeEdgeFactory, markTransformer);
			
			Set<V> visitedDst = visited(targetGraph, dst, src, positiveEdgeFactory, negativeEdgeFactory, markTransformer);
			
			
			if (visitedSrc != null && visitedDst != null) {
				for (V v1 : visitedDst) {
					for (V v2 : visitedSrc) {
						E e = targetGraph.findEdge(v1, v2);
						if (e != null && markTransformer.transform(e) == Mark.NEGATIVE) {
							targetGraph.addEdge(negativeEdgeFactory.get(), src, dst);
							return;
						}
					}
				}
				targetGraph.addEdge(rnd.nextDouble() < 0.75 ? positiveEdgeFactory.get() : negativeEdgeFactory.get(), src, dst);
			}
		}
	}
}
