package AssociationRuleBase;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSequence;
import Objects.IEqualHashCode;

public abstract class ACondition<T> implements IEqualHashCode{
		
	// should give the absolut position in the data sequence
	public abstract Optional<Integer> matchesFirstAbsolutPosition(DataElementSequence<T> dataSequence);
		
	public abstract int hashCode();

	public abstract boolean equals(Object o);
	
	public abstract String toString();
}
