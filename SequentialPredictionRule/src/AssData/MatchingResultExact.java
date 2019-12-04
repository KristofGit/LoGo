package AssData;

import java.util.Optional;

public class MatchingResultExact implements IMatchingResultBasic{

	private Optional<Integer> lastMatchingIndexPrecondition = Optional.empty();
	
	private MatchingResultExact()
	{
		
	}
		
	private MatchingResultExact(Optional<Integer> matchPos)
	{
		this.lastMatchingIndexPrecondition = matchPos;
	}
	
	public static MatchingResultExact create(Optional<Integer> matchPos)
	{
		return new MatchingResultExact(matchPos);
	}
	
	public static MatchingResultExact create()
	{
		return new MatchingResultExact();
	}
	
	public MatchingResultExact lastMatchingIndex(Optional<Integer> pos)
	{
		if(pos != null)
		{
			this.lastMatchingIndexPrecondition = pos;
		}
		return this;
	}

	public boolean doesMatch() {
		return lastMatchingIndexPrecondition.isPresent();
	}

	public Optional<Integer> getLastMatchingIndex() {
		return lastMatchingIndexPrecondition;
	}
}
