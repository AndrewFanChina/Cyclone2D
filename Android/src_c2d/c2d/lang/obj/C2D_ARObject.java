package c2d.lang.obj;

/**
 * C2D�Զ��ͷŶ���Ĭ������Ȩ��Ȼ�����Լ���������������������Զ������
 * @author AndrewFan
 *
 */
public abstract class C2D_ARObject implements C2D_ObjectHandler
{
	/** ��ǰ����Ȩ������ */
	protected C2D_ObjectHandler m_handler;
	/** ��ʹ�ô��� */
	private int m_usedCount = 0;
	
	private static int NUM_C2D_OBJ = 0;
	public static C2D_ARPoolStack DebugPoolStack = new C2D_ARPoolStack();
	public static boolean EnableDebugPool = false;
	/**
	 * ����һ������
	 */
	public void retain()
	{
		m_usedCount++;
	}

	/**
	 * ����������Զ��ͷŶ����
	 */
	public void autoRelease()
	{
		C2D_ARPoolStack.ARStack().current().fillIn(this);
	}
	/**
	 * ������Դ�ͷţ���������Ϊ��Դӵ���ߡ� ��������Ƿ��ͷţ�����ִ��һ�����ô������١�
	 * 
	 * @return �Ƿ�ж��
	 */
	public boolean doRelease()
	{
		boolean res = doRelease(this);
		return res;
	}
	/**
	 * ִ���ͷţ�ֻ�������һ�Ρ�����ɾ����,���������Դӵ���ߡ� ��ȷ��Ϊ��ǰ��Դ��ӵ���ߣ��������ü���Ϊ0�������ձ�ִ��ж�ء�
	 * ��������Ƿ��ͷţ�����ִ��һ�����ô������١�
	 * 
	 * @param handler
	 *            �������Դӵ����
	 * @return �Ƿ�ж��
	 */
	public final boolean doRelease(C2D_ObjectHandler handler)
	{
		if (m_usedCount > 0)
		{
			m_usedCount--;
		}
		if(m_handler!=null)
		{
			//������ü���Ϊ0����������Ǻ���Ȩ�ߣ�������Ȩ�����Լ���������ִ���ͷ�
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
			//������������������ã�����ķǶ������Ȩ���Ǻϣ���Ҫת����Ȩ�߸������
			else
			{
				if (m_handler.equals(handler) && !(m_handler instanceof C2D_ARPool))
				{
					// ԭ�еķǶ�����еĶ�����������Ѿ����ж�أ�
					// ��ǰ���󻹲���ж�أ�ֻ�ܽ���ǰ���������Զ������
					autoRelease();
				}
			}
		}
		return false;
	}
	/**
	 * ���Լ���Ϊ�Լ���������
	 * 
	 * @return �Լ�
	 */
	public C2D_ARObject handleSelf()
	{
		this.transHadler(this);
		return this;
	}

	/**
	 * �������ü�����ͬʱ��ת�Ƶ�ǰ���������Ȩ����
	 * 
	 * @param handler
	 *            ��ǰ���������Ȩ������
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
	 * ת�Ƶ�ǰ���������Ȩ����
	 * 
	 * @param handler
	 *            ��ǰ���������Ȩ������
	 */
	void transHadler(C2D_ObjectHandler handler)
	{
		if (handler != null && m_handler != null)
		{
			m_handler = handler;
		}
	}


	/**
	 * ��ȡ��ǰ�ذ���Դ��ӵ����
	 * 
	 * @return ��ǰ�ذ���Դ��ӵ����
	 */
	public C2D_ObjectHandler getHanlder()
	{
		return m_handler;
	}
	/**
	 * ������Դж��
	 * 
	 * @return �Ƿ�ɹ�ж��
	 */
	public abstract void onRelease();
}
