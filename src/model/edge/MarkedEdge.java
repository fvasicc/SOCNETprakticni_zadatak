package model.edge;

import interfaces.Marked;

public class MarkedEdge implements Marked<Mark>{
	
	private Mark mark;
	
	public MarkedEdge(Mark mark) {
		this.mark = mark;
	}

	@Override
	public Mark getMark() {
		return this.mark;
	}
	
	@Override
	public void setNewMark(Mark newMark) {
		this.mark = newMark;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (mark == Mark.NEGATIVE)
			return "-";
		else if (mark == Mark.POSITIVE)
			return "+";
		else 
			return "";
	}
}
