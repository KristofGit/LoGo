package AssociationRuleMining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import AssConfiguration.AssConfig;
import AssociationRuleBase.AEvaluateRule;
import AssociationRuleBase.AExtendRule;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import CollectionsHelper.ListHelper;
import RuleComponents.RuleCompartment;
import RuleComponents.RuleFragment;

public class AssMine<T> {

	/* 1) Create inital minimal rule set
	 * 2) Expand the rules
	 * 3) Filter the rules for further expansions
	 */
	
	private final AssConfig config;
	private final RuleFragment<T> fragment;
	private final DataElementSeqStorage<T> storage;
	private final AEvaluateRule<T> evaluation;
	private final AExtendRule<T> ruleExtension;
	
	private final List<List<RuleCompartment<T>>> ruleCollection = new ArrayList<>();
	
	public AssMine(
			AssConfig config,
			RuleFragment<T> rFragment,
			DataElementSeqStorage<T> storage,
			AEvaluateRule<T> evaluation,
			AExtendRule<T> ruleExtension)
	{
		this.config = config;
		this.fragment = rFragment;
		this.storage = storage;
		this.evaluation = evaluation;
		this.ruleExtension = ruleExtension;
	}
	
	public List<List<RuleCompartment<T>>> mineAssociationRules()
	{
		createInitalRuleSet();
		filterLastRuleSetForSupportAndLength();
		while(!isLastRuleCollectionEmpty())
		{
			createNextRuleSet();
			filterLastRuleSetForSupportAndLength();
		}
		filterAllRuleSetsForConfAndLift();
		removeEmptyRuleCollections();
		freeMemory();
		
		return ruleCollection;
	}
	
	private void createInitalRuleSet()
	{
		List<RuleCompartment<T>> ruleCompartments = CreateInitalAssRules.createInitalRuleSet(fragment, storage, ruleExtension);
		ruleCollection.add(ruleCompartments);
	}
	
	private void createNextRuleSet()
	{
		List<RuleCompartment<T>> lastRules = ListHelper.getLast(ruleCollection);
		
		List<RuleCompartment<T>> newRules = new ArrayList<RuleCompartment<T>>();
 		
		/*newRules = lastRules.stream().parallel().map(eachRule->{
			List<RuleCompartment<T>> singleNewRules = RuleCompartment.spawnNewPotentialRules(eachRule, evaluation, ruleExtension);
			return singleNewRules;
		}).flatMap(x->x.stream()).collect(Collectors.toList());
		*/

		for(RuleCompartment<T> eachRule : lastRules)
		{
			List<RuleCompartment<T>> singleNewRules = RuleCompartment.spawnNewPotentialRules(eachRule, evaluation, ruleExtension);
			newRules.addAll(singleNewRules);
		}
		
		if(!ListHelper.isNullOrEmpty(newRules))
		{
			System.out.println("Current Generation Rule Size:"+newRules.get(0).getRuleSize());
		}
		
		ruleCollection.add(newRules);
	}
	
	private boolean isLastRuleCollectionEmpty()
	{
		List<RuleCompartment<T>> lastRules = ListHelper.getLast(ruleCollection);
		return lastRules.isEmpty();
	}
	
	private void freeMemory()
	{
		ruleCollection.forEach(lastRules->{
			
			if(!ListHelper.isNullOrEmpty(lastRules))
			{
				System.out.println("Current Filter Rule Size:"+lastRules.get(0).getRuleSize());
			}
			
			Iterator<RuleCompartment<T>> iter = lastRules.iterator();
			
			while(iter.hasNext())
			{
				RuleCompartment<T> eachRule = iter.next();
				eachRule.freeMemoryFromStoredTraces();
			}
		});
	}
	
	private void removeEmptyRuleCollections()
	{
		Iterator<List<RuleCompartment<T>>> iter = ruleCollection.iterator();
		
		while(iter.hasNext())
		{
			List<?> eachColl = iter.next();
			
			if(eachColl.isEmpty())
			{
				iter.remove();
			}
		}
	}
	
	private void filterAllRuleSetsForConfAndLift()
	{
		//FILTER WHEN EVERYHTING IS DONE OTHERWISE 
		//we kick out a lot of rules preliminary before they can grow large enough to become
		//significant with regards to their confidence
		//==> deal with the increase in complexity based on rule length and min support
		ruleCollection.forEach(lastRules->{
			
			if(!ListHelper.isNullOrEmpty(lastRules))
			{
				System.out.println("Current Filter Rule Size:"+lastRules.get(0).getRuleSize());
			}
			
			Iterator<RuleCompartment<T>> iter = lastRules.iterator();
			
			while(iter.hasNext())
			{
				RuleCompartment<T> eachRule = iter.next();
										
				int ruleLength = eachRule.getRuleSize();
							
				if(ruleLength<config.getMinRuleLength())
				{
					iter.remove();
					continue;
				}
				
				double conf = eachRule.calculateConfidence(storage, evaluation);
				
				if(conf < config.minConf())
				{
					iter.remove();
					continue;
				}
			
				if(config.minLift()>0)
				{
					double lift = eachRule.calculateLift(storage, evaluation);
					
					if(lift < config.minLift())
					{
						iter.remove();
						continue;
					}
				}
			}
		});
	}
	
	private void filterLastRuleSetForSupportAndLength()
	{
		List<RuleCompartment<T>> lastRules = ListHelper.getLast(ruleCollection);

		Iterator<RuleCompartment<T>> iter = lastRules.iterator();
		
		while(iter.hasNext())
		{
			RuleCompartment<T> eachRule = iter.next();
			
			int ruleLength = eachRule.getRuleSize();
			
			if(ruleLength>config.getMaxRuleLength())
			{
				iter.remove();
				continue;
			}
			
			if(config.minSup()>0)
			{
				double sup = eachRule.calculateSupport(storage, evaluation);
				
				if(sup<config.minSup())
				{
					iter.remove();
					continue;
				}
			}
		}
	}	
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(List<RuleCompartment<T>> eachCol : ruleCollection)
		{
			builder.append("Rule Size:" + eachCol.get(0).getRuleSize());
			builder.append(System.getProperty("line.separator"));

			for(RuleCompartment<T> eachColForTrueNow : eachCol)
			{
				builder.append(eachColForTrueNow.toString());
				builder.append(System.getProperty("line.separator"));
			}
			
			builder.append(System.getProperty("line.separator"));
		}
		
		return builder.toString();
	}
}
