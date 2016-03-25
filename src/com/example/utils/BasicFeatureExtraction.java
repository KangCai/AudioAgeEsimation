package com.example.utils;

import java.io.File;
import java.io.IOException;


public class BasicFeatureExtraction {
	private final float sampleRate = 16000f;
	/**
	 * Lpc feature feature extraction
	 */
	public double[] extractLpc(double[] voiceSample) {
		// Preprocess
        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        voiceSample = voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        // lpc
        double[] lpcFeatures = new LpcFeaturesExtractor(sampleRate, 20).extractFeatures(voiceSample);
        return lpcFeatures;
    }	
	public double[] extractLpc(File voiceSampleFile) 
		throws IOException {        
	        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);
	        return extractLpc(audioSample);
	}
	/**
	 * MFCC feature feature extraction
	 */
	public double[] extractMfcc(double[] voiceSample) {
		// Preprocess
		float[] floatArray = new float[voiceSample.length];
		for (int i = 0 ; i < voiceSample.length; i++) {
		    floatArray[i] = (float) voiceSample[i];
		}
		PreProcess prep = new PreProcess(floatArray, 512, (int)sampleRate);
		FeatureExtract fe = new FeatureExtract(prep.getFramedSignal(), (int)sampleRate, 512);
		//System.out.println(prep.getFramedSignal().length);
		fe.makeMfccFeatureVector();
		double[] mfccFeatures = fe.getFeatureVector().getRowFeature();
        return mfccFeatures;
    }	
	public double[] extractMfcc(File voiceSampleFile) {        
	        double[] audioSample;
			try {
				audioSample = convertFileToDoubleArray(voiceSampleFile);
				return extractMfcc(audioSample);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	}
	
	private double[] convertFileToDoubleArray(File voiceSampleFile) 
            throws IOException {
    	WavFile wavFile;
		try {
			wavFile = WavFile.openWavFile(voiceSampleFile);
			int nframes = (int) wavFile.getNumFrames();
			double[] buffer = new double[nframes];
			wavFile.readFrames(buffer, nframes);
	    	return buffer;
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}
