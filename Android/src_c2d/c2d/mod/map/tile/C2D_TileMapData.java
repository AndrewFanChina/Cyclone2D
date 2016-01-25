package c2d.mod.map.tile;

import c2d.lang.obj.C2D_Object;
/**
 * �����ͼ����
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

	/** ��ɫԭ��ID. */
	public int anteTypeID;

	/** ��ɫ�ļ���ID. */
	public int actorFolderID;

	/** ��ɫID. */
	public int actorID;

	/** ����ID. */
	public int actionID;

	/** ֡ID. */
	public int frameID;

	/** �ɼ�״̬. */
	public byte isVisible;

	/** �ű�ID. */
	public short m_scriptIDs[];

	/**
	 * Instantiates a new map actor data.
	 */
	public C2D_TileMapData()
	{
	}

	/**
	 * ��������Ȩֵ����������
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
