package HelperObjects;

public class ActivityRuleCollection {

	private final String currentActivity;
	private final String nextPostActivity;
	
	public ActivityRuleCollection(String currentActivity, String nextPostActivity) {
		super();
		this.currentActivity = currentActivity;
		this.nextPostActivity = nextPostActivity;
	}
	
	@Override
	public String toString() {
		return currentActivity+"==>"+nextPostActivity;
	}

	public static ActivityRuleCollection of(String currentActivity, String nextPostActivity)
	{
		return new ActivityRuleCollection(currentActivity, nextPostActivity);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCurrentActivity() == null) ? 0 : getCurrentActivity().hashCode());
		result = prime * result + ((getNextPostActivity() == null) ? 0 : getNextPostActivity().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityRuleCollection other = (ActivityRuleCollection) obj;
		if (getCurrentActivity() == null) {
			if (other.getCurrentActivity() != null)
				return false;
		} else if (!getCurrentActivity().equals(other.getCurrentActivity()))
			return false;
		if (getNextPostActivity() == null) {
			if (other.getNextPostActivity() != null)
				return false;
		} else if (!getNextPostActivity().equals(other.getNextPostActivity()))
			return false;
		return true;
	}

	public String getCurrentActivity() {
		return currentActivity;
	}

	public String getNextPostActivity() {
		return nextPostActivity;
	}
	
}
