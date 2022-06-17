package tests.random_networks;

import java.util.Random;
import java.util.function.Supplier;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;
import networks.ErdosRenyiRandomModel;
import tests.NetworkWriter;

public class ErdosRenyiModel {
	
	private UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
	
	public ErdosRenyiModel(int nodes, int edges, double negativeLinkProbability) {
		
		if (negativeLinkProbability < 0.0 || negativeLinkProbability > 1.0)
			throw new IllegalArgumentException("");
		
		Supplier<Integer> nodeFactory = new Supplier<Integer>() {
			private int i = 0;
			@Override
			public Integer get() {
				return i++;
			}
		};
		Supplier<MarkedEdge> edgeFactory = new Supplier<MarkedEdge>() {
			@Override
			public MarkedEdge get() {
				Random rnd = new Random();
				return new MarkedEdge(rnd.nextDouble() < negativeLinkProbability ? Mark.NEGATIVE : Mark.POSITIVE);
			}
		};
		
		ErdosRenyiRandomModel<Integer, MarkedEdge> er = new ErdosRenyiRandomModel<>(nodes, edges, nodeFactory, edgeFactory);
		er.getGraph(graph);
		
	}
	
	public UndirectedSparseGraph<Integer, MarkedEdge> getGraph() {		
		return graph;
	}
	
	public static void main(String[] args) {
		ErdosRenyiModel er = new ErdosRenyiModel(250, 500, 0.5);
		UndirectedSparseGraph<Integer, MarkedEdge> g1 = er.getGraph();
		new NetworkWriter<Integer, MarkedEdge>(MarkedEdge::getMark).exportGML(g1, "res/ErdosRenyi.gml");
		ComponentClustererBFS<Integer, MarkedEdge> cc = new ComponentClustererBFS<>(g1, MarkedEdge::getMark);
		System.out.println(cc.getNegativeLinksCount());
		System.out.println(cc.getAllComponents().size());
		System.out.println(cc.isClusterable());
	}
}
