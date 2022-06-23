package tests;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.edge.Mark;
import model.node.GCNode;


public class NetworkWriter<V, E> {
	
	Transformer<E, Mark> markTransform; 
	
	public NetworkWriter(Transformer<E, Mark> transformer) {
		this.markTransform = transformer;
	} 
	
	public boolean exportCompGraphGML(UndirectedSparseGraph<GCNode, E> graph, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("graph [\n\tmultigraph 1\n");
		for (GCNode v : graph.getVertices()) {
			sb.append("    node [\n"
					+ "    id " + v.getId() + "\n"
					+ "    label \" " + v.getLabel() + "\"\n"
					+ "    weight " + v.getNodes() + "\n"
					+ "    coalition \" " + v.isCoalition() + "\"\n"
					+ "  ]\n");
		}
		
		for(GCNode v1 : graph.getVertices()) {
			for (GCNode v2 : graph.getVertices()) {
				E e = graph.findEdge(v1, v2);
				if (e != null) {
					sb.append("  edge [\n"
							+ "    source " + v1.getId() +"\n"
							+ "    target " + v2.getId() +"\n"
							+ "    sign " + markTransform.transform(e) +"\n"
							+ "  ]\n");
				}
			}
		}
		sb.append("]");
		
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write(sb.toString());
			fw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean exportGML(UndirectedSparseGraph<V, E> graph, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("graph [\n\tmultigraph 1\n");
		int id = 0;
		for (V v : graph.getVertices()) {
			sb.append("    node [\n"
					+ "    id " + id + "\n"
					+ "    label \" " + v + "\"\n"
					+ "  ]\n");
			id++;
		}
		
		int i = 0;
		for(V v1 : graph.getVertices()) {
			int j = 0;
			for (V v2 : graph.getVertices()) {
				E e = graph.findEdge(v1, v2);
				if (e != null) {
					sb.append("  edge [\n"
							+ "    source " + i +"\n"
							+ "    target " + j +"\n"
							+ "    sign " + markTransform.transform(e) +"\n"
							+ "  ]\n");
				}
				j++;
			}
			i++;
		}
		sb.append("]");
		
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write(sb.toString());
			fw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
