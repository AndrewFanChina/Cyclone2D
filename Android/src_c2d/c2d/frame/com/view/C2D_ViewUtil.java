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
	 * ��ָ����λ����Ӿ��ж����ͼƬ��
	 * 
	 * @param fileName
	 *            ͼƬ����
	 * @param x
	 * @param y
	 * @param z
	 * @return ͼƬ��
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
	 * ��ָ����λ����Ӿ��ж����ͼƬ��
	 * 
	 * @param ic
	 *            ͼƬ�п�
	 * @param x
	 * @param y
	 * @param z
	 * @return ͼƬ��
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
	 * ��ָ����λ�����ͼƬ��
	 * 
	 * @param fileName
	 *            ͼƬ����
	 * @param x
	 * @param y
	 * @param z
	 * @param anchor ê��
	 * @return ͼƬ��
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
	 * ��ָ����λ����Ӿ��ж����ͼƬ��
	 * 
	 * @param img
	 *            ͼƬ
	 * @param x
	 * @param y
	 * @param z
	 * @return ͼƬ��
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
	 * ��ָ����λ�����ͼƬ�ı���
	 * 
	 * @param content
	 *            �ı�����
	 * @param cfg
	 *            �ı�����
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @return ͼƬ�ı���
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
	 * ��ָ����λ�����һ��������ʾ���ı��򣬲�����ָ�����ı�����
	 * 
	 * @param content
	 *            �ı�������
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param color
	 *            �ı���ɫ
	 * @return �ı���
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
	 * ��ָ����λ�����һ���ı��򣬲�����ָ�����ı�����
	 * 
	 * @param content
	 *            �ı�������
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param anchor ê��
	 * @param color
	 *            �ı���ɫ
	 * @return �ı���
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
	 * ��ָ����λ�����һ�����ο�
	 * 
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param w ���
	 * @param h �߶�
	 * @param anchor ê��
	 * @param color
	 *            ��ɫ
	 * @return ���ο�
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
	 * ��ָ����λ�����һ�����߿�ľ��ο�
	 * 
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param w ���
	 * @param h �߶�
	 * @param anchor ê��
	 * @param color
	 *            ��ɫ
	 * @param borderColor
	 *            �߿���ɫ
	 * @return ���ο�
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
	 * ��ָ����λ�����һ��Բ��
	 * 
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param w ���
	 * @param h �߶�
	 * @param anchor ê��
	 * @param color
	 *            ��ɫ
	 * @return ���ο�
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
	 * ��ָ����λ�����һ��Բ��
	 * 
	 * @param x x����
	 * @param y y����
	 * @param z zOrder
	 * @param w ���
	 * @param h �߶�
	 * @param anchor ê��
	 * @param color
	 *            ��ɫ
	 * @param borderColor
	 *            �߿���ɫ
	 * @return ���ο�
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
	 * ��ָ����λ�����һ����ͼ
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @param h
	 * @param anchor
	 *            ê��
	 * @return ��������ͼ
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
	 * ���ڲ����һ����ͼ
	 * 
	 * @param marginX
	 *            �ڲ���ͼ��X�߾�
	 * @param marginY
	 *            �ڲ���ͼ��Y�߾�
	 * @param z
	 *            �ڲ���ͼ�����
	 * @param color
	 *            �ڲ���ͼ�ı�����ɫ
	 * @return ��������ͼ
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
	 * ���һ�����鵽��ǰ����
	 * @param fm �������
	 * @param folderID �ļ���ID
	 * @param spriteID ����ID
	 * @param x X����
	 * @param y Y����
	 * @param z Z���
	 * @return �´����ľ���
	 */
	public C2D_Sprite addSprite(C2D_FrameManager fm, short folderID, short spriteID, int x, int y, int z)
	{
		return addSprite(fm,folderID,spriteID,x,y,z,0);
	}
	/**
	 * ���һ�����鵽��ǰ����
	 * @param fm �������
	 * @param spriteID ����ID
	 * @param x X����
	 * @param y Y����
	 * @param z Z���
	 * @param folderID �ļ���ID
	 * @param actionID ����ID
	 * @return �´����ľ���
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
