package RuleComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import AssConfiguration.AssConfig;
import AssociationRuleBase.ACondition;
import AssociationRuleMiningDataBase.DataElementSequence;
import OptionalHelper.Optionals;

public class RuleFragment<T> {

	private ACondition<T> current;
	private ACondition<T> post;
	
	private RuleFragment()
	{
		
	}
		
	public String toString()
	{
		final String separator = " ==> ";
		
		StringBuilder builder = new StringBuilder();
	
		builder.append(current.toString());
		builder.append(separator);
		builder.append(post.toString());
				
		return builder.toString();		
	}
	
	public static RuleFragment newRule()
	{
		return new RuleFragment();
	} 
	
	public RuleFragment current(ACondition current)
	{
		this.current = current;
		return this;
	}
	
	public RuleFragment post(ACondition post)
	{
		this.post = post;
		return this;
	}
	
	public int getFragmentLength()
	{
		int result = 0;
		
		if(current != null)
		{
			result++;
		}
		if(post != null)
		{
			result++;
		}
		
		return result;
	}
	
	public  Optional<Integer> absoluteIndexOfCurrent(DataElementSequence<T> sequence)
	{
		return current.matchesFirstAbsolutPosition(sequence);
	}
	
	public  Optional<Integer> absolutIndexOfPost(DataElementSequence<T> sequence)
	{
		return post.matchesFirstAbsolutPosition(sequence);
	}
	
	public boolean containsCurrent(DataElementSequence<T> sequence)
	{
		return current.matchesFirstAbsolutPosition(sequence).isPresent();
	}
	
	public boolean containsPost(DataElementSequence<T> sequence)
	{
		return post.matchesFirstAbsolutPosition(sequence).isPresent();
	}
	
	public boolean matches(DataElementSequence<T> sequence)	{
		return !indexOfCurrentsWithAdjacentPOST(sequence).isEmpty();
	}
		
	public List<Integer> indexOfCurrentsWithAdjacentPOST(DataElementSequence<T> sequence)	{
		
		Optional<Integer> min = Optional.empty();
		
		List<Integer> indexesOfRuleFragments = new ArrayList<>(); 
					
		do
		{	if(min.isPresent())
			{
				sequence = sequence.setSequenceStartIndexTo_AFTER(min.get());
			}			
			
			Optional<Integer> matchesCurrent = Optional.empty(), matchesPost = Optional.empty();
		
			matchesCurrent = current.matchesFirstAbsolutPosition(sequence);
			
			if(matchesCurrent.isPresent())
			{	
				//necessary as otherwise both current and post could be matched to the same index
				//if I search for the same sequence element definition in CUR and POST
				//if such come directly after one another in the sequence such as A -> A -> A it would never find anything
				sequence = sequence.setSequenceStartIndexTo_AFTER(matchesCurrent.get());
			}
			
			matchesPost = post.matchesFirstAbsolutPosition(sequence);
			
			if(Optionals.isAdjacent(matchesCurrent, matchesPost))
			{
				//great we found it
				indexesOfRuleFragments.add(matchesCurrent.get());
			}
			else if(matchesCurrent.isPresent() && matchesPost.isPresent())
			{
				//Break because we got a bad current, i.e., a current which does not behave as we want it as its not followed by the 
				//exepcted post TODO CHECK IF THIS MAKES SENSE, at least it reduces the amount of incorrect rujle predictions
				//current should become part of the preconditions and then the rules are generated again with better results
				break;
			}
			
			if(!matchesCurrent.isPresent() || !matchesPost.isPresent())
			{
				break;
			}
			
			//use min so that we do not skip over any possible match (which could happen when using max e.g. ADAB cur a post b)
			min = Optionals.min(matchesCurrent, matchesPost);
		}
		while(true);
		
		return indexesOfRuleFragments;
	}
}
