package DataObjects;

public enum EventContentType {

	Activity(ContentDataType.Discreet),
	Resource(ContentDataType.Discreet),
	Timestamp(ContentDataType.Discreet);
	
	private ContentDataType dataType;
	
	private EventContentType(ContentDataType dataType)
	{
		this.dataType = dataType;
	}
}

