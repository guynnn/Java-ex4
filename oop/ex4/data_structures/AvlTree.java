package oop.ex4.data_structures;

import java.util.Iterator;

/**
 * This class represents an Avl-Tree
 */
public class AvlTree implements Iterable<Integer> {

    // The root of the tree
    private Node root;
    // Numbers of nodes in the tree
    private int nodesNum;
    // The violation a node has if it has Right-Right or Right-Left violation
    private static final int RIGHT_VIOLATION = -2;
    // The violation a node has if it has Left-Right or Left-Left violation
    private static final int LEFT_VIOLATION = 2;
    // the depth of a node if it is not in the tree
    private static final int NOT_IN_TREE = -1;


    //     ######constructors######

    /**
     * Builds an empty tree
     */
    public AvlTree(){
    }

    /**
     * Builds a tree that contains the number in data
     * @param data numbers to add into the tree
     */
    public AvlTree(int[] data){
        if (data == null){
            return;
        }
        for (int node : data) {
            add(node);
        }
    }

    /**
     * Deep-copy constructor
     * @param tree the tree to deep-copy
     */
    public AvlTree(AvlTree tree){
        Iterator<Integer> iterator = tree.iterator();
        while (iterator.hasNext()){
            add(iterator.next());
        }
    }


    //     ######Methods######

    /**
     * Adding new value to the tree
     * @param newValue the new number to be added
     * @return true if the newValue was successfully, false otherwise
     */
    public boolean add(int newValue){
        if (contains(newValue) != -1) {
            // Already in the tree - duplicates are not allowed
            return false;
        }
        nodesNum++;
        Node toAdd = new Node(newValue);
        // If the tree is empty, making newValue to be the root
        if (root == null){
            root = toAdd;
            return true;
        }
        // adding the new node
        addHelper(toAdd, root);
        //re-balancing:
        Node current = toAdd.getParent();
        // Finding the first node that is unbalanced
        while (current != null && (current.violation() != LEFT_VIOLATION &&
                                   current.violation() != RIGHT_VIOLATION)){
            current = current.getParent();
        }
        addReBalance(current);
        return true;
    }

    /*
     * Adding toAdd to the correct position in the tree
     */
    private void addHelper(Node toAdd, Node currentRoot){
        if (toAdd.getValue() > currentRoot.getValue()) {
            if (currentRoot.getRight() == null) {
                currentRoot.setRight(toAdd);
            }
            addHelper(toAdd, currentRoot.getRight());
        }
        if (toAdd.getValue() < currentRoot.getValue()) {
            if (currentRoot.getLeft() == null) {
                currentRoot.setLeft(toAdd);
            }
            addHelper(toAdd, currentRoot.getLeft());
        }
    }

    /**
     * Check whether the tree contains the given input value.
     * @param searchVal the value to search for.
     * @return the depth of the node (0 for the root) with the given value if it was found in the tree, −1
     * otherwise.
     */
    public int contains(int searchVal){
        if (root == null) {
            return NOT_IN_TREE;
        }
        return root.contains(searchVal);
    }

    /**
     * Removes the node with the given value from the tree, if it exists.
     * @param toDelete the value to remove from the tree.
     * @return true if the given value was found and deleted, false otherwise.
     */
    public boolean delete(int toDelete){
        if (contains(toDelete) == NOT_IN_TREE) {
            return false;
        }
        // deleting the desired node, and getting the node that might has a violation
        Node current = deleteHelper(toDelete);
        // re-balancing:
        while (current != null){
            if (current.violation() == LEFT_VIOLATION || current.violation() == RIGHT_VIOLATION) {
                // reBalancing, and getting the next node that might has violation
                current = deleteReBalance(current);
            }
            else{
                current = current.getParent();
            }
        }
        nodesNum--;
        return true;
    }

    /**
     * Calculates the minimum number of nodes in an AVL tree of height h.
     * @param h the height of the tree (a non−negative number) in question.
     * @return the minimum number of nodes in an AVL tree of the given height.
     */
    public static int findMinNodes(int h){
        if (h == 0) {
            return 1;
        }
        if (h == 1) {
            return 2;
        }
        int sum = 0;
        int current = 1, next = 2;
        for (int i = 2; i <= h; i++){
            // Very similar to Fibonacci recursion
            sum = current + next + 1;
            current = next;
            next = sum;
        }
        return sum;
    }

    /*
     * This method rotating the given node left in order to make the tree balance
     */
    private void leftRotate(Node node){
        Node y = node.getRight();
        node.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        }
        else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(y);
        }
        else {
            node.getParent().setRight(y);
        }
        y.setLeft(node);
        node.setParent(y);
    }

    /*
     * This method rotating the given node left in order to make the tree balance
     */
    private void rightRotate(Node node){
        Node y = node.getLeft();
        node.setLeft(y.getRight());
        if (y.getRight() != null) {
            y.getRight().setParent(node);
        }
        y.setParent(node.getParent());
        if (node.getParent() == null) {
            root = y;
        }
        else if (node == node.getParent().getRight()) {
            node.getParent().setRight(y);
        }
        else {
            node.getParent().setLeft(y);
        }
        y.setRight(node);
        node.setParent(y);
    }

    /**
     * @return the number of nodes in the tree
     */
    public int size(){
        return nodesNum;
    }

    /**
     * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
     * ascending order, and does NOT implement the remove() method.
     */
    @Override
    public Iterator<Integer> iterator() {
        final int[] list = computeList();
        return new Iterator<Integer>() {

            // current index the iterator should return
            private int index = 0;

            /**
             * @return true if the iterator has next, false otherwise.
             */
            @Override
            public boolean hasNext() {
                return index < list.length;
            }

            /**
             * @return the next Value of the iterator.
             */
            @Override
            public Integer next() {
                Integer value = list[index];
                index++;
                return value;
            }

            /**
             * Removes are not supported. Throwing UnsupportedOperationException.
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Removals are NOT supported");
            }
        };
    }

    /*
     * returns an array in an ascending order of the tree nodes
     */
    private int[] computeList(){
        int[] list = new int[size()];
        Node current = root.findMin();
        list[0] = current.getValue();
        for (int i = 1; i < list.length; i++){
            current = current.successor();
            list[i] = current.getValue();
        }
        return list;
    }

    /*
     * Rebalancing the tree after a new value has been added
     */
    private void addReBalance(Node current){
        if (current == null) {
            return; // no violation
        }
        if (current.violation() == RIGHT_VIOLATION){
            if (current.getRight().violation() == -1){
                // Right-Right violation:
                leftRotate(current);
            }
            else if (current.getRight().violation() == 1) {
                // Right-Left violation:
                rightRotate(current.getRight());
                leftRotate(current);
            }
        }
        else {
            if (current.getLeft().violation() == -1){
                // Left-Right violation:
                leftRotate(current.getLeft());
                rightRotate(current);
            }
            else if (current.getLeft().violation() == 1) {
                // Left-Left violation:
                rightRotate(current);
            }
        }
    }

    /*
     * Rebalancing the tree after a value has been removed.
     * @return a Node that it and its ancestors might have violation problem
     */
    private Node deleteReBalance(Node current){
        if (current == null) {
            return null; // no violation
        }
        Node newRoot;
        if (current.violation() == RIGHT_VIOLATION) {
            if (current.getRight().violation() == 1) {
                // Right-Left violation:
                newRoot = current.getRight().getLeft();
                rightRotate(current.getRight());
                leftRotate(current);
            }
            else {
                // Right-Right violation:
                newRoot = current.getRight();
                leftRotate(current);
            }
        }
        else{
            if (current.getLeft().violation() == -1){
                // Left-Right violation:
                newRoot = current.getLeft().getRight();
                leftRotate(current.getLeft());
                rightRotate(current);
            }
            else {
                // Left-Left violation:
                newRoot = current.getLeft();
                rightRotate(current);
            }
        }
        return newRoot;
    }

    /*
     * Deleting toDelete, and making sure the BST property still holds afterwards.
     */
    private Node deleteHelper(int toDelete){
        Node delete = root.intToNode(toDelete);
        if (delete.isLeaf()){
            // doesn't have children
            if (delete == root) {
                root = null;
            }
            else if (delete == delete.getParent().getRight()) {
                delete.getParent().setRight(null);
            }
            else {
                delete.getParent().setLeft(null);
            }
        }
        else if (delete.getRight() == null){ // has only a left child
            if (delete == root) {
                root = delete.getLeft();
            }
            else if (delete == delete.getParent().getRight()) {
                delete.getParent().setRight(delete.getLeft());
            }
            else {
                delete.getParent().setLeft(delete.getLeft());
            }
        }
        else if (delete.getLeft() == null){ // has only a right child
            if (delete == root) {
                root = delete.getRight();
            }
            else if (delete == delete.getParent().getRight()) {
                delete.getParent().setRight(delete.getRight());
            }
            else {
                delete.getParent().setLeft(delete.getRight());
            }
        }
        else{ // have both children
            Node replacement = delete.successor(); // replacement is the minimum in the right sub-tree
            Node nodeToReBalance = replacement.getParent() == delete ? replacement : replacement.getParent();
            if (replacement.getValue() > replacement.getParent().getValue()) {
                replacement.getParent().setRight(replacement.getRight());
            }
            else {
                replacement.getParent().setLeft(replacement.getRight());
            }
            replacement.setParent(null);
            if (delete.getParent() == null) { // the deleted node is actually the root
                root = replacement;
            }
            else if (replacement.getValue() > delete.getParent().getValue()) {
                delete.getParent().setRight(replacement);
            }
            else {
                delete.getParent().setLeft(replacement);
            }
            replacement.setLeft(delete.getLeft());
            replacement.setRight(delete.getRight());
            return nodeToReBalance;
        }
        return delete.getParent(); // Node to reBalance in any case but when the node has both children
    }
}
