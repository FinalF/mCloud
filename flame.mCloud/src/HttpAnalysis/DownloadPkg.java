package HttpAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	protected void fileScan(File f) throws IOException{

//		System.out.println("File name: "+f.getName());
//		System.out.println("The file size is: "+fileSize);
		FileInputStream fin = new FileInputStream(f);

		httpPackageDetect(fin,0);
	}
	
	
	private void httpPackageDetect(FileInputStream fin,long skipByte) throws IOException{
		fin.skip(skipByte);
		Scanner in = new Scanner(fin);
		boolean chunkEncoding=false;
		boolean emptyPkg=false;
		boolean typeDefined=false;
		String key=null;
//		if(!in.hasNextLine()){
//			endOfPkg=true;
//		}

		InfoItemSlot item = new InfoItemSlot(null,0,1);
		while(in.hasNextLine()){
			/*Process the header*/
//			System.out.println("This line has : "+line.length+" parts");
			String[] line;
			String l=null;

			if(!(l=in.nextLine().trim()).isEmpty()){
//				if(l.contains(String.valueOf(Character.toString((char) 152)))) 
//					System.out.println("Multiple http pkgs!");
				skipByte+=l.length()+1;
				line=l.split(": ");
				if(line[0].contains("Content-Length") && line.length>1){
					//we record the length
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
//				System.out.println(l);
			}else{
				skipByte+=1;//The empty line
//				System.out.println("Should be an empty line: "+l); //jump the blank line
				/*Process the messagebody*/
//				System.out.println("The header size of the http pkg: "+skipByte);
				skipByte+=item.returnSize();
//				System.out.println("The total size of the http pkg: "+skipByte);
				StringBuilder s = new StringBuilder();
				int sizeCount=0;
				if(chunkEncoding==true){
//					System.out.println("chunkencoding!");
//					System.out.println("The size is: "+String.valueOf(Integer.parseInt(in.nextLine().trim(), 16)));
					item.sizeUpdate(String.valueOf(Integer.parseInt(in.nextLine().trim(), 16)));//file length
						while(in.hasNextLine() && !in.nextLine().trim().equals("0")){
							String thisLine=in.nextLine();
							s.append(thisLine.trim());
							sizeCount+=thisLine.length();
						}
						item=updateDataTable(emptyPkg, key,s,item);
						break;
				}else{
//					System.out.println("No chunkencoding!");
					String thisLine=null;
					while(in.hasNextLine()){
						thisLine=in.nextLine();
						if(sizeCount+thisLine.length()>=item.returnSize()){
//							System.out.println("Last line length: "+thisLine.length());
							int i=0;
							for(;i<(item.returnSize()-sizeCount);i++){
								s.append(thisLine.charAt(i));
							}
							sizeCount+=i;
							
								
							skipByte=skipByte-item.returnSize()+thisLine.length()+2;
//							httpPackageDetect(fin,skipByte);
							item=updateDataTable(emptyPkg, key,s,item);
							break;
						}else{
						
							s.append(thisLine.trim());
//							System.out.println("CUrrent line length: "+thisLine.length());
							sizeCount+=thisLine.length();
						}
					}
					item=updateDataTable(emptyPkg, key,s,item);
				}

			}
		}

	}
	
	
	
	
	private InfoItemSlot updateDataTable(boolean emptyPkg, String key,StringBuilder s,InfoItemSlot item){
//		System.out.println("Processing item: "+item.toString());
		if(emptyPkg==false)
			key=DigestUtils.shaHex(s.toString());
//			System.out.println("The size of the message is: "+s.toString().length());
		
		/*update the hash map*/
		if(dataTable.containsKey(key)){
			dataTable.get(key).countPlus();
		}else{
			dataTable.put(key, item);
		}
		/*Another http package followed*/
//		httpPackageDetect(fin,skipByte+2);
		return new InfoItemSlot(null,0,1);
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
	
	protected void tablePrint(Map<String,InfoItemSlot> table){
		super.tablePrint(table);
	}
	
	protected Map<String,InfoItemSlot> returnDataTable(){
		return super.returnDataTable();
	}
	
	protected Map<String,InfoItemSlot> returnTypeTable(){
		return super.returnTypeTable();
	}
	
	protected Map<String,InfoItemSlot> dupTable(){
		return super.dupTable();
	}
}
