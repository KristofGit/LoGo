package Configuration;

public enum DataSource {

	// adapt the paths in accordance to your system
	BPIC_2012("..../BPIC12.xes"),
	Helpdesk("..../helpdesk.csv");

	private final String pathToXML;
	
	private DataSource(String path)
	{
		this.pathToXML = path;
	}
	
	public String getPath()
	{
		return pathToXML;
	}
}
