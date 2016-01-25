package c2d.gutil.misc;

import c2d.frame.base.C2D_TransScene;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.shape.C2D_Rectangle;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Image;
/**
 * ���س�����������ͼƬ������Դ���������ؽ��档
 * һ��logoͼƬ�����ڴ���logo��Ĭ��ˮƽ���У���ֱ�����ϲ���
 * һ�Ž�����ͼƬ�����ڴ�����������Ĭ��ˮƽ���У���ֱ�����²���
 * ��������ԴҪ�������ǰ���ͱ����������������ŷ���һ��ͼƬ�ڣ�
 * ���ҿ����ͬ����ͷ�����в���͸����Ϊ�����
 * @author AndrewFan
 *
 */
public class C2D_LoadingScene extends C2D_TransScene
{
	protected C2D_PicBox m_processBg;
	protected C2D_PicBox m_process;
	protected C2D_PicBox m_logo;
	protected C2D_Image m_imgProcess;
	protected C2D_Rectangle m_rect;
	/** ������ͼƬ���� */
	protected String m_img_process;
	/** LOGOͼƬ���� */
	protected String m_img_logo;
	/** ������ͼƬ��LOGOͼƬ֮��ļ�� */
	protected int m_cGap=10;
	/** ������ǰ���߶� */
	protected int m_processH;
	protected String m_folderName=C2D_Consts.STR_OTHER_IMGS;
	public C2D_LoadingScene(String img_logo,String img_process,int processH)
	{
		m_img_logo=img_logo;
		m_img_process=img_process;
		m_processH =processH;
	}
	public C2D_LoadingScene(String subFolder,String img_logo,String img_process,int processH)
	{
		m_folderName=subFolder;
		m_img_logo=img_logo;
		m_img_process=img_process;
		m_processH =processH;
	}
	protected void onAddedToStage()
	{
		super.onAddedToStage();
		setBGColor(0x0);
		if(m_rect==null)
		{
			m_rect=new C2D_Rectangle();
			m_rect.setSize(getWidth(), getHeight());
			m_rect.setZOrder(0);
			m_rect.setBgColor(0);
			addChild(m_rect);
		}
		if (m_imgProcess == null&&m_img_process!=null&&m_img_process.length()>0)
		{
			m_imgProcess = C2D_Image.createImage(m_img_process,m_folderName);
			if(m_imgProcess!=null)
			{
				m_imgProcess.transHadler(this);	
			}
		}
		float y1 = getHeight()*2/5;
		float y2 = getHeight()*4/5;
		//logo
		if (m_logo == null&&m_img_logo!=null&&m_img_logo.length()>0)
		{
			C2D_Image logoImg = C2D_Image.createImage(m_img_logo,m_folderName);
			if(logoImg!=null)
			{
				if(m_imgProcess!=null)
				{
					//����λ�ã����¸�Ԥ��3Gap
					float hGap=(getHeight()-logoImg.bitmapWidth()-m_imgProcess.bitmapHeight()-m_cGap)/2;
					y1=hGap+logoImg.bitmapHeight()/2;
					y2=getHeight()-hGap-logoImg.bitmapHeight();	
				}
				else
				{
					y1=getHeight()*5/10;
				}
				//���LOGO
				m_logo = new C2D_PicBox(logoImg);
				logoImg.transHadler(m_logo);
				m_logo.setZOrder(1);
				addChild(m_logo);
				m_logo.setPosTo(getWidth() / 2, y1);
				m_logo.setAnchor(C2D_Consts.HVCENTER);	
			}
			else
			{
				y2=getHeight()*5/10;
			}
		}

		if(m_imgProcess!=null)
		{
			int w=m_imgProcess.bitmapWidth();
			int h=m_imgProcess.bitmapHeight();
			//processBg
			if (m_processBg == null)
			{
				m_processBg = new C2D_PicBox(m_imgProcess);
				m_processBg.setContentRect(0, m_processH, w, h-m_processH);
				m_processBg.setZOrder(2);
				addChild(m_processBg);
				m_processBg.setPosTo((getWidth() - w) / 2, y2);
				m_processBg.setAnchor(C2D_Consts.VCENTER);
			}
			//process
			if (m_process == null)
			{
				m_process = new C2D_PicBox(m_imgProcess);
				m_process.setContentRect(0, 0, w, m_processH);
				m_process.setZOrder(3);
				addChild(m_process);
				m_process.setPosTo((getWidth() - w) / 2, y2);
				m_process.setAnchor(C2D_Consts.VCENTER);
				m_process.setContentW(0);
			}	
		}
	}

	public void onProcessChanged(int process)
	{
		if(m_process!=null&&m_imgProcess!=null)
		{
			m_process.setContentW((int)(process * m_imgProcess.bitmapWidth() / 100));	
		}
	}
	public void onRelease()
	{
		super.onRelease();
		if (m_imgProcess != null)
		{
			m_imgProcess.doRelease(this);
			m_imgProcess = null;
		}
		m_processBg = null;
		m_process = null;
		m_logo = null;
		m_rect=null;
	}
	/**
	 * ��ȡ������ͼƬ��LOGOͼƬ֮��ļ��
	 * @return �м���
	 */
	public int getCenterGap()
	{
		return m_cGap;
	}
	/**
	 * ���ý�����ͼƬ��LOGOͼƬ֮��ļ��
	 * @param cGap �м���
	 */
	public void setCenterGap(int cGap)
	{
		this.m_cGap = cGap;
	}
	protected void onLoadBegin()
	{
	}
	protected void onLoadEnd()
	{
	}
}
