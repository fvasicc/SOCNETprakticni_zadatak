package interfaces;

import java.util.List;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public interface ComponentClustererU<V, E> {

	UndirectedSparseGraph<V, E> getGiantComponent();
	
	List<UndirectedSparseGraph<V, E>> getAllComponents();
}
