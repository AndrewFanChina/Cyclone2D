package game.tutorial.c2d;

import c2d.frame.com.C2D_PicBox;
import c2d.frame.ext.scene.C2D_SceneUtil;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * -----L02-02、图片和图片切块----- <br>
 * 本节介绍图片的创建方式和内存占用，每个C2D_Image都代表了一张资源图片。<br>
 * 图片可以根据从jar包所携带的资源中直接被创建出来。也可以从网络进行加载，<br>
 * 还可以使用若干张图合成，最终形成C2D_Image，图片在游戏中一般是消耗内存<br>
 * 最多的资源，所以要谨慎处理图片的内存占用。
 * @author AndrewFan
 * 
 */
public class L02_02_ImageAndClip extends C2D_SceneUtil implements LessonUtil
{
	public L02_02_ImageAndClip()
	{
	}
	public String getBvrNodeName()
	{
		return "L02_02_ImageAndClip";
	}
	protected void onAddedToStage()
	{
		if (!isEmpty())
		{
			return;
		}
		//纯黑的背景色
		setBGColor(0x0);
		//第一个图片框，使用文件名构建。默认文件夹处于res/imgs_other文件夹
		C2D_PicBox p1=new C2D_PicBox("horse.png");
		addChild(p1,1);
		p1.setPosTo(100, 20);
		//第二个图片框，使用创建好的图片构建
		C2D_Image img1 = C2D_Image.createImage("superman.png");
		C2D_PicBox p2=new C2D_PicBox(img1);
		addChild(p2,2);
		p2.setPosTo(200, 20);
		//第三个图片框，使用创建好的图片切块构建
		C2D_ImageClip ic1=new C2D_ImageClip(img1);
		ic1.setContentRect(50, 0, 70, 95);
		C2D_PicBox p3=new C2D_PicBox(ic1);
		addChild(p3,3);
		p3.setPosTo(400, 20);
		//第四个图片框，使用创建好的网络图片构建
		C2D_Image img4 = C2D_Image.createHttpImage("http://www.baidu.com/img/bdlogo.gif");
		C2D_PicBox p4=new C2D_PicBox(img4);
		addChild(p4,4);
		p4.setPosTo(200, 350);
		
		//添加移动事件
		p3.Events_KeyPress().add(m_moveEvent);
		//设置返回键退出
		setBackKey(key_back);
	}

	protected void onPaint(C2D_Graphics g)
	{
		super.onPaint(g);
	}

	protected void onRemovedFromStage()
	{
		onRelease();
	}

	protected void onSentBack()
	{
	}

	protected void onSentTop()
	{
	}

	protected void onShown()
	{
	}

	protected void onHidden()
	{
	}
}
