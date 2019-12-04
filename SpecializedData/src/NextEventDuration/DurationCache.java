package NextEventDuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AssociationRuleMiningDataBase.DataElementSeqStorage;
import DataObjects.LogEvent;
import RuleComponents.RuleCompartment;

public class DurationCache {

	/* Stores duration to create temporal association rules based on inter event durations
	 * 
	 * 1) Calculated based on activity association rules
	 * 2) Hence only traces which support such activity rules are taken into consideration
	 * 3) From those traces extract durations as follows:
	 * 		
	 */
	
	private final Map<DurationCacheIdentifier, List<Duration>> extractedDurations = new HashMap<>();
	
	public DurationCache(DataElementSeqStorage<LogEvent> testing,  List<RuleCompartment<LogEvent>> activityRules)
	{
		
	}
}
