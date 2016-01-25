package c2d.mod.map.scroll;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_RegionI;
import c2d.mod.C2D_FrameManager;
import c2d.mod.physic.C2D_Physic;
import c2d.mod.physic.C2D_PhysicBox;
import c2d.mod.physic.C2D_PhysicBoxCreator;
import c2d.mod.sprite.C2D_Sprite;

public class C2D_MapSprite extends C2D_Sprite implements C2D_Physic, C2D_PhysicBoxCreator
{
	/** 相对相机镜头左上角的身体区域框，此区域对应到每个动画的帧，加入计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_bodyRect2Cam;
	/** 相对相机镜头左上角的攻击区域框，此区域对应到每个动画的帧，加入计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_atkRect2Cam;
	/** 相对世界的身体区域框，此区域对应到每个动画的帧，加入计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_bodyRect2Wrd;
	/** 相对世界的攻击区域框，此区域对应到每个动画的帧，加入计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_atkRect2Wrd;
	/**
	 * 内部的框缓冲，依次是： 相对自身的身体和攻击区域框 、 相对相机镜头左上角的身体和攻击区域框、 相对世界的身体和攻击区域框
	 */
	protected C2D_RegionI m_innerRect[];
	/** 朝向,右为1，左为-1 */
	public int m_face = 1;
	/** 所位于的地图精灵管理器 **/
	protected C2D_SpritesStore m_spritesMgr;
	protected boolean m_recycled = false;

	public C2D_MapSprite(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		m_needCheckLogicBox = true;
		this.m_C2DM = c2dManager;
		this.m_folderID = folderID;
		this.m_spriteID = spriteID;
		m_innerRect = new C2D_RegionI[4];
		for (int i = 0; i < m_innerRect.length; i++)
		{
			m_innerRect[i] = new C2D_RegionI();
		}
		initRes();
	}

	public C2D_MapSprite(C2D_FrameManager c2dManager,int antetypeFolderID, int anteTypeID)
	{
		m_needCheckLogicBox = true;
		this.m_C2DM = c2dManager;
		if (anteTypeID >= 0)
		{
			m_anteType = c2dManager.m_AniM.getAnteType(antetypeFolderID,anteTypeID);
		}
		if (m_anteType != null)
		{
			m_spriteID = m_anteType.actotorID;
		}
		m_innerRect = new C2D_RegionI[4];
		for (int i = 0; i < m_innerRect.length; i++)
		{
			m_innerRect[i] = new C2D_RegionI();
		}
		initRes();
	}

	public C2D_Sprite init()
	{
		super.init();
		m_recycled = false;
		setVisible(true);
		return this;
	}

	public void setManager(C2D_SpritesStore mgr)
	{
		m_spritesMgr = mgr;
	}

	/**
	 * 更新相对舞台的显示区域和自身占用区域
	 */
	protected void refreshRegions()
	{
		// 身体区域框
		if (m_bodyRect2Self != null)
		{
			m_bodyRect2Wrd = m_innerRect[0];
			m_bodyRect2Cam = m_innerRect[1];
			m_bodyRect2Wrd.setValue(m_bodyRect2Self);
			if (m_flipX)
			{
				int w = m_bodyRect2Wrd.m_r - m_bodyRect2Wrd.m_l;
				m_bodyRect2Wrd.m_l = -m_bodyRect2Wrd.m_r;
				m_bodyRect2Wrd.m_r = m_bodyRect2Wrd.m_l + w;
			}
			m_bodyRect2Cam.setValue(m_bodyRect2Wrd);
			m_bodyRect2Wrd.addOffset((int) m_x, (int) m_y);
			m_bodyRect2Cam.addOffset((int) m_xToTop, (int) m_yToTop);
		}
		else
		{
			m_bodyRect2Wrd = null;
			m_bodyRect2Cam = null;
		}
		// 攻击区域框
		if (m_atkRect2Self != null)
		{
			m_atkRect2Wrd = m_innerRect[2];
			m_atkRect2Cam = m_innerRect[3];
			m_atkRect2Wrd.setValue(m_atkRect2Self);
			if (m_flipX)
			{
				int w = m_atkRect2Wrd.m_r - m_atkRect2Wrd.m_l;
				m_atkRect2Wrd.m_l = -m_atkRect2Wrd.m_r;
				m_atkRect2Wrd.m_r = m_atkRect2Wrd.m_l + w;
			}
			m_atkRect2Cam.setValue(m_atkRect2Wrd);
			m_atkRect2Wrd.addOffset((int) m_x, (int) m_y);
			m_atkRect2Cam.addOffset((int) m_xToTop, (int) m_yToTop);
		}
		else
		{
			m_atkRect2Wrd = null;
			m_atkRect2Cam = null;
		}
	}

	/**
	 * 清除攻击框。有时为了限制攻击次数
	 */
	public void clearAtkRect()
	{
		m_atkRect2Cam = null;
		m_atkRect2Wrd = null;
	}

	/**
	 * 获取人物左边边界位于父容器坐标
	 * 
	 * @return 左边边界
	 */
	public float getBodyLeft()
	{
		if (m_bodyRect2Self != null)
		{
			if (m_face > 0)
			{
				return m_x + m_bodyRect2Self.m_l;
			}
			else
			{
				return m_x - m_bodyRect2Self.m_r;
			}
		}
		return m_x;
	}

	/**
	 * 获取人物右边边界位于父容器坐标
	 * 
	 * @return 右边边界
	 */
	public float getBodyRight()
	{
		if (m_bodyRect2Self != null)
		{
			if (m_face > 0)
			{
				return m_x + m_bodyRect2Self.m_r;
			}
			else
			{
				return m_x - m_bodyRect2Self.m_l;
			}
		}
		return m_x;
	}

	/**
	 * 获取左边人物可以到达的边界
	 * 
	 * @return 左边边界
	 */
	public int getLeftAdge()
	{
		if (m_bodyRect2Self != null)
		{
			if (m_face > 0)
			{
				return -m_bodyRect2Self.m_l;
			}
			else
			{
				return m_bodyRect2Self.m_r;
			}
		}
		return 0;
	}

	/**
	 * 获取左边人物可以到达的边界
	 * 
	 * @return 左边边界
	 */
	public float getRightAdge()
	{
		if (m_bodyRect2Self != null)
		{
			if (m_face > 0)
			{
				return WordSize.m_width - m_bodyRect2Self.m_r;
			}
			else
			{
				return WordSize.m_width + m_bodyRect2Self.m_l;
			}
		}
		return WordSize.m_width;
	}

	public int getFace()
	{
		return m_face;
	}

	public void setFace(int face)
	{
		if (face != 1 && face != -1)
		{
			return;
		}
		m_face = face;
		setFlipX(m_face > 0);
	}

	public void setDifFace(int face)
	{
		if (m_face == face)
		{
			return;
		}
		setFace(face);
	}

	public C2D_PhysicBox onCreatePhysicBox(C2D_Widget widget)
	{
		if (m_physicBox != null)
		{
			return m_physicBox;
		}
		return new C2D_PhysicBox();
	}

	public void recycleSelf()
	{
		if (m_recycled)
		{
			return;
		}
		// 进行回收
		if (m_spritesMgr != null)
		{
			m_spritesMgr.recycleSprite(this);
		}
		else
		{
			removeFromTree();
		}
		m_recycled = true;
	}

	public C2D_Camera getCamera()
	{
		if (m_parentNode == null)
		{
			return null;
		}
		if (!(m_parentNode instanceof C2D_ScrollLayer))
		{
			return null;
		}
		return ((C2D_ScrollLayer) m_parentNode).getCamera();
	}

}
