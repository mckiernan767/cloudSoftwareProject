package Resource;

public class Host {
	private int id;
	public int rackId;
	public int moduleGroupId;
	String name;
	String description;
	String hostType;
	double IP;
	
	public int getID(){
		return id;
	}
	public void setID(int x){
		id=x;
	}
	
	public int getRackID(){
		return rackId;
	}
	public void setRackID(int x){
		rackId=x;
	}

	public int getModuleGroupID(){
		return moduleGroupId;
	}
	public void setModuleGroupID(int x){
		moduleGroupId=x;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		description = d;
	}
	
	public String getHostType() {
		return hostType;
	}

	public void setHostType(String d) {
		hostType = d;
	}
	
	public double getIP(){
		return IP;
	}
	public void setIP(int x){
		IP=x;
	}
}
