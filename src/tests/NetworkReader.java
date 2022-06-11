package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.Mark;
import model.MarkedEdge;

public class NetworkReader {
	
	public static final int DEFAULT_LINES = 30000;
	
	public static UndirectedSparseGraph<Integer, MarkedEdge> readEpinionsOrSlashdot(String file) 
			throws IllegalArgumentException, FileNotFoundException, IOException {
		return readEpinionsOrSlashdot(file, DEFAULT_LINES);
	}
	
	public static UndirectedSparseGraph<Integer, MarkedEdge> readEpinionsOrSlashdot(String file, int lines)
			throws IllegalArgumentException, FileNotFoundException, IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			UndirectedSparseGraph<Integer, MarkedEdge> graph = new UndirectedSparseGraph<>();
			String inLine;
			int i = 0;
			while (i < lines && (inLine = in.readLine()) != null) {
				String[] tokens = inLine.split("\t");
				
				Integer node1 = Integer.parseInt(tokens[0].trim());
				Integer node2 = Integer.parseInt(tokens[1].trim());
				Mark mark = Mark.getMark(Integer.parseInt(tokens[2].trim()));

				MarkedEdge checkPotentialEdge = graph.findEdge(node1, node2);

				if (checkPotentialEdge != null) {
					if (checkPotentialEdge.getMark() == Mark.POSITIVE && mark == Mark.NEGATIVE)
						checkPotentialEdge.setNewMark(Mark.NEGATIVE);
				} else {
					graph.addEdge(new MarkedEdge(mark), node1, node2);
				}
				i++;
			}
			return graph;
		}
	}
	
	public static UndirectedSparseGraph<Integer, MarkedEdge> readWiki(String file) {
		return readWiki(file, DEFAULT_LINES);
	}

	public static UndirectedSparseGraph<Integer, MarkedEdge> readWiki(String file, int lines) {
		return null;
	}

}
