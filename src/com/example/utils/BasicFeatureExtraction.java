package com.example.utils;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;



public class BasicFeatureExtraction {
	private final float sampleRate = 16000f;
	/**
	 * feature feature extraction
	 */
	public double[] extractFeatures(double[] voiceSample) {
		// Preprocess
        AutocorrellatedVoiceActivityDetector voiceDetector = new AutocorrellatedVoiceActivityDetector();
        Normalizer normalizer = new Normalizer();
        voiceDetector.removeSilence(voiceSample, sampleRate);
        normalizer.normalize(voiceSample, sampleRate);
        
        double[] lpcFeatures = new LpcFeaturesExtractor(sampleRate, 20).extractFeatures(voiceSample);

        return lpcFeatures;
    }
	
	public double[] extractFeatures(File voiceSampleFile) 
		throws IOException {
	        
	        double[] audioSample = convertFileToDoubleArray(voiceSampleFile);

	        return extractFeatures(audioSample);
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
