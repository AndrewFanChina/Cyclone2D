package c2d.mod.map.tile;

import java.io.DataInputStream;
import java.io.IOException;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Math;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_MixedImage;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_GdiImage;


/**
 * <p>
 * 标题:Map类
 * </p>
 * <p>
 * 描述:Map类主要负责地图资源管理与地图绘制<br>
 * 所有的地图资源都应存放在c2d文件夹中。<br>
 * mapImgs.bin文件存放了地图所有使用到的图片列表。
 * map_xx.bin存放了第xx关的地图。mapStyle_xx.bin存放了第xx种地图风格。
 * 地图风格其实就是一些图片和地图块组合信息，这些块数不能超过一定的数量，因此用地图风格来分类有助于<br>
 * 实现这些信息的管理和存放，使得在载入某一关卡时，明确知道值只需要加载哪几张图片，同时也可以使用单<br>
 * 个字节来记录地图块ID。 Map类提供了若干静态方法来加载会绘制，你不需要拥有它的实例，却能够方便地使用它。
 * </p>
 * 
 * @author AndrewFan
 */
public class C2D_TileMap extends C2D_Object
{
	/**
	 * 构造一个方格地图对象
	 * @param folderName 地图所在的资源文件夹
	 * @param mapName 地图资源文件前缀名
	 * @param mapID 地图ID
	 * @param stageID 场景ID
	 */
	public C2D_TileMap(String folderName, String mapName, int mapID, int stageID)
	{
		m_folderName = folderName;
		m_mapName = mapName;
		m_mapID = mapID;
		m_stageID = stageID;
		load();
	}

	/** 地图资源名称. */
	private static String m_mapName;
	/** 地图资源所在文件夹名称. */
	public String m_folderName;
	public int m_mapID;
	public int m_stageID;
	public C2D_TileMapData actors_BACKGROUND[];
	public C2D_TileMapData actors_COMMON_OBJ[];
	public C2D_TileMapData actors_NPC_EVENT[][];
	/** 脚本ID. */
	public short m_scriptIDs[];

	private C2D_GdiImage m_mapBuffer;
	// 图层位标记
	private static final byte LEVELFLAG_PHYSIC = 1;// 图层定义---物理标记层

	/** 图层定义---底层地形层 */
	private static final byte LEVELFLAG_TILE_BG = 2;

	/** 图层定义---融合地形层 */
	private static final byte LEVELFLAG_TILE_SUR = 4;

	/** 图层定义---对象地形层 */
	private static final byte LEVEL_BG_OJECT = 8;

	/** 图层定义---无关对象层 */
	private static final byte LEVELFLAG_COMMON_OBJ = 16;

	/** 图层定义---角色事件层 */
	private static final byte LEVELFLAG_NPC_EVENT = 32;

	/** 当前地图X方向的地图方格数目. */
	public int tile_nb_x;

	/** 当前地图Y方向的地图方格数目. */
	public int tile_nb_y;

	/** 当前地图的地图方格宽度. */
	public short tile_width;

	/** 当前地图的地图方格高度. */
	public short tile_height;

	/** 当前地图的世界宽度. */
	public int world_width;

	/** 当前地图的世界高度. */
	public int world_height;

	/** 图层标志 */
	private byte LEVEL_FLAG;

	/** 地图颜色 */
	private int mapColor;

	/** 地图风格索引 */
	private short mapStyle;

	/** 物理标记层数据 */
	private short mapData_FLAG_PHY[];

	/** 底层地形层数据 */
	private short mapData_TILE_BG[];

	/** 融合地形层数据 */
	private short mapData_TILE_SUR[];

	/** 底层地形层翻转数据 */
	protected static byte mapData_TILE_BG_Flag[];

	/** 融合地形层翻转数据 */
	protected static byte mapData_TILE_SUR_Flag[];

	/** 地图图片集合 */
	private C2D_MixedImage mapImgs[];

	/** 地图切片集合 */
	private short mapClips[];

	/** 图片映射表 */
	private short mapImgsMappedID[];

	/** 地图切片长度[imgID,x,y,w,h,flag] */
	private static final int MAP_CLIP_LEN = 6;

	/**
	 * 根据关卡载入地图资源，将角色信息以[anteTypeID, actorID, posX, posY, npcID, actionID,
	 * frameID, isAlive]
	 * 的short数组形式存放在Vector中返回，以便于后续的角色初始化。传入3个Vector容器便于存放角色信息。
	 */
	public void load()
	{
		DataInputStream dataIn = null;
		short shortData = 0;
		byte byteData = 0;
		/************************************ 载入全局信息 ************************************/
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + m_mapName + "_mapimgs.bin";
		if (mapImgs == null)
		{
			try
			{
				dataIn = C2D_IOUtil.getDataInputStream(fileName);
				short count = C2D_IOUtil.readShort(shortData, dataIn);
				mapImgs = new C2D_MixedImage[count];
				for (int i = 0; i < count; i++)
				{
					mapImgs[i] = new C2D_MixedImage(dataIn, m_folderName);
				}
				C2D_Debug.log("[success to load imgs]");
			}
			catch (Exception e)
			{
				C2D_Debug.log("[==fail to load map imgs:" + e + "==]");
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
		/************************************ 载入关卡数据 ************************************/
		fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + m_mapName + "_map_" + (m_mapID) + ".bin";
		try
		{
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			mapColor = C2D_IOUtil.readInt(mapColor, dataIn); // 读入地图颜色
			mapStyle = C2D_IOUtil.readShort(mapStyle, dataIn); // 读入地图风格索引
			// 读入地砖尺寸
			tile_width = C2D_IOUtil.readShort(tile_width, dataIn);
			tile_height = C2D_IOUtil.readShort(tile_height, dataIn);
			// 读入地图尺寸
			tile_nb_x = C2D_IOUtil.readShort((short) tile_nb_x, dataIn);
			tile_nb_y = C2D_IOUtil.readShort((short) tile_nb_y, dataIn);
			world_width = tile_nb_x * tile_width;
			world_height = tile_nb_y * tile_height;
			// 读入图层标记
			LEVEL_FLAG = C2D_IOUtil.readByte(LEVEL_FLAG, dataIn);
			// 映射表信息
			short mappedTable[][] = null;
			// 地图数据-------------------------------------------------------

			// 物理标记层
			if ((LEVEL_FLAG & LEVELFLAG_PHYSIC) != 0)
			{
				mapData_FLAG_PHY = new short[tile_nb_x * tile_nb_y];
				int id = 0;
				for (int i = 0; i < tile_nb_x; i++)
				{
					for (int j = 0; j < tile_nb_y; j++)
					{
						mapData_FLAG_PHY[id++] = C2D_IOUtil.readShort(shortData, dataIn);
					}
				}
			}
			// 底层地形层
			if ((LEVEL_FLAG & LEVELFLAG_TILE_BG) != 0)
			{
				mapData_TILE_BG = new short[tile_nb_x * tile_nb_y];
				mapData_TILE_BG_Flag = new byte[mapData_TILE_BG.length];
				int id = 0;
				for (int i = 0; i < tile_nb_x; i++)
				{
					for (int j = 0; j < tile_nb_y; j++)
					{
						mapData_TILE_BG[id] = C2D_IOUtil.readShort(shortData, dataIn);
						if (mapData_TILE_BG[id] != 0)
						{
							mapData_TILE_BG_Flag[id] = C2D_IOUtil.readByte(byteData, dataIn);
						}
						id++;
					}
				}
			}
			// 融合地形层
			if ((LEVEL_FLAG & LEVELFLAG_TILE_SUR) != 0)
			{
				mapData_TILE_SUR = new short[tile_nb_x * tile_nb_y];
				mapData_TILE_SUR_Flag = new byte[mapData_TILE_SUR.length];
				int id = 0;
				for (int i = 0; i < tile_nb_x; i++)
				{
					for (int j = 0; j < tile_nb_y; j++)
					{
						mapData_TILE_SUR[id] = C2D_IOUtil.readShort(shortData, dataIn);
						if (mapData_TILE_SUR[id] != 0)
						{
							mapData_TILE_SUR_Flag[id] = C2D_IOUtil.readByte(byteData, dataIn);
						}
						id++;
					}
				}
			}
			// 对象类数据----------------------------------------------------------------
			// 对象地形层
			if ((LEVEL_FLAG & LEVEL_BG_OJECT) != 0)
			{
				int count = C2D_IOUtil.readShort(shortData, dataIn);
				actors_BACKGROUND = new C2D_TileMapData[count];
				for (int i = 0; i < count; i++)
				{
					actors_BACKGROUND[i] = readMapActorData(dataIn);
				}
			}
			// 无关对象层
			if ((LEVEL_FLAG & LEVELFLAG_COMMON_OBJ) != 0)
			{
				int count = C2D_IOUtil.readShort(shortData, dataIn);
				actors_COMMON_OBJ = new C2D_TileMapData[count];
				for (int i = 0; i < count; i++)
				{
					actors_COMMON_OBJ[i] = readMapActorData(dataIn);
				}
			}
			// 场景个数
			short stageCount = C2D_IOUtil.readShort(shortData, dataIn);
			// 角色事件层
			if ((LEVEL_FLAG & LEVELFLAG_NPC_EVENT) != 0)
			{
				actors_NPC_EVENT = new C2D_TileMapData[stageCount][];
				C2D_TileMapData mapActorData = new C2D_TileMapData();
				for (int iS = 0; iS < stageCount; iS++)
				{
					// 读取场景脚本
					int lenKss = C2D_IOUtil.readByte(byteData, dataIn) & 0xFF;
					if (iS == m_stageID)
					{
						m_scriptIDs = new short[lenKss];
						for (int i = 0; i < lenKss; i++)
						{
							m_scriptIDs[i] = C2D_IOUtil.readShort(shortData, dataIn);
						}
					}
					else
					{
						dataIn.skip(lenKss * 2);
					}
					// 读取NPC数据和脚本
					int count = C2D_IOUtil.readShort(shortData, dataIn);
					actors_NPC_EVENT[m_stageID] = new C2D_TileMapData[count];
					for (int i = 0; i < count; i++)
					{
						if (iS == m_stageID)
						{
							actors_NPC_EVENT[m_stageID][i] = readMapActorData(dataIn);
						}
						else
						{
							readMapActorData(dataIn);
						}

					}
				}
			}
			// 载入图片映射表
			for (int iS = 0; iS < stageCount; iS++)
			{
				short count = C2D_IOUtil.readShort(shortData, dataIn);
				if (iS == m_stageID)
				{
					mappedTable = new short[count][2];
					for (int i = 0; i < count; i++)
					{
						mappedTable[i][0] = C2D_IOUtil.readShort(mappedTable[i][0], dataIn);
						mappedTable[i][1] = C2D_IOUtil.readShort(mappedTable[i][1], dataIn);
					}
				}
				else
				{
					dataIn.skip(count * 4);
				}
			}
			// 处理映射表
			mapImgsMappedID = new short[mapImgs.length];
			for (int i = 0; i < mapImgsMappedID.length; i++)
			{
				mapImgsMappedID[i] = -1;
			}
			for (int i = 0; i < mappedTable.length; i++)
			{
				short fromID = mappedTable[i][0];
				short toID = mappedTable[i][1];
				if (fromID >= 0 && fromID < mapImgsMappedID.length && toID >= 0 && toID < mapImgsMappedID.length)
				{
					mapImgsMappedID[fromID] = toID;
				}
			}
			C2D_Debug.log("[success to load map]");
		}
		catch (Exception e)
		{
			C2D_Debug.log("[==fail to load map " + fileName + e + "==]");
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
		// 载入风格数据 ----------------------------------------------------
		fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + m_mapName + "_mapstyle_" + mapStyle + ".bin";
		short imgsUsed[] = null;
		try
		{
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			int count = C2D_IOUtil.readShort(shortData, dataIn);
			mapClips = new short[count * MAP_CLIP_LEN];
			int j = 0;
			for (int i = 0; i < count; i++)
			{
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // ID
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // 矩形颜色高16位或者切片X坐标
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // 矩形颜色低16位或者切片Y坐标
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // 区域宽度
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // 区域高度
				mapClips[j++] = (short) (C2D_IOUtil.readByte(byteData, dataIn) & 0xFF); // 翻转标志(对矩形无效)
			}
			// 读取当前地形使用到的图片
			count = C2D_IOUtil.readShort(shortData, dataIn);
			imgsUsed = new short[count];
			for (int i = 0; i < count; i++)
			{
				imgsUsed[i] = C2D_IOUtil.readShort(imgsUsed[i], dataIn);
			}
			C2D_Debug.logC2D ("[success to load mapStyle]");
		}
		catch (Exception e)
		{
			C2D_Debug.logErr("[==fail to load mapStyle:" + e + "==]");
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
		// 应用映射表
		for (int i = 0; i < mapImgsMappedID.length; i++)
		{
			if (mapImgsMappedID[i] >= 0)
			{
				for (int j = 0; j < imgsUsed.length; j++)
				{
					if (imgsUsed[j] == i)
					{
						imgsUsed[j] = mapImgsMappedID[i];
						break;
					}
				}
			}
		}
		// 载入映射后的地图图片
		if (mapImgs != null && imgsUsed != null)
		{
			for (int i = 0; i < imgsUsed.length; i++)
			{
				int id = imgsUsed[i];
				if (mapImgs != null && id >= 0 && id < mapImgs.length && mapImgs[id] != null && !mapImgs[id].isLoaded())
				{
					mapImgs[id].loadImage();
				}
				else
				{
					C2D_Debug.logErr("error img ID " + id);
				}
			}
		}
	}

	/**
	 * 读取一个地图活动对象单元数据.
	 * 
	 * @param actors_list
	 *            活动对象数据的存放容器
	 * @param dataIn
	 *            DataInputStream 输入流
	 * @throws Exception
	 *             the exception
	 */
	private C2D_TileMapData readMapActorData(DataInputStream dataIn) throws Exception
	{
		byte byteData = 0;
		short shortData = 0;
		C2D_TileMapData mapActorData = new C2D_TileMapData();
		mapActorData.posX = C2D_IOUtil.readByte(byteData, dataIn) & 0xFF;
		mapActorData.posY = C2D_IOUtil.readByte(byteData, dataIn) & 0xFF;
		mapActorData.npcID = C2D_IOUtil.readShort(shortData, dataIn);
		mapActorData.anteTypeID = C2D_IOUtil.readShort(shortData, dataIn);
		//#if (Platform=="Android")
		mapActorData.actorFolderID = C2D_IOUtil.readShort(shortData, dataIn);
		//#endif
		mapActorData.actorID = C2D_IOUtil.readShort(shortData, dataIn);
		mapActorData.actionID = C2D_IOUtil.readShort(shortData, dataIn);
		mapActorData.frameID = C2D_IOUtil.readShort(shortData, dataIn);
		mapActorData.isVisible = C2D_IOUtil.readByte(byteData, dataIn);
		mapActorData.posX = (short) (mapActorData.posX * tile_width + tile_width / 2);
		mapActorData.posY = (short) (mapActorData.posY * tile_height + tile_height / 2);
		if (mapActorData.npcID > 0)
		{
			int lenScripts = C2D_IOUtil.readByte(byteData, dataIn) & 0xFF;
			if (lenScripts > 0)
			{
				mapActorData.m_scriptIDs = new short[lenScripts];
				for (int i = 0; i < lenScripts; i++)
				{
					mapActorData.m_scriptIDs[i] = C2D_IOUtil.readShort(shortData, dataIn);
				}
			}
		}
		return mapActorData;
	}

	/**
	 * 获取映射后的图片ID.
	 * 
	 * @param srcID
	 *            int 源图ID
	 * @return int 映射图片ID
	 */
	private int getMappedImgID(int srcID)
	{
		if (mapImgsMappedID != null && srcID >= 0 && srcID < mapImgsMappedID.length)
		{
			if (mapImgsMappedID[srcID] >= 0)
			{
				return mapImgsMappedID[srcID];
			}
		}
		return srcID;
	}

	/**
	 * 得到地图的地形图片.
	 */
	public C2D_GdiImage getMapImage()
	{
		if (m_mapBuffer != null)
		{
			return m_mapBuffer;
		}
		m_mapBuffer = C2D_GdiImage.createImage(world_width, world_height);
		C2D_GdiGraphics g = m_mapBuffer.getGraphics();
		int tileX0 = 0;
		int tileY0 = 0;
		int tileX1 = tile_nb_x - 1;
		int tileY1 = tile_nb_y - 1;
		int resID, _x, _y, _w, _h;
		byte transFlag;
		int i, j, posX, posY, tileID, resType, color;
		byte tileFlag;
		int clipDataID = 0;
		posY = tileY0 * tile_height;
		// 绘制地形
		for (j = tileY0; j <= tileY1; j++)
		{
			if (j < 0 || j >= tile_nb_y)
			{
				continue;
			}
			posX = tileX0 * tile_width;
			for (i = tileX0; i <= tileX1; i++)
			{
				if (i < 0 || i >= tile_nb_x)
				{
					continue;
				}
				if ((LEVEL_FLAG & LEVELFLAG_TILE_BG) != 0)
				{
					tileID = getTileValue(LEVELFLAG_TILE_BG, i, j);
					tileFlag = getTileFlag(LEVELFLAG_TILE_BG, i, j);
					if (tileID > 0)
					{
						tileID -= 1;
						clipDataID = tileID * MAP_CLIP_LEN;
						resID = mapClips[clipDataID + 0];
						_x = mapClips[clipDataID + 1];
						_y = mapClips[clipDataID + 2];
						_w = mapClips[clipDataID + 3];
						_h = mapClips[clipDataID + 4];
						transFlag = (byte) mapClips[clipDataID + 5];
						transFlag = C2D_GdiGraphics.getMixedTrans(transFlag, tileFlag);
						g.drawImage(mapImgs[getMappedImgID(resID)].getImage(), posX, posY, _x, _y, _w, _h, 0, transFlag, null);
					}
				}
				// 融合地形层
				if ((LEVEL_FLAG & LEVELFLAG_TILE_SUR) != 0)
				{
					tileID = getTileValue(LEVELFLAG_TILE_SUR, i, j);
					tileFlag = getTileFlag(LEVELFLAG_TILE_SUR, i, j);
					if (tileID > 0)
					{
						tileID -= 1;
						clipDataID = tileID * MAP_CLIP_LEN;
						resID = mapClips[clipDataID + 0];
						_x = mapClips[clipDataID + 1];
						_y = mapClips[clipDataID + 2];
						_w = mapClips[clipDataID + 3];
						_h = mapClips[clipDataID + 4];
						transFlag = (byte) mapClips[clipDataID + 5];
						transFlag = C2D_GdiGraphics.getMixedTrans(transFlag, tileFlag);
						g.drawImage(mapImgs[resID].getImage(), posX, posY, _x, _y, _w, _h, 0, transFlag, null);
					}
				}
				posX += tile_width;
			}
			posY += tile_height;
		}
		return m_mapBuffer;
	}

	/**
	 * 设置物理层标记数值，（注意：不推荐使用这个方法，改变tile层可能带来不少问题）.
	 * 
	 * @param x
	 *            int 目标X方向的方格号
	 * @param y
	 *            int 目标Y方向的方格号
	 * @param type
	 *            byte 标记数值
	 */
	public void setPhysicalFlag(int x, int y, short type)
	{
		if (x < 0 || x >= world_width || y < 0 || y >= world_height)
		{
			return;
		}
		mapData_FLAG_PHY[(x / tile_width) * tile_nb_y + (y / tile_height)] = type;
	}

	/**
	 * 获取物理层标记数值.
	 * 
	 * @param x
	 *            float 目标X方向的方格号
	 * @param y
	 *            float 目标Y方向的方格号
	 * @return byte 标记数值
	 */
	public short getPhysicalFlag(float x, float y)
	{
		if (x < 0 || x >= world_width || y < 0 || y >= world_height)
		{
			return -1;
		}
		int xT = (int) C2D_Math.ceil(x);
		int yT = (int) C2D_Math.ceil(y);
		return ((mapData_FLAG_PHY[(xT / tile_width) * tile_nb_y + (yT / tile_height)]));
	}

	/**
	 * 获取指定层的地图方格数值.
	 * 
	 * @param level
	 *            : 指定的地图层(可以是物理层或者、底层地形层或者融合地形层)
	 * @param m_x
	 *            int 目标X方向的方格号
	 * @param m_y
	 *            int 目标Y方向的方格号
	 * @return : 地图方格数值, -1 代表发生错误
	 */
	public int getTileValue(byte level, int m_x, int m_y)
	{
		if (m_x < 0 || m_x >= tile_nb_x || m_y < 0 || m_y >= tile_nb_y)
		{
			return -1;
		}
		if (level == LEVELFLAG_PHYSIC)
		{
			return mapData_FLAG_PHY[m_x * tile_nb_y + m_y];
		}
		if (level == LEVELFLAG_TILE_BG)
		{
			return mapData_TILE_BG[m_x * tile_nb_y + m_y];
		}
		if (level == LEVELFLAG_TILE_SUR)
		{
			return mapData_TILE_SUR[m_x * tile_nb_y + m_y];
		}
		return -1;
	}

	/**
	 * 获取指定层的地图方格标志信息.
	 * 
	 * @param level
	 *            : 指定的地图层(可以是物理层或者、底层地形层或者融合地形层)
	 * @param m_x
	 *            int 目标X方向的方格号
	 * @param m_y
	 *            int 目标Y方向的方格号
	 * @return : 地图方格数值, -1 代表发生错误
	 */
	public byte getTileFlag(byte level, int m_x, int m_y)
	{
		if (m_x < 0 || m_x >= tile_nb_x || m_y < 0 || m_y >= tile_nb_y)
		{
			return -1;
		}
		if (level == LEVELFLAG_TILE_BG)
		{
			return mapData_TILE_BG_Flag[m_x * tile_nb_y + m_y];
		}
		if (level == LEVELFLAG_TILE_SUR)
		{
			return mapData_TILE_SUR_Flag[m_x * tile_nb_y + m_y];
		}
		return -1;

	}

	/**
	 * 释放地图资源
	 */
	public void onRelease()
	{
		mapData_FLAG_PHY = null;
		mapData_TILE_BG = null;
		mapData_TILE_SUR = null;
		if (mapImgs != null)
		{
			for (int i = 0; i < mapImgs.length; i++)
			{
				if (mapImgs[i] != null)
				{
					mapImgs[i].doRelease();
				}
				mapImgs[i] = null;
			}
			mapImgs = null;
		}
		mapClips = null;
		mapImgsMappedID = null;
	}
}
