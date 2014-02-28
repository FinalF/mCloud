package AbClasses;

import java.io.File;

import Interfaces.InfoItem;

public class InfoItemSlot implements InfoItem{
	//	private String type=null;
	//	private String size=null;
	//	private String count=null;
	private String[] item = new String[3];
	
	public InfoItemSlot(){
		item[0]=null;
		item[1]=null;
		item[2]=null;
	}
	
	
	public InfoItemSlot(String type, int size, int count){
		item[0]=type;
		item[1]=String.valueOf(size);
		item[2]=String.valueOf(count);
	}
	
	
	
	 public void countPlus(){
	 	int count = Integer.parseInt(item[2]);
	 	count++;
	 	item[2]=String.valueOf(count);
	 }
	
	 public void countPlus(int count){
	 	int oldCount = Integer.parseInt(item[2]);
	 	oldCount+=count;
	 	item[2]=String.valueOf(oldCount);
	 }
	 
	 
	 public void sizeUpdate(String size){
		 item[1]=size;
	 }
	 
	 public void sizePlus(int size){
		 item[1]+=String.valueOf(size+Integer.parseInt(item[1]));
		 if(Integer.parseInt(item[1])==0) item[1]="0";
	 }
	 
	 public void typeUpdate(String type){
		 item[0]=String.valueOf(type);
	 }
	
	 public String returnType(){
		return item[0];
	 }

	 public int returnCount(){
		 return Integer.parseInt(item[2]);
	 }

	 public int returnSize(){
		 return Integer.parseInt(item[1]);
	 }
	 
	 public String toString(){
		 return (item[0]+","+item[1]+","+item[2]);
	 }


}
