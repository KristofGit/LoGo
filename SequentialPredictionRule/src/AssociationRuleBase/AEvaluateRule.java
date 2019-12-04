package AssociationRuleBase;

import java.util.function.BiFunction;
import java.util.function.Function;

import AssData.MatchingResultExact;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;
import RuleComponents.Rule;

public abstract class AEvaluateRule<T> {

	public abstract MatchingResultExact matchesCurrent(Rule<T> rule, DataElementSequence<T> sequence);
	
	public abstract MatchingResultExact matchesPost(Rule<T> rule, DataElementSequence<T> sequence);

	public abstract MatchingResultExact matchesCurrentAndPost_GetFirstCurrentIndexWithAdjacentPost(Rule<T> rule, DataElementSequence<T> sequence);
	
	public abstract MatchingResultExact matchesPrecondition_getLastIndexOfPre(Rule<T> rule, DataElementSequence<T> sequence);

	public MatchingResultExact matchesPreCur_getLastIndexOfPre(Rule<T> rule, DataElementSequence<T> sequence)
	{
		return matchesComplex_getLastIndexOfPre(rule, sequence, (x,y) ->  {return matchesCurrent(y,x);});
	}
	
	private MatchingResultExact matchesComplex_getLastIndexOfPre(
			Rule<T> rule,
			DataElementSequence<T> sequence,
			BiFunction<DataElementSequence<T>, Rule<T>, MatchingResultExact> matchingFunction)
	{
		MatchingResultExact matchingResultPre = matchesPrecondition_getLastIndexOfPre(rule, sequence);
		
		if(matchingResultPre.doesMatch())
		{
			DataElementSequence<T> adaptedSequence = sequence.setSequenceStartIndexTo_AFTER(matchingResultPre.getLastMatchingIndex().get());
			adaptedSequence = adaptedSequence.resetEndIndexToMax();
			
			MatchingResultExact matchingResultCurPost = matchingFunction.apply(adaptedSequence, rule);
			
			if(matchingResultCurPost.doesMatch())
			{
				return matchingResultPre;
			}
		}
		
		return MatchingResultExact.create();
	}
	
	//matches pre, cur and pos
	public MatchingResultExact matchesCompleteReturnPrePos(Rule<T> rule, DataElementSequence<T> sequence)
	{
		return matchesComplex_getLastIndexOfPre(rule, sequence, (x,y) ->  {return matchesCurrentAndPost_GetFirstCurrentIndexWithAdjacentPost(y,x);});
	}
}
