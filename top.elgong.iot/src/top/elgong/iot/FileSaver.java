package top.elgong.iot;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *  保存文件使用的类方法
 * @author Misaya
 *
 */
public class FileSaver {

	/* 消息接收缓冲队列*/
	private ConcurrentLinkedQueue<DataFormat> dataArrReceive = null;
	
	Config conf = Config.getIntance();
	
	/**
	 *  构造函数
	 * @param dataArrReceive
	 */
	public FileSaver(ConcurrentLinkedQueue<DataFormat> dataArr) {
		this.dataArrReceive = dataArr;
	}
	
	/**
	 *  保存到文件
	 */
	public void writer() {
		
		// 待解析的数据
		long deviceID = 0;   
		String[] upTime = new String[1];
		int[] state = new int[4];
		int[] data = new int[4];
		
		// 遍历处理ConcurrentLinkedQueue中所有的 DataFormat数据对象
		// 当队列非空
		while(!this.dataArrReceive.isEmpty()) {
		    
			//  sb 解析一行的数据
			StringBuilder sb = new StringBuilder(30);
			
			//  拿到一个DataFormat对象
			DataFormat datTmp = this.dataArrReceive.poll();
			
			deviceID = datTmp.getDeviceID();
			String time = datTmp.getUpTime();
			state = datTmp.getState();
			data = datTmp.getData();
			
			// 每个设备对应一个数据库文件，设备名为文件名
			String fileName = conf.filePath + Long.toString(deviceID) + ".csv";
			
			File file = new File(fileName);
			
			//  第一次创建文件
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				appendMethodA(fileName,  conf.COLUMNS_NAME_CSV);
			}
			
			// 需要换行
		    sb.append("\n");
			
			// 信息提取到sb
			sb.append(time);
			for(int j=0; j<state.length; j++) {
				sb.append("," + Integer.toString(state[j]) );
			}
			for(int j=0; j<data.length; j++) {
				sb.append("," + Integer.toString(data[j]) );
			}
			System.out.println(sb.toString());
			appendMethodA(fileName,  sb.toString());
		}
	}
	
	private  static void appendMethodA(String fileName, String content) {
		 
	    try {
	         // 打开一个随机访问文件流，按读写方式
	         RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");

	         // 文件长度，字节数
	         long fileLength = randomFile.length();

	         // 将写文件指针移到文件尾。
	         randomFile.seek(fileLength);

	         randomFile.writeBytes(content);

	         randomFile.close();

	    } catch (IOException e) {

	         e.printStackTrace();
	    }
	}
}
