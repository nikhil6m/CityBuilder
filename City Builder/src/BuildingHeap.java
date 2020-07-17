public class BuildingHeap<T> {
	// Maximum capacity of the heap
	public int heapCapacity;
	// Heap array of BuildingPropertiesNodes
	public BuildingPropertiesNode[] node;
	// current heap size
	public int currentSize;

	public BuildingHeap(int heapCapacity) {
		// assign to class instance using this
		this.heapCapacity = heapCapacity;
		// heapCapacity + 1 because index 0 is not being considered
		node = new BuildingPropertiesNode[heapCapacity + 1];
		// Store a dummy value into heap
		node[0] = new BuildingPropertiesNode();
		node[0].executedTime = Integer.MIN_VALUE;
		// 0, as there is no element in heap
		currentSize = 0;
	}

	public void insert(BuildingPropertiesNode x) {
		// Returns with out insert, if the heap size increases by 2000(HeapCapacity)
		if (currentSize == heapCapacity) {
			System.out.println("heap is full");
			return;
		}
		// Increase the size of heap by 1
		currentSize++;
		int idx = currentSize;
		// Insert the element at the end of the heap
		node[idx] = x;
		// perform bubbleUp on Heap from position where new node is inserted
		bubbleUp(idx);
	}

	public void bubbleUp(int pos) {
		// Parent index in heap is always at pos / 2
		int parentIdx = pos / 2;
		int currentIdx = pos;
		// Compare with parent and swap until the executedTime in parent node is greater than one in child
		while (currentIdx > 0 && node[parentIdx].executedTime > node[currentIdx].executedTime) {
			swap(currentIdx, parentIdx);
			currentIdx = parentIdx;
			parentIdx = parentIdx / 2;
		}
	}

	public BuildingPropertiesNode removeMin() {
		// Node at index 1, will have the minimum element as the heap is a min heap
		BuildingPropertiesNode min = node[1];
		// Removal in heap is performed by placing the last index at min position
		// and minimum node at the end of heap
		node[1] = node[currentSize];
		// null as the minimum node is permanently removed from heap
		node[currentSize--] = null;
		// Heapify from the index position 1
		sinkDown(1);
		return min;
	}

	public void sinkDown(int k) {
		// smallest value is the node to compare the node with
		int smallest = k;
		// Left child to the smallest
		int leftChildIdx = 2 * k;
		// Right child to the smallest
		int rightChildIdx = 2 * k + 1;
		// If the left child is with in heapsize and the executed Time of left child is same as executed time of parent(smallest)
		if (leftChildIdx <= heapSize()  && node[smallest].executedTime == node[leftChildIdx].executedTime ) {
			if(node[smallest].buildingNum > node[leftChildIdx].buildingNum) {
				smallest = leftChildIdx;
			} 
		}
		else {
			if(leftChildIdx <= heapSize() && node[smallest].executedTime > node[leftChildIdx].executedTime) {
				smallest = leftChildIdx;
			}
		}
		// If the right child is with in heapsize and the executed Time of right child is same as executed time of parent(smallest)
		if (rightChildIdx <= heapSize()  && node[smallest].executedTime == node[rightChildIdx].executedTime ) {
			if(node[smallest].buildingNum > node[rightChildIdx].buildingNum) {
				smallest = rightChildIdx;
			} 
		}
		else {
			if(rightChildIdx <= heapSize() && node[smallest].executedTime > node[rightChildIdx].executedTime) {
				smallest = rightChildIdx;
			}
		}
		if (smallest != k) {
			swap(k, smallest);
			sinkDown(smallest);
		}
	}

	public void swap(int a, int b) {
		// Swap two nodes based in indices a , b
		BuildingPropertiesNode temp = node[a];
		node[a] = node[b];
		node[b] = temp;
	}

	public boolean isEmpty() {
		// Returns true if the heap's current size is 0, false otherwise
		return currentSize == 0;
	}

	public int heapSize() {
		// Returns the current size of the heap
		return currentSize;
	}

	// updateMinHeap method performs the construction of the building
	// and accepts new buildings into heap, if the minNode is 
	// executed for five days 
	public void updateMinHeap(int localCounter, RedBlackTree rbTree) {
		// extractMin() doesn't remove the minimum node from heap
		// only, minNode reference is pointed to it
		BuildingPropertiesNode minNode = extractMin();
		// if minNode is null, heap is empty, so no buildings to operate on
		if(minNode == null) {
			return;
		} else {
			// if minNode is executed for five days, we switch the flag to false
			// as we are going to perform 1 more day work on the building
			if(minNode.executedForFiveDays) {
				minNode.executedForFiveDays = false;
			}
			// perform 1 day work on minimum node building 
			minNode.executedTime++;
			// reset number is used to track the current consecutive days
			// of work on a building which is always less than 6,
			// if equal to 6 reset to 1
			minNode.resetNumber++;
			// executedForFiveDays flag is switched to true if worked on 
			// a building for five days(continuously)
			if(minNode.resetNumber == 6) {
				minNode.executedForFiveDays = true;
				minNode.resetNumber = 1;
			}
			// if executedTime of building is equal to totalTime 
			// show the building number and current working day 
			// at which it finished execution. 
			// Also, remove the building from MinHeap and RedBlackTree
			if(minNode.executedTime >= minNode.totalTime) {
				System.out.println("(" + minNode.buildingNum + "," + localCounter + ")");
				// removeMin() : Removes building from MinHeap
				removeMin();
				// remove(minNode) : Removes building from RedBlackTree
				rbTree.remove(minNode);
			}
			else if(minNode.executedForFiveDays){
				// If building is executedForFiveDays, heapify the minHeap from index 1
				sinkDown(1);
			}
		}
	}
	
	public BuildingPropertiesNode extractMin() {
		// Returns the node that has minimum executed time or
		// node with building number in case or tie(Tie breaker)
		return node[1];
	}

}