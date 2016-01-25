package c2d.mod.ai.ctl;

import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;
import c2d.mod.map.scroll.C2D_ActMapSprite;

/**
 * 攻击控制器 用于控制动作游戏中，记录被攻击过的敌人，避免造成重复伤害。
 * 
 * @author AndrewFan
 * 
 */
public class AttackControl extends C2D_Object
{
	C2D_Array m_attackList = new C2D_Array();
	/**
	 * 记录对某个战士的攻击
	 * @param w
	 */
	public void remAtk(C2D_ActMapSprite w)
	{
		m_attackList.addElement(w);
	}
	/**
	 * 查看某个战士是否被攻击过
	 * @param w 战士对象
	 * @return 是否被攻击过
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
	 * 清空攻击记录
	 */
	public void cleanAtkMem()
	{
		if(m_attackList!=null)
		{
			m_attackList.clear();	
		}
	}
	/**
	 * 卸载
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
