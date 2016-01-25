package c2d.mod.map.scroll;

import java.io.DataInputStream;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Array;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.script.C2D_ScriptExcutor;
import c2d.mod.script.C2D_Thread;
import c2d.mod.script.C2D_VM;
import c2d.mod.sprite.C2D_Sprite;

/**
 * 卷轴地图 它本身是一个视图，将从数据中读取关卡数据，并加载内容。 它的子树被分成了[卷轴背景1，景物层1]，[卷轴背景2，景物层2]...这类序列，
 * zorder是从0开始，以[0,5],[10,15]...的形式逐渐递增。其中景物层等
 * 我们也称之为分层视图，后续我们更多的操作是在这些分层视图上进行的。如果我们 需要增加分层视图，应该注意原先各个分层视图的zorder。
 * 卷轴背景是由指定的图片平铺构成的静态背景，一般我们无需关心； 景物层则是一个视图，其中每层中含有的众多精灵对象，我们可以根据ID获得
 * 相应的景物层，然后向其中添加精灵或者操作其中的精灵对象。如果你想加载你自己
 * 的精灵对象。你可以注册一个精灵加载器registerSpriteLoader，在其中的
 * 回调方法中实现加载自己的精灵。并且在加载精灵之后，精灵加载器的afterLoad 方法会被调用，此方法可以用于实现一些精灵的初始化。
 * 卷轴地图有一个摄像机对象，它指定了程序即将渲染的卷轴地图中的区域。 我们可以通过调整摄像机的位置变换渲染区域，也可以使用相机的一些跟随功能，来
 * 实现连续的区域切换。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ScrollMap extends C2D_View implements C2D_SpriteLoader
{
	private C2D_FrameManager m_frameManager;
	/** 世界宽度 */
	protected int m_w;
	/** 世界高度 */
	protected int m_h;
	/** 场景脚本执行器 */
	private C2D_ScriptExcutor m_scriptEx;
	/** 摄像机 */
	protected C2D_Camera m_camera;
	/** 分层视图容器 */
	protected C2D_Array m_levelViews;
	/** 分层视图背景 */
	protected C2D_Array m_levelBgs;
	/** 精灵加载器 */
	private C2D_SpriteLoader m_spriteLoader = this;

	/**
	 * 构造卷轴地图,从资源框架进行初始化
	 * 
	 * @param frameManager
	 *            C2D框架
	 */
	public C2D_ScrollMap(C2D_FrameManager frameManager)
	{
		m_frameManager = frameManager;

	}
	/**
	 * 构造一个自定义大小的卷轴地图
	 * @param w 世界宽度
	 * @param h 世界高度
	 */
	public C2D_ScrollMap(int w,int h)
	{
		init(w,h);
	}
	/**
	 * 初始化
	 * @param w 世界宽度
	 * @param h 世界高度
	 */
	protected void init(int w,int h)
	{
		m_w=w;
		m_h=h;
		setSize(m_w, m_h);
		m_camera = new C2D_Camera(this);
		m_levelViews = new C2D_Array();
		m_levelBgs=new C2D_Array();
	}
	/**
	 * 获得摄像机
	 * 
	 * @return
	 */
	public C2D_Camera getCamera()
	{
		return m_camera;
	}

	/**
	 * 载入关卡地图
	 * 
	 * @param mapID
	 *            关卡地图ID
	 * @return 是否载入成功
	 */
	public boolean loadMap(int mapID)
	{
		return loadMap(mapID, null, null);
	}
	static short[] buffer=new short[2];
	/**
	 * 载入关卡地图
	 * 
	 * @param mapID
	 *            关卡地图ID
	 * @param loadMode
	 *            载入模式,此参数用来表示分割加载背景图片，<br>
	 *            例如传入int[]{3,2}表示将使用5块分割图片进行加载，第一行3块，<br>
	 *            第二行2块，它们一起组成了原图层中的一张完整背景图<br>
	 * @param cameraFar
	 *            镜头距离参数
	 * @return 是否载入成功
	 */
	public boolean loadMap(int mapID, int[] loadMode, float[] cameraFar)
	{
		if (m_frameManager == null)
		{
			return false;
		}
		String resName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + m_frameManager.resName + "_smap_" + mapID + ".bin";
		DataInputStream di = C2D_IOUtil.getDataInputStream(resName);
		if (di == null)
		{
			return false;
		}
		try
		{
			//读取世界尺寸
			m_w = C2D_IOUtil.readInt(m_w, di);
			m_h = C2D_IOUtil.readInt(m_h, di);
			init(m_w,m_h);
			int len = 0;
			//读取场景脚本
			len = C2D_IOUtil.readInt(len, di);
			String scriptName = null;
			for (int i = 0; i < len; i++)
			{
				scriptName = C2D_IOUtil.readString(scriptName, di);
				if (m_scriptEx == null)
				{
					m_scriptEx = m_frameManager.m_SptM.createScriptExcutor();
					m_scriptEx.switchToThread(scriptName);
				}
			}
			//读取景物层
			len = C2D_IOUtil.readInt(len, di);
			String imageName = null;
			int scroll_x = 0;
			int scroll_y = 0;
			int scroll_len = 0;
			String folderName = "imgs_" + m_frameManager.resName + "/";
			int atID = 0;
			int actionID = 0;
			int frameID = 0;
			int atX = 0;
			int atY = 0;
			int numAt = 0;
			for (int i = 0; i < len; i++)
			{
				imageName = C2D_IOUtil.readString(imageName, di);
				scroll_x = C2D_IOUtil.readInt(scroll_x, di);
				scroll_y = C2D_IOUtil.readInt(scroll_y, di);
				scroll_len = C2D_IOUtil.readInt(scroll_len, di);
				//创建平铺地图
				if (imageName != null && !imageName.equals(""))
				{
					C2D_ScrollTile tBox = new C2D_ScrollTile(imageName, folderName, loadMode);
					tBox.setPosTo(scroll_x, scroll_y);
					tBox.setSize(scroll_len, tBox.m_imgSize.m_height);
					this.addChild(tBox);
					tBox.setZOrder(i * 10 + 0);
					if (cameraFar != null)
					{
						if (i < cameraFar.length)
						{
							tBox.setCameraFarX(cameraFar[i]);
						}
						else
						{
							tBox.setCameraFarX(0);
						}
					}
				}
				//读取景物
				C2D_ScrollLayer lvView = new C2D_ScrollLayer(m_frameManager);
				lvView.setSize(getWidth(), getHeight());
				this.addChild(lvView);
				lvView.setZOrder(i * 10 + 5);
				numAt = C2D_IOUtil.readInt(numAt, di);
				for (int j = 0; j < numAt; j++)
				{
					//读取景物数值
					atID = C2D_IOUtil.readInt(atID, di);
					actionID = C2D_IOUtil.readInt(actionID, di);
					frameID = C2D_IOUtil.readInt(frameID, di);
					atX = C2D_IOUtil.readInt(atX, di);
					atY = C2D_IOUtil.readInt(atY, di);
					//创建景物
					buffer[0]=0;//FIXME 角色原型文件夹ID
					buffer[1]=(short)atID;//FIXME 角色原型ID
					C2D_Sprite sprite = m_spriteLoader.loadSprite(m_frameManager, buffer,actionID,frameID,atX,atY,j);
					if(sprite!=null)
					{
						sprite.setAction(actionID);
						sprite.setFrame(frameID);
						sprite.setPosTo(atX, atY);
						lvView.addChild(sprite);
						sprite.setZOrder(j);
						//读取脚本
						sprite.readScript(di);
						//加载完毕
						m_spriteLoader.afterLoad(sprite);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (di != null)
			{
				try
				{
					di.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 获取分层视图，每个图层都保存了众多精灵
	 * 
	 * @return 分层视图集合
	 */
	public C2D_Array getLevelViews()
	{
		return m_levelViews;
	}

	/**
	 * 获取分层视图背景，每个图层都可能保存了一张背景图片
	 * 
	 * @return 分层视图背景集合
	 */
	public C2D_Array getLevelBgs()
	{
		return m_levelBgs;
	}
	/**
	 * 获取指定ID的分层视图，每个图层都保存了众多精灵
	 * 
	 * @return 指定的分层视图
	 */
	public C2D_ScrollLayer getLevelView(int id)
	{
		if (m_levelViews == null || id < 0 || id >= m_levelViews.size())
		{
			return null;
		}
		return (C2D_ScrollLayer) m_levelViews.elementAt(id);
	}
	/**
	 * 获取指定ID的分层视图背景，每个图层可能保存了一张背景图片
	 * 
	 * @return 指定的分层视图背景
	 */
	public C2D_ScrollTile getLevelBg(int id)
	{
		if (m_levelBgs == null || id < 0 || id >= m_levelBgs.size())
		{
			return null;
		}
		return (C2D_ScrollTile) m_levelBgs.elementAt(id);
	}

	/**
	 * 创建一个新的分层视图
	 * 
	 * @param zorder
	 * @return 新的分层视图
	 */
	public C2D_ScrollLayer createLevelView(int zorder)
	{
		C2D_ScrollLayer view = new C2D_ScrollLayer(m_frameManager);
		view.setSize(getWidth(), getHeight());
		addChild(view);
		view.setZOrder(zorder);
		refreshLevelViews();
		return view;
	}

	/**
	 * 创建一个新的分层视图
	 * 
	 * @param view
	 *            需要添加的分层视图
	 * @param zorder
	 *            即将添加的分层视图的ID
	 * @return 新的分层视图
	 */
	public void addLevelView(C2D_ScrollLayer view, int zorder)
	{
		if (view == null || m_levelViews.contains(view))
		{
			return;
		}
		view.setSize(getWidth(), getHeight());
		addChild(view);
		view.setZOrder(zorder);
		refreshLevelViews();
	}

	/**
	 * 刷新分层视图，创建分层视图之后会被自动调用。 当你手动改变了某个分层视图的zorder，你需要调用此方法
	 */
	public void refreshLevelViews()
	{
		orderChildren();
		m_levelViews.clear();
		m_levelBgs.clear();
		int len = m_nodeList.size();
		for (int i = 0; i < len; i++)
		{
			Object obj = m_nodeList.m_datas[i];
			if (obj != null)
			{
				if(obj instanceof C2D_ScrollLayer)
				{
					m_levelViews.addElement(obj);	
				}
				else if(obj instanceof C2D_ScrollTile)
				{
					m_levelBgs.addElement(obj);	
				}
			}
		}
	}

	/**
	 * 获取顶级分层视图
	 * 
	 * @return 顶级分层视图
	 */
	public C2D_ScrollLayer getTopLevelView()
	{
		if (m_levelViews == null || m_levelViews.size() == 0)
		{
			return null;
		}
		return (C2D_ScrollLayer) m_levelViews.elementAt(m_levelViews.size() - 1);
	}

	/**
	 * 获得丢的脚本执行器
	 * 
	 * @return 脚本执行器
	 */
	public C2D_ScriptExcutor getSEX()
	{
		return m_scriptEx;
	}

	/**
	 * 注册精灵加载器，用于加载自定义的精灵对象
	 * 
	 * @param loader加载器
	 */
	public void registerSpriteLoader(C2D_SpriteLoader loader)
	{
		if (loader != null)
		{
			m_spriteLoader = loader;
		}
	}

	public C2D_Sprite loadSprite(C2D_FrameManager frame, short[] anteTypeID,int actionID, int frameID, int atX, int atY, int atZ)
	{
		return new C2D_Sprite(frame, anteTypeID);
	}

	public void afterLoad(C2D_Sprite sprite)
	{
	}

	/**
	 * 遍历所有子节点，执行更新
	 * 
	 * @param stage
	 *            当前场景
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		//执行脚本
		if (m_scriptEx != null)
		{
			C2D_Thread ct = m_scriptEx.getCurrentThread();
			if (ct != null)
			{
				C2D_VM.C2DS_RunScript(ct);
			}
		}
		super.onUpdate(stage);
	}

	/**
	 * 更新完毕自身后处理相机中的物体刷新
	 */
	public void onAutoUpdateOther()
	{
		if(m_camera==null||m_levelViews==null)
		{
			return;
		}
		float cameraL = m_camera.getLeft();
		float cameraR = m_camera.getRight();
		float cameraT = m_camera.getTop();
		float cameraB = m_camera.getBottom();
		int lvNum = m_levelViews.size();
		//更新屏幕对象容器
		for (int i = 0; i < lvNum; i++)
		{
			C2D_ScrollLayer lvView = (C2D_ScrollLayer) m_levelViews.m_datas[i];
			if(lvView!=null)
			{
				lvView.refreshCameraObjs(cameraL, cameraR, cameraT, cameraB);	
			}
		}
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		if (m_levelViews != null)
		{
			m_levelViews.removeAllElements();
		}
		m_levelViews = null;
		if (m_levelBgs != null)
		{
			m_levelBgs.removeAllElements();
		}
		m_levelBgs = null;
		m_spriteLoader = null;
		if (m_scriptEx != null)
		{
			m_scriptEx.doRelease();
			m_scriptEx = null;
		}
	}
}
