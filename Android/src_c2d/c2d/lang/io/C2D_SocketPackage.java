package c2d.lang.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import c2d.lang.util.debug.C2D_Debug;

public class C2D_SocketPackage
{
	/** ����ģʽ��ֻ�� */
	public static final int MODE_READ = 1;
	/** ����ģʽ��ֻд */
	public static final int MODE_WRITE = 2;
	/** ����ģʽ����д */
	public static final int MODE_READ_WRITE = 3;
	/**
	 * socket����
	 */
	private Socket sock = null;
	/**
	 * �����
	 */
	private DataOutputStream dos = null;
	/**
	 * ������
	 */
	private DataInputStream dis = null;
	/**
	 * �Ƿ��Ѿ��ɹ�����
	 */
	private boolean m_connected = false;
	/**
	 * �Ƿ�����������
	 */
	private boolean m_connecting = false;
	/**
	 * ����ģʽ
	 */
	private int m_mode;

	/**
	 * ���Ӳ�������������־
	 * 
	 * @param addr
	 *            ���ӵ�ַ(���˿ں�)
	 * @param mode
	 *            ����ģʽ
	 */
	public boolean connect(final String addr, int mode)
	{
		if (addr == null || m_connected || m_connecting)
		{
			return false;
		}
		m_mode = mode;
		if (addr == null || addr.indexOf(":") < 0)
		{
			return false;
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
				sock.setTcpNoDelay(true);
				if (m_mode == MODE_WRITE || m_mode == MODE_READ_WRITE)
				{
					dos = new DataOutputStream(sock.getOutputStream());
				}
				if (m_mode == MODE_READ || m_mode == MODE_READ_WRITE)
				{
					dis = new DataInputStream(sock.getInputStream());
				}
				m_connected = true;
				C2D_Debug.logC2D("network connection ok,addr: " + addr);
			}

		}
		catch (Exception ex)
		{
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
		return m_connected;
	}

	/**
	 * ��������
	 * 
	 * @param data
	 *            ��������
	 * @return �Ƿ�ɹ�����
	 */
	public boolean write(byte data[])
	{
		if (!m_connected || dos == null || data == null)
		{
			return false;
		}
		try
		{
			dos.write(data);
			dos.flush();
		}
		catch (Exception ex)
		{
			close();
			return false;
		}
		return true;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ��ȡ��ȡ��������
	 */
	public byte[] read()
	{
		if (!m_connected || dis == null)
		{
			return null;
		}
		byte[] data = null;
		try
		{
			data = C2D_IOUtil.readByBuffer(dis);
		}
		catch (Exception ex)
		{
			close();
		}
		return data;
	}

	/**
	 * �ر�����
	 */
	public void close()
	{
		if (dis != null)
		{
			try
			{
				dis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
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
				sock.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		dos = null;
		sock = null;
		m_connected = false;
	}
}
