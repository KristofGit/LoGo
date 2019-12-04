package Main;
import java.util.Objects;

import AssociationRuleMiningDataBase.ADataElement;
import DataObjects.LogEvent;

public class DataElementLogEvent extends ADataElement<LogEvent>{

	public static DataElementLogEvent of(LogEvent event)
	{
		DataElementLogEvent result = new DataElementLogEvent();
		result.setValue(event);
		return result;
	}
	
	@Override
	protected int getHashCode() {
		return this.getValue().getActivityName().hashCode();
	}

	@Override
	protected boolean isEqual(ADataElement<LogEvent> other) {
	
		return Objects.equals(other.getValue().getActivityName(), this.getValue().getActivityName());
	}

}
