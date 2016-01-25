package c2d.mod.map.scroll;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_StoreTable;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.debug.C2D_Debug;
import c2d.mod.sprite.C2D_Sprite;

public class C2D_SpritesStore extends C2D_Object
{
	/** ����洢�� */
	protected C2D_StoreTable m_spritesTable = new C2D_StoreTable();
	/** ��ǰ���ľ��� */
	protected int m_aliveCount = 0;

	/**
	 * ������ͼ���������
	 * 
	 * @param atView
	 *            ������λ�ڵ���ͼ����
	 */
	public C2D_SpritesStore()
	{
		super();
	}

	/**
	 * ��þ����б�
	 * 
	 * @return �����б�
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
	 * ��ȡ��ǰ�����еľ�����Ŀ
	 * 
	 * @return ������Ŀ
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
	 * ��ȡ��ǰ��ͼ�ڴ��ĵ�������
	 * 
	 * @return
	 */
	public int getAliveCount()
	{
		return m_aliveCount;
	}

	/**
	 * ���վ��飬�����Խ�ɫԭ��ΪID���л��գ������ڽ�ɫԭ����ʹ�þ���ID
	 * 
	 * @param level
	 *            ����ȼ�
	 * @return �����ľ���
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
	 * ����һ����ǰ��ͼ�ڴ��ľ���
	 * 
	 * @return ���ص�ǰ��ͼ�ڴ��ľ���
	 */
	public int incAliveCount()
	{
		m_aliveCount++;
		C2D_Debug.logDebug("m_aliveCount:"+m_aliveCount);
		return m_aliveCount;
	}

	/**
	 * ����һ����ǰ��ͼ�ڴ��ľ���
	 * 
	 * @return ���ص�ǰ��ͼ�ڴ��ľ���
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
	 * ���ݴ����ָ�һ�����鷵��
	 * 
	 * @param idKey
	 *            ��ɫԭ��ID���߾���ID
	 * @return ����
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
	 * ��ȡָ�����͵Ļ�������Ŀ
	 * 
	 * @param spriteID
	 *            ��������ID
	 * @return �������Ŀ
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
	 * ��ȡָ�����͵Ļ�������Ŀ
	 * 
	 * @param antetypeID
	 *            ��ɫԭ��ID
	 * @return �������Ŀ
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
	 * ��ȡ�ִ��е�ָ�����͵ľ������Ŀ
	 * 
	 * @param idKey
	 *            ��ɫԭ��ID���߾�������ID
	 * @return �������Ŀ
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
	 * ��Ӿ��飬�����Խ�ɫԭ��ID��ΪKey������Ծ���ID��ΪKey
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
	 * ��ȡָ���Ļ����
	 * 
	 * @param id
	 *            ����ID
	 * @return ����
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
	 * ��ȡ�Ѿ����ڵ�ָ��ID�����
	 * 
	 * @param spriteID
	 *            ����ID
	 * @return ���
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
	 * ��ȡ�Ѿ����ڵ�ָ��ID�����
	 * 
	 * @param antetypeID
	 *            ����ID
	 * @return ���
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