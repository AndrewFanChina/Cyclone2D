package game.tutorial.c2d;

import c2d.frame.com.text.C2D_PTC_AvgW;
import c2d.frame.com.text.C2D_PTC_DynW;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;
import c2d.plat.gfx.C2D_Image;
/**
 * -----L02-05、使用图片文本框----- <br>
 * 图片文本框(C2D_PicTextBox)，用于显示图片文本。<br>
 * FIXME ....
 * 
 * @author AndrewFan
 *
 */
public class L02_05_PictureTxtBox extends C2D_SceneUtil implements LessonUtil
{
	C2D_Image m_fntImage;
	C2D_PTC_DynW m_dynCfg;
	public L02_05_PictureTxtBox()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_05_PictureTxtBox";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		setBGColor(0xFF);
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		
		//使用等宽配置来构建图片文本框
		C2D_PTC_AvgW avgCfg=new C2D_PTC_AvgW();
		m_fntImage = C2D_Image.createImage("nums.png");
		avgCfg.setParameters(m_fntImage, 0, 0, 13, 19, 0, 4, "0123456789FPS:");
		C2D_PicTextBox txtBox1 = new C2D_PicTextBox(avgCfg);
		stayView1.addChild(txtBox1,10);
		txtBox1.setPosTo(200, 100);
		txtBox1.setText("SPF:012\n3456789");
		//使用动态宽度配置来构建图片文本框
		m_dynCfg=new C2D_PTC_DynW();
		m_dynCfg.loadFromPTLib("fps");
		C2D_PicTextBox txtBox2 = new C2D_PicTextBox(m_dynCfg);
		stayView1.addChild(txtBox2,20);
		txtBox2.setPosTo(200, 200);
		txtBox2.setText("SPF:012\n3456789");
		txtBox2.Events_KeyPress().add(m_moveEvent);
		//设置返回键退出
		setBackKey(key_back);
	}
	public void onRelease()
	{
		super.onRelease();
		if(m_fntImage!=null)
		{
			m_fntImage.releaseBitmap();
			m_fntImage=null;
		}
		if(m_dynCfg!=null)
		{
			m_dynCfg.doRelease(this);
			m_dynCfg=null;
		}
	}
	
}
