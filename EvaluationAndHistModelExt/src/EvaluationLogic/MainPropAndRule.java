package EvaluationLogic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import AccuracyMeasure.Accuracy;
import Activity.ActivityCondition;
import Activity.ActivityExtendAssRules;
import ActivityHelper.ActivityClass;
import AssConfiguration.AssConfig;
import AssData.MatchingResultExact;
import AssociationRuleMining.AssMine;
import AssociationRuleMining.EvaluateAssRule;
import AssociationRuleMiningDataBase.ADataElement;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import AssociationRuleMiningDataBase.DataElementSequence;
import BaseObject.Tripple;
import BaseObject.Tuple;
import CollectionsHelper.ListHelper;
import Configuration.ConfigEvaluation;
import Configuration.DataSource;
import DataObjects.LogEvent;
import HelperObjects.ActivityRuleCollection;
import HelperObjects.NextEvent;
import Histograms.CustomClass;
import Histograms.HistogramCustomClasses;
import LogRepresentation.ExecutionEvent;
import LogRepresentation.ExecutionTrace;
import LogRepresentation.InternalEventType;
import LogRepresentation.ProcessModel;
import LogRepresentation.TrainingTesting;
import MeanAverageError.MAE;
import PredictionModel.PropabilityPredictionActivityAndTime;
import ReadData.ReadDataIntoObjects;
import RuleComponents.RuleCompartment;
import RuleComponents.RuleFragment;
import TraceSimilarity.DamerauLevenshtein_activity_and_dur;
import TraceSimilarity.DamerauLevenshtein_activity_and_dur.ItemEqualityNew;

public class MainPropAndRule {
	
	public static void main(String[] args) {
	
		/* Uncomment one of the evaluation methods given below to run the evaluation described in the paper
		 * First method => activity prediction, second method => timestamp prediction
		 */
		
		testActivityPropabilityANDRuleBasedPrediction();
		//testNextEventTimestampPropabilityAndRULEBasedPrediction();
	}
		
	private static void testNextEventTimestampPropabilityAndRULEBasedPrediction()
	{
		System.out.println("start reading");
		
		ReadDataIntoObjects readBPIC12 = new ReadDataIntoObjects();
		ProcessModel processes = readBPIC12.readData(ConfigEvaluation.dataToUse, false);

		System.out.println("finished reading");
		
		TrainingTesting data = processes.separateData();
		
		ProcessModel training = data.getDataForTraining();
		ProcessModel testing = data.getDataForTesting();
			
		MAE evaluationProp = new MAE();
		MAE evaluationRule = new MAE();

		predictionRuleGeneration(training);	
		
		for(ExecutionTrace eachTesting : testing.getAllTraces())
		{
			int lengthOfTrace = eachTesting.getTraceLength();
			//start with 1 as predicting the start event is not useful
			for(int currentPosition=1;currentPosition<lengthOfTrace-2;currentPosition++)
			{
				Instant activityDurObserved = eachTesting.getEvent(currentPosition+1).getPointInTime(); //to get the followup event

				Instant activityDurExpected_Prop = PropabilityPredictionActivityAndTime.
						determineMostProablyFollowUpEventTimestamp(eachTesting, currentPosition, null, training, null);

				String activityNameCurrent = eachTesting.getEvent(currentPosition).getActivityName();
				//String activityNameNEXTObserved = eachTesting.getEvent(currentPosition+1).getActivityName(); //to get the followup event

				Optional<String> activityNameNEXT_RULE = predictFolowupActivityBasedOnRule(activityNameCurrent, eachTesting.getTraceWithEventsBeforeAndIncludingIndex(currentPosition));
				String activityNameNEXT_PROPABILITY = PropabilityPredictionActivityAndTime.determineMostProablyFollowUpActivity(eachTesting, currentPosition, training);

				Instant activityDurExpected_Rule = PropabilityPredictionActivityAndTime.
						determineMostProablyFollowUpEventTimestamp(eachTesting, currentPosition, activityNameNEXT_RULE.orElse(activityNameNEXT_PROPABILITY), training, null);
				//Instant activityDurExpected_Rule = PropabilityPredictionActivityAndTime.
				//		determineMostProablyFollowUpEventTimestamp(eachTesting, currentPosition, activityNameNEXTObserved, training, null);
	
				evaluationProp.newPrediction(activityDurObserved, activityDurExpected_Prop);
				evaluationRule.newPrediction(activityDurObserved, activityDurExpected_Rule!=null?activityDurExpected_Rule:activityDurExpected_Prop);
				
				System.out.println("Done - Propability AND RULE");
				
				evaluationRule.print();	

			}
		}
		
		System.out.println("DONE COMPLETELY!");
		
		System.out.println("Done - Propability AND RULE");
		
		evaluationRule.print();	
	}

	private static void testActivityPropabilityANDRuleBasedPrediction() {
		System.out.println("start reading");
		
		ReadDataIntoObjects readBPIC12 = new ReadDataIntoObjects();
		ProcessModel processes = readBPIC12.readData(ConfigEvaluation.dataToUse, false);

		System.out.println("finished reading");
		
		TrainingTesting data = processes.separateData();
		
		ProcessModel training = data.getDataForTraining();
		ProcessModel testing = data.getDataForTesting();
		
		Accuracy evaluationPropability = new Accuracy();
		Accuracy evaluationPropabilityAndRule = new Accuracy();

		predictionRuleGeneration(testing);	
				
		int predictionTotal = 0;
		int predictedRule=0;
		int predictedCorrectRule=0;
		int predictedINCorrectRule=0;

		int predictedCorrectPropability=0;
		int predictedINCorrectPropability=0;

		for(ExecutionTrace eachTesting : testing.getAllTraces())
		{
			int lengthOfTrace = eachTesting.getTraceLength();
			//start with 1 as predicting the start event is not useful
			for(int currentPosition=1;currentPosition<lengthOfTrace-2;currentPosition++)
			{				
				String activityNameCurrent = eachTesting.getEvent(currentPosition).getActivityName();	 
				
				String activityNameNEXTObserved = eachTesting.getEvent(currentPosition+1).getActivityName(); //to get the followup event
				
				
				HistogramCustomClasses<String,ActivityClass> HIST = PropabilityPredictionActivityAndTime.determineMostProablyFollowUpActivityTESTRETURNHIST(eachTesting, currentPosition, training);

				String activityNameNEXT_PROPABILITY = PropabilityPredictionActivityAndTime.determineMostProablyFollowUpActivity(eachTesting, currentPosition, training);
				
				Optional<String> activityNameNEXT_RULE = predictFolowupActivityBasedOnRule(activityNameCurrent, eachTesting.getTraceWithEventsBeforeAndIncludingIndex(currentPosition));
				 
				boolean ruleRight = false, propRight = false;
				predictionTotal++;
				if(activityNameNEXT_RULE.isPresent())
				{
					predictedRule++;
					if(activityNameNEXTObserved.equals(activityNameNEXT_RULE.get()))
					{
						ruleRight = true;
						predictedCorrectRule++;
					}
					else
					{
						predictedINCorrectRule++;
					}
				}
								
				if(activityNameNEXTObserved.equals(activityNameNEXT_PROPABILITY))
				{
					propRight=true;
					predictedCorrectPropability++;
				}
				else
				{
					predictedINCorrectPropability++;
				}
				
				if(activityNameNEXT_RULE.isPresent())
				{
				if(propRight==false && ruleRight==true)
				{
					System.out.println("Rule got it right");
				}
				else if(propRight==true && ruleRight==false)
				{
					System.out.println("Rule got it false");
				}
				}
				
				String ruleActivity = null;
				
				if(activityNameNEXT_RULE.isPresent())
				{
					ruleActivity = activityNameNEXT_RULE.get();
				}
				else
				{
					ruleActivity = activityNameNEXT_PROPABILITY;
				}
				
				evaluationPropabilityAndRule.analyze(activityNameNEXTObserved, ruleActivity);					
				evaluationPropability.analyze(activityNameNEXTObserved, activityNameNEXT_PROPABILITY);			
				
				if(predictionTotal%20==0)
				{
					 			
					System.out.println("Done - Propability AND RULE");
					
					evaluationPropabilityAndRule.print();
				}
			}
		}
	
		
		System.out.println("Done - Propability AND RULE");
		
		evaluationPropabilityAndRule.print();

	}

	private static Map<ActivityRuleCollection, List<List<RuleCompartment<LogEvent>>>> ruleCache = new HashMap<>();
	
	private static Optional<String> predictFolowupActivityBasedOnRule(
			String activityNameCurrent, 
			ExecutionTrace relevantExecutionTrace) {
	
		
		//we get current activity
		//we get a trace which was cut to length, i.e., cut to the current trace index we predict upon
		
		//we want to predict post trace, hence we will not get it
		
		//hence, we check if the pre and the current part of the rule match
		
		List<ActivityRuleCollection> matchingKeys = ruleCache
				.keySet()
				.stream()
				.filter(x->x.getCurrentActivity().equals(activityNameCurrent))
				.collect(Collectors.toList());
		
		EvaluateAssRule<LogEvent> ruleEvaluation = new EvaluateAssRule<LogEvent>();

		Optional<String> activityNamePredictedByRules = Optional.empty();
				
		double confidenceMin = 0;
		int ruleLength = 0;
		RuleCompartment<LogEvent> mostConfRule = null;
		
		List<String> predictedActivitiesRule = new ArrayList<>();
		
		for(ActivityRuleCollection eachKey : matchingKeys)
		{
			//each list contains rules of a specific size
			List<List<RuleCompartment<LogEvent>>> ruleCollection = ruleCache.get(eachKey);
			
						
			for(List<RuleCompartment<LogEvent>> rules : ruleCollection)
			{
				for(RuleCompartment<LogEvent> eachRules : rules)
				{
					DataElementSequence<LogEvent> trace = relevantExecutionTrace.convert();
					MatchingResultExact matchResult = ruleEvaluation.matchesPreCur_getLastIndexOfPre(eachRules.getRule(), trace);
					
					if(matchResult.doesMatch())
					{
						String nextActivity = eachKey.getNextPostActivity();
						double confidence = eachRules.calculateConfidence(null, null); //confidence was already calculated during rule mining
						
						int length =  eachRules.getRuleSize();
						
						if(confidenceMin<=confidence && ruleLength<length)
						{
							confidenceMin = confidence;
							ruleLength=length;
							mostConfRule = eachRules;
							activityNamePredictedByRules = Optional.of(nextActivity);
						}
						
						predictedActivitiesRule.add(nextActivity);
					}					
				}					
			}
		}
		
		if(predictedActivitiesRule.stream().distinct().count() >1)
		{
			System.out.print("different opinions:");
		}
		
		return activityNamePredictedByRules;
	}
	
	private static void predictionRuleGeneration(ProcessModel testing) {
		
		System.out.println("Rule mining started");
		
		ruleCache.clear();
		
		DataElementSeqStorage<LogEvent> storage = testing.convert();
		
		AssConfig config = AssConfig.newConf();
		config.minConf(ConfigEvaluation.expectedRuleConf);
		
//		config.minSup(0.001);
		//config.setMaxRuleLength(15);

		Set<ADataElement<LogEvent>> uniqueLogEvents = storage.getUniqueDataElements();//.stream().filter(x->!x.getValue().getActivityName().equals(InternalEventType.TraceFinished.name())).collect(Collectors.toSet());
		
		EvaluateAssRule<LogEvent> ruleEvaluation = new EvaluateAssRule<LogEvent>();
				
		for(ADataElement<LogEvent> eachUniqueLogEventCurrent : uniqueLogEvents)
		{
			String activityCurrent = eachUniqueLogEventCurrent.getValue().getActivityName();

			for(ADataElement<LogEvent> eachUniqueLogEventPost : uniqueLogEvents)
			{
				String activityPost = eachUniqueLogEventPost.getValue().getActivityName();
						
				ActivityRuleCollection key = ActivityRuleCollection.of(activityCurrent, activityPost);
				
				if(ruleCache.containsKey(key))
				{
					continue;
				}
				
				if(activityCurrent.equals(InternalEventType.F.name()))
				{
					// no activity possible after the final one
					continue;
				}
				
				System.out.println("For:"+key);
				
				RuleFragment<LogEvent> ruleFrag = RuleFragment.newRule()
						.current(ActivityCondition.of(activityCurrent))
						.post(ActivityCondition.of(activityPost));
							
				AssMine<LogEvent> mine = new AssMine(config, ruleFrag, testing.convert(), ruleEvaluation, new ActivityExtendAssRules());
				List<List<RuleCompartment<LogEvent>>> rules = mine.mineAssociationRules();
				
				if(rules.stream().flatMap(List::stream).count()  != 0)
				{				
					ruleCache.put(key, rules);
					
					System.out.println("Rule Count:"+ ruleCache.values().stream().flatMap(List::stream).map(x->x.size()).collect(Collectors.summingInt(Integer::intValue)));
				}
			}
		}
		
		System.out.println("Finished rule mining");

	}
}
