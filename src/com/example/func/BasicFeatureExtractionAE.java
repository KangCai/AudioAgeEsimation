package com.example.func;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.example.utils.BasicFeatureExtraction;


public class BasicFeatureExtractionAE {
	//database
	private final String trainMaleDir = "/Users/karl/Work/database/age/traindata/male/";
	private final String trainFemaleDir = "/Users/karl/Work/database/age/traindata/female/";
	private final String trainChildDir = "/Users/karl/Work/database/age/traindata/children/";
	private final String trainChildNoiseDir = "/Users/karl/Work/database/age/traindata/childrennoise/";
	private final String testMaleDir = "/Users/karl/Work/database/age/testdata/male/";
	private final String testFemaleDir = "/Users/karl/Work/database/age/testdata/female/";
	private final String testChildDir = "/Users/karl/Work/database/age/testdata/children/";
	private final String testChildNoiseDir = "/Users/karl/Work/database/age/testdata/childrennoise/";
	private final String[] trainDir = {trainMaleDir, trainFemaleDir, trainChildDir, trainChildNoiseDir};
	private final String[] testDir = {testMaleDir, testFemaleDir, testChildDir, testChildNoiseDir};
	private final int trainNum = 300;
	private final int testNum = 100;
	private final int featureDim = 20;

	//Generate features, save them to ARFF format
	public void generateFeature(String[] dir, int numsPerDir, String saveFileName) throws IOException, UnsupportedAudioFileException {
		String[] trainFiles = genrateFiles(dir, numsPerDir);
		double[][] featureMatrix = new double[trainFiles.length][];
		for(int i = 0; i < trainFiles.length; i++) {
			featureMatrix[i] = new BasicFeatureExtraction().extractFeatures(new File(trainFiles[i]));
		}
		//String arffStr = featureMatrixToArff(featureMatrix);
		String arffStr = featureMatrixToLibsvm(featureMatrix);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(arffStr);
		fw.close();
	}
	
	public void generateOneFeature(String targetFile, String saveFileName) throws IOException {
		double[][] featureMatrix = new double[1][];
		featureMatrix[0] = new BasicFeatureExtraction().extractFeatures(new File(targetFile));
		//String arffStr = featureMatrixToArff(featureMatrix);
		String libsvmStr = featureMatrixToLibsvm(featureMatrix);
		FileWriter fw = new FileWriter(new File(saveFileName));
		fw.write(libsvmStr);
		fw.close();
	}

	//For test
	public double[][] fortest(String[] dir, int numsPerDir, String saveFileName) throws IOException, UnsupportedAudioFileException {
		String[] trainFiles = genrateFiles(dir, numsPerDir);
		double[][] featureMatrix = new double[trainFiles.length][];
		for(int i = 0; i < trainFiles.length; i++) {
			featureMatrix[i] = new BasicFeatureExtraction().extractFeatures(new File(trainFiles[i]));
		}
		return featureMatrix;
	}

	public String[] getTrainDir() {
		return trainDir;
	}
	public String[] getTestDir() {
		return testDir;
	}
	public int getTrainNum() {
		return trainNum;
	}
	public int getTestNum() {
		return testNum;
	}

	//Generate files
	private String[] genrateFiles(String[] dir, int num) {
		String[] files = new String[dir.length * num];
		for(int i = 0; i < dir.length; i++) {
			for(int j = 0; j < num; j++)
				files[i * num + j] = dir[i] + j + ".wav";
		}
		return files;
	}

	//Convert feature matrix into Libsvm format, remember to change public to private!!!
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
