package game.core;

import c2d.frame.com.C2D_Button;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.text.C2D_PtcList;
import c2d.frame.ext.group.C2D_NumbericUpDown;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImgList;

public class ProAssets
{
	public static C2D_PtcList PtcList=new C2D_PtcList();
	public static C2D_ImgList ImageList_ht=new C2D_ImgList("imgs_ht/");
	
	//创建一个输入组合框
	public static C2D_NumbericUpDown CreateInputGroup()
	{
		//图片
		C2D_Image img=ImageList_ht.getImage("numInputBoxBg.png");
		//增加按钮
		C2D_Button btnInc=new C2D_Button(
				img.makeClip(133, 7, 25, 24), 
				img.makeClip(159, 7, 25, 24), 
				img.makeClip(186, 7, 25, 24));
		
		//降低按钮
		C2D_Button btnDec=btnInc.cloneSelf().transGfxPos(0, 24);	
		//输入框
		C2D_PicTextBox textBox=new C2D_PicTextBox(ProAssets.PtcList.getPTC("fps"));
		//组合框
		C2D_NumbericUpDown inputGroup=new C2D_NumbericUpDown(img.makeClip(0, 0, 127, 63), textBox, btnInc, btnDec);
		//设置子组件位置
		inputGroup.m_IncBtn.setPosTo(94, 8);
		inputGroup.m_DecBtn.setPosTo(94, 24+8);
		inputGroup.m_InputBox.setPosTo(48, 32);
		return inputGroup;
	}

	public static void releaseRes()
	{
		PtcList.clear();
		ImageList_ht.clear();
	}
}
