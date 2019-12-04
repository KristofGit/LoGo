package AssociationRuleMiningDataBase;

public abstract class ADataElement<T> {

	private T value;
	
	@Override
	public String toString() {
		return value.toString();
	}

	public T getValue()
	{
		return value;
	}
	
	protected void setValue(T value)
	{
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return getHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ADataElement other = (ADataElement) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!isEqual(other))
			return false;
		return true;
	}
	
	protected abstract int getHashCode();
	
	protected abstract boolean isEqual(ADataElement<T> other);
}
