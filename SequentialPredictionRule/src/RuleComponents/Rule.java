package RuleComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import AssociationRuleBase.ACondition;

public class Rule<T> {

	private final List<ACondition<T>> preconditions = new ArrayList<>();
	private final RuleFragment<T> fragmentCurPost;
	
	public int getRuleSize()
	{
		return preconditions.size() + fragmentCurPost.getFragmentLength();
	}
	
	public String toString()
	{
		final String separator = " --> ";
		
		StringBuilder builder = new StringBuilder();
		
		for(ACondition<T> eachCond : preconditions)
		{
			builder.append(eachCond.toString());
			builder.append(separator);
		}
		
		builder.append(fragmentCurPost.toString());
		
		return builder.toString();		
	}
	
	
	private Rule(RuleFragment<T> fragment)
	{
		this.fragmentCurPost = fragment;
	}

	public static <T> Rule<T> of(RuleFragment<T> fragment)
	{
		return new Rule<T>(fragment);
	}	
	
	public List<ACondition<T>> getPreconditions()
	{
		return Collections.unmodifiableList(preconditions);
	}
	
	public RuleFragment<T> getCurPost()
	{
		return fragmentCurPost;
	}
	
	public Rule<T> add(ACondition<T> condition)
	{
		Rule<T> rule = of(fragmentCurPost);
		rule.preconditions.addAll(this.preconditions);
		rule.preconditions.add(condition);
		
		return rule;
	}	
}
