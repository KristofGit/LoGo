package LogRepresentation;

import java.time.Duration;
import java.time.Instant;

import BaseObject.HashEqual;

public class ExecutionEvent implements HashEqual{

	private final String resourceName;
	private final String activityName;
	private final Instant pointInTime;
	
	private final InternalEventType eventType;
	
	private ExecutionEvent(InternalEventType type)
	{
		this.resourceName = null;
		this.activityName = type.name();
		this.pointInTime = null;
		eventType = type;
	}
	
	public InternalEventType getEventType() {
		return eventType;
	}
	
	public static Duration between(ExecutionEvent one, ExecutionEvent two)
	{	
		if(one.getPointInTime() == null ||  two.getPointInTime() == null)
		{
			return null;
		}
		
		return Duration.between(one.getPointInTime(), two.getPointInTime()).abs();
	}
	
	public ExecutionEvent(String resourceName, String activityName, Instant pointInTime)
	{
		this.resourceName = resourceName;
		this.activityName = activityName;
		this.pointInTime = pointInTime;
		eventType = InternalEventType.N;
	}
	
	public static ExecutionEvent getStartEvent()
	{
		return new ExecutionEvent(InternalEventType.S);
	}
	
	public static ExecutionEvent getEndEvent()
	{
		return new ExecutionEvent(InternalEventType.F);
	}

	public boolean isArtificalStartEvent()
	{
		return eventType == InternalEventType.S;
	}
	
	public boolean isArtificalFinalEvent()
	{
		return eventType == InternalEventType.F;
	}
	
	public boolean isArtificalEvent()
	{
		return eventType == InternalEventType.F || eventType == InternalEventType.S;
	}
	
	public String getActivityName() {
		return activityName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityName == null) ? 0 : activityName.hashCode());
		result = prime * result + ((pointInTime == null) ? 0 : pointInTime.hashCode());
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
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
		ExecutionEvent other = (ExecutionEvent) obj;
		if (activityName == null) {
			if (other.activityName != null)
				return false;
		} else if (!activityName.equals(other.activityName))
			return false;
		if (pointInTime == null) {
			if (other.pointInTime != null)
				return false;
		} else if (!pointInTime.equals(other.pointInTime))
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "E [" + activityName + ", " + eventType + "]";
	}

	public Instant getPointInTime() {
		return pointInTime;
	}
}
