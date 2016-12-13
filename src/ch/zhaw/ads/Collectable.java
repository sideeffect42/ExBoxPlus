package ch.zhaw.ads;

public interface Collectable {
	public void setMark(boolean b);
	public boolean isMarked();
	public void finalize();
}
