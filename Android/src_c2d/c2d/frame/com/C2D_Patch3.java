package c2d.frame.com;

import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.math.type.C2D_SizeF;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 三片图类，可以拥有、设置、显示1张切片，这张切片沿X方向被均分成三份， 第1,3块分别用作控件的左右两侧边缘，第2块座位中间图片，可以根据指定
 * 的宽度在X方向连续进行平铺，高度来自于给定的中间图片高度。每次重新设 置切块大小之后，都将采用默认的3x1均匀分割，此时还可以设置边缘大小以
 * 实现不同的区块分布。 Android版本使用拉伸实现
 * 
 * @author AndrewFan
 */
public class C2D_Patch3 extends C2D_PicBox
{
	/** 控件宽度. */
	protected int m_width;
	/** 控件高度. */
	protected int m_height;
	/** 中间分块宽度. */
	protected int m_partW;
	/** 边缘分块宽度. */
	protected int m_adgeW;

	/**
	 * 使用给定的图片来创建图片控件，将使用整个图片产生切片
	 * 
	 * @param image
	 *            图片
	 */
	public C2D_Patch3(C2D_Image image)
	{
		super(image);
	}

	/**
	 * 使用给定的图片切块来创建图片控件，将使用此切片产生三块分切片
	 * 
	 * @param imageClip
	 *            图片切块
	 */
	public C2D_Patch3(C2D_ImageClip imageClip)
	{
		setImageClip(imageClip);
	}

	/**
	 * 直接从jar中加载图片并创建图片控件，默认文件夹位于imgs_ohter， 内部使用C2D_Image.createImage()来创建图片
	 * 
	 * @param imageName
	 */
	public C2D_Patch3(String imageName)
	{
		C2D_Image img = C2D_Image.createImage(imageName);
		if (img != null)
		{
			setImage(img);
		}
	}

	/**
	 * 直接从jar中加载图片并创建图片控件，指定位于res下的子文件夹名称，
	 * 内部使用C2D_Image.createImage(String,String)来创建图片
	 * 
	 * @param imageName
	 */
	public C2D_Patch3(String imageName, String subFolder)
	{
		super(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * 设置控件宽度
	 * 
	 * @param width
	 */
	public void setWidth(int width)
	{
		if (width > 0 && m_Clip != null)
		{
			m_Clip.setShowSize(width, m_Clip.getShowSize().m_height);
		}
	}

	/**
	 * 获取控件宽度
	 * 
	 * @return 控件宽度
	 */
	public float getWidth()
	{
		return m_Clip.getShowSize().m_width;
	}

	/**
	 * 获取控件高度
	 * 
	 * @return 控件高度
	 */
	public float getHeight()
	{
		return m_Clip.getShowSize().m_height;
	}

	private static C2D_RectF rectTemp = new C2D_RectF();

	/**
	 * 获取当前坐标根据当前尺寸进行锚点计算后的新位置
	 * 
	 * @return 新位置坐标
	 */
	public C2D_PointF getLeftTop()
	{
		if (m_Clip == null)
		{
			return null;
		}
		C2D_SizeF showSize = m_Clip.getShowSize();
		rectTemp.setValue(m_xToTop, m_yToTop, showSize.m_width, showSize.m_height);
		return C2D_GdiGraphics.applyAnchor(rectTemp, m_anchor);
	}

	/**
	 * 绘制节点
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
		// 左边
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y, m_adgeW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
		x += m_adgeW;
		// 中间
		while (x + m_partW < m_width - m_adgeW)
		{
			g.drawRegion(m_image, m_contentRect.m_x + m_adgeW, m_contentRect.m_y, m_partW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
			x += m_partW;
		}
		// 补齐
		if (x < m_width - m_adgeW)
		{
			g.drawRegion(m_image, m_contentRect.m_x + m_adgeW, m_contentRect.m_y, m_width - m_adgeW - x, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
			x = m_width - m_adgeW;
		}
		// 右边
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y, m_adgeW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
		// 焦点
		paintFocus(g, m_contentRect.m_width, m_contentRect.m_height);
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
		C2D_SizeF showSize = m_Clip.getShowSize();
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, showSize.m_width, showSize.m_height, m_anchor, (byte) 0, resultRect);
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
	 */
	public void setParameters(C2D_Image image, int _x, int _y, int _w, int _h, byte transform, int anchor, int width)
	{
		setImage(image);
		m_Clip.setContentRect(_x, _y, _w, _h);
		m_Clip.setTransform(transform);
		m_anchor = anchor;
		setWidth(width);
	}

	/**
	 * 当重新设置过切块大小
	 */
	protected void whenSetClipSize()
	{
		if (m_Clip != null)
		{
			m_height = m_Clip.getContentH();
			m_partW = m_Clip.getContentW() / 3;
			m_adgeW = m_partW;
			if (m_width <= 0)
			{
				m_width = m_partW + m_adgeW * 2;
			}
		}
	}

	/**
	 * 设置边缘大小
	 * 
	 * @param adgeW
	 *            边缘宽度
	 */
	public void setAdgeSize(int adgeW)
	{
		if (m_Clip != null)
		{
			m_adgeW = adgeW;
			m_adgeW = C2D_Math.limitNumber(m_adgeW, 0, m_Clip.getContentW() / 2);
			m_partW = m_Clip.getContentW() - m_adgeW * 2;
			if (m_width <= 0)
			{
				m_width = m_partW + 2 * m_adgeW;
			}
			if (m_height <= 0)
			{
				m_height = m_Clip.getContentH();
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
