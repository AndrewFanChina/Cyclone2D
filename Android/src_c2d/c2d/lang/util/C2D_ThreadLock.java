package c2d.lang.util;

public class C2D_ThreadLock
{
	/** ͬ������ */
	private Object m_synObj = new Object();
	private boolean m_invalid=false;
	/**
	 * �����̣߳����֮ǰ�������Ļ�
	 */
	public void unlockThread()
	{
		unlockThread(false);
	}
	/**
	 * �����̣߳����֮ǰ�������Ļ�
	 * @param invalid���Ƿ������ʹ��ʧЧ����������������
	 */
	public void unlockThread(boolean invalid)
	{
		if(m_synObj==null)
		{
			return;
		}
		synchronized (m_synObj)
		{
			try
			{
				System.out.println("�����߳�����"+m_synObj);
				m_synObj.notify();
			}
			catch (Exception e)
			{
				if (e != null)
				{
					e.printStackTrace();
				}
			}
			m_invalid=invalid;
		}
	}

	/**
	 * �����߳�
	 */
	public void lockThread()
	{
		if(m_synObj==null||m_invalid)
		{
			return;
		}
		synchronized (m_synObj)
		{
			try
			{
				System.out.println("�����̣߳�"+Thread.currentThread());
				m_synObj.wait();
			}
			catch (InterruptedException e)
			{
				if (e != null)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
