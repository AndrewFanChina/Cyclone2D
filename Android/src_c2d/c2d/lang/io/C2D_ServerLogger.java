package c2d.lang.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 服务器日志类，在成功连接之后，它将向服务器输出日志，并记录在。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ServerLogger
{
	/**
	 * socket连接
	 */
	private static Socket sock = null;
	/**
	 * 输出流
	 */
	private static DataOutputStream dos = null;
	/**
	 * 是否已经成功连接
	 */
	private static boolean m_connected = false;
	/**
	 * 正在连接中
	 */
	private static boolean m_connecting = false;
	private static String m_flag = "flag";

	/**
	 * 连接并创建服务器日志
	 * 
	 * @param addr
	 * @return 服务器日志
	 */
	public static void connect(final String addr, String flag)
	{
		if (m_connected || m_connecting)
		{
			return;
		}

		if (flag != null)
		{
			m_flag = flag;
		}
		if (addr == null || addr.indexOf(":") < 0)
		{
			return;
		}
		m_connecting = true;
		m_connected = false;

		try
		{
			C2D_IOUtil.increaseCon();
			String strs[] = addr.trim().split(":");
			if (strs.length == 2)
			{
				String url = strs[0];
				int port = Integer.parseInt(strs[1]);
				sock = new Socket(url, port);// "socket://" +
				dos = new DataOutputStream(sock.getOutputStream());
				dos.writeUTF(m_flag);
				m_connected = true;
				C2D_Debug.logC2D("network connection ok,addr: " + addr);
			}
		}
		catch (Exception ex)
		{
//			ex.printStackTrace();
			C2D_Debug.logErr("network connection error,addr: " + addr);
			m_connected = false;
		}
		finally
		{
			if (!m_connected)
			{
				close();
			}
			C2D_IOUtil.decreaseCon();
		}
		m_connecting = false;
	}

	/**
	 * 关闭连接
	 */
	public static void close()
	{
		m_connected = false;
		if (dos != null)
		{
			try
			{
				dos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (sock != null)
		{
			try
			{
				sock.shutdownOutput();
				sock.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dos = null;
		sock = null;
	}

	/**
	 * 检测是否成功进行了连接
	 * 
	 * @return
	 */
	public static boolean isConnected()
	{
		return m_connected;
	}

	/**
	 * 记录字符串信息
	 * 
	 * @param str
	 *            字符串信息
	 */
	public static void log(String str)
	{
		try
		{
			if (dos != null)
			{
				dos.writeUTF(str);
				dos.flush();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * 记录异常信息
	 * 
	 * @param str
	 *            异常信息
	 */
	public static void log(Exception e)
	{
		if (e != null)
		{
			log(e.toString());
		}
	}

	/**
	 * 记录字符串和异常信息
	 * 
	 * @param str
	 *            字符串
	 * @param e
	 *            异常信息
	 */
	public static void log(String str, Exception e)
	{
		log(str);
		log(e);
	}
}
