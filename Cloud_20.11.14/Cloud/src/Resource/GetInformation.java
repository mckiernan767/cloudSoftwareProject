package Resource;

public interface GetInformation {
	public void fetchPower(long starttime,long endtime);
	public void refresh();
	public void refreshInterval(long interval);
	
}
