import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JTextArea;

import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Classifier {
	private String dataPath;
	//private String[] options = new String[1];
	private Instances trainingInstances;
	private J48 classifier;
	private JTextArea output;

	public Classifier(String dataPath, JTextArea out){
		this.dataPath=dataPath;
		this.output=out;
	}
	
	
	public void trainSplitClassifier(Instances train, boolean best) throws Exception{
		J48 cls = new J48();
		
		Remove remove = new Remove();
		remove.setAttributeIndices("1,28");
		remove.setInvertSelection(false);
		remove.setInputFormat(train);
		train= Filter.useFilter(train, remove);
		
		if(best){
			Remove removeWorst = new Remove();
			removeWorst.setAttributeIndices("6,26");
			removeWorst.setInvertSelection(false);
			removeWorst.setInputFormat(train);
			train = Filter.useFilter(train, removeWorst);
		}
		
		trainingInstances=train;

		trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
		cls.buildClassifier(trainingInstances);
		output.append(cls.toSummaryString());
		classifier=cls;
	}

	public void trainClassifier(boolean best) throws Exception{
		J48 cls = new J48();
		Instances data = null;
		try{
			
		data = new Instances(new BufferedReader(new FileReader(dataPath)));
		data.setClassIndex(data.numAttributes() - 1);
		Remove remove = new Remove();
		remove.setAttributeIndices("1,28");
		remove.setInvertSelection(false);
		remove.setInputFormat(data);
		data = Filter.useFilter(data, remove);
		if(best){
			Remove removeWorst = new Remove();
			removeWorst.setAttributeIndices("6,26");
			removeWorst.setInvertSelection(false);
			removeWorst.setInputFormat(data);
			data = Filter.useFilter(data, removeWorst);
		}
		trainingInstances=data;
		cls.buildClassifier(data);
		output.append(cls.toSummaryString());
		classifier=cls;
		} catch (FileNotFoundException e) {
			output.append("Test data file not found in selected path! Exiting...\n");
			output.append(dataPath.toString());
			e.printStackTrace();
		}
		
		
	}
	
	public J48 getClassifier(){
		return classifier;
	}
	
	public Instances getTrainingInstances(){
		return trainingInstances;
	}

}