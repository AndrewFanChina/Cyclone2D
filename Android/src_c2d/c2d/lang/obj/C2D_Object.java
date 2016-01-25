package c2d.lang.obj;

import c2d.lang.math.C2D_Array;

/**
 * C2D对象，默认所有权归自己，只有自己允许释放自己。
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Object implements C2D_ObjectHandler
{
	private static int NUM_C2D_OBJ = 0;
	public static boolean EnableDebugPool = false;
	public static C2D_Array DebugArray = new C2D_Array();
	/** 当前所有权归属者 */
	protected C2D_ObjectHandler m_handler;
	/** 归属者对当前对象是否使用强引用，默认弱引用，当所有权被转移，引用加强*/
	protected boolean m_strongLink;
	public C2D_Object()
	{
		m_handler = this;
		m_strongLink=false;
		NUM_C2D_OBJ++;
		if (EnableDebugPool)
		{
			DebugArray.addElement(this);
		}
	}

	/**
	 * 进行资源卸载
	 * 
	 * @return 是否成功卸载
	 */
	public abstract void onRelease();

	/**
	 * 获取当前对象个数
	 * 
	 * @return 对象个数
	 */
	public static int GetNumC2D_OBJ()
	{
		return NUM_C2D_OBJ;
	}

	/**
	 * 执行释放，以自身作为当前资源拥有者进行释放
	 * 
	 * @return 是否被卸载
	 */
	public final boolean doRelease()
	{
		return doRelease(this);
	}

	/**
	 * 执行释放，验证是当前资源拥有者的情况下，或者当前引用是弱引用的情况下，允许进行释放
	 * @param handler
	 *            传入的资源拥有者
	 * @return 是否被卸载
	 */
	public final boolean doRelease(C2D_ObjectHandler handler)
	{
		if (m_handler == null)
		{
			return false;
		}
		if (handler != null)
		{
			if(handler.equals(m_handler)||!m_strongLink)
			{
				onRelease();
//				m_handler = null;
				if (EnableDebugPool)
				{
					DebugArray.remove(this);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 转移当前对象的所有权归属，同时加强引用
	 * 
	 * @param handler
	 *            当前对象的所有权归属者
	 */
	public void transHadler(C2D_ObjectHandler handler)
	{
		if (handler != null && m_handler != null)
		{
			m_handler = handler;
			m_strongLink=true;
		}
	}
	/**
	 * 重新设置成员，如果旧成员存在且不与新设置成员相同，则尝试卸载
	 * @param oldMember 旧成员
	 * @param newMember 新成员
	 */
	public void resetMemer(C2D_Object oldMember,C2D_Object newMember)
	{
		if(oldMember!=null &&!oldMember.equals(newMember))
		{
			oldMember.doRelease(this);
		}
	}
	/**
	 * 以自身为掌权者，卸载成员对象
	 * @param member 成员对象
	 */
	public void releaseMemer(C2D_Object member)
	{
		if(member!=null)
		{
			member.doRelease(this);
		}
	}
}
