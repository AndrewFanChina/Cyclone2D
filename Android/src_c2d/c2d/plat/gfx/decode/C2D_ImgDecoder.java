package c2d.plat.gfx.decode;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.util.debug.C2D_Debug;
import c2d.plat.gfx.C2D_Image;

public class C2D_ImgDecoder implements JPEGDecoder.PixelArray
{
	private JPEGDecoder m_jpgDecoder = null;
	private int[] pix;
	private int width, height;

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		pix = new int[width * height];
	}

	public void setPixel(int x, int y, int argb)
	{
		pix[x + y * width] = argb;
	}

	public C2D_Image decodeJpeg(String filePath)
	{
		if(m_jpgDecoder==null)
		{
			m_jpgDecoder = new JPEGDecoder();	
		}
		C2D_Image img = null;
		DataInputStream dis = null;
		try
		{
			dis = C2D_IOUtil.getDataInputStream(filePath);
			if (dis != null)
			{
				m_jpgDecoder.decode(dis, this);
				img = C2D_Image.createRGBImage(pix, width, height, true);
				pix=null;
			}
		}
		catch (Exception e)
		{
			String se = "error in decoding " + filePath;
			if (e != null)
			{
				se += " msg:"+e.getMessage();
			}
			C2D_Debug.log(se);
			if (e != null)
			{
				e.printStackTrace();
			}
		}
		finally
		{
			if (dis != null)
			{
				try
				{
					dis.close();
				}
				catch (Exception e)
				{
				}
				dis = null;
			}
		}
		return img;
	}
}
