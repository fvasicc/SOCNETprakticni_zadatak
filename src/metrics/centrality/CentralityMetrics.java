package metrics.centrality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.Graph;

public class CentralityMetrics<V, E> {
	
	private Graph<V, E> graph = null;
	private BetweennessCentrality<V, E> bc;
	private ClosenessCentrality<V, E> cc; 
	private EigenvectorCentrality<V, E> ec;
	
	
	private Map<V, Double> bcMap = new HashMap<>();
	private Map<V, Double> ccMap = new HashMap<>();
	private Map<V, Double> ecMap = new HashMap<>();
	
	public CentralityMetrics(Graph<V, E> graph) {
		this.graph = graph;
		this.bc = new BetweennessCentrality<>(graph);
		this.cc = new ClosenessCentrality<>(graph);
		this.ec = new EigenvectorCentrality<>(graph);
		this.ec.acceptDisconnectedGraph(true);
		this.ec.evaluate();
		
		for (V v : this.graph.getVertices()) {
			bcMap.put(v, this.bc.getVertexScore(v));
			ccMap.put(v, this.cc.getVertexScore(v));
			ecMap.put(v, this.ec.getVertexScore(v));
		}
	}
	
	public CentralityMetrics(Graph<V, E> graph, boolean centralize) {
		this(graph);
		if (centralize) {
			int val = (graph.getVertexCount() - 1) * (graph.getVertexCount() - 2) / 2;
			for (Entry<V, Double> entry : bcMap.entrySet()) {
				entry.setValue(entry.getValue() / val);
			}
		}
	}
	
	public Map<V, Double> getMaxNBC(int n) {
		return firstN(n, bcMap);
	}
	
	public Map<V, Double> getMaxNCC(int n) {
		return firstN(n, ccMap);
	}
	
	public Map<V, Double> getMaxNEC(int n) {
		return firstN(n, ecMap);
	}
	
	private Map<V, Double> firstN(int n, Map<V, Double> map) {
		
		List<Entry<V, Double>> l = new ArrayList<>(map.entrySet());
		
		for (Entry<V, Double> entry : l) {
			if (entry.getValue().isNaN()) entry.setValue(0.0);
		}
		
		l.sort(Entry.comparingByValue());
		
		Map<V, Double> ret = new LinkedHashMap<>();
		for (int i = l.size() - 1; i > l.size() - 1 - n; i--) {
			ret.put(l.get(i).getKey(), l.get(i).getValue());
		}
		
		return ret;
	}
	
	public double getBCfor(V v) {
		return this.bcMap.get(v);
	}
	
	public double getCCfor(V v) {
		return this.ccMap.get(v);
	}
	
	public double getECfor(V v) {
		return this.ecMap.get(v);
	}
	
	public V getNodeWithMaxBC() {
		return getRetValue(bcMap);
	}
	
	public V getNodeWithMaxCC() {
		return getRetValue(ccMap);
	}
	
	public V getNodeWithMaxEC() {
		return getRetValue(ecMap);
	}

	private V getRetValue(Map<V, Double> map) {
		V ret = null;
		for (Map.Entry<V, Double> entry : map.entrySet()) {
			if (ret == null) 
				ret = entry.getKey();
			else if (entry.getValue() > map.get(ret))
				ret = entry.getKey();
		}
		return ret;
	}
	
	public BetweennessCentrality<V, E> getBc() {
		return bc;
	}

	public ClosenessCentrality<V, E> getCc() {
		return cc;
	}

	public EigenvectorCentrality<V, E> getEc() {
		return ec;
	}

	public Map<V, Double> getBcMap() {
		return bcMap;
	}

	public Map<V, Double> getCcMap() {
		return ccMap;
	}

	public Map<V, Double> getEcMap() {
		return ecMap;
	}	
}
