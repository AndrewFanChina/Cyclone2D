package c2d.frame.com.view;

import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_Event_ActionEnd;
import c2d.mod.C2D_FrameManager;
import c2d.mod.map.scroll.C2D_SpritesStore;
import c2d.mod.sprite.C2D_Sprite;

public class C2D_SpriteView extends C2D_View
{
	protected C2D_SpritesStore m_spritesStore;
	protected C2D_FrameManager m_frameManager;
	protected int m_duration = 45;

	public C2D_SpriteView(C2D_FrameManager fm)
	{
		m_frameManager = fm;
	}

	/**
	 * ���Ĭ�ϵĲ�����ʱ
	 * 
	 * @return Ĭ�ϵĲ�����ʱ
	 */
	public int getDuration()
	{
		return m_duration;
	}

	/**
	 * ����Ĭ�ϵĲ�����ʱ
	 * 
	 * @param duration
	 *            Ĭ�ϵĲ�����ʱ
	 */
	public void setDuration(int duration)
	{
		this.m_duration = duration;
	}

	/**
	 * Ĭ�ϵĲ�������¼����������֮�������أ�����ͼ�Ƴ��� ��store�еĵ�ǰջת���ݴ�ջ
	 */
	protected C2D_Event_ActionEnd m_EventActionEndDef = new C2D_Event_ActionEnd()
	{
		protected boolean doEvent(C2D_Widget sprite)
		{
			if (sprite == null || !(sprite instanceof C2D_Sprite))
			{
				return false;
			}
			recycleSprite((C2D_Sprite) sprite);
			return false;
		}
	};

	/**
	 * ���յ�Ԫ
	 * 
	 * @param widget
	 */
	public void recycleSprite(C2D_Sprite sp)
	{
		if (m_spritesStore != null)
		{
			m_spritesStore.recycleSprite(sp);
		}
	}

	/**
	 * ��ָ��������x,y���������z������һ�����鶯���� ���鶯�����ļ���ID��folderID������ID��spriteID��
	 * ��ʼ������0��������ʱ��Ĭ����ֵ �������ִ��Ĭ���¼������أ�����״�ṹ�Ƴ�����store��ת���ݴ棩��
	 * 
	 * @param depend
	 *            �����Ķ���
	 * @param x
	 *            ָ��X����
	 * @param y
	 *            ָ��Y����
	 * @param z
	 *            ָ��Z����
	 * @param folderID
	 *            �ļ���ID
	 * @param spriteID
	 *            ����ID
	 * @return ���ŵľ���
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID)
	{
		return playSprite(depend, x, y, z, folderID, spriteID, 0, m_duration);
	}

	/**
	 * ��ָ��������x,y���������z������һ�����鶯���� ���鶯�����ļ���ID��folderID������ID��spriteID��
	 * ��ʼ������actionID��������ʱ��Ĭ����ֵ �������ִ��Ĭ���¼������أ�����״�ṹ�Ƴ�����store��ת���ݴ棩��
	 * 
	 * @param depend
	 *            �����Ķ���
	 * @param x
	 *            ָ��X����
	 * @param y
	 *            ָ��Y����
	 * @param z
	 *            ָ��Z����
	 * @param folderID
	 *            �ļ���ID
	 * @param spriteID
	 *            ����ID
	 * @param actionID
	 *            ���鶯��ID
	 * @return ���ŵľ���
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID, int actionID)
	{
		return playSprite(depend, x, y, z, folderID, spriteID, actionID, m_duration);
	}

	/**
	 * ��ָ��������x,y���������z������һ�����鶯���� ���鶯�����ļ���ID��folderID������ID��spriteID��
	 * ��ʼ������actionID��������ʱ��duration�� �������ִ���¼�event��
	 * 
	 * @param depend
	 *            �����Ķ���
	 * @param x
	 *            ָ��X����
	 * @param y
	 *            ָ��Y����
	 * @param z
	 *            ָ��Z����
	 * @param folderID
	 *            �ļ���ID
	 * @param spriteID
	 *            ����ID
	 * @param actionID
	 *            ���鶯��ID
	 * @param duration
	 *            ����������ʱ�����룩
	 * @return ���ŵľ���
	 */
	public C2D_Sprite playSprite(C2D_Sprite depend, int x, int y, int z, short folderID, short spriteID, int actionID, int duration)
	{
		if (m_frameManager == null)
		{
			return null;
		}
		if (m_spritesStore == null)
		{
			m_spritesStore = new C2D_SpritesStore();
		}
		C2D_Sprite sprite = (C2D_Sprite) m_spritesStore.recover(spriteID);
		if (sprite == null)
		{
			sprite = onCreateSprite(m_frameManager, folderID, spriteID, depend);
			if (sprite != null)
			{
				m_spritesStore.addSprite(sprite);
			}
		}
		if (sprite != null)
		{
			sprite.clearEvents();
			sprite.clearMotions();
			sprite.setAutoPlay(C2D_Sprite.AUTOPLAY_FRAME);
			sprite.setDuration(duration);
			sprite.setAction(actionID);
			sprite.setVisible(true);
			sprite.setPosTo(x, y);
			sprite.setFrame(0);
			this.addChild(sprite, z);
			sprite.init();
		}
		return sprite;
	}

	/**
	 * ���þ���ִ���
	 * 
	 * @param spritesStore
	 */
	public void setSpritesStore(C2D_SpritesStore spritesStore)
	{
		if (m_spritesStore != null)
		{
			m_spritesStore.doRelease(this);
			m_spritesStore = null;
		}
		m_spritesStore = spritesStore;
	}

	/**
	 * Ĭ�ϵĹ���sprite��ʽ���������Ҫ��ӵ�����չ�ľ�����󣬿��Ը����������
	 * 
	 * @param fm
	 *            C2D��ܹ�����
	 * @param folderID
	 *            �ļ���ID
	 * @param spriteID
	 *            ����ID
	 * @param depend
	 *            �����Ķ���
	 * @return �����õľ������
	 */
	protected C2D_Sprite onCreateSprite(C2D_FrameManager fm, short folderID, short spriteID, C2D_Sprite depend)
	{
		C2D_Sprite sprite = null;
		sprite = new C2D_Sprite(fm, folderID, spriteID);
		return sprite;
	}

	/**
	 * �Ƴ������������еľ��飬ȫ���洢����������
	 */
	public void storeAll()
	{
		int size = getChildCount();
		for (int i = 0; i < size; i++)
		{
			C2D_Widget son = (C2D_Widget) m_nodeList.elementAt(i);
			if (son != null && son instanceof C2D_Sprite)
			{
				C2D_Sprite sp = (C2D_Sprite) son;
				recycleSprite(sp);
				i--;
			}
		}
	}

	/**
	 * ж����Դ
	 */
	public void onRelease()
	{
		super.onRelease();
		if (m_spritesStore != null)
		{
			m_spritesStore.doRelease(this);
			m_spritesStore = null;
		}
		m_frameManager = null;
	}
}
