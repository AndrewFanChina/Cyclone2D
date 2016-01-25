package c2d.lang.io;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 网络数据包，每次连接都会产生一个线程，一个数据包
 */
public class C2D_HttpPackage implements C2D_HttpPkgReceiver
{
	/** 下载线程 */
	private Thread m_httpThread = null;

	/** HTTP连接状态-空，还没有货的结果 */
	public static final byte CON_EMPTY = -1;
	/** HTTP连接状态-成功 */
	public static final byte CON_SUCCESS = 0;
	/** HTTP连接状态-失败 */
	public static final byte CON_FAIL = 1;
	/** 数据连接结果 */
	private byte m_ConResult = CON_EMPTY;
	/** 是否正处于连接中 */
	private boolean m_connecting = false;
	/** 本连接是否被使用过 */
	private boolean m_used = false;
	/** 已经读取到的数据的长度 */
	private int loadedLen = 0;
	/** 读取到的二进制数据 */
	private byte[] m_data;
	/** 错误信息 */
	private String m_err;
	/** 超时时长 */
	public static int TimeOut = 12000;

	/**
	 * 构造函数
	 */
	public C2D_HttpPackage()
	{
	}

	/**
	 * 检测当前是否处于连接中
	 * 
	 * @return
	 */
	public boolean isConnecting()
	{
		return m_connecting;
	}

	/**
	 * 连接是否已经使用过
	 * 
	 * @return 是否已经被使用过
	 */
	private boolean hasBeenUsed()
	{
		if (m_used)
		{
			C2D_Debug.logWarn("HttpPackage只允许一次连接。");
			return true;
		}
		return false;
	}

	/**
	 * 以异步方式发送和读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param url
	 *            读取的路径
	 * @param receiver
	 *            接收数据的回调对象
	 * @param sendMethod
	 *            发送方法，POST或者GET
	 */
	public void requestMsgAsyn(final String url, final C2D_HttpPkgReceiver receiver, final String sendMethod)
	{
		if (hasBeenUsed())
		{
			return;
		}
		m_used = true;
		m_connecting = true;
		loadedLen = 0;
		m_data = null;

		m_httpThread = new Thread(new Runnable()
		{
			public void run()
			{
				HttpURLConnection httpCon = null;
				InputStream inS = null;
				byte data[] = null;
				int rc = 0;
				String err="";
				int tryIme = 0;
				do
				{
				try
				{
					C2D_IOUtil.increaseCon();
					C2D_Debug.log("[NET]try connect->" + url);
					URL urlCon = new URL(url);
					httpCon = (HttpURLConnection) urlCon.openConnection();
					if (httpCon != null)
					{
						// httpCon.setDoInput(true);
						// httpCon.setDoOutput(false);
						httpCon.setRequestMethod(sendMethod);
						httpCon.setUseCaches(false);
						httpCon.setInstanceFollowRedirects(true);
						httpCon.connect();
						rc = httpCon.getResponseCode();
						if (rc == HttpURLConnection.HTTP_OK)
						{
							inS = httpCon.getInputStream();
							int len = httpCon.getContentLength();
							if (len > 0)
							{
								int actual = 0;
								int bytesread = 0;
								data = new byte[len];
								while ((bytesread != len) && (actual != -1))
								{
									actual = inS.read(data, bytesread, len - bytesread);
									bytesread += actual;
									loadedLen = bytesread;
								}
							}
							else
							{
								data=C2D_IOUtil.readByBuffer(inS);
								if(data!=null)
								{
									loadedLen += data.length;
								}
							}
						}
						else
						{
							err = ("NET error (" + "HTTP response code: " + rc + ")");
						}
					}
					
				}
				catch (Exception e)
				{
					data = null;
					C2D_Debug.logErr("<NET exption>(" + e.getMessage() + ")");
					e.printStackTrace();
				}
				finally
				{
					if (inS != null)
					{
						try
						{
							inS.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						inS = null;
					}
					if (httpCon != null)
					{

						try
						{
							httpCon.disconnect();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						httpCon = null;
					}
					C2D_IOUtil.decreaseCon();
					}
					tryIme++;
				}
				while (data == null && tryIme < C2D_IOUtil.MaxTryTime);
				// 得到数据后处理
				m_data = data;
				m_ConResult = (data == null ? CON_FAIL : CON_SUCCESS);

				int len = data != null ? data.length : 0;
				C2D_Debug.log("[NET]connect end, got data " + len + " byte");
				// 无论是否得到数据，都反馈给回调函数
				if (receiver != null)
				{
					receiver.receiveData(data,err);
				}
				m_connecting = false;
			}
		});
		m_httpThread.start();
	}
	/**
	 * 以Post方式异步发送和读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param postUrl
	 *            连接的路径
	 * @param content
	 *            发送的内容
	 * @param receiver
	 *            接收数据的回调对象
	 */
	public void requestMsgAsynPost(final String postUrl, final String content, final C2D_HttpPkgReceiver receiver)
	{
		requestMsgAsyn(postUrl,content,receiver,"POST");
	}
	/**
	 * 以异步方式发送和读取数据，执行后函数立刻返回，待到读取结束会调用回调方法。
	 * 
	 * @param url
	 *            连接的路径
	 * @param content
	 *            发送的内容
	 * @param receiver
	 *            接收数据的回调对象
	 * @param sendMethod
	 *            发送方法，POST或者GET
	 */
	public void requestMsgAsyn(final String url, final String content, final C2D_HttpPkgReceiver receiver,final String sendMethod)
	{
		if (hasBeenUsed())
		{
			return;
		}
		m_used = true;
		m_connecting = true;
		loadedLen = 0;
		m_data = null;

		m_httpThread = new Thread(new Runnable()
		{
			public void run()
			{
				HttpURLConnection httpCon = null;
				InputStream inS = null;
				DataOutputStream dos = null;
				byte data[] = null;
				int rc = 0;
				String err="";
				int tryIme = 0;
				do
				{
				try
				{
					C2D_IOUtil.increaseCon();
					C2D_Debug.log("[NET]try connect->" + url);
					C2D_Debug.log("[NET]send data:" + content);
					byte[] sendBytes = content.getBytes("utf-8");
					int sendLen = sendBytes.length;
					URL urlCon = new URL(url);
					httpCon = (HttpURLConnection) urlCon.openConnection();
					httpCon.setDoInput(true);
					httpCon.setDoOutput(true);
					httpCon.setRequestMethod(sendMethod);
					httpCon.setUseCaches(false);
					httpCon.setInstanceFollowRedirects(true);
					httpCon.setRequestProperty("User-Agent", "C2D");
					httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					httpCon.setRequestProperty("Content-Length", Integer.toString(sendLen));
					httpCon.connect();
					dos = new DataOutputStream(httpCon.getOutputStream());
					dos.write(sendBytes);
					dos.flush();
					rc = httpCon.getResponseCode();
					if (rc == HttpURLConnection.HTTP_OK)
					{
						inS = httpCon.getInputStream();
						int len = (int) httpCon.getContentLength();
						if (len > 0)
						{
							int actual = 0;
							int bytesread = 0;
							data = new byte[len];
							while ((bytesread != len) && (actual != -1))
							{
								actual = inS.read(data, bytesread, len - bytesread);
								bytesread += actual;
								loadedLen = bytesread;
							}
						}
						else
						{
							data=C2D_IOUtil.readByBuffer(inS);
							if(data!=null)
							{
								loadedLen += data.length;
							}
						}
					}
					else
					{
						err = "NET error(" + "HTTP response code: " + rc + ")";
					}
				}
				catch (Exception e)
				{
					err = "NET exception (" + e.getMessage() + ")";
					e.printStackTrace();
				}
				finally
				{
					if (dos != null)
					{
						try
						{
							dos.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dos = null;
					}
					if (inS != null)
					{
						try
						{
							inS.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						inS = null;
					}
					if (httpCon != null)
					{
						try
						{
							httpCon.disconnect();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						httpCon = null;
					}
					C2D_IOUtil.decreaseCon();
				}
					tryIme++;
				}
				while (data == null && tryIme < C2D_IOUtil.MaxTryTime);
				//得到数据后处理
				m_data = data;
				m_ConResult = (data == null ? CON_FAIL : CON_SUCCESS);
				int len = data != null ? data.length : 0;
				C2D_Debug.log("[NET]connect end, got data " + len + " byte");
				//无论是否得到数据，都反馈给回调函数
				if (receiver != null)
				{
					receiver.receiveData(data,err);
				}
				m_connecting = false;
			}
		});
		m_httpThread.start();
	}

	/**
	 * 以Get方式同步载入数据直到完成，本函数执行时会阻滞当前线程，直到读取完成才会返回。
	 * 
	 * @param getUrl
	 *            路径
	 * @return C2D_HttpMessage 消息
	 */
	public byte[] requestMsgSynGet(String getUrl)
	{
		if (hasBeenUsed())
		{
			return null;
		}
		requestMsgAsyn(getUrl, this,"GET");
		waitForConnection();
//		C2D_MiscUtil.log("msg data " + (m_data==null?"null":(m_data.length + " byte")));
		return m_data;
	}

	/**
	 * 以Post方式发送XML，并同步载入数据直到完成，本函数执行时会阻滞当前线程，直到读取完成才会返回。
	 * 
	 * @param postUrl
	 *            连接的路径
	 * @param content
	 *            发送的内容
	 * @param requestCode
	 *            请求类型
	 * @return C2D_HttpMessage 消息
	 */
	public byte[] requestMsgSynPost(final String postUrl, final String content)
	{
		if (hasBeenUsed())
		{
			return "HttpPackage has been used".getBytes();
		}
		requestMsgAsynPost(postUrl, content, this);
		waitForConnection();
		return m_data;
	}

	/**
	 * 等待连接完成
	 * 
	 */
	private void waitForConnection()
	{
		try
		{
//			long timeBegin=System.currentTimeMillis();
			while (isConnecting())
			{
//				if(System.currentTimeMillis()-timeBegin>TimeOut)
//				{
//					if(m_httpThread!=null&&m_httpThread.isAlive())
//					{
//						C2D_MiscUtil.log("<Net Error> time out!");
//						m_httpThread.interrupt();//实际上interrupt并不会中断IO和抛出异常，线程一直在卡住。
//						m_httpThread.join();
//						break;
//					}
//				}
//				else
//				{
//					Thread.yield();
//				}
				Thread.yield();	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取接收到的数据
	 * @return 接收到的数据
	 */
	public byte[] getLoadData()
	{
		return m_data;
	}
	/**
	 * 获取错误信息
	 * @return
	 */
	public String getError()
	{
		return m_err;
	}
	/**
	 * 获得已经下载的长度
	 * 
	 * @return 已经下载的长度
	 */
	public int getLoadedLen()
	{
		return loadedLen;
	}

	/**
	 * 获取连接的结果
	 * 
	 * @return 连接结果
	 */
	public byte getResult()
	{
		return m_ConResult;
	}
	public void receiveData(byte[] pkgData,String err)
	{
		m_data = pkgData;
		m_err =err;
	}
	public static String DealResult(byte[] pkgData, String err)
	{
		String s = "";
		if(pkgData!=null)
		{
			if (err != null&&err.length()>0)
			{
				s += err + ",";
			}
			try
			{
				s += new String(pkgData, "utf-8");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return s;
	}
}
