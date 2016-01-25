package c2d.mod.sprite;

import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.tween.C2D_SpriteProto;

public class C2D_Sprite extends C2D_SpriteProto
{
	/**
	 * 根据角色文件夹ID和角色ID创建一个Sprite，并且读取角色所需要资源。
	 * 
	 * @param c2dManager C2DManager 角色数据所存在的C2DManager对象
	 * @param folderID short 传入的角色文件夹ID，来自头文件中的常量定义
	 * @param spriteID short 传入此Sprite的ID，来自头文件中的常量定义
	 */
	public C2D_Sprite(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		super(c2dManager,folderID, spriteID);
	}

	/**
	 * 根据角色原型文件夹ID和角色原型ID创建一个Actor，并且读取角色所需要资源。
	 * SpriteFolderID和SpriteID会从角色原型中自动获取。
	 * 
	 * @param c2dManager 角色数据所存在的C2DManager对象
	 * @param anteTypeID 传入的角色原型文件夹ID和角色原型双数值数组，来自头文件中的常量定义。
	 */
	public C2D_Sprite(C2D_FrameManager c2dManager, short[] anteTypeID)
	{
		super(c2dManager, anteTypeID);
	}
	/**
	 * 没有进行任何设置的构造函数。你必须进行传入m_C2DM的引用等设置
	 */
	protected C2D_Sprite()
	{
	}
	
	public C2D_Sprite init()
	{
		return this;
	}
}
