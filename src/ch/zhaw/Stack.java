package ch.zhaw;

public interface Stack<E> {
	public void push(E x);
	public E pop();
	public boolean isEmpty();
	public E peek();
	public void removeAll();
	public boolean isFull();
}
