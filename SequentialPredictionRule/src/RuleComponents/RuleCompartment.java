package RuleComponents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import AssData.MatchingResultExact;
import AssociationRuleBase.ACondition;
import AssociationRuleBase.AEvaluateRule;
import AssociationRuleBase.AExtendRule;
import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;

public class RuleCompartment<T> {

	private final Rule<T> rule;
	private final List<DataElementSequence<T>> remainingMatchingSequences_PreCurPost = new ArrayList<>(); // X u Y
	private final List<DataElementSequence<T>> remainingMatchingSequences_PreCur = new ArrayList<>(); // X
	private final List<DataElementSequence<T>> remainingMatchingSequences_CurPost = new ArrayList<>(); // Y

	private boolean hasBeenEvaluated_PreCurPost = false;
	private boolean hasBeenEvaluated_PreCur = false;
	private boolean hasBeenEvaluated_CurPost = false;

	private Optional<Double> confidence = Optional.empty();
	private Optional<Double> support = Optional.empty();
	private Optional<Double> lift = Optional.empty();

	public String toString()
	{
		return getRule().toString() + 
				" sup: " + (support.isPresent()?support.get():"NA") +
				" conf: " + (confidence.isPresent()?confidence.get():"NA")+
				" lift: " + (lift.isPresent()?lift.get():"NA");
	}
	
	public int getRuleSize()
	{
		return getRule().getRuleSize();
	}
	
	private RuleCompartment(Rule<T> rule,
			List<DataElementSequence<T>> allSequences)
	{
		this.hasBeenEvaluated_PreCurPost = false;
		this.hasBeenEvaluated_PreCur = false;
		this.hasBeenEvaluated_CurPost = false;
		
		this.rule = rule;
		
		this.remainingMatchingSequences_PreCurPost.addAll(allSequences);
		this.remainingMatchingSequences_PreCur.addAll(allSequences);
		this.remainingMatchingSequences_CurPost.addAll(allSequences);
	}
	
	private RuleCompartment(Rule<T> rule,
			List<DataElementSequence<T>> seqPreCurPost,
			List<DataElementSequence<T>> seqPre,
			List<DataElementSequence<T>> seqCurPost)
	{
		this.hasBeenEvaluated_PreCurPost = false;
		this.hasBeenEvaluated_PreCur = false;
		this.hasBeenEvaluated_CurPost = true;
		
		/* Assumption that curpost never will be less supported
		 * as we are not advancing it or are taking the develoipment
		 * of the pre rulepart into account
		 */
		
		
		this.rule = rule;
		
		this.remainingMatchingSequences_PreCurPost.addAll(seqPreCurPost);
		this.remainingMatchingSequences_PreCur.addAll(seqPre);
		this.remainingMatchingSequences_CurPost.addAll(seqCurPost);
	}

	public double calculateLift(DataElementSeqStorage<T> allData, AEvaluateRule<T> evaluation)
	{
		if(lift.isPresent())
		{
			return lift.get();
		}
		
		filterSequencesForRuleSupport_PreCurPost(evaluation);
		filterSequencesForRuleSupport_PreCur(evaluation);
		filterSequencesForRuleSupport_CurPost(evaluation);
	
		if(remainingMatchingSequences_PreCurPost.isEmpty() ||
				remainingMatchingSequences_PreCur.isEmpty() ||
				remainingMatchingSequences_CurPost.isEmpty())
		{
			return 0;
		}

		double allDataSize = allData.getSequenceCount();
		
		double upper = remainingMatchingSequences_PreCurPost.size()/allDataSize;
		double lowerOne = remainingMatchingSequences_PreCur.size()/allDataSize;
		double lowerTwo = remainingMatchingSequences_CurPost.size()/allDataSize;
		double lower = ((double)lowerOne)*lowerTwo;
		
		lift = Optional.of(upper/lower);
		
		return lift.get();
	}
	
	public double calculateSupport(DataElementSeqStorage<T> allData, AEvaluateRule<T> evaluation)
	{
		if(support.isPresent())
		{
			return support.get();
		}
		
		filterSequencesForRuleSupport_PreCurPost(evaluation);
		
		if(remainingMatchingSequences_PreCurPost.isEmpty())
		{
			return 0;
		}
				
		double allDataSize = allData.getSequenceCount();

		double upper = ((double)remainingMatchingSequences_PreCurPost.size());
		double lower = allDataSize;
		
		support = Optional.of(upper/lower);
		
		return support.get();
	}
	
	public void freeMemoryFromStoredTraces()
	{
		remainingMatchingSequences_PreCur.clear();
		remainingMatchingSequences_PreCurPost.clear();
		remainingMatchingSequences_CurPost.clear();
	}
	
	public double calculateConfidence(DataElementSeqStorage<T> allData, AEvaluateRule<T> evaluation)
	{
		/* Wie oft finde ich precondition + current + post = remainingMatchingSequences.size
		 * dividiert durch precondition + current = muss man in den gesamtdaten nachrechnen
		 * wie oft führt precondition bei gegeben current wirklich zu post
		 */
		if(confidence.isPresent())
		{
			return confidence.get();
		}
		
		//diejenigen aus allData rausstreichen die ich mittels remainingMatchinSequences bereits unterstütze
		//beim rest muss ich schauen und dann muss man zusammenrechnen (i.e., allData unterstützt + remaining size)
		filterSequencesForRuleSupport_PreCur(evaluation);
		if(remainingMatchingSequences_PreCur.isEmpty())
		{
			return 0;
		}
		
		//remainingMatchingSequences_PreCurPost.clear();
		//remainingMatchingSequences_PreCurPost.addAll(remainingMatchingSequences_PreCur);
		
		filterSequencesForRuleSupport_PreCurPost(evaluation);
		if(remainingMatchingSequences_PreCurPost.isEmpty())
		{
			return 0;
		}
		
		double allDataSize = allData.getSequenceCount();

		//wie viele untersützten pre+cur+post
		double upper = ((double)remainingMatchingSequences_PreCurPost.size())/allDataSize;
		//wie viele unterstützen pre+cur
		double lower = remainingMatchingSequences_PreCur.size()/allDataSize;
		
		confidence = Optional.of(upper/lower);
				
		return confidence.get();
	}
	
	public static <T> RuleCompartment<T> of(
			RuleFragment<T> fragment, 
			ACondition<T> initalPreCondition, 
			List<DataElementSequence<T>> sequences)
	{
		
		Rule<T> initalRule = Rule.of(fragment);
		initalRule = initalRule.add(initalPreCondition);
		
		return new RuleCompartment<>(initalRule, sequences);
	}
	
	public static <T> List<RuleCompartment<T>> spawnNewPotentialRules(
			RuleCompartment<T> rule,
			AEvaluateRule<T> evaluation,
			AExtendRule<T> ruleExtension)
	{
		return rule.spawnNewRules(evaluation, ruleExtension);
	}
	
	private List<RuleCompartment<T>> spawnNewRules(
			AEvaluateRule<T> evaluation,
			AExtendRule<T> ruleExtension)
	{
		List<RuleCompartment<T>> result = new ArrayList<>();
		
		this.filterSequencesForRuleSupport_PreCurPost(evaluation);
		this.filterSequencesForRuleSupport_PreCur(evaluation);
		// TODO WAS ONLY DISABLED TO INCRESE SPEED AS ITS NOT REQUIEST FOR CONFIDENCE this.filterSequencesForRuleSupport_CurPost(evaluation);
		
		//previously I have used remainingMatchingSequences_PreCur list, but remainingMatchingSequences_PreCurPost
		//seems to result in similar results as we are interested in conformance throughout the evaluation
		//TODO Maybe change this if support or so becomes relevant too
		Set<ACondition<T>> conditions = ruleExtension.generateUniquePotentialConditions(remainingMatchingSequences_PreCurPost);
				
		for(ACondition<T> eachCond : conditions)
		{
			Rule<T> extendedRule = this.getRule().add(eachCond);
						
			/*result.add(new RuleCompartment<>(extendedRule, 
					remainingMatchingSequences_PreCurPost, 
					remainingMatchingSequences_PreCur,
					remainingMatchingSequences_CurPost));*/
			
			result.add(new RuleCompartment<>(extendedRule, 
					remainingMatchingSequences_PreCurPost, 
					remainingMatchingSequences_PreCur,
					new ArrayList<>())); //TODO skipping on the last one to reduce memory consumption as its only relevant for LIFT but we look at CONF
		}
		
		return result;
	}
	
	private void filterSequencesForRuleSupport(
			List<DataElementSequence<T>> sequences,
			Function<DataElementSequence<T>, MatchingResultExact> evaluationFunction)
	{
		List<DataElementSequence<T>> filteredSequences = new ArrayList<>();
		
		Iterator<DataElementSequence<T>> iter = sequences.iterator();
			
		while(iter.hasNext())
		{
			DataElementSequence<T> eachSequence = iter.next();
			MatchingResultExact matchResult = evaluationFunction.apply(eachSequence);

			if(!matchResult.doesMatch())
			{
				continue;
			}
			
			Optional<Integer> matchingPos = matchResult.getLastMatchingIndex();
			
			filteredSequences.add(eachSequence.setSequenceStartIndexTo_AFTER(matchingPos.get()));
		}
		
		sequences.clear();
		sequences.addAll(filteredSequences);
	}
	
	//we assume that Pre and Cur should together be seen as one
	//as both are part of the IF part
	private void filterSequencesForRuleSupport_PreCur(AEvaluateRule<T> evaluation)
	{
		if(hasBeenEvaluated_PreCur)
		{
			return;
		}
		
		hasBeenEvaluated_PreCur = true;
		
		filterSequencesForRuleSupport(remainingMatchingSequences_PreCur,
				new Function<DataElementSequence<T>, MatchingResultExact>() {
			//give back the pre result to get a correct trimming of the rules which would permit to add the CUR element to the rules preconditions
			@Override
			public MatchingResultExact apply(DataElementSequence<T> t) {
				
				MatchingResultExact preResult = evaluation.matchesPreCur_getLastIndexOfPre(getRule(), t);
				/*
				MatchingResultExact preResult =  evaluation.matchesPrecondition_getLastIndexOfPre(getRule(), t);
				if(!preResult.doesMatch())
				{
					return preResult;
				}
				
				DataElementSequence<T> sequenceAfterPre = t.setSequenceStartIndexTo_AFTER(preResult.getLastMatchingIndex().get());
				
				MatchingResultExact matchesCurrent = evaluation.matchesCurrent(getRule(), sequenceAfterPre);

				if(!matchesCurrent.doesMatch())
				{
					return matchesCurrent;
				}*/
				
				return preResult;
			}
		});
	}
	
	private void filterSequencesForRuleSupport_CurPost(AEvaluateRule<T> evaluation)
	{
		if(hasBeenEvaluated_CurPost)
		{
			return;
		}
		
		hasBeenEvaluated_CurPost = true;
		
		filterSequencesForRuleSupport(remainingMatchingSequences_CurPost,
				new Function<DataElementSequence<T>, MatchingResultExact>() {

			@Override
			public MatchingResultExact apply(DataElementSequence<T> t) {
				return evaluation.matchesCurrentAndPost_GetFirstCurrentIndexWithAdjacentPost(getRule(), t.resetToStartIndexToZero().resetEndIndexToMax());
			}
		});
	}
	
	
	private void filterSequencesForRuleSupport_PreCurPost(AEvaluateRule<T> evaluation)
	{
		if(hasBeenEvaluated_PreCurPost)
		{
			return;
		}
		
		hasBeenEvaluated_PreCurPost = true;
			
		filterSequencesForRuleSupport(remainingMatchingSequences_PreCurPost,
				new Function<DataElementSequence<T>, MatchingResultExact>() {

			@Override
			public MatchingResultExact apply(DataElementSequence<T> t) {
				//matches pre, cur and pos
				return evaluation.matchesCompleteReturnPrePos(getRule(), t);
			}
		});
	}

	public Rule<T> getRule() {
		return rule;
	}		
}
