package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Activity.ActivityCondition;
import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;
import DataObjects.LogEvent;

public class TestDataSequenceAssRuleNormal {

	public static DataElementSeqStorage<LogEvent> getDataSequence()
	{
		List<DataElementSequence<LogEvent>> data = new ArrayList<>();
		
		String[] testData = {
			"1 1 2 3 1 3 4 3 6", 
			"1 4 3 2 3 1 5",
			"5 6 1 2 4 6 3 2", 
			"5 7 1 6 3 2 3"
			}; 
		
		for(String eachEntry : testData)
		{
			String[] dataParts = eachEntry.split(" ");
			
			List<ADataElement<LogEvent>> dataPartLine = new ArrayList<ADataElement<LogEvent>>();
			
			Arrays.stream(dataParts).forEach(x->{
				dataPartLine.add(ofAct(x));
			});
						
			data.add(DataElementSequence.of(dataPartLine));
		}
		
		return DataElementSeqStorage.of(data);
	}
	
	private static DataElementLogEvent ofAct(String activity)
	{
		return DataElementLogEvent.of(LogEvent.of(activity));
	}
}
