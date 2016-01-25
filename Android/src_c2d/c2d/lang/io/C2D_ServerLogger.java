package c2d.lang.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ��������־�࣬�ڳɹ�����֮������������������־������¼�ڡ�
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ServerLogger
{
	/**
	 * socket����
	 */
	private static Socket sock = null;
	/**
	 * �����
	 */
	private static DataOutputStream dos = null;
	/**
	 * �Ƿ��Ѿ��ɹ�����
	 */
	private static boolean m_connected = false;
	/**
	 * ����������
	 */
	private static boolean m_connecting = false;
	private static String m_flag = "flag";

	/**
	 * ���Ӳ�������������־
	 * 
	 * @param addr
	 * @return ��������־
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
	 * �ر�����
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
	 * ����Ƿ�ɹ�����������
	 * 
	 * @return
	 */
	public static boolean isConnected()
	{
		return m_connected;
	}

	/**
	 * ��¼�ַ�����Ϣ
	 * 
	 * @param str
	 *            �ַ�����Ϣ
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
	 * ��¼�쳣��Ϣ
	 * 
	 * @param str
	 *            �쳣��Ϣ
	 */
	public static void log(Exception e)
	{
		if (e != null)
		{
			log(e.toString());
		}
	}

	/**
	 * ��¼�ַ������쳣��Ϣ
	 * 
	 * @param str
	 *            �ַ���
	 * @param e
	 *            �쳣��Ϣ
	 */
	public static void log(String str, Exception e)
	{
		log(str);
		log(e);
	}
}
