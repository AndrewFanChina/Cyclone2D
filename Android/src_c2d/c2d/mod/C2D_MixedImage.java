package c2d.mod;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_GdiImage;


/**
 * ���ͼƬ��
 * @author AndrewFan
 */
public class C2D_MixedImage extends C2D_Object
{
	
	/** ͼƬ���ļ���׺����Ĭ��ʹ��png�����ʹ�ñ�ĸ�ʽ���ڶ�ȡǰ��Ҫ�޸Ĵ˱��� */
	public static String SpriteImagePostfix =  C2D_Consts.STR_IMG_PNG;
	/** �����ļ������� **/
	private String m_folderName;
	/** Դͼ����. */
	private String m_name; 
	/** ���. */
	private int width;
	
	/** �߶�. */
	private int height;
	
	/** �ڲ�ͼƬ. */
	private C2D_GdiImage m_image;

	/** ��ʹ�õĴ��������н�ɫ����ʱ���˴��������ӣ����˴�����0ʱ�����Ա�ж��. */
	private short usedTime = 0;
	/**
	 * �����г�ʼ��ͼƬ
	 * @param das
	 */
	public C2D_MixedImage(DataInputStream das,String folderName)
	{
		try
		{
			m_folderName=folderName;
			m_name = C2D_IOUtil.readString(m_name, das);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * ����ʹ�ô���
	 */
	public void useImage()
	{
		usedTime++;
	}
	/**
	 * �����ɫͼƬ��Դ
	 *
	 */
	public void loadImage()
	{
		loadImage(null);
	}
	/**
	 * �����ɫͼƬ��Դ
	 * C2D_Image imageP �Ӵ��ڵ�ͼƬ���أ����Ϊ�գ�������������Ϣ���¼���
	 */
	public void loadImage(C2D_GdiImage imageP)
	{
		//��ж��ͼƬ
		releaseImage();
		if (m_image == null)
		{
			C2D_GdiImage img = C2D_GdiImage.createImage(m_name, m_folderName);
			setImage(img);
		}
	}

	/**
	 * ����һ��ʹ�ô��������ʹ�ô���<=0����ִ��ж��
	 *
	 * @return �����Ƿ�ж��
	 */
	public boolean unuseImage()
	{
		if (usedTime > 0)
		{
			usedTime--;
		}
		if (usedTime <= 0)
		{
			releaseImage();
			return true;
		}
		return false;
	}
	/**
	 * ж��ͼƬ
	 */
	private void releaseImage()
	{
		if(m_image!=null)
		{
			m_image.doRelease(this);
			m_image = null;
		}
	}
	/**
	 * ���ͼƬ�Ƿ��Ѿ����غ�
	 * @return ͼƬ�Ƿ��Ѿ����غ�
	 */
	public boolean isLoaded()
	{
		return m_image!=null;
	}

	/**
	 * ��ȡͼƬ
	 *
	 * @return the image
	 */
	public C2D_GdiImage getImage()
	{
		return m_image;
	}
	/**
	 *����ͼƬ
	 *
	 * @param imageP the new image
	 */
	private void setImage(C2D_GdiImage imageP)
	{
		if(m_image!=null&&!m_image.equals(imageP))
		{
			m_image.doRelease(this);
			m_image = null;
		}
		if (imageP != null)
		{
			m_image = imageP;
			width = imageP.getWidth();
			height = imageP.getHeight();
		}
		else
		{
			width = 0;
			height = 0;
		}
	}
	/**
	 * ��ȡ���
	 *
	 * @return ͼƬ���
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 * ��ȡ�߶�
	 *
	 * @return ͼƬ�߶�
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 * ��ȡ����
	 *
	 * @return ����
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * �滻ͼƬ
	 * @param image ��Ҫ�滻��ͼƬ
	 */
	public void replaceImage(C2D_GdiImage image)
	{
		loadImage(image);
	}


	@Override
	public void onRelease()
	{
		m_folderName=null;
		m_name = null;
		releaseImage();
	}
}
