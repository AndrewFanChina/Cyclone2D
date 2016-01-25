package c2d.lang.obj;

import c2d.lang.math.C2D_Order;
import c2d.lang.math.C2D_Stack;
/**
 * 对象池，为了维护所有引用，建立一个对象池。所有对象被创建时，自动注册当前对象池对此引用具有所有权，
 * 同时将引用放入当前对象池。在过程开始，建立好对象池（默认也会存在），期间不断分配对象，最后需要释放
 * 时，销毁当前对象池。对象池中的对象，所有权也可以在运行期间被转移，这样在销毁对象池时，不会影响此对
 * 象。
 * @author AndrewFan
 *
 */
public class C2D_ARPoolStack implements C2D_ObjectHandler, C2D_Order
{
	private C2D_ARPool CurrentPool = null;
	private C2D_Stack Pools = new C2D_Stack();
	private int PoolCount = 0;
	private int m_poolId;
	private static C2D_ARPoolStack ARPoolStack;
	C2D_ARPoolStack()
	{
		m_poolId = PoolCount;
		PoolCount++;
		PushNew();
	}
	public static C2D_ARPoolStack ARStack()
	{
		if(ARPoolStack==null)
		{
			ARPoolStack=new C2D_ARPoolStack();
		}
		return ARPoolStack;
	}
	/**
	 * 当前对象池
	 * 
	 * @return
	 */
	public C2D_ARPool current()
	{
		return CurrentPool;
	}

	/**
	 * 创建新的对象池，设置成当前对象池，并且返回
	 * 
	 * @return 新对象池
	 */
	public C2D_ARPool PushNew()
	{
		C2D_ARPool newPool = new C2D_ARPool();
		Pools.push(CurrentPool);
		CurrentPool = newPool;
		return CurrentPool;
	}

	/**
	 * 弹出并销毁当前的对象池
	 */
	public void PopAndDrain()
	{
		if (CurrentPool != null)
		{
			CurrentPool.drain();
			C2D_ARPool NextPool = (C2D_ARPool) Pools.pop();
			if(NextPool!=null)
			{
				CurrentPool.transAll(NextPool);
			}
			CurrentPool=NextPool;
		}
	}
	/**
	 *销毁所有的对象池
	 */
	public void DestroyAll()
	{
		while(CurrentPool != null)
		{
			PopAndDrain();
		}
		Pools=null;
	}
	public int getOrderValue(int orderType)
	{
		return m_poolId;
	}

}
