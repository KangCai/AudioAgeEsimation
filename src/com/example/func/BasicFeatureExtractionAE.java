package com.example.func;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.example.utils.BasicFeatureExtraction;



public class BasicFeatureExtractionAE {
	//database
	private final String mainDir = "/Users/karl/Work/database/download/realdata/";
	private final String trainMaleDir = mainDir + "male/";
	private final String trainFemaleDir = mainDir + "female/";
	private final String trainChildDir = mainDir + "child/";
	private final String testMaleDir = "/Users/karl/Work/database/age/testdata/male/";
	private final String testFemaleDir = "/Users/karl/Work/database/age/testdata/female/";
	private final String testChildDir = "/Users/karl/Work/database/age/testdata/children/";
	private final String testChildNoiseDir = "/Users/karl/Work/database/age/testdata/childrennoise/";
	private final String[] trainNegDir = {trainMaleDir, trainFemaleDir};
	private final String[] trainPosDir = {trainChildDir};
	private final String[] testDir = {testMaleDir, testFemaleDir, testChildDir, testChildNoiseDir};
	private final int trainNeg = 75;
	private final int trainPos = 61;
	private final int testNum = 100;
	private final int featureDim = 40;

	//Generate features, save them to ARFF format
	public void generateFeature(String[] dir1, int num1, String[] dir2, int num2, String saveFileName) throws IOException, UnsupportedAudioFileException {
		String[] trainNeg = genrateFiles(dir1, num1);
		String[] trainPos = genrateFiles(dir2, num2);
		double[][] featureMatrix = new double[num1 + num2][];
		for(int i = 0; i < num1; i++) {
			featureMatrix[i] = new BasicFeatureExtraction().extractLpc(new File(trainNeg[i]));
		}
		for(int i = 0; i < num2; i++) {
			featureMatrix[num1 + i] = new BasicFeatureExtraction().extractLpc(new File(trainPos[i]));
		}
		//String arffStr = featureMatrixToArff(featureMatrix);
		String arffStr = trainFeatureMatrixToLibsvm(featureMatrix, num1, num2);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(arffStr);
		fw.close();
	}
	
	public void generateLMFeature(String[] dir1, int num1, String[] dir2, int num2, String saveFileName) throws IOException, UnsupportedAudioFileException {
		String[] trainNeg = genrateFiles(dir1, num1);
		String[] trainPos = genrateFiles(dir2, num2);
		double[][] featureMatrix = new double[num1 + num2][];
		BasicFeatureExtraction bfe = new BasicFeatureExtraction();
		for(int i = 0; i < num1; i++) {
			System.out.println(trainNeg[i]);
			double[] lpc = bfe.extractLpc(new File(trainNeg[i]));
			double[] mfcc = bfe.extractMfcc(new File(trainNeg[i]));
			featureMatrix[i] = new double[40];
			System.arraycopy(lpc, 0, featureMatrix[i], 0, 20);
			System.arraycopy(mfcc, 0, featureMatrix[i], 20, 20);
		}
		for(int i = 0; i < num2; i++) {
			double[] lpc = bfe.extractLpc(new File(trainPos[i]));
			double[] mfcc = bfe.extractMfcc(new File(trainPos[i]));
			featureMatrix[i + num1] = new double[40];
			System.arraycopy(lpc, 0, featureMatrix[i + num1], 0, 20);
			System.arraycopy(mfcc, 0, featureMatrix[i + num1], 20, 20);
		}
		//String arffStr = featureMatrixToArff(featureMatrix);
		String arffStr = trainFeatureMatrixToLibsvm(featureMatrix, num1, num2);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(arffStr);
		fw.close();
	}
	
	public void generateOneFeature(String targetFile, String saveFileName) throws IOException, UnsupportedAudioFileException {
		double[][] featureMatrix = new double[1][];
		featureMatrix[0] = new BasicFeatureExtraction().extractLpc(new File(targetFile));
		//String arffStr = featureMatrixToArff(featureMatrix);
		String arffStr = featureMatrixToLibsvm(featureMatrix);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(arffStr);
		fw.close();
	}
	
	public void generateOneLMFeature(String targetFile, String saveFileName) throws IOException {
		BasicFeatureExtraction bfe = new BasicFeatureExtraction();
		double[][] featureMatrix = new double[1][];
		double[] lpc = bfe.extractLpc(new File(targetFile));
		double[] mfcc = bfe.extractMfcc(new File(targetFile));
		featureMatrix[0] = new double[40];
		System.arraycopy(lpc, 0, featureMatrix[0], 0, 20);
		System.arraycopy(mfcc, 0, featureMatrix[0], 20, 20);
		//String arffStr = featureMatrixToArff(featureMatrix);
		String arffStr = featureMatrixToLibsvm(featureMatrix);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(arffStr);
		fw.close();
	}


	public String[] getNegTrainDir() {
		return trainNegDir;
	}
	public String[] getPosTrainDir() {
		return trainPosDir;
	}
	public String[] getTestDir() {
		return testDir;
	}
	public int getNegTrainNum() {
		return trainNeg;
	}
	public int getPosTrainNum() {
		return trainPos;
	}
	public int getTestNum() {
		return testNum;
	}

	//Generate files
	private String[] genrateFiles(String[] dir, int num) {
		String[] filenames = new String[num];
		int count = 0;
		for(String d : dir) {
			File file = new File(d);
			String[] names = file.list();
			for(String name : names) {
				if(name.substring(name.length() - 4).equals(".wav")) {
					filenames[count] = d + name;
					count += 1;
				}
			}
		}
		return filenames;
	}
	

	//Convert feature matrix into Libsvm format, remember to change public to private!!!
	public String trainFeatureMatrixToLibsvm(double[][] featureMatrix, int num1, int num2) {
		String str = "";		
		//numeric  
		for(int i = 0; i < num1; i++) {
			str += "-1";
			for(int j = 0; j < featureDim; j++) 
				str += " " + (j + 1) + ":" + featureMatrix[i][j];
			str += '\n';
		}
		for(int i = num1; i < num1 + num2; i++) {
			str += "1";
			for(int j = 0; j < featureDim; j++) 
				str += " " + (j + 1) + ":" + featureMatrix[i][j];
			str += '\n';
		}
		return str;
	}
	public String featureMatrixToLibsvm(double[][] featureMatrix) {
		String str = "";		
		//numeric  
		for(int i = 0; i < featureMatrix.length / 2; i++) {
			str += "-1";
			for(int j = 0; j < featureDim; j++) 
				str += " " + (j + 1) + ":" + featureMatrix[i][j];
			str += '\n';
		}
		for(int i = featureMatrix.length / 2; i < featureMatrix.length; i++) {
			str += "1";
			for(int j = 0; j < featureDim; j++) 
				str += " " + (j + 1) + ":" + featureMatrix[i][j];
			str += '\n';
		}
		return str;
	}

}
