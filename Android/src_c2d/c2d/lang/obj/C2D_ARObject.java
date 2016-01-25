package c2d.lang.obj;

/**
 * C2D自动释放对象，默认所有权仍然属于自己，除非你主动将其放入自动对象池
 * @author AndrewFan
 *
 */
public abstract class C2D_ARObject implements C2D_ObjectHandler
{
	/** 当前所有权归属者 */
	protected C2D_ObjectHandler m_handler;
	/** 被使用次数 */
	private int m_usedCount = 0;
	
	private static int NUM_C2D_OBJ = 0;
	public static C2D_ARPoolStack DebugPoolStack = new C2D_ARPoolStack();
	public static boolean EnableDebugPool = false;
	/**
	 * 增加一次引用
	 */
	public void retain()
	{
		m_usedCount++;
	}

	/**
	 * 将对象放入自动释放对象池
	 */
	public void autoRelease()
	{
		C2D_ARPoolStack.ARStack().current().fillIn(this);
	}
	/**
	 * 进行资源释放，以自身作为资源拥有者。 无论如何是否释放，都将执行一次引用次数减少。
	 * 
	 * @return 是否被卸载
	 */
	public boolean doRelease()
	{
		boolean res = doRelease(this);
		return res;
	}
	/**
	 * 执行释放，只允许进行一次“引用删除”,当传入的资源拥有者。 被确认为当前资源的拥有者，并且引用计数为0，才最终被执行卸载。
	 * 无论如何是否释放，都将执行一次引用次数减少。
	 * 
	 * @param handler
	 *            传入的资源拥有者
	 * @return 是否被卸载
	 */
	public final boolean doRelease(C2D_ObjectHandler handler)
	{
		if (m_usedCount > 0)
		{
			m_usedCount--;
		}
		if(m_handler!=null)
		{
			//如果引用计数为0，传入的是吻合掌权者，或者掌权者是自己，将允许执行释放
			if (m_usedCount == 0)
			{
				if (m_handler.equals(handler)||m_handler.equals(this))
				{
					NUM_C2D_OBJ--;
					if (EnableDebugPool)
					{
						DebugPoolStack.current().pullOut(this);
					}
					m_handler = null;
					onRelease();
					return true;
				}
			}
			//如果对象仍有其它引用，传入的非对象池掌权者吻合，需要转移掌权者给对象池
			else
			{
				if (m_handler.equals(handler) && !(m_handler instanceof C2D_ARPool))
				{
					// 原有的非对象池中的对象的所有者已经完成卸载，
					// 当前对象还不能卸载，只能将当前对象移入自动对象池
					autoRelease();
				}
			}
		}
		return false;
	}
	/**
	 * 让自己成为自己的所有者
	 * 
	 * @return 自己
	 */
	public C2D_ARObject handleSelf()
	{
		this.transHadler(this);
		return this;
	}

	/**
	 * 增加引用计数的同时，转移当前对象的所有权归属
	 * 
	 * @param handler
	 *            当前对象的所有权归属者
	 */
	public void retain(C2D_ObjectHandler handler)
	{
		if (handler != null && m_handler != null && !(m_handler.equals(handler)))
		{
			m_handler = handler;
			retain();
		}
	}

	/**
	 * 转移当前对象的所有权归属
	 * 
	 * @param handler
	 *            当前对象的所有权归属者
	 */
	void transHadler(C2D_ObjectHandler handler)
	{
		if (handler != null && m_handler != null)
		{
			m_handler = handler;
		}
	}


	/**
	 * 获取当前重磅资源的拥有者
	 * 
	 * @return 当前重磅资源的拥有者
	 */
	public C2D_ObjectHandler getHanlder()
	{
		return m_handler;
	}
	/**
	 * 进行资源卸载
	 * 
	 * @return 是否成功卸载
	 */
	public abstract void onRelease();
}
