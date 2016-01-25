package c2d.mod;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_GdiImage;


/**
 * 混合图片类
 * @author AndrewFan
 */
public class C2D_MixedImage extends C2D_Object
{
	
	/** 图片的文件后缀名，默认使用png，如果使用别的格式，在读取前需要修改此变量 */
	public static String SpriteImagePostfix =  C2D_Consts.STR_IMG_PNG;
	/** 所在文件夹名称 **/
	private String m_folderName;
	/** 源图名称. */
	private String m_name; 
	/** 宽度. */
	private int width;
	
	/** 高度. */
	private int height;
	
	/** 内部图片. */
	private C2D_GdiImage m_image;

	/** 被使用的次数，当有角色引用时，此次数会增加，当此次数是0时，可以被卸载. */
	private short usedTime = 0;
	/**
	 * 从流中初始化图片
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
	 * 增加使用次数
	 */
	public void useImage()
	{
		usedTime++;
	}
	/**
	 * 载入角色图片资源
	 *
	 */
	public void loadImage()
	{
		loadImage(null);
	}
	/**
	 * 载入角色图片资源
	 * C2D_Image imageP 从存在的图片加载，如果为空，则利用自身信息重新加载
	 */
	public void loadImage(C2D_GdiImage imageP)
	{
		//先卸载图片
		releaseImage();
		if (m_image == null)
		{
			C2D_GdiImage img = C2D_GdiImage.createImage(m_name, m_folderName);
			setImage(img);
		}
	}

	/**
	 * 减少一次使用次数，如果使用次数<=0将会执行卸载
	 *
	 * @return 返回是否被卸载
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
	 * 卸载图片
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
	 * 检查图片是否已经加载好
	 * @return 图片是否已经加载好
	 */
	public boolean isLoaded()
	{
		return m_image!=null;
	}

	/**
	 * 获取图片
	 *
	 * @return the image
	 */
	public C2D_GdiImage getImage()
	{
		return m_image;
	}
	/**
	 *设置图片
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
	 * 获取宽度
	 *
	 * @return 图片宽度
	 */
	public int getWidth()
	{
		return this.width;
	}

	/**
	 * 获取高度
	 *
	 * @return 图片高度
	 */
	public int getHeight()
	{
		return this.height;
	}

	/**
	 * 获取名称
	 *
	 * @return 名称
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * 替换图片
	 * @param image 需要替换的图片
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
