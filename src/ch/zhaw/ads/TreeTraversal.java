package ch.zhaw.ads;

public abstract class TreeTraversal<T extends Comparable<T>> implements Traversal<T> {

	protected TreeNode<T> root;

	public TreeTraversal(TreeNode<T> root) {
		this.root = root;
	}

	public abstract void inorder(Visitor<T> vis);

	public abstract void preorder(Visitor<T> vis);

	public abstract void postorder(Visitor<T> vis);

}
