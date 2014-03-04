package HttpAnalysis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;



public class Test {

	public static void main(String args[]) throws IOException{
		
		File[] upFolder = new File("uploaddata").listFiles();
		PrintWriter upOutputFile = new PrintWriter(new File("record/Overall_Upload.txt"));
		PrintWriter upOutputFile2 = new PrintWriter(new File("record/Type_Upload.txt"));
//		
		File[] downFolder = new File("downloaddata").listFiles();
		PrintWriter downOutputFile = new PrintWriter(new File("record/Overall_Download.txt"));
		PrintWriter downOutputFile2 = new PrintWriter(new File("record/Type_Download.txt"));
		
		DownloadPkg D = new DownloadPkg();
		System.out.println("Download Package #: "+downFolder.length);
		for(File f:downFolder){
			D.fileScan(f);		
		}
		D.typeTableGen();
		D.tablePrint(D.returnTypeTable());
		System.out.println("*********Dup table:*********\n");
		D.tablePrint(D.dupTable());
		System.out.println("******************\n");
		D.returnStatusCodeRecord();
		D.overallResultOutput(downOutputFile,"download");
		D.typeResultOutput(downOutputFile2,"downloadType");
		
		
		
		UploadPkg U = new UploadPkg();
		System.out.println("Upload Package #: "+upFolder.length);
		for(File f:upFolder){
			U.fileScan(f);		
		}
//		U.tablePrint();
		U.typeTableGen();
		U.tablePrint(U.returnTypeTable());
		System.out.println("*********Dup table:*********\n");
		U.tablePrint(U.dupTable());
		System.out.println("******************\n");
		U.overallResultOutput(upOutputFile,"upload");
		U.typeResultOutput(upOutputFile2,"uploadType");
		
	}
	
	
}
