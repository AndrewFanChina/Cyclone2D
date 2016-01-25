package c2d.mod.physic;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.map.scroll.C2D_MapSprite;

public class C2D_PhysicBox extends C2D_Object implements C2D_Physic, C2D_Consts
{
	/** 重量 */
	public int m_weight = 10;
	/** X方向运动速度 */
	public float m_speedX = 0;
	/** Y方向运动速度 */
	public float m_speedY = 0;
	/** 反弹系数----反弹后速度保留百分比 */
	/** 横向向反弹系数 */
	public float m_bounceX = 0.6f;
	/** 顶部反弹系数 */
	public float m_bounceTop = 1.0f;
	/** 底部反弹系数 */
	public float m_bounceBtm = 0.3f;
	/** 水平方向地面摩擦阻尼 */
	public float m_gFric_ground = 4.0f;
	/** 水平方向空气摩擦阻尼 */
	public float m_gFric_Air = 0.1f;
	/** 是否执行地图区域内的反弹 */
	public int m_mapBounce = LEFT | RIGHT | TOP | BOTTOM;
	// /** 朝向,右为1，左为-1 */
	// public int m_face = 1;
	/** 是否处于无重力加速度gY状态 */
	public boolean m_noG = false;
	/** 当前物理盒所绑定的组件 */
	protected C2D_Widget m_boundWidget;
	/** 回收器 */
	protected C2D_Recycler m_recycler;

	/** 地面高度 */
	protected float GroundY()
	{
		return WordSize.m_height;
	}

	public C2D_PhysicBox()
	{
	}

	protected static C2D_PointF m_gCurrent = new C2D_PointF();

	/**
	 * 自由移动
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
		// 垂直运动计算-----------------------------------------
		if (!m_onGround || m_speedY != 0)// Y方向垂直自由落体
		{
			// 原Y速度
			float sYOrg = m_speedY;
			// 变换Y速度
			if (!m_noG)
			{
				m_speedY += m_gCurrent.m_y;
			}
			// 当前Y速度
			float sYCur = m_speedY;
			onDown = sYOrg < 0 && sYCur >= 0;
			if (m_speedY != 0)
			{
				m_boundWidget.m_y += m_speedY;// 变换位置
				// 垂直反弹处理
				float v = m_boundWidget.m_y;
				if (v < 0 && (m_mapBounce & TOP) != 0)// 碰撞空间上限判断
				{
					collodeCeil();
					onDown = true;
				}
				if (v >= WordSize.m_height && (m_mapBounce & BOTTOM) != 0)// 碰撞空间下限判断
				{
					collideGround();
					m_onGround = true;
				}
			}
			setBWLC();
		}
		// 水平运动计算-----------------------------------------
		if (m_speedX != 0)// X方向摩擦阻力
		{
			// 变换速度
			m_speedX += m_gCurrent.m_x;
			if (C2D_Math.abs(m_speedX) <= C2D_Math.abs(m_gCurrent.m_x))
			{
				m_speedX = 0;
			}
			if (m_speedX != 0)
			{
				m_boundWidget.m_x += m_speedX;// 变换位置
				// 水平反弹处理
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
	 * 设置所绑定的控件的布局已经被改动
	 */
	protected void setBWLC()
	{
		if (m_boundWidget != null)
		{
			m_boundWidget.layoutChanged();
		}
	}

	/**
	 * 获取当前的重力加速度
	 * 
	 * @param m_g
	 */
	protected void getCurrentG(C2D_PointF m_g)
	{
		if (m_g == null)
		{
			return;
		}
		// X方向，空气或者地面的摩擦阻力，总是与速度方向相反
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
		// Y方向，重力加速度
		m_g.m_y = M_G;
	}

	/**
	 * 当从上升转变为下降
	 */
	protected void onDown()
	{
	}

	/**
	 * 碰撞天花板后的处理
	 */
	protected void collodeCeil()
	{
		if ((m_mapBounce & TOP) != 0)
		{
			m_speedY *= -1;
			m_speedY *= m_bounceTop;// 反向
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
	 * 碰撞地面后的处理
	 */
	protected void collideGround()
	{
		if ((m_mapBounce & BOTTOM) != 0)
		{
			m_boundWidget.m_y = WordSize.m_height;
			m_speedY *= -m_bounceBtm;// 反向
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
	 * 判断是否xy方向速度均为0
	 * 
	 * @return 是否速度均为0
	 */
	public boolean isSpeedZero()
	{
		return m_speedX == 0 && m_speedY == 0;
	}

	/**
	 * 设置速度为0
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
