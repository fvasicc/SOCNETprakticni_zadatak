package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static UndirectedSparseGraph<String, MarkedEdge> readWiki(String file) throws FileNotFoundException, IOException {
		return readWiki(file, DEFAULT_LINES);
	}

	public static UndirectedSparseGraph<String, MarkedEdge> readWiki(String file, int lines) throws FileNotFoundException, IOException {
		String regex = "(?sm)SRC:(?<src>.*?)TGT:(?<tgt>.*?)VOT:(?<vot>-?\\d{1}).*";
		Pattern pattern = Pattern.compile(regex);
		Matcher m = null;
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			UndirectedSparseGraph<String, MarkedEdge> graph = new UndirectedSparseGraph<>();
			String line;
			int linesCounter = 0;
			do {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < 7; i++) {
					line = in.readLine();
					sb.append(line +"\n");
				}
				m = pattern.matcher(sb.toString());
				if (!m.find()) continue;
				String node1 = m.group("src").trim();
				String node2 = m.group("tgt").trim();
				Integer vot = Integer.parseInt(m.group("vot"));
				
//				Mark mark = Mark.getMark(vot >= 0 ? 1 : -1); // neutralne cvorove posmatramo kao pozitivne
				
				Mark mark = Mark.getMark(vot > 0 ? 1 : -1); // neutralne cvorove posmatramo kao negativne
				
//				if (vot == 0) continue;
//				Mark mark = Mark.getMark(vot); // neutralne cvorove ne gledamo, preskacemo ih
				
				
				MarkedEdge checkPotentialEdge = graph.findEdge(node1, node2);
				
				if (checkPotentialEdge != null) {
					if (checkPotentialEdge.getMark() == Mark.POSITIVE && mark == Mark.NEGATIVE)
						checkPotentialEdge.setNewMark(Mark.NEGATIVE);
				} else {
					graph.addEdge(new MarkedEdge(mark), node1, node2);
				}
			} while (linesCounter++ < lines && (line = in.readLine()) != null);
//			while (linesCounter < lines && (line = in.readLine()) != null) {
//				StringBuilder sb = new StringBuilder();
//				for (int i = 0; i < 7; i++) {
//					line = in.readLine();
//					System.out.println(line);
//					sb.append(line +"\n");
//				}
//				m = pattern.matcher(sb.toString());
//				
//				String node1 = m.group("src").trim();
//				String node2 = m.group("tgt").trim();
//				Integer vot = Integer.parseInt(m.group("vot"));
//				Mark mark = Mark.getMark(vot >= 0 ? 1 : -1);
//				
//				MarkedEdge checkPotentialEdge = graph.findEdge(node1, node2);
//				
//				if (checkPotentialEdge != null) {
//					if (checkPotentialEdge.getMark() == Mark.POSITIVE && mark == Mark.NEGATIVE)
//						checkPotentialEdge.setNewMark(Mark.NEGATIVE);
//				} else {
//					graph.addEdge(new MarkedEdge(mark), node1, node2);
//				}
//				
//				linesCounter++;
//			}
			System.out.println(linesCounter);
			return graph;
		}
	}

}
