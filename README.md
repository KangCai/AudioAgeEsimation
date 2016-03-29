**AudioAgeEsimationDemo**
===============================

##Note

1. About the purpose. In fact, this project aims at judging whether a speaker is an adult or a child, but I don't know how to call this function. So I named it "age esimation" demo. :)
2. About the algorithms. Features: LPC + MFCC + CHROMA(Forget this feature for its bad performance); Model: SVM with linear kernal function.
3. About dataset. You should have your own dataset for training, One for adult and one for child. Adult training dataset would better consist of both male and female voice with nearly equal numbers. The total number of adult training dataset should be nearly equal to that of child training dataset. The whole dataset would better have a uniform distribution.
4. About the perfromance. With my own dataset and applications, the perfromance is about 80%.

##For developers

  1. MainActivity.java is at AudioAgeEsimationDemo/src/com/example/audiorecordtest/MainActivity.java
  2. You can also get to start by reference to AudioAgeEsimationDemo/src/com/example/func/AppAELocalTest.java
