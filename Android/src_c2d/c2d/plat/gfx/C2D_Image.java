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
 * ͼƬ��
 */
public class C2D_Image extends C2D_Object
{
	/**
	 * ʹ�������˲�,���ڴ�����ͼ��ʱ��ʹ�ø���ϸ�Ĳ�ֵ����,��������ģ��.
	 */
	public static boolean useLinearFilter = false;
	/**
	 * ������ͼʱ��������ͼ�Զ���С������£� ������ڿ�Ȼ��߸߶ȴ��ڴ�ֵ����ͼ�����Զ���Сֱ��С�ڴ�ֵ
	 */
	public static int MaxTextureSize = 1024;
	/**
	 * �Ƿ�����ͼ�Զ���С
	 */
	public static boolean AutoSize = true;
	/**
	 * ����Ļ��ߴ�������ͼԭʼ��С
	 */
	protected C2D_SizeI mBitmapSize = new C2D_SizeI();
	/**
	 * ��ͼ�����ź�ƥ��OpenGL��ʽ֮�����ɵ�λͼ��ʵ����Ч���سߴ磬���λͼû�о������ţ�Ӧ����mBitmapSize��ͬ
	 */
	private C2D_SizeI mPixelSize = new C2D_SizeI();
	/**
	 * ��ͼ�����ź�ƥ��OpenGL��ʽ֮�����ɵ�λͼ��С������OpenGL��ʽ��ͼ�ߴ磬��mPixelSize�����ϰ����˸�������
	 */
	protected C2D_SizeI mTextureSize = new C2D_SizeI();
	/**
	 * ��ͼID
	 */
	public int mTexuteID = 0;
	/**
	 * ������ͼ��λͼ
	 */
	private C2D_GdiImage mImage;
	/**
	 * ͼƬ��������С�ı���
	 */
	private float mZoomeOut = 1.0f;
	/**
	 * ��ʹ�õ���ͼ����
	 */
	protected static int TEXTRUE_TOTAL = 0;

	/**
	 * Ĭ�ϵĹ��캯��
	 */
	protected C2D_Image()
	{
	}

	/** ·������ */
	private String m_path;

	/**
	 * ���·��
	 * 
	 * @return
	 */
	public String getPath()
	{
		return m_path;
	}

	/**
	 * ����·��
	 * 
	 * @param path
	 */
	public void setPath(String path)
	{
		this.m_path = path;
	}
	/**
	 * 	����GDIͼƬ����
	 * 
	 * @param srcImage GDIͼƬ
	 * @return C2D_Image ���ر�������ͼƬ
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
	 * ���ݳߴ紴��ͼƬ��������λͼ(������)�����ں����ڴ�λͼ�Ͻ���λͼ�޸ģ������°󶨳���ͼ��
	 * �������Ҫ����ͼ��С����������ƣ�������ʹ�ô˹���֮ǰ�������ı��������ֵ����ֹͣ��ͼ�Զ���С��
	 * 
	 * @param width
	 *            ͼƬ���
	 * @param height
	 *            ͼƬ�߶�
	 * @return C2D_Image ���ر�������ͼƬ
	 */
	public static C2D_Image createImage(int width, int height)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createImage(width, height), false);
		return img;
	}

	/**
	 * ���ݶ��������ݴ���ָ��ͼƬ ���ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param imageData
	 *            ����������
	 * @param imageOffset
	 *            ������ʼƫ��
	 * @param imageLength
	 *            ���ݳ���
	 * @return C2D_Image ���ر�������ͼƬ
	 */
	public static C2D_Image createImage(byte[] imageData, int imageOffset, int imageLength)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createImage(imageData, imageOffset, imageLength), true);
		img.setPath("mem:"+img.bitmapWidth()+"x"+img.bitmapHeight());
		return img;
	}

	/**
	 * ����RGB���ݴ���ָ���ߴ��ͼƬ ���ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param rgb
	 *            RGB����
	 * @param width
	 *            ͼƬ���
	 * @param height
	 *            ͼƬ�߶�
	 * @param processAlpha
	 *            �Ƿ����͸��
	 * @return C2D_Image ���ر�������ͼƬ
	 */
	public static C2D_Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha)
	{
		C2D_Image img = new C2D_Image();
		img.setImage(C2D_GdiImage.createRGBImage(rgb, width, height), true);
		img.setPath("mem:"+img.bitmapWidth()+"x"+img.bitmapHeight());
		return img;
	}

	/**
	 * ֱ�Ӵ�Ӧ�ó�����д���ͼƬ��Ĭ���ļ���λ��imgs_ohter���Ƿ����ȡ���ڵ�ǰIMG_Confused
	 * Ĭ������£����ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param imageName
	 *            ͼƬ���ƣ�������׺��
	 * @return C2D_Image ���ر�������ͼƬ
	 */
	public static final C2D_Image createImage(String imageName)
	{
		return createImage(imageName, C2D_Consts.STR_OTHER_IMGS, C2D_GdiImage.IMG_Confused);
	}

	/**
	 * ֱ�Ӵ�Ӧ�ó�����д���ͼƬ��ָ��λ��res�µ����ļ������ƣ��Ƿ����ȡ���ڵ�ǰIMG_Confused
	 * Ĭ������£����ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param imageName
	 *            ͼƬ���ƣ�������׺��
	 * @param subFolder
	 *            res�����ļ�������(ͼƬĬ�ϴ����"res"����һ���ļ�����)
	 * @return C2D_Image ���ر�������ͼƬ
	 */
	public static final C2D_Image createImage(String imageName, String subFolder)
	{
		return createImage(imageName, subFolder, C2D_GdiImage.IMG_Confused);
	}

	/**
	 * ֱ�Ӵ�jar�д���ͼƬ��ָ��λ��res�µ����ļ������ƣ�ָ��ͼƬ�Ƿ����
	 * 
	 * @param imageName
	 *            ͼƬ����
	 * @param subFolder
	 *            res�����ļ�������(ͼƬĬ�ϴ����"res"����һ���ļ�����)
	 * @param confused
	 *            �Ƿ����
	 * @return C2D_Image ���ر�������ͼƬ
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
	 * ����ͼƬ���ƺ͵�ɫ���ļ������ɫ��ͼƬ
	 * 
	 * @param folderName
	 *            �����ļ�������
	 * @param imageName
	 *            ԴͼƬ����
	 * @param strPmt
	 *            ��ɫ���ļ�����
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
	 * ���������ͼƬ
	 * Ĭ������£����ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param imageName
	 *            ����url��ͼƬ·�������ļ���׺��
	 * @return ���ɺõ�ͼƬ
	 */
	public static final C2D_Image createHttpImage(String imageName)
	{
		C2D_Image newInstance = new C2D_Image();
		newInstance.setImage(C2D_GdiImage.createHttpImage(imageName), true);
		return newInstance;
	}

	/**
	 * ���������ͼƬ
	 * Ĭ������£����ڴ��������ͼ֮���Զ����ٴ�����Դλͼ
	 * 
	 * @param httpUrl
	 *            ������Դ�ļ���·��
	 * @param name
	 *            ��Դ����,����׺��
	 * @return ���ɺõ�ͼƬ
	 */
	public static final C2D_Image createHttpImage(String httpUrl, String name)
	{
		C2D_Image temp = createHttpImage(httpUrl + name);
		return temp;
	}

	/**
	 * ����λͼ������ͼͼƬ����ָʾ�Ƿ��ڴ��������ͼ֮���Զ�����λͼ
	 * ע��ִ�д˹��췽��֮��C2D_Image��ӵ��C2D_GdiImageʵ�ʿ���Ȩ���벻Ҫ�ٶԴ�λͼ���в�����
	 * ��Ϊ�ڴ�����ͼ�����У����ܻ��ؽ���С��������ԭͼ������㴫���ԭͼ���ܻ��Ѿ������١�
	 * 
	 * @param image
	 *            λͼ
	 * @param releaseBitmap
	 *            �Ƿ��ͷ�λͼ
	 */
	public static final C2D_Image createImage(C2D_GdiImage image, boolean releaseBitmap)
	{
		C2D_Image newInstance = new C2D_Image();
		newInstance.setImage(image, releaseBitmap);
		return newInstance;
	}

	/**
	 * ���ݴ��ڵ�λͼ������ͼͼƬ����ָ����ͼ�������Ƿ�����λͼͼƬ��
	 * ע����ô˷���֮��C2D_GdiImage��ӵ�д�imageʵ�ʿ���Ȩ�����ⲿ�벻Ҫ�ٶ������в�����
	 * ��Ϊ�ڴ�����ͼ�����У����ܻ��ؽ���С��������ԭͼ������㴫���ԭͼ���ܻ��Ѿ������١�
	 * 
	 * @param image
	 *            ����������ͼ��λͼ
	 * @param releaseImage
	 *            �Ƿ��ͷ�λͼ
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
	 * ��ȡͼƬ��ֻ��ѡ������ʽ��������ͼ�Ż�ӵ��δ�����ٵ�ͼƬ
	 * 
	 * @return λͼͼƬ
	 */
	public C2D_GdiImage getImage()
	{
		return mImage;
	}

	/**
	 * ��ͼ���
	 */
	public int textureWidth()
	{
		return mTextureSize.m_width;
	}

	/**
	 * ��ͼ�߶�
	 */
	public int textureHight()
	{
		return mTextureSize.m_height;
	}

	/**
	 * λͼԭʼ���
	 */
	public int bitmapWidth()
	{
		return mBitmapSize.m_width;
	}

	/**
	 * λͼԭʼ�߶�
	 */
	public int bitmapHeight()
	{
		return mBitmapSize.m_height;
	}

	/**
	 * λͼ�����ź�ƥ��OpenGL��ʽ֮�����ɵ�λͼ��ʵ����Ч���صĿ��
	 */
	public int pixelWidth()
	{
		return mPixelSize.m_width;
	}

	/**
	 * λͼ�����ź�ƥ��OpenGL��ʽ֮�����ɵ�λͼ��ʵ����Ч���صĸ߶�
	 */
	public int pixelHeight()
	{
		return mPixelSize.m_height;
	}

	/**
	 * ��ȡλͼ��������С�Ĳ㼶
	 * @return λͼ��������С�Ĳ㼶
	 */
	public float getZoomOut()
	{
		return mZoomeOut;
	}
/**
	 * �����ڽ��㷨����ͼƬ
	 * 
	 * @param src
	 *            Դͼ���ػ���
	 * @param w
	 *            Դͼ���
	 * @param h
	 *            Դͼ�߶�
	 * @param dst
	 *            Ŀ��ͼ���ػ���
	 * @param newW
	 *            Ŀ��ͼ���
	 * @param newH
	 *            Ŀ��ͼ�߶�
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
	 * �������Բ�ֵ����ͼƬ.
	 * 
	 * @param src
	 *            Դͼ���ػ���
	 * @param w
	 *            Դͼ���
	 * @param h
	 *            Դͼ�߶�
	 * @param dst
	 *            Ŀ��ͼ���ػ���
	 * @param newW
	 *            Ŀ��ͼ���
	 * @param newH
	 *            Ŀ��ͼ�߶�
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
	 * ���Բ�ֵ����.
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
	 * ���°���ͼ����ʹ�ñ���λͼ��ʽ����C2D_GdiImageʱ�� �����ڻ�����������λͼ֮��ʹ�ô˺������°󶨵�OpenGL
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
	 * ����ͼƬ.
	 * 
	 * @param img
	 *            - ���������ŵ�Դͼ
	 * @param newW
	 *            - ��ͼ�Ŀ��
	 * @param newH
	 *            - ��ͼ�ĸ߶�
	 * @param fast
	 *            - �Ƿ���ټ���(���ټ�������ڽ��㷨������ʹ�����Բ�ֵ)
	 * @return �����´�����ͼƬ
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
	 * ��û��ƾ��.
	 * 
	 * @return ��ͼ����
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
	 * �ͷŴ�����ͼ��λͼ
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
	 * �ͷ���ͼ
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
	 * ������ͼ�������
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
	 * ������ͼ�������
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
	 * ��ʾ���е���ͼ
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
	 * ���ݱ�ͼƬ����һ����Ƭ���������ó�ָ��������ͳߴ�
	 * @param x ��������X
	 * @param y ��������Y
	 * @param w ������W
	 * @param h ����߶�H
	 * @return �²�������Ƭ
	 */
	public C2D_ImageClip makeClip(int x,int y,int w,int h)
	{
		C2D_ImageClip clip=new C2D_ImageClip(this);
		clip.setContentRect(x, y, w, h);
		return clip;
	}
	/**
	 * ��ӡ��Ϣ
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
	 * ��ӡ��Ϣ
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
	 * �ͷ�������Դ
	 */
	public void onRelease()
	{
		releaseTextureImage();
		releaseBitmap();

	}
}
