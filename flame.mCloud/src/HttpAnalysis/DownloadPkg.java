package HttpAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;


import AbClasses.*;


public class DownloadPkg extends PackageAnalysis {
//	Map<String,InfoItemSlot> dataTable = new HashMap<String,InfoItemSlot>();
	
	DownloadPkg(){
		super();
	}

	protected void fileScan(File f) throws FileNotFoundException{

//		System.out.println("File name: "+f.getName());
		
		FileInputStream fin = new FileInputStream(f);
		Scanner in = new Scanner(fin);
		httpPackageDetect(in);
	}
	
	
	private void httpPackageDetect(Scanner in){
		boolean chunkEncoding=false;
		boolean emptyPkg=false;
		boolean typeDefined=false;
		String key=null;
		InfoItemSlot item = new InfoItemSlot(null,0,1);
		while(in.hasNextLine()){
			/*Process the header*/
//			System.out.println("This line has : "+line.length+" parts");
			String[] line;
			String l=null;

			if(!(l=in.nextLine().trim()).isEmpty()){
				
				if(l.contains(String.valueOf(Character.toString((char) 152)))) 
					System.out.println("Multiple http pkgs!");
				
				line=l.split(": ");
				if(line[0].contains("Content-Length") && line.length>1){
					//we record the length
//					System.out.println("This line has : "+line.length+" parts");
//					System.out.println("SIze is: "+line[1]);
					item.sizeUpdate(line[1]);
					if(line[1].equals("0")){
						if(typeDefined==false){
						item.typeUpdate("Empty");
						System.out.println("An emplty package! ");
						key="emptyDownloadPkg";
						}else{
							key="emptyDownloadPkg"+item.returnType();
						}
						emptyPkg=true;
					}

				}else if(line[0].contains("Content-Type") && line.length>1){
					typeDefined=true; //it has a type
					String[] contentType = line[1].toString().split(";");
					item.typeUpdate(contentType[0]);
//					System.out.println("Type is: "+contentType[0]);
//					if(emptyPkg==true) break;
				
					
				}else if(line[0].contains("Transfer-Encoding")){
					//transfer-encoding used, we need to extract length from the message body
					chunkEncoding=true;
//					System.out.println("This package has used chunkencoding");
				}
			}else{
//				System.out.println("Should be an empty line: "+in.nextLine()); //jump the blank line
				/*Process the messagebody*/
				StringBuilder s = new StringBuilder();
				int sizeCount=0;
				if(chunkEncoding==true){
					item.sizeUpdate(String.valueOf(Integer.parseInt(in.nextLine().trim(), 16)));//file length
						while(in.hasNextLine() && !in.nextLine().trim().equals("0")){
							s.append(in.nextLine().trim());
						}
				}else{
					while(in.hasNextLine()){
						s.append(in.nextLine().trim());
					}
				}
				if(emptyPkg==false)
				key=DigestUtils.shaHex(s.toString());
				System.out.println("The size of the message is: "+s.toString().length());
//				System.out.println("THe hash value is: "+key);
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
		System.out.println("Download Table size: "+dataTable.size());
		super.resultOutput(outputFile, pkgType,dataTable);
	}
	
	protected void	 typeResultOutput(PrintWriter outputFile, String pkgType){
		System.out.println("Download Type Table size: "+typeTable.size());
		super.resultOutput(outputFile, pkgType,typeTable);
	}
	
	protected void tablePrint(){
		super.tablePrint();
	}
}
