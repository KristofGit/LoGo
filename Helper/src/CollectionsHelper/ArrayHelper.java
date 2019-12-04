package CollectionsHelper;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;

public class ArrayHelper {

	public static boolean hasValues(Object... list)
	{
		return list != null && list.length>0;
	}
	
	public static <T> boolean contains(T[] enums, T contains)
	{
		boolean result = false;
		
		if(enums != null && contains != null)
		{
			for(T eachEnum : enums)
			{
				if(Objects.equals(eachEnum, contains))
				{
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
}
