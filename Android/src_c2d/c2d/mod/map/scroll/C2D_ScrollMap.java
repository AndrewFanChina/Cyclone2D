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
 * �����ͼ ��������һ����ͼ�����������ж�ȡ�ؿ����ݣ����������ݡ� �����������ֳ���[���ᱳ��1�������1]��[���ᱳ��2�������2]...�������У�
 * zorder�Ǵ�0��ʼ����[0,5],[10,15]...����ʽ�𽥵��������о�����
 * ����Ҳ��֮Ϊ�ֲ���ͼ���������Ǹ���Ĳ���������Щ�ֲ���ͼ�Ͻ��еġ�������� ��Ҫ���ӷֲ���ͼ��Ӧ��ע��ԭ�ȸ����ֲ���ͼ��zorder��
 * ���ᱳ������ָ����ͼƬƽ�̹��ɵľ�̬������һ������������ģ� ���������һ����ͼ������ÿ���к��е��ڶྫ��������ǿ��Ը���ID���
 * ��Ӧ�ľ���㣬Ȼ����������Ӿ�����߲������еľ�������������������Լ�
 * �ľ�����������ע��һ�����������registerSpriteLoader�������е�
 * �ص�������ʵ�ּ����Լ��ľ��顣�����ڼ��ؾ���֮�󣬾����������afterLoad �����ᱻ���ã��˷�����������ʵ��һЩ����ĳ�ʼ����
 * �����ͼ��һ�������������ָ���˳��򼴽���Ⱦ�ľ����ͼ�е����� ���ǿ���ͨ�������������λ�ñ任��Ⱦ����Ҳ����ʹ�������һЩ���湦�ܣ���
 * ʵ�������������л���
 * 
 * @author AndrewFan
 * 
 */
public class C2D_ScrollMap extends C2D_View implements C2D_SpriteLoader
{
	private C2D_FrameManager m_frameManager;
	/** ������ */
	protected int m_w;
	/** ����߶� */
	protected int m_h;
	/** �����ű�ִ���� */
	private C2D_ScriptExcutor m_scriptEx;
	/** ����� */
	protected C2D_Camera m_camera;
	/** �ֲ���ͼ���� */
	protected C2D_Array m_levelViews;
	/** �ֲ���ͼ���� */
	protected C2D_Array m_levelBgs;
	/** ��������� */
	private C2D_SpriteLoader m_spriteLoader = this;

	/**
	 * ��������ͼ,����Դ��ܽ��г�ʼ��
	 * 
	 * @param frameManager
	 *            C2D���
	 */
	public C2D_ScrollMap(C2D_FrameManager frameManager)
	{
		m_frameManager = frameManager;

	}
	/**
	 * ����һ���Զ����С�ľ����ͼ
	 * @param w ������
	 * @param h ����߶�
	 */
	public C2D_ScrollMap(int w,int h)
	{
		init(w,h);
	}
	/**
	 * ��ʼ��
	 * @param w ������
	 * @param h ����߶�
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
	 * ��������
	 * 
	 * @return
	 */
	public C2D_Camera getCamera()
	{
		return m_camera;
	}

	/**
	 * ����ؿ���ͼ
	 * 
	 * @param mapID
	 *            �ؿ���ͼID
	 * @return �Ƿ�����ɹ�
	 */
	public boolean loadMap(int mapID)
	{
		return loadMap(mapID, null, null);
	}
	static short[] buffer=new short[2];
	/**
	 * ����ؿ���ͼ
	 * 
	 * @param mapID
	 *            �ؿ���ͼID
	 * @param loadMode
	 *            ����ģʽ,�˲���������ʾ�ָ���ر���ͼƬ��<br>
	 *            ���紫��int[]{3,2}��ʾ��ʹ��5��ָ�ͼƬ���м��أ���һ��3�飬<br>
	 *            �ڶ���2�飬����һ�������ԭͼ���е�һ����������ͼ<br>
	 * @param cameraFar
	 *            ��ͷ�������
	 * @return �Ƿ�����ɹ�
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
			//��ȡ����ߴ�
			m_w = C2D_IOUtil.readInt(m_w, di);
			m_h = C2D_IOUtil.readInt(m_h, di);
			init(m_w,m_h);
			int len = 0;
			//��ȡ�����ű�
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
			//��ȡ�����
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
				//����ƽ�̵�ͼ
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
				//��ȡ����
				C2D_ScrollLayer lvView = new C2D_ScrollLayer(m_frameManager);
				lvView.setSize(getWidth(), getHeight());
				this.addChild(lvView);
				lvView.setZOrder(i * 10 + 5);
				numAt = C2D_IOUtil.readInt(numAt, di);
				for (int j = 0; j < numAt; j++)
				{
					//��ȡ������ֵ
					atID = C2D_IOUtil.readInt(atID, di);
					actionID = C2D_IOUtil.readInt(actionID, di);
					frameID = C2D_IOUtil.readInt(frameID, di);
					atX = C2D_IOUtil.readInt(atX, di);
					atY = C2D_IOUtil.readInt(atY, di);
					//��������
					buffer[0]=0;//FIXME ��ɫԭ���ļ���ID
					buffer[1]=(short)atID;//FIXME ��ɫԭ��ID
					C2D_Sprite sprite = m_spriteLoader.loadSprite(m_frameManager, buffer,actionID,frameID,atX,atY,j);
					if(sprite!=null)
					{
						sprite.setAction(actionID);
						sprite.setFrame(frameID);
						sprite.setPosTo(atX, atY);
						lvView.addChild(sprite);
						sprite.setZOrder(j);
						//��ȡ�ű�
						sprite.readScript(di);
						//�������
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
	 * ��ȡ�ֲ���ͼ��ÿ��ͼ�㶼�������ڶྫ��
	 * 
	 * @return �ֲ���ͼ����
	 */
	public C2D_Array getLevelViews()
	{
		return m_levelViews;
	}

	/**
	 * ��ȡ�ֲ���ͼ������ÿ��ͼ�㶼���ܱ�����һ�ű���ͼƬ
	 * 
	 * @return �ֲ���ͼ��������
	 */
	public C2D_Array getLevelBgs()
	{
		return m_levelBgs;
	}
	/**
	 * ��ȡָ��ID�ķֲ���ͼ��ÿ��ͼ�㶼�������ڶྫ��
	 * 
	 * @return ָ���ķֲ���ͼ
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
	 * ��ȡָ��ID�ķֲ���ͼ������ÿ��ͼ����ܱ�����һ�ű���ͼƬ
	 * 
	 * @return ָ���ķֲ���ͼ����
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
	 * ����һ���µķֲ���ͼ
	 * 
	 * @param zorder
	 * @return �µķֲ���ͼ
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
	 * ����һ���µķֲ���ͼ
	 * 
	 * @param view
	 *            ��Ҫ��ӵķֲ���ͼ
	 * @param zorder
	 *            ������ӵķֲ���ͼ��ID
	 * @return �µķֲ���ͼ
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
	 * ˢ�·ֲ���ͼ�������ֲ���ͼ֮��ᱻ�Զ����á� �����ֶ��ı���ĳ���ֲ���ͼ��zorder������Ҫ���ô˷���
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
	 * ��ȡ�����ֲ���ͼ
	 * 
	 * @return �����ֲ���ͼ
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
	 * ��ö��Ľű�ִ����
	 * 
	 * @return �ű�ִ����
	 */
	public C2D_ScriptExcutor getSEX()
	{
		return m_scriptEx;
	}

	/**
	 * ע�ᾫ������������ڼ����Զ���ľ������
	 * 
	 * @param loader������
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
	 * ���������ӽڵ㣬ִ�и���
	 * 
	 * @param stage
	 *            ��ǰ����
	 */
	protected void onUpdate(C2D_Stage stage)
	{
		//ִ�нű�
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
	 * ������������������е�����ˢ��
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
		//������Ļ��������
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
	 * ж����Դ
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
