package c2d.lang.obj;

import c2d.lang.math.C2D_Array;

/**
 * C2D����Ĭ������Ȩ���Լ���ֻ���Լ������ͷ��Լ���
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Object implements C2D_ObjectHandler
{
	private static int NUM_C2D_OBJ = 0;
	public static boolean EnableDebugPool = false;
	public static C2D_Array DebugArray = new C2D_Array();
	/** ��ǰ����Ȩ������ */
	protected C2D_ObjectHandler m_handler;
	/** �����߶Ե�ǰ�����Ƿ�ʹ��ǿ���ã�Ĭ�������ã�������Ȩ��ת�ƣ����ü�ǿ*/
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
	 * ������Դж��
	 * 
	 * @return �Ƿ�ɹ�ж��
	 */
	public abstract void onRelease();

	/**
	 * ��ȡ��ǰ�������
	 * 
	 * @return �������
	 */
	public static int GetNumC2D_OBJ()
	{
		return NUM_C2D_OBJ;
	}

	/**
	 * ִ���ͷţ���������Ϊ��ǰ��Դӵ���߽����ͷ�
	 * 
	 * @return �Ƿ�ж��
	 */
	public final boolean doRelease()
	{
		return doRelease(this);
	}

	/**
	 * ִ���ͷţ���֤�ǵ�ǰ��Դӵ���ߵ�����£����ߵ�ǰ�����������õ�����£���������ͷ�
	 * @param handler
	 *            �������Դӵ����
	 * @return �Ƿ�ж��
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
	 * ת�Ƶ�ǰ���������Ȩ������ͬʱ��ǿ����
	 * 
	 * @param handler
	 *            ��ǰ���������Ȩ������
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
	 * �������ó�Ա������ɳ�Ա�����Ҳ��������ó�Ա��ͬ������ж��
	 * @param oldMember �ɳ�Ա
	 * @param newMember �³�Ա
	 */
	public void resetMemer(C2D_Object oldMember,C2D_Object newMember)
	{
		if(oldMember!=null &&!oldMember.equals(newMember))
		{
			oldMember.doRelease(this);
		}
	}
	/**
	 * ������Ϊ��Ȩ�ߣ�ж�س�Ա����
	 * @param member ��Ա����
	 */
	public void releaseMemer(C2D_Object member)
	{
		if(member!=null)
		{
			member.doRelease(this);
		}
	}
}
