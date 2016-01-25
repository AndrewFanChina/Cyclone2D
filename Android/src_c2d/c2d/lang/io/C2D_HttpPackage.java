package c2d.lang.io;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import c2d.lang.util.debug.C2D_Debug;

/**
 * �������ݰ���ÿ�����Ӷ������һ���̣߳�һ�����ݰ�
 */
public class C2D_HttpPackage implements C2D_HttpPkgReceiver
{
	/** �����߳� */
	private Thread m_httpThread = null;

	/** HTTP����״̬-�գ���û�л��Ľ�� */
	public static final byte CON_EMPTY = -1;
	/** HTTP����״̬-�ɹ� */
	public static final byte CON_SUCCESS = 0;
	/** HTTP����״̬-ʧ�� */
	public static final byte CON_FAIL = 1;
	/** �������ӽ�� */
	private byte m_ConResult = CON_EMPTY;
	/** �Ƿ������������� */
	private boolean m_connecting = false;
	/** �������Ƿ�ʹ�ù� */
	private boolean m_used = false;
	/** �Ѿ���ȡ�������ݵĳ��� */
	private int loadedLen = 0;
	/** ��ȡ���Ķ��������� */
	private byte[] m_data;
	/** ������Ϣ */
	private String m_err;
	/** ��ʱʱ�� */
	public static int TimeOut = 12000;

	/**
	 * ���캯��
	 */
	public C2D_HttpPackage()
	{
	}

	/**
	 * ��⵱ǰ�Ƿ���������
	 * 
	 * @return
	 */
	public boolean isConnecting()
	{
		return m_connecting;
	}

	/**
	 * �����Ƿ��Ѿ�ʹ�ù�
	 * 
	 * @return �Ƿ��Ѿ���ʹ�ù�
	 */
	private boolean hasBeenUsed()
	{
		if (m_used)
		{
			C2D_Debug.logWarn("HttpPackageֻ����һ�����ӡ�");
			return true;
		}
		return false;
	}

	/**
	 * ���첽��ʽ���ͺͶ�ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param url
	 *            ��ȡ��·��
	 * @param receiver
	 *            �������ݵĻص�����
	 * @param sendMethod
	 *            ���ͷ�����POST����GET
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
				// �õ����ݺ���
				m_data = data;
				m_ConResult = (data == null ? CON_FAIL : CON_SUCCESS);

				int len = data != null ? data.length : 0;
				C2D_Debug.log("[NET]connect end, got data " + len + " byte");
				// �����Ƿ�õ����ݣ����������ص�����
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
	 * ��Post��ʽ�첽���ͺͶ�ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param postUrl
	 *            ���ӵ�·��
	 * @param content
	 *            ���͵�����
	 * @param receiver
	 *            �������ݵĻص�����
	 */
	public void requestMsgAsynPost(final String postUrl, final String content, final C2D_HttpPkgReceiver receiver)
	{
		requestMsgAsyn(postUrl,content,receiver,"POST");
	}
	/**
	 * ���첽��ʽ���ͺͶ�ȡ���ݣ�ִ�к������̷��أ�������ȡ��������ûص�������
	 * 
	 * @param url
	 *            ���ӵ�·��
	 * @param content
	 *            ���͵�����
	 * @param receiver
	 *            �������ݵĻص�����
	 * @param sendMethod
	 *            ���ͷ�����POST����GET
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
				//�õ����ݺ���
				m_data = data;
				m_ConResult = (data == null ? CON_FAIL : CON_SUCCESS);
				int len = data != null ? data.length : 0;
				C2D_Debug.log("[NET]connect end, got data " + len + " byte");
				//�����Ƿ�õ����ݣ����������ص�����
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
	 * ��Get��ʽͬ����������ֱ����ɣ�������ִ��ʱ�����͵�ǰ�̣߳�ֱ����ȡ��ɲŻ᷵�ء�
	 * 
	 * @param getUrl
	 *            ·��
	 * @return C2D_HttpMessage ��Ϣ
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
	 * ��Post��ʽ����XML����ͬ����������ֱ����ɣ�������ִ��ʱ�����͵�ǰ�̣߳�ֱ����ȡ��ɲŻ᷵�ء�
	 * 
	 * @param postUrl
	 *            ���ӵ�·��
	 * @param content
	 *            ���͵�����
	 * @param requestCode
	 *            ��������
	 * @return C2D_HttpMessage ��Ϣ
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
	 * �ȴ��������
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
//						m_httpThread.interrupt();//ʵ����interrupt�������ж�IO���׳��쳣���߳�һֱ�ڿ�ס��
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
	 * ��ȡ���յ�������
	 * @return ���յ�������
	 */
	public byte[] getLoadData()
	{
		return m_data;
	}
	/**
	 * ��ȡ������Ϣ
	 * @return
	 */
	public String getError()
	{
		return m_err;
	}
	/**
	 * ����Ѿ����صĳ���
	 * 
	 * @return �Ѿ����صĳ���
	 */
	public int getLoadedLen()
	{
		return loadedLen;
	}

	/**
	 * ��ȡ���ӵĽ��
	 * 
	 * @return ���ӽ��
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
