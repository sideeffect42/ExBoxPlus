package ch.zhaw;

import java.util.List;
import java.util.LinkedList;
import ch.zhaw.Stack;

/** Implementation des Abstrakten
	Datentyp (ADT) Stack mit
	Hilfe der vordefinierten
	Klasse java.util.LinkedList
*/

public class StackLinkedList<E> implements Stack<E> {
	private List<E> list;

	public StackLinkedList() {
		removeAll();
	}

	public void push(E x) {
		list.add(0, x);
	}

	public E pop() {
		if (isEmpty()) {
			return null;
		};
		return list.remove(0);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public E peek() {
		if (isEmpty()) {
			return null;
		};
		return list.get(0);
	}

	public void removeAll() {
		list = new LinkedList<E>();
	}

	public boolean isFull() {
		return false;
	}
}
