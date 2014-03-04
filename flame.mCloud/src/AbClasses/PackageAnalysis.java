package AbClasses;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;





public abstract class PackageAnalysis {
	

	
	/*define the behaviour for analysis for down/upload packages*/
	protected  Map<String,InfoItemSlot> dataTable;
	protected  Map<String,InfoItemSlot> typeTable;
	protected  Map<String,InfoItemSlot> dupTable;
	
	
	protected PackageAnalysis(){
		dataTable = new HashMap<String,InfoItemSlot>();
		typeTable = new HashMap<String,InfoItemSlot>();
		dupTable = new HashMap<String,InfoItemSlot>();
	}
	
	/*read in packages, return a hashmap contains all the information for analysis*/
	protected abstract void fileScan(File f)  throws IOException;
	
	
	
	/*check dataTable, merge them based on transaction type.
	 * Construct another hashmap to record it*/
	protected  void typeTableGen(){

		for(String key: dataTable.keySet()){
				/*This http transanction happens more than once*/
				String typeType = dataTable.get(key).returnType();
				System.out.println("Pcik out record with type of: "+typeType);
				int typeCount = dataTable.get(key).returnCount();
				int typeSize = typeCount*dataTable.get(key).returnSize();
				
				InfoItemSlot item = new InfoItemSlot(typeType,typeSize,typeCount);
				/*generate the type-based table*/
				if(!typeTable.containsKey(typeType)){
					/*update the dupTable*/
					typeTable.put(typeType,item);
	
				}else{
					typeTable.get(typeType).sizePlus(typeSize);
					typeTable.get(typeType).countPlus(typeCount);
				}
				/*generate the type-based dup table*/
				
				if(typeCount>1){ 
					/*we only count the unique part!*/
					typeCount--;
					typeSize-=dataTable.get(key).returnSize();
					InfoItemSlot dupItem = new InfoItemSlot(typeType,typeSize,typeCount);
					if(!dupTable.containsKey(typeType)){
						/*update the dupTable*/
						dupTable.put(typeType,dupItem);
		
					}else{
						dupTable.get(typeType).sizePlus(typeSize);
						dupTable.get(typeType).countPlus(typeCount);
					}
				}
		}
	}
	
	protected  void dupTableGen(){
	
	}
	
	protected void tablePrint(Map<String,InfoItemSlot> table){
		for(String key: table.keySet()){
			System.out.print(key);
			System.out.print(":\t");
			System.out.println(table.get(key).toString());
		}
	}
	
	protected Map<String,InfoItemSlot> returnDataTable(){
		return dataTable;
	}
	
	protected Map<String,InfoItemSlot> returnTypeTable(){
		return typeTable;
	}
	
	protected Map<String,InfoItemSlot> dupTable(){
		return dupTable;
	}
	
 	protected void resultOutput(PrintWriter outputFile, String pkgType, Map<String,InfoItemSlot> table){
		outputFile.flush();
		outputFile.println(keyTranslate(pkgType)+"\t\t\tType\t\t\tSize\t\t\tCount");
		for(String key: table.keySet()){
			outputFile.print(key);
			outputFile.print("\t,\t");
			outputFile.println(table.get(key).toString());
		}
		outputFile.close();
	}

	private String keyTranslate(String pkgType){
		String key = null;
		if(pkgType.equals("download")){
			key = "Hash";
		}else if(pkgType.equals("upload")){
			key = "URL";
		}else if(pkgType.equals("uploadType")){
			key = "Upload Type";
		}else if(pkgType.equals("downloadType")){
			key = "Download Type";
		}else{
			key = "error";
		}
		return key;
	}
}
