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
 * ����:���䶯��������
 * </p>
 * <p>
 * ���鶯���࣬�����鶯����ص���Ϣ�����鶯������ͻ��Ƶ�һ��������Ԫ�� ���Լ��Ķ�����֡��ʱ��ȿ�����Ϣ��������ṩC2DManager����Ȼ��ָ��
 * ��ص�ID������һ�����飬������ֱ��new������Ҳ����ʹ��releaseResж�� ����Ҫ�Լ������ڴ��ͷš�<br>
 * ע�⣬ê����뷽ʽ�Ծ�����Ч���������ڲ������Ķ��룬û�й̶��ߴ粻�ܻ��Ƴ����� �������鶯�����ڲ��ṹ�Ƚ϶࣬����һ����Բ���ȥ��ᡣ�����·���ʱ���ᣬʱ
 * ������·���ʱ����㣬ʱ�������·��ǹؼ�֡��ʱ�������Ϣ���ؼ�֡���� ���Ƕ����ͼ�п飬ÿ����ͼ�п鶼������ͬһ����ͼ���е�OpenGL��ͼ��<br>
 * ���⣬��������һЩ����������Ϣ����ɼ��ԡ�λ����Ϣ��������Ϣ�ȵȡ�<br>
 * </p>
 * .
 * 
 * @author AndrewFan
 */
public class C2D_SpriteProto extends C2D_Widget implements C2D_ScriptFunctionHandler
{

	/** �������������ڵ�C2D������. */
	protected C2D_FrameManager m_C2DM = null;

	/** �������ݶ���. */
	protected C2D_SpriteModel m_spriteData = null;

	/** ����ԭ����Ϣ. */
	protected C2D_AnteType m_anteType = null;

	/** ����ID. */
	protected short m_spriteID = -1;

	/** ���鶯��ID. */
	protected short m_actionID = -1;

	/** ���鶯���ؼ�֡ID. */
	protected short m_frameID = -1;

	/** NPC ID. */
	protected short m_npcID = -1;

	/** ��������. */
	private C2D_Prop m_property = null;

	/** �����Ƿ�ѭ������. */
	private boolean m_animLoop = true;

	/** ����һ֡��ʱ������λΪ����. */
	private int m_duaration = 15;

	/** ��ǰ֡��ȥʱ������λΪ����. */
	private int m_timePassed = 0;

	/** �Զ�������ʽ - ���������Զ�����. */
	public static final byte AUTOPLAY_NONE = 0;
	/** ��ǰ���鶯���ĳ��ȣ�����ǰ�������ܹ���ʱ֡��. */
	protected int m_actionLen = 0;
	/** ��ǰ���鶯���Ĳ�����ʱ��������ǰ�������ܹ���ʱ֡��*֡����ʱ��. */
	protected int m_actionDuration = 0;
	/**
	 * �Զ�������ʽ - ����������ʱ���Զ����ţ�����durationΪ����� �Ƽ�ʹ�������Զ�������ʽ�����Ա����ڲ�ͬ��֡�����棬�����ͬ�Ķ�����
	 * �ٶȣ�Ҳ��Ҫע���Խ֡���Ŵ���������.
	 */
	public static final byte AUTOPLAY_TIME = 1;
	/** �Զ�������ʽ - ��������֡ͣ���Զ�����. */
	public static final byte AUTOPLAY_FRAME = 2;
	/** �Զ�������ʽ */
	private byte m_autoPlay = AUTOPLAY_NONE;

	/** ��ǰ�����Ƿ�ͣ���ڶ�����β. */
	private boolean m_atEnd = false;

	/** �Ƿ�Ծ�����к���ת. */
	protected boolean m_flipX = false;

	/** ������ʹ�õ�ͼƬ��������ActorData���. */
	private short m_imgsUsedIndexs_Actor[] = null;

	/** �¼��б�-�������� */
	private C2D_EventPool_ActionEnd m_Events_ActionEnd;

	/** �����ļ���ID. */
	protected short m_folderID = -1;

	/** ��ת�Ƕ� ,Χ��-Z(Ҳ��˳ʱ��) */
	public float m_degree = 0;

	/** ���ű��� */
	private float m_zoomOut = 1.0f;
	/** �ű�ִ�ж��� */
	private C2D_ScriptExcutor m_scriptEx;
	// ����ƫ�����Ͷ���
	/** �޶���ƫ��. */
	public static final byte ACTIONOFFSET_NULL = 0;
	/** X������ƫ��. */
	public static final byte ACTIONOFFSET_X = 1;
	/** XY������ƫ��. */
	public static final byte ACTIONOFFSET_XY = 2;
	/** XYZ������ƫ��. */
	public static final byte ACTIONOFFSET_XYZ = 3;
	// �߼�������
	/** ���������. */
	public static final byte LOGIC_BODY = 0;
	/** ���������. */
	public static final byte LOGIC_FEET = 1;
	/** ���������. */
	public static final byte LOGIC_ATCK = 2;
	/** ���������. */
	public static final byte LOGIC_BEAT = 3;
	/** �߼������. */
	public static final byte LOGIC_END = 19;
	/** ��Ҫÿ֡����߼��򣨰���һ������������һ����������� */
	public boolean m_needCheckLogicBox = false;
	/** �߽���Σ�������������꣬����Ǿ��飬���Ǿ��鵱ǰ֡�ı߽���Σ������㵱ǰ�����ˮƽ��ת״̬ */
	public C2D_RectS m_boundsRect;
	/** ����������������򣬴������Ӧ��ÿ��������֡�������㵱ǰ�����ˮƽ��ת״̬ */
	public C2D_RegionI m_bodyRect2Self;
	/** �������Ĺ�������򣬴������Ӧ��ÿ��������֡�������㵱ǰ�����ˮƽ��ת״̬ */
	public C2D_RegionI m_atkRect2Self;

	/** �Ƿ���й����� */
	private boolean m_played=false;
	// ============================ ���캯�� ============================

	/**
	 * ���ݾ����ļ���ID�;���ID����һ��Sprite�����Ҷ�ȡ��������Ҫ��Դ��
	 * 
	 * @param c2dManager
	 *            C2DManager �������������ڵ�C2DManager����
	 * @param folderID
	 *            short ����ľ����ļ���ID������ͷ�ļ��еĳ�������
	 * @param spriteID
	 *            short �����Sprite��ID������ͷ�ļ��еĳ�������
	 */
	public C2D_SpriteProto(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		this.m_C2DM = c2dManager;
		this.m_folderID = folderID;
		this.m_spriteID = spriteID;
		initRes();
	}

	/**
	 * ���ݾ���ԭ���ļ���ID�;���ԭ��ID����һ��Actor�����Ҷ�ȡ��������Ҫ��Դ��
	 * SpriteFolderID��SpriteID��Ӿ���ԭ�����Զ���ȡ��
	 * 
	 * @param c2dManager
	 *            �������������ڵ�C2DManager����
	 * @param anteTypeID
	 *            ����ľ���ԭ���ļ���ID�;���ԭ��˫��ֵ���飬����ͷ�ļ��еĳ������塣
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
	 * û�н����κ����õĹ��캯�����������д���m_C2DM�����õ�����
	 */
	protected C2D_SpriteProto()
	{
	}

	// ============================ ��Դ�������ͷ� ============================
	/**
	 * ��ʼ����Դ.
	 */
	protected void initRes()
	{
		C2D_Debug.logC2D("create sprite:"+m_spriteID);
		loadActorData();
		loadActorImgs();
		setActionAndFrame(0, 0);
	}

	/**
	 * ����C2D���
	 * 
	 * @param c2dm
	 */
	public void setC2DM(C2D_FrameManager c2dm)
	{
		m_C2DM = c2dm;
		initRes();
	}

	/**
	 * ���뵱ǰ����ͼƬ��Դ.
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
	 * ���뵱ǰ����ͼƬ��Դ.
	 */
	private void loadActorImgs()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null)
		{
			m_C2DM.m_AniM.spriteUseImages(this);
		}
	}

	/**
	 * �ͷž��鶯��������Դ
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
	 * �ͷŶ����б�Sprite�����ͼƬ��Դ
	 */
	private void releaseActorImgs()
	{
		if (m_C2DM != null && m_C2DM.m_AniM != null)
		{
			m_C2DM.m_AniM.spriteUnuseImages(this);
		}
	}

	/**
	 * �ͷ���Դ.
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
//			m_scriptEx.doRelease(this);//TODO ������Ӽ̳�
			m_scriptEx = null;
		}
	}

	// ============================ �������� ============================
	/**
	 * �л�����ԭ�����ͣ�����ͬʱ��ActionID���ú�����ͼƬ.
	 * 
	 * @param antetypeIDT
	 *            short ����ԭ���ļ���ID�;���ԭ��ID˫��ֵ����
	 * @param resetTimePos
	 *            boolean �Ƿ�����ʱ����λ��
	 * @param resetAction
	 *            boolean �Ƿ����ö���
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
			// ��ж��
			releaseActorImgs();
			releaseSpriteData();
			m_anteType = newAnteType;
			m_spriteID = m_anteType.actotorID;
			// �ټ���
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
	 * �л�����ID������ͬʱ��ActionID���ú�����ͼƬ.
	 * 
	 * @param spriteID
	 *            short ����ID
	 */
	public void setSpriteID(short spriteID)
	{
		setSpriteID(spriteID,true,true);
	}
	/**
	 * �л�����ID������ͬʱ��ActionID���ú�����ͼƬ.
	 * 
	 * @param spriteID
	 *            short ����ID
	 * @param resetTimePos
	 *            boolean �Ƿ�����ʱ����λ��
	 * @param resetAction
	 *            boolean �Ƿ����ö���
	 */
	public void setSpriteID(short spriteID, boolean resetTimePos, boolean resetAction)
	{
		if (spriteID >=0 &&spriteID!=getSpriteID())
		{
			// ��ж��
			releaseActorImgs();
			releaseSpriteData();
			m_spriteID = spriteID;
			// �ټ���
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
	 * ����ʹ�õ���ͼƬ����.
	 * 
	 * @return short[] ʹ�õ���ͼƬ����
	 */
	short[] getUsedImgIDs()
	{
		return m_imgsUsedIndexs_Actor;
	}

	/**
	 * �����µĶ���ID����Ĭ�Ͻ�֡ID���0
	 * 
	 * @param actionID
	 *            �µĶ���ID
	 * @return �Ƿ����������
	 */
	public boolean setAction(int actionID)
	{
		return setActionAndFrame(actionID, 0);
	}

	/**
	 * ����֡ID
	 * 
	 * @param frameID
	 *            �µ�֡ID
	 * @return �Ƿ����������
	 */
	public boolean setFrame(int frameID)
	{
		return setActionAndFrame(m_actionID, frameID);
	}

	/**
	 * ���ö���ID��֡ID
	 * 
	 * @param actionID
	 *            �µĶ���ID
	 * @param frameID
	 *            �µ�֡ID
	 * @return �Ƿ����������
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
		// FIXME �������й���֡��Ϣ
		m_C2DM.m_AniM.setSpriteTime(this, m_actionID, m_frameID);
		if (m_needCheckLogicBox)
		{
			checkLogicBox();
		}
		layoutChanged();
		return true;
	}

	/**
	 * ��ǰ����N֡����λΪ֡��
	 * 
	 * @param nStay
	 *            ����֡��
	 * @param animLoop
	 *            �Ƿ�ѭ������
	 * @return �����Ƿ��Ѿ������β֡
	 */
	protected boolean playFame(int nStay, boolean animLoop)
	{
		if (m_spriteData == null || m_C2DM == null)
		{
			return false;
		}
		boolean reachEnd = false;
		int frameID = m_frameID; // �ؼ�֡ID
		int keyFrameCount = m_spriteData.getFrameCount(m_actionID);// ��ǰ�����Ĺؼ�֡����
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
		// ���ý��
		if (frameID != m_frameID)
		{
			setFrame(frameID);
		}
		m_atEnd = reachEnd;
		// ִ�ж��������¼�
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
	 * ��ǰ��������һ֡����.�Ƿ�ѭ��ȡ���������ѭ������
	 * 
	 * @return boolean �����Ƿ��Ѿ�����β֡
	 */
	public boolean playFrame()
	{
		return playFame(1, m_animLoop);
	}

	/**
	 * ��ǰ����N֡����.�Ƿ�ѭ��ȡ���������ѭ������
	 * 
	 * @param nStay
	 *            ����֡ͣ����
	 * @return �����Ƿ��Ѿ�����β֡
	 */
	public boolean playFrame(int nStay)
	{
		return playFame(nStay, m_animLoop);
	}

	/**
	 * ��ǰ���ŵ�ǰ��Ϸѭ����ȥʱ��
	 * 
	 * @return �����Ƿ��Ѿ�����β֡
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
	 * ��ǰ����һ��ʱ��,��λΪ����
	 * 
	 * @param time
	 *            ����ʱ��
	 * @param animLoop
	 *            �Ƿ�ѭ������
	 * @return �����Ƿ��Ѿ�����β֡
	 */
	protected boolean playTime(int time, boolean animLoop)
	{
		if (m_spriteData == null || m_C2DM == null)
		{
			return false;
		}
		boolean reachEnd = false;
		int timePassed = m_timePassed;// ��ǰ֡��ȥʱ��
		int frameID = m_frameID; // �ؼ�֡ID
		int keyFrameCount = m_spriteData.getFrameCount(m_actionID);// ��ǰ�����Ĺؼ�֡����
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
		// ���ý��
		m_timePassed = timePassed;
		m_atEnd = reachEnd;
		if (frameID != m_frameID)
		{
			setFrame(frameID);
		}
		// ִ�ж��������¼�
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
	 * �鿴�Ƿ�λ�ڶ���β֡.
	 * 
	 * @return boolean �����Ƿ�λ�ڶ���β֡
	 */
	public boolean atActionEnd()
	{
		return m_atEnd;
	}

	/**
	 * ���ؾ���ID.
	 * 
	 * @return short ����ID
	 */
	public short getSpriteID()
	{
		return this.m_spriteID;
	}

	/**
	 * ���ؾ����NPC ID.
	 * 
	 * @return short ����NPC ID
	 */
	public int getNpcID()
	{
		return this.m_npcID;
	}

	/**
	 * ���ؾ���ľ���ԭ��ID.
	 * 
	 * @return int ����ԭ��ID
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
	 * ���ص�ǰ����ID.
	 * 
	 * @return int ��ǰ����ID
	 */
	public int getActionID()
	{
		return this.m_actionID;
	}

	/**
	 * ���ص�ǰ��������.
	 * 
	 * @return int ��ǰ��������
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
	 * ���ص�ǰ�ؼ�֡ID.
	 * 
	 * @return int ��ǰ�ؼ�֡ID
	 */
	public int getFrameID()
	{
		return this.m_frameID;
	}

	/**
	 * ����ָ����������������֡���ܺ� (���Ӷ�����ʼ��������������������������֡���ϼ�).
	 * 
	 * @param actionID
	 *            int ָ������ID
	 * @return int ������������֡���ϼ�
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
	 * ���ص�ǰ��������������֡���ܺ� (���Ӷ�����ʼ��������������������������֡ͣ���ϼ�).
	 * 
	 * @return int ������������֡ͣ���ϼ�
	 */
	public int getActionFrameTotal()
	{
		return getActionFrameTotal(m_actionID);
	}

	/**
	 * ���ص�ǰ����ʣ���֡ͣ��(���ӵ�ǰ֡λ�ã�����β֡������֡ͣ���ϼ�).
	 * 
	 * @return int ǰ������֡ͣ��.
	 */
	public int getActionFrameLeft()
	{
		int frameCount = m_spriteData.getFrameCount(m_actionID);
		int stay = frameCount - m_frameID;
		return stay;
	}

	/**
	 * ���ؾ����ļ���ID.
	 * 
	 * @return short �����ļ���ID
	 */
	public short getActorFolderID()
	{
		return this.m_folderID;
	}

	/**
	 * ���ص�ǰ�����ĳ��ȣ����ؼ�֡֡�ĸ���.
	 * 
	 * @return int �����ĳ���
	 */
	public int getKeyFrameCount()
	{
		return getKeyFrameCount(m_actionID);
	}

	/**
	 * ����ָ�������ĳ��ȣ����ؼ�֡�ĸ���.
	 * 
	 * @param actionID
	 *            ָ������ID
	 * @return �����ĳ���
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
	 * ���ض���֡X����ƫ��(ע��ƫ��ʹ�õѿ�������ϵ,+Xָ����,+yָ���ϣ�+zָ����)
	 * 
	 * @return int ��ǰ֡��X������ƫ��
	 */
	public int getActionOffset_X()
	{
		return m_spriteData.getActionOffset((byte) 0, m_actionID, m_frameID);
	}

	/**
	 * ���ص�ǰ֡Y������ƫ��(ע��ƫ��ʹ�õѿ�������ϵ,+Xָ����,+yָ���ϣ�+zָ����)
	 * 
	 * @return int ��ǰ֡��Y������ƫ��
	 */
	public int getActionOffset_Y()
	{
		return m_spriteData.getActionOffset((byte) 1, m_actionID, m_frameID);
	}

	/**
	 * ���ص�ǰ֡֡Z������ƫ��(ע��ƫ��ʹ�õѿ�������ϵ,+Xָ����,+yָ���ϣ�+zָ����)
	 * 
	 * @return int ��ǰ֡��Z������ƫ��
	 */
	public int getActionOffset_Z()
	{
		return m_spriteData.getActionOffset((byte) 2, m_actionID, m_frameID);
	}

	/**
	 * ��ȡ�Ƿ���X�ᷭת
	 * 
	 * @return boolean �Ƿ���X�ᷭת
	 */
	public boolean getFlipX()
	{
		return m_flipX;
	}

	/**
	 * �����Ƿ���X�ᷭת
	 * 
	 * @param flipX
	 *            �Ƿ���X�ᷭת
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
	 * ��ȡ�Ƿ�ѭ������
	 * 
	 * @return boolean �Ƿ�ѭ������
	 */
	public boolean getAnimLoop()
	{
		return m_animLoop;
	}

	/**
	 * �����Ƿ�ѭ������
	 * 
	 * @param animLoop
	 *            �Ƿ�ѭ������
	 */
	public void setAnimLoop(boolean animLoop)
	{
		if (m_animLoop != animLoop)
		{
			m_animLoop = animLoop;
		}
	}

	/**
	 * ��ȡNPC ID
	 * 
	 * @return short NPC ID
	 */
	public short setNpcID()
	{
		return m_npcID;
	}

	/**
	 * ����NPC ID
	 * 
	 * @param npcID
	 *            �� NPC ID
	 */
	public void setNpcID(short npcID)
	{
		m_npcID = npcID;
	}

	/**
	 * ��ȡ����һ֡��ʱ������λΪ����
	 * 
	 * @return ����һ֡��ʱ��
	 */
	public int getDuration()
	{
		return m_duaration;
	}

	/**
	 * ���ò���һ֡��ʱ������λΪ���룬����������ֵ
	 * 
	 * @param duration
	 *            ����һ֡��ʱ��
	 * @return �Ƿ�ɹ�����
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
	 * ��ȡ��ѭ����������
	 * 
	 * @return byte ѭ����������
	 */
	public byte getAutoPlay()
	{
		return m_autoPlay;
	}

	/**
	 * �����Ƿ��Զ��������ͣ����Ը���ʱ����߰�֡ͣ�������Զ����� �������Ϊ���Զ����ţ���Ҫʹ��nextFrame�����ֶ���֡����
	 * 
	 * @param autoPlay
	 *            �Զ���������
	 */
	public void setAutoPlay(byte autoPlay)
	{
		if (m_autoPlay != autoPlay && autoPlay >= AUTOPLAY_NONE && autoPlay <= AUTOPLAY_FRAME)
		{
			m_autoPlay = autoPlay;
		}
	}

	/**
	 * ��ȡ��ǰͣ��֡�Ѿ���ȥ��ʱ�䣬����Ϊ��λ
	 * 
	 * @return ��ȥ��ʱ��
	 */
	public int getTimePassed()
	{
		return m_timePassed;
	}

	// ============================ ��������ʾ ============================
	/**
	 * ���������ӽڵ㣬ִ�и���
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
	 * ��ȡ��Բ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ�� �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ����
	 * ���β������������Ƿ�ɹ�ȡ�á�
	 * 
	 * @param resultRect
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
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
	 * ����ָ������Ϣ���ƾ��� ��ʱ���Ӿ��鱾�����ָ꣬��λ�á�����ID��֡ID��ˮƽ��ת����Ϣ.
	 * 
	 * @param g
	 *            ��ͼָ��
	 * @param screenX
	 *            ��ĻX����
	 * @param screenY
	 *            ��ĻY����
	 * @param actionID
	 *            ָ���Ķ���ID
	 * @param frameID
	 *            ָ����֡ID
	 * @param flipX
	 *            ָ����ˮƽ��ת
	 * @param limitRect
	 *            ָ����������ʾ����
	 */
	public void display(C2D_Graphics g, int screenX, int screenY, int actionID, int frameID, boolean flipX, C2D_RectS limitRect)
	{
		if (m_C2DM != null)
		{
			m_C2DM.m_AniM.drawSpriteKeyFrame(this, actionID, frameID, m_zoomOut, screenX, screenY, flipX, 0, -1, null, 0);
		}
	}

	/**
	 * ���ýǶ�
	 * 
	 * @param degreeT
	 */
	public void setDegree(float degreeT)
	{
		this.m_degree = degreeT;
	}

	/**
	 * ������ű���
	 * 
	 * @return ���ű���
	 */
	public float getZoomOut()
	{
		return m_zoomOut;
	}

	/**
	 * �������ű���
	 * 
	 * @param m_zoomOut
	 *            ���ű���
	 */
	public void setZoomOut(float m_zoomOut)
	{
		this.m_zoomOut = m_zoomOut;
	}

	// ============================ ��Ļ���ͼ��ʾ ============================
	/**
	 * �������������ڵ�ͼ����ʾ����ʾʱ��ȥ������������.
	 * 
	 * @param zoomLevel
	 *            float ���ű���
	 * @param cameraL
	 *            float �����Ե�ͼ����X
	 * @param cameraT
	 *            float �����Ե�ͼ����Y
	 * @param flipX
	 *            boolean �Ƿ�ˮƽ��ת��ʾ
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
	 * ��ʾ����Ļ��ָ�������꣬��ʱ���Ӿ��鱾������.
	 * 
	 * @param zoomLevel
	 *            float ���ű���
	 * @param screenX
	 *            float ��ĻX����
	 * @param screenY
	 *            float ��ĻY����
	 * @param limitRect
	 *            short[] ������ʾ����(x,y,w,h��ʽ)
	 */
	public void display(float zoomLevel, float screenX, float screenY, short limitRect[])
	{
		display(zoomLevel, screenX, screenY, false, limitRect, m_degree);
	}

	/**
	 * ��ʾ����Ļ��ָ�������꣬��ʱ���Ӿ��鱾������.
	 * 
	 * @param zoomLevel
	 *            float ���ű���
	 * @param screenX
	 *            float ��ĻX����
	 * @param screenY
	 *            float ��ĻY����
	 * @param flipX
	 *            boolean �Ƿ�ˮƽ��ת��ʾ
	 * @param limitRect
	 *            short[] ������ʾ����(x,y,w,h��ʽ)
	 * @param degree
	 *            float ��ת�Ƕ�
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
	 * ��ʾ����Ļ��ָ�������꣬��ʱ���Ӿ��鱾�����ָ꣬��λ�á�����ID��֡ID��������ʾ����.
	 * 
	 * @param zoomLevel
	 *            float ���ű���
	 * @param screenX
	 *            float ��ĻX����
	 * @param screenY
	 *            float ��ĻY����
	 * @param actionID
	 *            int ָ���Ķ���ID
	 * @param frameID
	 *            int ָ����֡ID
	 * @param limitRect
	 *            short[] ָ����������ʾ����
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

	// ============================ �߼����� ============================
	/**
	 * ��þ��鵱ǰ֡������ָ�����͵��߼������ĵ������Ļ������ʽ(�Ծ��������ȥ����������Ļ����Ϊ����)�����ػ�õ��߼�����Ŀ�����ݴ洢��������.
	 * 
	 * @param dataBoxs
	 *            int[] �߼���洢���飬��x,y,w,h��ʽ�������ھ������ĵ���߼���
	 * @param logicType
	 *            int �߼�������[0-255]֮��
	 * @param cameraL
	 *            int �������X
	 * @param cameraT
	 *            int �������Y
	 * @return int �߼�������(�������󷵻�-1�����ݻ��岻�㷵��-2)
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
	 * ��þ��鵱ǰ֡������ָ�����͵��߼������ĵ��������������ʽ(�Ծ�����������Ϊ����)�����ػ�õ��߼�����Ŀ�����ݴ洢��������.
	 * �߼�����ֵ��Զ�������
	 * 
	 * @param dataBoxs
	 *            int[] �߼���洢���飬��xl,yt,xr,yb��ʽ�������ھ������ĵ���߼���
	 * @param logicType
	 *            int �߼�������[0-255]֮��
	 * @return int �߼�������(�������󷵻�-1�����ݻ��岻�㷵��-2)
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
	 * ��ָ���ľ��鶯����ָ��֡���ָ�����͵������߼������ĵ������Ļ������ʽ(�Ծ��������ȥ����������Ļ����Ϊ����)
	 * �����ػ�õ��߼�����Ŀ�����ݴ洢�������С�.
	 * 
	 * @param dataBoxs
	 *            int[]�߼���洢���飬��x,y,w,h��ʽ�������ھ������ĵ���߼���
	 * @param logicType
	 *            int �߼�������[0-255]֮��
	 * @param cameraL
	 *            int ��ͷ�����������X
	 * @param cameraT
	 *            int ��ͷ�����������Y
	 * @param actionId
	 *            int ����ID
	 * @param frameID
	 *            int ֡ID
	 * @return int �߼�������(�������󷵻�-1�����ݻ��岻�㷵��-2)
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
	 * ��ָ���ľ��鶯����ָ��֡���ָ�����͵������߼������ĵ��������������ʽ(�Ծ�����������Ϊ����)�� �����ػ�õ��߼�����Ŀ�����ݴ洢�������С�.
	 * 
	 * @param dataBoxs
	 *            int[]�߼���洢���飬��x,y,w,h��ʽ�������ھ������ĵ���߼���
	 * @param logicType
	 *            int �߼�������[0-255]֮��
	 * @param actionId
	 *            int ����ID
	 * @param frameID
	 *            int ֡ID
	 * @return int �߼�������(�������󷵻�-1�����ݻ��岻�㷵��-2)
	 */
	public int getLogicBoxByID(int dataBoxs[], int logicType, int actionId, int frameID)
	{
		if (m_C2DM != null)
		{
			return m_C2DM.m_AniM.getLogicBox(dataBoxs, logicType, this.m_spriteID, actionId, frameID, m_x, m_y, m_flipX);
		}
		return -1;
	}

	// ============================ ����������� ============================
	/**
	 * ����������������.
	 * 
	 * @param actorProperty
	 *            ���Զ���
	 */
	public void setProperty(C2D_Prop actorProperty)
	{
		this.m_property = actorProperty;
	}

	/**
	 * ��ȡ��������Զ���.
	 * 
	 * @return PropInstance �������Զ���
	 */
	public C2D_Prop getProperty()
	{
		return m_property;
	}

	// ============================ �¼����� ============================
	/**
	 * ��ö������Ž����¼���
	 * 
	 * @return �������Ž����¼���
	 */
	public C2D_EventPool_ActionEnd Events_ActionEnd()
	{
		if (m_Events_ActionEnd == null)
		{
			m_Events_ActionEnd = new C2D_EventPool_ActionEnd(this);
		}
		return m_Events_ActionEnd;
	}

	// ============================ ���ݼ�� ============================


	/**
	 * ˢ��ָ�������֡�߽�.
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
			// ��λBounds
			boolean inited = false;
			int m_BoundsL = Short.MIN_VALUE;
			int m_BoundsT = Short.MIN_VALUE;
			int m_BoundsR = Short.MIN_VALUE;
			int m_BoundsB = Short.MIN_VALUE;
			// ֡��Ϣ����
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
				// ��Ƭ��Ϣ����
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
				{ // ����
					clip_Flag = C2D_Graphics.TRANS_NONE;
					String s = m_C2DM.m_TxtM.getString(res_ID - C2D_SpriteManager.SEPERATE_TEXT, true);
					if (s != null)
					{
						_w = (short) C2D_TextFont.getDefaultFont().stringWidth(s);
						_h = (short) C2D_TextFont.getDefaultFont().getFontHeight();
					}
				}
				// ֡��ת����
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
				cx += off_x - C2D_SpriteManager.getHalfWidth(clip_Flag, frameFlag, _w, _h);// X����ƫ�Ƶ���
				cy += off_y - C2D_SpriteManager.getHalfHeight(clip_Flag, frameFlag, _w, _h);// Y����ƫ�Ƶ���
				// ��ǰ��Ƭ�ı߿�
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
			// �洢
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
	// ============================ �����Ż� ============================
	
	public void readScript(DataInputStream di)
	{
		if (di == null)
		{
			return;
		}
		try
		{
			// ��ȡ�����ű�
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
	 * ������������߼���(����һ������������һ�����������)
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
		// ���������
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
		// ���������
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
	 * �Ӹ��������Ƴ�����������
	 */
	public void removeFromTree()
	{
		//�Ӹ��������Ƴ�
		C2D_View sp = getParentNode();
		if (sp != null)
		{
			sp.removeChild(this);
		}
		setVisible(false);
	}
}
