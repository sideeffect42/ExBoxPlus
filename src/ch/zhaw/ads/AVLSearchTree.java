/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 * @author K. Rege - Generics
 */

package ch.zhaw.ads;

public abstract class AVLSearchTree<T extends Comparable<T>>
	implements Tree<T> {

	/** The tree root. */
	protected TreeNode<T> root;
	public TreeNode<T> getRoot() {
		return root;
	}

	/**
	 * Test if the tree is logically empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return size(root);
	}

	private static <T extends Comparable<T>> int size(TreeNode<T> p) {
		if (p == null) {
			return 0;
		} else {
			return (1 + size(p.getLeft()) + size(p.getRight()));
		}
	}

	public int height() {
		return height(root);
	}

	/**
	 * Return the height of node t, or -1, if null.
	 */
	private static <T extends Comparable<T>> int height(TreeNode<T> t) {
		return (t == null ? 0 : t.getHeight());
	}

	/**
	 * Insert into the tree; duplicates are ignored.
	 * @param x the item to insert.
	 */
	public void add(T element) {
		root = insertAt(root, element);
	}

	/**
	 * Internal method to insert into a subtree.
	 * @param x the item to insert.
	 * @param t the node that roots the tree.
	 * @return the new root.
	 */
	protected abstract TreeNode<T> insertAt(TreeNode<T> p, T element);
	// private TreeNode<T> insertAt(TreeNode<T> p, T element) {
	// 	if (p == null) {
	// 		p = new TreeNode<T>(element);
	// 	} else {
	// 		int c = element.compareTo((T)p.element);
	// 		if (c <= 0) {
	// 			p.left = insertAt(p.left, element);
	// 			if (height(p.left) - height(p.right) == 2) {
	// 				if (height(p.left.left) > height(p.left.right)) {
	// 					// TODO: implement
	// 				} else {
	// 					// TODO: implement
	// 				}
	// 			}
	// 		} else if (c > 0) {
	// 			p.right = insertAt(p.right, element);
	// 			if (height(p.right) - height(p.left) == 2) {
	// 				if (height(p.right.right) > height(p.right.left)) {
	// 					// TODO: implement
	// 				} else {
	// 					// TODO: implement
	// 				}
	// 			}
	// 		}
	// 	}
	// 	p.height = (Math.max(height(p.left), height(p.right)) + 1);
	// 	return p;
	// }

	/**
	 * Remove from the tree. Nothing is done if x is not found.
	 * @param x the item to remove.
	 */
	public T remove(T element) {
		throw new UnsupportedOperationException();
	}

	public T removeLast() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Rotate binary tree node with left child.
	 * For AVL trees, this is a single rotation for case 1.
	 * Update heights, then return new root.
	 */
	private static <T extends Comparable<T>>
		TreeNode<T> rotateR(TreeNode<T> k2) {

		TreeNode<T> k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight((Math.max(height(k2.getLeft()),
							   height(k2.getRight())) + 1));
		k1.setHeight((Math.max(height(k1.getLeft()), k2.getHeight()) + 1));
		return k1;
	}

	/**
	 * Rotate binary tree node with right child.
	 * For AVL trees, this is a single rotation for case 4.
	 * Update heights, then return new root.
	 */
	private static <T extends Comparable<T>>
		TreeNode<T> rotateL(TreeNode<T> k1) {

		TreeNode<T> k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);
		k1.setHeight((Math.max(height(k1.getLeft()),
							   height(k1.getRight())) + 1));
		k2.setHeight((Math.max(height(k2.getRight()), k1.getHeight()) + 1));
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child
	 * with its right child; then node k3 with new left child.
	 * For AVL trees, this is a double rotation for case 2.
	 * Update heights, then return new root.
	 */
	private static <T extends Comparable<T>>
		TreeNode<T> rotateLR(TreeNode<T> k3) {

		k3.setLeft(rotateL(k3.getLeft()));
		return rotateR(k3);
	}

	/**
	 * Double rotate binary tree node: first right child
	 * with its left child; then node k1 with new right child.
	 * For AVL trees, this is a double rotation for case 3.
	 * Update heights, then return new root.
	 */
	private static <T extends Comparable<T>>
		TreeNode<T> rotateRL(TreeNode<T> k1) {

		k1.setRight(rotateR(k1.getRight()));
		return rotateL(k1);
	}

	public abstract Traversal<T> traversal();

}
