package OptionalHelper;

import java.util.Optional;

public class Optionals {

	public static boolean isAdjacent(Optional<Integer> first, Optional<Integer> second)
	{
		if(first == null || second == null || !first.isPresent() || !second.isPresent())
		{
			return false;
		}
		
		return first.get()+1 == second.get();
	}
	
	public static Integer orZero(Optional<Integer> value)
	{
		if(value == null || !value.isPresent())
		{
			return 0;
		}
		
		return value.get();
	}
	
	public static <T extends Number & Comparable> Optional<T> min(Optional<T>... values)
	{
		Optional<T> result = Optional.empty();
		
		if(values != null)
		{
			T maxSoFar = null;
			
			for(Optional<T> each : values)
			{
				if(each == null || !each.isPresent())
				{
					continue;
				}
				
				if(maxSoFar == null)
				{
					maxSoFar = each.get();
					continue;
				}
				
				T eachNumber = each.get();
				if(maxSoFar.compareTo(eachNumber)>0)
				{
					maxSoFar = each.get();
				}
			}
			
			result = Optional.ofNullable(maxSoFar);
		}
		
		return result;
	}
	
	public static <T extends Number & Comparable> Optional<T> max(Optional<T>... values)
	{
		Optional<T> result = Optional.empty();
		
		if(values != null)
		{
			T maxSoFar = null;
			
			for(Optional<T> each : values)
			{
				if(each == null || !each.isPresent())
				{
					continue;
				}
				
				if(maxSoFar == null)
				{
					maxSoFar = each.get();
					continue;
				}
				
				T eachNumber = each.get();
				if(maxSoFar.compareTo(eachNumber)<0)
				{
					maxSoFar = each.get();
				}
			}
			
			result = Optional.ofNullable(maxSoFar);
		}
		
		return result;
	}
}
