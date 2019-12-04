package AssociationRuleMiningDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import Objects.UniqueObject;

/* Enables to work with sublists while at the same time enabling to access the raw original data if 
 * necessary 
 */
public class DataElementSequence<T> extends UniqueObject{

	private final List<ADataElement<T>> values = new ArrayList<>();
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(int i=0;i<values.size();i++)
		{
			if(i==firstIndex)
			{
				builder.append(">>>>");
			}
			
			builder.append(">"+i+ " " + values.get(i) + " ");
			
			if(i==lastIndex)
			{
				builder.append("<<<<");
			}
		}
		
		
		return builder.toString();
	}

	private final int firstIndex, lastIndex; //first index (inclusive), last index (inclusive)
	
	public Set<ADataElement<T>> getUniqueDataElements()
	{		
		return new HashSet<>(getIndexConsideringRepresentation());
	}
		
	public List<ADataElement<T>> getData()
	{
		return Collections.unmodifiableList(getIndexConsideringRepresentation());
	}
	
	public int getFirstIndex()
	{
		return firstIndex;
	}
	
	private List<ADataElement<T>> getIndexConsideringRepresentation()
	{		
		return values.subList(firstIndex, lastIndex);
	}
	
	public List<ADataElement<T>> getRawData()
	{
		return Collections.unmodifiableList(values);
	}
	
	public DataElementSequence<T> getRawSequenceData()
	{
		return new DataElementSequence<T>(getRawData());
	}

	public boolean isRawEmpty()
	{
		return values.isEmpty();
	}
	
	//bis to
	public DataElementSequence<T> setSequenceNonInclusiveEndIndexTo(int to)
	{		
		if(to > values.size())
		{
			to = values.size();
		}
		
		if(to < firstIndex)
		{
			to = firstIndex;
		}
				
		return of(firstIndex, to, this);
	}
	
	public int relativeToAbsoluteIndex(int relativeIndex)
	{
		return relativeIndex+firstIndex;
	}
	
	public DataElementSequence<T> setSequenceStartIndexTo_AFTER(int to)
	{
		return setSequenceStartIndexTo(to+1);
	}
	
	//ab to
	public DataElementSequence<T> setSequenceStartIndexTo(int to)
	{
		if(to > lastIndex)
		{
			to = lastIndex;
		}
		
		if(to <0)
		{
			to = 0;
		}
		
		return of(to, lastIndex, this);
	}
	
	public DataElementSequence<T> resetToStartIndexToZero()
	{
		return of(0, lastIndex, this);
	}
	
	public DataElementSequence<T> clone()
	{
		return of(firstIndex, lastIndex, this);
	}
	
	public DataElementSequence<T> resetEndIndexToMax()
	{
		return of(firstIndex, values.size(), this);
	}
			
	public DataElementSequence<T> resetStartEndIndexToMax()
	{
		return of(0, values.size(), this);
	}
	
	private DataElementSequence(int from, int to, List<ADataElement<T>> values)
	{		
		this.values.addAll(values);
		this.firstIndex = from;
		this.lastIndex = to;
	}
	
	private DataElementSequence(List<ADataElement<T>> values)
	{		
		this(0, values.size(), values);
	}
	
	private DataElementSequence<T>of(int from, int to, DataElementSequence<T> originalList)
	{
		DataElementSequence<T> result = new DataElementSequence<>(from, to, originalList.getRawData());
		result.set(this);
		return result;
	}
	
	public static <T> DataElementSequence<T>of(List<ADataElement<T>> list)
	{
		return new DataElementSequence<>(list);
	}
	
	public static <T> DataElementSequence<T>of(ADataElement<T>... list)
	{
		return new DataElementSequence<>(Arrays.asList(list));
	}
	
	public Optional<Integer> indexOf(ADataElement<T> dataElement)
	{
		int index = getIndexConsideringRepresentation().indexOf(dataElement);
		if(index < 0)
		{
			return Optional.empty();
		}
		
		return Optional.of(index);
	}
}
