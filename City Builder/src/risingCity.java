import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class risingCity {
	public static void main(String args[]) throws IOException {
		String fileName = args[0];
		// Input file to scan the operations to build the city
		File file = new File(fileName);
		
		// Check if the input file exits in path given
		if (file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line1;
			line1 = br.readLine();
			if (line1 == null) {	// If there are no operations in file, there is no building to build
				br.close();
				return;
			} else {
				// Output file to print building status and completion time
				File outputFile = new File("C:\\Users\\NIKHIL MALLADI\\Desktop\\MALLADI_NIKHIL\\MALLADI_NIKHIL\\output_file.txt");
				BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
				
				PrintStream printStream = new PrintStream(outputFile);
				// Using System.setOut(printStream) to print into Output file 
				System.setOut(printStream);

				// Initializing MinHeap of max_size 2000 (maximum active buildings limit) of BuildingPropertiesNode type
				BuildingHeap<BuildingPropertiesNode> minHeap = new BuildingHeap<BuildingPropertiesNode>(2000);
				
				// Initializing RedBlackTree
				RedBlackTree rbTree = new RedBlackTree();

				// tearDownBuildingPropertyValues method extracts the Building Properties like Building Number, Executed Time, Total Time from given string
				BuildingPropertiesNode firstBuildingProps = tearDownBuildingPropertyValues(line1.split(":")[1] , 8 , 0);
				
				//Inserting the BuildingPropertiesNode object into MinHeap and RedBlackTree
				minHeap.insert(firstBuildingProps);
				rbTree.insert(firstBuildingProps);

				// localCounter is the current working day of the city. Initialized to 1, as it starts operations from Day 1
				int localCounter = 1;
				
				// When there is a building for whose construction is currently in progress, we use bufferQ
				// to store until the heap is ready to accept insertions, which can go to maximum size 5
				BuildingPropertiesNode[] bufferQ = new BuildingPropertiesNode[5];
				
				// Since bufferQ is designed to be Queue data structure, front and rear indices are used
				int  front = 0, rear = 0;
				
				// Now we start working on the buildings, initially until last operation in file is scanned
				while ((line1 = br.readLine()) != null) {
					
					// Split the line based on colon delimiter
					String[] nextColonSplit = line1.split(":", 2);
					
					// nextColonSplit[0] will contain globalCounter
					// nextColonSplit[1] will contain operation example: Insert(100,0,50)
					// nextGlobalCounter and nextOperation contain the values of globalCounter and corresponding operation in nextLine of input
					int nextGlobalCounter = Integer.parseInt(nextColonSplit[0]);
					String nextOperation = nextColonSplit[1];
					
					// We work on the buildings until we reach the globalCounter of next building
					while (localCounter <= nextGlobalCounter) {
						
						// If the localCounter is equal to nextGlobalCounter and the next operation is Insert
						if (localCounter == nextGlobalCounter && nextOperation.contains("Insert")) {
							
							// Extract the building properties from nextOpeartion and store it in buildingProps Object
							BuildingPropertiesNode buildingProps = tearDownBuildingPropertyValues(nextOperation, 8, Integer.parseInt(nextColonSplit[0]));
							
							// Insert the new buildingProps object in RedBlackTree
							rbTree.insert(buildingProps);
							
							// If the building in heap is NOT executed for five days
							// the buildingProps object is NOT inserted into the heap
							// It is stored temporarily in bufferQ (If condition)
							// Otherwise, insert the buildingProps object directly into heap (else condition)
							if (minHeap.node[1] != null && !minHeap.node[1].executedForFiveDays) {
								bufferQ[rear++] = buildingProps;
							} else {		
								minHeap.insert(buildingProps);
							}
							
							localCounter++;
							// MinHeap is updated for every localCounter which increments by one
							minHeap.updateMinHeap(localCounter, rbTree);
							
						}
						// If the localCounter is equal to nextGlobalCounter and the next operation is PrintBuilding
						else if (localCounter == nextGlobalCounter && nextOperation.contains("PrintBuilding")) {
							// Extract the range values from nextOperation whose buildings statuses are to be shown
							nextOperation = nextOperation.substring(15, nextColonSplit[1].length() - 1);
							String[] buildingValues = nextOperation.split(",", 2);
							
							// Minimum building value in range
							int minimumBuilding = Integer.parseInt(buildingValues[0]);
							// Maximum building value in range
							int maximumBuilding = Integer.parseInt(buildingValues[1]);
							// Update the MinHeap before showing the statuses of buildings
							minHeap.updateMinHeap(localCounter++, rbTree);
							// Use the RedBlackTree to display the values
							rbTree.printBuildingsStatusInRange(minimumBuilding, maximumBuilding);
						} 
						// If the localCounter is equal to nextGlobalCounter and the next operation is Print
						else if (localCounter == nextGlobalCounter && nextOperation.contains("Print")) {
							// Extract the buildingNumber whose status is to be shown
							nextOperation = nextOperation.substring(7, nextColonSplit[1].length() - 1);
							// Named it as buildingToSearch
							int buildingToSearch = Integer.parseInt(nextOperation);
							// Update the MinHeap before showing the status of the building
							minHeap.updateMinHeap(localCounter++, rbTree);
							// Perform search operation on RedBlackTree
							BuildingPropertiesNode found = rbTree.search(buildingToSearch);
							// If the search is successful, show the building Number, executed time and total time of the building
							// Otherwise, print (0,0,0)
							if (found != null) {
								System.out.println("(" + found.buildingNum + "," + found.executedTime + ","
										+ found.totalTime + ")");
							} else {
								System.out.println("(0,0,0)");
							}
						} else {
							localCounter++;
							// If the localCounter is not equal nextGlobalCounter, simply update the MinHeap for one localCounter increment.
							minHeap.updateMinHeap(localCounter, rbTree);
						}
						// If after updating MinHeap in any above case and there are building objects in buffer 
						// waiting to go into Heap, insert the first element(FIFO) from buffereQ to MinHeap
						if (!minHeap.isEmpty() && minHeap.node[1].executedForFiveDays && rear > 0) {
							minHeap.insert(bufferQ[front]);
							for (int i = front + 1; i < rear; i++) {
								bufferQ[i - 1] = bufferQ[i];
							}
							rear--;
						}
					}
				}
				// Assign localCounter to current working day
				localCounter--;
				// Now, finish the construction of buildings until heap becomes empty
				while (!minHeap.isEmpty()) {
					if (rear > 0 && localCounter > bufferQ[0].globalCounter && minHeap.node[1].executedForFiveDays) {
						minHeap.insert(bufferQ[front]);
						for (int i = front + 1; i < rear; i++) {
							bufferQ[i - 1] = bufferQ[i];
						}
						rear--;
					}
					localCounter++;
					// Update the MinHeap by working  on localCounter 
					minHeap.updateMinHeap(localCounter, rbTree);
				}
				// Closing the streams BufferedReader and BufferedWriter
				br.close();
				out.close();
			}
		}
	}

	// Method to extract the BuildingPropertyValues from operation for Insert operation only
	private static BuildingPropertiesNode tearDownBuildingPropertyValues(String operation, int valueStartIndex, int globalCounter) {
		// propertyStartIndex is exact index from where building property value starts from
		operation = operation.substring(valueStartIndex, operation.length() - 1);
		// split based on comma
		String[] buildingProps = operation.split(",", 2);
		// BuildingPropertiesNode constructor takes arguments buildingNumber, executionTime = 0 , totalTime
		BuildingPropertiesNode buildingProperties = new BuildingPropertiesNode(
				Integer.parseInt(buildingProps[0]), 0, Integer.parseInt(buildingProps[1]));
		buildingProperties.globalCounter = globalCounter;
		
		// Return buildingProperties object 
		return buildingProperties;
		
	}
}
