package c2d.mod.map.scroll;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.frame.com.view.C2D_SpriteView;
import c2d.lang.math.C2D_SortableArray;
import c2d.lang.math.type.C2D_RectF;
import c2d.mod.C2D_FrameManager;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_ScrollLayer extends C2D_SpriteView
{
	public C2D_ScrollLayer(C2D_FrameManager fm)
	{
		super(fm);
	}
	/** 分层视图中将执行更新逻辑的对象列表，一般是指相机镜头内的可见对象以及始终更新对象*/
	protected C2D_SortableArray m_uplateList = new C2D_SortableArray();
	/** 分层视图中将进行图像绘制的对象列表，一般是指相机镜头内的可见对象 以及始终可见对象*/
	protected C2D_SortableArray m_paintList = new C2D_SortableArray();
	/**
	 * 遍历所有子节点，执行更新
	 * 
	 * @param stage
	 *            当前场景
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		updateSelf(stage);
		updateList(stage, m_uplateList);
	}

	/**
	 * 自动更新子树
	 */
	protected void onAutoUpdateSonList()
	{
		super.autoUpdateList(m_uplateList);
	}

	/**
	 * 绘制子节点
	 * 
	 * @param g
	 */
	protected void paintSonList(C2D_Graphics g)
	{
		paintSonList(g, m_paintList);
	}

	/**
	 * 通知子树列表更新
	 */
	protected void setUpdateSonListPos()
	{
		setUpdateListPos(m_uplateList);
	}

	/**
	 * 刷新相机内对象
	 * 
	 * @param cameraL
	 * @param cameraR
	 * @param cameraT
	 * @param cameraB
	 */
	protected void refreshCameraObjs(float cameraL, float cameraR, float cameraT, float cameraB)
	{
		m_uplateList.clear();
		m_paintList.clear();
		int childLen = getChildCount();
		for (int j = 0; j < childLen; j++)
		{
			C2D_Widget widget = (C2D_Widget) m_nodeList.m_datas[j];
			C2D_RectF rectBody = widget.getBoundingRect();
			float xl;
			float yt;
			if(rectBody!=null)
			{
				xl = rectBody.m_x;
				yt = rectBody.m_y;
				widget.m_inCamera = (xl + rectBody.m_width >= cameraL &&
						   yt + rectBody.m_height >= cameraT &&
						   xl <= cameraR && yt <= cameraB);	
			}
			else
			{
				xl = widget.m_x;
				yt = widget.m_y;
				widget.m_inCamera = (xl >= cameraL &&
						   yt >= cameraT &&
						   xl <= cameraR && yt <= cameraB);	
			}
			if (widget.m_inCamera)
			{
				m_paintList.addElement(widget);
			}
			if(widget.m_alwaysUpdate || widget.m_inCamera)
			{
				m_uplateList.addElement(widget);
			}
		}
	}
	public boolean addChild(C2D_Widget child)
	{
		boolean res = super.addChild(child);
		if(res)
		{
			m_uplateList.addElement(child);
		}
		return res;
	}

	public C2D_Camera getCamera()
	{
		if (m_parentNode == null)
		{
			return null;
		}
		if (!(m_parentNode instanceof C2D_ScrollMap))
		{
			return null;
		}
		return ((C2D_ScrollMap)m_parentNode).getCamera();
	}
}
