package c2d.frame.com.view;

import c2d.frame.base.C2D_View;
import c2d.frame.com.C2D_PicBox;
import c2d.frame.com.shape.C2D_Circle;
import c2d.frame.com.shape.C2D_Rectangle;
import c2d.frame.com.text.C2D_PTC;
import c2d.frame.com.text.C2D_PicTextBox;
import c2d.frame.com.text.C2D_TextBox;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.C2D_Sprite;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

public class C2D_ViewUtil extends C2D_View
{
	/**
	 * 在指定的位置添加居中对齐的图片框
	 * 
	 * @param fileName
	 *            图片名称
	 * @param x
	 * @param y
	 * @param z
	 * @return 图片框
	 */
	public C2D_PicBox addPicBox(String fileName, int x, int y, int z)
	{
		C2D_PicBox p1 = new C2D_PicBox(fileName);
		addChild(p1);
		p1.setPosTo(x, y);
		p1.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		p1.setZOrder(z);
		return p1;
	}

	/**
	 * 在指定的位置添加居中对齐的图片框
	 * 
	 * @param ic
	 *            图片切块
	 * @param x
	 * @param y
	 * @param z
	 * @return 图片框
	 */
	public C2D_PicBox addPicBox(C2D_ImageClip ic, int x, int y, int z)
	{
		C2D_PicBox p1 = new C2D_PicBox(ic);
		addChild(p1);
		p1.setPosTo(x, y);
		p1.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		p1.setZOrder(z);
		return p1;
	}

	/**
	 * 在指定的位置添加图片框
	 * 
	 * @param fileName
	 *            图片名称
	 * @param x
	 * @param y
	 * @param z
	 * @param anchor 锚点
	 * @return 图片框
	 */
	public C2D_PicBox addPicBox(String fileName, int x, int y, int z,int anchor)
	{
		C2D_PicBox p1 = new C2D_PicBox(fileName);
		addChild(p1);
		p1.setPosTo(x, y);
		p1.setZOrder(z);
		p1.setAnchor(anchor);
		return p1;
	}

	/**
	 * 在指定的位置添加居中对齐的图片框
	 * 
	 * @param img
	 *            图片
	 * @param x
	 * @param y
	 * @param z
	 * @return 图片框
	 */
	public C2D_PicBox addPicBox(C2D_Image img, int x, int y, int z)
	{
		C2D_PicBox p1 = new C2D_PicBox(img);
		addChild(p1);
		p1.setPosTo(x, y);
		p1.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		p1.setZOrder(z);
		return p1;
	}

	/**
	 * 在指定的位置添加图片文本框
	 * 
	 * @param content
	 *            文本内容
	 * @param cfg
	 *            文本配置
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @return 图片文本框
	 */
	public C2D_PicTextBox addPicTxtBox(String content, C2D_PTC cfg, int x, int y, int z)
	{
		C2D_PicTextBox ptb = new C2D_PicTextBox(cfg);
		addChild(ptb);
		ptb.setText(content);
		ptb.setPosTo(x, y);
		ptb.setZOrder(z);
		ptb.setAnchor(C2D_Consts.HVCENTER);
		return ptb;
	}

	/**
	 * 在指定的位置添加一个居中显示的文本框，并设置指定的文本内容
	 * 
	 * @param content
	 *            文本框内容
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param color
	 *            文本颜色
	 * @return 文本框
	 */
	public C2D_TextBox addTxtBox(String content, float x, float y, int z, int color)
	{
		C2D_TextBox txtBox = new C2D_TextBox();
		txtBox.setText(content);
		txtBox.setPosTo(x, y);
		txtBox.setZOrder(z);
		txtBox.setAnchor(C2D_Consts.HCENTER | C2D_Consts.VCENTER);
		txtBox.setColor(color);
		addChild(txtBox);
		return txtBox;
	}

	/**
	 * 在指定的位置添加一个文本框，并设置指定的文本内容
	 * 
	 * @param content
	 *            文本框内容
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param anchor 锚点
	 * @param color
	 *            文本颜色
	 * @return 文本框
	 */
	public C2D_TextBox addTxtBox(String content, float x, float y, int z,int anchor, int color)
	{
		C2D_TextBox txtBox = new C2D_TextBox();
		addChild(txtBox);
		txtBox.setText(content);
		txtBox.setPosTo(x, y);
		txtBox.setZOrder(z);
		txtBox.setColor(color);
		txtBox.setAnchor(anchor);
		return txtBox;
	}

	/**
	 * 在指定的位置添加一个矩形框
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param w 宽度
	 * @param h 高度
	 * @param anchor 锚点
	 * @param color
	 *            颜色
	 * @return 矩形框
	 */
	public C2D_Rectangle addRectBox(float x, float y, int z, float w, float h, int anchor, int color)
	{
		C2D_Rectangle rect = new C2D_Rectangle();
		addChild(rect);
		rect.setPosTo(x, y);
		rect.setSize(w, h);
		rect.setZOrder(z);
		rect.setAnchor(anchor);
		rect.setBgColor(color);
		return rect;
	}
	/**
	 * 在指定的位置添加一个带边框的矩形框
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param w 宽度
	 * @param h 高度
	 * @param anchor 锚点
	 * @param color
	 *            颜色
	 * @param borderColor
	 *            边框颜色
	 * @return 矩形框
	 */
	public C2D_Rectangle addBorderRectBox(float x, float y, int z, int w, int h, int anchor, int color,int borderColor)
	{
		C2D_Rectangle rect = new C2D_Rectangle();
		addChild(rect);
		rect.setPosTo(x, y);
		rect.setSize(w, h);
		rect.setZOrder(z);
		rect.setAnchor(anchor);
		rect.setBgColor(color);
		rect.setBorderColor(borderColor);
		return rect;
	}
	/**
	 * 在指定的位置添加一个圆形
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param w 宽度
	 * @param h 高度
	 * @param anchor 锚点
	 * @param color
	 *            颜色
	 * @return 矩形框
	 */
	public C2D_Rectangle addCircleBox(float x, float y, int z, int w, int h, int anchor, int color)
	{
		C2D_Circle circle = new C2D_Circle();
		addChild(circle);
		circle.setPosTo(x, y);
		circle.setSize(w, h);
		circle.setZOrder(z);
		circle.setAnchor(anchor);
		circle.setBgColor(color);
		return circle;
	}
	/**
	 * 在指定的位置添加一个圆形
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * @param z zOrder
	 * @param w 宽度
	 * @param h 高度
	 * @param anchor 锚点
	 * @param color
	 *            颜色
	 * @param borderColor
	 *            边框颜色
	 * @return 矩形框
	 */
	public C2D_Rectangle addBorderCircleBox(int x, int y, int z, int w, int h, int anchor, int color,int borderColor)
	{
		C2D_Circle circle = new C2D_Circle();
		addChild(circle);
		circle.setPosTo(x, y);
		circle.setSize(w, h);
		circle.setZOrder(z);
		circle.setAnchor(anchor);
		circle.setBgColor(color);
		circle.setBorderColor(borderColor);
		return circle;
	}
	/**
	 * 在指定的位置添加一个视图
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @param h
	 * @param anchor
	 *            锚点
	 * @return 创建的视图
	 */
	public C2D_ViewUtil addView(float x, float y, int z, float w, float h, int anchor)
	{
		C2D_ViewUtil view = new C2D_ViewUtil();
		addChild(view);
		view.setPosTo(x, y);
		view.setSize(w, h);
		view.setZOrder(z);
		view.setAnchor(anchor);
		return view;
	}

	/**
	 * 在内部添加一个视图
	 * 
	 * @param marginX
	 *            内部视图的X边距
	 * @param marginY
	 *            内部视图的Y边距
	 * @param z
	 *            内部视图的深度
	 * @param color
	 *            内部视图的背景颜色
	 * @return 创建的视图
	 */
	public C2D_ViewUtil addInnerView(int marginX, int marginY, int z, int color)
	{
		C2D_ViewUtil view = new C2D_ViewUtil();
		addChild(view);
		float w = getWidth() - marginX * 2;
		float h = getHeight() - marginY * 2;
		view.setPosTo(marginX, marginY);
		view.setSize(w, h);
		view.setZOrder(z);
		view.setBGColor(color);
		return view;
	}

	/**
	 * 添加一个精灵到当前容器
	 * @param fm 动画框架
	 * @param folderID 文件夹ID
	 * @param spriteID 精灵ID
	 * @param x X坐标
	 * @param y Y坐标
	 * @param z Z深度
	 * @return 新创建的精灵
	 */
	public C2D_Sprite addSprite(C2D_FrameManager fm, short folderID, short spriteID, int x, int y, int z)
	{
		return addSprite(fm,folderID,spriteID,x,y,z,0);
	}
	/**
	 * 添加一个精灵到当前容器
	 * @param fm 动画框架
	 * @param spriteID 精灵ID
	 * @param x X坐标
	 * @param y Y坐标
	 * @param z Z深度
	 * @param folderID 文件夹ID
	 * @param actionID 动作ID
	 * @return 新创建的精灵
	 */
	public C2D_Sprite addSprite(C2D_FrameManager fm, short folderID, short spriteID, int x, int y, int z,int actionID)
	{
		C2D_Sprite sprite=new C2D_Sprite(fm,folderID, spriteID);
		addChild(sprite,z);
		sprite.setPosTo(x, y);
		sprite.setAction(actionID);
		return sprite;
	}
}
