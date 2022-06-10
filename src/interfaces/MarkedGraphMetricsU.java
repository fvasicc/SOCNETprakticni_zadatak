package interfaces;

import java.util.List;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.EdgeInfo;

public interface MarkedGraphMetricsU<V, E> {
	
	List<UndirectedSparseGraph<V, E>> getCoalitions();
	
	List<UndirectedSparseGraph<V, E>> getNonCoalitions();
	
	boolean isClusterable();
	
	List<EdgeInfo<E, V>> getNegativeLinks() throws GraphIsClusterableException;

}
