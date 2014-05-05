package Test;

import java.io.File;
import java.io.FileNotFoundException;

import pkgAnalysis.TCP_rtt;

public class tcp_rtt_mesuare {

	public static void main(String args[]) throws FileNotFoundException{
		String clientIP = args[0];
		String filename = args[1];
		File f = new File("/home/wang/Research/mCloudlet/App&Data/TCP_pkg/pkg/"+filename);
		File output = new File("/home/wang/Research/mCloudlet/App&Data/TCP_pkg/stats/"+filename);
		TCP_rtt P = new TCP_rtt(clientIP,f);
		P.fileScan();
		P.printTable();
		P.outputTable(output);
	}
	
	
}
