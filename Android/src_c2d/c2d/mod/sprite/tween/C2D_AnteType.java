package c2d.mod.sprite.tween;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;


/**
 * <p>
 * 标题:角色原型类
 * </p>
 * <p>
 * 描述:用于包装图片ID映射的一个类，含有被引用角色ID和一个图片映射表。<br>
 * 绘制角色时，如果角色的角色原型信息存在，则将根据角色原型的映射表获得<br>
 * 映射后的图片ID进行绘制。
 * </p>.
 */
public class C2D_AnteType extends C2D_Object
{
	/** 对应角色的文件夹ID. */
	public short actotorFolderID = 0;
	/** 对应角色的角色ID. */
	public short actotorID = 0;

	/**
	 * 角色原型构造函数，将从输入流中读取角色ID和映射信息.
	 *
	 * @param dis DataInputStream 构造输入流
	 */
	public C2D_AnteType(DataInputStream dis)
	{
		try
		{
			// 读取actorFolderID
			actotorFolderID = C2D_IOUtil.readShort(actotorFolderID, dis);
			// 读取actorID
			actotorID = C2D_IOUtil.readShort(actotorID, dis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 根据原始图片ID获取映射后的ID.
	 *
	 * @param id short 原始图片ID
	 * @return short 映射图片ID
	 */
	public short getmappedID(short id)
	{
		//..TODO
		return id;
	}

	/**
	 * 释放资源.
	 */
	public void onRelease()
	{
	}
}
