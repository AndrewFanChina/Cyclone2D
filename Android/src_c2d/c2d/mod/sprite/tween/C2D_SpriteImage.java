package c2d.mod.sprite.tween;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_GdiImage;
import c2d.plat.gfx.C2D_Image;


public class C2D_SpriteImage extends C2D_Image
{
	/** ����ͼƬ���ļ���׺����Ĭ��ʹ��png�����ʹ�ñ�ĸ�ʽ���ڶ�ȡǰ��Ҫ�޸Ĵ˱��� */
	public static String SpriteImagePostfix =  C2D_Consts.STR_IMG_PNG;
	/** �����ļ������� **/
	private String m_folderName;
	/** Դͼ����. */
	private String m_sptImgName;
	
	/** ��ʹ�õĴ��������н�ɫ����ʱ���˴��������ӣ����˴�����0ʱ�����Ա�ж��. */
	private short usedTime = 0;
	
	/**
	 * ���캯�����������й����SpriteImage��
	 * ���Զ���ȡͼƬ���ƺ�ͼƬ��ԭʼ�ߴ磬
	 * ��ʱ���������ͼ
	 * @param dis ������
	 * @param folderName �ļ�������
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
			//FIXME ������Ҫ��С��ͼƬ���߷�GL��׼�ߴ�ͼƬ����ʱ�������BUG
			mTextureSize.setValue(mBitmapSize);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * ����һ��ʹ�ô���
	 */
	void useImage()
	{
		usedTime++;
	}
	/**
	 * ����ͼƬ
	 *
	 */
	public void loadImage()
	{
		//������ԭʼ��ͼƬ
		C2D_GdiImage imageP = C2D_GdiImage.createImage(m_sptImgName+SpriteImagePostfix,  m_folderName);
		setImage(imageP,true);
	}
	/**
	 * ����һ��ʹ�ô��������ʹ�ô���<=0����ִ��ж��
	 *
	 * @return �����Ƿ�ж��
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
	 * �ͷ���Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		m_folderName=null;
		m_sptImgName = null;
	}
	
	/**
	 * ��ѯ�Ƿ��Ѿ�������
	 * @return �Ƿ񱻼���
	 */
	public boolean isLoaded()
	{
		return mTexuteID!=0;
	}

}
