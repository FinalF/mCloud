package AbClasses;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;





public abstract class PackageAnalysis {
	

	
	/*define the behaviour for analysis for down/upload packages*/
	protected static Map<String,InfoItemSlot> dataTable = new HashMap<String,InfoItemSlot>();
	
	
	protected PackageAnalysis(){
		
	}
	
	protected abstract void fileScan(File f) throws FileNotFoundException;
	
	
	protected void tablePrint(){
		for(String key: dataTable.keySet()){
			System.out.print(key);
			System.out.print(":\t");
			System.out.println(dataTable.get(key).toString());
		}
	}
	
	protected void resultOutput(PrintWriter outputFile, String pkgType){
		outputFile.flush();
		outputFile.println(keyTranslate(pkgType)+"\t\t\tType\t\t\tSize\t\t\tCount");
		for(String key: dataTable.keySet()){
			outputFile.print(key);
			outputFile.print("\t,\t");
			outputFile.println(dataTable.get(key).toString());
		}
		outputFile.close();
	}

	private String keyTranslate(String pkgType){
		String key = null;
		if(pkgType.equals("download")){
			key = "Hash";
		}else if(pkgType.equals("upload")){
			key = "URL";
		}else{
			key = "error";
		}
		return key;
	}
}
