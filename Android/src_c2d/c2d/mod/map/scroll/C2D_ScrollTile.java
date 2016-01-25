package c2d.mod.map.scroll;

import c2d.frame.base.C2D_View;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.lang.math.type.C2D_RectS;
import c2d.lang.math.type.C2D_SizeI;
import c2d.lang.util.C2D_Misc;
import c2d.plat.gfx.C2D_Graphics;
import c2d.plat.gfx.C2D_Image;

/**
 * ƽ��ͼƬ�ؼ��࣬����ӵ�С����á���ʾһ��ͼƬ����������XY������������ƽ�̡�
 * 
 * @author AndrewFan
 */
public class C2D_ScrollTile extends C2D_View
{
	/** X�����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0 */
	protected float m_cameraFarX = 1;
	/** Y�����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0 */
	protected float m_cameraFarY = 1;
	/** X����ѭ��ƽ�� */
	public static final int SCROLL_X = 1;
	/** Y����ѭ��ƽ�� */
	public static final int SCROLL_Y = 2;
	/** XY����ѭ��ƽ�� */
	public static final int SCROLL_XY = 3;
	/** ��ǰƽ�̷���Ĭ��ʹ��X����ѭ��ƽ�� */
	protected int m_scrollDir = SCROLL_X;
	/** ������ͼͼƬ */
	C2D_Image m_imgTile;
	/** ��ͼͼƬ��С */
	C2D_SizeI m_imgSize = new C2D_SizeI();
	/** ���ָ�ĵ�ͼͼƬ */
	C2D_Image m_imgTiles[][];
	/** �ϴμ��ص�ͼƬ���� */
	String m_imageName;
	/** �ϴμ��ص�ͼƬ�ļ��� */
	String m_subFolder;
	/** �ϴμ���ʹ�õļ���ģʽ */
	int[] m_loadMode;

	/**
	 * ������������ͼƬ
	 * 
	 * @param imageName
	 *            ͼƬ����
	 * @param subFolder
	 *            ���ļ�������
	 * @param loadMode
	 *            ����ģʽ
	 */
	public C2D_ScrollTile(String imageName, String subFolder, int[] loadMode)
	{
		setImage(imageName, subFolder, loadMode);
	}

	/**
	 * ��ȡX����ľ���ֵ����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0
	 * 
	 * @return C2D_Float
	 */
	public float getCameraFarX()
	{
		return m_cameraFarX;
	}

	/**
	 * ����ƽX����ľ���ֵ����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0
	 * 
	 * @param far
	 *            ����ֵ
	 */
	public void setCameraFarX(float far)
	{
		m_cameraFarX = far;
	}

	/**
	 * ��ȡY����ľ���ֵ����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0
	 * 
	 * @return C2D_Float
	 */
	public float getCameraFarY()
	{
		return m_cameraFarY;
	}

	/**
	 * ����ƽY����ľ���ֵ����ʼֵΪ1����ֵԽС�����������ԽԶ������������ƶ�Խ��������Զ��Ϊ0
	 * 
	 * @param far
	 *            ����ֵ
	 */
	public void setCameraFarY(float far)
	{
		m_cameraFarY = far;
	}

	protected static C2D_RectS m_limitRect = new C2D_RectS();

	/**
	 * ���ƽڵ�
	 * 
	 * @param g
	 *            ����
	 */
	protected void onPaint(C2D_Graphics g)
	{
		if (g == null || !m_visible || m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		if (m_parentNode == null || !(m_parentNode instanceof C2D_ScrollMap))
		{
			return;
		}
		C2D_Camera camera = ((C2D_ScrollMap) m_parentNode).getCamera();
		if (camera == null)
		{
			return;
		}
		// �����������
		C2D_SizeI cameraSize = camera.getSize();
		C2D_PointF lt = getLeftTop();
		float ltX = lt.m_x;
		float ltY = lt.m_y;
		if ((m_scrollDir & SCROLL_X) != 0)
		{
			ltX = lt.m_x * m_cameraFarX;
		}
		if ((m_scrollDir & SCROLL_Y) != 0)
		{
			ltY = lt.m_y * m_cameraFarY;
		}
		if (ltX < -m_width || ltX > cameraSize.m_width || ltY < -m_height || ltY > cameraSize.m_height)
		{
			return;
		}
		int wImg = m_imgSize.getWidth();
		int hImg = m_imgSize.getHeight();
		if (wImg <= 0 || hImg <= 0)
		{
			return;
		}
		// ���������㵽��ӽ���Ļ��ֵ
		if (ltX < 0)
		{
			ltX += ((-ltX) / wImg) * wImg;
		}
		if (ltY < 0)
		{
			ltY += ((-ltY) / hImg) * hImg;
		}
		// ����ͼƬ����������
		int y = (int) ltY;
		int CW = cameraSize.m_width;
		int CH = cameraSize.m_height;
		// ����Ե�ͼ����
		if (m_imgTile != null)
		{
			C2D_Image image = m_imgTile;
			while (y < CH)
			{
				if (y + hImg >= 0)
				{
					int xT = (int) ltX;
					while (xT < CW)
					{
						if (xT + wImg >= 0)
						{
							g.drawRegion(image, 0, 0, wImg, hImg, C2D_Graphics.TRANS_NONE, xT, y, 0);
						}
						xT += wImg;
						if (m_scrollDir == SCROLL_Y)
						{
							break;
						}
					}
				}
				y += hImg;
				if (m_scrollDir == SCROLL_X)
				{
					break;
				}
			}
		}
		// �Զ�ͼ����
		else if (m_imgTiles != null)
		{
			while (y < CH)
			{
				if (y + hImg >= 0)
				{
					float x = ltX;
					int yID = 0;
					while (x < CW)
					{
						if (x + wImg >= 0)
						{
							int yRow = y;
							for (int i = 0; i < m_imgTiles.length; i++)
							{
								float xCol = x;
								int _h = m_imgTiles[yID][0].bitmapHeight();
								if (yRow + _h >= 0 && yRow < CH)
								{
									for (int j = 0; j < m_imgTiles[yID].length; j++)
									{
										int _w = m_imgTiles[yID][j].bitmapWidth();
										if (xCol + _w >= 0 && xCol < CW)
										{
											g.drawImage(m_imgTiles[yID][j], xCol, yRow, 0);
										}
										xCol += _w;
									}
								}
								yRow += _h;
								yID++;
								yID %= m_imgTiles.length;
							}
						}
						x += wImg;
						if (m_scrollDir == SCROLL_Y)
						{
							break;
						}
					}
				}
				y += hImg;
				if (m_scrollDir == SCROLL_X)
				{
					break;
				}
			}
		}
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
	 * ���ù�������ͼƬ
	 * 
	 * @param imageName
	 *            ͼƬ����
	 * @param subFolder
	 *            ���ļ�������
	 * @param loadMode
	 *            ����ģʽ
	 */
	private void setImage(String imageName, String subFolder, int[] loadMode)
	{
		// �Ƚ������
		releaseImg();
		m_imageName = imageName;
		m_subFolder = subFolder;
		m_loadMode = loadMode;
		if (loadMode == null)
		{
			C2D_Image image = C2D_Image.createImage(imageName, subFolder);
			m_imgTile = image;
			if (m_imgTile != null)
			{
				m_imgSize.setValue(m_imgTile.bitmapWidth(), m_imgTile.bitmapHeight());
				m_imgTile.transHadler(this);
				;
			}
		}
		else
		{
			String namePt[] = C2D_Misc.getSubffix(imageName);
			m_imgTiles = new C2D_Image[loadMode.length][];
			int w = 0;
			int h = 0;
			for (int i = 0; i < m_imgTiles.length; i++)
			{
				m_imgTiles[i] = new C2D_Image[loadMode[i]];
				int wI = 0;
				for (int j = 0; j < m_imgTiles[i].length; j++)
				{
					m_imgTiles[i][j] = C2D_Image.createImage(namePt[0] + "_" + i + "_" + j + namePt[1], subFolder);
					wI += m_imgTiles[i][j].bitmapWidth();
					m_imgTiles[i][j].transHadler(this);
					;
				}
				if (m_imgTiles[i][0] != null)
				{
					h += m_imgTiles[i][0].bitmapHeight();
				}
				if (wI > w)
				{
					w = wI;
				}
			}
			m_imgSize.setValue(w, h);
		}
	}

	/**
	 * ж��ͼƬ��Դ
	 */
	public void releaseImg()
	{
		if (m_imgTile != null)
		{
			m_imgTile.doRelease();
			m_imgTile = null;
		}
		if (m_imgTiles != null)
		{
			for (int i = 0; i < m_imgTiles.length; i++)
			{
				if (m_imgTiles[i] == null)
				{
					continue;
				}
				for (int j = 0; j < m_imgTiles[i].length; j++)
				{
					if (m_imgTiles[i][j] != null)
					{
						m_imgTiles[i][j].doRelease(this);
						m_imgTiles[i][j] = null;
					}
				}
			}
		}
	}

	/**
	 * ���¼���ͼƬ��֮ǰ�����Ѿ����ع�ͼƬ����¼��ͼƬ��·���ͼ���ģʽ
	 */
	public void reloadImg()
	{
		setImage(m_imageName, m_subFolder, m_loadMode);
	}

	/**
	 * ��ȡƽ�̷���
	 * 
	 * @return ƽ�̷���
	 */
	public int getScrollDir()
	{
		return m_scrollDir;
	}

	/**
	 * ����ƽ�̷���
	 * 
	 * @param scrollDir
	 *            ƽ�̷���
	 */
	public void setScrollDir(int scrollDir)
	{
		this.m_scrollDir = scrollDir;
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		m_imageName = null;
		m_subFolder = null;
		m_loadMode = null;
		releaseImg();
	}
}
