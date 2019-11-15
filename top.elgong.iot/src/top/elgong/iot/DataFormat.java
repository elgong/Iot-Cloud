package top.elgong.iot;

import java.util.ArrayList;

/**
 *  数据协议的封装
 * @author elgong
 *
 */
public class DataFormat {

	Config conf = Config.getIntance();
	
	private long deviceID = 0;   // 设备 ID 
	private String[] upTime = new String[1];
	private int[] state = new int[4];
	private int[] data = new int[4];

	// 默认构造函数
	public DataFormat() {
		
	}
	
	// 对数据的修改操作

	public void setDeviceID(long deviceID) {
		this.deviceID = deviceID;
	}
	public void setState(String sta) {

		String[] tmp = sta.split(" ");
		for(int i=0; i < tmp.length;  i++) {
			
			this.state[i] = Integer.valueOf(tmp[i]);
		}
	}
	
	public void setData(String dat) {
		
		String[] tmp = dat.split(" ");
		for(int i=0; i < tmp.length;  i++) {
			
			this.data[i] = Integer.valueOf(tmp[i]);
		}
	}
	
	public void setUpTime(String time) {
		
		this.upTime[0] = time;
	}
	
	
	// 查询操作
	public int[] getState() {
		
		return state;
	}

	public int[] getData() {
		return data;
	}

	public char getHead() {
		return conf.DATA_FORMAT_HEAD;
	}
	public char getEnd() {
		return conf.DATA_FORMAT_END;
	}
	public String getUpTime() {
		return upTime[0];
	}
	
	public long getDeviceID() {
		return deviceID;
	}
	
	public boolean setAll(String dat)  {
		
		String[] dataArr = dat.split(","); 
		
		// 数据长度验证  6个
		if(dataArr.length != conf.DATA_FORMAT_LEN) {
			System.out.println(dataArr.length);
			return false;
		}
		
		this.setDeviceID(Long.valueOf(dataArr[1]));  // 设备 ID
		this.setUpTime(dataArr[2]);    // 设置时间
		this.setState(dataArr[3]);  // 设置 state
		this.setData(dataArr[4]);   // 设置 data
		
		return true;
	}
}
