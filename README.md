# Java-ex4


=============================
=      File description     =
=============================
1. README - this file.
2. Node.java - a class which represents a single node in the tree.
3. AvlTree.java - a class which represents an Avl-Tree.

=============================
=          Design           =
=============================
The program describes an avl tree. The AvlTree is our main class and it implements the interface
iterable. The second class is Node which represents a single node in the tree. Every node has a references
to its sons, and even a pointer to its parent (the parent pointer is needed to the delete method, and it
makes other method simpler). In the AvlTree class there's an iterator method, which I decide to implement
with an anonymous class, because I only needed that class once, and the code is much more readable this way.

=============================
=  Implementation details   =
=============================
In order to make the size() method run in O(1), I added a field that increasing by 1 after a new value was
added, and the opposite in the delete() method. I chose this way because it is not making the running time
of the other method worse, because it is only costs O(1). However, if I chose to travel through all the nodes
and count them one by one it would cost O(n).

Both add() and delete() have their own helper method in my implementation.

In the add() method I checked first if the tree is empty. If it was I let the root be the newValue. If not,
I called a helper method, which added it just like a BST-tree. Then I search if there is a violation in
one of the new node ancestors, and reBalance it if was needed with the helper method addReBalance().

In the delete() method first the helper method is called, which delete the desired node considering 3 cases:
1. If the node is a leaf, we can simply delete it.
2. If the node has only one child (left / right), we first set the child to be the child of its
   grand-father, and then we delete the node.
3. If the node has 2 children, we first get his successor. Now, if the successor is a leaf, we take the
   right and left children of node and the father of node and set them as successor's children and father, and
   then we can delete the node. If the successor has a right children, we first set the child to be the son
   of its grand-father, and then we continue as if the successor has no children.
after the delete helper is done, it returns the node that might has a violation. We then check from that
node through the root if there's a node that has a violation, and reBalance the tree where there is.

=============================
=    Answers to questions   =
=============================
5.1---Analyzing the Avl-Tree---

A1. The series is (in this order): (30, 35, 25, 45, 31, 28, 20, 50, 29, 21, 19, 22).

5.2---The complexity of constructing an Avl-Tree---

A1. Since add() has a time complexity of O(logn), The time complexity for inserting n elements in a row is:
    log(0) + log(1) + log(2) + ... + log(n - 1) <= log(n - 1) + ... + log(n - 1) = n * log(n - 1)
    <= n * logn   ==> the time complexity for AvlTree(int[] data) is O(nlogn).

A2. The best running time is O(n). If we take an ascending sorted array, then use the median algorithm:

def buildAvlMedian(int[] a, int start ,int end):
1. n = end - start + 1
2. if (n <= 0){return null}
3. r = 1 + (n - 1) / 2
4. T = new AvlTree()
5. if (n > 1) do:
6.      T.left = buildAvlMedian(a, start, r - 1)
7.      T.right = buildAvlMedian(a, r + 1, end)
8. return T

The run-tine complexity of this algorithm is T(n) = 2T(n / 2) + O(1).
By using the master theorem we conclude that the running time is O(n). (it's even theta(n) to be exact)

5.3---The complexity of copying an Avl-Tree---

A1. the next() method of the Iterator takes O(1), since getting data from an array requires only O(1).
    hasNext() takes also O(1). Building the iterator itself takes O(n * logn), because successor() and
    findMin() take O(logn) each, and we iterating for n times. Adding n elements to the new tree is
    O(n * logn) as explained before. All in all it takes O(n * logn).

A2. If we use the median algorithm that has been explained in question 5.2.2 for a sorted array, we would
    get a running time of O(n).

5.4---The complexity of calculating the minimal number of nodes in an Avl-Tree of a given height---

A1. There are 8 simple operations outside of the loop, and 3 simple operations inside the loop.
    The loop makes h - 1 rounds, so the time complexity function is T(h) = 3*(h - 1) + 8
    ==> T(h) = 3*h + 5 ==> T(h) = O(h). The time complexity of my implementation is therefore O(h).

A2. There exists a closed form expression for the n'th element in the minimal nodes series (just like there
    exists such a formula for the fibonacci series). Since all the arithmetic operation are done in O(1),
    we can achieve an algorithm to do it in O(1).
