package HttpAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;



import AbClasses.*;

public class UploadPkg  extends PackageAnalysis {

	
	UploadPkg(){
		super();
	}

	protected void fileScan(File f) throws FileNotFoundException{
		//Do not consider the size of pkgs
		String key=null;
		boolean chunkEncoding=false;
		boolean emptyPkg=false;
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
				if(line.length>2 && line[2].contains("HTTP/1")){
//					System.out.println("Type: "+line[0]);
					item.typeUpdate(line[0]);
//					System.out.println("Type: "+item.returnType());
					key=line[1];
					updateDataTable(key,item);
//					System.out.println("URL: "+key);
				}
				
//				if(line[0].contains("Content-Length") && line.length>1){
//					//we record the length
////					System.out.println("lentgh: "+line[1]);
//					item.sizeUpdate(line[1]);
//				}else if(line[0].contains("Transfer-Encoding")){
//					//transfer-encoding used, we need to extract length from the message body
//					chunkEncoding=true;
//				}
//			}else{
////				System.out.println("Should be an empty line: "+l); //jump the blank line
//				/*Process the messagebody*/
//				StringBuilder s = new StringBuilder();
//				int sizeCount=0;
//				if(chunkEncoding==true){
////					System.out.println("chunkencoding!");
////					System.out.println("The size is: "+String.valueOf(Integer.parseInt(in.nextLine().trim(), 16)));
//					item.sizeUpdate(String.valueOf(Integer.parseInt(in.nextLine().trim(), 16)));//file length
//						while(in.hasNextLine() && !in.nextLine().trim().equals("0")){
//							String thisLine=in.nextLine();
//							s.append(thisLine.trim());
//							sizeCount+=thisLine.length();
//						}
//						item=updateDataTable(emptyPkg, key,s,item);
//						break;
//				}else{
////					System.out.println("No chunkencoding!");
//					String thisLine=null;
//					while(in.hasNextLine()){
//						thisLine=in.nextLine();
////						System.out.println("1st line of content is: "+thisLine);
//						if(!thisLine.isEmpty() && sizeCount+thisLine.length()>=item.returnSize()){
////							System.out.println("Last line length: "+thisLine.length());
//							int i=0;
//							for(;i<(item.returnSize()-sizeCount);i++){
//								s.append(thisLine.charAt(i));
//							}
//							sizeCount+=i;
//							
////							httpPackageDetect(fin,skipByte);
//							item=updateDataTable(emptyPkg, key,s,item);
//							break;
//						}else{
//						
//							s.append(thisLine.trim());
////							System.out.println("CUrrent line length: "+thisLine.length());
//							sizeCount+=thisLine.length();
//						}
//					}
//					item=updateDataTable(emptyPkg, key,s,item);
//				}
//
			}
		}
	}
	
		private InfoItemSlot updateDataTable(String key,InfoItemSlot item){
//			System.out.println("Processing item: "+item.toString());
			
			/*update the hash map*/
			if(dataTable.containsKey(key)){
				dataTable.get(key).countPlus();
			}else{
				dataTable.put(key, item);
			}
//			System.out.println("Put item of type: "+item.returnType());
			/*Another http package followed*/
//			httpPackageDetect(fin,skipByte+2);
			return new InfoItemSlot(null,0,1);
		}
		

	
	
	
	
	
	
	
	protected  void typeTableGen(){
		super.typeTableGen();
	}
	
	protected void	 resultOutput(PrintWriter outputFile, String pkgType,Map<String,InfoItemSlot> table){
//		System.out.println("Download Table size: "+table.size());
		super.resultOutput(outputFile, pkgType,table);
	}
	
	
	protected void tablePrint(Map<String,InfoItemSlot> table){
		super.tablePrint(table);
	}
	protected Map<String,InfoItemSlot> returnDataTable(){
		return super.returnDataTable();
	}
	
	protected Map<String,InfoItemSlot> returnTypeTable(){
		return super.returnTypeTable();
	}
	
	protected Map<String,InfoItemSlot> returnDupTable(){
		return super.returnDupTable();
	}
}
