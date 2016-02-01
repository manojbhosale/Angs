package com.arraygen.ngs.readqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestDriver {

	public static void main(String args[])throws IOException{
		long StartTime = System.currentTimeMillis();
		File fq = new File(TestRun.FastqPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fq)));
		System.out.println("REad Count :"+TestRun.getReadCount(br));
		long EndTime = System.currentTimeMillis();
		System.out.println("It Took "+((EndTime -StartTime)/1000)/60+" Minutes to complete");
	}
	
}
