package c2d.mod.map.tile;

import c2d.lang.obj.C2D_Object;
/**
 * 方格地图数据
 * @author AndrewFan
 *
 */
public class C2D_TileMapData extends C2D_Object
{

	/** posX. */
	public int posX = 0;

	/** posY. */
	public int posY = 0;

	/** NPCID. */
	public short npcID;

	/** 角色原型ID. */
	public int anteTypeID;

	/** 角色文件夹ID. */
	public int actorFolderID;

	/** 角色ID. */
	public int actorID;

	/** 动作ID. */
	public int actionID;

	/** 帧ID. */
	public int frameID;

	/** 可见状态. */
	public byte isVisible;

	/** 脚本ID. */
	public short m_scriptIDs[];

	/**
	 * Instantiates a new map actor data.
	 */
	public C2D_TileMapData()
	{
	}

	/**
	 * 返回排序权值，不起作用
	 */
	public int getZOrder()
	{
		return 0;
	}
	public void onRelease()
	{
		m_scriptIDs=null;
	}
}
