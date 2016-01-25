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
 * ����:Map��
 * </p>
 * <p>
 * ����:Map����Ҫ�����ͼ��Դ�������ͼ����<br>
 * ���еĵ�ͼ��Դ��Ӧ�����c2d�ļ����С�<br>
 * mapImgs.bin�ļ�����˵�ͼ����ʹ�õ���ͼƬ�б�
 * map_xx.bin����˵�xx�صĵ�ͼ��mapStyle_xx.bin����˵�xx�ֵ�ͼ���
 * ��ͼ�����ʵ����һЩͼƬ�͵�ͼ�������Ϣ����Щ�������ܳ���һ��������������õ�ͼ���������������<br>
 * ʵ����Щ��Ϣ�Ĺ���ʹ�ţ�ʹ��������ĳһ�ؿ�ʱ����ȷ֪��ֵֻ��Ҫ�����ļ���ͼƬ��ͬʱҲ����ʹ�õ�<br>
 * ���ֽ�����¼��ͼ��ID�� Map���ṩ�����ɾ�̬���������ػ���ƣ��㲻��Ҫӵ������ʵ����ȴ�ܹ������ʹ������
 * </p>
 * 
 * @author AndrewFan
 */
public class C2D_TileMap extends C2D_Object
{
	/**
	 * ����һ�������ͼ����
	 * @param folderName ��ͼ���ڵ���Դ�ļ���
	 * @param mapName ��ͼ��Դ�ļ�ǰ׺��
	 * @param mapID ��ͼID
	 * @param stageID ����ID
	 */
	public C2D_TileMap(String folderName, String mapName, int mapID, int stageID)
	{
		m_folderName = folderName;
		m_mapName = mapName;
		m_mapID = mapID;
		m_stageID = stageID;
		load();
	}

	/** ��ͼ��Դ����. */
	private static String m_mapName;
	/** ��ͼ��Դ�����ļ�������. */
	public String m_folderName;
	public int m_mapID;
	public int m_stageID;
	public C2D_TileMapData actors_BACKGROUND[];
	public C2D_TileMapData actors_COMMON_OBJ[];
	public C2D_TileMapData actors_NPC_EVENT[][];
	/** �ű�ID. */
	public short m_scriptIDs[];

	private C2D_GdiImage m_mapBuffer;
	// ͼ��λ���
	private static final byte LEVELFLAG_PHYSIC = 1;// ͼ�㶨��---�����ǲ�

	/** ͼ�㶨��---�ײ���β� */
	private static final byte LEVELFLAG_TILE_BG = 2;

	/** ͼ�㶨��---�ںϵ��β� */
	private static final byte LEVELFLAG_TILE_SUR = 4;

	/** ͼ�㶨��---������β� */
	private static final byte LEVEL_BG_OJECT = 8;

	/** ͼ�㶨��---�޹ض���� */
	private static final byte LEVELFLAG_COMMON_OBJ = 16;

	/** ͼ�㶨��---��ɫ�¼��� */
	private static final byte LEVELFLAG_NPC_EVENT = 32;

	/** ��ǰ��ͼX����ĵ�ͼ������Ŀ. */
	public int tile_nb_x;

	/** ��ǰ��ͼY����ĵ�ͼ������Ŀ. */
	public int tile_nb_y;

	/** ��ǰ��ͼ�ĵ�ͼ������. */
	public short tile_width;

	/** ��ǰ��ͼ�ĵ�ͼ����߶�. */
	public short tile_height;

	/** ��ǰ��ͼ��������. */
	public int world_width;

	/** ��ǰ��ͼ������߶�. */
	public int world_height;

	/** ͼ���־ */
	private byte LEVEL_FLAG;

	/** ��ͼ��ɫ */
	private int mapColor;

	/** ��ͼ������� */
	private short mapStyle;

	/** �����ǲ����� */
	private short mapData_FLAG_PHY[];

	/** �ײ���β����� */
	private short mapData_TILE_BG[];

	/** �ںϵ��β����� */
	private short mapData_TILE_SUR[];

	/** �ײ���β㷭ת���� */
	protected static byte mapData_TILE_BG_Flag[];

	/** �ںϵ��β㷭ת���� */
	protected static byte mapData_TILE_SUR_Flag[];

	/** ��ͼͼƬ���� */
	private C2D_MixedImage mapImgs[];

	/** ��ͼ��Ƭ���� */
	private short mapClips[];

	/** ͼƬӳ��� */
	private short mapImgsMappedID[];

	/** ��ͼ��Ƭ����[imgID,x,y,w,h,flag] */
	private static final int MAP_CLIP_LEN = 6;

	/**
	 * ���ݹؿ������ͼ��Դ������ɫ��Ϣ��[anteTypeID, actorID, posX, posY, npcID, actionID,
	 * frameID, isAlive]
	 * ��short������ʽ�����Vector�з��أ��Ա��ں����Ľ�ɫ��ʼ��������3��Vector�������ڴ�Ž�ɫ��Ϣ��
	 */
	public void load()
	{
		DataInputStream dataIn = null;
		short shortData = 0;
		byte byteData = 0;
		/************************************ ����ȫ����Ϣ ************************************/
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
		/************************************ ����ؿ����� ************************************/
		fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + m_mapName + "_map_" + (m_mapID) + ".bin";
		try
		{
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			mapColor = C2D_IOUtil.readInt(mapColor, dataIn); // �����ͼ��ɫ
			mapStyle = C2D_IOUtil.readShort(mapStyle, dataIn); // �����ͼ�������
			// �����ש�ߴ�
			tile_width = C2D_IOUtil.readShort(tile_width, dataIn);
			tile_height = C2D_IOUtil.readShort(tile_height, dataIn);
			// �����ͼ�ߴ�
			tile_nb_x = C2D_IOUtil.readShort((short) tile_nb_x, dataIn);
			tile_nb_y = C2D_IOUtil.readShort((short) tile_nb_y, dataIn);
			world_width = tile_nb_x * tile_width;
			world_height = tile_nb_y * tile_height;
			// ����ͼ����
			LEVEL_FLAG = C2D_IOUtil.readByte(LEVEL_FLAG, dataIn);
			// ӳ�����Ϣ
			short mappedTable[][] = null;
			// ��ͼ����-------------------------------------------------------

			// �����ǲ�
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
			// �ײ���β�
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
			// �ںϵ��β�
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
			// ����������----------------------------------------------------------------
			// ������β�
			if ((LEVEL_FLAG & LEVEL_BG_OJECT) != 0)
			{
				int count = C2D_IOUtil.readShort(shortData, dataIn);
				actors_BACKGROUND = new C2D_TileMapData[count];
				for (int i = 0; i < count; i++)
				{
					actors_BACKGROUND[i] = readMapActorData(dataIn);
				}
			}
			// �޹ض����
			if ((LEVEL_FLAG & LEVELFLAG_COMMON_OBJ) != 0)
			{
				int count = C2D_IOUtil.readShort(shortData, dataIn);
				actors_COMMON_OBJ = new C2D_TileMapData[count];
				for (int i = 0; i < count; i++)
				{
					actors_COMMON_OBJ[i] = readMapActorData(dataIn);
				}
			}
			// ��������
			short stageCount = C2D_IOUtil.readShort(shortData, dataIn);
			// ��ɫ�¼���
			if ((LEVEL_FLAG & LEVELFLAG_NPC_EVENT) != 0)
			{
				actors_NPC_EVENT = new C2D_TileMapData[stageCount][];
				C2D_TileMapData mapActorData = new C2D_TileMapData();
				for (int iS = 0; iS < stageCount; iS++)
				{
					// ��ȡ�����ű�
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
					// ��ȡNPC���ݺͽű�
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
			// ����ͼƬӳ���
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
			// ����ӳ���
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
		// ���������� ----------------------------------------------------
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
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // ������ɫ��16λ������ƬX����
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // ������ɫ��16λ������ƬY����
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // ������
				mapClips[j++] = C2D_IOUtil.readShort(shortData, dataIn); // ����߶�
				mapClips[j++] = (short) (C2D_IOUtil.readByte(byteData, dataIn) & 0xFF); // ��ת��־(�Ծ�����Ч)
			}
			// ��ȡ��ǰ����ʹ�õ���ͼƬ
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
		// Ӧ��ӳ���
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
		// ����ӳ���ĵ�ͼͼƬ
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
	 * ��ȡһ����ͼ�����Ԫ����.
	 * 
	 * @param actors_list
	 *            ��������ݵĴ������
	 * @param dataIn
	 *            DataInputStream ������
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
	 * ��ȡӳ����ͼƬID.
	 * 
	 * @param srcID
	 *            int ԴͼID
	 * @return int ӳ��ͼƬID
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
	 * �õ���ͼ�ĵ���ͼƬ.
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
		// ���Ƶ���
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
				// �ںϵ��β�
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
	 * �������������ֵ����ע�⣺���Ƽ�ʹ������������ı�tile����ܴ����������⣩.
	 * 
	 * @param x
	 *            int Ŀ��X����ķ����
	 * @param y
	 *            int Ŀ��Y����ķ����
	 * @param type
	 *            byte �����ֵ
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
	 * ��ȡ���������ֵ.
	 * 
	 * @param x
	 *            float Ŀ��X����ķ����
	 * @param y
	 *            float Ŀ��Y����ķ����
	 * @return byte �����ֵ
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
	 * ��ȡָ����ĵ�ͼ������ֵ.
	 * 
	 * @param level
	 *            : ָ���ĵ�ͼ��(�������������ߡ��ײ���β�����ںϵ��β�)
	 * @param m_x
	 *            int Ŀ��X����ķ����
	 * @param m_y
	 *            int Ŀ��Y����ķ����
	 * @return : ��ͼ������ֵ, -1 ����������
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
	 * ��ȡָ����ĵ�ͼ�����־��Ϣ.
	 * 
	 * @param level
	 *            : ָ���ĵ�ͼ��(�������������ߡ��ײ���β�����ںϵ��β�)
	 * @param m_x
	 *            int Ŀ��X����ķ����
	 * @param m_y
	 *            int Ŀ��Y����ķ����
	 * @return : ��ͼ������ֵ, -1 ����������
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
	 * �ͷŵ�ͼ��Դ
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
