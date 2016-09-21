package ch.zhaw;

import java.util.*;

public interface Stack {
	public void push(Object x);
	public Object pop();
	public boolean isEmpty();
	public Object peek();
	public void removeAll();
	public boolean isFull();
}
