import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.classifiers.trees.J48;

public class GUI extends JFrame{

	private static final long serialVersionUID = -8382390132956880387L;
	private JTextField trainingField;
	private JTextField testingField;
	private JTextField percentageField;
	private int percentage;
	private String trainingPath;
	private String testingPath;
	private JTextArea outputTA;
	private JRadioButton rdbtnTrainingData;
	private JRadioButton rdbtnCrossValidation;
	private JRadioButton rdbtnTestingData;
	private JRadioButton rdbtnPercentage;
	private JButton btnStart;
	private JButton btnBestEval;
	private J48 cls;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public GUI(){
		
		setTitle("Weka J48 Classifier");
		setBounds(150, 100, 800, 546);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		loader();
		evaluationOptions();
		classifyButtons();
		
		outputTA = new JTextArea();
		getContentPane().add(outputTA);
		JScrollPane scrollPane = new JScrollPane(outputTA);
		scrollPane.setBounds(300, 10, 485, 500);
		scrollPane.getVerticalScrollBar().setBackground(Color.gray);
		getContentPane().add(scrollPane);
		
	}
	
	public void loader(){
		JLabel lblSelectTrainingFile = new JLabel("Select training data");
		lblSelectTrainingFile.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSelectTrainingFile.setBounds(14, 13, 140, 25);
		getContentPane().add(lblSelectTrainingFile);

		trainingField = new JTextField();
		trainingField.setBounds(14, 35, 230, 25);
		getContentPane().add(trainingField);
		trainingField.setColumns(10);
		
		JButton btnSelectTrainingFile = new JButton("Select file");
		btnSelectTrainingFile.setBounds(145, 65, 97, 30);
		btnSelectTrainingFile.setBackground(Color.gray);
		getContentPane().add(btnSelectTrainingFile);
		btnSelectTrainingFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF File", "arff");
				fileChooser.setFileFilter(filter);
				int temp = fileChooser.showOpenDialog(null);
				if (temp == JFileChooser.APPROVE_OPTION) {
					trainingPath = fileChooser.getSelectedFile().getAbsolutePath();
					trainingField.setText(trainingPath);
					btnStart.setEnabled(true);
				}
			}
		});
		
		JLabel lblSelectTestingFile = new JLabel("Select testing data");
		lblSelectTestingFile.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSelectTestingFile.setBounds(14, 100, 140, 25);
		getContentPane().add(lblSelectTestingFile);

		testingField = new JTextField();
		testingField.setBounds(14, 122, 230, 25);
		getContentPane().add(testingField);
		testingField.setColumns(10);
		
		JButton btnSelectTestingFile = new JButton("Select file");
		btnSelectTestingFile.setBounds(145, 152, 97, 30);
		btnSelectTestingFile.setBackground(Color.gray);
		getContentPane().add(btnSelectTestingFile);
		btnSelectTestingFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF File", "arff");
				fileChooser.setFileFilter(filter);
				int temp = fileChooser.showOpenDialog(null);
				if (temp == JFileChooser.APPROVE_OPTION) {
					testingPath = fileChooser.getSelectedFile().getAbsolutePath();
					testingField.setText(testingPath);
					rdbtnTestingData.setEnabled(true);
				}
			}
		});
		
	}
	
	public void evaluationOptions(){
		JLabel lblEvaluationOptions = new JLabel("Evaluation Options");
		lblEvaluationOptions.setFont(new Font("Arial", Font.PLAIN, 15));
		lblEvaluationOptions.setBounds(14, 205, 140, 20);
		getContentPane().add(lblEvaluationOptions);

		rdbtnTrainingData = new JRadioButton("Test with training data");
		rdbtnTrainingData.setBounds(14, 225, 180, 25);
		getContentPane().add(rdbtnTrainingData);

		rdbtnCrossValidation = new JRadioButton("10-Fold Cross-Validation");
		rdbtnCrossValidation.setBounds(14, 245, 180, 25);
		getContentPane().add(rdbtnCrossValidation);

		rdbtnTestingData = new JRadioButton("Test with separate testing data");
		rdbtnTestingData.setBounds(14, 265, 255, 25);
		getContentPane().add(rdbtnTestingData);
		
		rdbtnPercentage = new JRadioButton("Percentage split");
		rdbtnPercentage.setBounds(14, 285, 180, 25);
		getContentPane().add(rdbtnPercentage);
		percentageField = new JTextField();
		percentageField.setBounds(35, 300, 40, 25);
		getContentPane().add(percentageField);
		percentageField.setColumns(10);
		percentageField.setVisible(false);
		rdbtnPercentage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(rdbtnPercentage.isSelected()){
					percentageField.setVisible(true);
				}

			}
		});

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnTrainingData);
		buttonGroup.add(rdbtnCrossValidation);
		buttonGroup.add(rdbtnTestingData);
		buttonGroup.add(rdbtnPercentage);
		rdbtnTrainingData.setSelected(true);
		rdbtnTestingData.setEnabled(false);
		
	}
	
	
	public void classifyButtons(){
		btnStart = new JButton("Start Evaluation");
		JButton btnViewRules = new JButton("View rules");
		JButton btnViewTree = new JButton("View tree");
		btnBestEval = new JButton("Evaluation with best attributes");
		btnStart.setBounds(20, 340, 230, 25);
		btnStart.setBackground(Color.gray);
		getContentPane().add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String testOption = "";

				if (rdbtnCrossValidation.isSelected())
					testOption = "cross";
				else if (rdbtnTrainingData.isSelected()&& !trainingField.equals(""))
					testOption = "train";
				else if (rdbtnTestingData.isSelected() && !trainingField.equals("") && !testingField.equals(""))
					testOption = "test";
				else if (rdbtnPercentage.isSelected() && !percentageField.equals("")){
					String p =percentageField.getText();
					try {
					    percentage = Integer.parseInt(p);
					}
					catch(NumberFormatException ex)
					{
					    System.out.println("Exception : "+ex);
					}
					testOption = "percentage";
				}
				try {
					Classifier classifier = new Classifier(trainingPath.toString(), outputTA);
					
					new Evaluator(classifier, testingPath, trainingPath,
							testOption, percentage, outputTA, false).evaluate();
					cls = classifier.getClassifier();
					btnViewRules.setEnabled(true);
					btnViewTree.setEnabled(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStart.setEnabled(false);

		
		btnViewRules.setBounds(20, 370, 230, 25);
		btnViewRules.setBackground(Color.gray);
		getContentPane().add(btnViewRules);
		btnViewRules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					displayRules();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnViewRules.setEnabled(false);

		
		btnViewTree.setBounds(20, 400,230, 25);
		btnViewTree.setBackground(Color.gray);
		getContentPane().add(btnViewTree);
		btnViewTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					displayTree();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnViewTree.setEnabled(false);
		
		btnBestEval.setBounds(20, 430, 230, 25);
		btnBestEval.setBackground(Color.gray);
		getContentPane().add(btnBestEval);
		btnBestEval.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String testOption = "";

				if (rdbtnCrossValidation.isSelected())
					testOption = "cross";
				else if (rdbtnTrainingData.isSelected()&& !trainingField.equals(""))
					testOption = "train";
				else if (rdbtnTestingData.isSelected() && !trainingField.equals("") && !testingField.equals(""))
					testOption = "test";
				else if (rdbtnPercentage.isSelected() && !percentageField.equals("")){
					String p =percentageField.getText();
					try {
					    percentage = Integer.parseInt(p);
					}
					catch(NumberFormatException ex)
					{
					    System.out.println("Exception : "+ex);
					}
					testOption = "percentage";
				}
				try {
					Classifier classifier = new Classifier(trainingPath.toString(), outputTA);
					
					new Evaluator(classifier, testingPath, trainingPath,
							testOption, percentage, outputTA, true).evaluate();
					cls = classifier.getClassifier();
					btnViewRules.setEnabled(true);
					btnViewTree.setEnabled(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnStart.setEnabled(true);

		
		
	}
	
	public void displayTree() throws Exception{
		final javax.swing.JFrame jf = new javax.swing.JFrame("Weka J48 Tree Visualizer");
		jf.setSize(700,500);
		jf.getContentPane().setLayout(new BorderLayout());
		TreeVisualizer tv = new TreeVisualizer(null,cls.graph(),new PlaceNode2());
		jf.getContentPane().add(tv, BorderLayout.CENTER);
		jf.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				jf.dispose();
			}
		});

		jf.setVisible(true);
		tv.fitToScreen();
	}
	
	public void displayRules() throws Exception{
		outputTA.append(cls.toString());
	}
	
	
}
