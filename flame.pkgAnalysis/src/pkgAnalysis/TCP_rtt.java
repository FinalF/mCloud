package pkgAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;




public class TCP_rtt{
/*Aim to get the stats of RTT of every TCP transmission*/
/*Use a hash table to record information
 * for each tCP transmission, record:
 * Key: pair of port: Master > Client (i.e. 8123>57120)
 * (0)rtt_begin (time stamp of 1st pkg sent) | (1)rtt_end (time stamp of last pkg sent | 
 * (2)# of received pkgs |
 * (3)# of sent pkgs | (4) total size (len)*/	
	
	private  Map<String,double[]> infoTable;
	private File f;
	private String cltIP=null;
	
	public TCP_rtt( String cltIP,File f){
		this.f = f;
		this.cltIP=cltIP;
		this.infoTable = new HashMap<String,double[]>() ;
	}
	
	public void fileScan() throws FileNotFoundException{
		Scanner in = new Scanner(f);
		in.nextLine();
		while(in.hasNextLine()){
			boolean toClient=true;
			String[] line = in.nextLine().split("\",\"");
				/*Exceptions*/
//				if(line[4].contains("RELOAD"))
//					break;
				if(!line[4].equals("TCP"))
					continue;
				//determine the direction of this pkg
				if(!line[3].equals(cltIP)){
					toClient=false; //this is a pkg sent from client to master
				}
				
			double len = Double.parseDouble(line[5]);
			int countIndex=3;
			System.out.println(line[6]);
			String[] info=line[6].split(" ");
			
//			if(info[3].equals("[FIN,"))
//				break;
			
			String port;
				if(toClient==true){
					port = info[2];
					countIndex = 2;
				}else{
					port = info[0];				
				}
			System.out.println("Port is: "+port);
			if(infoTable.containsKey(port)){
				double[] tmp = infoTable.get(port);
				tmp[countIndex]++;
//				tmp[4]+=len;//TCP size
				tmp[4]+=findLength(info); //only data length
				if(line[6].contains("[FIN")) //toClient==false && 
//				tmp[1]= findTSval(info);   //accurate time
					tmp[1]=Double.parseDouble(line[1]); //capture time stamp, not accurate
				infoTable.put(port, tmp);
			}else{
				//has to be the SYN pck from the client
				double[] tmp = new double[5];
//				tmp[0]= findTSval(info);
				if(line[6].contains("[SYN]"))
				tmp[0]=Double.parseDouble(line[1]); //capture time stamp, not accurate
//				tmp[4]+=len;
				tmp[4]+=findLength(info); //only data length
				tmp[3]=1;
				infoTable.put(port, tmp);
			}
	
		}
	}

	double findTSval(String[] s){
		double TSval=0;
		boolean find=false;
		int i=0;
		while(find ==false && i<s.length){
			String tmp = s[i];
			if(tmp.contains("TSval")){
				find=true;
				TSval=Double.parseDouble(tmp.substring(6));
			}
			i++;
		}
		return TSval;
	}
	
	double findLength(String[] s){
		double length=0;
		boolean find=false;
		int i=0;
		while(find ==false && i<s.length){
			String tmp = s[i];
			if(tmp.contains("Len=")){
				find=true;
				String tmpNum=tmp.split("\"")[0];
				length=Double.parseDouble(tmpNum.substring(4));
			}
			i++;
		}
		return length;
	}

	public void printTable(){
			Map<String,double[]> sortedInfoTable = new TreeMap<String,double[]>(infoTable);
			System.out.println("Port | Begin time | End time | # of receive | # of sent | length");
			for(String key: sortedInfoTable.keySet()){
				System.out.print(key);
				System.out.print(":\t");
				double[] data = sortedInfoTable.get(key);
				for(int i=0;i<data.length;i++){
					System.out.print(data[i]);
					System.out.print(":\t");
				}
				System.out.println();
			}
	}
	
	public void outputTable(File f) throws FileNotFoundException{
		Map<String,double[]> sortedInfoTable = new TreeMap<String,double[]>(infoTable);
		PrintWriter out = new PrintWriter(f);
		out.flush();
		out.println("Port | Begin time | End time | # of receive | # of sent | length");
		for(String key: sortedInfoTable.keySet()){
			out.print(key);
			out.print("\t");
			double[] data = sortedInfoTable.get(key);
			for(int i=0;i<data.length;i++){
				out.print(data[i]);
				out.print("\t");
			}
			out.println();
		}
		out.close();
	}
}
	
