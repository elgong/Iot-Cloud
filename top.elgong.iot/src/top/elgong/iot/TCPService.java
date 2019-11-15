package top.elgong.iot;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TCPService {
	
	// 服务器配置参数
    Config conf = Config.getIntance();
    
    // 接收消息队列
    ConcurrentLinkedQueue<DataFormat> dataArrReceive = new ConcurrentLinkedQueue<DataFormat>();

    /**
         *  主程序
     * @param args
     */
    public static void main(String[] args) {
        TCPService service1 = new TCPService();
        service1.startService();
    }

    /**
         *   服务器程序
     */
    public void startService(){
        try {
            InetAddress address = InetAddress.getByName(conf.SERVICE_IP);
            Socket connect = null;
            ExecutorService pool = Executors.newFixedThreadPool(10);
            
            //创建sv任务：将接收消息队列中数据写入文件
            Saver sv = new Saver();
            
            //放入线程池等待运行
            pool.execute(sv);
          
            try (ServerSocket service = new ServerSocket(conf.SERVICE_PORT,5,address)){
                while(true){

                	connect = service.accept();
                	                    
                    // 有TCP新连接，建立连接任务
                    ServiceTask serviceTask = new ServiceTask(connect);
                    //放入线程池等待运行
                    pool.execute(serviceTask);  
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(connect!=null)
                    connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *      有TCP新连接，建立连接任务
     * @author Misaya
     *
     */
    class ServiceTask implements Runnable{
        private Socket socket;

        ServiceTask(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                StringBuilder receiveMsg = new StringBuilder();
                InputStream in = socket.getInputStream();
                for (int c = in.read(); c != conf.DATA_FORMAT_END; c = in.read()) {
                    if(c ==-1)
                        break;
                    receiveMsg.append((char)c);
                }
                receiveMsg.append('#');
                
                System.out.println("receive data:  " + receiveMsg.toString());
                DataFormat data = new DataFormat();
                data.setAll(receiveMsg.toString());
                dataArrReceive.offer(data);
                //Thread.currentThread().sleep(5000);
                
                String response = "SERVICE GET" + conf.DATA_FORMAT_END + "\n";
                OutputStream out = socket.getOutputStream();
                out.write(response.getBytes());
                
            }catch (Exception e){
                e.printStackTrace();
            }finally {
               if(socket!=null)
                   try {
                       socket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
            }
        }
    }
    
    
    class Saver implements Runnable{

        @Override
        public void run() {
        	while(true) {
        		if(dataArrReceive.size() !=0) {
            		FileSaver fs = new FileSaver(dataArrReceive);
            		fs.writer();
            		System.out.println("write OK");
        		}

        	}

        }
    }
}

