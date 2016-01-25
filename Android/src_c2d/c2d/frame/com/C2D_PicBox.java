package c2d.frame.com;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * 图片控件类，可以拥有、设置、显示一张图片
 * 
 * @author AndrewFan
 */
public class C2D_PicBox extends C2D_Widget
{
	protected C2D_ImageClip m_Clip;
	protected C2D_Image m_Image;

	protected C2D_PicBox()
	{
	}

	/**
	 * 使用给定的图片来创建图片控件
	 * 
	 * @param image
	 *            图片
	 */
	public C2D_PicBox(C2D_Image image)
	{
		setImage(image);
	}

	/**
	 * 使用给定的图片剪切区来创建图片控件
	 * 
	 * @param imageClip
	 *            图片切块
	 */
	public C2D_PicBox(C2D_ImageClip imageClip)
	{
		setImageClip(imageClip);
	}

	/**
	 * 直接从jar中加载图片并创建图片控件，默认文件夹位于imgs_ohter， 内部使用C2D_Image.createImage()来创建图片
	 * 
	 * @param imageName
	 */
	public C2D_PicBox(String imageName)
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
	public C2D_PicBox(String imageName, String subFolder)
	{
		setImage(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * 获取内容区域，你在外部不应该修改这个返回值，如需设置， 请用setContentRect
	 * 
	 * @return C2D_RectS 内容区域
	 */
	public C2D_RectS getContentRect()
	{
		if (m_Clip != null)
		{
			return m_Clip.getContentRect();
		}
		return null;
	}

	/**
	 * 设置内容区域
	 * 
	 * @param contentRect
	 *            内容区域
	 */
	public void setContentRect(C2D_RectS contentRect)
	{
		if (contentRect != null)
		{
			m_Clip.setContentRect(contentRect);
			whenSetClipSize();
			layoutChanged();
		}
	}

	/**
	 * 设置内容区域
	 * 
	 * @param cX
	 *            显示内容坐标X
	 * @param cY
	 *            显示内容坐标Y
	 * @param cW
	 *            显示内容宽度W
	 * @param cH
	 *            显示内容高度H
	 */
	public void setContentRect(int cX, int cY, int cW, int cH)
	{
		m_Clip.setContentRect(cX, cY, cW, cH);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 设置内容区域宽度
	 * 
	 * @param cW
	 *            内容区域宽度W
	 */
	public void setContentW(int cW)
	{
		m_Clip.setContentW(cW);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 设置内容区域高度
	 * 
	 * @param cH
	 *            内容区域高度H
	 */
	public void setContentH(int cH)
	{
		m_Clip.setContentH(cH);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 获取内容区域宽度
	 * 
	 * @return 内容区域宽度
	 */
	public int getContentW()
	{
		return m_Clip.getContentW();
	}

	/**
	 * 获取内容区域高度
	 * 
	 * @return 内容区域高度
	 */
	public int getContentH()
	{
		return m_Clip.getContentH();
	}

	/**
	 * 设置内容区域尺寸
	 * 
	 * @param width
	 *            内容区域宽度
	 * @param height
	 *            内容区域高度
	 */
	public void setContentSize(int width, int height)
	{
		m_Clip.setContentSize(width, height);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 设置内容区域位置
	 * 
	 * @param cX
	 *            内容区域坐标X
	 * @param cY
	 *            内容区域坐标Y
	 */
	public void setContentPos(int cX, int cY)
	{
		m_Clip.setContentPos(cX, cY);
		layoutChanged();
	}

	/**
	 * 获取图片
	 * 
	 * @return 图片
	 */
	public C2D_Image getImage()
	{
		return m_Clip.getImage();
	}

	/**
	 * 获取内容--贴图切块
	 * 
	 * @return 贴图切块
	 */
	public C2D_ImageClip getContent()
	{
		return m_Clip;
	}

	/**
	 * 设置指定图片切块的图片源和尺寸，如果目前没有图片，会将显示区域设置成整个图片大小
	 * 
	 * @param imageClip
	 *            新图片切块，允许传入空值
	 */
	public void setImageClip(C2D_ImageClip imageClip)
	{
		if (imageClip == null)
		{
			return;
		}
		if (m_Clip != null)
		{
			m_Clip.doRelease(this);
			m_Clip = null;
		}
		m_Clip = imageClip;
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 设置新图片，如果目前没有图片，会将显示区域设置成整个图片大小
	 * 
	 * @param image
	 *            新图片，允许传入空值
	 */
	public void setImage(C2D_Image image)
	{
		if (m_Image != null)
		{
			m_Image.doRelease(this);
			m_Image = null;
		}
		m_Image = image;
		if (m_Clip == null)
		{
			m_Clip = new C2D_ImageClip(image);
			m_Clip.transHadler(this);
		}
		else
		{
			m_Clip.setImage(image);
		}
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * 设置内容图片的翻转标志
	 * 
	 * @param transform
	 *            内容图片的翻转标志
	 */
	public void setTransform(byte transform)
	{
		m_Clip.setTransform(transform);
		layoutChanged();
	}

	/**
	 * 获取翻转标志
	 * 
	 * @return 翻转标志
	 */
	public byte getTransform()
	{
		return m_Clip.getTransform();
	}

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		C2D_Image m_image = m_Clip.getImage();
		C2D_RectS m_contentRect = m_Clip.getContentRect();
		byte m_transform = m_Clip.getTransform();
		if (m_image == null || g == null || stage == null || !m_visible || !m_inCamera)
		{
			return;
		}
		//FIXME ----->>>>>>>>
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y, m_contentRect.m_width, m_contentRect.m_height, m_transform, m_xToTop, m_yToTop, m_anchor);
		paintFocus(g, m_contentRect.m_width, m_contentRect.m_height);
	}

	/**
	 * 获取相对父容器的布局矩形，即基于当前的坐标，尺寸，锚点，翻转等信息，<br>
	 * 计算出相对于其父节点所占据的矩形区域。将信息存放在传入的 矩形参数，并返回是否成功取得。<br>
	 * 
	 * @param rectI
	 *            用于结果存放的矩形对象
	 * @return 是否成功取得
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		C2D_RectS m_contentRect = m_Clip.getContentRect();
		byte m_transform = m_Clip.getTransform();
		return C2D_Graphics.computeLayoutRect(m_x, m_y, m_contentRect.m_width, m_contentRect.m_height, m_anchor, m_transform, resultRect);
	}

	/**
	 * 设置图片的所有参数
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
	 *            本控件的锚点
	 */
	public void setParameters(C2D_Image image, int _x, int _y, int _w, int _h, byte transform, int anchor)
	{
		setImage(image);
		m_Clip.setContentRect(_x, _y, _w, _h);
		m_Clip.setTransform(transform);
		whenSetClipSize();
		m_anchor = anchor;
		layoutChanged();
	}

	/**
	 * 当重新设置过切块大小
	 */
	protected void whenSetClipSize()
	{
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		releaseClip();
		releaseImage();
	}

	private void releaseImage()
	{
		if (m_Image != null)
		{
			m_Image.doRelease(this);
			m_Image = null;
		}
	}

	private void releaseClip()
	{
		if (m_Clip != null)
		{
			m_Clip.doRelease();
			m_Clip = null;
		}
	}

	protected float getWidth()
	{
		return getContentW();
	}

	protected float getHeight()
	{
		return getContentH();
	}
}
