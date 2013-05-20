package service.activity.com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 套接字底层发送和接收
 * 
 * @author Liang
 * 
 */
public class SocketClient {
	public Socket socket;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private InetAddress host;
	private int port;

	/**
	 * 当接收到信息时的监听器(回调接口)
	 * 
	 * @author Liang
	 */
	public interface OnReceiveListener {
		public void onReceive(String receiveContent);
	}

	/**
	 * 生成一个针对 host主机上port端口的socket对象
	 * 
	 * @param host
	 *            目标主机名
	 * @param port
	 *            主机端口号
	 * @throws IOException
	 * @throws Exception
	 */
	public SocketClient(String host, int port) throws IOException, Exception {
		this.host = InetAddress.getByName(host);
		this.port = port;
	}

	/**
	 * 开启端口连接
	 * 
	 * @throws IOException
	 */
	public void createSocket() throws IOException {
		socket = new Socket(host, port);
	}

	/**
	 * 开始监听
	 * 
	 * @param orl
	 * @throws IOException
	 */
	public void startListening(OnReceiveListener orl) throws IOException {
		dataInputStream = new DataInputStream(socket.getInputStream());
		while (true) {
			String receive = dataInputStream.readUTF();
			orl.onReceive(receive);
			System.out.println("Peer:  " + receive);
		}
	}

	/**
	 * 发送内容字符串
	 * 
	 * @param content
	 * @throws IOException
	 */
	public void sendString(String content) throws IOException {
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
		dataOutputStream.writeUTF(content);
		System.out.println("Send:      " + content);
		dataOutputStream.flush();
	}

	/**
	 * 关闭发送、接收频道
	 */
	public void closeChannel() {
		try {
			if (dataOutputStream != null) {
				dataOutputStream.close();
			}
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭socket对象
	 */
	public void close() {
		try {
			closeChannel();
			if (!socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
