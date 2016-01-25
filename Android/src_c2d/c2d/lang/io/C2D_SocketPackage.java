package c2d.lang.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import c2d.lang.util.debug.C2D_Debug;

public class C2D_SocketPackage
{
	/** 连接模式，只读 */
	public static final int MODE_READ = 1;
	/** 连接模式，只写 */
	public static final int MODE_WRITE = 2;
	/** 连接模式，读写 */
	public static final int MODE_READ_WRITE = 3;
	/**
	 * socket连接
	 */
	private Socket sock = null;
	/**
	 * 输出流
	 */
	private DataOutputStream dos = null;
	/**
	 * 输入流
	 */
	private DataInputStream dis = null;
	/**
	 * 是否已经成功连接
	 */
	private boolean m_connected = false;
	/**
	 * 是否正在连接中
	 */
	private boolean m_connecting = false;
	/**
	 * 连接模式
	 */
	private int m_mode;

	/**
	 * 连接并创建服务器日志
	 * 
	 * @param addr
	 *            连接地址(含端口号)
	 * @param mode
	 *            连接模式
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
	 * 发送数据
	 * 
	 * @param data
	 *            数据数组
	 * @return 是否成功发送
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
	 * 读取数据
	 * 
	 * @return 获取读取到的数据
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
	 * 关闭连接
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
