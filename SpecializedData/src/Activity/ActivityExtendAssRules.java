package Activity;

import java.util.List;
import java.util.Set;

import AssociationRuleBase.ACondition;
import AssociationRuleBase.AExtendRule;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;
import RuleComponents.RuleCompartment;
import DataObjects.LogEvent;

public class ActivityExtendAssRules extends AExtendRule<LogEvent> {
	
	@Override
	public Set<ACondition<LogEvent>> generateUniquePotentialConditions(DataElementSequence<LogEvent> dataSequence) {
			
		return ActivityCondition.constructUniqueConditions(dataSequence);
	}

}
