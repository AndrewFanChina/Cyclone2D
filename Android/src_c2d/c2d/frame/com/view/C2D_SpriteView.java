package c2d.frame.com.view;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_ActionEnd;
import c2d.mod.C2D_FrameManager;
import c2d.mod.map.scroll.C2D_SpritesStore;
import c2d.mod.sprite.C2D_Sprite;

public class C2D_SpriteView extends C2D_View
{
	protected C2D_SpritesStore m_spritesStore;
	protected C2D_FrameManager m_frameManager;
	protected int m_duration = 45;

	public C2D_SpriteView(C2D_FrameManager fm)
	{
		m_frameManager = fm;
	}

	/**
	 * 获得默认的播放延时
	 * 
	 * @return 默认的播放延时
	 */
	public int getDuration()
	{
		return m_duration;
	}

	/**
	 * 设置默认的播放延时
	 * 
	 * @param duration
	 *            默认的播放延时
	 */
	public void setDuration(int duration)
	{
		this.m_duration = duration;
	}

	/**
	 * 默认的播放完毕事件，播放完成之后将其隐藏，从视图移除， 从store中的当前栈转入暂存栈
	 */
	protected C2D_Event_ActionEnd m_EventActionEndDef = new C2D_Event_ActionEnd()
	{
		protected boolean doEvent(C2D_Widget sprite)
		{
			if (sprite == null || !(sprite instanceof C2D_Sprite))
			{
				return false;
			}
			recycleSprite((C2D_Sprite) sprite);
			return false;
		}
	};

	/**
	 * 回收单元
	 * 
	 * @param widget
	 */
	public void recycleSprite(C2D_Sprite sp)
	{
		if (m_spritesStore != null)
		{
			m_spritesStore.recycleSprite(sp);
		}
	}

	/**
	 * 在指定的坐标x,y处，以深度z，播放一个精灵动画。 精灵动画的文件夹ID是folderID，精灵ID是spriteID，
	 * 开始动作是0，播放延时是默认数值 播放完毕执行默认事件（隐藏，从树状结构移除，在store中转入暂存）。
	 * 
	 * @param depend
	 *            依赖的对象
	 * @param x
	 *            指定X坐标
	 * @param y
	 *            指定Y坐标
	 * @param z
	 *            指定Z坐标
	 * @param folderID
	 *            文件夹ID
	 * @param spriteID
	 *            精灵ID
	 * @return 播放的精灵
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID)
	{
		return playSprite(depend, x, y, z, folderID, spriteID, 0, m_duration);
	}

	/**
	 * 在指定的坐标x,y处，以深度z，播放一个精灵动画。 精灵动画的文件夹ID是folderID，精灵ID是spriteID，
	 * 开始动作是actionID，播放延时是默认数值 播放完毕执行默认事件（隐藏，从树状结构移除，在store中转入暂存）。
	 * 
	 * @param depend
	 *            依赖的对象
	 * @param x
	 *            指定X坐标
	 * @param y
	 *            指定Y坐标
	 * @param z
	 *            指定Z坐标
	 * @param folderID
	 *            文件夹ID
	 * @param spriteID
	 *            精灵ID
	 * @param actionID
	 *            精灵动作ID
	 * @return 播放的精灵
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID, int actionID)
	{
		return playSprite(depend, x, y, z, folderID, spriteID, actionID, m_duration);
	}

	/**
	 * 在指定的坐标x,y处，以深度z，播放一个精灵动画。 精灵动画的文件夹ID是folderID，精灵ID是spriteID，
	 * 开始动作是actionID，播放延时是duration。 播放完毕执行事件event。
	 * 
	 * @param depend
	 *            依赖的对象
	 * @param x
	 *            指定X坐标
	 * @param y
	 *            指定Y坐标
	 * @param z
	 *            指定Z坐标
	 * @param folderID
	 *            文件夹ID
	 * @param spriteID
	 *            精灵ID
	 * @param actionID
	 *            精灵动作ID
	 * @param duration
	 *            动画播放延时（毫秒）
	 * @return 播放的精灵
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID, int actionID, int duration)
	{
		if (m_frameManager == null)
		{
			return null;
		}
		if (m_spritesStore == null)
		{
			m_spritesStore = new C2D_SpritesStore();
		}
		C2D_Sprite sprite = (C2D_Sprite) m_spritesStore.recover(spriteID);
		if (sprite == null)
		{
			sprite = onCreateSprite(m_frameManager, folderID, spriteID, depend);
			if (sprite != null)
			{
				m_spritesStore.addSprite(sprite);
			}
		}
		if (sprite != null)
		{
			sprite.clearEvents();
			sprite.clearMotions();
			sprite.setAutoPlay(C2D_Sprite.AUTOPLAY_FRAME);
			sprite.setDuration(duration);
			sprite.setAction(actionID);
			sprite.setVisible(true);
			sprite.setPosTo(x, y);
			sprite.setFrame(0);
			this.addChild(sprite, z);
			sprite.init();
		}
		return sprite;
	}

	/**
	 * 设置精灵仓储器
	 * 
	 * @param spritesStore
	 */
	public void setSpritesStore(C2D_SpritesStore spritesStore)
	{
		if (m_spritesStore != null)
		{
			m_spritesStore.doRelease(this);
			m_spritesStore = null;
		}
		m_spritesStore = spritesStore;
	}

	/**
	 * 默认的构建sprite方式，如果你需要添加的是扩展的精灵对象，可以覆盖这个方法
	 * 
	 * @param fm
	 *            C2D框架管理器
	 * @param folderID
	 *            文件夹ID
	 * @param spriteID
	 *            精灵ID
	 * @param depend
	 *            依赖的对象
	 * @return 构建好的精灵对象
	 */
	protected C2D_Sprite onCreateSprite(C2D_FrameManager fm, short folderID, short spriteID, C2D_Sprite depend)
	{
		C2D_Sprite sprite = null;
		sprite = new C2D_Sprite(fm, folderID, spriteID);
		return sprite;
	}

	/**
	 * 移除所有正在运行的精灵，全部存储和隐藏起来
	 */
	public void storeAll()
	{
		int size = getChildCount();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (son != null && son instanceof C2D_Sprite)
			{
				C2D_Sprite sp = (C2D_Sprite) son;
				recycleSprite(sp);
				i--;
			}
		}
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		if (m_spritesStore != null)
		{
			m_spritesStore.doRelease(this);
			m_spritesStore = null;
		}
		m_frameManager = null;
	}
}
