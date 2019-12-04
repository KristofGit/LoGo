package Objects;

public class Flag {

	private boolean value;
	
	public static Flag of()
	{
		return new Flag();
	}
	
	public void setFlag()
	{
		this.value = true;
	}
	
	public boolean isSet()
	{
		return this.value;
	}
}
