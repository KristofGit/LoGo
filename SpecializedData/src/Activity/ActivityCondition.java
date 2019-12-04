package Activity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import AssociationRuleBase.ACondition;
import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSequence;
import DataObjects.LogEvent;

public class ActivityCondition extends ACondition<LogEvent>{

	private String activityName;
	
	private ActivityCondition(String activityName)
	{
		this.activityName = activityName;
	}
	
	public static ActivityCondition of(String activityName)
	{
		return new ActivityCondition(activityName);
	}

	public static Set<ACondition<LogEvent>> constructUniqueConditions(DataElementSequence<LogEvent> dataSequence)
	{
		List<ADataElement<LogEvent>> data = dataSequence.getData();
		
		return data.stream().map(x->of(x.getValue().getActivityName())).collect(Collectors.toSet());
	}
		
	@Override
	public Optional<Integer> matchesFirstAbsolutPosition(DataElementSequence<LogEvent> dataSequence) {
		
		Optional<Integer> result = Optional.empty();
		
		List<ADataElement<LogEvent>> dataElements = dataSequence.getData();
		
		for(int i=0;i<dataElements.size();i++)
		{
			ADataElement<LogEvent> eachElement = dataElements.get(i);
			
			LogEvent event = eachElement.getValue();
			
			if(Objects.equals(event.getActivityName(), activityName))
			{
				result = Optional.of(dataSequence.relativeToAbsoluteIndex(i));
				break;
			}			
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		return activityName.hashCode();
	}

	@Override
	public boolean equals(Object o) {

		if(o == null || !(o instanceof ActivityCondition))
		{
			return false;
		}
		
		return Objects.equals(((ActivityCondition)o).activityName, activityName);
	}

	@Override
	public String toString() {
		return activityName;
	}
}
