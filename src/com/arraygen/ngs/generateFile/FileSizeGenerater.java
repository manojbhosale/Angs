package com.arraygen.ngs.generateFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.omg.CORBA.PRIVATE_MEMBER;

public class FileSizeGenerater {
	
	private static String FastqPath = "C:\\Manoj\\ArrayGen\\NGS_Data\\SRR850208_sample.fastq";
	private static String oneGbfile = "C:\\Manoj\\ArrayGen\\NGS_Data\\test1gb.fq";
	
	public static void main(String args[])throws IOException{
		
		File fqFile = new File(FastqPath);
		
		PrintWriter pw = new PrintWriter(new File(oneGbfile));
		
		while(((oneGbfile.length())/(1024*1024*1024))<1){
			
			BufferedReader br = new BufferedReader(new FileReader(fqFile));
			String record = "";
				
			while((record = br.readLine()) != null){
				
				pw.append(record+"\n");
				
			}
			br.close();
			
		}
		pw.close();
	}

}
