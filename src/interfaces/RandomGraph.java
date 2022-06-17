package interfaces;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public interface RandomGraph<V, E> {
	
	void getGraph(UndirectedSparseGraph<V, E> targetGraph);
}
