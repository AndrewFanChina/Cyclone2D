package c2d.plat.gfx;

import java.io.InputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.C2D_Consts;

/**
 * GDIͼƬ�࣬�ڲ�ʹ�ã������Զ�����ʽ����λͼ��ά��λͼռ�õ��ڴ档
 * 
 * C2D_GdiImage: GDIͼƬ�࣬�ڲ�ʹ�ã������Զ�����ʽ����λͼ��ά��λͼռ�õ��ڴ档
 * C2D_GProto: ԭʼ�����࣬�ڲ�ʹ�ã��ṩ����ڲ����ʵķ�װ����C2D_GdiGraphics�ĸ��ࡣ
 * C2D_GdiGraphics: GDI�����࣬�ڲ�ʹ�ã��������GDIͼƬ����޸ģ����ṩ�����ϸ�����ʽ�Ļ��ƹ��ܡ�
 * C2D_Image: ͼƬ�࣬�ṩ�û�ʹ�ã������װGDIͼƬ�࣬�ṩ���û�������ʽ�Ĵ����ӿڡ�
 * C2D_Graphics: �����࣬�ṩ�û�ʹ�ã�����C2D_Image�Զ�����ʽ���Ƶ���Ļ��
 */
public class C2D_GdiImage extends C2D_Object
{
	android.graphics.Bitmap imgInner = null;
	/** ���͸��ɫ����ȡBMPͼƬʱ�����͸��ɫ�����滻��͸��ɫ.һ�������ɫFF00FF��ΪBMP�����͸��ɫ */
	public static final int TransColor = 0xFFFF00FF;
	/** ��� */
	private int m_width;
	/** �߶� */
	private int m_height;

	C2D_GdiGraphics g = new C2D_GdiGraphics();
	/** ���� */
	private String name="unkown";;
	/**
	 * ���캯��.
	 */
	protected C2D_GdiImage()
	{
	}
	/**
	 * �������
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * ��������
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * ���ڴ洴��ָ���ߴ��ͼƬ��һ����������ͼ
	 * 
	 * @param width
	 *            ͼƬ���
	 * @param height
	 *            ͼƬ�߶�
	 * @return C2D_GdiImage ���ر�������ͼƬ
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
	 * ���ݶ��������ݴ���ָ��ͼƬ.
	 * 
	 * @param imageData
	 *            ����������
	 * @param imageOffset
	 *            ������ʼƫ��
	 * @param imageLength
	 *            ���ݳ���
	 * @return C2D_GdiImage ���ر�������ͼƬ
	 */
	public static C2D_GdiImage createImage(byte[] imageData, int imageOffset, int imageLength)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.BitmapFactory.decodeByteArray(imageData, imageOffset, imageLength);
		img.updateSize();
		return img;
	}

	/**
	 * ����RGB���ݴ���ָ���ߴ��ͼƬ.
	 * 
	 * @param rgb
	 *            RGB����
	 * @param width
	 *            ͼƬ���
	 * @param height
	 *            ͼƬ�߶�
	 * @return C2D_GdiImage ���ر�������ͼƬ
	 */
	public static C2D_GdiImage createRGBImage(int[] rgb, int width, int height)
	{
		C2D_GdiImage img = new C2D_GdiImage();
		img.imgInner = android.graphics.Bitmap.createBitmap(rgb, width, height, android.graphics.Bitmap.Config.ARGB_8888);
		img.updateSize();
		return img;
	}


	/**
	 * ֱ�Ӵ�Ӧ�ó�����д���ͼƬ��Ĭ���ļ���λ��imgs_ohter���Ƿ����ȡ���ڵ�ǰIMG_Confused
	 * 
	 * @param imageName
	 *            ͼƬ���ƣ�������׺��
	 * @return C2D_GdiImage ���ر�������ͼƬ
	 */
	public static final C2D_GdiImage createImage(String imageName)
	{
		return createImage(imageName, C2D_Consts.STR_OTHER_IMGS, IMG_Confused);
	}

	/**
	 * ֱ�Ӵ�Ӧ�ó�����д���ͼƬ��ָ��λ��res�µ����ļ������ƣ��Ƿ����ȡ���ڵ�ǰIMG_Confused
	 * 
	 * @param imageName
	 *            ͼƬ���ƣ�������׺��
	 * @param subFolder
	 *            res�����ļ�������(ͼƬĬ�ϴ����"res"����һ���ļ�����)
	 * @return C2D_GdiImage ���ر�������ͼƬ
	 */
	public static final C2D_GdiImage createImage(String imageName, String subFolder)
	{
		return createImage(imageName, subFolder, IMG_Confused);
	}

	/**
	 * ֱ�Ӵ�Ӧ�ó�����д���ͼƬ��ָ��λ��res�µ����ļ������ƣ��Ƿ����ȡ���ڵ�ǰIMG_Confused
	 * 
	 * @param imageName
	 *            ͼƬ����
	 * @param subFolder
	 *            res�����ļ�������(ͼƬĬ�ϴ����"res"����һ���ļ�����)
	 * @param confused
	 *            �Ƿ����
	 * @return C2D_GdiImage ���ر�������ͼƬ
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
	 * ����ͼƬ���ƺ͵�ɫ���ļ������ɫ��ͼƬ
	 * 
	 * @param folderName
	 *            �����ļ�������
	 * @param imageName
	 *            ԴͼƬ����
	 * @param strPmt
	 *            ��ɫ���ļ�����
	 * @return �����˵�ɫ���ͼƬ
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
	 * ����ͼƬ���ơ���͸�����ͼ���ơ���ɫ���ļ���ָ����͸��������ͼƬ ���ͼƬ������ִ�е�ɫ�廻ɫ������ͼƬAlpha��ϣ������ֵAplha���.
	 * 
	 * @param imgName
	 *            ԴͼƬ���ƣ�������׺��
	 * @param alphaImgName
	 *            ��͸�����ͼ���ƣ�������׺��
	 * @param pmtImgName
	 *            ��ɫ���ļ����ƣ�������׺��
	 * @param postfixName
	 *            ǰ��ͼƬ�ĺ�׺��
	 * @param alpha
	 *            ��Ҫ���ֵİ�͸����
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
	 * ����ͼƬ��ָ������ֵAplha��Ϣ���ػ�Ϻ��ͼƬ.
	 * 
	 * @param srcImg
	 *            ԴͼƬ
	 * @param alpha
	 *            ��Ҫ���ֵİ�͸����
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
	 * ���������ͼƬ
	 * 
	 * @param imageName
	 *            ����url��ͼƬ·�������ļ���׺��
	 * @return ���ɺõ�ͼƬ
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
	 * ���������ͼƬ
	 * 
	 * @param httpUrl
	 *            ������Դ�ļ���·��
	 * @param name
	 *            ��Դ����,����׺��
	 * @return ���ɺõ�ͼƬ
	 */
	public static final C2D_GdiImage createHttpImage(String httpUrl, String name)
	{
		C2D_GdiImage temp = createHttpImage(httpUrl + name);
		return temp;
	}
	
	/**
	 * ��ɢͼƬ�Ƿ񱻻�����Ĭ��Ϊfalse������ȡ��ͼƬ��Դ������ʱ����Ҫ��������Ϊtrue���ٽ��ж�ȡ������֮
	 */
	public static boolean IMG_Confused = false;
	/**
	 * ���³ߴ�
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
	 * ���ԭͼ���.һ���㲻��Ҫ���ô˷���
	 * 
	 * @return Image ԭͼ���
	 */
	public android.graphics.Bitmap getInner()
	{
		return imgInner;
	}

	/**
	 * ����ԭͼ���.һ���㲻��Ҫ���ô˷���
	 * 
	 */
	public void setInner(android.graphics.Bitmap inner)
	{
		imgInner = inner;
		updateSize();
		g.gInner = null;
	}

	/**
	 * ��û��ƾ��.
	 * 
	 * @return Graphics ���ƾ��
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
	 * ���ͼƬ���.
	 * 
	 * @return int ���
	 */
	public int getWidth()
	{
		return m_width;
	}

	/**
	 * ���ͼƬ�߶�.
	 * 
	 * @return int �߶�
	 */
	public int getHeight()
	{
		return m_height;
	}

	/**
	 * ��ȡͼƬָ���ü������RGB��ɫ������Ϣ.
	 * 
	 * @param pixes
	 *            ������ŵĻ���
	 * @param offset
	 *            �����д�ŵ���ʼλ��
	 * @param scanLen
	 *            ����ɨ�賤��
	 * @param x
	 *            �ü������x����
	 * @param y
	 *            �ü������y����
	 * @param width
	 *            �ü�����Ŀ��
	 * @param height
	 *            �ü�����ĸ߶�
	 */
	public void getRGB(int pixes[], int offset, int scanLen, int x, int y, int width, int height)
	{
		imgInner.getPixels(pixes, offset, scanLen, x, y, width, height);
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
	 * ����һ��ԭͼ�ļ���ͼ
	 * 
	 * @param source Դͼ 
	 * @param x �����Դͼ���Ͻǵ�x����
	 * @param y �����Դͼ���Ͻǵ�y����
	 * @param width Ŀ��ͼ�Ŀ��
	 * @param height Ŀ��ͼ�ĸ߶�
	 * @return ����ͼ
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
