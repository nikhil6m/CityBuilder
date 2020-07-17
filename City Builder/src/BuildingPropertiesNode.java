public class BuildingPropertiesNode {
	
	// Building Properties 
	public int buildingNum;
	public int executedTime;
	public int totalTime;
	// GlobalCounter when building is inserted
	public int globalCounter;
	
	// ExecutedForFiveDays is flag that gives the status if a building 
	// is constructed for five consecutive days, initially false for every
	// building when created or just inserted
	public boolean executedForFiveDays = false;
	// ResetNumber is used to switch the flag executedForFiveDays
	// It increments to maximum value of 6, then gets reset to 1 again
	public int resetNumber = 1;

	BuildingPropertiesNode(int buildingNum, int executedTime, int totalTime) {
		this.buildingNum = buildingNum;
		this.executedTime = executedTime;
		this.totalTime = totalTime;
	}
	
	// RedBlackTree Properties
	
	// Reference to the parent node
	BuildingPropertiesNode parent;
	// Reference to left child node
	BuildingPropertiesNode left;
	// Reference to right child node
	BuildingPropertiesNode right;
	
	// Color to the node BLACK = 0, RED = 1
	public static final int BLACK = 0;
	public static final int RED = 1;
	
	public int numLeft = 0;
	public int numRight = 0;
	public int color;

	BuildingPropertiesNode() {
		color = BLACK;
		numLeft = 0;
		numRight = 0;
		parent = null;
		left = null;
		right = null;
	}

}