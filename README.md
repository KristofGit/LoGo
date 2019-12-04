LoGo: Combining Local and Global Techniques for Predictive Business Process Monitoring
=============

Prototypical implementation for the submission: Kristof Böhmer and Stefanie Rinderle-Ma: LoGo: Combining Local and Global Techniques for Predictive Business Process Monitoring. 

The given prototypical implementation of the presented prediction approach was applied during the evaluation of the submission. It analyzes process execution logs to create a prediction models (based on SPRs) which enables to predict future behavior for (incomplete) process execution traces. Further, it contains additional algorithms which were only utilized during the evaluation (e.g., to analyze the the prediction performance). Finally, it holds a helper project with contains auxiliary functions which ease the handling of collections but also additional supportive data structures. The main projects are:

ReadData
---------
The implementation was split into multiple projects. First, the **ReadData** project which enables to read and prepare information from XES and CSV based execution logs as input for the following steps. Hereby, it especially focuses on real life logs, i.e., BPIC and Helpdesk. Further, details on these log sources are given in the paper. Note, each log utilizes different parameter names and approaches to encode aspects, such as, the start and end of activity executions. Hence, the respective log source can be configured based on the DataSource enumeration as BPIC2012 or Helpdesk. Additional configurations can be set in the Config project which holds a ConfigEvaluation class which contains all the configuration values utilizes by the projects described in the following (e.g., the minimum confidence threshold). 

SequentialPredictionRule
---------

Secondly, the **SequentialPredictionRule** project implements a SPR mining approach which extends on existing sequential rule mining algorithms. Further this project also holds the definition of the SPRs to represent activity sequences and relation based on given log files as rules. In accordance to the description in the paper this project starts by initializing the final prediction model (SPR set, resp.) with minimal rules (i.e., each SPR contains only pre and fut conditions) based on the events in the given log files. Subsequently, iterative rule extension and verification steps are applied. Rule extension extends given SPRs by adding additional conditions to generate novel rule candidates. These candidates are subsequently analyzed to determine their confidence, if the confidence is found as being below a user configured threshold it is not integrated in the final prediction model. By repeating the last two steps multiple times in a row, SPRs which a user chosen conformance are generated. 

EvaluationAndHistModelExt
---------

Fourthly, the **EvaluationAndHistModelExt** project combines all the results from the other projects. Initially, it reads process behavior from process execution logs and splits this behavior into training and testing data. Subsequently, the training data is mined to create a prediction model based on Sequential Prediction Rules (Global, main technique) and Probabilities (Local, extended fallback technique). Afterwards, sub traces are generated from the testing data and their future is predicted based on LoGo. The information collected throughout the related steps are utilized to calculate performance metrics (i.e., activity prediction accuracy, and Mean Absolute Error for timestamp prediction) which are required to assess the prediction performance of the presented approach. 


