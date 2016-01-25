package game.tutorial.c2d;

import c2d.frame.com.text.C2D_TextBox;
import c2d.frame.com.view.C2D_ViewUtil;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.lang.app.C2D_Device;
import c2d.mod.C2D_Consts;
/**
 * -----L02-04、使用系统文本框----- <br>
 * 系统文本框(C2D_TextBox)，用于显示系统生成的文本。<br>
 * 在J2me中，这种文本大小和字体不可控，颜色可以设置，在Android中，可以使用默认的手机字体，<br>
 * 也可以使用自己配置的字体文件,大小、字体、颜色等都是可以控制的。系统文本的绘制速度是很慢的，<br>
 * 在J2me中可以适当考虑使用C2D_BufferedView进行缓冲，提升绘图效率。另外也可以选择后面<br>
 * 会提及的图片文本框，那里会使用预先生成的图片来替代文字显示。<br>
 * 系统文本框是占据一块区域的文本段落，你可以使用不同的构造函数来确定使用单行文本还是多行文本。<br>
 * 文本框有受限制的宽度和高度，你可以通过setLimitSize(int limitW, int limitH)来<br>
 * 设置受限尺寸，通过setMargin(int, int)来设置内容边距，即内容呈现时离上下左右边框的<br>
 * 间隔。内容设置可以通过setText(String),如果是多行文本，内容将实现自动换行，除了越界的<br>
 * 换行之外，'\n'的换行符也会引起换行。可以通过setColor(C2D_Color)来设置文本的颜色。<br>
 * setTxtBgColor(C2D_Color)可以用于设置文本框的背景颜色。另外如果需要带边缘颜色的文本。<br>
 * 可以通过设置setBorderColor(C2D_Color)来获得想要的效果，这样的文本会大幅增加绘图性能消耗。<br>
 * 如果需要让文本实现按行滚动效果，可以调用其scrollFront()和scrollBack()方法。<br>
 * 
 * @author AndrewFan
 *
 */
public class L02_04_TxtBox extends C2D_SceneUtil implements LessonUtil
{

	public L02_04_TxtBox()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_04_TxtBox";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		setBGColor(0xFF);
		C2D_ViewUtil stayView1 = addInnerView(50, 50, 1, 0x666666);
		//使用默认的方式构造多行文本框
		C2D_TextBox txtBox1 = new C2D_TextBox();
		txtBox1.setText("故垒西边\n人道是\n三国周郎赤壁");
		txtBox1.setPosTo(stayView1.getWidth()/2, stayView1.getHeight()/2);
		txtBox1.setZOrder(1);
		txtBox1.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		txtBox1.setColor(0xFFFFFF);
		//限制显示的宽度60
		txtBox1.setLimitWidth(60);
		stayView1.addChild(txtBox1);
		//使用便利函数构造多行文本
		C2D_TextBox txtBox2 = stayView1.addTxtBox("大江东去浪淘尽\n千古风流人物", stayView1.getWidth()/2, stayView1.getHeight()/4, 2, 0x00FF00);
		txtBox2.Events_KeyPress().add(m_moveEvent);
		//限制显示的宽度60
		txtBox2.setLimitWidth(60);
		//构造单行文本，限制显示宽度120
		C2D_TextBox txtBox3=new C2D_TextBox(120);
		txtBox3.setText("乱石穿空，惊涛拍岸，卷起千堆雪。");
		txtBox3.setPosTo(stayView1.getWidth()/2, stayView1.getHeight()*3/4);
		txtBox3.setZOrder(1);
		txtBox3.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		txtBox3.setColor(0xFF0000);
		stayView1.addChild(txtBox3);
		//设置返回键退出
		setBackKey(key_back);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}
}
