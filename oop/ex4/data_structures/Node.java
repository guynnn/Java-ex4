package oop.ex4.data_structures;

/**
 * This class represents a node in a BST-tree. Can be used for Avl-tree as well
 */
public class Node{

    // ######data members######

    // the data the node holds
    private int value;
    // the right son of the node
    private Node right;
    // the left son of the node
    private Node left;
    // the parent of the node
    private Node parent;
    // the depth of a node if it is not in the given root
    private static final int NOT_IN_TREE = -1;

    // ######Constructor######

    /**
     * Builds a new node
     * @param value the data the node holds.
     */
    public Node(int value){
        this.value = value;
    }

    // ######Methods######


    /**
     * @return the right son of the node
     */
    public Node getRight(){
        return right;
    }

    /**
     * Setting a new right child
     * @param child the node to become right child
     */
    public void setRight(Node child){
        right = child;
        if (child != null)
            child.setParent(this);
    }

    /**
     * @return the left son of the node
     */
    public Node getLeft(){
        return left;
    }

    /**
     * Setting a new left child
     * @param child the node to become left child
     */
    public void setLeft(Node child){
        left = child;
        if (child != null) {
            child.setParent(this);
        }
    }

    /**
     * @return the parent of the node
     */
    public Node getParent(){
        return parent;
    }

    /**
     * Setting a new parent to the node
     * @param father the new parent
     */
    public void setParent(Node father){
        parent = father;
    }

    /*
     * returns the height of the right sub-tree
     */
    private int rightHeight(){
        return height(this.right);
    }

    /*
     * returns the height of the left sub-tree
     */
    private int leftHeight(){
        return height(this.left);
    }

    /**
     * @return the violation the node has.
     */
    public int violation(){
        return leftHeight() - rightHeight();
    }

    /**
     * @param root the root of the tree.
     * @return the height of the tree.
     */
    public int height(Node root){
        if (root == null) {
            // the tree is empty, setting it to be -1 because a tree with only one node has a height of 0.
            return -1;
        }
        return Math.max(height(root.left), height(root.right)) + 1;
    }

    /**
     * checking if searchVal is in the tree where node is the root.
     * @param searchVal the value to search for.
     * @return the depth of the node if searchVal is in the sub-tree of node, -1 otherwise.
     */
    public int contains(int searchVal){
        Node node = this;
        for (int i = 0; node != null; i++){
            if (searchVal == node.value) {
                return i;
            }
            if (searchVal > node.value) {
                node = node.right;
            }
            else if (searchVal < node.value) {
                node = node.left;
            }
        }
        return NOT_IN_TREE;
    }

    /**
     * @return the Node with the minimum value in a tree where node is its root.
     */
    public Node findMin(){
        return findMinHelper(this);
    }

    /*
     * Helper recursive method for FindMin()
     */
    private Node findMinHelper(Node node){
        if (node == null || node.left == null) {
            return node;
        }
        return findMinHelper(node.left);
    }

    /*
     * Helper method for intToNode()
     */
    private Node intToNodeHelper(int searchVal, Node node){
        if (node == null) {
            return null;
        }
        if (searchVal == node.value) {
            return node;
        }
        if (searchVal > node.value) {
            return intToNodeHelper(searchVal, node.right);
        }
        else { // searchVal < node.value
            return intToNodeHelper(searchVal, node.left);
        }
    }

    /**
     * @param searchVal the value to search.
     * @return the node that as the same value as searchVal, in the tree where node is its root. If no such
     * node exist, returns null.
     */
    public Node intToNode(int searchVal){
        return intToNodeHelper(searchVal, this);
    }

    /**
     * @return the successor of node. If the successor does not exists, returns null.
     */
    public Node successor(){
        return successorHelper(this);
    }

    /*
     * Helper method for successor()
     */
    private Node successorHelper(Node node){
        if (node == null) {
            return null;
        }
        if (node.right != null) {
            return findMinHelper(node.right);
        }
        Node father = node.parent;
        Node child = node;
        while (father != null && child == father.right){
            child = father;
            father = father.parent;
        }
        return father;
    }

    /**
     * @return true if node is a leaf, false otherwise.
     */
    public boolean isLeaf(){
        return right == null & left == null;
    }

    /**
     * @return the value of the node.
     */
    public int getValue(){
        return value;
    }
}
