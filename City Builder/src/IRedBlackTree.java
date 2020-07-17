
public interface IRedBlackTree {
	
	public void insert(BuildingPropertiesNode z);
	
	public void remove(BuildingPropertiesNode nodeToRemove);
	
	public BuildingPropertiesNode search(int buildingNum);
	
	public void printBuildingsStatusInRange(int minimumBuilding, int maximumBuilding);

}
