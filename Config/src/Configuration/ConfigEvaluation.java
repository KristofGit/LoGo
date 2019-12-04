package Configuration;

public class ConfigEvaluation {
	
	//Note, each evaluation case got its own config, disable/enable the sections accordingly
	//You will also need to adapt the main evaluation class => MainPropAndRule to decide if control flow or temporal prediction should be evaluated
	
	//WORKS WELL FOR HELPDESK DURATION - WITH RULES
	
	//config for the evaluation data preparation
	public final static DataSource dataToUse = DataSource.Helpdesk;
	public static double DataSeparataionForTraining = 2.0/3; 
	
	//config for the global prediction approach.
	public static double expectedRuleConf = 0.80;
		
	//config for the local prediction fallback approach
	public static double EqualEventIndexWideningRelative = 0.2;
	public static double MostSimTracesTaken = 0.05;
	public static int FixedAmountOfMostSimTraces = 400;
	public static int PrefixForSimCalculation = 10;
	public static double histMostLikelySeparation = 0.1;
	
	//WORKS WELL FOR BPIC ACTIVIVTY WITH RULE!
	
	/*
	public final static DataSource dataToUse = DataSource.BPIC_2012;
	public static double DataSeparataionForTraining = 2.0/3;
		
	public static double EqualEventIndexWideningRelative = 0.05; 
	public static double MostSimTracesTaken = 0.58;
	public static int FixedAmountOfMostSimTraces = 0;
	public static int PrefixForSimCalculation = 10; 
	public static double histMostLikelySeparation = 0.1;
	
	public static double expectedRuleConf = 0.80; */

	//WORKS WELL FOR HELPDEST ACTIVITY WITH RULE!
	/*
	public final static DataSource dataToUse = DataSource.Helpdesk;
	public static double DataSeparataionForTraining = 2.0/3;
	
	public static double EqualEventIndexWideningRelative = 0.05;
	public static double MostSimTracesTaken = 0.05;
	public static int FixedAmountOfMostSimTraces = 10; 
	public static int PrefixForSimCalculation = 10;
	public static double histMostLikelySeparation = 0.1;
	
	public static double expectedRuleConf = 0.80; */
	
	//WORKS WELL FOR BPIC DURATION WITH RULES
	
	/*
    public final static DataSource dataToUse = DataSource.BPIC_2012;
	public static double DataSeparataionForTraining = 2.0/3; 
	   
	public static double EqualEventIndexWideningRelative = 0.2;
	public static double MostSimTracesTaken = 0.58;
	public static int FixedAmountOfMostSimTraces = 50;
	public static int PrefixForSimCalculation = 20;
	public static double histMostLikelySeparation = 0.2;
	
	public static double expectedRuleConf = 0.80; */
	



	
	
	
	

}
