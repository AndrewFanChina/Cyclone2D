package c2d.frame.com.text;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.lang.obj.C2D_Object;

public class C2D_PtcList extends C2D_Object
{
	public String m_folder;
	private Hashtable<String, C2D_PTC> m_ptcList = new Hashtable<String, C2D_PTC>();

	public C2D_PtcList()
	{
		m_folder = "fnts/";
	}

	public C2D_PtcList(String folder)
	{
		m_folder = folder;
	}

	/**
	 * 获取需要的图片文本配置对象，这些资源位于你定义的文件夹目录下，未定义默认为"fnts/"
	 * 
	 * @param name
	 * @return
	 */
	public C2D_PTC_DynW getPTC(String name)
	{
		return getPTC(name, m_folder);
	}

	/**
	 * 获取需要的图片文本配置对象，这些资源位于你定义的文件夹目录下
	 * 
	 * @param name
	 *            配置文件名称
	 * @param folder
	 *            配置文件所在文件夹
	 * @return 获得的图片文本配置对象
	 */
	public C2D_PTC_DynW getPTC(String name, String folder)
	{
		String key = folder + name;
		Object obj = m_ptcList.get(key);
		if (obj != null && obj instanceof C2D_PTC_DynW)
		{
			return (C2D_PTC_DynW) obj;
		}
		else
		{
			delPTC(key);
			C2D_PTC_DynW ptc = new C2D_PTC_DynW();
			ptc.loadFromPTLib(name, folder);
			m_ptcList.put(key, ptc);
			ptc.transHadler(this);
			;
			return ptc;
		}
	}

	/**
	 * 卸载指定的图片文本配置对象，这些资源位于你定义的文件夹目录下，未定义默认为"fnts/"
	 * 
	 * @param name
	 *            配置文件名称
	 * @return 是否成功卸载
	 */
	public boolean delPTC(String name)
	{
		return delPTC(name, m_folder);
	}

	/**
	 * 卸载指定的图片文本配置对象，这些资源位于你定义的文件夹目录下
	 * 
	 * @param name
	 *            配置文件名称
	 * @param folder
	 *            配置文件所在文件夹
	 * @return 是否成功卸载
	 */
	public boolean delPTC(String name, String folder)
	{
		if (name == null || folder == null)
		{
			return false;
		}
		String key = folder + name;
		Object obj = m_ptcList.get(key);
		if (obj != null)
		{
			m_ptcList.remove(name);
			if (obj instanceof C2D_PTC_DynW)
			{
				C2D_PTC_DynW ptc = (C2D_PTC_DynW) obj;
				ptc.doRelease();
				return true;
			}
		}
		return false;
	}

	/**
	 * 卸载所有资源
	 */
	public void clear()
	{
		Enumeration<C2D_PTC> en = m_ptcList.elements();
		while (en.hasMoreElements())
		{
			Object obj = en.nextElement();
			if (obj != null && obj instanceof C2D_PTC_DynW)
			{
				C2D_PTC_DynW ptc = (C2D_PTC_DynW) obj;
				ptc.doRelease(this);
			}
		}
		m_ptcList.clear();
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		clear();
	}
}
