package ch.zhaw;

import java.util.*;
import ch.zhaw.Stack;

/** Implementation des Abstrakten
	Datentyp (ADT) Stack mit
	Hilfe der vordefinierten
	Klasse java.util.LinkedList
*/

public class StackLinkedList implements Stack {
	private List list;

	public StackLinkedList() {
		removeAll();
	}

	public void push(Object x) {
		list.add(0, x);
	}

	public Object pop() {
		if (isEmpty()) {
			return null;
		};
		return list.remove(0);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Object peek() {
		if (isEmpty()) {
			return null;
		};
		return list.get(0);
	}

	public void removeAll() {
		list = new LinkedList();
	}

	public boolean isFull() {
		return false;
	}
}
