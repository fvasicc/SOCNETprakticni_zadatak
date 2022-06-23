package clusterability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import exceptions.GraphIsClusterableException;
import interfaces.ComponentClustererU;
import interfaces.MarkedGraphMetricsU;
import model.edge.EdgeInfo;
import model.edge.Mark;
import model.node.GCNode;
import tests.NetworkWriter;


public class ComponentClustererBFS<V, E> implements ComponentClustererU<V, E>, MarkedGraphMetricsU<V, E> {
	
	private UndirectedSparseGraph<V, E> graph;
	private Transformer<E, Mark> markTransformer;
	
	private List<UndirectedSparseGraph<V, E>> clustersWithNegativeLink = new ArrayList<>();
	private List<UndirectedSparseGraph<V, E>> clustersWithoutNegativeLink = new ArrayList<>();
	private List<UndirectedSparseGraph<V, E>> components;
	List<EdgeInfo<E, V>> negativeEdges = new ArrayList<>();
	
	private UndirectedSparseGraph<UndirectedSparseGraph<V, E>, E> graphComponents = new UndirectedSparseGraph<>();
	
	private UndirectedSparseGraph<GCNode, E> clusterNetwork = new UndirectedSparseGraph<>();
	
	private HashSet<V> visited = null;
	
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
		
		generateClusterGraph();
		
		Collections.sort(this.components, (c1, c2) -> c2.getVertexCount() - c1.getVertexCount());		
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
	
	private void identifyNegativeLink(UndirectedSparseGraph<V, E> component) {
		List<V> nodes = new ArrayList<>(component.getVertices());
		boolean hasNegativeLink = false;
		
		for (int i = 0; i < nodes.size() - 1; i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				V node1 = nodes.get(i);
				V node2 = nodes.get(j);
				
				E link = this.graph.findEdge(node1, node2);
				
				if (link != null) {
					
					Mark markedLink = markTransformer.transform(link);
					
					if (markedLink == Mark.NEGATIVE) {
						if (component.findEdge(node1, node2) == null)
							component.addEdge(link, node1, node2);
						if (!hasNegativeLink)
							clustersWithNegativeLink.add(component);
						this.negativeEdges.add(new EdgeInfo<E,V>(link, node1, node2));
						hasNegativeLink = true;
					}
				}
			}
		}
		
		if (!hasNegativeLink) 
			clustersWithoutNegativeLink.add(component);
	}
	
	private void generateClusterGraph() {
		for (int i = 0; i < this.components.size(); i++) {
			UndirectedSparseGraph<V, E> comp1 = this.components.get(i);
			this.graphComponents.addVertex(comp1);
			GCNode gcn1 = new GCNode(i, "CL_" + i, comp1.getVertexCount(), this.clustersWithoutNegativeLink.contains(comp1));
			this.clusterNetwork.addVertex(gcn1);
			for (int j = i + 1; j < this.components.size(); j++) {
				UndirectedSparseGraph<V, E> comp2 = this.components.get(j);
				E link = conectedComponents(comp1, comp2);
				if (link != null) {
					this.graphComponents.addEdge(link, comp1, comp2);
					this.clusterNetwork.addEdge(link, gcn1, new GCNode(j, "CL_" + j, comp2.getVertexCount(), this.clustersWithoutNegativeLink.contains(comp2)));
				}
			}
		}
	}
	
	private E conectedComponents(UndirectedSparseGraph<V, E> comp1, UndirectedSparseGraph<V, E> comp2) {
		for (V vertex1 : comp1.getVertices()) {
			for (V vertex2 : comp2.getVertices()) {
				E link = this.graph.findEdge(vertex1, vertex2);
				if (link != null)
					return link;
			}
		}
		return null;
	}
	
	public void exportCompGraph(String fileName) {
		new NetworkWriter<V, E>(markTransformer).exportCompGraphGML(this.clusterNetwork, fileName);
	}
	
	public UndirectedSparseGraph<GCNode, E> getClusterNetwork() {		
		return this.clusterNetwork;
	}
	
	public int getCoalitionsWithOneVertexCount() {
		return (int) this.clustersWithoutNegativeLink.stream().filter(c -> c.getVertexCount() == 1).count();
	}

	@Override
	public List<UndirectedSparseGraph<V, E>> getAllComponents() {
		return components;
	}
	
	@Override
	public UndirectedSparseGraph<V, E> getGiantComponent() {
		return components.get(0);
	}
	
	@Override
	public UndirectedSparseGraph<UndirectedSparseGraph<V, E>, E> getComponentsGraph() {
		return this.graphComponents;
	}
	
	@Override
	public List<UndirectedSparseGraph<V, E>> getCoalitions() {
		return clustersWithoutNegativeLink;
	}
	
	@Override
	public List<UndirectedSparseGraph<V, E>> getNonCoalitions() {
		return clustersWithNegativeLink;
	}
	
	@Override
	public boolean isClusterable() {
		return clustersWithNegativeLink.size() == 0;
	}
	
	@Override
	public List<EdgeInfo<E, V>> getNegativeLinks() throws GraphIsClusterableException {
		if (this.isClusterable()) 
			throw new GraphIsClusterableException("The graph is clustered. Components have only positive links!!!");
		
		return this.negativeEdges;
	}

	@Override
	public int getPositiveLinksCount() {
		return (int) this.graph.getEdges().stream().filter(e -> markTransformer.transform(e) == Mark.POSITIVE).count();
	}

	@Override
	public int getNegativeLinksCount() {
		return (int) this.graph.getEdges().stream().filter(e -> markTransformer.transform(e) == Mark.NEGATIVE).count();
	}

	@Override
	public int getOriginalGraphVertexCount() {
		return this.graph.getVertexCount();
	}
}