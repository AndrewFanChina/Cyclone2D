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
	/** ���������б� */
	protected C2D_Array m_parasticList;
	/** �������� */
	protected C2D_ActMapSprite m_dependSprite;
	/* ���������� */
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
	 * ���������߼���Ĭ��ʹ��������
	 * 
	 * @param enemies
	 *            ���ܱ������ĵ��˵ļ���
	 * @param strengthX
	 *            X���򹥻�������
	 * @param strengthY
	 *            Y���򹥻�������
	 * @param attack
	 *            �����˺�
	 * @return ���д���
	 */
	public int attack(C2D_Array enemies, int strengthX, int strengthY, int attack)
	{
		return attack(m_atkRect2Wrd, enemies, strengthX, strengthY, attack);
	}

	/***
	 * ���������߼�
	 * 
	 * @param atkRegion
	 *            ��������
	 * @param enemies
	 *            ���ܱ������ĵ��˵ļ���
	 * @param strengthX
	 *            X���򹥻�������
	 * @param strengthY
	 *            Y���򹥻�������
	 * @param attack
	 *            �����˺�
	 * @return ���и���
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
				// FIXME miss��Ч
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
	 * ������������ʱ����
	 * 
	 * @param enemy
	 *            ����
	 * @param hurt
	 *            ��ɵ��˺�
	 */
	protected void onAttackEnemy(C2D_ActMapSprite enemy, int hurt)
	{
	}

	/***
	 * �������������߼�
	 * 
	 * @param atkRegion
	 *            ��������
	 * @param warrior
	 *            ���ܱ������ĵ���
	 * @param strengthX
	 *            X���򹥻�������
	 * @param strengthY
	 *            Y���򹥻�������
	 * @param attack
	 *            �����˺�
	 * @return ��ɵ��˺�������-1��ʾδ���У������ʾ���к���ɵ��˺�
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
		if (hurt >= 0)// ȷ��λ�ڹ�����Χ֮��,����û�м����жϣ�
		{
			m_attackControl.remAtk(warrior);
		}
		return hurt;
	}

	/**
	 * ���������߼�
	 * 
	 * @param atkRegion
	 *            ��������
	 * @param strengthX
	 *            ���򹥻�����������������Ϊ�ң���Ϊ��
	 * @param strengthY
	 *            ���򹥻�����������������Ϊ�£���Ϊ��
	 * @param atk
	 *            ������
	 * @return ��ɵ��˺�������-1��ʾδ���У������ʾ���к���ɵ��˺�
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
		// ����miss����
		if (isMiss())
		{
			return -1;
		}
		// ���˼���
		int hurt = onHurt(atk);
		if (hurt > 0)
		{
			int borderX = (m_bodyRect2Wrd.m_r - m_bodyRect2Wrd.m_l) >> 4;
			int borderY = (m_bodyRect2Wrd.m_b - m_bodyRect2Wrd.m_t) >> 4;
			xHit = C2D_Math.getRandom(m_bodyRect2Wrd.m_l + borderX, m_bodyRect2Wrd.m_r - borderX);
			yHit = C2D_Math.getRandom(m_bodyRect2Wrd.m_t + borderY, m_bodyRect2Wrd.m_b - borderY);
			receiveStrength(strengthX, strengthY);
			// �Զ�ת��
			autoTurnBack(strengthX);
			// ��ʾ��ֵ
			onGenHitEffect(xHit - getCamera().getLeft(), yHit - getCamera().getTop(), hurt);
		}
		return hurt;
	}

	/**
	 * ����Ƿ���Ҫ�Զ�ת��
	 * 
	 * @param hurtStrngthX
	 *            �˺��ķ�������
	 */
	protected void autoTurnBack(int hurtStrngthX)
	{
		if ((hurtStrngthX > 0 && m_face > 0) || (hurtStrngthX < 0 && m_face < 0))
		{
			setFace(-m_face);
		}
	}

	/**
	 * �Ƿ��˺�miss
	 * 
	 * @return
	 */
	public boolean isMiss()
	{
		return false;
	}

	/**
	 * ���ܵ�����ʱ��������Ч
	 * 
	 * @param xHit
	 *            ����λ��X����(��Ļ����)
	 * @param yHit
	 *            ����λ��Y����(��Ļ����)
	 * @param hurt
	 *            �������˺�
	 */
	protected abstract void onGenHitEffect(float xHit, float yHit, int hurt);

	protected abstract int onHurt(int atk);

	/**
	 * �ܵ�x��y�����������
	 * 
	 * @param strengthX
	 *            x����������
	 * @param strengthY
	 *            y����������
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
	 * ���õ�ǰ������������󣬵�ǰ���󽫱���������Ŀ��ļ����б� ͬʱ��Ŀ�������ͬһ�����������������µ���������֮ǰ���� �жϵ�ǰ���ڵ�������ϵ��
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
	 * �жϵ�ǰ���󱻱���������֮���������ϵ
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
	 * �жϵ�ǰ�������е�������ϵ�������������˺ͱ���������
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
	 * �жϵ�ǰ�������ָ��ID����������֮���������ϵ
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
	 * ��鵱ǰ�����Ƿ�ָ���Ķ���������
	 * 
	 * @return �Ƿ����
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
	 * ˢ�����꣬�⽫�Ѿ�ȷ�����ֵ���ݸ��������꣬ ��������Զ�����ͼ������͸�����ʾ��ռ������
	 * һ����˵���������Ĭ�ϻ�����������Ϣ�����仯ʱ�Զ������ã������ֶ����á�
	 */
	public void refreshPos()
	{
		super.refreshPos();
		// ��֪������������λ���Ѿ������仯
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
	 * ����������λ�÷����仯
	 * 
	 * @param host
	 *            ��������
	 * @param x
	 * @param y
	 */
	protected void onHostPosChanged(C2D_ActMapSprite host, float x, float y)
	{
		setPosTo(x, y);
		refreshPos();
	}

	/**
	 * �ж�Ŀ���Ƿ����ҵ���ǰ��
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
	 * ����Ƿ�����б����Ƿ���ĳ�����͵ļ�������
	 * 
	 * @param spriteID
	 *            ������������
	 * @return ��������
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
