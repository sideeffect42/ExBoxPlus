package ch.zhaw.ads;

public class TreeNode<T extends Comparable<T>> {
	protected T element;
	protected TreeNode<T> left, right;
	protected int height = -1;

	public TreeNode(T element) {
		this.element = element;
	}

	public TreeNode(T element, TreeNode<T> left, TreeNode<T> right) {
		this(element);
		this.left = left;
		this.right = right;
	}

	public T getValue() {
		return this.element;
	}

	public TreeNode<T> getLeft() {
		return this.left;
	}

	public void setLeft(TreeNode<T> left) {
		this.left = left;
		this.height = -1;
		// FIXME: also invalidate height of all parents
	}

	public TreeNode<T> getRight() {
		return this.right;
	}

	public void setRight(TreeNode<T> right) {
		this.right = right;
		this.height = -1;
		// FIXME: also invalidate height of all parents
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
