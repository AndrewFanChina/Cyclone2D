package c2d.mod;

import c2d.lang.obj.C2D_Object;
import c2d.mod.prop.C2D_PropManager;
import c2d.mod.script.C2D_ScriptManager;
import c2d.mod.sprite.C2D_TextManager;
import c2d.mod.sprite.tween.C2D_SpriteManager;

/**
 * <p>
 * 标题:C2D框架结构管理器
 * </p>
 * <p>
 * 描述:C2D框架结构的入口类，它拥有若干独立部分管理器，包括动画、文本、脚本、属性的管理器，以及资源加载器。<br>
 * 这个入口类将从一个框架资源文件中加载所需的结构信息，例如导出的时候命名为scene.bin,那么它就是这个资源启<br>
 * 动文件，C2D结构对应的所有二进制数据放在c2d目录下，图片文件放在imgs_scene目录下，声音资源放在<br>
 * sounds目录下。
 * </p>
 * @author AndrewFan
 */
public class C2D_FrameManager extends C2D_Object
{
	
	/** 动画管理器，负责C2D结构的动画部分管理. */
	public C2D_SpriteManager m_AniM;
	
	/** 文本管理器，负责C2D结构的文本部分管理. */
	public C2D_TextManager m_TxtM;
	
	/** 脚本管理器，负责脚本管理和运行. */
	public C2D_ScriptManager m_SptM;
	
	/** 属性管理器，负责C2D结构的属性部分管理. */
	public C2D_PropManager m_PropM;
	
	/** C2D资源名称. */
	public String resName;
	
	/** 图片是否被混淆. */
	public boolean imgConfused = false;
	
	

	/**
	 * 根据资源名称创建C2D管理器，资源加载器会被自动创建，并且使用C2D管理器的资源名称作为加载目录.
	 *
	 * @param resNameT String C2D框架资源文件名称
	 * @param readAllTxt boolean 是否在文本管理器在初始化时，一次性读取所有文本。详情见TextsManager
	 */
	public C2D_FrameManager(String resNameT, boolean readAllTxt)
	{
		resName = resNameT.substring(0, resNameT.lastIndexOf('.'));
		m_AniM = new C2D_SpriteManager(this);
		m_TxtM = new C2D_TextManager(this, readAllTxt);
		m_PropM = new C2D_PropManager(this);
		m_SptM = new C2D_ScriptManager(this);
	}

	/**
	 * 返回数据是否被混淆(目前C2D结构中的数据混淆仅仅是指图片混淆).
	 *
	 * @return boolean 混淆标记
	 */
	public boolean isDataConfused()
	{
		return imgConfused;
	}

	/**
	 * 获得C2D资源名称.
	 *
	 * @return String C2D资源名称
	 */
	public String getResName()
	{
		return resName;
	}

	/**
	 * 释放资源，C2DManager中的所有数据被释放，所有的管理器中的资源也都将被释放，.
	 */
	@Override
	public void onRelease()
	{
		if(m_AniM!=null)
		{
			m_AniM.doRelease(this);
			m_AniM=null;
		}
		if(m_TxtM!=null)
		{
			m_TxtM.doRelease(this);
			m_TxtM=null;
		}
		if(m_SptM!=null)
		{
			m_SptM.doRelease(this);
			m_SptM=null;
		}
		if(m_PropM!=null)
		{
			m_PropM.doRelease(this);
			m_PropM=null;
		}
	}
}
