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
 * ��Ƭͼ�࣬����ӵ�С����á���ʾ1����Ƭ��������Ƭ��X���򱻾��ֳ����ݣ� ��1,3��ֱ������ؼ������������Ե����2����λ�м�ͼƬ�����Ը���ָ��
 * �Ŀ����X������������ƽ�̣��߶������ڸ������м�ͼƬ�߶ȡ�ÿ�������� ���п��С֮�󣬶�������Ĭ�ϵ�3x1���ȷָ��ʱ���������ñ�Ե��С��
 * ʵ�ֲ�ͬ������ֲ��� Android�汾ʹ������ʵ��
 * 
 * @author AndrewFan
 */
public class C2D_Patch3 extends C2D_PicBox
{
	/** �ؼ����. */
	protected int m_width;
	/** �ؼ��߶�. */
	protected int m_height;
	/** �м�ֿ���. */
	protected int m_partW;
	/** ��Ե�ֿ���. */
	protected int m_adgeW;

	/**
	 * ʹ�ø�����ͼƬ������ͼƬ�ؼ�����ʹ������ͼƬ������Ƭ
	 * 
	 * @param image
	 *            ͼƬ
	 */
	public C2D_Patch3(C2D_Image image)
	{
		super(image);
	}

	/**
	 * ʹ�ø�����ͼƬ�п�������ͼƬ�ؼ�����ʹ�ô���Ƭ�����������Ƭ
	 * 
	 * @param imageClip
	 *            ͼƬ�п�
	 */
	public C2D_Patch3(C2D_ImageClip imageClip)
	{
		setImageClip(imageClip);
	}

	/**
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���Ĭ���ļ���λ��imgs_ohter�� �ڲ�ʹ��C2D_Image.createImage()������ͼƬ
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
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���ָ��λ��res�µ����ļ������ƣ�
	 * �ڲ�ʹ��C2D_Image.createImage(String,String)������ͼƬ
	 * 
	 * @param imageName
	 */
	public C2D_Patch3(String imageName, String subFolder)
	{
		super(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * ���ÿؼ����
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
	 * ��ȡ�ؼ����
	 * 
	 * @return �ؼ����
	 */
	public float getWidth()
	{
		return m_Clip.getShowSize().m_width;
	}

	/**
	 * ��ȡ�ؼ��߶�
	 * 
	 * @return �ؼ��߶�
	 */
	public float getHeight()
	{
		return m_Clip.getShowSize().m_height;
	}

	private static C2D_RectF rectTemp = new C2D_RectF();

	/**
	 * ��ȡ��ǰ������ݵ�ǰ�ߴ����ê���������λ��
	 * 
	 * @return ��λ������
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
	 * ���ƽڵ�
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
		// ���
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y, m_adgeW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
		x += m_adgeW;
		// �м�
		while (x + m_partW < m_width - m_adgeW)
		{
			g.drawRegion(m_image, m_contentRect.m_x + m_adgeW, m_contentRect.m_y, m_partW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
			x += m_partW;
		}
		// ����
		if (x < m_width - m_adgeW)
		{
			g.drawRegion(m_image, m_contentRect.m_x + m_adgeW, m_contentRect.m_y, m_width - m_adgeW - x, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
			x = m_width - m_adgeW;
		}
		// �ұ�
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y, m_adgeW, m_contentRect.m_height, m_transform, ltX + x, ltY + 0, 0);
		// ����
		paintFocus(g, m_contentRect.m_width, m_contentRect.m_height);
	}

	/**
	 * ��ȡ��Բ��־��Σ������ڵ�ǰ�����꣬�ߴ磬ê�㣬��ת����Ϣ�� �����������丸�ڵ���ռ�ݵľ������򡣽���Ϣ����ڴ����
	 * ���β������������Ƿ�ɹ�ȡ�á�
	 * 
	 * @param resultRect
	 *            ���ڽ����ŵľ��ζ���
	 * @return �Ƿ�ɹ�ȡ��
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
	 * ����ͼƬ�����в������������ߴ�
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
	 *            ���ؼ���ê�㣬����ͼƬ��ê�㶨�����Ͻ�
	 * @param width
	 *            ���ؼ��Ŀ��
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
	 * ���������ù��п��С
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
	 * ���ñ�Ե��С
	 * 
	 * @param adgeW
	 *            ��Ե���
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
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
	}
}
