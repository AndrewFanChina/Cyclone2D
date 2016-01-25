package c2d.plat.gfx;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.lang.obj.C2D_Object;

public class C2D_ImgList extends C2D_Object
{
	public String m_folder;
	private Hashtable ImgList=new Hashtable();
	public C2D_ImgList()
	{
		m_folder = "imgs_other/";
	}
	public C2D_ImgList(String folder)
	{
		m_folder = folder;
	}
	/**
	 * ��ȡ��Ҫ��ͼƬ��Դ����Щ��Դλ��folderĿ¼��
	 * @param name
	 * @return ��õ�ͼƬ����
	 */
	public C2D_Image getImage(String name)
	{
		String key=name;
		Object obj = ImgList.get(key);
		if(obj!=null&&obj instanceof C2D_Image)
		{
			return (C2D_Image)obj;
		}
		else
		{
			delImage(key);
			C2D_Image img = C2D_Image.createImage(name, m_folder);
			if(img!=null)
			{
				img.transHadler(this);
				ImgList.put(key, img);	
				return img;
			}
		}
		return null;
	}
	/**
	 * ��ȡ��Ҫ��ͼƬ��Դ����Щ��Դλ��folderĿ¼��
	 * @param name
	 * @return
	 */
	public C2D_Image getHttpImage(String name)
	{
		return getHttpImage(name,m_folder);
	}
	/**
	 * ��ȡ��Ҫ��ͼƬ��Դ����Щ��Դλ��folderĿ¼��
	 * @param name
	 * @param folder
	 * @return ��õ�ͼƬ����
	 */
	private C2D_Image getHttpImage(String name,String folder)
	{
		String key=folder+name;
		Object obj = ImgList.get(key);
		if(obj!=null&&obj instanceof C2D_Image)
		{
			return (C2D_Image)obj;
		}
		else
		{
			delImage(key);
			C2D_Image img = C2D_Image.createHttpImage(folder,name);
			if(img!=null)
			{
				img.transHadler(this);;
				ImgList.put(key, img);	
				return img;
			}
		}
		return null;
	}
	/**
	 * ж��ָ����ͼƬ��Դ����Щ��Դλ��folderĿ¼��
	 * @param name
	 * @return �Ƿ�ɹ�ж��
	 */
	public boolean delImage(String name)
	{
		if(name==null)
		{
			return false;
		}
		Object obj = ImgList.get(name);

		if(obj!=null)
		{
			ImgList.remove(name);
			if(obj instanceof C2D_Image)
			{
				C2D_Image img = (C2D_Image)obj;
				img.doRelease(this);
				return true;
			}
		}
		return false;
	}
	/**
	 * ж�����е�ͼƬ��Դ
	 */
	public void clear()
	{
		Enumeration en = ImgList.elements();
		while (en.hasMoreElements())
		{
			Object obj = en.nextElement();
			if(obj!=null&&obj instanceof C2D_Image)
			{
				C2D_Image img = (C2D_Image)obj;
				img.doRelease(this);
			}
		}
		ImgList.clear();
	}
	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		clear();
	}
	/**
	 * ������ɫ��ͼƬ
	 * @param imgName
	 * @param pmtName
	 * @return ��ɫ��ͼƬ
	 */
	public C2D_Image getPmtImage(String imgName, String pmtName)
	{
		String key=imgName+pmtName;
		Object obj = ImgList.get(key);
		if(obj!=null&&obj instanceof C2D_Image)
		{
			return (C2D_Image)obj;
		}
		else
		{
			delImage(key);
			C2D_Image img = C2D_Image.createPalleteImage(m_folder, imgName, pmtName);
			if(img!=null)
			{
				img.transHadler(this);;
				ImgList.put(key, img);
				return img;	
			}
		}
		return null;
	}
	
}
