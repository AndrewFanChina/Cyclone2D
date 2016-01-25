package c2d.lang.obj;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;
import c2d.lang.util.debug.C2D_Debug;

/**
 * 对象池，为了维护所有引用，建立一个对象池。所有对象被创建时，自动注册当前对象池对此引用具有所有权，
 * 同时将引用放入当前对象池。在过程开始，建立好对象池（默认也会存在），期间不断分配对象，最后需要释放
 * 时，销毁当前对象池。对象池中的对象，所有权也可以在运行期间被转移，这样在销毁对象池时，不会影响此对 象。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ARPool implements C2D_ObjectHandler, C2D_Order
{
	private static int PoolCount = 0;
	private int m_poolId;
	private C2D_Array m_objArray;

	C2D_ARPool()
	{
		m_poolId = PoolCount;
		PoolCount++;
	}

	/**
	 * 添加对象到当前对象池，同时转移所有权到当前对象池
	 * 
	 * @param obj
	 *            被添加的对象
	 */
	void fillIn(C2D_ARObject obj)
	{
		if (obj != null)
		{
			if (m_objArray == null)
			{
				m_objArray = new C2D_Array();
			}
			if (!obj.getHanlder().equals(this))
			{
				m_objArray.addElement(obj);
				obj.transHadler(this);
			}
		}
	}

	/**
	 * 将对象从当前对象池移除【系统方法，用户无需调用】
	 * 
	 * @param obj
	 *            要移除的对象
	 */
	void pullOut(C2D_ARObject obj)
	{
		if (obj != null)
		{
			if (m_objArray != null)
			{
				m_objArray.remove(obj);
			}
		}
	}

	/**
	 * 卸载那些引用计数为0的对象，计数不为0的对象将被继续保存，并且紧密排列在新建立的数组中。
	 */
	void drain()
	{
		C2D_Debug.logC2D("## begin drain arpool");
		int size = size();
		if (size > 0)
		{
			C2D_Array bakArray = new C2D_Array();
			for (int i = 0; i < size; i++)
			{
				C2D_ARObject obj = (C2D_ARObject) m_objArray.elementAt(i);
				if (obj != null)
				{
					if (!this.equals(obj.m_handler))// 越过失去控制权的对象
					{
						continue;
					}
					if (!obj.doRelease(this))
					{
						bakArray.addElement(obj);
					}
				}
			}
			m_objArray.clear();
			m_objArray = bakArray;
		}
		C2D_Debug.logC2D("## endof drain arpool,left[" + size() + "]");
		if (C2D_ARObject.EnableDebugPool)
		{
			C2D_ARObject.DebugPoolStack.current().show();
		}
		C2D_Debug.logC2D("## NUM C2D OBJ:" + C2D_Object.GetNumC2D_OBJ());
	}

	/**
	 * 获得当前对象池占用的大小
	 * 
	 * @return
	 */
	public int size()
	{
		if (m_objArray == null)
		{
			return 0;
		}
		return m_objArray.size();
	}

	public int getOrderValue(int orderType)
	{
		return m_poolId;
	}

	public void show()
	{
		C2D_Debug.logC2D("==> Debug Pool ==>");
		int size = size();
		for (int i = 0; i < size; i++)
		{
			C2D_Object obj = (C2D_Object) m_objArray.elementAt(i);
			C2D_Debug.logC2D("" + obj);
		}
		C2D_Debug.logC2D("==< Debug Pool <==");
	}

	/**
	 * 将当前对象池中，控制权属于当前对象池的对象转移到指定的对象池，越过那些逝去控制权的对象。 转移之后，当前对象池将被清空
	 * 
	 * @param destPool
	 *            转移目标对象池
	 */
	void transAll(C2D_ARPool destPool)
	{
		if (m_objArray == null)
		{
			return;
		}
		if (destPool != null)
		{
			int size = size();
			for (int i = 0; i < size; i++)
			{
				C2D_ARObject objI = (C2D_ARObject) m_objArray.elementAt(i);
				if (objI == null || !this.equals(objI.m_handler))// 越过失去控制权的对象
				{
					continue;
				}
				destPool.fillIn(objI);
			}
			m_objArray.clear();

		}
	}

}
