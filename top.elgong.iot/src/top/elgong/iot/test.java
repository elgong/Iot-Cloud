package top.elgong.iot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class test {

	public static void main(String[] args) throws Exception {
		
		
		DataFormat data = new DataFormat();
		
		String dataStr = "$,1,1995-02-03-11:20,3 4 5 6,7 8 9 10,#";
		data.setAll(dataStr);
		
		ConcurrentLinkedQueue<DataFormat> dataArr = new ConcurrentLinkedQueue<DataFormat>();
	
		dataArr.offer(data);

		FileSaver fs = new FileSaver(dataArr);
		fs.writer();
		System.out.println(data.getUpTime());
	}
}
