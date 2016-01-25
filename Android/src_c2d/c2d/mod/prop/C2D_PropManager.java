package c2d.mod.prop;

import java.io.DataInputStream;
import java.io.IOException;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;


/**
 * 属性管理器
 */
public class C2D_PropManager extends C2D_Object
{
	
	/** 属性分类. */
	private C2D_PropStyle propInstanceStyles[] = null;
	
	/** C2D管理器. */
	private C2D_FrameManager c2dManager;

	/**
	 * 属性管理器构造函数
	 *
	 * @param c2dManagerT the C2DManager
	 */
	public C2D_PropManager(C2D_FrameManager c2dManagerT)
	{
		c2dManager = c2dManagerT;
		init();
	}

	/**
	 *  初始化.
	 */
	private void init()
	{
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.getResName() + C2D_Consts.STR_PROP;
		DataInputStream dataIn = null;
		try
		{
			// 创建输入流
			dataIn = C2D_IOUtil.getDataInputStream(fileName);
			short len = 0;
			len = C2D_IOUtil.readShort(len, dataIn);
			propInstanceStyles = new C2D_PropStyle[len];
			// 注意读入的顺序，因为有相互引用的存在，所以必须先初始化所有对象的存在，再读取具体数据和相互之间的关系
			for (int i = 0; i < propInstanceStyles.length; i++)
			{
				propInstanceStyles[i] = new C2D_PropStyle(this);
				propInstanceStyles[i].readObjectInit(dataIn);
			}
			for (int i = 0; i < propInstanceStyles.length; i++)
			{
				propInstanceStyles[i].readObject(dataIn);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dataIn != null)
			{
				try
				{
					dataIn.close();
				}
				catch (IOException ex)
				{
				}
				dataIn = null;
			}
		}
	}

	/**
	 * 返回属性实例.
	 *
	 * @param styleID the style id
	 * @param instanceID the instance id
	 * @return the property instance
	 */
	public C2D_Prop getProp(int styleID, int instanceID)
	{
		if (styleID < 0 || propInstanceStyles==null || styleID >= propInstanceStyles.length)
		{
			return null;
		}
		C2D_PropStyle propertyStyle = propInstanceStyles[styleID];
		if (instanceID < 0 || instanceID >= propertyStyle.vPropInstances.size())
		{
			return null;
		}
		return (C2D_Prop) (propertyStyle.vPropInstances.elementAt(instanceID));
	}

	/**
	 * Gets the prop instances lens.
	 *
	 * @param styleID the style id
	 * @return the prop instances lens
	 */
	public int getPropInstancesLens(int styleID)
	{
		C2D_PropStyle propertyStyle = propInstanceStyles[styleID];
		return propertyStyle.vPropInstances.size();
	}

	/**
	 * PropertiesManager 卸载资源.
	 */
	public void onRelease()
	{
		if (propInstanceStyles != null)
		{
			for (int i = 0; i < propInstanceStyles.length; i++)
			{
				propInstanceStyles[i].doRelease();
				propInstanceStyles[i] = null;
			}
			propInstanceStyles = null;
		}
	}
}
