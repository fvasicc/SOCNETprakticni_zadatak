package clusterability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import interfaces.ComponentClustererU;
import model.Mark;

public class ComponentClustererBFS<V, E> implements ComponentClustererU<V, E>{
	
	private UndirectedSparseGraph<V, E> graph;
	private Transformer<E, Mark> markTransformer;
	
	private List<UndirectedSparseGraph<V, E>> clustersWithNegativeLink = new ArrayList<>();;
	private List<UndirectedSparseGraph<V, E>> clustersWithoutNegtiveLink = new ArrayList<>();;
	private List<UndirectedSparseGraph<V, E>> components;
	
	private HashSet<V> visited;
	
	public ComponentClustererBFS(UndirectedSparseGraph<V, E> graph, Transformer<E, Mark> markTransformer) {
		this.graph = graph;
		this.markTransformer = markTransformer;
		
		this.components = new ArrayList<>();
		this.visited = new HashSet<>();
		
		identifyComponents();
		
	}
	
	private void identifyComponents() {
		
		for (V vertex : this.graph.getVertices()) {
			if (!this.visited.contains(vertex))
				identifyComponent(vertex);
		}	
		
		Collections.sort(this.components, (c1, c2) -> c2.getVertexCount() - c1.getVertexCount());		
	}

	private void identifyNegativeLink(UndirectedSparseGraph<V, E> component) {
		List<V> nodes = new ArrayList<>(component.getVertices());
		boolean hasNegativeLink = false;
		
		for (int i = 0; i < nodes.size() - 1; i++) {
			for (int j = 1; j < nodes.size(); j++) {
				V node1 = nodes.get(i);
				V node2 = nodes.get(j);
				
				E link = this.graph.findEdge(node1, node2);
				
				if (link != null) {
					
					Mark markedLink = markTransformer.transform(link);
					
					if (markedLink == Mark.NEGATIVE) {
						clustersWithNegativeLink.add(component);
						hasNegativeLink = true;
					}
				}
			}
		}
		
		if (!hasNegativeLink) 
			clustersWithoutNegtiveLink.add(component);
	}

	private void identifyComponent(V startNode) {
		LinkedList<V> queue = new LinkedList<>();
		queue.add(startNode);
		UndirectedSparseGraph<V, E> component = 
				new UndirectedSparseGraph<V, E>();
		component.addVertex(startNode);
		
		while (!queue.isEmpty()) {
			V current = queue.removeFirst();
			
			for (V neighbor : this.graph.getNeighbors(current)) {
				
				// ako je link u originalnom grafu preskace se
				if (markTransformer.transform(this.graph.findEdge(current, neighbor)) == Mark.NEGATIVE)
					continue;
				
				if (!this.visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.addLast(neighbor);
					component.addVertex(neighbor);
				}
				if (component.findEdge(current, neighbor) == null) {
					E link = this.graph.findEdge(current, neighbor);
					component.addEdge(link, current, neighbor);
				}
			}
		}
		
		identifyNegativeLink(component);
		
		components.add(component);
	}
	
	@Override
	public List<UndirectedSparseGraph<V, E>> getAllComponents() {
		return components;
	}
	
	@Override
	public UndirectedSparseGraph<V, E> getGiantComponent() {
		return components.get(0);
	}
	
	public List<UndirectedSparseGraph<V, E>> getCoalitions() {
		return clustersWithoutNegtiveLink;
	}
	
	public List<UndirectedSparseGraph<V, E>> getNonCoalitions() {
		return clustersWithNegativeLink;
	}
	
	public boolean isClusterable() {
		return clustersWithNegativeLink.size() == 0;
	}
}
