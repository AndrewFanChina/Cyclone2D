package c2d.mod.physic;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.map.scroll.C2D_MapSprite;

public class C2D_PhysicBox extends C2D_Object implements C2D_Physic, C2D_Consts
{
	/** ���� */
	public int m_weight = 10;
	/** X�����˶��ٶ� */
	public float m_speedX = 0;
	/** Y�����˶��ٶ� */
	public float m_speedY = 0;
	/** ����ϵ��----�������ٶȱ����ٷֱ� */
	/** �����򷴵�ϵ�� */
	public float m_bounceX = 0.6f;
	/** ��������ϵ�� */
	public float m_bounceTop = 1.0f;
	/** �ײ�����ϵ�� */
	public float m_bounceBtm = 0.3f;
	/** ˮƽ�������Ħ������ */
	public float m_gFric_ground = 4.0f;
	/** ˮƽ�������Ħ������ */
	public float m_gFric_Air = 0.1f;
	/** �Ƿ�ִ�е�ͼ�����ڵķ��� */
	public int m_mapBounce = LEFT | RIGHT | TOP | BOTTOM;
	// /** ����,��Ϊ1����Ϊ-1 */
	// public int m_face = 1;
	/** �Ƿ������������ٶ�gY״̬ */
	public boolean m_noG = false;
	/** ��ǰ��������󶨵���� */
	protected C2D_Widget m_boundWidget;
	/** ������ */
	protected C2D_Recycler m_recycler;

	/** ����߶� */
	protected float GroundY()
	{
		return WordSize.m_height;
	}

	public C2D_PhysicBox()
	{
	}

	protected static C2D_PointF m_gCurrent = new C2D_PointF();

	/**
	 * �����ƶ�
	 */
	public void autoMotion()
	{
		if (m_boundWidget == null)
		{
			return;
		}
		m_onGround = m_boundWidget.m_y == WordSize.m_height && m_speedY == 0;
		getCurrentG(m_gCurrent);
		boolean onDown = false;
		// ��ֱ�˶�����-----------------------------------------
		if (!m_onGround || m_speedY != 0)// Y����ֱ��������
		{
			// ԭY�ٶ�
			float sYOrg = m_speedY;
			// �任Y�ٶ�
			if (!m_noG)
			{
				m_speedY += m_gCurrent.m_y;
			}
			// ��ǰY�ٶ�
			float sYCur = m_speedY;
			onDown = sYOrg < 0 && sYCur >= 0;
			if (m_speedY != 0)
			{
				m_boundWidget.m_y += m_speedY;// �任λ��
				// ��ֱ��������
				float v = m_boundWidget.m_y;
				if (v < 0 && (m_mapBounce & TOP) != 0)// ��ײ�ռ������ж�
				{
					collodeCeil();
					onDown = true;
				}
				if (v >= WordSize.m_height && (m_mapBounce & BOTTOM) != 0)// ��ײ�ռ������ж�
				{
					collideGround();
					m_onGround = true;
				}
			}
			setBWLC();
		}
		// ˮƽ�˶�����-----------------------------------------
		if (m_speedX != 0)// X����Ħ������
		{
			// �任�ٶ�
			m_speedX += m_gCurrent.m_x;
			if (C2D_Math.abs(m_speedX) <= C2D_Math.abs(m_gCurrent.m_x))
			{
				m_speedX = 0;
			}
			if (m_speedX != 0)
			{
				m_boundWidget.m_x += m_speedX;// �任λ��
				// ˮƽ��������
				if ((m_mapBounce & LEFT) != 0 || (m_mapBounce & RIGHT) != 0)
				{
					float value = m_boundWidget.m_x;
					int l = 0;
					float r = WordSize.m_width;
					if (m_boundWidget instanceof C2D_MapSprite)
					{
						C2D_MapSprite mw = (C2D_MapSprite) m_boundWidget;
						l = mw.getLeftAdge();
						r = mw.getRightAdge();
					}
					if (value < l && (m_mapBounce & LEFT) != 0)
					{
						m_boundWidget.m_x = l;
						m_speedX *= -m_bounceX;
					}
					if (value > r && (m_mapBounce & RIGHT) != 0)
					{
						m_boundWidget.m_x = r;
						m_speedX *= -m_bounceX;
					}
				}
			}
			setBWLC();
		}
		if (onDown)
		{
			onDown();
		}
		if (m_recycler != null)
		{
			m_recycler.checkRecycle(m_boundWidget);
		}
	}

	/**
	 * �������󶨵Ŀؼ��Ĳ����Ѿ����Ķ�
	 */
	protected void setBWLC()
	{
		if (m_boundWidget != null)
		{
			m_boundWidget.layoutChanged();
		}
	}

	/**
	 * ��ȡ��ǰ���������ٶ�
	 * 
	 * @param m_g
	 */
	protected void getCurrentG(C2D_PointF m_g)
	{
		if (m_g == null)
		{
			return;
		}
		// X���򣬿������ߵ����Ħ���������������ٶȷ����෴
		if (m_speedX != 0)
		{
			if (atGround())
			{
				m_g.m_x = m_gFric_ground;
			}
			else
			{
				m_g.m_x = m_gFric_Air;
			}
			if (m_speedX > 0)
			{
				m_g.m_x *= -1;
			}
		}
		else
		{
			m_g.m_x = 0;
		}
		// Y�����������ٶ�
		m_g.m_y = M_G;
	}

	/**
	 * ��������ת��Ϊ�½�
	 */
	protected void onDown()
	{
	}

	/**
	 * ��ײ�컨���Ĵ���
	 */
	protected void collodeCeil()
	{
		if ((m_mapBounce & TOP) != 0)
		{
			m_speedY *= -1;
			m_speedY *= m_bounceTop;// ����
		}
		else
		{
			m_speedY = 0;
		}
	}

	protected boolean m_onGround = false;

	public boolean atGround()
	{
		return m_onGround;
	}

	/**
	 * ��ײ�����Ĵ���
	 */
	protected void collideGround()
	{
		if ((m_mapBounce & BOTTOM) != 0)
		{
			m_boundWidget.m_y = WordSize.m_height;
			m_speedY *= -m_bounceBtm;// ����
			if (C2D_Math.abs(m_speedY) < M_G)
			{
				m_speedY = 0;
			}
		}
		else
		{
			m_boundWidget.m_y = WordSize.m_height;
			m_speedY = 0;
		}
	}

	public void onRelease()
	{
		m_boundWidget = null;
	}

	public void setBoundWidget(C2D_Widget widget)
	{
		m_boundWidget = widget;
	}

	public void setRecycler(C2D_Recycler recycler)
	{
		m_recycler = recycler;
	}

	public void receiveStrength(int strengthX, int strengthY)
	{
		if (m_weight != 0)
		{
			m_speedX += strengthX / m_weight;
			m_speedY += strengthY / m_weight;
		}
	}

	/**
	 * �ж��Ƿ�xy�����ٶȾ�Ϊ0
	 * 
	 * @return �Ƿ��ٶȾ�Ϊ0
	 */
	public boolean isSpeedZero()
	{
		return m_speedX == 0 && m_speedY == 0;
	}

	/**
	 * �����ٶ�Ϊ0
	 */
	public void setSpeedZero()
	{
		m_speedX = 0;
		m_speedY = 0;
	}

	public boolean isSpeedXZero()
	{
		return m_speedX == 0;
	}

	public boolean isSpeedYZero()
	{
		return m_speedY == 0;
	}
}
