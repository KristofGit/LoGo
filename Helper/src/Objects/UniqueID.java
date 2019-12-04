package Objects;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueID {

	private final static AtomicInteger id = new AtomicInteger(0);
	
	public static int getUniqueID()
	{
		return id.getAndIncrement();
	}
}
