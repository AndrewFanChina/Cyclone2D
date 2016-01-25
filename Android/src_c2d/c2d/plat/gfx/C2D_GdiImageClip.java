package c2d.plat.gfx;

import c2d.lang.math.type.C2D_RectS;

/**
 * ͼƬ�п���
 * @author AndrewFan
 *
 */
public class C2D_GdiImageClip
{
	/** ͼƬ */
	private C2D_GdiImage m_image;
	
	/** ��ת��־ */
	private byte m_transform = C2D_GdiGraphics.TRANS_NONE;
	
	/** ��������λ��ͼƬ�ϵľ�������ֻ�д��ھ��������ڵ����زŻᱻ��ʾ���� */
	private C2D_RectS m_contentRect=new C2D_RectS();
	
	public C2D_GdiImageClip()
	{
	}
	/**
	 * ����һ��ͼƬ����Ƭ������Ƭ��Ĭ��ʹ��ͼƬ��������С����ʹ��(0,0,imgW,imgH)
	 * @param image ͼƬ����
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
	 * ����һ��ͼƬ����Ƭ������Ƭ��Ĭ��ʹ��ͼƬ��������С����ʹ��(0,0,imgW,imgH)
	 * @param fileName ͼƬ�ļ����ƣ�λ����Դ�ļ��е�Ĭ��ͼƬĿ¼
	 */
	public C2D_GdiImageClip(String fileName)
	{
		this(C2D_GdiImage.createImage(fileName));
	}
	/**
	 * ������������
	 * @param contentRect ��������
	 */
	public void setContentRect(C2D_RectS contentRect)
	{
		m_contentRect.m_x = contentRect.m_x;
		m_contentRect.m_y = contentRect.m_y;
		m_contentRect.m_width = contentRect.m_width;
		m_contentRect.m_height = contentRect.m_height;
	}
	/**
	 * ��ȡ�������������ⲿ��Ӧ���޸��������ֵ���������ã�
	 * ����setContentRect
	 * @return C2D_RectS ��������
	 */
	public C2D_RectS getContentRect()
	{
		return m_contentRect;
	}
	/**
	 * ������������
	 * @param cX ��ʾ��������X
	 * @param cY ��ʾ��������Y
	 * @param cW ��ʾ���ݿ��W
	 * @param cH ��ʾ���ݸ߶�H
	 */
	public void setContentRect(int cX,int cY,int cW,int cH)
	{
		m_contentRect.m_x=(short)cX;
		m_contentRect.m_y=(short)cY;
		m_contentRect.m_width=(short)cW;
		m_contentRect.m_height=(short)cH;
	}
	/**
	 * ��������������
	 * @param cW ����������W
	 */
	public void setContentW(int cW)
	{
		m_contentRect.m_width=(short)cW;
	}
	/**
	 * ������������߶�
	 * @param cH ��������߶�H
	 */
	public void setContentH(int cH)
	{
		m_contentRect.m_height=(short)cH;
	}
	/**
	 * ��ȡ����������
	 * @return ����������
	 */
	public short getContentW()
	{
		return m_contentRect.m_width;
	}
	/**
	 * ��ȡ��������߶�
	 * @return ��������߶�
	 */
	public short getContentH()
	{
		return m_contentRect.m_height;
	}
	/**
	 * ������������ߴ�
	 * @param width ����������
	 * @param height ��������߶�
	 */
	public void setContentSize(int width,int height)
	{
		m_contentRect.m_width=(short)width;
		m_contentRect.m_height=(short)height;
	}
	/**
	 * ������������λ��
	 * @param cX ������������X
	 * @param cY ������������Y
	 */
	public void setContentPos(int cX,int cY)
	{
		m_contentRect.m_x=(short)cX;
		m_contentRect.m_y=(short)cY;
	}
	/**
	 * ����ͼ�����֮ǰͼƬΪ�գ����Զ����ó�ͼƬ��С
	 * @param image ͼƬ����
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
	 * ��ȡͼƬ
	 * @return ͼƬ
	 */
	public C2D_GdiImage getImage()
	{
		return m_image;
	}
	/**
	 * ���÷�ת��־
	 * 
	 * @param transform ��ת��־
	 */
	public void setTransform(byte transform)
	{
		m_transform = transform;
	}
	/**
	 * ��ȡ��ת��־
	 * 
	 * @return ��ת��־
	 */
	public byte getTransform()
	{
		return m_transform;
	}

}
