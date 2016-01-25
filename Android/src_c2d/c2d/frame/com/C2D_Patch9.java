package c2d.frame.com;

import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.math.type.C2D_SizeI;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 九片图类，可以拥有、设置、显示1张切片，这张切片沿X,Y方向被均分成3x3份， 四个边角块用于控件的四角铺设，四个边缘中心块用于边缘连续铺设，中间块用于
 * 水平和垂直方向上的内部区域连续铺设。每次重新设置切块大小之后，都将采用默认 的3x3均匀分割，此时还可以设置边缘大小以实现不同的区块分布。
 * 
 * @author AndrewFan
 */
public class C2D_Patch9 extends C2D_PicBox
{
	/** 控件宽度. */
	protected int m_width;
	/** 控件高度. */
	protected int m_height;
	/** 中间分块宽度. */
	protected int m_partW;
	/** 分块高度. */
	protected int m_partH;
	/** 边缘分块宽度. */
	protected int m_adgeW;
	/** 边缘分块高度. */
	protected int m_adgeH;
	/** 填充颜色，如果此值存在，则将使用此颜色填充中间平铺区域 */
	protected C2D_Color m_fillColor;

	/**
	 * 使用给定的图片来创建图片控件，将使用整个图片产生切片
	 * 
	 * @param image
	 *            图片
	 */
	public C2D_Patch9(C2D_Image image)
	{
		super(image);
	}

	/**
	 * 使用给定的图片切块来创建图片控件，将使用此切片产生三块分切片
	 * 
	 * @param imageClip
	 *            图片切块
	 */
	public C2D_Patch9(C2D_ImageClip imageClip)
	{
		super(imageClip);
	}

	/**
	 * 直接从jar中加载图片并创建图片控件，默认文件夹位于imgs_ohter， 内部使用C2D_Image.createImage()来创建图片
	 * 
	 * @param imageName
	 */
	public C2D_Patch9(String imageName)
	{
		C2D_Image image = C2D_Image.createImage(imageName);
		if (image != null)
		{
			setImage(image);
		}

	}

	/**
	 * 直接从jar中加载图片并创建图片控件，指定位于res下的子文件夹名称，
	 * 内部使用C2D_Image.createImage(String,String)来创建图片
	 * 
	 * @param imageName
	 */
	public C2D_Patch9(String imageName, String subFolder)
	{
		super(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * 设置控件宽度
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 */
	public void setSize(float width, float height)
	{
		m_width = (int)width;
		m_height = (int)height;
		layoutChanged();
	}

	/**
	 * 设置填充颜色，如果填充颜色存在，则将使用此颜色填充中间平铺区域。
	 * 
	 * @param fillColor
	 *            填充颜色，可以为null表示不使用颜色填充
	 */
	public void setFillColor(C2D_Color fillColor)
	{
		m_fillColor = fillColor;
		layoutChanged();
	}

	/**
	 * 获取控件宽度
	 * 
	 * @return 控件宽度
	 */
	public float getWidth()
	{
		return m_width;
	}

	/**
	 * 获取控件高度
	 * 
	 * @return 控件高度
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Image m_image = m_Clip.getImage();
		C2D_RectS m_contentRect = m_Clip.getContentRect();
		byte m_transform = m_Clip.getTransform();
		if (m_image == null || g == null || !m_visible || m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		C2D_PointF lt = getLeftTop();
		float ltX = lt.m_x;
		float ltY = lt.m_y;

		int x = 0;
		int y = 0;
		// 四个角落
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y, m_adgeW, m_adgeH, m_transform, ltX + x, ltY + y, 0);
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y, m_adgeW, m_adgeH, m_transform, ltX + x + m_width - m_adgeW, ltY + y, 0);
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y + m_contentRect.m_height - m_adgeH, m_adgeW, m_adgeH, m_transform, ltX + x, ltY + y + m_height - m_adgeH, 0);
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y + m_contentRect.m_height - m_adgeH, m_adgeW, m_adgeH, m_transform, ltX + x + m_width - m_adgeW, ltY + y + m_height - m_adgeH, 0);
		// 第一行
		paintRow(g, m_adgeW, 0, m_adgeW, 0, m_adgeH);
		// 最终行
		paintRow(g, m_adgeW, m_height - m_adgeH, m_adgeW, m_contentRect.m_height - m_adgeH);
		// 第一列
		paintCol(g, 0, m_adgeH, 0, m_adgeH, m_adgeW);
		// 最终列
		paintCol(g, m_width - m_adgeW, m_adgeH, m_contentRect.m_width - m_adgeW, m_adgeH, m_adgeW);
		// 中间
		x = m_adgeW;
		y = m_adgeH;
		if (m_fillColor == null)
		{
			while (y + m_partH < m_height - m_adgeH)
			{
				paintRow(g, x, y, m_adgeW, m_adgeH);
				y += m_partH;
			}
			// 补齐
			if (y < m_height - m_adgeH)
			{
				paintRow(g, x, y, m_adgeW, m_adgeH, m_height - m_adgeH - y);
			}
		}
		else
		{
			g.fillRect(ltX + x, ltY + y, m_width - m_adgeW - x, m_height - m_adgeH - y, m_fillColor.getColor());
		}
		// 焦点
		paintFocus(g, m_contentRect.m_width, m_contentRect.m_height);
	}

	/**
	 * 绘制一行，以指定的高度
	 * 
	 * @param g
	 * @param x
	 *            目标x,即相对控件左上角位置的x坐标
	 * @param y
	 *            目标y,即相对控件左上角位置的y坐标
	 * @param _x
	 *            源图x,即被绘区域相对九片图切块左上角位置的x坐标
	 * @param _y
	 *            源图y,即被绘区域相对九片图切块左上角位置的y坐标
	 * @param _h
	 *            要绘制的高度
	 * 
	 */
	private void paintRow(C2D_Graphics g, float x, float y, int _x, int _y, int _h)
	{
		C2D_PointF lt = getLeftTop();
		C2D_RectS m_contentRect = m_Clip.getContentRect();
		C2D_Image m_image = m_Clip.getImage();
		byte m_transform = m_Clip.getTransform();
		float ltX = lt.m_x;
		float ltY = lt.m_y;
		while (x + m_partW < m_width - m_adgeW)
		{
			g.drawRegion(m_image, m_contentRect.m_x + _x, m_contentRect.m_y + _y, m_partW, _h, m_transform, ltX + x, ltY + y, 0);
			x += m_partW;
		}
		// 补齐
		if (x < m_width - m_adgeW)
		{
			g.drawRegion(m_image,m_contentRect.m_x + _x, m_contentRect.m_y + _y, m_width - m_adgeW - (int)x, _h,m_transform, ltX + x, ltY + y,  0);
			x = m_width - m_adgeW;
		}
	}

	/**
	 * 绘制一行，以中间分块高度
	 * 
	 * @param g
	 * @param x
	 *            目标x,即相对控件左上角位置的x坐标
	 * @param y
	 *            目标y,即相对控件左上角位置的y坐标
	 * @param _x
	 *            源图x,即被绘区域相对九片图切块左上角位置的x坐标
	 * @param _y
	 *            源图y,即被绘区域相对九片图切块左上角位置的y坐标
	 */
	private void paintRow(C2D_Graphics g, float x, float y, int _x, int _y)
	{
		paintRow(g, x, y, _x, _y, m_partH);
	}

	/**
	 * 绘制一列，以指定的宽度
	 * 
	 * @param g
	 * @param x
	 *            目标x,即相对控件左上角位置的x坐标
	 * @param y
	 *            目标y,即相对控件左上角位置的y坐标
	 * @param _x
	 *            源图x,即被绘区域相对九片图切块左上角位置的x坐标
	 * @param _y
	 *            源图y,即被绘区域相对九片图切块左上角位置的y坐标
	 * @param _w
	 *            要绘制的宽度
	 */
	private void paintCol(C2D_Graphics g, float x, float y, int _x, int _y, int _w)
	{
		C2D_PointF lt = getLeftTop();
		C2D_RectS m_contentRect = m_Clip.getContentRect();
		C2D_Image m_image = m_Clip.getImage();
		byte m_transform = m_Clip.getTransform();
		float ltX = lt.m_x;
		float ltY = lt.m_y;
		while (y + m_partH < m_height - m_adgeH)
		{
			g.drawRegion(m_image,m_contentRect.m_x + _x, m_contentRect.m_y + _y, _w, m_partH, m_transform, ltX + x, ltY + y,  0);
			y += m_partH;
		}
		// 补齐
		if (y < m_height - m_adgeH)
		{
			g.drawRegion(m_image, m_contentRect.m_x + _x, m_contentRect.m_y + _y, _w, m_height -m_adgeH - (int)y, m_transform,ltX + x, ltY + y, 0);
			y = m_height - m_adgeH;
		}
	}

	/**
	 * 绘制一列，以中间分块宽度
	 * 
	 * @param g
	 * @param x
	 *            目标x,即相对控件左上角位置的x坐标
	 * @param y
	 *            目标y,即相对控件左上角位置的y坐标
	 * @param _x
	 *            源图x,即被绘区域相对九片图切块左上角位置的x坐标
	 * @param _y
	 *            源图y,即被绘区域相对九片图切块左上角位置的y坐标
	 */
	protected void paintCol(C2D_Graphics g, int x, int y, int _x, int _y)
	{
		paintCol(g, x, y, _x, _y, m_partW);
	}

	/**
	 * 获取相对布局矩形，即基于当前的坐标，尺寸，锚点，翻转等信息， 计算出相对于其父节点所占据的矩形区域。将信息存放在传入的
	 * 矩形参数，并返回是否成功取得。
	 * 
	 * @param resultRect
	 *            用于结果存放的矩形对象
	 * @return 是否成功取得
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		return C2D_Graphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, (byte) 0, resultRect);
	}

	/**
	 * 设置图片的所有参数，不包括尺寸
	 * 
	 * @param image
	 *            内容图片
	 * @param _x
	 *            内容区域位于图片中的X坐标
	 * @param _y
	 *            内容区域位于图片中的Y坐标
	 * @param _w
	 *            内容宽度
	 * @param _h
	 *            内容高度
	 * @param transform
	 *            内容图片的翻转信息
	 * @param anchor
	 *            本控件的锚点，内容图片的锚点定在左上角
	 * @param width
	 *            本控件的宽度
	 * @param height
	 *            本控件的高度
	 */
	public void setParameters(C2D_Image image, int _x, int _y, int _w, int _h, byte transform, int anchor, int width, int height)
	{
		setImage(image);
		m_Clip.setContentRect(_x, _y, _w, _h);
		m_Clip.setTransform(transform);
		m_anchor = anchor;
		setSize(width, height);
		layoutChanged();
	}

	/**
	 * 当重新设置过切块大小
	 */
	protected void whenSetClipSize()
	{
		if (m_Clip != null)
		{
			m_partW = m_Clip.getContentW() / 3;
			m_partH = m_Clip.getContentH() / 3;
			m_adgeW = m_partW;
			m_adgeH = m_partH;
			if (m_width <= 0)
			{
				m_width = m_partW * 3;
			}
			if (m_height <= 0)
			{
				m_height = m_partH * 3;
			}
		}
	}

	/**
	 * 设置边缘大小
	 * 
	 * @param adgeSize
	 *            边缘大小
	 */
	public void setAdgeSize(C2D_SizeI adgeSize)
	{
		if (adgeSize != null)
		{
			setAdgeSize(adgeSize.m_width, adgeSize.m_height);
		}
	}

	/**
	 * 设置边缘大小
	 * 
	 * @param adgeW
	 *            边缘宽度
	 * @param adgeH
	 *            边缘高度
	 */
	public void setAdgeSize(int adgeW, int adgeH)
	{
		if (m_Clip != null)
		{
			m_adgeW = adgeW;
			m_adgeH = adgeH;
			m_adgeW = C2D_Math.limitNumber(m_adgeW, 0, m_Clip.getContentW() / 2);
			m_adgeH = C2D_Math.limitNumber(m_adgeH, 0, m_Clip.getContentH() / 2);
			m_partW = m_Clip.getContentW() - m_adgeW * 2;
			m_partH = m_Clip.getContentH() - m_adgeH * 2;

			if (m_width <= 0)
			{
				m_width = m_partW + 2 * m_adgeW;
			}
			if (m_height <= 0)
			{
				m_height = m_partH + 2 * m_adgeH;
			}
			layoutChanged();
		}
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
	}
}
