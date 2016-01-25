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
 * ����:���������
 * </p>
 * <p>
 * ����:����c2d�ṹ�еľ��鶯�����ֹ����洢�����еľ��鶯�����ݡ�ͼƬ�б���ɫԭ���б���б����ݡ�����<br>
 * c2d�ṹ�Ļ������֣�������������������еĶ���ͼƬ�����鶯�����ݡ���������ɫԭ�����ݵļ��ء��洢��ж�أ���ˣ�<br>
 * ����ʵ���ϵĶ�������ӵ���ߺ�ͳһ�����ߣ���Spriteֻ�Ƕ������ݵ�ʹ���ߣ���Sprite����ʱ��������ͨ��SpriteManager<br>
 * ������ͼƬ�����ݵļ������ء�
 * </p>
 * .
 * 
 * @author AndrewFan
 */
public class C2D_SpriteManager extends C2D_Object
{

	/** C2D �ĵ�������������������ĸ�����. */
	private C2D_FrameManager c2dManager;
	// ============================ �������� ============================
	/** C2D�ĵ������ж���ͼƬ�б� */
	public C2D_SpriteImage spriteImgs[] = null;

	/** 
	 * C2D�ļ������ж���������Ƭ���ݣ� ��ʽΪ{ͼƬ����,���о���[X,Y,W,H]}

	 */
	public C2D_SpriteClip spriteClips[] = null;

	/** �����ɫ�����б�. */
	private C2D_SpriteModel spriteModels[][] = null;
	/** ���н�ɫ�������� */
	private int spriteModelCount = 0;

	/** ��������λ������(�������Ǽ��ϴ���ʱ������Ч). */
	private int spriteDatasPos[][] = null;

	/** C2D�ļ����������������б�. */
	String soundNames[] = null;
	/** ��ɫԭ���б�. */
	private C2D_AnteType anteTypes[][] = null;

	/** ʹ������������Ϣ��־�������ʹ�ô���޷���������������ֻ���ֶ�����. */
	boolean useSoundIndexs = false;

	/** ʹ�ù��������־�������ʹ�ô�����ݽ�����������������Ϣ. */
	boolean useAttackVetor = false;

	/** ʹ�ù��������־�������ʹ�ô�����ݽ�����������������Ϣ. */
	public byte actionOffsetType = C2D_Sprite.ACTIONOFFSET_NULL;
	/** ���ݳʷ���״̬��־�����Ա༭����������ʾ��ȡ�����������Ǵ����ɷ����ļ��������ͬһ�����ļ�. */
	private boolean dataDiscrete = false;

	/*--- ����Ԫ�����Ͷ��壬�����ڲ�ʹ��(J2me��).---- */
	/** δ��������. */
	public static final short TYPE_NULL = -1;

	/** ͼƬ����. */
	public static final short TYPE_IMG = (short) (TYPE_NULL + 1);

	/** �������. */
	public static final short TYPE_RECT = (short) (TYPE_IMG + 1);

	/** ����. */
	public static final short TYPE_TEXT = (short) (TYPE_RECT + 1);

	/** ���α߿�. */
	public static final short TYPE_FRAME = (short) (TYPE_TEXT + 1);

	/** �߼���. */
	public static final short TYPE_LOGIC = (short) (TYPE_FRAME + 1);
	// ����Ԫ�����ͷֽ�㶨��

	/** ����ͼ�α߿�ֽ�� */
	static final short SEPERATE_FRAME = 6409;

	/** �߼����α߿�ʼ�� */
	static final short SEPERATE_LOGIC_BEG = 6410;

	/** �߼����α߿������ */
	static final short SEPERATE_LOGIC_END = 6665;

	/** �������ֽ�� */
	static final short SEPERATE_RECT = 6666;

	/** ���ַֽ�� */
	static final short SEPERATE_TEXT = 6667;

	/**
	 * �������캯�� �ڹ��캯���У�����ݴ����C2D������������ӵ�е�resName����ȡ�����Ļ������ݡ�
	 * �������ݰ���������Ϣ(���������������ݷ����ȿ���)��ͼƬ�б���Ϣ�������б���Ϣ�����л�����Ƭ��Ϣ��SpriteData�ĳ��Ⱥ�λ����Ϣ�����н�ɫԭ������.
	 * 
	 * @param c2dManagerT
	 *            C2DManager C2D�ĵ�����������
	 */
	public C2D_SpriteManager(C2D_FrameManager c2dManagerT)
	{
		c2dManager = c2dManagerT;
		readObject();
	}

	/**
	 * ��ȡ������Ϣ��������Ƭ��.
	 * 
	 * @return boolean �����Ƿ��ʼ�����
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
			// ����������
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			// ����������Ϣ------------------------------------------------
			// ��ȡͼƬ��������
			c2dManager.imgConfused = C2D_IOUtil.readBoolean(c2dManager.imgConfused, dataIn);
			// ���붯�������Ϣ
			useAttackVetor = C2D_IOUtil.readBoolean(useAttackVetor, dataIn);
			// ��ȡ����λ������
			actionOffsetType = C2D_IOUtil.readByte(byteData, dataIn);
			// ��������������ݿ���
			dataDiscrete = C2D_IOUtil.readBoolean(dataDiscrete, dataIn);
			// ���붯��ͼƬ��
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
			// ���붯����Ƭ��
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
			// ���붯��������Ϣ
			short nbFolders = C2D_IOUtil.readShort(shortData, dataIn);// �ļ�����Ŀ
			spriteModelCount = 0;
			if (nbFolders > 0)
			{
				spriteModels = new C2D_SpriteModel[nbFolders][];
				for (short i = 0; i < nbFolders; i++)
				{
					short nbSprites = C2D_IOUtil.readShort(shortData, dataIn);// ��ɫ��Ŀ
					spriteModels[i] = new C2D_SpriteModel[nbSprites];
					spriteModelCount += nbSprites;
				}
			}
			// �����ɫ����λ����Ϣ
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
			// ��ɫԭ����Ϣ
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
	 * ���ݶ�άID����[idCount][folderID,spriteID]�����ɫ���� �������Ϊnullʱ��ִ��ȫ������.
	 * 
	 * @param ids
	 *            int[][] ��ɫ�ļ���ID����ɫID����
	 */
	private void loadSpriteData(int ids[][])
	{
		if (spriteModels == null)
		{
			return;
		}
		// ��ʼ����
		DataInputStream dataIn = null;
		try
		{
			// �ȴ��������������ID
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
			// ��ʼ��ȡ������Ǽ������ݣ��򴴽�������
			if (!dataDiscrete)
			{
				dataIn = C2D_IOUtil.getDataInputStream(C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + "_sds.bin");
			}
			// ����ID˳���ȡ
			int lastPosition = 0;
			for (int idI = 0; idI < ids.length; idI++)
			{
				int folderID = ids[idI][0];
				int spriteID = ids[idI][1];
				// ����������
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
				// �رշ���Դ
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
	 * ����sprite��ȡ�����õ���ͼƬSpriteImage����������SpriteImage��ʹ�ü�����
	 * ���SpriteImage��û�м��أ����Զ���ȡ���ڴ档
	 * 
	 * @param sprite
	 *            ָ���Ľ�ɫ����
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
	 * ����sprite�ͷ������õ���ͼƬSpriteImage����������ʹ�õ�����KImage��ʹ�ü�����
	 * ���SpriteImageʹ�ü�����0����SpriteImage���ڲ����ݴ��ڴ���ɾ����
	 * 
	 * @param sprite
	 *            ָ���Ľ�ɫ����
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
	 * ����spriteFolderID��spriteID��ȡSpriteModel����������SpriteModel��ʹ�ü�����
	 * ���SpriteModel�����ڣ��򴴽��µ�ʵ����
	 * 
	 * @param spriteFolderID
	 *            ��ɫ�ļ���ID
	 * @param spriteID
	 *            ��ɫID
	 * @return SpriteModel ���ؽ�ɫ����
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
	 * ��spriteFolderID��spriteID�ͷ�SpriteModel�����ȼ���SpriteData��ʹ�ü���
	 * ���SpriteModelʹ�ü�����0������ڴ���ɾ����SpriteModel.
	 * 
	 * @param spriteID
	 *            ��ɫID
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
	 * �ͷ���Դ��������ɫ�������ݡ�ͼƬ�б���ɫԭ���б������б��������Ϣ.
	 */
	@Override
	public void onRelease()
	{
		// �ͷ�����
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
		// �ͷ�ͼƬ
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

	/* ==============================���ݻ��============================== */
	/**
	 * ���ݽ�ɫԭ���ļ���ID�ͽ�ɫԭ��ID��ý�ɫԭ������.
	 * 
	 * @param anteTypeID
	 *            int ���ݽ�ɫԭ��ID
	 * @return AnteType ��ɫԭ�Ͷ���
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
	 * ���ݽ�ɫԭ�Ͷ��󷵻����ɫԭ��ID.
	 * 
	 * @param antyType
	 *            AnteType��ɫԭ�Ͷ���
	 * @return int ��ɫԭ��ID,�����AnimManager�в����д˽�ɫԭ���򷵻�-1
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
	 * �����������SpriteModel�ĸ���.
	 * 
	 * @return int ���spriteModels����Ϊnull���򷵻�-1
	 */
	public int getSpriteModelCount()
	{
		return spriteModelCount;
	}
	/**
	 * ��ö������߼��򣬷��ػ�õ��߼�����Ŀ����[x��y��w��h��x��y��w��h...]����ʽ�����ݴ洢��������
	 * 
	 * @param dataBoxs
	 *            int[] ������������ݴ洢�����飬���Ȳ���ʱ�᷵��-2
	 * @param logicType
	 *            int �߼�������[0-255]֮��
	 * @param actorId
	 *            int ��ɫID
	 * @param actionId
	 *            int ����ID
	 * @param frameId
	 *            int ֡ID
	 * @param x
	 *            int �����ɫ�ĵ�ǰ��������X�����ص�����ͬ�����������
	 * @param y
	 *            int �����ɫ�ĵ�ǰ��������Y�����ص�����ͬ�����������
	 * @param flipX
	 *            boolean �Ƿ�ˮƽ��ת
	 * @return int ���ػ�õ��߼�����Ŀ���������󷵻�-1�����ݻ��岻�㷵��-2
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
//		// �ݴ�
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
//		// ֡��Ϣ����
//		int baseFrameID = actorData.getFrameFlag_BaseID(actionId, frameId);
//		byte frameFlag = actorData.getFrameFlag_Trans(actionId, frameId);
//		if (flipX)
//		{
//			frameFlag = C2D_Graphics.TRANS_ARRAY[frameFlag][C2D_Graphics.TRANS_HORIZENTAL];
//		}
//		int id = baseFrameID << 1;
//		short clipsBegin = actorData.baseFrames_pos[id];
//		short clipsCount = actorData.baseFrames_pos[id + 1];
//		// �߼�����Ŀ
		int findCount = 0;
//		for (int i = 0; i < clipsCount; i++)
//		{
//			// ��Ƭ��Ϣ����
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
//			// ֡��ת����
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
//			// �洢����
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
	/* ==============================�ؼ�֡����============================== */
	/**
	 * Sprite�ؼ�֡���ƣ�ʹ��sprite��ǰ������֡ID ���е�sprite�ؼ�֡���ƶ������Ķ���ķ�ʽ��.
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param zoomLevel
	 *            float ���ű���
	 * @param x
	 *            float Ŀ��X����
	 * @param y
	 *            float Ŀ��Y����
	 * @param flipX
	 *            boolean �Ƿ����ת
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
	 * �����������Sprite�ؼ�֡���ƣ�ʹ��Sprite��ǰ�����Ͳ��Ž��� ���е�Sprite�ؼ�֡���ƶ������Ķ���ķ�ʽ��
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param zoomLevel
	 *            float ���ű���
	 * @param x
	 *            float Ŀ��X����
	 * @param y
	 *            float Ŀ��Y����
	 * @param flipX
	 *            boolean �Ƿ����ת
	 * @param limitRegion
	 *            short[] �������򣬡�X��Y��W��H����ʽ��short����
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, float zoomLevel, float x, float y, boolean flipX, short limitRegion[])
	{
		drawSpriteKeyFrame(sprite, sprite.m_actionID, sprite.m_frameID, zoomLevel, x, y, flipX, 0, Integer.MAX_VALUE, limitRegion, sprite.m_degree);
	}

	/**
	 * ָ��������֡ID��Sprite�ؼ�֡���� ���е�Sprite�ؼ�֡���ƶ������Ķ���ķ�ʽ��
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param zoomLevel
	 *            float ���ű���
	 * @param actionID
	 *            int ָ���Ķ���ID
	 * @param playTime
	 *            int ָ�����Ž���
	 * @param x
	 *            int Ŀ��X����
	 * @param y
	 *            int Ŀ��Y����
	 * @param flipX
	 *            boolean �Ƿ����ת
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, int actionID, int playTime, float zoomLevel, int x, int y, boolean flipX)
	{
		drawSpriteKeyFrame(sprite, actionID, playTime, zoomLevel, x, y, flipX, 0, Integer.MAX_VALUE, null, sprite.m_degree);
	}

	/**
	 * ָ����ƬID�����Sprite�ؼ�֡���ƣ�ʹ��Sprite��ǰ�����Ͳ��Ž��ȣ� ���е�sprite�ؼ�֡���ƶ������Ķ���ķ�ʽ��
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param zoomLevel
	 *            float ���ű���
	 * @param x
	 *            int Ŀ��X����
	 * @param y
	 *            int Ŀ��Y����
	 * @param flipX
	 *            boolean �Ƿ����ת
	 * @param timeLineStart
	 *            int ��ʼʱ����ID(����)
	 * @param timeLineEnd
	 *            int ����ʱ����ID(����)
	 */
	public void drawSpriteKeyFrame(C2D_SpriteProto sprite, float zoomLevel, float x, float y, boolean flipX, int timeLineStart, int timeLineEnd)
	{
		drawSpriteKeyFrame(sprite, sprite.m_actionID, sprite.m_frameID, zoomLevel, x, y, flipX, timeLineStart, timeLineEnd, null, sprite.m_degree);
	}

	/**
	 * ����������ģ�ָ��������֡ID�ģ���ѡ��Ƭ�����Sprite�ؼ�֡���ƣ�
	 * ʹ��Sprite��ǰ�����Ͳ��Ž��ȣ����е�Sprite�ؼ�֡���ƶ������Ķ���ķ�ʽ��.
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param zoomLevel
	 *            float ���ű���
	 * @param actionId
	 *            int ָ���Ķ���ID
	 * @param frameID
	 *            int ָ�����Ž���
	 * @param xS
	 *            float Ŀ����ĻX����
	 * @param yS
	 *            float Ŀ����ĻY����
	 * @param flipX
	 *            boolean �Ƿ����ת
	 * @param timeLineStart
	 *            int ��ʼʱ����ID(����)
	 * @param timeLineEnd
	 *            int ����ʱ����ID(����)
	 * @param limitRegion
	 *            short[] �������򣬡�X��Y��W��H����ʽ��short����
	 * @param angleDegree
	 *            float ��ת�Ƕ�
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
		// Ҫ���ƵĶ���
		C2D_Action action = spriteModel.sonList[actionId];
		// if(action==null)
		// {
		// return;
		// }
		// ����Ҫ���Ƶ�ʱ������
		int timeSpan = action.sonList.length;
		if (timeLineEnd <= 0 || timeLineEnd >= timeSpan)
		{
			timeLineEnd = timeSpan - 1;
		}
		// ��ʼ��������
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
					// �����������
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
	 * Ԥ������Դ���⺯�������ڽ��д��ģ����Sprite֮ǰ��ͳһ�������еĽ�ɫ����(SpriteModel)
	 * �����ݳʷǷ���״̬ʱ�����������ļ������ϴ�������ǽ��д��ģSprite���룬������ҪƵ����ȡ�˶��������ļ���
	 * ���Ч���ϵ��˷ѣ�Ϊ�˱���������������ǿ���Ԥ��һ���Խ����д�AnimManager�еĶ������ݼ��ص��ڴ棬�����ģ
	 * ��Sprite��ʼ�����֮��������ʹ��preLoadResEnd�ͷŲ���Ҫ�Ľ�ɫ���ݣ��������Խ�ʡЧ�ʡ�.
	 */
	public void preLoadResBegin()
	{
		if (!dataDiscrete)
		{
			this.loadSpriteData(null);
		}
	}

	/**
	 * ж��Ԥ������Դ��ɾ������Ҫ��(�����ü���Ϊ0)��ɫ���ݡ� �����preLoadResBegin()����.
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
	 * ���ö���ʱ��
	 * 
	 * @param sprite
	 *            Sprite ָ���Ľ�ɫ
	 * @param actionId
	 *            int ָ���Ķ���ID
	 * @param frameID
	 *            int ָ�����Ž���
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
		// Ҫ���ƵĶ���
		C2D_Action action = spriteModel.GetSon(actionId);
		if (action == null)
		{
			return;
		}
		// ����Ҫ���Ƶ�ʱ������
		int timeSpan = action.Count();
		// ��ʼ��������
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
