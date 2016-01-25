package c2d.frame.com;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_ImageClip;

public class C2D_Button extends C2D_PicBox implements C2D_Consts
{
	/** °´ÏÂÇÐÆ¬ */
	private C2D_ImageClip m_btnPressedClip;
	/** ½¹µãÇÐÆ¬ */
	private C2D_ImageClip m_btnFocusedClip;



	/**
	 * °´Å¥×é¼þ¹¹Ôìº¯Êý
	 * 
	 * @param bgClip
	 *            ±³¾°×´Ì¬ÇÐÆ¬
	 * @param focusClip
	 *            ½¹µã×´Ì¬ÇÐÆ¬
	 * @param pressedClip
	 *            °´ÏÂ×´Ì¬ÇÐÆ¬
	 */
	public C2D_Button(C2D_ImageClip bgClip, C2D_ImageClip focusClip, C2D_ImageClip pressedClip)
	{
		super(bgClip);
		this.setFocusable(true);
		this.setBtnFocusedImage(focusClip);
		this.setBtnPressdImage(pressedClip);
	}
	/**
	 * ÉèÖÃ½¹µãÍ¼Æ¬ÇÐÆ¬
	 * 
	 * @param imgClip
	 *            ½¹µãÇÐÆ¬
	 */
	public void setBtnFocusedImage(C2D_ImageClip imgClip)
	{
		if(m_btnFocusedClip!=null)
		{
			m_btnFocusedClip.doRelease(this);
		}
		m_btnFocusedClip = imgClip;
	}
	/**
	 * ÉèÖÃ°´ÏÂµÄÍ¼Æ¬ÇÐÆ¬
	 * 
	 * @param imgClip
	 *            ½¹µãÇÐÆ¬
	 */
	public void setBtnPressdImage(C2D_ImageClip imgClip)
	{
		if(m_btnPressedClip!=null)
		{
			m_btnPressedClip.doRelease(this);
		}
		m_btnPressedClip = imgClip;
	}
	public C2D_Button cloneSelf()
	{
		C2D_ImageClip bgClip=null;
		C2D_ImageClip focusClip=null;
		C2D_ImageClip pressedClip=null;
		if(m_Clip!=null)
		{
			bgClip=m_Clip.cloneSelf();
		}
		if(m_focusImgClip!=null)
		{
			focusClip=m_focusImgClip.cloneSelf();
		}
		if(m_btnPressedClip!=null)
		{
			pressedClip=m_btnPressedClip.cloneSelf();
		}
		C2D_Button newBtn=new C2D_Button(bgClip, focusClip, pressedClip);
		return newBtn;
	}
	public C2D_Button transGfxPos(int x,int y)
	{
		if(m_Clip!=null)
		{
			m_Clip.setContentPos(m_Clip.getContentX()+x, m_Clip.getContentY()+y);
		}
		if(m_focusImgClip!=null)
		{
			m_focusImgClip.setContentPos(m_focusImgClip.getContentX()+x, m_focusImgClip.getContentY()+y);
		}
		if(m_btnPressedClip!=null)
		{
			m_btnPressedClip.setContentPos(m_btnPressedClip.getContentX()+x, m_btnPressedClip.getContentY()+y);
		}
		return this;
	}

	/**
	 * Ë¢ÐÂ°´Å¥×´Ì¬
	 */
	private void refreshBtnState()
	{
		if (m_BtnState == Btn_Float)
		{
			setFocusImage(m_btnFocusedClip, m_focusX, m_focusY);
		}
		else if (m_BtnState == Btn_Focused)
		{
			setFocusImage(m_btnFocusedClip, m_focusX, m_focusY);
		}
		else if (m_BtnState == Btn_PressBegin)
		{
			setFocusImage(m_btnPressedClip, m_focusX, m_focusY);
		}
		else if (m_BtnState == Btn_ReleaseBegin)
		{
			setFocusImage(m_btnFocusedClip, m_focusX, m_focusY);
		}
	}



	public void onRelease()
	{
		super.onRelease();
		if (m_btnFocusedClip != null)
		{
			m_btnFocusedClip.doRelease();
			m_btnFocusedClip = null;
		}
		if (m_btnPressedClip != null)
		{
			m_btnPressedClip.doRelease();
			m_btnPressedClip = null;
		}
	}
}
