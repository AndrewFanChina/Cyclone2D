package c2d.lang.obj;

import c2d.lang.math.C2D_Order;
import c2d.lang.math.C2D_Stack;
/**
 * ����أ�Ϊ��ά���������ã�����һ������ء����ж��󱻴���ʱ���Զ�ע�ᵱǰ����ضԴ����þ�������Ȩ��
 * ͬʱ�����÷��뵱ǰ����ء��ڹ��̿�ʼ�������ö���أ�Ĭ��Ҳ����ڣ����ڼ䲻�Ϸ�����������Ҫ�ͷ�
 * ʱ�����ٵ�ǰ����ء�������еĶ�������ȨҲ�����������ڼ䱻ת�ƣ����������ٶ����ʱ������Ӱ��˶�
 * ��
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
	 * ��ǰ�����
	 * 
	 * @return
	 */
	public C2D_ARPool current()
	{
		return CurrentPool;
	}

	/**
	 * �����µĶ���أ����óɵ�ǰ����أ����ҷ���
	 * 
	 * @return �¶����
	 */
	public C2D_ARPool PushNew()
	{
		C2D_ARPool newPool = new C2D_ARPool();
		Pools.push(CurrentPool);
		CurrentPool = newPool;
		return CurrentPool;
	}

	/**
	 * ���������ٵ�ǰ�Ķ����
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
	 *�������еĶ����
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
