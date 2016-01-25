package c2d.frame.com;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * ͼƬ�ؼ��࣬����ӵ�С����á���ʾһ��ͼƬ
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
	 * ʹ�ø�����ͼƬ������ͼƬ�ؼ�
	 * 
	 * @param image
	 *            ͼƬ
	 */
	public C2D_PicBox(C2D_Image image)
	{
		setImage(image);
	}

	/**
	 * ʹ�ø�����ͼƬ������������ͼƬ�ؼ�
	 * 
	 * @param imageClip
	 *            ͼƬ�п�
	 */
	public C2D_PicBox(C2D_ImageClip imageClip)
	{
		setImageClip(imageClip);
	}

	/**
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���Ĭ���ļ���λ��imgs_ohter�� �ڲ�ʹ��C2D_Image.createImage()������ͼƬ
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
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���ָ��λ��res�µ����ļ������ƣ�
	 * �ڲ�ʹ��C2D_Image.createImage(String,String)������ͼƬ
	 * 
	 * @param imageName
	 */
	public C2D_PicBox(String imageName, String subFolder)
	{
		setImage(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * ��ȡ�������������ⲿ��Ӧ���޸��������ֵ���������ã� ����setContentRect
	 * 
	 * @return C2D_RectS ��������
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
	 * ������������
	 * 
	 * @param contentRect
	 *            ��������
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
	 * ������������
	 * 
	 * @param cX
	 *            ��ʾ��������X
	 * @param cY
	 *            ��ʾ��������Y
	 * @param cW
	 *            ��ʾ���ݿ��W
	 * @param cH
	 *            ��ʾ���ݸ߶�H
	 */
	public void setContentRect(int cX, int cY, int cW, int cH)
	{
		m_Clip.setContentRect(cX, cY, cW, cH);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * ��������������
	 * 
	 * @param cW
	 *            ����������W
	 */
	public void setContentW(int cW)
	{
		m_Clip.setContentW(cW);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * ������������߶�
	 * 
	 * @param cH
	 *            ��������߶�H
	 */
	public void setContentH(int cH)
	{
		m_Clip.setContentH(cH);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return ����������
	 */
	public int getContentW()
	{
		return m_Clip.getContentW();
	}

	/**
	 * ��ȡ��������߶�
	 * 
	 * @return ��������߶�
	 */
	public int getContentH()
	{
		return m_Clip.getContentH();
	}

	/**
	 * ������������ߴ�
	 * 
	 * @param width
	 *            ����������
	 * @param height
	 *            ��������߶�
	 */
	public void setContentSize(int width, int height)
	{
		m_Clip.setContentSize(width, height);
		whenSetClipSize();
		layoutChanged();
	}

	/**
	 * ������������λ��
	 * 
	 * @param cX
	 *            ������������X
	 * @param cY
	 *            ������������Y
	 */
	public void setContentPos(int cX, int cY)
	{
		m_Clip.setContentPos(cX, cY);
		layoutChanged();
	}

	/**
	 * ��ȡͼƬ
	 * 
	 * @return ͼƬ
	 */
	public C2D_Image getImage()
	{
		return m_Clip.getImage();
	}

	/**
	 * ��ȡ����--��ͼ�п�
	 * 
	 * @return ��ͼ�п�
	 */
	public C2D_ImageClip getContent()
	{
		return m_Clip;
	}

	/**
	 * ����ָ��ͼƬ�п��ͼƬԴ�ͳߴ磬���Ŀǰû��ͼƬ���Ὣ��ʾ�������ó�����ͼƬ��С
	 * 
	 * @param imageClip
	 *            ��ͼƬ�п飬�������ֵ
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
	 * ������ͼƬ�����Ŀǰû��ͼƬ���Ὣ��ʾ�������ó�����ͼƬ��С
	 * 
	 * @param image
	 *            ��ͼƬ���������ֵ
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
	 * ��������ͼƬ�ķ�ת��־
	 * 
	 * @param transform
	 *            ����ͼƬ�ķ�ת��־
	 */
	public void setTransform(byte transform)
	{
		m_Clip.setTransform(transform);
		layoutChanged();
	}

	/**
	 * ��ȡ��ת��־
	 * 
	 * @return ��ת��־
	 */
	public byte getTransform()
	{
		return m_Clip.getTransform();
	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
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
	 * ��ȡ��Ը������Ĳ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ��<br>
	 * �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ���� ���β������������Ƿ�ɹ�ȡ�á�<br>
	 * 
	 * @param rectI
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
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
	 * ����ͼƬ�����в���
	 * 
	 * @param image
	 *            ����ͼƬ
	 * @param _x
	 *            ��������λ��ͼƬ�е�X����
	 * @param _y
	 *            ��������λ��ͼƬ�е�Y����
	 * @param _w
	 *            ���ݿ��
	 * @param _h
	 *            ���ݸ߶�
	 * @param transform
	 *            ����ͼƬ�ķ�ת��Ϣ
	 * @param anchor
	 *            ���ؼ���ê��
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
	 * ���������ù��п��С
	 */
	protected void whenSetClipSize()
	{
	}

	/**
	 * ж����Դ
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
