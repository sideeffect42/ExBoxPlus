package ch.zhaw.ads;

public abstract class DijkstraNode<E> extends Node<E>
	implements Comparable<DijkstraNode<E>> {

	boolean mark;
	DijkstraNode<E> prev;
	double dist;

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public void setMark(boolean m) {
		mark = m;
	}

	public boolean getMark() {
		return mark;
	}

	public void setPrev(DijkstraNode<E> p) {
		prev = p;
	}

	public DijkstraNode<E> getPrev() {
		return prev;
	}

	public abstract int compareTo(DijkstraNode<E> n);
}
