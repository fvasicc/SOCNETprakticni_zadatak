package test;

import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;

public class FirstTest {
	public static void main(String[] args) {
		UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 0, 1);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 2);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 1, 3);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 1, 5);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 4, 5);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 6);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 7);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 5, 8);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 6, 7);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 6, 8);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 7, 8);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 7, 11);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 7, 13);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 9);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 10);
		graph.addEdge(new MarkedEdge(Mark.NEGATIVE), 8, 11);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 9, 10);
		graph.addEdge(new MarkedEdge(Mark.POSITIVE), 11, 12);
		
		System.out.println(graph.getVertexCount());
		ComponentClustererBFS<Integer, MarkedEdge> a = new ComponentClustererBFS<>(graph, MarkedEdge::getMark);
		
		System.out.println(a.getGiantComponent());
	}
}
