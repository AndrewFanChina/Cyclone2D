package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;

/**
 * ʱ����ӵ���ߣ�������һ��������Ԫ��Ҳ����������һ�ֿ�Ԫ����
 * 
 * @author Andrew
 */
public class C2D_TimeLineHolder extends C2D_Object
{
	public C2D_TimeLine[] sonList;
	protected C2D_SpriteModel parent;

	public C2D_TimeLineHolder(C2D_SpriteModel parentT)
	{
		parent = parentT;
	}

	/**
	 * ��ȡ���ʱ���᳤�ȣ�Ҳ�������ܳ�
	 * 
	 * @return ���ʱ���᳤��
	 */
	public int getMaxFrameCount()
	{
		int len = 0;
		for (int i = 0; i < sonList.length; i++)
		{
			C2D_TimeLine son = sonList[i];
			int lenI = son.getTimeSpan();
			if (lenI > len)
			{
				len = lenI;
			}
		}
		return len;
	}

	/**
	 * ����������ȡ����
	 * 
	 * @param dataIn
	 *            DataInputStream ������
	 * @throws Exception
	 */
	public void readObject(DataInputStream dataIn) throws Exception
	{
		short number = 0;
		number = C2D_IOUtil.readShort(number, dataIn);
		sonList = new C2D_TimeLine[number];
		for (int i = 0; i < number; i++)
		{
			C2D_TimeLine lement = new C2D_TimeLine(this);
			lement.readObject(dataIn);
			sonList[i] = lement;
			lement.transHadler(this);
		}
	}

	/**
	 * ��ȡ����Ԫ���
	 * 
	 * @return ����Ԫ���
	 */
	public C2D_SpriteModel GetParent()
	{
		return (C2D_SpriteModel) parent;
	}

	/**
	 * ��ȡ�ӵ�Ԫ
	 * 
	 * @return �ӵ�Ԫ
	 */
	public C2D_TimeLine GetSon(int id)
	{
		return sonList[id];
	}

	/**
	 * ��ȡ�ӵ�Ԫ��Ŀ
	 * 
	 * @return �ӵ�Ԫ��Ŀ
	 */
	public int Count()
	{
		return sonList.length;
	}

	/**
	 * �ͷ���Դ
	 */
	public void onRelease()
	{
		parent = null;
		for (int i = 0; i < sonList.length; i++)
		{
			sonList[i].doRelease(this);
			sonList[i] = null;
		}
		sonList = null;
	}
}