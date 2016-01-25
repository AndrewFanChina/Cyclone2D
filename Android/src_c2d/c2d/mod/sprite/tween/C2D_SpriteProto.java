package c2d.mod.sprite.tween;

import java.io.DataInputStream;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_EventPool_ActionEnd;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.math.type.C2D_RegionI;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_FrameManager;
import c2d.mod.prop.C2D_Prop;
import c2d.mod.script.C2D_ScriptExcutor;
import c2d.mod.script.C2D_ScriptFunctionHandler;
import c2d.mod.script.C2D_Thread;
import c2d.mod.sprite.tween.model.C2D_SpriteModel;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;

/**
 * <p>
 * 标题:补间动画精灵类
 * </p>
 * <p>
 * 精灵动画类，管理精灵动画相关的信息。精灵动画管理和绘制的一个基本单元。 有自己的动作、帧、时间等控制信息。你可以提供C2DManager对象，然后指派
 * 相关的ID来创建一个精灵，即可以直接new出来，也可以使用releaseRes卸载 ，需要自己控制内存释放。<br>
 * 注意，锚点对齐方式对精灵无效。精灵由于采用中心对齐，没有固定尺寸不能绘制出焦点 补见精灵动画的内部结构比较多，不过一般可以不用去理会。它的下分是时间轴，时
 * 间轴的下分是时间轴层，时间轴层的下分是关键帧与时间过渡信息，关键帧的下 分是多个贴图切块，每个贴图切块都引用了同一个贴图表中的OpenGL贴图。<br>
 * 另外，它还含有一些其它精灵信息，如可见性、位置信息、属性信息等等。<br>
 * </p>
 * .
 * 
 * @author AndrewFan
 */
public class C2D_SpriteProto extends C2D_Widget implements C2D_ScriptFunctionHandler
{

	/** 精灵数据所存在的C2D管理器. */
	protected C2D_FrameManager m_C2DM = null;

	/** 精灵数据对象. */
	protected C2D_SpriteModel m_spriteData = null;

	/** 精灵原型信息. */
	protected C2D_AnteType m_anteType = null;

	/** 精灵ID. */
	protected short m_spriteID = -1;

	/** 精灵动作ID. */
	protected short m_actionID = -1;

	/** 精灵动画关键帧ID. */
	protected short m_frameID = -1;

	/** NPC ID. */
	protected short m_npcID = -1;

	/** 精灵属性. */
	private C2D_Prop m_property = null;

	/** 动画是否循环播放. */
	private boolean m_animLoop = true;

	/** 播放一帧的时长，单位为毫秒. */
	private int m_duaration = 15;

	/** 当前帧逝去时长，单位为毫秒. */
	private int m_timePassed = 0;

	/** 自动播放形式 - 动画不会自动播放. */
	public static final byte AUTOPLAY_NONE = 0;
	/** 当前精灵动作的长度，即当前动作的总共延时帧数. */
	protected int m_actionLen = 0;
	/** 当前精灵动作的播放总时长，即当前动作的总共延时帧数*帧播放时长. */
	protected int m_actionDuration = 0;
	/**
	 * 自动播放形式 - 动画将根据时间自动播放，设置duration为间隔， 推荐使用这种自动播放形式，可以保持在不同的帧率下面，获得相同的动画，
	 * 速度，也需要注意跨越帧播放带来的问题.
	 */
	public static final byte AUTOPLAY_TIME = 1;
	/** 自动播放形式 - 动画根据帧停留自动播放. */
	public static final byte AUTOPLAY_FRAME = 2;
	/** 自动播放形式 */
	private byte m_autoPlay = AUTOPLAY_NONE;

	/** 当前动画是否停留在动画结尾. */
	private boolean m_atEnd = false;

	/** 是否对精灵进行横向翻转. */
	protected boolean m_flipX = false;

	/** 精灵所使用的图片索引，从ActorData获得. */
	private short m_imgsUsedIndexs_Actor[] = null;

	/** 事件列表-动画结束 */
	private C2D_EventPool_ActionEnd m_Events_ActionEnd;

	/** 精灵文件夹ID. */
	protected short m_folderID = -1;

	/** 旋转角度 ,围绕-Z(也即顺时针) */
	public float m_degree = 0;

	/** 缩放比率 */
	private float m_zoomOut = 1.0f;
	/** 脚本执行对象 */
	private C2D_ScriptExcutor m_scriptEx;
	// 动作偏移类型定义
	/** 无动作偏移. */
	public static final byte ACTIONOFFSET_NULL = 0;
	/** X方向动作偏移. */
	public static final byte ACTIONOFFSET_X = 1;
	/** XY方向动作偏移. */
	public static final byte ACTIONOFFSET_XY = 2;
	/** XYZ方向动作偏移. */
	public static final byte ACTIONOFFSET_XYZ = 3;
	// 逻辑框类型
	/** 身体区域框. */
	public static final byte LOGIC_BODY = 0;
	/** 脚下区域框. */
	public static final byte LOGIC_FEET = 1;
	/** 攻击区域框. */
	public static final byte LOGIC_ATCK = 2;
	/** 被攻区域框. */
	public static final byte LOGIC_BEAT = 3;
	/** 逻辑框结束. */
	public static final byte LOGIC_END = 19;
	/** 需要每帧检查逻辑框（包含一个身体区域框和一个攻击区域框） */
	public boolean m_needCheckLogicBox = false;
	/** 边界矩形，相对于自身坐标，如果是精灵，则是精灵当前帧的边界矩形，不计算当前精灵的水平翻转状态 */
	public C2D_RectS m_boundsRect;
	/** 相对自身的身体区域框，此区域对应到每个动画的帧，不计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_bodyRect2Self;
	/** 相对自身的攻击区域框，此区域对应到每个动画的帧，不计算当前精灵的水平翻转状态 */
	public C2D_RegionI m_atkRect2Self;

	/** 是否进行过播放 */
	private boolean m_played=false;
	// ============================ 构造函数 ============================

	/**
	 * 根据精灵文件夹ID和精灵ID创建一个Sprite，并且读取精灵所需要资源。
	 * 
	 * @param c2dManager
	 *            C2DManager 精灵数据所存在的C2DManager对象
	 * @param folderID
	 *            short 传入的精灵文件夹ID，来自头文件中的常量定义
	 * @param spriteID
	 *            short 传入此Sprite的ID，来自头文件中的常量定义
	 */
	public C2D_SpriteProto(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		this.m_C2DM = c2dManager;
		this.m_folderID = folderID;
		this.m_spriteID = spriteID;
		initRes();
	}

	/**
	 * 根据精灵原型文件夹ID和精灵原型ID创建一个Actor，并且读取精灵所需要资源。
	 * SpriteFolderID和SpriteID会从精灵原型中自动获取。
	 * 
	 * @param c2dManager
	 *            精灵数据所存在的C2DManager对象
	 * @param anteTypeID
	 *            传入的精灵原型文件夹ID和精灵原型双数值数组，来自头文件中的常量定义。
	 */
	public C2D_SpriteProto(C2D_FrameManager c2dManager, short[] anteTypeID)
	{
		this.m_C2DM = c2dManager;
		if (anteTypeID != null && anteTypeID.length >= 2)
		{
			m_anteType = c2dManager.m_AniM.getAnteType(anteTypeID[0], anteTypeID[1]);
		}
		if (m_anteType != null)
		{
			m_spriteID = m_anteType.actotorID;
		}
		initRes();
	}

	/**
	 * 没有进行任何设置的构造函数。你必须进行传入m_C2DM的引用等设置
	 */
	protected C2D_SpriteProto()
	{
	}

	// ============================ 资源加载与释放 ============================
	/**
	 * 初始化资源.
	 */
	protected void initRes()
	{
		C2D_Debug.logC2D("create sprite:"+m_spriteID);
		loadActorData();
		loadActorImgs();
		setActionAndFrame(0, 0);
	}

	/**
	 * 设置C2D框架
	 * 
	 * @param c2dm
	 */
	public void setC2DM(C2D_FrameManager c2dm)
	{
		m_C2DM = c2dm;
		initRes();
	}

	/**
	 * 载入当前精灵图片资源.
	 */
	private void loadActorData()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null && m_folderID >= 0 && m_spriteID >= 0)
		{
			m_spriteData = m_C2DM.m_AniM.spriteUseData(m_folderID, m_spriteID);
			if(m_spriteData!=null)
			{
				m_imgsUsedIndexs_Actor = m_spriteData.getUsedImgIDs();	
			}
		}
	}

	/**
	 * 载入当前精灵图片资源.
	 */
	private void loadActorImgs()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null)
		{
			m_C2DM.m_AniM.spriteUseImages(this);
		}
	}

	/**
	 * 释放精灵动画数据资源
	 */
	private void releaseSpriteData()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null)
		{
			m_C2DM.m_AniM.spriteUnuseData(m_folderID, m_spriteID);
		}
		m_spriteData = null;
		m_anteType = null;
		m_imgsUsedIndexs_Actor = null;
	}

	/**
	 * 释放动画中本Sprite级别的图片资源
	 */
	private void releaseActorImgs()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null)
		{
			m_C2DM.m_AniM.spriteUnuseImages(this);
		}
	}

	/**
	 * 释放资源.
	 */
	public void onRelease()
	{
		super.onRelease();
		releaseActorImgs();
		releaseSpriteData();
		m_C2DM = null;
		m_property = null;
		m_imgsUsedIndexs_Actor = null;
		m_spriteData = null;
		m_anteType = null;
		m_boundsRect = null;
		if (m_Events_ActionEnd != null)
		{
			m_Events_ActionEnd.doRelease(this);
			m_Events_ActionEnd = null;
		}
		if (m_scriptEx != null)
		{
//			m_scriptEx.doRelease(this);//TODO 后续添加继承
			m_scriptEx = null;
		}
	}

	// ============================ 动画控制 ============================
	/**
	 * 切换精灵原型类型，可以同时对ActionID重置和载入图片.
	 * 
	 * @param antetypeIDT
	 *            short 精灵原型文件夹ID和精灵原型ID双数值数组
	 * @param resetTimePos
	 *            boolean 是否重置时间轴位置
	 * @param resetAction
	 *            boolean 是否重置动作
	 */
	public void setAnteTypeID(short[] antetypeIDT, boolean resetTimePos, boolean resetAction)
	{
		C2D_AnteType newAnteType = null;
		if (antetypeIDT != null && antetypeIDT.length >= 2)
		{
			newAnteType = m_C2DM.m_AniM.getAnteType(antetypeIDT[0], antetypeIDT[1]);
		}
		if (newAnteType != null)
		{
			// 先卸载
			releaseActorImgs();
			releaseSpriteData();
			m_anteType = newAnteType;
			m_spriteID = m_anteType.actotorID;
			// 再加载
			initRes();
			if (resetAction)
			{
				setAction(0);
			}
			if (resetTimePos)
			{
				setFrame(0);
			}
		}
	}
	/**
	 * 切换精灵ID，可以同时对ActionID重置和载入图片.
	 * 
	 * @param spriteID
	 *            short 精灵ID
	 */
	public void setSpriteID(short spriteID)
	{
		setSpriteID(spriteID,true,true);
	}
	/**
	 * 切换精灵ID，可以同时对ActionID重置和载入图片.
	 * 
	 * @param spriteID
	 *            short 精灵ID
	 * @param resetTimePos
	 *            boolean 是否重置时间轴位置
	 * @param resetAction
	 *            boolean 是否重置动作
	 */
	public void setSpriteID(short spriteID, boolean resetTimePos, boolean resetAction)
	{
		if (spriteID >=0 &&spriteID!=getSpriteID())
		{
			// 先卸载
			releaseActorImgs();
			releaseSpriteData();
			m_spriteID = spriteID;
			// 再加载
			initRes();
			if (resetAction)
			{
				setAction(0);
			}
			if (resetTimePos)
			{
				setFrame(0);
			}
		}
	}
	/**
	 * 返回使用到的图片索引.
	 * 
	 * @return short[] 使用到的图片索引
	 */
	short[] getUsedImgIDs()
	{
		return m_imgsUsedIndexs_Actor;
	}

	/**
	 * 设置新的动作ID，会默认将帧ID变成0
	 * 
	 * @param actionID
	 *            新的动作ID
	 * @return 是否进行了设置
	 */
	public boolean setAction(int actionID)
	{
		return setActionAndFrame(actionID, 0);
	}

	/**
	 * 设置帧ID
	 * 
	 * @param frameID
	 *            新的帧ID
	 * @return 是否进行了设置
	 */
	public boolean setFrame(int frameID)
	{
		return setActionAndFrame(m_actionID, frameID);
	}

	/**
	 * 设置动作ID和帧ID
	 * 
	 * @param actionID
	 *            新的动作ID
	 * @param frameID
	 *            新的帧ID
	 * @return 是否进行了设置
	 */
	public boolean setActionAndFrame(int actionID, int frameID)
	{
		if (m_C2DM == null || m_spriteData == null)
		{
			return false;
		}
		if (actionID == m_actionID && frameID == m_frameID&&m_played)
		{
			return false;
		}
		m_played=true;
		if (actionID < 0 || actionID >= m_spriteData.getActionCount())
		{
			return false;
		}
		if (frameID < 0 || frameID >= m_spriteData.getFrameCount(actionID))
		{
			return false;
		}
		m_frameID = (short) frameID;
		m_actionID = (short) actionID;
		m_actionLen=getActionFrameTotal();
		m_actionDuration=m_actionLen*m_duaration;
		m_atEnd = false;
		checkBounds();
		// FIXME 更新所有过渡帧信息
		m_C2DM.m_AniM.setSpriteTime(this, m_actionID, m_frameID);
		if (m_needCheckLogicBox)
		{
			checkLogicBox();
		}
		layoutChanged();
		return true;
	}

	/**
	 * 向前播放N帧，单位为帧数
	 * 
	 * @param nStay
	 *            播放帧数
	 * @param animLoop
	 *            是否循环播放
	 * @return 返回是否已经到达过尾帧
	 */
	protected boolean playFame(int nStay, boolean animLoop)
	{
		if (m_spriteData == null || m_C2DM == null)
		{
			return false;
		}
		boolean reachEnd = false;
		int frameID = m_frameID; // 关键帧ID
		int keyFrameCount = m_spriteData.getFrameCount(m_actionID);// 当前动作的关键帧总数
		while (nStay > 0)
		{
			nStay -= 1;
			frameID++;
			if (frameID >= keyFrameCount)
			{
				reachEnd = true;
				if (animLoop)
				{
					frameID = 0;
				}
				else
				{
					frameID--;
					break;
				}
			}
		}
		// 设置结果
		if (frameID != m_frameID)
		{
			setFrame(frameID);
		}
		m_atEnd = reachEnd;
		// 执行动画结束事件
		if (m_atEnd)
		{
			if (m_Events_ActionEnd != null)
			{
				m_Events_ActionEnd.onCalled();
			}
		}
		return m_atEnd;
	}

	/**
	 * 向前播放至下一帧动画.是否循环取决于自身的循环变量
	 * 
	 * @return boolean 返回是否已经到达尾帧
	 */
	public boolean playFrame()
	{
		return playFame(1, m_animLoop);
	}

	/**
	 * 向前播放N帧动画.是否循环取决于自身的循环变量
	 * 
	 * @param nStay
	 *            播放帧停留数
	 * @return 返回是否已经到达尾帧
	 */
	public boolean playFrame(int nStay)
	{
		return playFame(nStay, m_animLoop);
	}

	/**
	 * 向前播放当前游戏循环逝去时间
	 * 
	 * @return 返回是否已经到达尾帧
	 */
	public boolean playTime()
	{
		C2D_Stage stage = getStageAt();
		if (stage == null)
		{
			return false;
		}
		return playTime(stage.getTimePassed(), m_animLoop);
	}

	/**
	 * 向前播放一段时间,单位为毫秒
	 * 
	 * @param time
	 *            播放时长
	 * @param animLoop
	 *            是否循环播放
	 * @return 返回是否已经到达尾帧
	 */
	protected boolean playTime(int time, boolean animLoop)
	{
		if (m_spriteData == null || m_C2DM == null)
		{
			return false;
		}
		boolean reachEnd = false;
		int timePassed = m_timePassed;// 当前帧逝去时间
		int frameID = m_frameID; // 关键帧ID
		int keyFrameCount = m_spriteData.getFrameCount(m_actionID);// 当前动作的关键帧总数
		while (time > 0)
		{
			if (timePassed + time > m_duaration)
			{
				time -= m_duaration - timePassed;
				timePassed = 0;
				frameID++;
				if (frameID >= keyFrameCount)
				{
					reachEnd = true;
					if (animLoop)
					{
						frameID = 0;
					}
					else
					{
						frameID--;
						break;
					}
				}
			}
			else
			{
				timePassed += time;
				time = 0;
			}
		}
		// 设置结果
		m_timePassed = timePassed;
		m_atEnd = reachEnd;
		if (frameID != m_frameID)
		{
			setFrame(frameID);
		}
		// 执行动画结束事件
		if (reachEnd)
		{
			if (m_Events_ActionEnd != null)
			{
				m_Events_ActionEnd.onCalled();
			}
		}
		return reachEnd;
	}

	/**
	 * 查看是否位于动作尾帧.
	 * 
	 * @return boolean 返回是否位于动作尾帧
	 */
	public boolean atActionEnd()
	{
		return m_atEnd;
	}

	/**
	 * 返回精灵ID.
	 * 
	 * @return short 精灵ID
	 */
	public short getSpriteID()
	{
		return this.m_spriteID;
	}

	/**
	 * 返回精灵的NPC ID.
	 * 
	 * @return short 精灵NPC ID
	 */
	public int getNpcID()
	{
		return this.m_npcID;
	}

	/**
	 * 返回精灵的精灵原型ID.
	 * 
	 * @return int 精灵原型ID
	 */
	public int getAntetypeID()
	{
		if (this.m_anteType == null)
		{
			return -1;
		}
		else
		{
			return m_C2DM.m_AniM.getAnteTypeID(m_anteType);
		}
	}

	/**
	 * 返回当前动作ID.
	 * 
	 * @return int 当前动作ID
	 */
	public int getActionID()
	{
		return this.m_actionID;
	}

	/**
	 * 返回当前动作数量.
	 * 
	 * @return int 当前动作数量
	 */
	public int getActionCount()
	{
		if (m_spriteData == null)
		{
			return 0;
		}
		return this.m_spriteData.getActionCount();
	}

	/**
	 * 返回当前关键帧ID.
	 * 
	 * @return int 当前关键帧ID
	 */
	public int getFrameID()
	{
		return this.m_frameID;
	}

	/**
	 * 返回指定动作整个动作的帧数总和 (即从动作起始，到动作结束，整个动作所有帧数合计).
	 * 
	 * @param actionID
	 *            int 指定动作ID
	 * @return int 整个动作所有帧数合计
	 */
	public int getActionFrameTotal(int actionID)
	{
		if (m_spriteData == null || actionID < 0 || actionID >= getActionCount())
		{
			return 0;
		}
		int stay = 0;
		int frameCount = m_spriteData.getFrameCount(actionID);
		for (int i = 0; i < frameCount; i++)
		{
			stay += 1;
		}
		return stay;
	}

	/**
	 * 返回当前动作整个动作的帧数总和 (即从动作起始，到动作结束，整个动作所有帧停留合计).
	 * 
	 * @return int 整个动作所有帧停留合计
	 */
	public int getActionFrameTotal()
	{
		return getActionFrameTotal(m_actionID);
	}

	/**
	 * 返回当前动作剩余的帧停留(即从当前帧位置，到结尾帧，所有帧停留合计).
	 * 
	 * @return int 前动作的帧停留.
	 */
	public int getActionFrameLeft()
	{
		int frameCount = m_spriteData.getFrameCount(m_actionID);
		int stay = frameCount - m_frameID;
		return stay;
	}

	/**
	 * 返回精灵文件夹ID.
	 * 
	 * @return short 精灵文件夹ID
	 */
	public short getActorFolderID()
	{
		return this.m_folderID;
	}

	/**
	 * 返回当前动作的长度，即关键帧帧的个数.
	 * 
	 * @return int 动作的长度
	 */
	public int getKeyFrameCount()
	{
		return getKeyFrameCount(m_actionID);
	}

	/**
	 * 返回指定动作的长度，即关键帧的个数.
	 * 
	 * @param actionID
	 *            指定动作ID
	 * @return 动作的长度
	 */
	public int getKeyFrameCount(int actionID)
	{
		if (m_spriteData == null || actionID < 0 || actionID >= getActionCount())
		{
			return 0;
		}
		int frameCount = m_spriteData.getFrameCount(actionID);
		return frameCount;
	}

	/**
	 * 返回动作帧X方向偏移(注意偏移使用笛卡尔坐标系,+X指向右,+y指向上，+z指向外)
	 * 
	 * @return int 当前帧的X方向动作偏移
	 */
	public int getActionOffset_X()
	{
		return m_spriteData.getActionOffset((byte) 0, m_actionID, m_frameID);
	}

	/**
	 * 返回当前帧Y方向动作偏移(注意偏移使用笛卡尔坐标系,+X指向右,+y指向上，+z指向外)
	 * 
	 * @return int 当前帧的Y方向动作偏移
	 */
	public int getActionOffset_Y()
	{
		return m_spriteData.getActionOffset((byte) 1, m_actionID, m_frameID);
	}

	/**
	 * 返回当前帧帧Z方向动作偏移(注意偏移使用笛卡尔坐标系,+X指向右,+y指向上，+z指向外)
	 * 
	 * @return int 当前帧的Z方向动作偏移
	 */
	public int getActionOffset_Z()
	{
		return m_spriteData.getActionOffset((byte) 2, m_actionID, m_frameID);
	}

	/**
	 * 获取是否沿X轴翻转
	 * 
	 * @return boolean 是否沿X轴翻转
	 */
	public boolean getFlipX()
	{
		return m_flipX;
	}

	/**
	 * 设置是否沿X轴翻转
	 * 
	 * @param flipX
	 *            是否沿X轴翻转
	 */
	public void setFlipX(boolean flipX)
	{
		if (m_flipX != flipX)
		{
			m_flipX = flipX;
			layoutChanged();
		}

	}

	/**
	 * 获取是否循环播放
	 * 
	 * @return boolean 是否循环播放
	 */
	public boolean getAnimLoop()
	{
		return m_animLoop;
	}

	/**
	 * 设置是否循环播放
	 * 
	 * @param animLoop
	 *            是否循环播放
	 */
	public void setAnimLoop(boolean animLoop)
	{
		if (m_animLoop != animLoop)
		{
			m_animLoop = animLoop;
		}
	}

	/**
	 * 获取NPC ID
	 * 
	 * @return short NPC ID
	 */
	public short setNpcID()
	{
		return m_npcID;
	}

	/**
	 * 设置NPC ID
	 * 
	 * @param npcID
	 *            即 NPC ID
	 */
	public void setNpcID(short npcID)
	{
		m_npcID = npcID;
	}

	/**
	 * 获取播放一帧的时长，单位为毫秒
	 * 
	 * @return 播放一帧的时长
	 */
	public int getDuration()
	{
		return m_duaration;
	}

	/**
	 * 设置播放一帧的时长，单位为毫秒，必须设置正值
	 * 
	 * @param duration
	 *            播放一帧的时长
	 * @return 是否成功设置
	 */
	public boolean setDuration(int duration)
	{
		if (duration > 0 && m_duaration != duration)
		{
			m_duaration = duration;
			m_actionDuration=m_actionLen*m_duaration;
			return true;
		}
		return false;
	}

	/**
	 * 获取是循环播放类型
	 * 
	 * @return byte 循环播放类型
	 */
	public byte getAutoPlay()
	{
		return m_autoPlay;
	}

	/**
	 * 设置是否自动播放类型，可以根据时间或者按帧停留进行自动播放 如果设置为不自动播放，需要使用nextFrame进行手动按帧播放
	 * 
	 * @param autoPlay
	 *            自动播放类型
	 */
	public void setAutoPlay(byte autoPlay)
	{
		if (m_autoPlay != autoPlay && autoPlay >= AUTOPLAY_NONE && autoPlay <= AUTOPLAY_FRAME)
		{
			m_autoPlay = autoPlay;
		}
	}

	/**
	 * 获取当前停留帧已经逝去的时间，毫秒为单位
	 * 
	 * @return 逝去的时间
	 */
	public int getTimePassed()
	{
		return m_timePassed;
	}

	// ============================ 更新与显示 ============================
	/**
	 * 遍历所有子节点，执行更新
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		if (m_autoPlay == AUTOPLAY_TIME)
		{
			playTime(stage.getTimePassed(), m_animLoop);
		}
		else if (m_autoPlay == AUTOPLAY_FRAME)
		{
			playFrame();
		}
		super.onUpdate(stage);
	}

	/**
	 * 获取相对布局矩形，即基于当前的坐标，尺寸，锚点，翻转等信息， 计算出相对于其父节点所占据的矩形区域。将信息存放在传入的
	 * 矩形参数，并返回是否成功取得。
	 * 
	 * @param resultRect
	 *            用于结果存放的矩形对象
	 * @return 是否成功取得
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null || m_boundsRect == null)
		{
			return false;
		}
		return C2D_GdiGraphics.computeLayoutRect(m_x + m_boundsRect.m_x, m_y + m_boundsRect.m_y, m_boundsRect.m_width, m_boundsRect.m_height, 0, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if (!m_visible || stage == null)
		{
			return;
		}
		if (m_C2DM != null)
		{
			m_C2DM.m_AniM.drawSpriteKeyFrame(this, m_zoomOut, m_xToTop, m_yToTop, m_flipX, 0, -1);
		}
	}

	/**
	 * 根据指定的信息绘制精灵 此时无视精灵本身坐标，指定位置、动作ID、帧ID、水平翻转等信息.
	 * 
	 * @param g
	 *            绘图指针
	 * @param screenX
	 *            屏幕X坐标
	 * @param screenY
	 *            屏幕Y坐标
	 * @param actionID
	 *            指定的动作ID
	 * @param frameID
	 *            指定的帧ID
	 * @param flipX
	 *            指定的水平翻转
	 * @param limitRect
	 *            指定的限制显示区域
	 */
	public void display(C2D_Graphics g, int screenX, int screenY, int actionID, int frameID, boolean flipX, C2D_RectS limitRect)
	{
		if (m_C2DM != null)
		{
			m_C2DM.m_AniM.drawSpriteKeyFrame(this, actionID, frameID, m_zoomOut, screenX, screenY, flipX, 0, -1, null, 0);
		}
	}

	/**
	 * 设置角度
	 * 
	 * @param degreeT
	 */
	public void setDegree(float degreeT)
	{
		this.m_degree = degreeT;
	}

	/**
	 * 获得缩放比率
	 * 
	 * @return 缩放比率
	 */
	public float getZoomOut()
	{
		return m_zoomOut;
	}

	/**
	 * 设置缩放比率
	 * 
	 * @param m_zoomOut
	 *            缩放比率
	 */
	public void setZoomOut(float m_zoomOut)
	{
		this.m_zoomOut = m_zoomOut;
	}

	// ============================ 屏幕与地图显示 ============================
	/**
	 * 根据自身坐标在地图上显示，显示时减去传入的相机坐标.
	 * 
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param cameraL
	 *            float 相机相对地图坐标X
	 * @param cameraT
	 *            float 相机相对地图坐标Y
	 * @param flipX
	 *            boolean 是否水平翻转显示
	 */
	public void displayInMap(float zoomLevel, float cameraL, float cameraT, boolean flipX)
	{
		if (!m_visible)
		{
			return;
		}
		if (m_C2DM != null)
		{
			m_C2DM.m_AniM.drawSpriteKeyFrame(this, zoomLevel, m_x - cameraL, m_y - cameraT, flipX);
		}
	}

	/**
	 * 显示到屏幕上指定的坐标，此时无视精灵本身坐标.
	 * 
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param screenX
	 *            float 屏幕X坐标
	 * @param screenY
	 *            float 屏幕Y坐标
	 * @param limitRect
	 *            short[] 限制显示区域(x,y,w,h形式)
	 */
	public void display(float zoomLevel, float screenX, float screenY, short limitRect[])
	{
		display(zoomLevel, screenX, screenY, false, limitRect, m_degree);
	}

	/**
	 * 显示到屏幕上指定的坐标，此时无视精灵本身坐标.
	 * 
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param screenX
	 *            float 屏幕X坐标
	 * @param screenY
	 *            float 屏幕Y坐标
	 * @param flipX
	 *            boolean 是否水平翻转显示
	 * @param limitRect
	 *            short[] 限制显示区域(x,y,w,h形式)
	 * @param degree
	 *            float 旋转角度
	 */
	public void display(float zoomLevel, float screenX, float screenY, boolean flipX, short limitRect[], float degree)
	{
		if (!m_visible)
		{
			return;
		}
		m_C2DM.m_AniM.drawSpriteKeyFrame(this, m_actionID, m_frameID, zoomLevel, screenX, screenY, flipX, 0, Integer.MAX_VALUE, limitRect, degree);
	}

	/**
	 * 显示到屏幕上指定的坐标，此时无视精灵本身坐标，指定位置、动作ID、帧ID和限制显示区域.
	 * 
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param screenX
	 *            float 屏幕X坐标
	 * @param screenY
	 *            float 屏幕Y坐标
	 * @param actionID
	 *            int 指定的动作ID
	 * @param frameID
	 *            int 指定的帧ID
	 * @param limitRect
	 *            short[] 指定的限制显示区域
	 */
	public void display(float zoomLevel, float screenX, float screenY, int actionID, int frameID, short limitRect[])
	{
		display(zoomLevel, screenX, screenY, actionID, frameID, limitRect, m_degree);
	}

	public void display(float zoomLevel, float screenX, float screenY, int actionID, int frameID, short limitRect[], float degreeT)
	{
		if (m_C2DM != null)
		{
			m_C2DM.m_AniM.drawSpriteKeyFrame(this, actionID, frameID, zoomLevel, screenX, screenY, false, 0, 9999, limitRect, degreeT);
		}
	}

	// ============================ 逻辑框获得 ============================
	/**
	 * 获得精灵当前帧的所有指定类型的逻辑框，中心点相对屏幕坐标形式(以精灵坐标减去相机坐标的屏幕坐标为中心)，返回获得的逻辑框数目，数据存储在数组中.
	 * 
	 * @param dataBoxs
	 *            int[] 逻辑框存储数组，以x,y,w,h形式存放相对于精灵中心点的逻辑框
	 * @param logicType
	 *            int 逻辑框类型[0-255]之间
	 * @param cameraL
	 *            int 相机坐标X
	 * @param cameraT
	 *            int 相机坐标Y
	 * @return int 逻辑框数量(发生错误返回-1，数据缓冲不足返回-2)
	 */
	public int getLogicBoxToScreen(int dataBoxs[], int logicType, int cameraL, int cameraT)
	{
		if (m_C2DM != null)
		{
			return m_C2DM.m_AniM.getLogicBox(dataBoxs, logicType, this.m_spriteID, m_actionID, this.m_frameID, m_x - cameraL, m_y - cameraT, m_flipX);
		}
		return -1;
	}

	/**
	 * 获得精灵当前帧的所有指定类型的逻辑框，中心点相对世界坐标形式(以精灵世界坐标为中心)，返回获得的逻辑框数目，数据存储在数组中.
	 * 逻辑框数值相对动画中心
	 * 
	 * @param dataBoxs
	 *            int[] 逻辑框存储数组，以xl,yt,xr,yb形式存放相对于精灵中心点的逻辑框
	 * @param logicType
	 *            int 逻辑框类型[0-255]之间
	 * @return int 逻辑框数量(发生错误返回-1，数据缓冲不足返回-2)
	 */
	public int getLogicBox(int dataBoxs[], int logicType)
	{
		if (m_C2DM != null)
		{
			return m_C2DM.m_AniM.getLogicBox(dataBoxs, logicType, this.m_spriteID, m_actionID, this.m_frameID, 0, 0, m_flipX);
		}
		return -1;
	}

	/**
	 * 在指定的精灵动作的指定帧获得指定类型的所有逻辑框，中心点相对屏幕坐标形式(以精灵坐标减去相机坐标的屏幕坐标为中心)
	 * ，返回获得的逻辑框数目，数据存储在数组中。.
	 * 
	 * @param dataBoxs
	 *            int[]逻辑框存储数组，以x,y,w,h形式存放相对于精灵中心点的逻辑框
	 * @param logicType
	 *            int 逻辑框类型[0-255]之间
	 * @param cameraL
	 *            int 镜头相对世界坐标X
	 * @param cameraT
	 *            int 镜头相对世界坐标Y
	 * @param actionId
	 *            int 动作ID
	 * @param frameID
	 *            int 帧ID
	 * @return int 逻辑框数量(发生错误返回-1，数据缓冲不足返回-2)
	 */
	public int getLogicBoxByID(int dataBoxs[], int logicType, int cameraL, int cameraT, int actionId, int frameID)
	{
		if (m_C2DM != null)
		{
			return m_C2DM.m_AniM.getLogicBox(dataBoxs, logicType, this.m_spriteID, actionId, frameID, m_x - cameraL, m_y - cameraT, m_flipX);
		}
		return -1;
	}

	/**
	 * 在指定的精灵动作的指定帧获得指定类型的所有逻辑框，中心点相对世界坐标形式(以精灵世界坐标为中心)， ，返回获得的逻辑框数目，数据存储在数组中。.
	 * 
	 * @param dataBoxs
	 *            int[]逻辑框存储数组，以x,y,w,h形式存放相对于精灵中心点的逻辑框
	 * @param logicType
	 *            int 逻辑框类型[0-255]之间
	 * @param actionId
	 *            int 动作ID
	 * @param frameID
	 *            int 帧ID
	 * @return int 逻辑框数量(发生错误返回-1，数据缓冲不足返回-2)
	 */
	public int getLogicBoxByID(int dataBoxs[], int logicType, int actionId, int frameID)
	{
		if (m_C2DM != null)
		{
			return m_C2DM.m_AniM.getLogicBox(dataBoxs, logicType, this.m_spriteID, actionId, frameID, m_x, m_y, m_flipX);
		}
		return -1;
	}

	// ============================ 精灵属性相关 ============================
	/**
	 * 设置整个精灵属性.
	 * 
	 * @param actorProperty
	 *            属性对象
	 */
	public void setProperty(C2D_Prop actorProperty)
	{
		this.m_property = actorProperty;
	}

	/**
	 * 获取精灵的属性对象.
	 * 
	 * @return PropInstance 精灵属性对象
	 */
	public C2D_Prop getProperty()
	{
		return m_property;
	}

	// ============================ 事件处理 ============================
	/**
	 * 获得动画播放结束事件池
	 * 
	 * @return 动画播放结束事件池
	 */
	public C2D_EventPool_ActionEnd Events_ActionEnd()
	{
		if (m_Events_ActionEnd == null)
		{
			m_Events_ActionEnd = new C2D_EventPool_ActionEnd(this);
		}
		return m_Events_ActionEnd;
	}

	// ============================ 数据检查 ============================


	/**
	 * 刷新指定精灵的帧边界.
	 * 
	 */
	protected void checkBounds()
	{
		/*
		C2D_SpriteClip all_clips[] = m_C2DM.m_AniM.spriteClips;
		C2D_SpriteModel actorData = m_spriteData;
		if (actorData == null || all_clips == null)
		{
			C2D_Debug.log("null pointer in getKeyFrameBounds");
			return;
		}
		if (m_actionID < 0 || (m_actionID > 0 && m_actionID >= actorData.getActionCount()) || m_frameID < 0 || (m_frameID > 0 && m_frameID >= actorData.getFrameCount(m_actionID)))
		{
			C2D_Debug.log("actionId or frameId - Array index out of bounds");
			return;
		}
		m_boundsRect = actorData.getFrameBound(m_actionID, m_frameID);
		if (m_boundsRect == null)
		{
			m_boundsRect = new C2D_RectS();
			// 复位Bounds
			boolean inited = false;
			int m_BoundsL = Short.MIN_VALUE;
			int m_BoundsT = Short.MIN_VALUE;
			int m_BoundsR = Short.MIN_VALUE;
			int m_BoundsB = Short.MIN_VALUE;
			// 帧信息整理
			int baseFrameIndex = actorData.getFrameFlag_BaseID(m_actionID, m_frameID);
			byte frameFlag = actorData.getFrameFlag_Trans(m_actionID, m_frameID);
			// if (m_flipX)
			// {
			// frameFlag =
			// C2D_Graphics.TRANS_ARRAY[frameFlag][C2D_Graphics.TRANS_HORIZENTAL];
			// }
			short clipsBegin = actorData.baseFrames_pos[baseFrameIndex << 1];
			short clipsCount = actorData.baseFrames_pos[(baseFrameIndex << 1) + 1];
			int clipID, clipIDT, off_x, off_y, res_ID, _w, _h, resType;
			int cx, cy, off_xo, wReal, hReal;
			byte clip_Flag, complex_Falg;
			int xL, yT, xR, yB;
			for (int i = 0; i < clipsCount; i++)
			{
				// 切片信息整理
				int cid = clipsBegin + (i << 2);// *
												// C2D_SpriteData.BASE_FRAME_STEP
				clipID = actorData.baseFrames[cid];
				off_x = actorData.baseFrames[cid + 1];
				off_y = actorData.baseFrames[cid + 2];
				clip_Flag = (byte) actorData.baseFrames[cid + 3];
				if (clipID >= m_C2DM.m_AniM.all_clips_count)
				{
					C2D_Debug.log("clipID - Array index out of bounds");
					continue;
				}
				clipIDT = clipID * C2D_SpriteManager.CLIP_CHUNK_LEN;
				res_ID = all_clips[clipIDT + 0];
				_w = all_clips[clipIDT + 3];
				_h = all_clips[clipIDT + 4];
				resType = C2D_SpriteManager.getResType(res_ID);
				if (resType == C2D_SpriteManager.TYPE_LOGIC)
				{
					continue;
				}
				if (resType == C2D_SpriteManager.TYPE_TEXT)
				{ // 文字
					clip_Flag = C2D_Graphics.TRANS_NONE;
					String s = m_C2DM.m_TxtM.getString(res_ID - C2D_SpriteManager.SEPERATE_TEXT, true);
					if (s != null)
					{
						_w = (short) C2D_TextFont.getDefaultFont().stringWidth(s);
						_h = (short) C2D_TextFont.getDefaultFont().getFontHeight();
					}
				}
				// 帧翻转处理
				cx = 0;
				cy = 0;
				if ((frameFlag & C2D_Graphics.TRANS_HORIZENTAL) != 0)
				{
					off_x = -off_x;
				}
				if ((frameFlag & C2D_Graphics.TRANS_VERTICAL) != 0)
				{
					off_y = -off_y;
				}
				if ((frameFlag & C2D_Graphics.TRANS_FOLD_RT2LB) != 0)
				{
					off_xo = off_x;
					off_x = off_y;
					off_y = off_xo;
				}
				complex_Falg = C2D_Graphics.TRANS_ARRAY[clip_Flag][frameFlag];
				wReal = _w;
				hReal = _h;
				if ((complex_Falg & C2D_Graphics.TRANS_FOLD_RT2LB) != 0)
				{
					wReal = _h;
					hReal = _w;
				}
				cx += off_x - C2D_SpriteManager.getHalfWidth(clip_Flag, frameFlag, _w, _h);// X方向偏移调整
				cy += off_y - C2D_SpriteManager.getHalfHeight(clip_Flag, frameFlag, _w, _h);// Y方向偏移调整
				// 当前切片的边框
				xL = cx;
				yT = cy;
				xR = cx + wReal;
				yB = cy + hReal;
				if (!inited)
				{
					inited = true;
					m_BoundsL = (short) xL;
					m_BoundsT = (short) yT;
					m_BoundsR = (short) xR;
					m_BoundsB = (short) yB;
				}
				else
				{
					if (xL < m_BoundsL)
					{
						m_BoundsL = (short) xL;
					}
					if (yT < m_BoundsT)
					{
						m_BoundsT = (short) yT;
					}
					if (xR > m_BoundsR)
					{
						m_BoundsR = (short) xR;
					}
					if (yB > m_BoundsB)
					{
						m_BoundsB = (short) yB;
					}
				}
			}
			// 存储
			if (inited)
			{
				m_boundsRect.m_x = (short) m_BoundsL;
				m_boundsRect.m_y = (short) m_BoundsT;
				m_boundsRect.m_width = (short) (m_BoundsR - m_BoundsL);
				m_boundsRect.m_height = (short) (m_BoundsB - m_BoundsT);
				actorData.setFrameBound(m_actionID, m_frameID, m_boundsRect);
			}
		}
		if (m_regionShow != null)
		{
			m_regionShow.setValue((m_flipX ? (-m_boundsRect.m_x - m_boundsRect.m_width) : m_boundsRect.m_x) + m_xToTop, m_boundsRect.m_y + m_yToTop, m_boundsRect.m_width, m_boundsRect.m_height);
		}
		*/
	}
	// ============================ 数据优化 ============================
	
	public void readScript(DataInputStream di)
	{
		if (di == null)
		{
			return;
		}
		try
		{
			// 读取场景脚本
			int len = 0;
			len = C2D_IOUtil.readInt(len, di);
			String scriptName = null;
			for (int i = 0; i < len; i++)
			{
				scriptName = C2D_IOUtil.readString(scriptName, di);
				if (m_scriptEx == null)
				{
					m_scriptEx = m_C2DM.m_SptM.createScriptExcutor();
					m_scriptEx.setFunctionHandler(this);
					m_scriptEx.switchToThread(scriptName);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean handleFunction(C2D_Thread currentThread, int functionID)
	{
		return false;
	}

	private static int dataBoxs[] = new int[8];

	/**
	 * 检测相对自身的逻辑框(包括一个身体区域框和一个攻击区域框)
	 */
	protected void checkLogicBox()
	{
		/*
		short all_clips[] = m_C2DM.m_AniM.spriteClips;
		C2D_SpriteModel actorData = m_spriteData;
		if (actorData == null || all_clips == null || actorData.baseFrames == null)
		{
			C2D_Debug.log("null pointer in getKeyFrameBounds");
			return;
		}
		if (m_actionID < 0 || (m_actionID > 0 && m_actionID >= actorData.getActionCount()) || m_frameID < 0 || (m_frameID > 0 && m_frameID >= actorData.getFrameCount(m_actionID)))
		{
			C2D_Debug.log("actionId or frameId - Array index out of bounds");
			return;
		}
		// 身体区域框
		m_bodyRect2Self = actorData.getBodyRect(m_actionID, m_frameID);
		if (m_bodyRect2Self == null)
		{
			int rectCount = m_C2DM.m_AniM.getLogicBox(dataBoxs, C2D_Sprite.LOGIC_BODY, m_actorID, m_actionID, m_frameID, 0, 0, false);
			if (rectCount <= 0)
			{
				m_bodyRect2Self = null;
			}
			else
			{
				m_bodyRect2Self = new C2D_RegionI();
				m_bodyRect2Self.setValue(dataBoxs[0], dataBoxs[1], dataBoxs[2], dataBoxs[3]);
			}
			actorData.setBodyRect(m_actionID, m_frameID, m_bodyRect2Self);
		}
		// 攻击区域框
		m_atkRect2Self = actorData.getAtkRect(m_actionID, m_frameID);
		if (m_atkRect2Self == null)
		{
			int rectCount = m_C2DM.m_AniM.getLogicBox(dataBoxs, C2D_Sprite.LOGIC_ATCK, m_actorID, m_actionID, m_frameID, 0, 0, false);
			if (rectCount <= 0)
			{
				m_atkRect2Self = null;
			}
			else
			{
				m_atkRect2Self = new C2D_RegionI();
				m_atkRect2Self.setValue(dataBoxs[0], dataBoxs[1], dataBoxs[2], dataBoxs[3]);
			}
			actorData.setAtkRect(m_actionID, m_frameID, m_atkRect2Self);
		}
		*/
	}

	protected float getWidth()
	{
		if (m_boundsRect != null)
		{
			return m_boundsRect.m_width;
		}
		return 0;
	}

	protected float getHeight()
	{
		if (m_boundsRect != null)
		{
			return m_boundsRect.m_height;
		}
		return 0;
	}
	/**
	 * 从父容器中移除并设置隐藏
	 */
	public void removeFromTree()
	{
		//从父容器中移除
		C2D_View sp = getParentNode();
		if (sp != null)
		{
			sp.removeChild(this);
		}
		setVisible(false);
	}
}
