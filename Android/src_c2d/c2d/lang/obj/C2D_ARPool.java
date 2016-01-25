package c2d.lang.obj;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;
import c2d.lang.util.debug.C2D_Debug;

/**
 * ����أ�Ϊ��ά���������ã�����һ������ء����ж��󱻴���ʱ���Զ�ע�ᵱǰ����ضԴ����þ�������Ȩ��
 * ͬʱ�����÷��뵱ǰ����ء��ڹ��̿�ʼ�������ö���أ�Ĭ��Ҳ����ڣ����ڼ䲻�Ϸ�����������Ҫ�ͷ�
 * ʱ�����ٵ�ǰ����ء�������еĶ�������ȨҲ�����������ڼ䱻ת�ƣ����������ٶ����ʱ������Ӱ��˶� ��
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
	 * ��Ӷ��󵽵�ǰ����أ�ͬʱת������Ȩ����ǰ�����
	 * 
	 * @param obj
	 *            ����ӵĶ���
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
	 * ������ӵ�ǰ������Ƴ���ϵͳ�������û�������á�
	 * 
	 * @param obj
	 *            Ҫ�Ƴ��Ķ���
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
	 * ж����Щ���ü���Ϊ0�Ķ��󣬼�����Ϊ0�Ķ��󽫱��������棬���ҽ����������½����������С�
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
					if (!this.equals(obj.m_handler))// Խ��ʧȥ����Ȩ�Ķ���
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
	 * ��õ�ǰ�����ռ�õĴ�С
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
	 * ����ǰ������У�����Ȩ���ڵ�ǰ����صĶ���ת�Ƶ�ָ���Ķ���أ�Խ����Щ��ȥ����Ȩ�Ķ��� ת��֮�󣬵�ǰ����ؽ������
	 * 
	 * @param destPool
	 *            ת��Ŀ������
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
				if (objI == null || !this.equals(objI.m_handler))// Խ��ʧȥ����Ȩ�Ķ���
				{
					continue;
				}
				destPool.fillIn(objI);
			}
			m_objArray.clear();

		}
	}

}
