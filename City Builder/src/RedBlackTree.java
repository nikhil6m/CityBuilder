public class RedBlackTree implements IRedBlackTree{
	private BuildingPropertiesNode nil = new BuildingPropertiesNode();
	private BuildingPropertiesNode root = nil;
	
	private boolean newLine = false;
	private boolean commaAppearance = false;

	public RedBlackTree() {
		root.left = nil;
		root.right = nil;
		root.parent = nil;
	}
	
	// Performs LeftRotate over the node BuildingPropertiesNode x
	public void leftRotate(BuildingPropertiesNode x) {
		// leftRotateFixup method updates the numLeft and numRight values
		leftRotateFixup(x);
		// Perform the left rotate as described in the algorithm
		// in the course text.
		BuildingPropertiesNode y;
		y = x.right;
		x.right = y.left;
		// Check for existence of y.left and make pointer changes
		if (!isNil(y.left))
			y.left.parent = x;
		y.parent = x.parent;
		// x's parent is nul
		if (isNil(x.parent))
			root = y;
		// x is the left child of it's parent
		else if (x.parent.left == x)
			x.parent.left = y;
		// x is the right child of it's parent.
		else
			x.parent.right = y;
		// Finish of the leftRotate
		y.left = x;
		x.parent = y;
	}

	private void leftRotateFixup(BuildingPropertiesNode x) {
		// Case 1: Only x, x.right and x.right.right always are not nil.
		if (isNil(x.left) && isNil(x.right.left)) {
			x.numLeft = 0;
			x.numRight = 0;
			x.right.numLeft = 1;
		} 
		// Case 2: x.right.left also exists in addition to Case 1
		else if (isNil(x.left) && !isNil(x.right.left)) {
			x.numLeft = 0;
			x.numRight = 1 + x.right.left.numLeft + x.right.left.numRight;
			x.right.numLeft = 2 + x.right.left.numLeft + x.right.left.numRight;
		}
		// Case 3: x.left also exists in addition to Case 1
		else if (!isNil(x.left) && isNil(x.right.left)) {
			x.numRight = 0;
			x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;

		}
		// Case 4: x.left and x.right.left both exist in addtion to Case 1
		else {
			x.numRight = 1 + x.right.left.numLeft + x.right.left.numRight;
			x.right.numLeft = 3 + x.left.numLeft + x.left.numRight + x.right.left.numLeft + x.right.left.numRight;
		}

	}

	private void rightRotate(BuildingPropertiesNode y) {
		rightRotateFixup(y);
		BuildingPropertiesNode x = y.left;
		y.left = x.right;
		// Check for existence of x.right
		if (!isNil(x.right))
			x.right.parent = y;
		x.parent = y.parent;

		// y.parent is nil
		if (isNil(y.parent))
			root = x;

		// y is a right child of it's parent.
		else if (y.parent.right == y)
			y.parent.right = x;

		 // y is a left child of it's parent.
		else
			y.parent.left = x;
		x.right = y;

		y.parent = x;

	}

	// y is the node around which the righRotate is to be performed.
	// Updates the numLeft and numRight values affected by the rotate
	private void rightRotateFixup(BuildingPropertiesNode y) {
		// Case 1: Only y, y.left and y.left.left exists.
		if (isNil(y.right) && isNil(y.left.right)) {
			y.numRight = 0;
			y.numLeft = 0;
			y.left.numRight = 1;
		} 
		// Case 2: y.left.right also exists in addition to Case 1
		else if (isNil(y.right) && !isNil(y.left.right)) {
			y.numRight = 0;
			y.numLeft = 1 + y.left.right.numRight + y.left.right.numLeft;
			y.left.numRight = 2 + y.left.right.numRight + y.left.right.numLeft;
		}
		// Case 3: y.right also exists in addition to Case 1
		else if (!isNil(y.right) && isNil(y.left.right)) {
			y.numLeft = 0;
			y.left.numRight = 2 + y.right.numRight + y.right.numLeft;

		}
		// Case 4: y.right & y.left.right exist in addition to Case 1
		else {
			y.numLeft = 1 + y.left.right.numRight + y.left.right.numLeft;
			y.left.numRight = 3 + y.right.numRight + y.right.numLeft + y.left.right.numRight + y.left.right.numLeft;
		}
	}

	public void insert(BuildingPropertiesNode z) {
		// Create a reference to root & initialize a node to nil
		BuildingPropertiesNode y = nil;
		BuildingPropertiesNode x = root;
		// While we haven't reached a the end of the tree keep
		// tryint to figure out where z should go
		while (!isNil(x)) {
			y = x;
			if (Integer.compare(z.buildingNum, x.buildingNum) < 0) {
				x.numLeft++;
				x = x.left;
			} else {
				x.numRight++;
				x = x.right;
			}
		}
		// y will hold z's parent
		z.parent = y;
		// Depending on the value of y.key, put z as the left or
		// right child of y
		if (isNil(y))
			root = z;
		else if (Integer.compare(z.buildingNum, y.buildingNum) < 0)
			y.left = z;
		else
			y.right = z;
		// Initialize z's children to nil and z's color to red
		z.left = nil;
		z.right = nil;
		z.color = BuildingPropertiesNode.RED;
		// Call insertFixup(z)
		insertFixup(z);
	}

	// Fixes up the violation of the RedBlackTree properties that may have
	// been caused during insert(z)
	private void insertFixup(BuildingPropertiesNode z) {
		BuildingPropertiesNode y = nil;
		// While there is a violation of the RedBlackTree properties..
		while (z.parent.color == BuildingPropertiesNode.RED) {
			// If z's parent is the the left child of it's parent.
			if (z.parent == z.parent.parent.left) {
				// Initialize y to z 's cousin
				y = z.parent.parent.right;
				// Case 1: if y is red...recolor
				if (y.color == BuildingPropertiesNode.RED) {
					z.parent.color = BuildingPropertiesNode.BLACK;
					y.color = BuildingPropertiesNode.BLACK;
					z.parent.parent.color = BuildingPropertiesNode.RED;
					z = z.parent.parent;
				} 
				// Case 2: if y is black & z is a right child
				else if (z == z.parent.right) {
					z = z.parent;
					leftRotate(z);
				}
				// Case 3: else y is black & z is a left child
				else {
					z.parent.color = BuildingPropertiesNode.BLACK;
					z.parent.parent.color = BuildingPropertiesNode.RED;
					rightRotate(z.parent.parent);
				}
			}
			// If z's parent is the right child of it's parent.
			else {
				// Initialize y to z's cousin
				y = z.parent.parent.left;
				// Case 1: if y is red...recolor
				if (y.color == BuildingPropertiesNode.RED) {
					z.parent.color = BuildingPropertiesNode.BLACK;
					y.color = BuildingPropertiesNode.BLACK;
					z.parent.parent.color = BuildingPropertiesNode.RED;
					z = z.parent.parent;
				} 
				// Case 2: if y is black and z is a left child
				else if (z == z.parent.left) {
					z = z.parent;
					rightRotate(z);
				} 
				// Case 3: if y  is black and z is a right child
				else {
					z.parent.color = BuildingPropertiesNode.BLACK;
					z.parent.parent.color = BuildingPropertiesNode.RED;
					leftRotate(z.parent.parent);
				}
			}
		}
		root.color = BuildingPropertiesNode.BLACK;

	}

	private BuildingPropertiesNode treeMinimum(BuildingPropertiesNode node) {
		// while there is a smaller key, keep going left
		while (!isNil(node.left))
			node = node.left;
		return node;
	}

	private BuildingPropertiesNode treeSuccessor(BuildingPropertiesNode x) {
		// if x.left is not nil, call treeMinimum(x.right) and
		// return it's value
		if (!isNil(x.left))
			return treeMinimum(x.right);

		BuildingPropertiesNode y = x.parent;
		// while x is it's parent's right child...
		while (!isNil(y) && x == y.right) {
			x = y;
			y = y.parent;
		}
		return y;
	}

	public void remove(BuildingPropertiesNode nodeToRemove) {
		// Remove's z from the RedBlackTree rooted at root
		// nodeToRemove.buildingNum is the key search in tree
		BuildingPropertiesNode z = search(nodeToRemove.buildingNum);

		if (z == null) {
			return;
		}
		// if either one of z's children is nil, then we must remove z
		BuildingPropertiesNode x = nil;
		// else we must remove the successor of z
		BuildingPropertiesNode y = nil;
		if (isNil(z.left) || isNil(z.right))
			y = z;
		else
			y = treeSuccessor(z);
		if (!isNil(y.left))
			x = y.left;
		else
			x = y.right;
		// link x's parent to y's parent
		x.parent = y.parent;
		// If y's parent is nil, then x is the root
		if (isNil(y.parent))
			root = x;
		// else if y is a left child, set x to be y's left sibling
		else if (!isNil(y.parent.left) && y.parent.left == y)
			y.parent.left = x;
		else if (!isNil(y.parent.right) && y.parent.right == y)
			y.parent.right = x;
		if (y != z) {
			z.buildingNum = y.buildingNum;		// copy the values of buildingNum from y to z
			z.executedTime = y.executedTime;	// copy the values of executedTime from y to z
			z.totalTime = y.totalTime;			// copy the values of totalTime from y to z
		}
		fixNodeData(x, y);
		if (y.color == BuildingPropertiesNode.BLACK)
			removeFixup(x);
	}

	private void fixNodeData(BuildingPropertiesNode x, BuildingPropertiesNode y) {
		// Initialize two variables which will help us traverse the tree
		BuildingPropertiesNode current = nil;
		BuildingPropertiesNode track = nil;
		// if x is nil, then we will start updating at y.parent
		// Set track to y, y.parent's child
		if (isNil(x)) {
			current = y.parent;
			track = y;
		} 
		// if x is not nil, then we start updating at x.parent
		// Set track to x, x.parent's child
		else {
			current = x.parent;
			track = x;
		}
		
		// while we haven't reached the root
		while (!isNil(current)) {
			// if the node we deleted has a different key than
			// the current node
			if (y.buildingNum != current.buildingNum) {
				// if the node we deleted is greater than
				// current.key then decrement current.numRight
				if (Integer.compare(y.buildingNum, current.buildingNum) > 0)
					current.numRight--;
				// if the node we deleted is less than
				// current.key then decrement current.numLeft
				if (Integer.compare(y.buildingNum, current.buildingNum) < 0)
					current.numLeft--;
			} 
			// if the node we deleted has the same key as the
			// current node we are checking
			else {
				// the cases where the current node has any nil
				// children and update appropriately
				if (isNil(current.left))
					current.numLeft--;
				else if (isNil(current.right))
					current.numRight--;
				// the cases where current has two children and
				// we must determine whether track is it's left
				// or right child and update appropriately
				else if (track == current.right)
					current.numRight--;
				else if (track == current.left)
					current.numLeft--;
			}
			// update track and current
			track = current;
			current = current.parent;
		}

	}

	// Restores the Red Black properties that may have been violated during
	// the removal of a node in remove(RedBlackNode v)
	private void removeFixup(BuildingPropertiesNode x) {
		BuildingPropertiesNode w;
		while (x != root && x.color == BuildingPropertiesNode.BLACK) {
			// if x is it's parent's left child
			if (x == x.parent.left) {
				// set w = x's sibling
				w = x.parent.right;
				// Case 1, w's color is red.
				if (w.color == BuildingPropertiesNode.RED) {
					w.color = BuildingPropertiesNode.BLACK;
					x.parent.color = BuildingPropertiesNode.RED;
					leftRotate(x.parent);
					w = x.parent.right;
				}
				// Case 2, both of w's children are black
				if (w.left.color == BuildingPropertiesNode.BLACK && w.right.color == BuildingPropertiesNode.BLACK) {
					w.color = BuildingPropertiesNode.RED;
					x = x.parent;
				} 
				// Case 3 / Case 4
				else {
					// Case 3, w's right child is black
					if (w.right.color == BuildingPropertiesNode.BLACK) {
						w.left.color = BuildingPropertiesNode.BLACK;
						w.color = BuildingPropertiesNode.RED;
						rightRotate(w);
						w = x.parent.right;
					}
					// Case 4, w = black, w.right = red
					w.color = x.parent.color;
					x.parent.color = BuildingPropertiesNode.BLACK;
					w.right.color = BuildingPropertiesNode.BLACK;
					leftRotate(x.parent);
					x = root;
				}
			}
			// if x is it's parent's right child
			else {
				// set w to x's sibling
				w = x.parent.left;
				// Case 1, w's color is red
				if (w.color == BuildingPropertiesNode.RED) {
					w.color = BuildingPropertiesNode.BLACK;
					x.parent.color = BuildingPropertiesNode.RED;
					rightRotate(x.parent);
					w = x.parent.left;
				}
				// Case 2, both of w's children are black
				if (w.right.color == BuildingPropertiesNode.BLACK && w.left.color == BuildingPropertiesNode.BLACK) {
					w.color = BuildingPropertiesNode.RED;
					x = x.parent;
				}
				// Case 3 / Case 4
				else {
					// Case 3, w's left child is black
					if (w.left.color == BuildingPropertiesNode.BLACK) {
						w.right.color = BuildingPropertiesNode.BLACK;
						w.color = BuildingPropertiesNode.RED;
						leftRotate(w);
						w = x.parent.left;
					}
					// Case 4, w = black, and w.left = red
					w.color = x.parent.color;
					// set x to black to ensure there is no violation of
					// RedBlack tree Properties
					x.parent.color = BuildingPropertiesNode.BLACK;
					w.left.color = BuildingPropertiesNode.BLACK;
					rightRotate(x.parent);
					x = root;
				}
			}
		}
		x.color = BuildingPropertiesNode.BLACK;
	}

	public BuildingPropertiesNode search(int buildingNum) {
		// search starts from root
		BuildingPropertiesNode current = root;
		while (!isNil(current)) {
			// if node with same building number found, return the BuildingPropertiesNode
			// buildingNumbers are all unique by definition
			if (current.buildingNum == (buildingNum))
				return current;
			// if current building number less than searching building number
			// traverse right sub-tree
			else if (Integer.compare(current.buildingNum, buildingNum) < 0)
				current = current.right;
			// if current building number greater than searching building number
			// traverse right sub-tree
			else
				current = current.left;
		}
		// if search failed, return null
		return null;

	}

	private boolean isNil(BuildingPropertiesNode node) {
		// return appropriate value
		return node == nil;
	}
	
	public void printBuildingsStatusInRange(int minimumBuilding, int maximumBuilding) {
		// call buildingsInRange with root as an extra argument
		buildingsInRange(root, minimumBuilding, maximumBuilding);
		// If print is not empty then print starts from nextLine
		if(newLine) {
			System.out.println();
		}
		// reset commaAppearance to false
		commaAppearance = false;
	}

	// Method to print the buildings numbers in the range min to max
	private void buildingsInRange(BuildingPropertiesNode root, int min, int max) {
		// base condition
		if (root == null || root.buildingNum == 0) {
			return ;
		}
		// if min is less than root building number, traverse to left sub-tree
		if (min < root.buildingNum) {
			buildingsInRange(root.left, min, max);
		}
		// if min and max are in between root, print the building status
		if (min <= root.buildingNum && max >= root.buildingNum) {
			if(!commaAppearance) {
				newLine = true;
				System.out.print("(" + root.buildingNum + "," + root.executedTime + "," + root.totalTime + ")");
				// set the comma to true, to enable comma in next print
				commaAppearance = true;
			}
			else {
				newLine = true;
				// print stream with comma included
				System.out.print(","+"(" + root.buildingNum + "," + root.executedTime + "," + root.totalTime + ")");
			}
		}
		// if max is greater than root building number, traverse the right sub-tree
		if (max > root.buildingNum) {
			buildingsInRange(root.right, min, max);
		}
	}
}