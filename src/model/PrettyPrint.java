package model;

import java.util.List;

import clusterability.ComponentClustererBFS;
import exceptions.GraphIsClusterableException;

public class PrettyPrint<V, E> {

	public void printMenu() {

		System.out.println("1 - Da li je mreza klasterabilna?");
		System.out.println("2 - Koliko mreza ima klastera?");
		System.out.println("3 - Koliko mreza ima koalicija?");
		System.out.println("4 - Koliko mreza komponenti ima cvorova?");
		System.out.println("5 - Koliko mreza ima klastera koji nisu koalicije?");
		System.out.println("6 - Koje veze treba izbrisati da bi mreza bila klasterabilna?");
		System.out.println("7 - Koliko cvorova ima gigantska komponenta?");
		System.out.println("8 - ");
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
			System.out.println("Gigantska komponenta ima " + ccBFS.getGiantComponent().getVertexCount() + " cvorova");
			break;
		}
		case 8: {

			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + choice);
		}
	}
}
