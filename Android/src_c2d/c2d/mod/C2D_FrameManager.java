package c2d.mod;

import c2d.lang.obj.C2D_Object;
import c2d.mod.prop.C2D_PropManager;
import c2d.mod.script.C2D_ScriptManager;
import c2d.mod.sprite.C2D_TextManager;
import c2d.mod.sprite.tween.C2D_SpriteManager;

/**
 * <p>
 * ����:C2D��ܽṹ������
 * </p>
 * <p>
 * ����:C2D��ܽṹ������࣬��ӵ�����ɶ������ֹ������������������ı����ű������ԵĹ��������Լ���Դ��������<br>
 * �������ཫ��һ�������Դ�ļ��м�������Ľṹ��Ϣ�����絼����ʱ������Ϊscene.bin,��ô�����������Դ��<br>
 * ���ļ���C2D�ṹ��Ӧ�����ж��������ݷ���c2dĿ¼�£�ͼƬ�ļ�����imgs_sceneĿ¼�£�������Դ����<br>
 * soundsĿ¼�¡�
 * </p>
 * @author AndrewFan
 */
public class C2D_FrameManager extends C2D_Object
{
	
	/** ����������������C2D�ṹ�Ķ������ֹ���. */
	public C2D_SpriteManager m_AniM;
	
	/** �ı�������������C2D�ṹ���ı����ֹ���. */
	public C2D_TextManager m_TxtM;
	
	/** �ű�������������ű����������. */
	public C2D_ScriptManager m_SptM;
	
	/** ���Թ�����������C2D�ṹ�����Բ��ֹ���. */
	public C2D_PropManager m_PropM;
	
	/** C2D��Դ����. */
	public String resName;
	
	/** ͼƬ�Ƿ񱻻���. */
	public boolean imgConfused = false;
	
	

	/**
	 * ������Դ���ƴ���C2D����������Դ�������ᱻ�Զ�����������ʹ��C2D����������Դ������Ϊ����Ŀ¼.
	 *
	 * @param resNameT String C2D�����Դ�ļ�����
	 * @param readAllTxt boolean �Ƿ����ı��������ڳ�ʼ��ʱ��һ���Զ�ȡ�����ı��������TextsManager
	 */
	public C2D_FrameManager(String resNameT, boolean readAllTxt)
	{
		resName = resNameT.substring(0, resNameT.lastIndexOf('.'));
		m_AniM = new C2D_SpriteManager(this);
		m_TxtM = new C2D_TextManager(this, readAllTxt);
		m_PropM = new C2D_PropManager(this);
		m_SptM = new C2D_ScriptManager(this);
	}

	/**
	 * ���������Ƿ񱻻���(ĿǰC2D�ṹ�е����ݻ���������ָͼƬ����).
	 *
	 * @return boolean �������
	 */
	public boolean isDataConfused()
	{
		return imgConfused;
	}

	/**
	 * ���C2D��Դ����.
	 *
	 * @return String C2D��Դ����
	 */
	public String getResName()
	{
		return resName;
	}

	/**
	 * �ͷ���Դ��C2DManager�е��������ݱ��ͷţ����еĹ������е���ԴҲ�������ͷţ�.
	 */
	@Override
	public void onRelease()
	{
		if(m_AniM!=null)
		{
			m_AniM.doRelease(this);
			m_AniM=null;
		}
		if(m_TxtM!=null)
		{
			m_TxtM.doRelease(this);
			m_TxtM=null;
		}
		if(m_SptM!=null)
		{
			m_SptM.doRelease(this);
			m_SptM=null;
		}
		if(m_PropM!=null)
		{
			m_PropM.doRelease(this);
			m_PropM=null;
		}
	}
}
