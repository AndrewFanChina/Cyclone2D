package c2d.mod.sprite.tween.model;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;

/**
 * 时间轴拥有者（可以是一个动作单元，也可以是任意一种库元件）
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
	 * 获取最大时间轴长度，也即最大的总长
	 * 
	 * @return 最大时间轴长度
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
	 * 从输入流读取数据
	 * 
	 * @param dataIn
	 *            DataInputStream 输入流
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
	 * 获取父单元句柄
	 * 
	 * @return 父单元句柄
	 */
	public C2D_SpriteModel GetParent()
	{
		return (C2D_SpriteModel) parent;
	}

	/**
	 * 获取子单元
	 * 
	 * @return 子单元
	 */
	public C2D_TimeLine GetSon(int id)
	{
		return sonList[id];
	}

	/**
	 * 获取子单元数目
	 * 
	 * @return 子单元数目
	 */
	public int Count()
	{
		return sonList.length;
	}

	/**
	 * 释放资源
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