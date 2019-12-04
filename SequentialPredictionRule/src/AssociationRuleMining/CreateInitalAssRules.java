package AssociationRuleMining;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import AssociationRuleBase.ACondition;
import AssociationRuleBase.AExtendRule;
import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;
import RuleComponents.RuleCompartment;
import RuleComponents.RuleFragment;

public class CreateInitalAssRules {

	public static <T> List<RuleCompartment<T>> createInitalRuleSet(
			RuleFragment<T> ruleFragment,
			DataElementSeqStorage<T> storage,
			AExtendRule<T> extender)
	{	
		List<DataElementSequence<T>> sequences = storage.getSequences();
		
		Set<ACondition<T>> uniqueConditions = extender.generateUniquePotentialConditions(sequences);
		
		List<RuleCompartment<T>> initalRules = new ArrayList<>();
		
		for(ACondition<T> eachCond : uniqueConditions)
		{
			RuleCompartment<T> rule = RuleCompartment.of(ruleFragment, eachCond, sequences);
			initalRules.add(rule);
		}		
		
		return initalRules;
	}	
}
