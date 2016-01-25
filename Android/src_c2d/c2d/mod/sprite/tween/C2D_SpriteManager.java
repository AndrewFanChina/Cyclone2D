package c2d.mod.sprite.tween;

import java.io.DataInputStream;
import java.io.IOException;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Math;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;
import c2d.mod.sprite.tween.model.C2D_Action;
import c2d.mod.sprite.tween.model.C2D_Frame;
import c2d.mod.sprite.tween.model.C2D_FrameUnit;
import c2d.mod.sprite.tween.model.C2D_FrameUnit_Bitmap;
import c2d.mod.sprite.tween.model.C2D_SpriteClip;
import c2d.mod.sprite.tween.model.C2D_SpriteModel;
import c2d.mod.sprite.tween.model.C2D_TimeLine;
import c2d.plat.gfx.C2D_Graphics;

/**
 * <p>
 * 标题:精灵管理器
 * </p>
 * <p>
 * 描述:负责c2d结构中的精灵动画部分管理，存储了所有的精灵动画数据、图片列表、角色原型列表等列表数据。它是<br>
 * c2d结构的基础部分，由于它负责管理了所有的动画图片、精灵动画数据、声音、角色原型数据的加载、存储与卸载，因此，<br>
 * 它是实际上的动画数据拥有者和统一管理者，而Sprite只是动画数据的使用者，当Sprite创建时，会主动通过SpriteManager<br>
 * 来进行图片、数据的检查与加载。
 * </p>
 * .
 * 
 * @author AndrewFan
 */
public class C2D_SpriteManager extends C2D_Object
{

	/** C2D 文档管理器，精灵管理器的父类句柄. */
	private C2D_FrameManager c2dManager;
	// ============================ 动画数据 ============================
	/** C2D文档中所有动画图片列表 */
	public C2D_SpriteImage spriteImgs[] = null;

	/** 
	 * C2D文件中所有动画基础切片数据， 形式为{图片索引,剪切矩形[X,Y,W,H]}

	 */
	public C2D_SpriteClip spriteClips[] = null;

	/** 分组角色数据列表. */
	private C2D_SpriteModel spriteModels[][] = null;
	/** 所有角色数据总数 */
	private int spriteModelCount = 0;

	/** 对象级数据位置数组(当数据是集合存在时，其有效). */
	private int spriteDatasPos[][] = null;

	/** C2D文件中所有声音名称列表. */
	String soundNames[] = null;
	/** 角色原型列表. */
	private C2D_AnteType anteTypes[][] = null;

	/** 使用声音索引信息标志，如果不使用此项，无法自行载入声音，只能手动载入. */
	boolean useSoundIndexs = false;

	/** 使用攻击方向标志，如果不使用此项，数据将不附带攻击方向信息. */
	boolean useAttackVetor = false;

	/** 使用攻击方向标志，如果不使用此项，数据将不附带攻击方向信息. */
	public byte actionOffsetType = C2D_Sprite.ACTIONOFFSET_NULL;
	/** 数据呈分立状态标志，来自编辑器导出，表示读取动画数据是是从若干分立文件读入而非同一集合文件. */
	private boolean dataDiscrete = false;

	/*--- 动画元素类型定义，引擎内部使用(J2me用).---- */
	/** 未定义类型. */
	public static final short TYPE_NULL = -1;

	/** 图片类型. */
	public static final short TYPE_IMG = (short) (TYPE_NULL + 1);

	/** 矩形填充. */
	public static final short TYPE_RECT = (short) (TYPE_IMG + 1);

	/** 文字. */
	public static final short TYPE_TEXT = (short) (TYPE_RECT + 1);

	/** 矩形边框. */
	public static final short TYPE_FRAME = (short) (TYPE_TEXT + 1);

	/** 逻辑框. */
	public static final short TYPE_LOGIC = (short) (TYPE_FRAME + 1);
	// 动画元素类型分界点定义

	/** 矩形图形边框分界点 */
	static final short SEPERATE_FRAME = 6409;

	/** 逻辑矩形边框开始点 */
	static final short SEPERATE_LOGIC_BEG = 6410;

	/** 逻辑矩形边框结束点 */
	static final short SEPERATE_LOGIC_END = 6665;

	/** 矩形填充分界点 */
	static final short SEPERATE_RECT = 6666;

	/** 文字分界点 */
	static final short SEPERATE_TEXT = 6667;

	/**
	 * 动画构造函数 在构造函数中，会根据传入的C2D管理器对象所拥有的resName来读取动画的基本数据。
	 * 基本数据包括配置信息(混淆、声音、数据分立等开关)、图片列表信息、声音列表信息、所有基础切片信息、SpriteData的长度和位置信息、所有角色原型数据.
	 * 
	 * @param c2dManagerT
	 *            C2DManager C2D文档管理器对象。
	 */
	public C2D_SpriteManager(C2D_FrameManager c2dManagerT)
	{
		c2dManager = c2dManagerT;
		readObject();
	}

	/**
	 * 读取基本信息，包括切片等.
	 * 
	 * @return boolean 返回是否初始化完成
	 */
	private boolean readObject()
	{
		boolean success = true;
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + ".bin";
		DataInputStream dataIn = null;
		byte byteData = 0;
		short shortData = 0;
		int intData = 0;
		try
		{
			// 创建输入流
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			// 读入配置信息------------------------------------------------
			// 读取图片混淆开关
			c2dManager.imgConfused = C2D_IOUtil.readBoolean(c2dManager.imgConfused, dataIn);
			// 读入动作标记信息
			useAttackVetor = C2D_IOUtil.readBoolean(useAttackVetor, dataIn);
			// 读取动作位移类型
			actionOffsetType = C2D_IOUtil.readByte(byteData, dataIn);
			// 读入分立动画数据开关
			dataDiscrete = C2D_IOUtil.readBoolean(dataDiscrete, dataIn);
			// 读入动画图片表
			short len = C2D_IOUtil.readShort(shortData, dataIn);
			if (len > 0)
			{
				if (spriteImgs == null)
				{
					spriteImgs = new C2D_SpriteImage[len];
				}
				String imgFolder = C2D_Consts.STR_IMGS_ + c2dManager.resName + "/";
				for (short i = 0; i < len; i++)
				{
					spriteImgs[i] = new C2D_SpriteImage(dataIn, imgFolder);
					spriteImgs[i].transHadler(this);
				}
			}
			// 读入动画切片表
			spriteClips = null;
			short all_clips_count = C2D_IOUtil.readShort(shortData, dataIn);
			if (all_clips_count > 0)
			{
				spriteClips = new C2D_SpriteClip[all_clips_count];
				for (short i = 0; i < all_clips_count; i++)
				{
					spriteClips[i] = new C2D_SpriteClip(this, dataIn);
				}
			}
			// 读入动画分组信息
			short nbFolders = C2D_IOUtil.readShort(shortData, dataIn);// 文件夹数目
			spriteModelCount = 0;
			if (nbFolders > 0)
			{
				spriteModels = new C2D_SpriteModel[nbFolders][];
				for (short i = 0; i < nbFolders; i++)
				{
					short nbSprites = C2D_IOUtil.readShort(shortData, dataIn);// 角色数目
					spriteModels[i] = new C2D_SpriteModel[nbSprites];
					spriteModelCount += nbSprites;
				}
			}
			// 读入角色数据位置信息
			if (!dataDiscrete)
			{
				spriteDatasPos = new int[nbFolders][];
				for (short i = 0; i < nbFolders; i++)
				{
					spriteDatasPos[i] = new int[spriteModels[i].length];
					for (int j = 0; j < spriteDatasPos[i].length; j++)
					{
						spriteDatasPos[i][j] = C2D_IOUtil.readInt(intData, dataIn);
					}
				}
			}
			// 角色原型信息
			len = C2D_IOUtil.readShort(shortData, dataIn);
			anteTypes = new C2D_AnteType[len][];
			for (int i = 0; i < len; i++)
			{
				short nbAntetypes = C2D_IOUtil.readShort(shortData, dataIn);
				anteTypes[i] = new C2D_AnteType[nbAntetypes];
				for (int j = 0; j < nbAntetypes; j++)
				{
					anteTypes[i][j] = new C2D_AnteType(dataIn);
				}
			}
		}
		catch (Exception e)
		{
			success = false;
			C2D_Debug.log("[loadAnimManager: " + fileName + " error " + e + "]");
			e.printStackTrace();
		}
		finally
		{
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException ex)
				{
				}
				dataIn = null;
			}
		}
		return success;
	}

	/**
	 * 根据二维ID数组[idCount][folderID,spriteID]载入角色数据 如果传入为null时将执行全部载入.
	 * 
	 * @param ids
	 *            int[][] 角色文件夹ID、角色ID数组
	 */
	private void loadSpriteData(int ids[][])
	{
		if (spriteModels == null)
		{
			return;
		}
		// 开始读入
		DataInputStream dataIn = null;
		try
		{
			// 先创建或者排序加载ID
			if (ids == null)
			{
				ids = new int[spriteModelCount][2];
				int idsID = 0;
				for (int i = 0; i < ids.length; i++)
				{
					for (int j = 0; j < spriteModels[i].length; j++)
					{
						ids[idsID][0] = i;
						ids[idsID][1] = j;
						idsID++;
					}
				}
			}
			else
			{
				C2D_Math.orderArrayMinByAll(ids, 0, ids.length - 1);
			}
			// 开始读取，如果是集合数据，则创建输入流
			if (!dataDiscrete)
			{
				dataIn = C2D_IOUtil.getDataInputStream(C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + "_sds.bin");
			}
			// 按照ID顺序读取
			int lastPosition = 0;
			for (int idI = 0; idI < ids.length; idI++)
			{
				int folderID = ids[idI][0];
				int spriteID = ids[idI][1];
				// 调整输入流
				if (dataDiscrete)
				{
					dataIn = C2D_IOUtil.getDataInputStream(C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + "_sds_" + folderID + "_" + spriteID + ".bin");
				}
				else
				{
					int dataPosition = spriteDatasPos[folderID][spriteID];
					dataIn.skip(dataPosition - lastPosition);
				}
				if (spriteModels[folderID][spriteID] == null)
				{
					spriteModels[folderID][spriteID] = new C2D_SpriteModel(this);
					spriteModels[folderID][spriteID].readObject(dataIn);
					spriteModels[folderID][spriteID].transHadler(this);
				}
				// 关闭分立源
				if (dataDiscrete)
				{
					if (dataIn != null)
					{
						dataIn.close();
						dataIn = null;
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
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException ex)
				{
				}
				dataIn = null;
			}
		}

	}

	/**
	 * 根据sprite获取所有用到的图片SpriteImage，并且增加SpriteImage的使用计数，
	 * 如果SpriteImage还没有加载，则自动读取到内存。
	 * 
	 * @param sprite
	 *            指定的角色对象
	 */
	void spriteUseImages(C2D_SpriteProto sprite)
	{
		if (sprite == null)
		{
			return;
		}
		short imgIndexs[] = sprite.getUsedImgIDs();
		if (imgIndexs == null)
		{
			return;
		}
		C2D_AnteType anteType = sprite.m_anteType;
		for (int j = 0; j < imgIndexs.length; j++)
		{
			short id = imgIndexs[j];
			if (anteType != null)
			{
				id = anteType.getmappedID(id);
			}
			if (spriteImgs != null && id >= 0 && id < spriteImgs.length)
			{
				if (spriteImgs[id] != null && !spriteImgs[id].isLoaded())
				{
					spriteImgs[id].loadImage();
				}
				if (spriteImgs[id] != null)
				{
					spriteImgs[id].useImage();
				}
			}
		}
	}

	/**
	 * 根据sprite释放所有用到的图片SpriteImage，即减少所使用的所有KImage的使用计数，
	 * 如果SpriteImage使用计数归0，则将SpriteImage的内部数据从内存中删除。
	 * 
	 * @param sprite
	 *            指定的角色对象
	 */
	void spriteUnuseImages(C2D_SpriteProto sprite)
	{
		if (sprite == null)
		{
			return;
		}
		short imgIndexs[] = sprite.getUsedImgIDs();
		C2D_AnteType anteType = sprite.m_anteType;
		if (spriteImgs == null || imgIndexs == null)
		{
			return;
		}
		for (int i = 0; i < imgIndexs.length; i++)
		{
			short index = imgIndexs[i];
			if (anteType != null)
			{
				index = anteType.getmappedID(index);
			}
			if (index < spriteImgs.length && index >= 0)
			{
				spriteImgs[index].unuseImage();
			}
		}
	}

	/**
	 * 根据spriteFolderID和spriteID获取SpriteModel，并且增加SpriteModel的使用计数，
	 * 如果SpriteModel不存在，则创建新的实例。
	 * 
	 * @param spriteFolderID
	 *            角色文件件ID
	 * @param spriteID
	 *            角色ID
	 * @return SpriteModel 返回角色数据
	 */
	C2D_SpriteModel spriteUseData(int spriteFolderID, int spriteID)
	{
		if (spriteModels == null || spriteFolderID < 0 || spriteFolderID >= spriteModels.length)
		{
			return null;
		}
		if (spriteModels[spriteFolderID][spriteID] == null)
		{
			loadSpriteData(new int[][]
			{ new int[]
			{ spriteFolderID, spriteID } });
		}
		if (spriteModels[spriteFolderID][spriteID] != null)
		{
			spriteModels[spriteFolderID][spriteID].useData();
		}
		return spriteModels[spriteFolderID][spriteID];
	}

	/**
	 * 据spriteFolderID和spriteID释放SpriteModel。首先减少SpriteData的使用计数
	 * 如果SpriteModel使用计数归0，则从内存中删除此SpriteModel.
	 * 
	 * @param spriteID
	 *            角色ID
	 */
	void spriteUnuseData(int spriteFolderID, int spriteID)
	{
		if (spriteModels == null || spriteFolderID < 0 || spriteFolderID >= spriteModels.length)
		{
			return;
		}
		if (spriteModels[spriteFolderID] != null)
		{
			if (spriteModels[spriteFolderID][spriteID].unuseData())
			{
				spriteModels[spriteFolderID][spriteID] = null;
			}
		}
	}

	/**
	 * 释放资源，包括角色动画数据、图片列表、角色原型列表、声音列表等所有信息.
	 */
	@Override
	public void onRelease()
	{
		// 释放数据
		spriteClips = null;
		if (spriteModels != null)
		{
			for (int i = 0; i < spriteModels.length; i++)
			{
				if (spriteModels[i] != null)
				{
					for (int j = 0; j < spriteModels[i].length; j++)
					{
						if (spriteModels[i][j] != null)
						{
							spriteModels[i][j].doRelease(this);
							spriteModels[i][j] = null;
						}
					}
					spriteModels[i] = null;
				}
			}
		}
		spriteModels = null;
		spriteDatasPos = null;
		if (soundNames != null)
		{
			for (int i = 0; i < soundNames.length; i++)
			{
				soundNames[i] = null;
			}
		}
		soundNames = null;
		if (anteTypes != null)
		{
			for (int i = 0; i < anteTypes.length; i++)
			{
				for (int j = 0; j < anteTypes[i].length; j++)
				{
					anteTypes[i][j].doRelease(this);
					anteTypes[i][j] = null;
				}
				anteTypes[i] = null;
			}
		}
		anteTypes = null;
		// 释放图片
		if (spriteImgs != null)
		{
			for (int i = 0; i < spriteImgs.length; i++)
			{
				if (spriteImgs[i] != null)
				{
					spriteImgs[i].doRelease(this);
					spriteImgs[i] = null;
				}
			}
			spriteImgs = null;
		}
	}

	/* ==============================数据获得============================== */
	/**
	 * 根据角色原型文件件ID和角色原型ID获得角色原型数据.
	 * 
	 * @param anteTypeID
	 *            int 根据角色原型ID
	 * @return AnteType 角色原型对象
	 */
	public C2D_AnteType getAnteType(int antetypeFolderID, int anteTypeID)
	{
		if (anteTypes == null || antetypeFolderID < 0 || antetypeFolderID >= anteTypes.length)
		{
			return null;
		}
		return anteTypes[antetypeFolderID][anteTypeID];
	}

	/**
	 * 根据角色原型对象返回其角色原型ID.
	 * 
	 * @param antyType
	 *            AnteType角色原型对象
	 * @return int 角色原型ID,如果在AnimManager中不含有此角色原型则返回-1
	 */
	public int getAnteTypeID(C2D_AnteType antyType)
	{
		if (anteTypes == null || antyType == null)
		{
			return -1;
		}
		for (int i = 0; i < anteTypes.length; i++)
		{
			if (anteTypes[i].equals(antyType))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获得所包含的SpriteModel的个数.
	 * 
	 * @return int 如果spriteModels对象为null，则返回-1
	 */
	public int getSpriteModelCount()
	{
		return spriteModelCount;
	}
	/**
	 * 获得动画的逻辑框，返回获得的逻辑框数目，以[x、y、w、h、x、y、w、h...]的形式将数据存储在数组中
	 * 
	 * @param dataBoxs
	 *            int[] 传入的用于数据存储的数组，长度不够时会返回-2
	 * @param logicType
	 *            int 逻辑框类型[0-255]之间
	 * @param actorId
	 *            int 角色ID
	 * @param actionId
	 *            int 动作ID
	 * @param frameId
	 *            int 帧ID
	 * @param x
	 *            int 传入角色的当前世界坐标X，返回的坐标同样相对于世界
	 * @param y
	 *            int 传入角色的当前世界坐标Y，返回的坐标同样相对于世界
	 * @param flipX
	 *            boolean 是否水平翻转
	 * @return int 返回获得的逻辑框数目，发生错误返回-1，数据缓冲不足返回-2
	 */
	public int getLogicBox(int dataBoxs[], int logicType, int actorId, int actionId, int frameId, float x, float y, boolean flipX)
	{
//		if (spriteModels == null || actorId >= spriteModels.length || actorId < 0)
//		{
//			C2D_Debug.log("null pointer or Array index out of bounds in getLogicBox");
//			return -1;
//		}
//		C2D_SpriteModel actorData = spriteModels[actorId][actionId];
//		if (dataBoxs == null || logicType < C2D_Sprite.LOGIC_BODY || logicType > C2D_Sprite.LOGIC_END)
//		{
//			return -1;
//		}
//		// 容错
//		if (actorData == null || spriteClips == null)
//		{
//			C2D_Debug.log("null pointer in drawKeyFrame");
//			return -1;
//		}
//		if (actionId < 0 || (actionId > 0 && actionId >= actorData.getActionCount()) || frameId < 0 || (frameId > 0 && frameId >= actorData.getFrameCount(actionId)))
//		{
//			C2D_Debug.log("actionId or frameId - Array index out of bounds");
//			return -1;
//		}
//		// 帧信息整理
//		int baseFrameID = actorData.getFrameFlag_BaseID(actionId, frameId);
//		byte frameFlag = actorData.getFrameFlag_Trans(actionId, frameId);
//		if (flipX)
//		{
//			frameFlag = C2D_Graphics.TRANS_ARRAY[frameFlag][C2D_Graphics.TRANS_HORIZENTAL];
//		}
//		int id = baseFrameID << 1;
//		short clipsBegin = actorData.baseFrames_pos[id];
//		short clipsCount = actorData.baseFrames_pos[id + 1];
//		// 逻辑框数目
		int findCount = 0;
//		for (int i = 0; i < clipsCount; i++)
//		{
//			// 切片信息整理
//			int cid = clipsBegin + (i << 2);// * C2D_SpriteData.BASE_FRAME_STEP
//			int clipID = actorData.baseFrames[cid];
//			int off_x = actorData.baseFrames[cid + 1];
//			int off_y = actorData.baseFrames[cid + 2];
//			byte clip_Flag = (byte) actorData.baseFrames[cid + 3];
//			if (clipID >= spriteClips.length)
//			{
//				C2D_Debug.log("clipID - Array index out of bounds");
//				continue;
//			}
//			int clipIDT = clipID * CLIP_CHUNK_LEN;
//			int img_ID = spriteClips[clipIDT + 0];
//			int _w = spriteClips[clipIDT + 3];
//			int _h = spriteClips[clipIDT + 4];
//			if (img_ID < SEPERATE_LOGIC_BEG || img_ID > SEPERATE_LOGIC_END || img_ID - SEPERATE_LOGIC_BEG != logicType)
//			{
//				continue;
//			}
//			// 帧翻转处理
//			int cx = x;
//			int cy = y;
//			if ((frameFlag & C2D_Graphics.TRANS_HORIZENTAL) != 0)
//			{
//				off_x = -off_x;
//			}
//			if ((frameFlag & C2D_Graphics.TRANS_VERTICAL) != 0)
//			{
//				off_y = -off_y;
//			}
//			if ((frameFlag & C2D_Graphics.TRANS_FOLD_RT2LB) != 0)
//			{
//				int off_xo = off_x;
//				off_x = off_y;
//				off_y = off_xo;
//			}
//			clip_Flag = C2D_Graphics.TRANS_ARRAY[clip_Flag][frameFlag];
//			int wReal = _w;
//			int hReal = _h;
//			if ((clip_Flag & C2D_Graphics.TRANS_FOLD_RT2LB) != 0)
//			{
//				wReal = _h;
//				hReal = _w;
//			}
//			cx += off_x - wReal / 2;
//			cy += off_y - hReal / 2;
//			// 存储数据
//			if (dataBoxs.length < (findCount << 1))
//			{
//				return -2;
//			}
//			int dbid = (findCount << 2);
//			dataBoxs[dbid + 0] = cx;
//			dataBoxs[dbid + 1] = cy;
//			dataBoxs[dbid + 2] = cx + wReal;
//			dataBoxs[dbid + 3] = cy + hReal;
//			findCount++;
//		}
		return findCount;
	}
	/* ==============================关键帧绘制============================== */
	/**
	 * Sprite关键帧绘制，使用sprite当前动作和帧ID 所有的sprite关键帧绘制都以中心对齐的方式。.
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param x
	 *            float 目标X坐标
	 * @param y
	 *            float 目标Y坐标
	 * @param flipX
	 *            boolean 是否横向翻转
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, float zoomLevel, float x, float y, boolean flipX)
	{
		if (sprite == null)
		{
			return;
		}
		drawSpriteKeyFrame(sprite, sprite.m_actionID, sprite.m_frameID, zoomLevel, x, y, flipX, 0, Integer.MAX_VALUE, null, sprite.m_degree);
	}

	/**
	 * 带限制区域的Sprite关键帧绘制，使用Sprite当前动作和播放进度 所有的Sprite关键帧绘制都以中心对齐的方式。
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param x
	 *            float 目标X坐标
	 * @param y
	 *            float 目标Y坐标
	 * @param flipX
	 *            boolean 是否横向翻转
	 * @param limitRegion
	 *            short[] 限制区域，【X、Y、W、H】形式的short数组
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, float zoomLevel, float x, float y, boolean flipX, short limitRegion[])
	{
		drawSpriteKeyFrame(sprite, sprite.m_actionID, sprite.m_frameID, zoomLevel, x, y, flipX, 0, Integer.MAX_VALUE, limitRegion, sprite.m_degree);
	}

	/**
	 * 指定动作和帧ID的Sprite关键帧绘制 所有的Sprite关键帧绘制都以中心对齐的方式。
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param actionID
	 *            int 指定的动作ID
	 * @param playTime
	 *            int 指定播放进度
	 * @param x
	 *            int 目标X坐标
	 * @param y
	 *            int 目标Y坐标
	 * @param flipX
	 *            boolean 是否横向翻转
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, int actionID, int playTime, float zoomLevel, int x, int y, boolean flipX)
	{
		drawSpriteKeyFrame(sprite, actionID, playTime, zoomLevel, x, y, flipX, 0, Integer.MAX_VALUE, null, sprite.m_degree);
	}

	/**
	 * 指定切片ID区间的Sprite关键帧绘制，使用Sprite当前动作和播放进度， 所有的sprite关键帧绘制都以中心对齐的方式。
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param x
	 *            int 目标X坐标
	 * @param y
	 *            int 目标Y坐标
	 * @param flipX
	 *            boolean 是否横向翻转
	 * @param timeLineStart
	 *            int 起始时间轴ID(包含)
	 * @param timeLineEnd
	 *            int 结束时间轴ID(包含)
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, float zoomLevel, float x, float y, boolean flipX, int timeLineStart, int timeLineEnd)
	{
		drawSpriteKeyFrame(sprite, sprite.m_actionID, sprite.m_frameID, zoomLevel, x, y, flipX, timeLineStart, timeLineEnd, null, sprite.m_degree);
	}

	/**
	 * 带限制区域的，指定动作、帧ID的，可选切片区间的Sprite关键帧绘制，
	 * 使用Sprite当前动作和播放进度，所有的Sprite关键帧绘制都以中心对齐的方式。.
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param zoomLevel
	 *            float 缩放比率
	 * @param actionId
	 *            int 指定的动作ID
	 * @param frameID
	 *            int 指定播放进度
	 * @param xS
	 *            float 目标屏幕X坐标
	 * @param yS
	 *            float 目标屏幕Y坐标
	 * @param flipX
	 *            boolean 是否横向翻转
	 * @param timeLineStart
	 *            int 起始时间轴ID(包含)
	 * @param timeLineEnd
	 *            int 结束时间轴ID(包含)
	 * @param limitRegion
	 *            short[] 限制区域，【X、Y、W、H】形式的short数组
	 * @param angleDegree
	 *            float 旋转角度
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, int actionId, float frameID, float zoomLevel, float xS, float yS, boolean flipX, int timeLineStart, int timeLineEnd, short limitRegion[], float angleDegree)
	{
		// if (sprite == null)
		// {
		// return;
		// }
		C2D_SpriteModel spriteModel = sprite.m_spriteData;
		// if (actionId < 0 || (actionId > 0 && actionId >= spriteModel.Count())
		// || playTime < 0 || (playTime >=
		// spriteModel.getActionTimeSpan(actionId)))
		// {
		// C2D_Debug.log("==[actionId or playTime out of bounds]==");
		// return;
		// }
		// 要绘制的动作
		C2D_Action action = spriteModel.sonList[actionId];
		// if(action==null)
		// {
		// return;
		// }
		// 计算要绘制的时间轴数
		int timeSpan = action.sonList.length;
		if (timeLineEnd <= 0 || timeLineEnd >= timeSpan)
		{
			timeLineEnd = timeSpan - 1;
		}
		// 开始遍历绘制
		C2D_Graphics.glPushMatrix();
		if (flipX)
		{
			C2D_Graphics.glTranslatef(xS, 0, 0);
			C2D_Graphics.glScalef(-1, 1, 1);
			C2D_Graphics.glTranslatef(-xS, 0, 0);
		}
		for (int i = timeLineStart; i <= timeLineEnd; i++)
		{
			C2D_TimeLine timeLine = action.sonList[i];
			C2D_Frame frame = timeLine.getFrameByX(frameID);
			if (frame != null)
			{
				int clipsCount = frame.sonList.length;
				for (int k = 0; k < clipsCount; k++)
				{
					// 后续添加类型
					C2D_FrameUnit_Bitmap son = (C2D_FrameUnit_Bitmap) frame.sonList[k];
					if (son.scaleX != 0.0f && son.scaleY != 0.0f)
					{
						son.clipElement.drawWithTransform(zoomLevel, xS, yS, son.mTransform, son.mParentAlpha, angleDegree);
					}
				}
			}
		}
		C2D_Graphics.glPopMatrix();
	}

	/**
	 * 预加载资源，这函数用来在进行大规模载入Sprite之前，统一载入所有的角色数据(SpriteModel)
	 * 当数据呈非分立状态时，动画数据文件往往较大，如果我们进行大规模Sprite载入，往往需要频繁读取此动画数据文件，
	 * 造成效率上的浪费，为了避免这种情况，我们可以预先一次性将所有此AnimManager中的动画数据加载到内存，当大规模
	 * 的Sprite初始化完成之后，我们再使用preLoadResEnd释放不需要的角色数据，这样可以节省效率。.
	 */
	public void preLoadResBegin()
	{
		if (!dataDiscrete)
		{
			this.loadSpriteData(null);
		}
	}

	/**
	 * 卸载预加载资源，删除不需要的(即引用计数为0)角色数据。 详情见preLoadResBegin()函数.
	 */
	public void preLoadResEnd()
	{
		if (spriteModels == null)
		{
			return;
		}
		for (int i = 0; i < spriteModels.length; i++)
		{
			for (int j = 0; j < spriteModels[i].length; j++)
			{
				if (spriteModels[i][j] != null && spriteModels[i][j].getUsedTime() <= 0)
				{
					spriteModels[i][j].doRelease(this);
					spriteModels[i][j] = null;
				}
			}
		}
	}

	/**
	 * 设置动画时间
	 * 
	 * @param sprite
	 *            Sprite 指定的角色
	 * @param actionId
	 *            int 指定的动作ID
	 * @param frameID
	 *            int 指定播放进度
	 */
	public void setSpriteTime(C2D_SpriteProto sprite, int actionId, float frameID)
	{
		if (sprite == null)
		{
			return;
		}
		C2D_SpriteModel spriteModel = sprite.m_spriteData;
		if (actionId < 0 || (actionId > 0 && actionId >= spriteModel.getActionCount()) || frameID < 0 || (frameID >= spriteModel.getFrameCount(actionId)))
		{
			C2D_Debug.log("==[actionId or playTime out of bounds]==");
			return;
		}
		// 要绘制的动作
		C2D_Action action = spriteModel.GetSon(actionId);
		if (action == null)
		{
			return;
		}
		// 计算要绘制的时间轴数
		int timeSpan = action.Count();
		// 开始遍历更新
		for (int i = 0; i < timeSpan; i++)
		{
			C2D_TimeLine timeLine = action.GetSon(i);
			C2D_Frame frame = timeLine.getFrameByX(frameID);
			if (frame != null)
			{
				int frameCount = frame.Count();
				for (int k = 0; k < frameCount; k++)
				{
					C2D_FrameUnit son = frame.GetSon(k);
					son.setTransform(frameID);
				}
			}
		}
	}
}
