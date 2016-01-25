package c2d.lang.util;

public class C2D_ThreadLock
{
	/** 同步对象 */
	private Object m_synObj = new Object();
	private boolean m_invalid=false;
	/**
	 * 解锁线程，如果之前有锁定的话
	 */
	public void unlockThread()
	{
		unlockThread(false);
	}
	/**
	 * 解锁线程，如果之前有锁定的话
	 * @param invalid，是否解锁后使其失效，即不再允许锁定
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
				System.out.println("解锁线程锁："+m_synObj);
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
	 * 锁定线程
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
				System.out.println("锁定线程："+Thread.currentThread());
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
