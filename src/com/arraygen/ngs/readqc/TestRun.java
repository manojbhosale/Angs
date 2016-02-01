package com.arraygen.ngs.readqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import com.arraygen.ngs.readqc.*;

public class TestRun {

	// Input File paths
	public static String FastqPath = "C:\\Manoj\\ArrayGen\\NGS_Data\\test1gb.fq";
	// Output file path
	private static String ResultPath = "C:\\Manoj\\ArrayGen\\NGS_Data\\ngsQCresult.xls";

	// Limit of the read length handled in code
	int ExpectedreadLength = 50;

	// Per read position nucleotide composition
	NucleotideBase readPos[] = new NucleotideBase[ExpectedreadLength];

	// Per read, read properties arraylist 
	static ArrayList<ReadProperties> readProps = new ArrayList<ReadProperties>();
	static HashMap<String, Long> readSeqHmap = new HashMap<String, Long>();


	// Declare and initialize read count.Code might break at large number of reads
	static long readCount = 0;

	//Initialize readPositions with NucleotideBase objects
	public void initialiseReadPos(){
		for(int i = 0 ; i < readPos.length ; i++){
			readPos[i] = new NucleotideBase();
		}
	}




	// Read file ad return buffered reader object
	public BufferedReader readFile() throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(FastqPath));

		return br;
	}

	public static long getReadCount(BufferedReader br) throws IOException{
		
		long i = 0;
		
		for(String line; (line = br.readLine())!=null; i++){

			br.readLine();
			br.readLine();
			br.readLine();
			
			
		}
		
		return i;
	}
	
	//read file and get sequences line by line. Provide these sequences to get ATGC details per read and total ATGC details in NucleotideBase object.
	public ArrayList<String> getSequences(BufferedReader br) throws IOException{

		String record ="";
		long zero = 0;
		ArrayList<String> seqList = new ArrayList<String>();
		FileOutputStream fos = new FileOutputStream("C:\\Manoj\\ArrayGen\\NGS_Data\\test.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		long target = 100000;
		long current = 0;

		while((record=br.readLine()) != null){
			//record.trim();
			if(record.startsWith("@")){
				record=br.readLine();
				//record.trim();
				countBaseAtPos(record,oos);

				//				if(readSeqHmap.containsKey(record)){
				//					long tempCount = readSeqHmap.get(record);
				//					tempCount += 1;
				//					readSeqHmap.put(record, tempCount);
				//				}
				//				else{
				//					readSeqHmap.put(record, zero);
				//				}
				current++;
				if(current == target){
					System.gc();
					oos.flush();
					target += 1000000;
					System.out.println("Flushed"+current);
				}

			}


		}
		System.out.println("Total reads: "+current);
		oos.close();
		br.close();
		return seqList;
	}

	// Print any list 
	public void printList(Collection<String> clns){

		Iterator<String> lIter = clns.iterator();
		while(lIter.hasNext()){
			Object element = lIter.next();
			System.out.println(lIter.next());
		}
	}

	// Count bases at at position and per read 
	public void countBaseAtPos (String record, ObjectOutputStream oos)throws IOException{
		String[] baseArr = record.split("",-1);

		int aCount = 0;
		int tCount = 0;
		int gCount = 0;
		int cCount = 0;
		int nCount = 0;

		for(int i = 0 ; i < baseArr.length ; i++){

			if(baseArr[i].equals("A")){
				readPos[i].incrementAcount();
			}else if(baseArr[i].equals("T")){
				readPos[i].incrementTcount();
			}else if(baseArr[i].equals("G")){		
				readPos[i].incrementGcount();
			}else if(baseArr[i].equals("C")){
				readPos[i].incrementCcount();
			}else if(baseArr[i].equals("N")){
				readPos[i].incrementNcount();
			}else{


			}

		}

		for(int i = 0 ; i < baseArr.length ; i++){
			if(baseArr[i].equals("A")){
				aCount++;
			}else if(baseArr[i].equals("T")){
				tCount++;
			}else if(baseArr[i].equals("G")){		
				gCount++;
			}else if(baseArr[i].equals("C")){
				cCount++;
			}else if(baseArr[i].equals("N")){
				nCount++;
			}else{


			}
		}

				if((gCount+cCount+aCount+tCount+nCount) != 0){
					Double readGCpercent = (double) (gCount+cCount)/(gCount+cCount+aCount+tCount+nCount)*100;
					int readLength = record.length();
					ReadProperties tempReadProp = new ReadProperties();
					tempReadProp.setGcContent(readGCpercent);
					tempReadProp.setReadLength(readLength);
					//readProps.add(readCount, tempReadProp);
					//readCount++;
					oos.writeObject(tempReadProp);
					//oos.flush();
				}
	}


	public PercentNucleotideBase[]  getATGCpercentarray(){

		PercentNucleotideBase perNTbase[] = new PercentNucleotideBase[ExpectedreadLength];
		double Apercent = 0.0;
		double Tpercent = 0.0;
		double Gpercent = 0.0;
		double Cpercent = 0.0;
		double Npercent = 0.0;
		for(int i = 0 ; i < perNTbase.length ; i++){
			perNTbase[i] = new PercentNucleotideBase();
		}

		for(int i = 0; i < readPos.length ; i++){
			long cCount = readPos[i].getCcount();
			long gCount = readPos[i].getGcount();
			long tCount = readPos[i].getTcount();
			long aCount = readPos[i].getAcount();
			long nCount = readPos[i].getNcount();
			try{
				Apercent = (double) (aCount)/(tCount+gCount+cCount+nCount+aCount)*100;
			}catch(ArithmeticException e){
				Apercent = 0.0;
				e.printStackTrace();
			}
			try{
				Tpercent = (double) (tCount)/(aCount+tCount+cCount+nCount+gCount)*100;
			}catch(ArithmeticException e){
				Tpercent = 0.0;
				e.printStackTrace();
			}
			try{
				Gpercent = (double) (gCount)/(aCount+tCount+cCount+nCount+gCount)*100;
			}catch(ArithmeticException e){
				Gpercent = 0.0;
				e.printStackTrace();
			}
			try{
				Cpercent = (double)(cCount)/(aCount+tCount+gCount+nCount+cCount)*100;
			}catch(ArithmeticException e){
				Cpercent = 0.0;
				e.printStackTrace();
			}
			try{
				Npercent = (double) (nCount)/(tCount+gCount+cCount+aCount+nCount)*100;
			}catch(ArithmeticException e){
				Cpercent = 0.0;
				e.printStackTrace();
			}
			perNTbase[i].setApercent(Apercent);
			perNTbase[i].setTpercent(Tpercent);
			perNTbase[i].setGpercent(Gpercent);
			perNTbase[i].setCpercent(Cpercent);
			perNTbase[i].setNpercent(Npercent);
		}
		return perNTbase;
	}

	//get per base gc content from each read position
	public Double[] perBaseAvgGC(){

		Double[] GCperArr = new Double[readPos.length];
		Double GCratio = 0.0;
		Double minGCratioPerRead;
		Double maxGCratioPerRead;
		for(int i = 0; i < readPos.length ; i++){
			long cCount = readPos[i].getCcount();
			long gCount = readPos[i].getGcount();
			long tCount = readPos[i].getTcount();
			long aCount = readPos[i].getAcount();
			long nCount = readPos[i].getNcount();

			if((gCount+cCount+aCount+tCount+nCount) != 0){

				GCratio = (double)(gCount+cCount)/(double)(gCount+cCount+aCount+tCount+nCount)*100;
				GCperArr[i] = GCratio;
			}

		}
		return GCperArr;
	}

	//	print  any map
	public static void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	// get average of any double array
	public Double getAverage(Double[] inputArr){
		Double resAverage = 0.0;
		Double resSum = getSum(inputArr);

		return resAverage = resSum/inputArr.length;
	}

	// get sum for any double array.
	public Double getSum(Double[] inputArr){
		Double resSum = 0.0;
		for(int i=0 ; i < inputArr.length ; i++){
			resSum += inputArr[i];
		}

		return resSum;
	} 

	//execute some of methods above step by step
	public static void main(String args[]) throws IOException{

		long StartTime = System.currentTimeMillis();


		BufferedReader br=null;
		File fq = new File(FastqPath);
		
		TestRun tr = new TestRun();
		tr.initialiseReadPos();
		//tr.initialiseReadProps();
		ArrayList<String> seqs = new ArrayList<String>();
		try {
			br =  tr.readFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			seqs =	tr.getSequences(br);
			System.out.println("Read Count : "+getReadCount(br));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Double[] testPerGC = tr.perBaseAvgGC();

		//printMap(readSeqHmap);

		PrintWriter bw = null;
		try {
			bw = new PrintWriter(new FileWriter(ResultPath));
		} catch (IOException e) {
			e.printStackTrace();
		}


		bw.append("Per Base GC content\t");

		for(int i = 0; i < testPerGC.length ;i++){
			bw.append(testPerGC[i]+"\t");
		}

		PercentNucleotideBase[] perNTbase = tr.getATGCpercentarray();

		bw.append("\nPer Base A content\t");

		for(int i = 0; i < perNTbase.length ;i++){
			bw.append(perNTbase[i].getApercent()+"\t");
		}

		bw.append("\nPer Base T content\t");

		for(int i = 0; i < perNTbase.length ;i++){
			bw.append(perNTbase[i].getTpercent()+"\t");
		}

		bw.append("\nPer Base G content\t");

		for(int i = 0; i < perNTbase.length ;i++){
			bw.append(perNTbase[i].getGpercent()+"\t");
		}

		bw.append("\nPer Base C content\t");

		for(int i = 0; i < perNTbase.length ;i++){
			bw.append(perNTbase[i].getCpercent()+"\t");
		}

		bw.append("\nPer Base N content\t");

		for(int i = 0; i < perNTbase.length ;i++){
			bw.append(perNTbase[i].getNpercent()+"\t");
		}


		long avgReadLength = 0;
		long totalReadLength = 0;
		double totalGCcontent = 0.0;
		double avgGCcontent = 0.0;

		FileInputStream fis = new FileInputStream("C:\\Manoj\\ArrayGen\\NGS_Data\\test.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);

		ReadProperties rp;
		try {
			while((rp = (ReadProperties) ois.readObject())!= null){
				totalReadLength += rp.getReadLength();
				totalGCcontent += rp.getGcContent();
				readCount++;
			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (EOFException ex){
			System.out.println("EOF reached !! continuing...");
		}
		ois.close();
		//		for(int i = 1 ; i < readProps.size();i++){
		//			totalReadLength += readProps.get(i).getReadLength();
		//			totalGCcontent += readProps.get(i).getGcContent();
		//		}
		//
		//		long avgReadLength = 0;
		//		long totalReadLength = 0;
		//		double totalGCcontent = 0.0;
		//		double avgGCcontent = 0.0;
		//
		//
		//		for(int i = 1 ; i < readProps.size();i++){
		//			totalReadLength += readProps.get(i).getReadLength();
		//			totalGCcontent += readProps.get(i).getGcContent();
		//		}
		//
						bw.append("\nAverage Read Length\t");
				
						avgReadLength = totalReadLength/readCount;
						bw.append((int)avgReadLength+"");
				
						bw.append("\nAverage GC content per read\t");
				
						avgGCcontent = totalGCcontent/readCount;
						bw.append((int)avgGCcontent+"");
						bw.append("\nNumber of reads\t"+readCount+"\n");
				bw.close();
		long EndTime = System.currentTimeMillis();
		System.out.println("It Took "+((EndTime -StartTime)/1000)/60+" Seconds to complete");
	}





}
