package c2d.mod.map.scroll;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_StoreTable;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.sprite.C2D_Sprite;

public class C2D_SpritesStore extends C2D_Object
{
	/** 精灵存储表 */
	protected C2D_StoreTable m_spritesTable = new C2D_StoreTable();
	/** 当前存活的精灵 */
	protected int m_aliveCount = 0;

	/**
	 * 创建地图精灵管理器
	 * 
	 * @param atView
	 *            精灵所位于的视图容器
	 */
	public C2D_SpritesStore()
	{
		super();
	}

	/**
	 * 获得精灵列表
	 * 
	 * @return 精灵列表
	 */
	public C2D_Array getSprites()
	{
		if (m_spritesTable == null)
		{
			return null;
		}
		return m_spritesTable.getAliveList();
	}

	/**
	 * 获取当前容器中的精灵数目
	 * 
	 * @return 精灵数目
	 */
	public int getCount()
	{
		if (m_spritesTable == null)
		{
			return 0;
		}
		return m_spritesTable.size();
	}

	public void onRelease()
	{
		if (m_spritesTable != null)
		{
			m_spritesTable.recoverAll();
			int size = m_spritesTable.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Sprite m = (C2D_Sprite) m_spritesTable.elementAt(i);
				m.doRelease();
			}
			m_spritesTable.clearAll();
			m_spritesTable = null;
		}
	}

	/**
	 * 获取当前地图内存活的敌人数量
	 * 
	 * @return
	 */
	public int getAliveCount()
	{
		return m_aliveCount;
	}

	/**
	 * 回收精灵，优先以角色原型为ID进行回收，不存在角色原型则使用精灵ID
	 * 
	 * @param level
	 *            精灵等级
	 * @return 创建的精灵
	 */
	public void recycleSprite(C2D_Sprite sprite)
	{
		if (sprite != null)
		{
			sprite.removeFromTree();
			int id = sprite.getAntetypeID();
			if (id < 0)
			{
				id = sprite.getSpriteID();
			}
			if (m_spritesTable.store(sprite, id))
			{
				decAliveCount();
			}
		}
	}

	/**
	 * 增加一个当前地图内存活的精灵
	 * 
	 * @return 返回当前地图内存活的精灵
	 */
	public int incAliveCount()
	{
		m_aliveCount++;
		C2D_Debug.logDebug("m_aliveCount:"+m_aliveCount);
		return m_aliveCount;
	}

	/**
	 * 减少一个当前地图内存活的精灵
	 * 
	 * @return 返回当前地图内存活的精灵
	 */
	int decAliveCount()
	{
		if (m_aliveCount > 0)
		{
			m_aliveCount--;
			C2D_Debug.logDebug("m_aliveCount:"+m_aliveCount);
		}
		return m_aliveCount;
	}

	/**
	 * 从暂存区恢复一个精灵返回
	 * 
	 * @param idKey
	 *            角色原型ID或者精灵ID
	 * @return 精灵
	 */
	public C2D_Sprite recover(int idKey)
	{
		if (m_spritesTable == null)
		{
			return null;
		}
		return (C2D_Sprite) m_spritesTable.recoverElement(idKey);
	}

	/**
	 * 获取指定类型的活动精灵的数目
	 * 
	 * @param spriteID
	 *            精灵类型ID
	 * @return 活动精灵数目
	 */
	public int getCount(short spriteID)
	{
		if (m_spritesTable == null)
		{
			return 0;
		}
		return m_spritesTable.getSize(spriteID);
	}

	/**
	 * 获取指定类型的活动精灵的数目
	 * 
	 * @param antetypeID
	 *            角色原型ID
	 * @return 活动精灵数目
	 */
	public int getCountByAT(int antetypeID)
	{
		if (m_spritesTable == null)
		{
			return 0;
		}
		return m_spritesTable.getSize(antetypeID);
	}

	/**
	 * 获取仓储中的指定类型的精灵的数目
	 * 
	 * @param idKey
	 *            角色原型ID或者精灵类型ID
	 * @return 活动精灵数目
	 */
	public int getStoreCount(int idKey)
	{
		if (m_spritesTable == null)
		{
			return 0;
		}
		return m_spritesTable.getStoredSize(idKey);
	}


	/**
	 * 添加精灵，优先以角色原型ID作为Key，其次以精灵ID作为Key
	 * 
	 * @param sprite
	 */
	public void addSprite(C2D_Sprite sprite)
	{
		if (m_spritesTable != null && sprite != null)
		{
			int id=sprite.getAntetypeID();
			if(id<0)
			{
				id=sprite.getSpriteID();
			}
			m_spritesTable.addElement(sprite, id);
		}
	}

	/**
	 * 获取指定的活动精灵
	 * 
	 * @param id
	 *            精灵ID
	 * @return 精灵
	 */
	public C2D_Sprite elementAt(int id)
	{
		if (m_spritesTable == null)
		{
			return null;
		}
		return (C2D_Sprite) m_spritesTable.elementAt(id);
	}

	/**
	 * 获取已经存在的指定ID的物件
	 * 
	 * @param spriteID
	 *            精灵ID
	 * @return 物件
	 */
	public C2D_Sprite getExsitSprite(short spriteID)
	{
		int size = getCount();
		for (int i = 0; i < size; i++)
		{
			C2D_Sprite sprite = elementAt(i);
			if (sprite != null)
			{
				if (sprite.getSpriteID() == spriteID)
				{
					return sprite;
				}
			}
		}
		return null;
	}

	/**
	 * 获取已经存在的指定ID的物件
	 * 
	 * @param antetypeID
	 *            精灵ID
	 * @return 物件
	 */
	public C2D_Sprite getExsitSpriteByAT(int antetypeID)
	{
		int size = getCount();
		for (int i = 0; i < size; i++)
		{
			C2D_Sprite sprite = elementAt(i);
			if (sprite != null)
			{
				if (sprite.getAntetypeID() == antetypeID)
				{
					return sprite;
				}
			}
		}
		return null;
	}
}