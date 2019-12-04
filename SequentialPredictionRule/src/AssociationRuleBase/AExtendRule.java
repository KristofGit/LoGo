package AssociationRuleBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import AssociationRuleMiningDataBase.DataElementSequence;

public abstract class AExtendRule<T> {

	public abstract Set<ACondition<T>> generateUniquePotentialConditions(DataElementSequence<T> dataSequence);
	
	public Set<ACondition<T>> generateUniquePotentialConditions(List<DataElementSequence<T>> sequences)
	{
		return sequences.stream().map(x->generateUniquePotentialConditions(x))
				.flatMap(x->x.stream()).collect(Collectors.toSet());
	}
}
