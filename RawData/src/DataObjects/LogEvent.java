package DataObjects;

import java.time.Duration;

public class LogEvent {

	private String activityName;
	
	@Override
	public String toString() {
		return activityName;
	}

	private LogEvent()
	{
	}
	
	public String getActivityName()
	{
		return activityName;
	}
	
	public static LogEvent newEvent()
	{
		return new LogEvent();
	}
	
	public static LogEvent of(String activity)
	{
		LogEvent event = newEvent();
		return event.activity(activity);
	}
	
	public static LogEvent of(String activity, Duration durationMin, Duration durationMax)
	{
		return null;
	}
	
	private LogEvent activity(String activityName)
	{
		this.activityName = activityName;
		return this;
	}
	
	
}
