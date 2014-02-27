package HttpAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.codec.digest.DigestUtils;

public class Test {

	public static void main(String args[]) throws FileNotFoundException{
		
		File[] folder = new File("data").listFiles();
		PrintWriter outputFile = new PrintWriter(new File("record/HashmapRecord.txt"));
		
		
//		System.out.println(DigestUtils.shaHex("asdasd2e"));
		
		DownloadPkg D = new DownloadPkg();
		System.out.println("Package #: "+folder.length);
		for(File f:folder){
			D.fileScan(f);
			
		}
		D.tablePrint();
		D.resultOutput(outputFile,"download");
	}
	
	
}
