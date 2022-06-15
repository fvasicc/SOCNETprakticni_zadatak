package tests;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;


public class NetworkWriter<V, E> {
	
	Transformer<E, Mark> markTransform; 
	
	public NetworkWriter(Transformer<E, Mark> transformer) {
		this.markTransform = transformer;
	} 

	public boolean exportGML(UndirectedSparseGraph<V, E> graph, String fileName) {
		String s = "graph [\n\tmultigraph 1\n";
		int id = 0;
		for (V v : graph.getVertices()) {
			s += "    node [\n"
					+ "    id " + id + "\n"
					+ "    label \" " + v + "\"\n"
					+ "  ]\n";
			id++;
		}
		
		int i = 0;
		for(V v1 : graph.getVertices()) {
			int j = 0;
			for (V v2 : graph.getVertices()) {
				E e = graph.findEdge(v1, v2);
				if (e != null) {
					s+= "  edge [\n"
							+ "    source " + i +"\n"
							+ "    target " + j +"\n"
							+ "    sign " + markTransform.transform(e) +"\n"
							+ "  ]\n";
				}
				j++;
			}
			i++;
		}
		s+= "]";
		
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write(s);
			fw.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
