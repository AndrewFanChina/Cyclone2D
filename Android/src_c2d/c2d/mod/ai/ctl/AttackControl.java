package c2d.mod.ai.ctl;

import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;
import c2d.mod.map.scroll.C2D_ActMapSprite;

/**
 * ���������� ���ڿ��ƶ�����Ϸ�У���¼���������ĵ��ˣ���������ظ��˺���
 * 
 * @author AndrewFan
 * 
 */
public class AttackControl extends C2D_Object
{
	C2D_Array m_attackList = new C2D_Array();
	/**
	 * ��¼��ĳ��սʿ�Ĺ���
	 * @param w
	 */
	public void remAtk(C2D_ActMapSprite w)
	{
		m_attackList.addElement(w);
	}
	/**
	 * �鿴ĳ��սʿ�Ƿ񱻹�����
	 * @param w սʿ����
	 * @return �Ƿ񱻹�����
	 */
	public boolean checkAtk(C2D_ActMapSprite w)
	{
		if (w == null)
		{
			return false;
		}
		return m_attackList.contains(w);
	}
	/**
	 * ��չ�����¼
	 */
	public void cleanAtkMem()
	{
		if(m_attackList!=null)
		{
			m_attackList.clear();	
		}
	}
	/**
	 * ж��
	 */
	public void onRelease()
	{
		if (m_attackList != null)
		{
			m_attackList.clear();
		}
	}
	public int size()
	{
		if (m_attackList != null)
		{
			return m_attackList.size();
		}
		return 0;
	}
}
