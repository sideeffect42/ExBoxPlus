package ch.zhaw.ads;

class TreeNode<T extends Comparable<T>> {
	T element;
	TreeNode left, right;
	int height;
	
	TreeNode(T element){
		this.element = element;
	}
	TreeNode(T element, TreeNode left, TreeNode right){
		this(element); this.left = left; this.right = right;
	}
	
	T getValue(){return element;}
	TreeNode getLeft(){return left;}
	TreeNode getRight(){return right;}
}