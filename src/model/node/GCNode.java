package model.node;

import java.util.Objects;

public class GCNode {
	
	private int id;
	private String label;
	private int nodes;
	
	public GCNode(int id, String label, int nodes) {
		super();
		this.id = id;
		this.label = label;
		this.nodes = nodes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, label, nodes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GCNode other = (GCNode) obj;
		return id == other.id && Objects.equals(label, other.label) && nodes == other.nodes;
	}

	@Override
	public String toString() {
		return "[id: " + this.id + ", label: " + this.label + ", nodes: " + this.nodes + "]";
	}
}
