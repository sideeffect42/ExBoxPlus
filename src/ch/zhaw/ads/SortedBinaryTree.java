package ch.zhaw.ads;

public abstract class SortedBinaryTree<T extends Comparable<T>>
	implements Tree<T> {

	protected TreeNode<T> root;
	private T removed;

	private TreeNode<T> insertAt(TreeNode<T> node, T x) {
		if (node == null) {
			return new TreeNode<T>(x);
		} else {
			if (x.compareTo(node.getValue()) <= 0)
				node.setLeft(insertAt(node.getLeft(), x));
			else
				node.setRight(insertAt(node.getRight(), x));
			return node;
		}
	}

	public void add(T x) {
		root = insertAt(root, x);
	}

	// find node to replace
	TreeNode<T> rep;
	private TreeNode<T> findRepAt(TreeNode<T> node) {
		if (node.getRight() != null) {
			node.setRight(findRepAt(node.getRight()));
			return node;
		} else {
			this.rep = node;
			return node.getLeft();
		}
	}

	// remove node
	private TreeNode<T> removeAt(TreeNode<T> node, T x) {
		if (node == null) {
			return null;
		} else {
			if (x.compareTo(node.getValue()) == 0) {
				// found
				this.removed = node.getValue();
				if (node.getLeft() == null) { return node.getRight(); }
				else if (node.getRight() == null) { return node.getLeft(); }
				else {
					node.setLeft(findRepAt(node.getLeft()));
					this.rep.setLeft(node.getLeft());
					this.rep.setRight(node.getRight());
					return this.rep;
				}
			} else if (x.compareTo(node.getValue()) <= 0) {
				// search left
				node.setLeft(removeAt(node.getLeft(), x));
			} else {
				// search right
				node.setRight(removeAt(node.getRight(), x));
			}

			return node;
		}
	}

	public T remove(T x) {
		this.removed = null;
		this.root = removeAt(this.root, x);
		return this.removed;
	}

	public T removeLast() {
		if (this.root.getRight() != null) {
			this.root.setRight(findRepAt(this.root.getRight()));
		} else {
			this.rep = this.root;
			this.root = root.getLeft();
		}
		return this.rep.getValue();
	}

	public boolean isEmpty() {
		return this.root == null;
	}

	public abstract Traversal<T> traversal();

}
