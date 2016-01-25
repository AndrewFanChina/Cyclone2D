package c2d.mod.sprite;

import c2d.mod.C2D_FrameManager;
import c2d.mod.sprite.tween.C2D_SpriteProto;

public class C2D_Sprite extends C2D_SpriteProto
{
	/**
	 * ���ݽ�ɫ�ļ���ID�ͽ�ɫID����һ��Sprite�����Ҷ�ȡ��ɫ����Ҫ��Դ��
	 * 
	 * @param c2dManager C2DManager ��ɫ���������ڵ�C2DManager����
	 * @param folderID short ����Ľ�ɫ�ļ���ID������ͷ�ļ��еĳ�������
	 * @param spriteID short �����Sprite��ID������ͷ�ļ��еĳ�������
	 */
	public C2D_Sprite(C2D_FrameManager c2dManager, short folderID, short spriteID)
	{
		super(c2dManager,folderID, spriteID);
	}

	/**
	 * ���ݽ�ɫԭ���ļ���ID�ͽ�ɫԭ��ID����һ��Actor�����Ҷ�ȡ��ɫ����Ҫ��Դ��
	 * SpriteFolderID��SpriteID��ӽ�ɫԭ�����Զ���ȡ��
	 * 
	 * @param c2dManager ��ɫ���������ڵ�C2DManager����
	 * @param anteTypeID ����Ľ�ɫԭ���ļ���ID�ͽ�ɫԭ��˫��ֵ���飬����ͷ�ļ��еĳ������塣
	 */
	public C2D_Sprite(C2D_FrameManager c2dManager, short[] anteTypeID)
	{
		super(c2dManager, anteTypeID);
	}
	/**
	 * û�н����κ����õĹ��캯�����������д���m_C2DM�����õ�����
	 */
	protected C2D_Sprite()
	{
	}
	
	public C2D_Sprite init()
	{
		return this;
	}
}
