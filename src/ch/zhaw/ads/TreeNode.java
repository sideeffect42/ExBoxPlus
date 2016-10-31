package ch.zhaw.ads;

public class TreeNode<T extends Comparable<T>> {
	T element;
	TreeNode<T> left, right;
	int height;

	TreeNode(T element) {
		this.element = element;
	}

	TreeNode(T element, TreeNode<T> left, TreeNode<T> right) {
		this(element); this.left = left; this.right = right;
	}

	public T getValue() {
		return element;
	}

	public TreeNode<T> getLeft() {
		return left;
	}

	public TreeNode<T> getRight() {
		return right;
	}
}
