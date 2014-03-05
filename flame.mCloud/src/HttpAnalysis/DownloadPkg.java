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

	int[] statusCodeRecord = new int[5];
	
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
		int lineCount=0;

		InfoItemSlot item = new InfoItemSlot(null,0,1);
		while(in.hasNextLine()){
			/*Process the header*/
//			System.out.println("This line has : "+line.length+" parts");
			String[] line;
			String l=null;
			lineCount++;

			if(!(l=in.nextLine().trim()).isEmpty()){
				skipByte+=l.length()+1;
				line=l.split(" ");
				/*We filter those bad/error pkgs*/
				if(lineCount==1 &&  line.length>1)
					if(Invalid(line[1].charAt(0)))
						break;

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
	
	private boolean Invalid(char h){
		/*update the record*/
		boolean invalid=false;
		statusCodeRecord[Character.getNumericValue(h)-1]++;
		if(h=='1' || h=='4' || h=='5')
			invalid =true;
		return invalid;
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
	protected void	 resultOutput(PrintWriter outputFile, String pkgType,Map<String,InfoItemSlot> table){
//		System.out.println("Download Table size: "+table.size());
		super.resultOutput(outputFile, pkgType,table);
	}
	
	protected void pkgTypeOutput(PrintWriter outputFile){
		outputFile.flush();
		outputFile.println("The response type:  #");
		for(int i=0; i<statusCodeRecord.length;i++){
			outputFile.println(i+"xx: "+ statusCodeRecord[i]);
		}
		outputFile.close();
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
	
	public int[] returnStatusCodeRecord(){
		for(int i=0; i<statusCodeRecord.length;i++){
			System.out.println(i+"xx: "+ statusCodeRecord[i]);
		}
		return statusCodeRecord;
	}
}
