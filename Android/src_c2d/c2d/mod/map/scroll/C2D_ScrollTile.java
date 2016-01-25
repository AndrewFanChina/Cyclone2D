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
 * 平铺图片控件类，可以拥有、设置、显示一张图片，并可以在XY方向连续进行平铺。
 * 
 * @author AndrewFan
 */
public class C2D_ScrollTile extends C2D_View
{
	/** X景深，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0 */
	protected float m_cameraFarX = 1;
	/** Y景深，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0 */
	protected float m_cameraFarY = 1;
	/** X方向循环平铺 */
	public static final int SCROLL_X = 1;
	/** Y方向循环平铺 */
	public static final int SCROLL_Y = 2;
	/** XY方向循环平铺 */
	public static final int SCROLL_XY = 3;
	/** 当前平铺方向，默认使用X方向循环平铺 */
	protected int m_scrollDir = SCROLL_X;
	/** 整个地图图片 */
	C2D_Image m_imgTile;
	/** 地图图片大小 */
	C2D_SizeI m_imgSize = new C2D_SizeI();
	/** 被分割的地图图片 */
	C2D_Image m_imgTiles[][];
	/** 上次加载的图片名称 */
	String m_imageName;
	/** 上次加载的图片文件夹 */
	String m_subFolder;
	/** 上次加载使用的加载模式 */
	int[] m_loadMode;

	/**
	 * 创建滚动方格图片
	 * 
	 * @param imageName
	 *            图片名称
	 * @param subFolder
	 *            子文件夹名称
	 * @param loadMode
	 *            加载模式
	 */
	public C2D_ScrollTile(String imageName, String subFolder, int[] loadMode)
	{
		setImage(imageName, subFolder, loadMode);
	}

	/**
	 * 获取X方向的景深值，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0
	 * 
	 * @return C2D_Float
	 */
	public float getCameraFarX()
	{
		return m_cameraFarX;
	}

	/**
	 * 设置平X方向的景深值，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0
	 * 
	 * @param far
	 *            景深值
	 */
	public void setCameraFarX(float far)
	{
		m_cameraFarX = far;
	}

	/**
	 * 获取Y方向的景深值，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0
	 * 
	 * @return C2D_Float
	 */
	public float getCameraFarY()
	{
		return m_cameraFarY;
	}

	/**
	 * 设置平Y方向的景深值，初始值为1，此值越小，代表离相机越远，随着相机的移动越缓慢，最远处为0
	 * 
	 * @param far
	 *            景深值
	 */
	public void setCameraFarY(float far)
	{
		m_cameraFarY = far;
	}

	protected static C2D_RectS m_limitRect = new C2D_RectS();

	/**
	 * 绘制节点
	 * 
	 * @param g
	 *            画笔
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
		// 计算填充的起点
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
		// 将坐标折算到最接近屏幕的值
		if (ltX < 0)
		{
			ltX += ((-ltX) / wImg) * wImg;
		}
		if (ltY < 0)
		{
			ltY += ((-ltY) / hImg) * hImg;
		}
		// 计算图片上纹理的起点
		int y = (int) ltY;
		int CW = cameraSize.m_width;
		int CH = cameraSize.m_height;
		// 如果以单图绘制
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
		// 以多图绘制
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
	 * 设置滚动方格图片
	 * 
	 * @param imageName
	 *            图片名称
	 * @param subFolder
	 *            子文件夹名称
	 * @param loadMode
	 *            加载模式
	 */
	private void setImage(String imageName, String subFolder, int[] loadMode)
	{
		// 先进行清除
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
	 * 卸载图片资源
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
	 * 重新加载图片，之前必须已经加载过图片，记录了图片的路径和加载模式
	 */
	public void reloadImg()
	{
		setImage(m_imageName, m_subFolder, m_loadMode);
	}

	/**
	 * 获取平铺方向
	 * 
	 * @return 平铺方向
	 */
	public int getScrollDir()
	{
		return m_scrollDir;
	}

	/**
	 * 设置平铺方向
	 * 
	 * @param scrollDir
	 *            平铺方向
	 */
	public void setScrollDir(int scrollDir)
	{
		this.m_scrollDir = scrollDir;
	}

	/**
	 * 卸载资源
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
