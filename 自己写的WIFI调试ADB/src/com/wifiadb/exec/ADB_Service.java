package com.wifiadb.exec;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

@SuppressWarnings("unused")
public class ADB_Service {
	protected static int execRootCmdSilent(String paramString)
			throws IOException, InterruptedException {
		Process localProcess = Runtime.getRuntime().exec("su");
		OutputStream localOutputStream = (OutputStream) localProcess
				.getOutputStream();
		DataOutputStream localDataOutputStream = new DataOutputStream(
				localOutputStream);
		String str1 = String.valueOf(String.valueOf(paramString));
		String str2 = (String) (str1 + "\n");
		localDataOutputStream.writeBytes(str2);
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("exit\n");
		localDataOutputStream.flush();
		localProcess.waitFor();
		return localProcess.exitValue();
	}

	protected static boolean haveRoot() throws IOException,
			InterruptedException {
		int i = execRootCmdSilent("ls");
		PrintStream localPrintStream = System.out;
		String str = "i->" + i;
		localPrintStream.println(str);
		int j = -1;
		int k;
		if (i != j)
			k = 1;
		while (true) {
			Object localObject = null;
		}
	}

	protected static int reset() throws IOException, InterruptedException {
		Process localProcess = Runtime.getRuntime().exec("su");
		OutputStream localOutputStream = (OutputStream) localProcess
				.getOutputStream();
		DataOutputStream localDataOutputStream = new DataOutputStream(
				localOutputStream);
		localDataOutputStream.writeBytes("setprop service.adb.tcp.port -1\n");
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("stop adbd\n");
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("start adbd\n");
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("exit\n");
		localDataOutputStream.flush();
		localProcess.waitFor();
		return localProcess.exitValue();
	}

	protected static int set(int paramInt) throws IOException,
			InterruptedException {
		Process localProcess = Runtime.getRuntime().exec("su");
		OutputStream localOutputStream = (OutputStream) localProcess
				.getOutputStream();
		DataOutputStream localDataOutputStream = new DataOutputStream(
				localOutputStream);
		String str = "setprop service.adb.tcp.port " + paramInt + "\n";
		localDataOutputStream.writeBytes(str);
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("stop adbd\n");
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("start adbd\n");
		localDataOutputStream.flush();
		localDataOutputStream.writeBytes("exit\n");
		localDataOutputStream.flush();
		localProcess.waitFor();
		return localProcess.exitValue();
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}
}

/*
 * Location:
 * D:\Android-SDK16\UnpackAPK\dex2jar-0.0.7-SNAPSHOT\classes.dex.dex2jar.jar
 * Qualified Name: com.ilovn.app.wifi_adb.ADB_Service JD-Core Version: 0.5.4
 */