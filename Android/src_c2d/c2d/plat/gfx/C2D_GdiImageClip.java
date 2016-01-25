package c2d.plat.gfx;

import c2d.lang.math.type.C2D_RectS;

/**
 * 图片切块类
 * @author AndrewFan
 *
 */
public class C2D_GdiImageClip
{
	/** 图片 */
	private C2D_GdiImage m_image;
	
	/** 翻转标志 */
	private byte m_transform = C2D_GdiGraphics.TRANS_NONE;
	
	/** 内容区域，位于图片上的矩形区域，只有存在矩形区域内的像素才会被显示出来 */
	private C2D_RectS m_contentRect=new C2D_RectS();
	
	public C2D_GdiImageClip()
	{
	}
	/**
	 * 创建一张图片的切片对象，切片将默认使用图片的完整大小，即使用(0,0,imgW,imgH)
	 * @param image 图片对象
	 */
	public C2D_GdiImageClip(C2D_GdiImage image)
	{
		if(image!=null)
		{
			setImage(image);
			setContentRect(0, 0, image.getWidth(), image.getHeight());
		}
	}
	/**
	 * 创建一张图片的切片对象，切片将默认使用图片的完整大小，即使用(0,0,imgW,imgH)
	 * @param fileName 图片文件名称，位于资源文件夹的默认图片目录
	 */
	public C2D_GdiImageClip(String fileName)
	{
		this(C2D_GdiImage.createImage(fileName));
	}
	/**
	 * 设置内容区域
	 * @param contentRect 内容区域
	 */
	public void setContentRect(C2D_RectS contentRect)
	{
		m_contentRect.m_x = contentRect.m_x;
		m_contentRect.m_y = contentRect.m_y;
		m_contentRect.m_width = contentRect.m_width;
		m_contentRect.m_height = contentRect.m_height;
	}
	/**
	 * 获取内容区域，你在外部不应该修改这个返回值，如需设置，
	 * 请用setContentRect
	 * @return C2D_RectS 内容区域
	 */
	public C2D_RectS getContentRect()
	{
		return m_contentRect;
	}
	/**
	 * 设置内容区域
	 * @param cX 显示内容坐标X
	 * @param cY 显示内容坐标Y
	 * @param cW 显示内容宽度W
	 * @param cH 显示内容高度H
	 */
	public void setContentRect(int cX,int cY,int cW,int cH)
	{
		m_contentRect.m_x=(short)cX;
		m_contentRect.m_y=(short)cY;
		m_contentRect.m_width=(short)cW;
		m_contentRect.m_height=(short)cH;
	}
	/**
	 * 设置内容区域宽度
	 * @param cW 内容区域宽度W
	 */
	public void setContentW(int cW)
	{
		m_contentRect.m_width=(short)cW;
	}
	/**
	 * 设置内容区域高度
	 * @param cH 内容区域高度H
	 */
	public void setContentH(int cH)
	{
		m_contentRect.m_height=(short)cH;
	}
	/**
	 * 获取内容区域宽度
	 * @return 内容区域宽度
	 */
	public short getContentW()
	{
		return m_contentRect.m_width;
	}
	/**
	 * 获取内容区域高度
	 * @return 内容区域高度
	 */
	public short getContentH()
	{
		return m_contentRect.m_height;
	}
	/**
	 * 设置内容区域尺寸
	 * @param width 内容区域宽度
	 * @param height 内容区域高度
	 */
	public void setContentSize(int width,int height)
	{
		m_contentRect.m_width=(short)width;
		m_contentRect.m_height=(short)height;
	}
	/**
	 * 设置内容区域位置
	 * @param cX 内容区域坐标X
	 * @param cY 内容区域坐标Y
	 */
	public void setContentPos(int cX,int cY)
	{
		m_contentRect.m_x=(short)cX;
		m_contentRect.m_y=(short)cY;
	}
	/**
	 * 设置图，如果之前图片为空，会自动设置成图片大小
	 * @param image 图片参数
	 */
	public void setImage(C2D_GdiImage image)
	{
		if(m_image ==null && image!=null)
		{
			setContentRect(0, 0, image.getWidth(), image.getHeight());
		}
		m_image=image;
	}
	/**
	 * 获取图片
	 * @return 图片
	 */
	public C2D_GdiImage getImage()
	{
		return m_image;
	}
	/**
	 * 设置翻转标志
	 * 
	 * @param transform 翻转标志
	 */
	public void setTransform(byte transform)
	{
		m_transform = transform;
	}
	/**
	 * 获取翻转标志
	 * 
	 * @return 翻转标志
	 */
	public byte getTransform()
	{
		return m_transform;
	}

}
