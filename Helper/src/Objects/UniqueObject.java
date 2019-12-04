package Objects;

public abstract class UniqueObject {

	private int uniqueID = UniqueID.getUniqueID();

	@Override
	public int hashCode() {
		return uniqueID;
	}

	protected void set(UniqueObject other)
	{
		this.uniqueID = other.uniqueID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UniqueObject other = (UniqueObject) obj;
		if (uniqueID != other.uniqueID)
			return false;
		return true;
	}
	
	
}
