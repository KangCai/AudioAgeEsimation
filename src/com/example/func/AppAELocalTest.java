package com.example.func;

import java.io.File;

public class AppAELocalTest {
	private static final String trainFile = "/Users/karl/Work/database/forsimpletest/trainFeatures1";
	private static final String testFile = "/Users/karl/Work/database/forsimpletest/testFeatures1";
	private static final String testOneFile = "/Users/karl/Work/database/forsimpletest/onetestFeature";
	private static final String wavFile = "/Users/karl/Work/database/download/realdata/child/";
	private static final String modelFile = "/Users/karl/Work/database/model/model_test";
	public static void main(String[] arg) throws Exception {
		BasicFeatureExtractionAE aef = new BasicFeatureExtractionAE();
		//aef.generateFeature(aef.getTrainDir(), aef.getTrainNum(), trainFile);
		//aef.generateFeature(aef.getTestDir(), aef.getTestNum(), testFile);
		int right = 0, count = 0;
		File file = new File(wavFile);
		String[] names = file.list();
		for(String name : names) {
			if(name.substring(name.length() - 4).equals(".wav")) {
				aef.generateOneFeature(wavFile + name, testOneFile);
				BasicClassification bc = new BasicClassification();
				//bc.libtraining(trainFile, modelFile);
				if(bc.libpredicting(testOneFile, modelFile)[0] == "1.0")
					right++;
				count = 0;
			}
		}

		System.out.println(right + " " + count);
	}
}
