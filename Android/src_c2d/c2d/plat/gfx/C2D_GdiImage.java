package c2d.plat.gfx;

import java.io.InputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * GDI图片类，内部使用，负责以多种形式创建位图并维护位图占用的内存。
 * 
 * C2D_GdiImage: GDI图片类，内部使用，负责以多种形式创建位图并维护位图占用的内存。
 * C2D_GProto: 原始画笔类，内部使用，提供针对内部画笔的封装，是C2D_GdiGraphics的父类。
 * C2D_GdiGraphics: GDI画笔类，内部使用，负责针对GDI图片类的修改，即提供在其上各种形式的绘制功能。
 * C2D_Image: 图片类，提供用户使用，负责包装GDI图片类，提供给用户多种形式的创建接口。
 * C2D_Graphics: 画笔类，提供用户使用，负责将C2D_Image以多种形式绘制到屏幕。
 */
public class C2D_GdiImage extends C2D_Object
{
	android.graphics.Bitmap imgInner = null;
	/** 替代透明色，读取BMP图片时，替代透明色将被替换成透明色.一般用洋红色FF00FF作为BMP的替代透明色 */
	public static final int TransColor = 0xFFFF00FF;
	/** 宽度 */
	private int m_width;
	/** 高度 */
	private int m_height;

	C2D_GdiGraphics g = new C2D_GdiGraphics();
	/** 名称 */
	private String name="unkown";;
	/**
	 * 构造函数.
	 */
	protected C2D_GdiImage()
	{
	}
	/**
	 * 获得名称
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * 从内存创建指定尺寸的图片，一般用做缓冲图
	 * 
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static C2D_GdiImage createImage(int width, int height)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
		img.updateSize();
		img.setName("mem_"+img.getWidth()+"x"+img.getHeight());
		return img;
	}

	/**
	 * 根据二进制数据创建指定图片.
	 * 
	 * @param imageData
	 *            二进制数据
	 * @param imageOffset
	 *            数据起始偏移
	 * @param imageLength
	 *            数据长度
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static C2D_GdiImage createImage(byte[] imageData, int imageOffset, int imageLength)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength);
		img.updateSize();
		return img;
	}

	/**
	 * 根据RGB数据创建指定尺寸的图片.
	 * 
	 * @param rgb
	 *            RGB数据
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static C2D_GdiImage createRGBImage(int[] rgb, int width, int height)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.Bitmap.createBitmap(rgb, width, height, android.graphics.Bitmap.Config.ARGB_8888);
		img.updateSize();
		return img;
	}


	/**
	 * 直接从应用程序包中创建图片，默认文件夹位于imgs_ohter，是否混淆取决于当前IMG_Confused
	 * 
	 * @param imageName
	 *            图片名称，包括后缀名
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static final C2D_GdiImage createImage(String imageName)
	{
		return createImage(imageName, C2D_Consts.STR_OTHER_IMGS, IMG_Confused);
	}

	/**
	 * 直接从应用程序包中创建图片，指定位于res下的子文件夹名称，是否混淆取决于当前IMG_Confused
	 * 
	 * @param imageName
	 *            图片名称，包括后缀名
	 * @param subFolder
	 *            res的子文件夹名称(图片默认存放在"res"的下一级文件夹内)
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static final C2D_GdiImage createImage(String imageName, String subFolder)
	{
		return createImage(imageName, subFolder, IMG_Confused);
	}

	/**
	 * 直接从应用程序包中创建图片，指定位于res下的子文件夹名称，是否混淆取决于当前IMG_Confused
	 * 
	 * @param imageName
	 *            图片名称
	 * @param subFolder
	 *            res的子文件夹名称(图片默认存放在"res"的下一级文件夹内)
	 * @param confused
	 *            是否混淆
	 * @return C2D_GdiImage 返回被创建的图片
	 */
	public static final C2D_GdiImage createImage(String imageName, String subFolder, boolean confused)
	{
		C2D_GdiImage temp = null;
		byte data[] = null;
		if (subFolder == null || imageName == null)
		{
			return null;
		}
		String path=C2D_Consts.STR_RES + subFolder + imageName;
		data = C2D_IOUtil.readRes_Bytes(path, 0, -1);
		if (confused)
		{
			C2D_IOUtil.deConfuse(data);
		}
		if (data != null)
		{
			try
			{
				temp = C2D_GdiImage.createImage(data, 0, data.length);
				if(temp!=null)
				{
					temp.setName(path+"_"+temp.getWidth()+"x"+temp.getHeight());
				}
			}
			catch (Exception e)
			{
				C2D_Debug.log("loadImage failed. " + imageName);
				e.printStackTrace();
			}
		}
		return temp;
	}

	/**
	 * 根据图片名称和调色板文件载入调色板图片
	 * 
	 * @param folderName
	 *            所在文件夹名称
	 * @param imageName
	 *            源图片名称
	 * @param strPmt
	 *            调色板文件名称
	 * @return 加载了调色板的图片
	 */
	public static final C2D_GdiImage createPalleteImage(String folderName, String imageName, String strPmt)
	{
		C2D_GdiImage temp = null;
		try
		{
			byte imgData[] = null;
			imgData = C2D_IOUtil.readRes_Bytes(imageName, 0, -1);
			if (imgData != null)
			{
				if (strPmt != null && !strPmt.equals(""))
				{
					C2D_ColorTable colorTable = new C2D_ColorTable(C2D_Consts.STR_RES + folderName + strPmt + C2D_Consts.STR_IMG_PMT);
					colorTable.changePngPalette(imgData);
					temp = C2D_GdiImage.createImage(imgData, 0, imgData.length);
					if(temp!=null)
					{
						temp.setName(imageName+"_"+temp.getWidth()+"x"+temp.getHeight());
					}
					colorTable.doRelease();
					colorTable = null;

				}
				else
				{
					temp = C2D_GdiImage.createImage(imgData, 0, imgData.length);
				}
			}
			imgData = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (temp == null || temp.imgInner == null)
		{
			C2D_Debug.log("loadImage failed... " + imageName + "," + strPmt);
		}
		return temp;
	}

	/**
	 * 根据图片名称、半透明混合图名称、调色板文件和指定半透明度载入图片 这个图片将会先执行调色板换色，接着图片Alpha混合，最后数值Aplha混合.
	 * 
	 * @param imgName
	 *            源图片名称，不带后缀名
	 * @param alphaImgName
	 *            半透明混合图名称，不带后缀名
	 * @param pmtImgName
	 *            调色板文件名称，不带后缀名
	 * @param postfixName
	 *            前述图片的后缀名
	 * @param alpha
	 *            想要呈现的半透明度
	 * @return the C2D image
	 */
	public static final C2D_GdiImage createAlphaImage(String folderName, String imgName, String alphaImgName, String pmtImgName, String postfixName, short alpha)
	{
		if (imgName == null || imgName.length() == 0)
		{
			return null;
		}
		if (alpha == 0xFF && (pmtImgName == null || pmtImgName.length() == 0) && (alphaImgName == null || alphaImgName.length() == 0))
		{
			return createImage(imgName + postfixName, folderName);
		}
		C2D_GdiImage imgsrcImg = createPalleteImage(folderName, imgName + postfixName, pmtImgName);
		if (imgsrcImg == null || imgsrcImg.imgInner == null)
		{
			return null;
		}
		int w = imgsrcImg.getWidth();
		int h = imgsrcImg.getHeight();
		if (w * h == 0)
		{
			C2D_Debug.log("size of image is not valid .... " + imgName + "," + pmtImgName);
			return null;
		}
		int dataSrc[] = null;
		try
		{
			if ((alphaImgName != null && alphaImgName.length() > 0) || alpha != 0xFF)
			{
				dataSrc = new int[w * h];
				int dataLen = dataSrc.length;
				imgsrcImg.getRGB(dataSrc, 0, w, 0, 0, w, h);
				imgsrcImg.doRelease();
				imgsrcImg = null;
				if (alphaImgName != null && alphaImgName.length() > 0)
				{
					C2D_GdiImage alphaImg = createImage(alphaImgName + postfixName, folderName);
					if (alphaImg != null && alphaImg.getWidth() == w && alphaImg.getHeight() == h)
					{
						int dataAlpha[] = new int[w * h];
						alphaImg.getRGB(dataAlpha, 0, w, 0, 0, w, h);
						alphaImg = null;
						for (int i = 0; i < dataLen; i++)
						{
							dataSrc[i] = (dataSrc[i] & 0xFFFFFF) | ((dataAlpha[i] & 0xFF) << 24);
						}
						dataAlpha = null;
					}
				}
				if (alpha != 0xFF)
				{
					for (int i = 0; i < dataLen; i++)
					{
						dataSrc[i] = (dataSrc[i] & 0xFFFFFF) | ((((dataSrc[i] >> 24) & 0xFF) * alpha / 0xFF) << 24);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (dataSrc != null)
		{
			imgsrcImg = C2D_GdiImage.createRGBImage(dataSrc, w, h);
			dataSrc = null;
		}
		if (imgsrcImg == null)
		{
			C2D_Debug.log("loadImage failed.... " + imgName + "," + pmtImgName);
		}
		return imgsrcImg;
	}

	/**
	 * 根据图片和指定的数值Aplha信息返回混合后的图片.
	 * 
	 * @param srcImg
	 *            源图片
	 * @param alpha
	 *            想要呈现的半透明度
	 * @return the C2D image
	 */
	public static final C2D_GdiImage createAlphaImage(C2D_GdiImage srcImg, int alpha)
	{
		if (srcImg == null)
		{
			return null;
		}
		int w = srcImg.getWidth();
		int h = srcImg.getHeight();
		int dataSrc[] = new int[w * h];
		srcImg.getRGB(dataSrc, 0, w, 0, 0, w, h);
		srcImg = null;
		int dataLen = dataSrc.length;
		for (int i = 0; i < dataLen; i++)
		{
			if (((dataSrc[i] >> 24) & 0xFF) != 0)
			{
				dataSrc[i] = (dataSrc[i] & 0xFFFFFF) | ((alpha & 0xFF) << 24);
			}
		}
		C2D_GdiImage newImg = null;
		newImg = C2D_GdiImage.createRGBImage(dataSrc, w, h);
		dataSrc = null;
		return newImg;
	}

	/**
	 * 从网络加载图片
	 * 
	 * @param imageName
	 *            包含url的图片路径，带文件后缀名
	 * @return 生成好的图片
	 */
	public static final C2D_GdiImage createHttpImage(String imageName)
	{
		android.graphics.Bitmap img = null;
		InputStream in = null;
		try
		{
			java.net.URL pictureUrl = new java.net.URL(imageName);
			in = pictureUrl.openStream();
			img = android.graphics.BitmapFactory.decodeStream(in);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		if (img != null)
		{
			C2D_GdiImage c2dImg = new C2D_GdiImage();
			c2dImg.imgInner = img;
			c2dImg.updateSize();
			c2dImg.setName(imageName+"_"+img.getWidth()+"x"+img.getHeight());
			return c2dImg;
		}
		return null;
	}

	/**
	 * 从网络加载图片
	 * 
	 * @param httpUrl
	 *            网络资源文件夹路径
	 * @param name
	 *            资源名称,带后缀名
	 * @return 生成好的图片
	 */
	public static final C2D_GdiImage createHttpImage(String httpUrl, String name)
	{
		C2D_GdiImage temp = createHttpImage(httpUrl + name);
		return temp;
	}
	
	/**
	 * 零散图片是否被混淆，默认为false，当读取的图片资源被混淆时，需要将其设置为true，再进行读取，反则反之
	 */
	public static boolean IMG_Confused = false;
	/**
	 * 更新尺寸
	 */
	private void updateSize()
	{
		if (imgInner != null)
		{
			m_width = imgInner.getWidth();
			m_height = imgInner.getHeight();
		}
		else
		{
			m_width = 0;
			m_height = 0;
		}
	}
	
	/**
	 * 获得原图句柄.一般你不需要调用此方法
	 * 
	 * @return Image 原图句柄
	 */
	public android.graphics.Bitmap getInner()
	{
		return imgInner;
	}

	/**
	 * 设置原图句柄.一般你不需要调用此方法
	 * 
	 */
	public void setInner(android.graphics.Bitmap inner)
	{
		imgInner = inner;
		updateSize();
		g.gInner = null;
	}

	/**
	 * 获得绘制句柄.
	 * 
	 * @return Graphics 绘制句柄
	 */
	public C2D_GdiGraphics getGraphics()
	{
		if (g.gInner == null)
		{
			g.gInner = new android.graphics.Canvas(imgInner);
		}
		return g;
	}

	/**
	 * 获得图片宽度.
	 * 
	 * @return int 宽度
	 */
	public int getWidth()
	{
		return m_width;
	}

	/**
	 * 获得图片高度.
	 * 
	 * @return int 高度
	 */
	public int getHeight()
	{
		return m_height;
	}

	/**
	 * 获取图片指定裁剪区域的RGB颜色阵列信息.
	 * 
	 * @param pixes
	 *            用来存放的缓冲
	 * @param offset
	 *            缓冲中存放的起始位置
	 * @param scanLen
	 *            横向扫描长度
	 * @param x
	 *            裁剪区域的x坐标
	 * @param y
	 *            裁剪区域的y坐标
	 * @param width
	 *            裁剪区域的宽度
	 * @param height
	 *            裁剪区域的高度
	 */
	public void getRGB(int pixes[], int offset, int scanLen, int x, int y, int width, int height)
	{
		imgInner.getPixels(pixes, offset, scanLen, x, y, width, height);
	}
	/**
	 * 采用邻近算法缩放图片
	 * 
	 * @param src
	 *            源图像素缓冲
	 * @param w
	 *            源图宽度
	 * @param h
	 *            源图高度
	 * @param dst
	 *            目标图像素缓冲
	 * @param newW
	 *            目标图宽度
	 * @param newH
	 *            目标图高度
	 */
	private static final void resizeNeighbor(int src[], int w, int h, int dst[], int newW, int newH)
	{
		int ofs = 0;
		if (newW == 0)
		{
			return;
		}
		int dX = (w << 16) / newW;
		int dY = (h << 16) / newH;
		int sY = 0;
		for (int y = 0; y < newH; y++)
		{
			int startY = w * (sY >> 16);
			int sX = 0;
			for (int x = 0; x < newW; x++)
			{
				int startX = (sX >> 16);
				int rgb = src[startY + startX];
				dst[ofs + x] = rgb;
				sX += dX;
			}
			sY += dY;
			ofs += newW;
		}
	}

	/**
	 * 采用线性插值缩放图片.
	 * 
	 * @param src
	 *            源图像素缓冲
	 * @param w
	 *            源图宽度
	 * @param h
	 *            源图高度
	 * @param dst
	 *            目标图像素缓冲
	 * @param newW
	 *            目标图宽度
	 * @param newH
	 *            目标图高度
	 */
	private static final void resizeLinear(int src[], int w, int h, int dst[], int newW, int newH)
	{
		int ofs = 0;
		int dX = (w << 16) / newW;
		int dY = (h << 16) / newH;
		int sY = 0;
		int eY = dY;
		for (int y = 0; y < newH; y++)
		{
			int startY = w * (sY >> 16);
			int endY = w * (eY >> 16);
			int sX = 0;
			int eX = dX;
			for (int x = 0; x < newW; x++)
			{
				int startX = (sX >> 16);
				int endX = (eX >> 16);
				int avg = -1;
				int wgt = 0;
				for (int Y = startY; Y < endY; Y += w)
				{
					for (int X = startX; X < endX; X++)
					{
						int rgb = src[Y + X];
						avg = (avg != -1) ? linearColor(avg, wgt, rgb, 1) : rgb;
						wgt++;
					}
				}
				dst[ofs + x] = avg;
				sX += dX;
				eX += dX;
			}
			sY += dY;
			eY += dY;
			ofs += newW;
		}
	}

	/**
	 * 线性插值计算.
	 * 
	 * @param p1
	 *            the p1
	 * @param w1
	 *            the w1
	 * @param p2
	 *            the p2
	 * @param w2
	 *            the w2
	 * @return the int
	 */
	private static final int linearColor(int p1, int w1, int p2, int w2)
	{
		long ag1 = (p1 >> 8) & 0x00FF00FFL;
		long rb1 = (p1) & 0x00FF00FFL;
		long ag2 = (p2 >> 8) & 0x00FF00FFL;
		long rb2 = (p2) & 0x00FF00FFL;
		long v = (w1 << 8) / (w1 + w2);
		long ag = (ag2 << 8) + (v * (ag1 - ag2));
		long rb = (rb2 << 8) + (v * (rb1 - rb2));
		return (int) (((ag & 0xFF00FF00L)) | ((rb >> 8) & 0x00FF00FFL));
	}

	/**
	 * 缩放图片.
	 * 
	 * @param img
	 *            - 即将被缩放的源图
	 * @param newW
	 *            - 新图的宽度
	 * @param newH
	 *            - 新图的高度
	 * @param fast
	 *            - 是否快速计算(快速计算采用邻近算法、否则使用线性插值)
	 * @return 返回新创建的图片
	 */
	public static final C2D_GdiImage resize(C2D_GdiImage img, int newW, int newH, boolean fast)
	{
		int srcw = img.getWidth();
		int srch = img.getHeight();
		if (srcw == newW && srch == newH)
		{
			return img;
		}
		int[] pixes = new int[srcw * srch];
		img.getRGB(pixes, 0, srcw, 0, 0, srcw, srch);

		int[] newpixes = new int[newW * newH];
		if (fast)
		{
			resizeNeighbor(pixes, srcw, srch, newpixes, newW, newH);
		}
		else
		{
			resizeLinear(pixes, srcw, srch, newpixes, newW, newH);
		}

		C2D_GdiImage result = C2D_GdiImage.createRGBImage(newpixes, newW, newH);
		newpixes = pixes = null;
		System.gc();
		return result;
	}

	/**
	 * 返回一张原图的剪切图
	 * 
	 * @param source 源图 
	 * @param x 相对于源图左上角的x坐标
	 * @param y 相对于源图左上角的y坐标
	 * @param width 目标图的宽度
	 * @param height 目标图的高度
	 * @return 剪切图
	 */
	public static final C2D_GdiImage createImage(C2D_GdiImage source, int x, int y, int width, int height)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.Bitmap.createBitmap(source.getInner(), x, y, width, height);
		img.updateSize();
		return img;
	}
	@Override
	public void onRelease()
	{
		if (imgInner != null)
		{
			imgInner.recycle();
		}
		imgInner = null;
	}
}
