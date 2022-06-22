package model.edge;

public enum Mark {
	POSITIVE(1), NEGATIVE(-1);
	
	private int value;
	
	private Mark(int value) {
		this.value = value;
	}
	
	public static Mark getMark(int val) {
		for (Mark m : values()) {
			if (m.value == val)
				return m;
		}
		throw new IllegalArgumentException("Wrong mark");
	}
}
