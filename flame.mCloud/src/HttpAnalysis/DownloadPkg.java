package HttpAnalysis;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;


import AbClasses.*;


public class DownloadPkg extends PackageAnalysis {
//	Map<String,InfoItemSlot> dataTable = new HashMap<String,InfoItemSlot>();
	
	DownloadPkg(){
		super();
	}

	protected void fileScan(File f) throws FileNotFoundException{
		boolean chunkEncoding=false;
		boolean emptyPkg=false;
		boolean typeDefined=false;
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
				if(line[0].contains("Content-Length")){
					//we record the length
					item.sizeUpdate(line[1]);
//					System.out.println("SIze is: "+line[1]);
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

				}else if(line[0].contains("Content-Type")){
					typeDefined=true; //it has a type
					String[] contentType = line[1].toString().split(";");
					item.typeUpdate(contentType[0]);
					System.out.println("Type is: "+contentType[0]);
					if(emptyPkg==true) break;
				
					
				}else if(line[0].contains("Transfer-Encoding")){
					//transfer-encoding used, we need to extract length from the message body
					chunkEncoding=true;
//					System.out.println("This package has used chunkencoding");
				}
			}else{
//				System.out.println("Should be an empty line: "+in.nextLine()); //jump the blank line
				/*Process the messagebody*/
				StringBuilder s = new StringBuilder();
				if(chunkEncoding==true){
					item.sizeUpdate(String.valueOf(Integer.parseInt(in.nextLine(), 16)));//file length
						while(in.hasNextLine()){
							s.append(in.nextLine());
						}
				}else{
					while(in.hasNextLine()){
						s.append(in.nextLine());
					}
				}
				if(emptyPkg==false)
				key=DigestUtils.shaHex(s.toString());
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
	protected void	resultOutput(PrintWriter outputFile, String pkgType){
		System.out.println("Download Table size: "+dataTable.size());
		super.resultOutput(outputFile, pkgType);
	}
	protected void tablePrint(){
		super.tablePrint();
	}
}
