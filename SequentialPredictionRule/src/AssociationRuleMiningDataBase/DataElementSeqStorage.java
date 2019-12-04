package AssociationRuleMiningDataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator.OfDouble;
import java.util.Set;
import java.util.stream.Collectors;

import RuleComponents.RuleFragment;

public class DataElementSeqStorage<T> {

	private final List<DataElementSequence<T>> eachDataElementSequence = new ArrayList<>();
	
	private DataElementSeqStorage(List<DataElementSequence<T>> values)
	{
		this.eachDataElementSequence.addAll(values);
	}
	
	public static <T> DataElementSeqStorage<T> of(List<DataElementSequence<T>> elements)
	{
		return new DataElementSeqStorage<>(elements);
	}

	public DataElementSeqStorage<T> remove(List<DataElementSequence<T>> toRemove)
	{
		List<DataElementSequence<T>> remaining = new ArrayList<>();
		
		for(DataElementSequence<T> eachOrignal : eachDataElementSequence)
		{
			boolean preserve = false;
			for(DataElementSequence<T> eachRemove : toRemove)
			{
				if(eachOrignal == eachRemove)
				{
					preserve = true;
					break;
				}
			}	
			
			if(preserve)
			{
				remaining.add(eachOrignal);
			}
		}
		
		return of(remaining);
	}
	
	/* Cycle through each data sequence
	 * If the data sequence contains a pair of elements matching to the rule fragment
	 * then cut it on that position (i.e., just after the rule fragment)
	 */
	public DataElementSeqStorage<T> cutToLengthIfPossible(RuleFragment<T> rule)
	{
		List<DataElementSequence<T>> filteredData = new ArrayList<>();
		
		for(DataElementSequence<T> eachSequence : eachDataElementSequence)
		{		
			List<Integer> indexesForCuts = rule.indexOfCurrentsWithAdjacentPOST(eachSequence);
			
			Optional<Integer> max = indexesForCuts.stream().max((x,y)->Integer.compare(x, y));
			
			if(max.isPresent())
			{
				//+1 to include the adjacent post
				DataElementSequence<T> cutSequence = eachSequence.setSequenceNonInclusiveEndIndexTo(max.get()+1);
				
				filteredData.add(cutSequence);
			}		
			else
			{
				filteredData.add(eachSequence);
			}
		}
		
		return new DataElementSeqStorage<T>(filteredData);
	}
	
	/* Assumption: Not all data sequences are relevant
	 * 1) Those that do not contain the current element of the rule fragment
	 * 2) The part of the data sequence after the current/post part is not valuable
	 * 
	 * TODO: should we split up the sequence into multiple or add it with multiple length 
	 * if they contain the current/post element multiple times?
	 */
	/*public DataElementSeqStorage<T> filterRuleFragment(RuleFragment<T> rule)
	{
		List<DataElementSequence<T>> filteredData = new ArrayList<>();
		
		for(DataElementSequence<T> eachSequence : eachDataElementSequence)
		{
			if(rule.containsCurrent(eachSequence))
			{
				continue;
			}
		
			List<Integer> indexesForCuts = rule.getBeforeIndexes(eachSequence);
			
			Optional<Integer> max = indexesForCuts.stream().max((x,y)->Integer.compare(x, y));
			
			if(max.isPresent())
			{
				DataElementSequence<T> cutSequence = eachSequence.cutSequenceTo(max.get());

				filteredData.add(cutSequence);
			}			
		}
		
		return new DataElementSeqStorage<T>(filteredData);
	}*/
	
	public Set<ADataElement<T>> getUniqueDataElements()
	{
		Set<ADataElement<T>> result = new HashSet<>();
		
		eachDataElementSequence.stream().forEach(x->{
			result.addAll(x.getUniqueDataElements());
		});
				
		return result;
	}
	
	public int getSequenceCount()
	{
		return eachDataElementSequence.size();
	}
	
	public List<DataElementSequence<T>> getSequences()
	{
		return Collections.unmodifiableList(eachDataElementSequence);
	}
}
