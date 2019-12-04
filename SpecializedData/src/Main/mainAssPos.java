package Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import Activity.ActivityCondition;
import Activity.ActivityExtendAssRules;
import AssConfiguration.AssConfig;
import AssociationRuleMining.AssMine;
import AssociationRuleMining.EvaluateAssRule;
import AssociationRuleMiningDataBase.DataElementSeqStorage;
import DataObjects.LogEvent;
import RuleComponents.RuleCompartment;
import RuleComponents.RuleFragment;


public class mainAssPos {

	public static void main(String[] args) throws MalformedURLException {

		DataElementSeqStorage<LogEvent> storage = TestDataSequenceAssRuleNormal.getDataSequence();
		
		RuleFragment<LogEvent> ruleFrag = RuleFragment.newRule()
				.current(ActivityCondition.of("4"))
				.post(ActivityCondition.of("3"));
				
		storage = storage.cutToLengthIfPossible(ruleFrag);
		
		AssConfig config = AssConfig.newConf();
		config.minConf(0.5);
		config.minSup(0.3);

		config.setMaxRuleLength(6);
		
		AssMine<LogEvent> mine = new AssMine(config, ruleFrag, storage, new EvaluateAssRule(), new ActivityExtendAssRules());
		
		List<List<RuleCompartment<LogEvent>>> rules = mine.mineAssociationRules();
		
		String displayString = mine.toString();
		
		System.out.print(displayString);
	}

}
