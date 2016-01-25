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
 * ��Ƭͼ�࣬����ӵ�С����á���ʾ1����Ƭ��������Ƭ��X,Y���򱻾��ֳ�3x3�ݣ� �ĸ��߽ǿ����ڿؼ����Ľ����裬�ĸ���Ե���Ŀ����ڱ�Ե�������裬�м������
 * ˮƽ�ʹ�ֱ�����ϵ��ڲ������������衣ÿ�����������п��С֮�󣬶�������Ĭ�� ��3x3���ȷָ��ʱ���������ñ�Ե��С��ʵ�ֲ�ͬ������ֲ���
 * 
 * @author AndrewFan
 */
public class C2D_Patch9 extends C2D_PicBox
{
	/** �ؼ����. */
	protected int m_width;
	/** �ؼ��߶�. */
	protected int m_height;
	/** �м�ֿ���. */
	protected int m_partW;
	/** �ֿ�߶�. */
	protected int m_partH;
	/** ��Ե�ֿ���. */
	protected int m_adgeW;
	/** ��Ե�ֿ�߶�. */
	protected int m_adgeH;
	/** �����ɫ�������ֵ���ڣ���ʹ�ô���ɫ����м�ƽ������ */
	protected C2D_Color m_fillColor;

	/**
	 * ʹ�ø�����ͼƬ������ͼƬ�ؼ�����ʹ������ͼƬ������Ƭ
	 * 
	 * @param image
	 *            ͼƬ
	 */
	public C2D_Patch9(C2D_Image image)
	{
		super(image);
	}

	/**
	 * ʹ�ø�����ͼƬ�п�������ͼƬ�ؼ�����ʹ�ô���Ƭ�����������Ƭ
	 * 
	 * @param imageClip
	 *            ͼƬ�п�
	 */
	public C2D_Patch9(C2D_ImageClip imageClip)
	{
		super(imageClip);
	}

	/**
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���Ĭ���ļ���λ��imgs_ohter�� �ڲ�ʹ��C2D_Image.createImage()������ͼƬ
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
	 * ֱ�Ӵ�jar�м���ͼƬ������ͼƬ�ؼ���ָ��λ��res�µ����ļ������ƣ�
	 * �ڲ�ʹ��C2D_Image.createImage(String,String)������ͼƬ
	 * 
	 * @param imageName
	 */
	public C2D_Patch9(String imageName, String subFolder)
	{
		super(C2D_Image.createImage(imageName, subFolder));
	}

	/**
	 * ���ÿؼ����
	 * 
	 * @param width
	 *            ���
	 * @param height
	 *            �߶�
	 */
	public void setSize(float width, float height)
	{
		m_width = (int)width;
		m_height = (int)height;
		layoutChanged();
	}

	/**
	 * ���������ɫ����������ɫ���ڣ���ʹ�ô���ɫ����м�ƽ������
	 * 
	 * @param fillColor
	 *            �����ɫ������Ϊnull��ʾ��ʹ����ɫ���
	 */
	public void setFillColor(C2D_Color fillColor)
	{
		m_fillColor = fillColor;
		layoutChanged();
	}

	/**
	 * ��ȡ�ؼ����
	 * 
	 * @return �ؼ����
	 */
	public float getWidth()
	{
		return m_width;
	}

	/**
	 * ��ȡ�ؼ��߶�
	 * 
	 * @return �ؼ��߶�
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
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
		// �ĸ�����
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y, m_adgeW, m_adgeH, m_transform, ltX + x, ltY + y, 0);
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y, m_adgeW, m_adgeH, m_transform, ltX + x + m_width - m_adgeW, ltY + y, 0);
		g.drawRegion(m_image, m_contentRect.m_x, m_contentRect.m_y + m_contentRect.m_height - m_adgeH, m_adgeW, m_adgeH, m_transform, ltX + x, ltY + y + m_height - m_adgeH, 0);
		g.drawRegion(m_image, m_contentRect.m_x + m_contentRect.m_width - m_adgeW, m_contentRect.m_y + m_contentRect.m_height - m_adgeH, m_adgeW, m_adgeH, m_transform, ltX + x + m_width - m_adgeW, ltY + y + m_height - m_adgeH, 0);
		// ��һ��
		paintRow(g, m_adgeW, 0, m_adgeW, 0, m_adgeH);
		// ������
		paintRow(g, m_adgeW, m_height - m_adgeH, m_adgeW, m_contentRect.m_height - m_adgeH);
		// ��һ��
		paintCol(g, 0, m_adgeH, 0, m_adgeH, m_adgeW);
		// ������
		paintCol(g, m_width - m_adgeW, m_adgeH, m_contentRect.m_width - m_adgeW, m_adgeH, m_adgeW);
		// �м�
		x = m_adgeW;
		y = m_adgeH;
		if (m_fillColor == null)
		{
			while (y + m_partH < m_height - m_adgeH)
			{
				paintRow(g, x, y, m_adgeW, m_adgeH);
				y += m_partH;
			}
			// ����
			if (y < m_height - m_adgeH)
			{
				paintRow(g, x, y, m_adgeW, m_adgeH, m_height - m_adgeH - y);
			}
		}
		else
		{
			g.fillRect(ltX + x, ltY + y, m_width - m_adgeW - x, m_height - m_adgeH - y, m_fillColor.getColor());
		}
		// ����
		paintFocus(g, m_contentRect.m_width, m_contentRect.m_height);
	}

	/**
	 * ����һ�У���ָ���ĸ߶�
	 * 
	 * @param g
	 * @param x
	 *            Ŀ��x,����Կؼ����Ͻ�λ�õ�x����
	 * @param y
	 *            Ŀ��y,����Կؼ����Ͻ�λ�õ�y����
	 * @param _x
	 *            Դͼx,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�x����
	 * @param _y
	 *            Դͼy,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�y����
	 * @param _h
	 *            Ҫ���Ƶĸ߶�
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
		// ����
		if (x < m_width - m_adgeW)
		{
			g.drawRegion(m_image,m_contentRect.m_x + _x, m_contentRect.m_y + _y, m_width - m_adgeW - (int)x, _h,m_transform, ltX + x, ltY + y,  0);
			x = m_width - m_adgeW;
		}
	}

	/**
	 * ����һ�У����м�ֿ�߶�
	 * 
	 * @param g
	 * @param x
	 *            Ŀ��x,����Կؼ����Ͻ�λ�õ�x����
	 * @param y
	 *            Ŀ��y,����Կؼ����Ͻ�λ�õ�y����
	 * @param _x
	 *            Դͼx,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�x����
	 * @param _y
	 *            Դͼy,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�y����
	 */
	private void paintRow(C2D_Graphics g, float x, float y, int _x, int _y)
	{
		paintRow(g, x, y, _x, _y, m_partH);
	}

	/**
	 * ����һ�У���ָ���Ŀ��
	 * 
	 * @param g
	 * @param x
	 *            Ŀ��x,����Կؼ����Ͻ�λ�õ�x����
	 * @param y
	 *            Ŀ��y,����Կؼ����Ͻ�λ�õ�y����
	 * @param _x
	 *            Դͼx,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�x����
	 * @param _y
	 *            Դͼy,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�y����
	 * @param _w
	 *            Ҫ���ƵĿ��
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
		// ����
		if (y < m_height - m_adgeH)
		{
			g.drawRegion(m_image, m_contentRect.m_x + _x, m_contentRect.m_y + _y, _w, m_height -m_adgeH - (int)y, m_transform,ltX + x, ltY + y, 0);
			y = m_height - m_adgeH;
		}
	}

	/**
	 * ����һ�У����м�ֿ���
	 * 
	 * @param g
	 * @param x
	 *            Ŀ��x,����Կؼ����Ͻ�λ�õ�x����
	 * @param y
	 *            Ŀ��y,����Կؼ����Ͻ�λ�õ�y����
	 * @param _x
	 *            Դͼx,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�x����
	 * @param _y
	 *            Դͼy,������������Ծ�Ƭͼ�п����Ͻ�λ�õ�y����
	 */
	protected void paintCol(C2D_Graphics g, int x, int y, int _x, int _y)
	{
		paintCol(g, x, y, _x, _y, m_partW);
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
		return C2D_Graphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, (byte) 0, resultRect);
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
	 * @param height
	 *            ���ؼ��ĸ߶�
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
	 * ���������ù��п��С
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
	 * ���ñ�Ե��С
	 * 
	 * @param adgeSize
	 *            ��Ե��С
	 */
	public void setAdgeSize(C2D_SizeI adgeSize)
	{
		if (adgeSize != null)
		{
			setAdgeSize(adgeSize.m_width, adgeSize.m_height);
		}
	}

	/**
	 * ���ñ�Ե��С
	 * 
	 * @param adgeW
	 *            ��Ե���
	 * @param adgeH
	 *            ��Ե�߶�
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
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
	}
}
