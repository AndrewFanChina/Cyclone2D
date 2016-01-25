package c2d.mod.sprite.tween;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;


/**
 * <p>
 * ����:��ɫԭ����
 * </p>
 * <p>
 * ����:���ڰ�װͼƬIDӳ���һ���࣬���б����ý�ɫID��һ��ͼƬӳ���<br>
 * ���ƽ�ɫʱ�������ɫ�Ľ�ɫԭ����Ϣ���ڣ��򽫸��ݽ�ɫԭ�͵�ӳ�����<br>
 * ӳ����ͼƬID���л��ơ�
 * </p>.
 */
public class C2D_AnteType extends C2D_Object
{
	/** ��Ӧ��ɫ���ļ���ID. */
	public short actotorFolderID = 0;
	/** ��Ӧ��ɫ�Ľ�ɫID. */
	public short actotorID = 0;

	/**
	 * ��ɫԭ�͹��캯���������������ж�ȡ��ɫID��ӳ����Ϣ.
	 *
	 * @param dis DataInputStream ����������
	 */
	public C2D_AnteType(DataInputStream dis)
	{
		try
		{
			// ��ȡactorFolderID
			actotorFolderID = C2D_IOUtil.readShort(actotorFolderID, dis);
			// ��ȡactorID
			actotorID = C2D_IOUtil.readShort(actotorID, dis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ����ԭʼͼƬID��ȡӳ����ID.
	 *
	 * @param id short ԭʼͼƬID
	 * @return short ӳ��ͼƬID
	 */
	public short getmappedID(short id)
	{
		//..TODO
		return id;
	}

	/**
	 * �ͷ���Դ.
	 */
	public void onRelease()
	{
	}
}
