package CollectionsHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ListHelper {
	public static boolean hasValues(Collection<?> list)
	{
		return list != null && !list.isEmpty();
	}
	
	public static <T> List<T> stripFirst(List<T> list)
	{
		if(list == null || list.isEmpty())
		{ 
			return list;
		}
		
		return list.subList(1, list.size());
	}
		
	public static boolean isConsecutive(List<Integer> listElements)
	{
		if(!hasValues(listElements))
		{
			return false;
		}
				
		boolean result = true;
		int last = listElements.get(0);
		for(int i=1;i<listElements.size();i++)
		{
			int cur = listElements.get(i);
			
			if(last!=cur-1)
			{
				result = false;
				break;
			}
			
			last = cur;
		}
		
		return result;
	}
	
	public static boolean isNullOrEmpty(List<?> list)
	{
		return list == null || list.isEmpty();
	}
	
	public static <T> T mostCommon(List<T> list) {
	    Map<T, Integer> map = new HashMap<>();

	    for (T t : list) {
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }

	    Entry<T, Integer> max = null;

	    for (Entry<T, Integer> e : map.entrySet()) {
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }

	    return max.getKey();
	}

	
	public static <T> T getLast(List<T> list)
	{
		T result = null;
		
		if(list == null  || list.isEmpty())
		{
			return result;
		}
		
		result = get(list, list.size()-1);
		
		return result;
	}
	
	public static <T> T get(List<T> list, int index)
	{
		T result = null;
		
		if(list == null  || list.isEmpty() || index<0 || index>list.size()-1)
		{
			return result;
		}
		
		result = list.get(index);
				
		return result;
	}
}
