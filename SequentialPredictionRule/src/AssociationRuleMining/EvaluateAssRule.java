package AssociationRuleMining;

import java.util.List;
import java.util.Optional;

import AssData.IMatchingResultBasic;
import AssData.MatchingResultExact;
import AssociationRuleBase.ACondition;
import AssociationRuleBase.AEvaluateRule;
import AssociationRuleMiningDataBase.DataElementSequence;
import CollectionsHelper.ListHelper;
import RuleComponents.Rule;
import RuleComponents.RuleFragment;

public class EvaluateAssRule<T> extends AEvaluateRule<T> {

	@Override
	public MatchingResultExact matchesPrecondition_getLastIndexOfPre(Rule<T> rule, DataElementSequence<T> sequence) {
	
		sequence = sequence.resetToStartIndexToZero();
		
		List<ACondition<T>> precondition = rule.getPreconditions();
		
		Optional<Integer> lastPosPrecMatch = Optional.empty(); 
		
		if(precondition.size()>=2)
		{
			System.out.print("");
		}
		
		for(int i=0;i<precondition.size();i++)
		{
			ACondition<T> eachPrec = precondition.get(i);
			
			Optional<Integer> match = eachPrec.matchesFirstAbsolutPosition(sequence);
			
			if(match.isPresent())
			{
				// such that the next precodition item can be matched.
				sequence = sequence.setSequenceStartIndexTo_AFTER(match.get());
			}
			else
			{
				//to make it clear that not all parts of the precondition were matched
				lastPosPrecMatch = Optional.empty();
				break;
			}
		
			lastPosPrecMatch = match;
		}
		
		MatchingResultExact result = MatchingResultExact.create();
		result.lastMatchingIndex(lastPosPrecMatch);
		return result;
	}

	@Override
	public MatchingResultExact matchesCurrent(Rule<T> rule, DataElementSequence<T> sequence) {

		Optional<Integer> firstRelativeCurrentMatching = rule.getCurPost().absoluteIndexOfCurrent(sequence);
		 
		return MatchingResultExact.create(firstRelativeCurrentMatching);
	}

	@Override
	public MatchingResultExact matchesCurrentAndPost_GetFirstCurrentIndexWithAdjacentPost(Rule<T> rule, DataElementSequence<T> sequence) {

		List<Integer> indexes =	rule.getCurPost().indexOfCurrentsWithAdjacentPOST(sequence);
		
		if(!ListHelper.hasValues(indexes))
		{
			return MatchingResultExact.create();
		}
		
		return MatchingResultExact.create(Optional.of(indexes.stream().min(Integer::compareTo).get()));
	}

	@Override
	public MatchingResultExact matchesPost(Rule<T> rule, DataElementSequence<T> sequence) {
		
		Optional<Integer> firstRelativeCurrentMatching = rule.getCurPost().absolutIndexOfPost(sequence);
		 
		return MatchingResultExact.create(firstRelativeCurrentMatching);
	}
}
