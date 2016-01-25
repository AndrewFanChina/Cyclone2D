package c2d.plat.gfx;

import java.nio.IntBuffer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLUtils;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_SizeI;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * 图片类
 */
public class C2D_Image extends C2D_Object
{
	/**
	 * 使用线性滤波,将在创建贴图的时候使用更精细的插值运算,会引起画面模糊.
	 */
	public static boolean useLinearFilter = false;
	/**
	 * 设置贴图时，开启贴图自动大小的情况下， 如果大于宽度或者高度大于此值，贴图将被自动缩小直到小于此值
	 */
	public static int MaxTextureSize = 1024;
	/**
	 * 是否开启贴图自动大小
	 */
	public static boolean AutoSize = true;
	/**
	 * 传入的或者创建的贴图原始大小
	 */
	protected C2D_SizeI mBitmapSize = new C2D_SizeI();
	/**
	 * 贴图经缩放和匹配OpenGL格式之后生成的位图的实际有效像素尺寸，如果位图没有经过缩放，应该与mBitmapSize相同
	 */
	private C2D_SizeI mPixelSize = new C2D_SizeI();
	/**
	 * 贴图经缩放和匹配OpenGL格式之后生成的位图大小，最终OpenGL格式贴图尺寸，在mPixelSize基础上包含了附加区域
	 */
	protected C2D_SizeI mTextureSize = new C2D_SizeI();
	/**
	 * 贴图ID
	 */
	public int mTexuteID = 0;
	/**
	 * 用作贴图的位图
	 */
	private C2D_GdiImage mImage;
	/**
	 * 图片曾经被缩小的倍率
	 */
	private float mZoomeOut = 1.0f;
	/**
	 * 总使用的贴图个数
	 */
	protected static int TEXTRUE_TOTAL = 0;

	/**
	 * 默认的构造函数
	 */
	protected C2D_Image()
	{
	}

	/** 路径名称 */
	private String m_path;

	/**
	 * 获得路径
	 * 
	 * @return
	 */
	public String getPath()
	{
		return m_path;
	}

	/**
	 * 设置路径
	 * 
	 * @param path
	 */
	public void setPath(String path)
	{
		this.m_path = path;
	}
	/**
	 * 	基于GDI图片创建
	 * 
	 * @param srcImage GDI图片
	 * @return C2D_Image 返回被创建的图片
	 */
	public static final C2D_Image createImage( C2D_GdiImage srcImage)
	{
		C2D_Image img = new C2D_Image();
		img.mImage = srcImage;
		img.rebindTexture();
		img.setPath("sys:"+img.bitmapWidth()+"x"+img.bitmapHeight());
		return img;
	}
	/**
	 * 根据尺寸创建图片，并保留位图(不销毁)，用于后续在此位图上进行位图修改，再重新绑定成贴图。
	 * 如果你需要此贴图大小超过最大限制，可以在使用此构造之前，主动改变最大限制值或者停止贴图自动大小。
	 * 
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @return C2D_Image 返回被创建的图片
	 */
	public static C2D_Image createImage(int width, int height)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createImage(width, height), false);
		return img;
	}

	/**
	 * 根据二进制数据创建指定图片 会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param imageData
	 *            二进制数据
	 * @param imageOffset
	 *            数据起始偏移
	 * @param imageLength
	 *            数据长度
	 * @return C2D_Image 返回被创建的图片
	 */
	public static C2D_Image createImage(byte[] imageData, int imageOffset, int imageLength)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createImage(imageData, imageOffset, imageLength), true);
		img.setPath("mem:"+img.bitmapWidth()+"x"+img.bitmapHeight());
		return img;
	}

	/**
	 * 根据RGB数据创建指定尺寸的图片 会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param rgb
	 *            RGB数据
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param processAlpha
	 *            是否处理半透明
	 * @return C2D_Image 返回被创建的图片
	 */
	public static C2D_Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createRGBImage(rgb, width, height), true);
		img.setPath("mem:"+img.bitmapWidth()+"x"+img.bitmapHeight());
		return img;
	}

	/**
	 * 直接从应用程序包中创建图片，默认文件夹位于imgs_ohter，是否混淆取决于当前IMG_Confused
	 * 默认情况下，会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param imageName
	 *            图片名称，包括后缀名
	 * @return C2D_Image 返回被创建的图片
	 */
	public static final C2D_Image createImage(String imageName)
	{
		return createImage(imageName, C2D_Consts.STR_OTHER_IMGS, C2D_GdiImage.IMG_Confused);
	}

	/**
	 * 直接从应用程序包中创建图片，指定位于res下的子文件夹名称，是否混淆取决于当前IMG_Confused
	 * 默认情况下，会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param imageName
	 *            图片名称，包括后缀名
	 * @param subFolder
	 *            res的子文件夹名称(图片默认存放在"res"的下一级文件夹内)
	 * @return C2D_Image 返回被创建的图片
	 */
	public static final C2D_Image createImage(String imageName, String subFolder)
	{
		return createImage(imageName, subFolder, C2D_GdiImage.IMG_Confused);
	}

	/**
	 * 直接从jar中创建图片，指定位于res下的子文件夹名称，指定图片是否混淆
	 * 
	 * @param imageName
	 *            图片名称
	 * @param subFolder
	 *            res的子文件夹名称(图片默认存放在"res"的下一级文件夹内)
	 * @param confused
	 *            是否混淆
	 * @return C2D_Image 返回被创建的图片
	 */
	public static final C2D_Image createImage(String imageName, String subFolder, boolean confused)
	{
		C2D_Image temp = null;
		byte data[] = null;
		if (subFolder == null || imageName == null)
		{
			return null;
		}
		String filePath = C2D_Consts.STR_RES + subFolder + imageName;
		data = C2D_IOUtil.readRes_Bytes(filePath);
		if (confused)
		{
			C2D_IOUtil.deConfuse(data);
		}
		if (data != null)
		{
			try
			{
				temp = C2D_Image.createImage(data, 0, data.length);
				C2D_Debug.logC2D("loadImage success: " + imageName + ",MEM:"
						+ (Runtime.getRuntime().freeMemory() / 1024) + "k");
			}
			catch (Exception e)
			{
				C2D_Debug.logErr("loadImage failed: " + imageName);
				e.printStackTrace();
			}
		}
		if(temp!=null)
		{
			temp.setPath(filePath);	
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
	 * @return
	 */
	public static final C2D_Image createPalleteImage(String folderName, String imageName, String strPmt)
	{
		C2D_Image temp = null;
		String path=C2D_Consts.STR_RES + folderName + imageName;
		try
		{
			byte imgData[] = null;
			imgData = C2D_IOUtil.readRes_Bytes(path);
			if (imgData != null)
			{
				if (strPmt != null && !strPmt.equals(""))
				{
					C2D_ColorTable colorTable = new C2D_ColorTable(C2D_Consts.STR_RES + folderName + strPmt
							+ C2D_Consts.STR_IMG_PMT);
					colorTable.changePngPalette(imgData);
					temp = C2D_Image.createImage(imgData, 0, imgData.length);
					colorTable.doRelease();
					colorTable = null;
				}
				else
				{
					temp = C2D_Image.createImage(imgData, 0, imgData.length);
				}
			}
			imgData = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (temp == null || temp.mImage == null)
		{
			C2D_Debug.log("loadImage failed... " + imageName + "," + strPmt);
		}
		if(temp!=null)
		{
			temp.setPath(path);	
		}
		return temp;
	}
	
	/**
	 * 从网络加载图片
	 * 默认情况下，会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param imageName
	 *            包含url的图片路径，带文件后缀名
	 * @return 生成好的图片
	 */
	public static final C2D_Image createHttpImage(String imageName)
	{
		C2D_Image newInstance = new C2D_Image();
		newInstance.setImage(C2D_GdiImage.createHttpImage(imageName), true);
		return newInstance;
	}

	/**
	 * 从网络加载图片
	 * 默认情况下，会在创建完成贴图之后，自动销毁创建的源位图
	 * 
	 * @param httpUrl
	 *            网络资源文件夹路径
	 * @param name
	 *            资源名称,带后缀名
	 * @return 生成好的图片
	 */
	public static final C2D_Image createHttpImage(String httpUrl, String name)
	{
		C2D_Image temp = createHttpImage(httpUrl + name);
		return temp;
	}

	/**
	 * 根据位图创建贴图图片，并指示是否在创建完成贴图之后，自动销毁位图
	 * 注意执行此构造方法之后，C2D_Image将拥有C2D_GdiImage实际控制权，请不要再对此位图进行操作，
	 * 因为在创建贴图过程中，可能会重建大小，并销毁原图。因此你传入的原图可能会已经被销毁。
	 * 
	 * @param image
	 *            位图
	 * @param releaseBitmap
	 *            是否释放位图
	 */
	public static final C2D_Image createImage(C2D_GdiImage image, boolean releaseBitmap)
	{
		C2D_Image newInstance = new C2D_Image();
		newInstance.setImage(image, releaseBitmap);
		return newInstance;
	}

	/**
	 * 根据存在的位图创建贴图图片，并指定贴图创建后是否销毁位图图片。
	 * 注意调用此方法之后，C2D_GdiImage将拥有此image实际控制权，在外部请不要再对它进行操作，
	 * 因为在创建贴图过程中，可能会重建大小，并销毁原图。因此你传入的原图可能会已经被销毁。
	 * 
	 * @param image
	 *            用来制作贴图的位图
	 * @param releaseImage
	 *            是否释放位图
	 */
	public void setImage(C2D_GdiImage image, boolean releaseImage)
	{
		if (mImage != null)
		{
			mImage.doRelease(this);
			mImage = null;
		}
		if (image != null)
		{
			mBitmapSize.setValue(image.getWidth(), image.getHeight());
			mPixelSize.setValue(mBitmapSize.m_width, mBitmapSize.m_height);
			mTextureSize.m_width = C2D_Math.getMultipleOfTwo(mPixelSize.m_width);
			mTextureSize.m_height = C2D_Math.getMultipleOfTwo(mPixelSize.m_height);
			mZoomeOut = 1.0f;
			if (AutoSize)
			{
				while (mTextureSize.m_width > MaxTextureSize || mTextureSize.m_height > MaxTextureSize)
				{
					mTextureSize.m_width /= 2;
					mTextureSize.m_height /= 2;
					mPixelSize.m_width *= 0.5f;
					mPixelSize.m_height *= 0.5f;
					mZoomeOut *= 0.5f;
				}
			}
			if (mPixelSize.m_width != mTextureSize.m_width || mPixelSize.m_height != mTextureSize.m_height)
			{
				boolean hasAlpha = image.getInner().hasAlpha();
				Bitmap bitmapScaled = Bitmap.createBitmap(mTextureSize.m_width, mTextureSize.m_height, hasAlpha ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
				Canvas canvas = new Canvas(bitmapScaled);
				canvas.drawBitmap(image.getInner(), new Rect(0, 0, image.getWidth(), image.getHeight()), new RectF(0, 0, image.getWidth() * mZoomeOut, image.getHeight() * mZoomeOut), null);
				image.doRelease(this);
				image.setInner(bitmapScaled);
			}
			mImage = image;
			rebindTexture();
		}
		if (releaseImage)
		{
			this.releaseBitmap();
		}
	}

	/**
	 * 获取图片，只有选择保留方式创建的贴图才会拥有未被销毁的图片
	 * 
	 * @return 位图图片
	 */
	public C2D_GdiImage getImage()
	{
		return mImage;
	}

	/**
	 * 贴图宽度
	 */
	public int textureWidth()
	{
		return mTextureSize.m_width;
	}

	/**
	 * 贴图高度
	 */
	public int textureHight()
	{
		return mTextureSize.m_height;
	}

	/**
	 * 位图原始宽度
	 */
	public int bitmapWidth()
	{
		return mBitmapSize.m_width;
	}

	/**
	 * 位图原始高度
	 */
	public int bitmapHeight()
	{
		return mBitmapSize.m_height;
	}

	/**
	 * 位图经缩放和匹配OpenGL格式之后生成的位图的实际有效像素的宽度
	 */
	public int pixelWidth()
	{
		return mPixelSize.m_width;
	}

	/**
	 * 位图经缩放和匹配OpenGL格式之后生成的位图的实际有效像素的高度
	 */
	public int pixelHeight()
	{
		return mPixelSize.m_height;
	}

	/**
	 * 获取位图曾经被缩小的层级
	 * @return 位图曾经被缩小的层级
	 */
	public float getZoomOut()
	{
		return mZoomeOut;
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
	 * 重新绑定贴图，当使用保留位图方式构造C2D_GdiImage时， 可以在绘制新内容至位图之后使用此函数重新绑定到OpenGL
	 */
	public void rebindTexture()
	{
		if (mImage == null)
		{
			return;
		}
		setPath(mImage.getName());
		mTextureSize.m_width = mImage.getWidth();
		mTextureSize.m_height = mImage.getHeight();

		C2D_Graphics.setTextureState(C2D_Graphics.TexState_TEXTURE);
		IntBuffer intBuf = IntBuffer.allocate(1);
		intBuf.put(mTexuteID);
		intBuf.position(0);
		if (mTexuteID != 0)
		{
			C2D_Graphics.glDeleteTextures(1, intBuf);
			mTexuteID = 0;
			DecreaseTexture();
		}
		IntBuffer textures = IntBuffer.allocate(1);
		C2D_Graphics.glGenTextures(1, textures);
		mTexuteID = textures.array()[0];
		IncreaseTexture();

		C2D_Graphics.glBindTexture(C2D_Graphics.GL_TEXTURE_2D, mTexuteID);
		if (useLinearFilter)
		{
			C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_MIN_FILTER, C2D_Graphics.GL_LINEAR);
			C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_MAG_FILTER, C2D_Graphics.GL_LINEAR);
		}
		else
		{
			C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_MIN_FILTER, C2D_Graphics.GL_NEAREST);
			C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_MAG_FILTER, C2D_Graphics.GL_NEAREST);
		}
		C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_WRAP_S, C2D_Graphics.GL_CLAMP_TO_EDGE);
		C2D_Graphics.glTexParameterf(C2D_Graphics.GL_TEXTURE_2D, C2D_Graphics.GL_TEXTURE_WRAP_T, C2D_Graphics.GL_CLAMP_TO_EDGE);

		android.graphics.Bitmap bitmap = mImage.getInner();
//		int pixels[] = new int[bitmap.getWidth() * bitmap.getHeight()];
//		bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		// byte pixelBytes[]=MathUtil.pixelInt2Byte(pixels);
		// C2D_Graphics.glTexImage2D(C2D_Graphics.GL_TEXTURE_2D, 0, 4, bitmap.getWidth(),
		// bitmap.getHeight(), 0, C2D_Graphics.GL_RGBA, C2D_Graphics.GL_UNSIGNED_BYTE,
		// pixelBytes) ;
		// GLUtils.texImage2D(C2D_Graphics.GL_TEXTURE_2D,0,4,bitmap,C2D_Graphics.GL_UNSIGNED_BYTE,0);
//		pixels = null;
		// pixelBytes=null;
		GLUtils.texImage2D(C2D_Graphics.GL_TEXTURE_2D, 0, bitmap, 0);
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
	public static final C2D_Image resize(C2D_Image img, int newW, int newH, boolean fast)
	{
		if (img == null ||newW<=0||newH<=0)
		{
			return img;
		}
		C2D_GdiImage image = C2D_GdiImage.resize(img.getImage(), newW, newH, fast);
		if(image!=null)
		{
			img.setImage(image, true);
		}
		return img;
	}
	/**
	 * 获得绘制句柄.
	 * 
	 * @return 绘图画笔
	 */
	public C2D_GdiGraphics getGraphics()
	{
		if (mImage == null)
		{
			return null;
		}
		return mImage.getGraphics();
	}

	/**
	 * 释放创建贴图的位图
	 */
	public void releaseBitmap()
	{
		if (mImage != null)
		{
			mImage.doRelease(this);
			mImage = null;
			if(m_path!=null)
			{
				C2D_Debug.log("release image:"+m_path);
			}
		}
	}

	/**
	 * 释放贴图
	 */
	protected void releaseTextureImage()
	{
		if (mTexuteID != 0)
		{
			C2D_Graphics.setTextureState(C2D_Graphics.TexState_TEXTURE);
			IntBuffer textures = IntBuffer.allocate(1);
			textures.put(mTexuteID);
			textures.position(0);
			C2D_Graphics.glDeleteTextures(1, textures);
			mTexuteID = 0;
			DecreaseTexture();
		}
	}

	private static C2D_Array M_TxtrueList = new C2D_Array();

	/**
	 * 增加贴图缓冲计数
	 */
	protected void IncreaseTexture()
	{
		if (!M_TxtrueList.contains(this))
		{
			M_TxtrueList.addElement(this);
			TEXTRUE_TOTAL++;
			// C2D_MiscUtil.log("TEXTRUE_TOTAL:"+TEXTRUE_TOTAL);
		}

	}

	/**
	 * 减少贴图缓冲计数
	 */
	protected void DecreaseTexture()
	{
		if (M_TxtrueList.contains(this))
		{
			M_TxtrueList.remove(this);
			TEXTRUE_TOTAL--;
			// C2D_MiscUtil.log("TEXTRUE_TOTAL:"+TEXTRUE_TOTAL);
		}
	}

	/**
	 * 显示所有的贴图
	 */
	public static void LogTextureList(String flag)
	{
		C2D_Debug.log("[-----" + flag + "-----");
		int numTXT=M_TxtrueList.size();
		for (int i = 0; i < numTXT; i++)
		{
			C2D_Image img = (C2D_Image) M_TxtrueList.m_datas[i];
			if (img != null)
			{
				img.logDetail();
			}
		}
		C2D_Debug.log("-----" + flag + "-----]");
	}
	/**
	 * 根据本图片产生一个切片，区域设置成指定的坐标和尺寸
	 * @param x 区域坐标X
	 * @param y 区域坐标Y
	 * @param w 区域宽度W
	 * @param h 区域高度H
	 * @return 新产生的切片
	 */
	public C2D_ImageClip makeClip(int x,int y,int w,int h)
	{
		C2D_ImageClip clip=new C2D_ImageClip(this);
		clip.setContentRect(x, y, w, h);
		return clip;
	}
	/**
	 * 打印信息
	 */
	public void logError()
	{
		if (getPath() != null)
		{
			C2D_Debug.logErr(getPath()+" lost");
		}
		else
		{
			C2D_Debug.logErr("unknown image path"+" lost");
		}
	}
	/**
	 * 打印信息
	 */
	public void logDetail()
	{
		if (getPath() != null)
		{
			C2D_Debug.log(getPath());
		}
		else
		{
			C2D_Debug.log("unknown image path");
		}
	}
	/**
	 * 释放所有资源
	 */
	public void onRelease()
	{
		releaseTextureImage();
		releaseBitmap();

	}
}
