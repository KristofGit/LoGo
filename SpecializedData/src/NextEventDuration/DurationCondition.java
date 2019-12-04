package NextEventDuration;

import java.util.Optional;

import AssociationRuleBase.ACondition;
import AssociationRuleMiningDataBase.DataElementSequence;
import DataObjects.LogEvent;

public class DurationCondition extends ACondition<LogEvent>{

	@Override
	public Optional<Integer> matchesFirstAbsolutPosition(DataElementSequence<LogEvent> dataSequence) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
