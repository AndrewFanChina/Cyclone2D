package c2d.mod.map.scroll;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_RegionI;
import c2d.mod.C2D_FrameManager;
import c2d.mod.ai.ctl.AttackControl;
import c2d.mod.physic.C2D_Physic;
import c2d.mod.sprite.C2D_Sprite;
import c2d.plat.gfx.C2D_Graphics;

public abstract class C2D_ActMapSprite extends C2D_MapSprite implements C2D_Physic
{
	/** 寄生对象列表 */
	protected C2D_Array m_parasticList;
	/** 依赖对象 */
	protected C2D_ActMapSprite m_dependSprite;
	/* 攻击控制器 */
	protected AttackControl m_attackControl = new AttackControl();

	public C2D_ActMapSprite(C2D_FrameManager c2dManagerT, short folderID, short spriteID)
	{
		super(c2dManagerT, folderID,spriteID);
	}

	public C2D_ActMapSprite(C2D_FrameManager c2dManagerT,int antetypeFolderID, int anteTypeID)
	{
		super(c2dManagerT, antetypeFolderID,anteTypeID);
	}

	public C2D_Sprite init()
	{
		super.init();
		bindPhysicBox(this);
		m_alwaysUpdate = true;
		return this;
	}

	/***
	 * 攻击敌人逻辑，默认使用物理攻击
	 * 
	 * @param enemies
	 *            可能被攻击的敌人的集合
	 * @param strengthX
	 *            X方向攻击的力量
	 * @param strengthY
	 *            Y方向攻击的力量
	 * @param attack
	 *            攻击伤害
	 * @return 击中次数
	 */
	public int attack(C2D_Array enemies, int strengthX, int strengthY, int attack)
	{
		return attack(m_atkRect2Wrd, enemies, strengthX, strengthY, attack);
	}

	/***
	 * 攻击敌人逻辑
	 * 
	 * @param atkRegion
	 *            攻击区域
	 * @param enemies
	 *            可能被攻击的敌人的集合
	 * @param strengthX
	 *            X方向攻击的力量
	 * @param strengthY
	 *            Y方向攻击的力量
	 * @param attack
	 *            攻击伤害
	 * @return 击中个数
	 */
	public int attack(C2D_RegionI atkRegion, C2D_Array enemies, int strengthX, int strengthY, int attack)
	{
		if ((enemies == null || atkRegion == null))
		{
			return 0;
		}
		int num = 0;
		int size = enemies.size();
		for (int i = 0; i < size; i++)
		{
			C2D_ActMapSprite mI = (C2D_ActMapSprite) enemies.elementAt(i);
			if (m_attackControl.checkAtk(mI))
			{
				continue;
			}
			int hurt = mI.beAttacked(atkRegion, strengthX * m_face, strengthY, attack);
			if (hurt >= 0)
			{
				onAttackEnemy(mI, hurt);
			}
			if (hurt > 0)
			{
				num++;
				// FIXME miss特效
				m_attackControl.remAtk(mI);
			}
			if (!getVisible())
			{
				break;
			}
		}
		return num;
	}

	/**
	 * 当攻击到敌人时发生
	 * 
	 * @param enemy
	 *            敌人
	 * @param hurt
	 *            造成的伤害
	 */
	protected void onAttackEnemy(C2D_ActMapSprite enemy, int hurt)
	{
	}

	/***
	 * 攻击单个敌人逻辑
	 * 
	 * @param atkRegion
	 *            攻击区域
	 * @param warrior
	 *            可能被攻击的敌人
	 * @param strengthX
	 *            X方向攻击的力量
	 * @param strengthY
	 *            Y方向攻击的力量
	 * @param attack
	 *            攻击伤害
	 * @return 造成的伤害，返回-1表示未击中，否则表示击中后造成的伤害
	 */
	public int attack(C2D_RegionI atkRegion, C2D_ActMapSprite warrior, int strengthX, int strengthY, int attack)
	{
		if (warrior == null)
		{
			return 0;
		}
		if (m_attackControl.checkAtk(warrior))
		{
			return 0;
		}
		int hurt = warrior.beAttacked(atkRegion, strengthX * m_face, strengthY, attack);
		if (hurt >= 0)// 确认位于攻击范围之内,纵向没有加入判断，
		{
			m_attackControl.remAtk(warrior);
		}
		return hurt;
	}

	/**
	 * 被攻击的逻辑
	 * 
	 * @param atkRegion
	 *            攻击区域
	 * @param strengthX
	 *            横向攻击分量，分正负，正为右，负为左
	 * @param strengthY
	 *            纵向攻击分量，分正负，正为下，负为上
	 * @param atk
	 *            攻击力
	 * @return 造成的伤害，返回-1表示未击中，否则表示击中后造成的伤害
	 */
	public int beAttacked(C2D_RegionI atkRegion, int strengthX, int strengthY, int atk)
	{
		int xHit = 0;
		int yHit = 0;
		if (m_bodyRect2Wrd == null)
		{
			return -1;
		}
		if (atkRegion == null)
		{
			return -1;
		}
		C2D_RegionI crossRegion = atkRegion.crossedRegion(m_bodyRect2Wrd);
		if (crossRegion == null)
		{
			return -1;
		}
		// 进行miss计算
		if (isMiss())
		{
			return -1;
		}
		// 受伤计算
		int hurt = onHurt(atk);
		if (hurt > 0)
		{
			int borderX = (m_bodyRect2Wrd.m_r - m_bodyRect2Wrd.m_l) >> 4;
			int borderY = (m_bodyRect2Wrd.m_b - m_bodyRect2Wrd.m_t) >> 4;
			xHit = C2D_Math.getRandom(m_bodyRect2Wrd.m_l + borderX, m_bodyRect2Wrd.m_r - borderX);
			yHit = C2D_Math.getRandom(m_bodyRect2Wrd.m_t + borderY, m_bodyRect2Wrd.m_b - borderY);
			receiveStrength(strengthX, strengthY);
			// 自动转向
			autoTurnBack(strengthX);
			// 显示数值
			onGenHitEffect(xHit - getCamera().getLeft(), yHit - getCamera().getTop(), hurt);
		}
		return hurt;
	}

	/**
	 * 检查是否需要自动转向
	 * 
	 * @param hurtStrngthX
	 *            伤害的发力方向
	 */
	protected void autoTurnBack(int hurtStrngthX)
	{
		if ((hurtStrngthX > 0 && m_face > 0) || (hurtStrngthX < 0 && m_face < 0))
		{
			setFace(-m_face);
		}
	}

	/**
	 * 是否伤害miss
	 * 
	 * @return
	 */
	public boolean isMiss()
	{
		return false;
	}

	/**
	 * 当受到攻击时，产生特效
	 * 
	 * @param xHit
	 *            产生位置X坐标(屏幕坐标)
	 * @param yHit
	 *            产生位置Y坐标(屏幕坐标)
	 * @param hurt
	 *            产生的伤害
	 */
	protected abstract void onGenHitEffect(float xHit, float yHit, int hurt);

	protected abstract int onHurt(int atk);

	/**
	 * 受到x和y方向的作用力
	 * 
	 * @param strengthX
	 *            x方向作用力
	 * @param strengthY
	 *            y方向作用力
	 */
	public void receiveStrength(int strengthX, int strengthY)
	{
		if (m_physicBox != null)
		{
			m_physicBox.receiveStrength(strengthX, strengthY);
		}
	}

	public void onRelease()
	{
		super.onRelease();
		if (m_attackControl != null)
		{
			m_attackControl.doRelease();
		}
		cutDependence();
		cutDependOn();
	}

	public void recycleSelf()
	{
		if (m_recycled)
		{
			return;
		}
		super.recycleSelf();
		cutDependence();
		cutDependOn();
	}

	/**
	 * 设置当前对象的依赖对象，当前对象将被加入依赖目标的寄生列表。 同时与目标对象共享同一个父容器。在设置新的依赖对象之前，会 切断当前存在的依赖关系。
	 * 
	 * @param depend
	 */
	public void dependOn(C2D_ActMapSprite depend)
	{
		cutDependOn();
		if (depend == null)
		{
			return;
		}
		if (depend.m_parasticList == null)
		{
			depend.m_parasticList = new C2D_Array();
		}
		depend.m_parasticList.addElement(this);
		m_dependSprite = depend;
		m_parentNode = m_dependSprite.m_parentNode;
	}

	/**
	 * 中断当前对象被被依赖对象之间的依赖关系
	 */
	public void cutDependOn()
	{
		if (m_dependSprite != null)
		{
			if (m_dependSprite.m_parasticList != null)
			{
				m_dependSprite.m_parasticList.remove(this);
			}
			m_dependSprite = null;
		}
		m_parentNode = null;
	}

	/**
	 * 中断当前对象所有的依赖关系，包括依赖他人和被他人依赖
	 */
	public void cutDependence()
	{
		if (m_parasticList != null)
		{
			int size = m_parasticList.size();
			for (int i = 0; i < size; i++)
			{
				C2D_ActMapSprite eI = (C2D_ActMapSprite) m_parasticList.elementAt(i);
				if (eI != null)
				{
					eI.m_dependSprite = null;
				}
			}
			m_parasticList.clear();
			m_dependSprite = null;
		}
	}

	/**
	 * 中断当前被对象和指定ID的依赖者们之间的依赖关系
	 */
	public void cutDependBy(short id)
	{
		if (m_parasticList != null)
		{
			int size = m_parasticList.size();
			for (int i = 0; i < size; i++)
			{
				C2D_ActMapSprite eI = (C2D_ActMapSprite) m_parasticList.elementAt(i);
				if (eI != null && eI.getSpriteID() == id)
				{
					eI.m_dependSprite = null;
					m_parasticList.removeElementAt(i);
					i--;
					size--;
				}
			}
		}
	}

	/**
	 * 检查当前对象是否被指定的对象所依赖
	 * 
	 * @return 是否包含
	 */
	public boolean dependBy(C2D_ActMapSprite parastic)
	{
		if (m_parasticList == null)
		{
			return false;
		}
		return m_parasticList.contains(parastic);
	}

	protected void onPaint(C2D_Graphics g)
	{
		if (m_parasticList != null)
		{
			int size = m_parasticList.size();
			for (int i = 0; i < size; i++)
			{
				C2D_ActMapSprite eI = (C2D_ActMapSprite) m_parasticList.elementAt(i);
				if (eI != null && !eI.equals(this))
				{
					eI.onPaint(g);
				}
			}
		}
	}

	/**
	 * 刷新坐标，这将把精确坐标的值传递给整数坐标， 并计算相对顶层视图的坐标和更新显示和占用区域。
	 * 一般来说，这个函数默认会在组件相关信息发生变化时自动被调用，无需手动调用。
	 */
	public void refreshPos()
	{
		super.refreshPos();
		// 告知寄生对象，宿主位置已经发生变化
		if (m_parasticList != null)
		{
			int size = m_parasticList.size();
			for (int i = 0; i < size; i++)
			{
				C2D_ActMapSprite eI = (C2D_ActMapSprite) m_parasticList.elementAt(i);
				if (eI != null)
				{
				}
				eI.onHostPosChanged(this, m_x, m_y);
			}
		}
	}

	/**
	 * 当宿主对象位置发生变化
	 * 
	 * @param host
	 *            宿主对象
	 * @param x
	 * @param y
	 */
	protected void onHostPosChanged(C2D_ActMapSprite host, float x, float y)
	{
		setPosTo(x, y);
		refreshPos();
	}

	/**
	 * 判断目标是否在我的正前方
	 * 
	 * @param dest
	 * @return
	 */
	public boolean atMyFront(C2D_ActMapSprite dest)
	{
		if (dest == null || dest.equals(this))
		{
			return false;
		}
		if (dest.getX() < getX())
		{
			return m_face < 0;
		}
		if (dest.getX() > getX())
		{
			return m_face > 0;
		}
		return false;
	}

	/**
	 * 检查是否寄生列表中是否含有某个类型的寄生对象
	 * 
	 * @param spriteID
	 *            寄生对象类型
	 * @return 寄生对象
	 */
	public C2D_ActMapSprite getParastic(short spriteID)
	{
		if (m_parasticList == null)
		{
			return null;
		}
		int size = m_parasticList.size();
		for (int i = 0; i < size; i++)
		{
			C2D_ActMapSprite eI = (C2D_ActMapSprite) m_parasticList.elementAt(i);
			if (eI.getSpriteID() == spriteID)
			{
				return eI;
			}
		}
		return null;
	}
}
