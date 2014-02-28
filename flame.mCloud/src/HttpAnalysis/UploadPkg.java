package HttpAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


import AbClasses.*;

public class UploadPkg  extends PackageAnalysis {

	
	UploadPkg(){
		super();
	}

	protected void fileScan(File f) throws FileNotFoundException{

		String key=null;
		InfoItemSlot item = new InfoItemSlot(null,0,1);
//		System.out.println("File name: "+f.getName());
		
		FileInputStream fin = new FileInputStream(f);
		Scanner in = new Scanner(fin);
		while(in.hasNextLine()){
			/*Process the header*/
//			System.out.println("This line has : "+line.length+" parts");
			String[] line;
			String l=null;
			if(!(l=in.nextLine().trim()).isEmpty()){
				line=l.split(" ");
				if(line.length>2 && line[2].contains("HTTP/1.1")){
//					System.out.println("URL: "+line[1]);
					item.typeUpdate(line[0]);
					key=line[1];
				}
				if(line[0].contains("Content-Length") && line.length>1){
					//we record the length
//					System.out.println("lentgh: "+line[1]);
					item.sizeUpdate(line[1]);
				}
			}
		}
		/*update the hash map*/
		if(dataTable.containsKey(key)){
			dataTable.get(key).countPlus();
		}else{
			dataTable.put(key, item);
		}
	}
	
	protected  void typeTableGen(){
		super.typeTableGen();
	}
	
	protected void	 overallResultOutput(PrintWriter outputFile, String pkgType){
		System.out.println("Upload Table size: "+dataTable.size());
		super.resultOutput(outputFile, pkgType,dataTable);
	}
	
	protected void	 typeResultOutput(PrintWriter outputFile, String pkgType){
		System.out.println("Upload Type Table size: "+dataTable.size());
		super.resultOutput(outputFile, pkgType,typeTable);
	}
	
	protected void tablePrint(){
		super.tablePrint();
	}
}
