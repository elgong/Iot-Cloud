package top.elgong.iot;

import java.io.File;
import java.io.IOException;

/**
 *   服务器 参数配置
 *   
 *  单例模式实现
 *      懒汉模式---非线程安全
 * @author elgong
 *
 */
public class Config {
	

	/* 服务器连接参数*/
	public static final String SERVICE_IP = "192.168.1.118";   // IP地址
	public static final int SERVICE_PORT = 10101;     // 端口号
	
	
	/* DataFormat 数据协议使用的参数 */
	public final char DATA_FORMAT_HEAD = '$';   // 数据协议的 包头
	public final char  DATA_FORMAT_END = '#';   // 数据协议的包尾
	public final int DATA_FORMAT_LEN = 6;       //  切分后的数据字段长度
	
	/* FileSaver 使用的参数 */
	public final String filePath = getProjectPath() + "\\database\\";   //当前项目下路径
    public final String COLUMNS_NAME_CSV = "time,state1,state2,state3,state4,data1,data2,data3,data4";  // csv 文件头
    
    
    
    /* 单例模式的实现 */
	private static Config conf = null;
	
	private Config() {
		
	}
	public static Config getIntance() {
		
		if(conf == null) {
			conf = new Config();
		}
		return conf;
	}
	
	/* 得到工程的当前路径 */
	private String getProjectPath() {
	    File file = new File("");
		String projectPath = null;
		try {
			projectPath = file.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return projectPath;
	}
}
