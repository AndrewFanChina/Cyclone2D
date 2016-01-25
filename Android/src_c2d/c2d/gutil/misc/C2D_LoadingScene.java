package c2d.gutil.misc;

import c2d.frame.base.C2D_TransScene;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.shape.C2D_Rectangle;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Image;
/**
 * 加载场景，从两张图片加载资源，产生加载界面。
 * 一张logo图片，用于创建logo，默认水平居中，垂直居于上部。
 * 一张进度条图片，用于创建进度条，默认水平居中，垂直居于下部。
 * 进度条资源要求进度条前景和背景从上至下依次排放在一张图片内，
 * 并且宽度相同，两头可以有部分透明作为间隔。
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
	/** 进度条图片名称 */
	protected String m_img_process;
	/** LOGO图片名称 */
	protected String m_img_logo;
	/** 进度条图片和LOGO图片之间的间隔 */
	protected int m_cGap=10;
	/** 进度条前景高度 */
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
					//调整位置，上下各预留3Gap
					float hGap=(getHeight()-logoImg.bitmapWidth()-m_imgProcess.bitmapHeight()-m_cGap)/2;
					y1=hGap+logoImg.bitmapHeight()/2;
					y2=getHeight()-hGap-logoImg.bitmapHeight();	
				}
				else
				{
					y1=getHeight()*5/10;
				}
				//添加LOGO
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
	 * 获取进度条图片和LOGO图片之间的间隔
	 * @return 中间间隔
	 */
	public int getCenterGap()
	{
		return m_cGap;
	}
	/**
	 * 设置进度条图片和LOGO图片之间的间隔
	 * @param cGap 中间间隔
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
