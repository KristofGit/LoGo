package Randomization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper {

	private static Random generator = ThreadLocalRandom.current();

	public static Random getRandom()
	{
		return generator;
	}
	
	public static <T> T getRandom(List<T> values, int firstIndex, int lastIndex)
	{
		if(values == null)
		{
			return null;
		}
		
		List<T> list = values.subList(firstIndex, lastIndex);
		
		if(list.isEmpty())
		{
			return null;
		}
		
		return getRandom(list, 1).get(0);
	}
	
	public static <T> List<T> getRandom(List<T> values, int amount)
	{
		List<T> result = new ArrayList<T>();
		
		if(values == null)
		{
			return result;
		}
		amount = Math.min(values.size(), amount);
		
		Set<Integer> alradyChosenInteger = new HashSet<>();
		
		while(result.size()<amount)
		{
			int randomElement = getRandom().nextInt(values.size());
			
			if(alradyChosenInteger.contains(randomElement))
			{
				continue;
			}
			alradyChosenInteger.add(randomElement);
			
			result.add(values.get(randomElement));
		}
		
		return result;			
	}
	
	public static <T> T getRandom(List<T> values)
	{
		if(values == null || values.isEmpty())
		{
			return null;
		}
		
		int randomIndex = getRandom().nextInt(values.size());
		
		return values.get(randomIndex);
	}
	
	public static <T> T getRandom(T[] values)
	{
		if(values == null || values.length == 0)
		{
			return null;
		}
		
		int randomIndex = getRandom().nextInt(values.length);
		
		return values[randomIndex];
	}
	
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz)
	{
	    int index = getRandom().nextInt(clazz.getEnumConstants().length);
	    return clazz.getEnumConstants()[index];
	}
}
