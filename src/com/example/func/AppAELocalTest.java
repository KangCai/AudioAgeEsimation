package com.example.func;


public class AppAELocalTest {
	private static final String trainFile = "/Users/karl/Work/database/forsimpletest/trainFeatures1";
	private static final String testFile = "/Users/karl/Work/database/forsimpletest/testFeatures1";
	private static final String testOneFile = "/Users/karl/Work/database/forsimpletest/onetestFeature";
	private static final String wavFile = "/Users/karl/Work/database/speaker/certainSpeaker/";
	private static final String modelFile = "/Users/karl/Work/database/model/model_test";
	public static void main(String[] arg) throws Exception {
		BasicFeatureExtractionAE aef = new BasicFeatureExtractionAE();
		//aef.generateFeature(aef.getTrainDir(), aef.getTrainNum(), trainFile);
		//aef.generateFeature(aef.getTestDir(), aef.getTestNum(), testFile);
		int right = 0;
		for(int i = 0; i < 100; i++) {
		aef.generateOneFeature(wavFile + i + ".wav", testOneFile);
		
		BasicClassification bc = new BasicClassification();
		//bc.libtraining(trainFile, modelFile);
		if(bc.libpredicting(testOneFile, modelFile)[0] == "1.0")
			right++;
		}
		System.out.println(right * 1.0/100);
	}
}
