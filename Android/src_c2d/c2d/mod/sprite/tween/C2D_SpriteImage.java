package c2d.mod.sprite.tween;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_GdiImage;
import c2d.plat.gfx.C2D_Image;


public class C2D_SpriteImage extends C2D_Image
{
	/** 精灵图片的文件后缀名，默认使用png，如果使用别的格式，在读取前需要修改此变量 */
	public static String SpriteImagePostfix =  C2D_Consts.STR_IMG_PNG;
	/** 所在文件夹名称 **/
	private String m_folderName;
	/** 源图名称. */
	private String m_sptImgName;
	
	/** 被使用的次数，当有角色引用时，此次数会增加，当此次数是0时，可以被卸载. */
	private short usedTime = 0;
	
	/**
	 * 构造函数，从流进行构造的SpriteImage，
	 * 将自动读取图片名称和图片的原始尺寸，
	 * 此时不会加载贴图
	 * @param dis 输入流
	 * @param folderName 文件夹名称
	 */
	public C2D_SpriteImage(DataInputStream dis,String folderName)
	{
		m_folderName=folderName;
		m_sptImgName = C2D_IOUtil.readString(m_sptImgName, dis);
		try
		{
			short data=0;
			mBitmapSize.m_width=C2D_IOUtil.readShort(data, dis);
			mBitmapSize.m_height=C2D_IOUtil.readShort(data, dis);
			//FIXME 当是需要缩小的图片或者非GL标准尺寸图片加载时，会出现BUG
			mTextureSize.setValue(mBitmapSize);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * 增加一次使用次数
	 */
	void useImage()
	{
		usedTime++;
	}
	/**
	 * 载入图片
	 *
	 */
	public void loadImage()
	{
		//创建出原始的图片
		C2D_GdiImage imageP = C2D_GdiImage.createImage(m_sptImgName+SpriteImagePostfix,  m_folderName);
		setImage(imageP,true);
	}
	/**
	 * 减少一次使用次数，如果使用次数<=0将会执行卸载
	 *
	 * @return 返回是否被卸载
	 */
	boolean unuseImage()
	{
		if (usedTime > 0)
		{
			usedTime--;
		}
		if (usedTime <= 0)
		{
			if(mTexuteID!=0)
			{
				this.releaseTextureImage();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 释放资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_folderName=null;
		m_sptImgName = null;
	}
	
	/**
	 * 查询是否已经被加载
	 * @return 是否被加载
	 */
	public boolean isLoaded()
	{
		return mTexuteID!=0;
	}

}
