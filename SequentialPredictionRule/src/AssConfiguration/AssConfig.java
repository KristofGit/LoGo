package AssConfiguration;

public class AssConfig {

	private double minimumSupport = 0, minimumConfidence = 0, minimumLift = 0;
	private int maxRuleLength = Integer.MAX_VALUE, minRuleLength = 0;
	
	public AssConfig minSup(double sup)
	{
		this.minimumSupport = sup;
		return this;
	}
	
	public AssConfig minConf(double conf)
	{
		this.minimumConfidence = conf;
		return this;
	}
	
	public AssConfig minLift(double conf)
	{
		this.minimumLift = conf;
		return this;
	}
	
	public double minSup()
	{
		return minimumSupport;
	}
	
	public double minConf()
	{
		return minimumConfidence;
	}
	
	public double minLift()
	{
		return minimumLift;
	}
	
	public static AssConfig newConf()
	{
		return new AssConfig();
	}
	
	private AssConfig()
	{
		
	}

	public int getMaxRuleLength() {
		return maxRuleLength;
	}

	public void setMaxRuleLength(int maxRuleLength) {
		this.maxRuleLength = maxRuleLength;
	}

	public int getMinRuleLength() {
		return minRuleLength;
	}

	public void setMinRuleLength(int minRuleLength) {
		this.minRuleLength = minRuleLength;
	}
}
