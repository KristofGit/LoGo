package NextEventDuration;

public class DurationCacheIdentifier {

	private final String activityFrom;
	private final String activityTo;
	
	public DurationCacheIdentifier(String activityFrom, String activityTo) {
		super();
		this.activityFrom = activityFrom;
		this.activityTo = activityTo;
	}
	
	public String getActivityFrom() {
		return activityFrom;
	}

	public String getActivityTo() {
		return activityTo;
	}
}
