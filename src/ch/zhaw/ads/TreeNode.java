package ch.zhaw.ads;

public class TreeNode<T extends Comparable<T>> {
	private T element;
	private TreeNode<T> left, right;
	private int height;

	public TreeNode(T element) {
		this.element = element;
	}

	public TreeNode(T element, TreeNode<T> left, TreeNode<T> right) {
		this(element); this.left = left; this.right = right;
	}

	public T getValue() {
		return this.element;
	}

	public TreeNode<T> getLeft() {
		return this.left;
	}

	public void setLeft(TreeNode<T> left) {
		this.left = left;
	}

	public TreeNode<T> getRight() {
		return this.right;
	}

	public void setRight(TreeNode<T> right) {
		this.right = right;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
