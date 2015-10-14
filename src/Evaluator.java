import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JTextArea;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Evaluator {

	private Evaluation eval;
	private String testOption;
	private Classifier classifier;
	private String testingPath;
	private String trainingPath;
	private JTextArea output;
	private double splitPerc;
	private boolean bestAtt;

	public Evaluator(Classifier classifier, String testingPath, String trainingPath, String testOption, int perc, JTextArea output, boolean bestatt) {
		this.classifier = classifier;
		this.testingPath = testingPath;
		this.trainingPath = trainingPath;
		this.testOption = testOption;
		this.output = output;
		this.splitPerc=(perc/100.0);
		this.bestAtt = bestatt;
		
	}
	
	private void trainingTest() throws Exception{
		classifier.trainClassifier(bestAtt);
		eval = new Evaluation(classifier.getTrainingInstances());
		eval.evaluateModel(classifier.getClassifier(),classifier.getTrainingInstances());
	}
	
	private void crossValidation() throws Exception{
		classifier.trainClassifier(bestAtt);
		eval = new Evaluation(classifier.getTrainingInstances());
		eval.crossValidateModel(classifier.getClassifier(), classifier.getTrainingInstances(), 10, new Random(1));
	}
	
	private void testingTest() throws Exception{
		classifier.trainClassifier(bestAtt);
		Instances data = new Instances(new BufferedReader(new FileReader(testingPath)));
		if(bestAtt){
			Remove removeWorst = new Remove();
			removeWorst.setAttributeIndices("6,26");
			removeWorst.setInvertSelection(false);
			removeWorst.setInputFormat(data);
			data = Filter.useFilter(data, removeWorst);
		}
		data.setClassIndex(data.numAttributes() - 1);
		eval = new Evaluation(data);
		eval.evaluateModel(classifier.getClassifier(), data);
	}
	
	private void percentageSplit(double perc) throws Exception{
		Instances data = null;
		try {
			data = new Instances(new BufferedReader(new FileReader(trainingPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int trainSize = (int) Math.round(data.numInstances() * perc);
		int testSize = data.numInstances() - trainSize;
		Instances train = new Instances(data, 0, trainSize);
		Instances test = new Instances(data, trainSize, testSize);
		
		classifier.trainSplitClassifier(train, bestAtt);
		Remove remove = new Remove();
		remove.setAttributeIndices("1,28");
		remove.setInvertSelection(false);
		remove.setInputFormat(test);
		test= Filter.useFilter(test, remove);
		if(bestAtt){
			Remove removeWorst = new Remove();
			removeWorst.setAttributeIndices("6,26");
			removeWorst.setInvertSelection(false);
			removeWorst.setInputFormat(test);
			test = Filter.useFilter(test, removeWorst);
		}
		test.setClassIndex(test.numAttributes() - 1);
		
		eval = new Evaluation(test);
		eval.evaluateModel(classifier.getClassifier(),test);
	}

	public void evaluate() throws Exception {
		double elapsedTime;
		long tStart = System.currentTimeMillis();

		if(testOption.equals("train")){
			trainingTest();
		}
				
		else if (testOption.equals("cross")) {
			crossValidation();
		} else if (testOption.equals("test")) {
			testingTest();
		}
		else if (testOption.equals("percentage")){
			percentageSplit(splitPerc);
		}

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		elapsedTime = tDelta / 1000.0;

		output.append(eval.toSummaryString());
		output.append("Evaluation time:\t\t" + elapsedTime + "s\n\n");
		output.append(eval.toMatrixString() + "\n");

	}

	

}
