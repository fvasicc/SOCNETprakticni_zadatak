package tests;

import java.util.List;
import java.util.Scanner;

import clusterability.ClusteringCoefficient;
import clusterability.ComponentClustererBFS;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import model.edge.EdgeInfo;
import model.edge.MarkedEdge;

public class PrettyPrint<V, E> {

	public void printMenu() {
		System.out.println("1 - Da li je mreza klasterabilna?");
		System.out.println("2 - Koliko mreza ima klastera?");
		System.out.println("3 - Koliko mreza ima koalicija?");
		System.out.println("4 - Koliko mreza komponenti ima cvorova?");
		System.out.println("5 - Koliko mreza ima klastera koji nisu koalicije?");
		System.out.println("6 - Koje veze treba izbrisati da bi mreza bila klasterabilna?");
		System.out.println("7 - Informacije o gigantskoj komponenti?");
		System.out.println("8 - Koliko graf ima cvorova?");
		System.out.println("9 - Koliko graf ima pozitivnih linkova?");
		System.out.println("10 - Koliko graf ima negativnih linkova?");
		System.out.println("11 - Koliko graf ima koalicija sa jednim cvorom?");
		System.out.println("12 - Izvoz mreze komponenti kao grafa u gml fajl?");
	}

	public void getResultByChoice(int choice, ComponentClustererBFS<V, E> ccBFS) throws GraphIsClusterableException {
		switch (choice) {
		case 1: {
			if (ccBFS.isClusterable()) 
				System.out.println("Mreza je klasterabilna");
			else 
				System.out.println("Mreza nije klasterabilna");
			break;
		}
		case 2: {
			System.out.println("Mreza ima " + ccBFS.getAllComponents().size() + " klastera");
			break;
		}
		case 3: {
			System.out.println("Mreza ima " + ccBFS.getCoalitions().size() + " koalicija");
			break;
		}
		case 4: {
			System.out.println("Mreza komponenti ima " + ccBFS.getComponentsGraph().getVertexCount() + " cvorova");
			break;
		}
		case 5: {
			System.out.println("Mreza ima " + ccBFS.getNonCoalitions().size() + " klastera koji nisu koalicije");
			break;
		}
		case 6: {
			List<EdgeInfo<E, V>> eil = ccBFS.getNegativeLinks();
			System.out.println("Ukupno treba izbrisati " + eil.size() + " linkova da mreza postane klasterabilna");
			System.out.println("Linkovi koje treba izbrisati da bi mreza bila klasterabilna:");
			for (EdgeInfo<E, V> ei : eil) {
				System.out.println(ei);
			}
			break;
		}
		case 7: {
			UndirectedSparseGraph<V, E> g = ccBFS.getGiantComponent();
			ClusteringCoefficient<V, E> cc = new ClusteringCoefficient<>(g);
			System.out.println("Gigantska komponenta ima " + g.getVertexCount() + " cvorova");
			System.out.println("Prosecan koeficijent klasterisanja u gigantskoj komponenti >> " + cc.averageClusteringCoeficient());
			V node = cc.getNodeWithMaxClusteringCoefficient();
			System.out.println("Cvor sa najvecim koeficijentom klasterisanja u gigantskoj komponenti >> " + node + " --> " + cc.getClusteringCoefficientForNode(node));
			System.out.println("Diameter >> " + DistanceStatistics.diameter(g));
			break;
		}
		case 8: {
			System.out.println("Graf ima " + ccBFS.getOriginalGraphVertexCount() + " cvorova");
			break;
		}
		case 9: {
			System.out.println("Graf ima " + ccBFS.getPositiveLinksCount() + " pozitivnih linkova");
			break;
		}
		case 10: {
			System.out.println("Graf ima " + ccBFS.getNegativeLinksCount() + " negativnih linkova");
			break;
		}
		case 11: {
			System.out.println("Graf ima " + ccBFS.getCoalitionsWithOneVertexCount() + " koalicija sa samo jednim cvorom");
			break;
		}
		case 12: {
			Scanner scanner = new Scanner(System.in); 
			System.out.println("Unesi ime fajla za cuvanje grafa >> ");
			String file = "res/" + scanner.nextLine() + ".gml";
			ccBFS.exportCompGraph(file);
//			scanner.close();
			System.out.println("Graf je sacuvan na lokaciji: " + file);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + choice);
		}
	}
}
