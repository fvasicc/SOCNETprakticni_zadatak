package model.edge;

public class EdgeInfo<E, V>{
	
	private E link;
	private V src;
	private V dest;
	
	public EdgeInfo(E edge, V node1, V node2) {
		this.link = edge;
		this.src = node1;
		this.dest = node2;
	}
		
	public E getLink() {
		return link;
	}

	public V getSrc() {
		return src;
	}

	public V getDest() {
		return dest;
	}

	@Override
	public String toString() {
		return link.toString() + "["+ src.toString() + ", " + dest.toString() + "]";
	}
}
