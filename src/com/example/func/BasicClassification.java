package com.example.func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;



public class BasicClassification {
	public final String modelFile = "/Users/karl/Work/database/model/model_test";
	void libtraining(String trainFile, String mf) throws IOException {
		/**
		 * Need to be replaced
		 */
		String[] arg = { "-b", "1", "-t", "0", trainFile, // 存放SVM训练模型用的数据的路径
				mf }; // 存放SVM通过训练数据训/ //练出来的模型的路径

		// 创建一个训练对象
		svm_train t = new svm_train();
		t.main(arg); // 调用
	}
	public String[] libpredicting(String predictFile, String mf) throws IOException {
		/**
		 * Need to be replaced
		 */
		String[] parg = { "-b", "1", predictFile, // 这个是存放测试数据
				mf, // 调用的是训练以后的模型
				mf + "result" }; // 生成的结果的文件的路径
		// 创建一个预测或者分类的对象
		svm_predict p = new svm_predict();
		p.main(parg);
		File file = new File(mf + "result");
		if(!file.exists())
			file.createNewFile();
		BufferedReader reader = new BufferedReader(
				new FileReader(mf + "result"));
		//return Double.parseDouble(reader.readLine());
		// 创建一个预测或者分类的对象
		reader.readLine();
		String[] result =  (reader.readLine()).split(" ");
		return result;
	}

}
