package HttpAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {

	public static void main(String args[]) throws FileNotFoundException{
		
		File[] upFolder = new File("uploaddata").listFiles();
		File[] downFolder = new File("downloaddata").listFiles();
		PrintWriter upOutputFile = new PrintWriter(new File("record/HashmapRecordUpload.txt"));
		PrintWriter downOutputFile = new PrintWriter(new File("record/HashmapRecordDownload.txt"));
		
		DownloadPkg D = new DownloadPkg();
		System.out.println("Download Package #: "+downFolder.length);
		for(File f:downFolder){
			D.fileScan(f);
			
		}
//		D.tablePrint();
		D.resultOutput(downOutputFile,"download");
		
		
		
		UploadPkg U = new UploadPkg();
		System.out.println("Upload Package #: "+upFolder.length);
		for(File f:upFolder){
			U.fileScan(f);		
		}
//		U.tablePrint();
		U.resultOutput(upOutputFile,"upload");
		
	}
	
	
}
