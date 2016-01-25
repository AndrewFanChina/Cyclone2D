package c2d.mod.sprite;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_SortableArrayI;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;


/**
 * 文本管理器
 */
public class C2D_TextManager extends C2D_Object
{
	
	/** C2D管理器对象. */
	private C2D_FrameManager c2dManager;

	/**
	 * 文本管理器构造函数s
	 *
	 * @param c2dManagerT C2D管理器对象
	 * @param readAllTxt boolean 是否在初始化时，一次性读取所有文本
	 */
	public C2D_TextManager(C2D_FrameManager c2dManagerT, boolean readAllTxt)
	{
		c2dManager = c2dManagerT;
		loadTextsEntries(readAllTxt);
	}

	/** 文本容器. */
	private String all_texts[] = null;

	/**
	 * 初始化字段入口.
	 *
	 * @param readAllTxt boolean 是否一次性读取所有文本
	 */
	private void loadTextsEntries(boolean readAllTxt)
	{
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + C2D_Consts.STR_TXT;
		DataInputStream dIn = C2D_IOUtil.getDataInputStream(fileName);
		if (dIn != null)
		{
			try
			{
				if (dIn.available() > 0)
				{
					short count = 0;
					count = C2D_IOUtil.readShort(count, dIn); // 字段数目
					all_texts = new String[count];
					if (readAllTxt)
					{
						for (int i = 0; i < count; i++)
						{
							all_texts[i] = C2D_IOUtil.readString(all_texts[i], dIn);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (dIn != null)
					{
						dIn.close();
						dIn = null;
					}
				}
				catch (Exception ex)
				{
				}
			}
		}
	}

	/**
	 * 根据ID返回字符串，并可以设定是否存储在此容器中，存储的话，
	 * 下一次获取则不需要再次从外部文件读取，而是直接从内存获取。.
	 *
	 * @param id int 文本ID
	 * @param save boolean 是否存储
	 * @return String 返回获取的文本
	 */
	public String getString(int id, boolean save)
	{
		if (all_texts == null || id < 0 || id >= all_texts.length)
		{
			return null;
		}
		if (all_texts[id] != null)
		{
			return all_texts[id];
		}
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + C2D_Consts.STR_TXT;
		DataInputStream dIn = C2D_IOUtil.getDataInputStream(fileName);
		String text = null;
		if (dIn != null)
		{
			try
			{
				short count = 0;
				count = C2D_IOUtil.readShort(count, dIn); // 字段数目
				short skip = 0;
				for (int i = 0; i < count; i++)
				{
					if (id != i)
					{
						skip = C2D_IOUtil.readShort(skip, dIn);
						dIn.skip(skip);
					}
					else
					{
						text = C2D_IOUtil.readString(text, dIn);
						if (save)
						{
							all_texts[id] = text;
						}
						break;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (dIn != null)
					{
						dIn.close();
						dIn = null;
					}
				}
				catch (Exception ex)
				{
				}
			}
		}
		return text;
	}

	/**
	 * 根据ID容器读取一系列字符串并返回，传入的textIDs中含有字符ID，这些ID被设置成字符串后返回
	 * @param textIDs
	 * @return 字符串数组
	 */
	public String[] getStrings(C2D_SortableArrayI textIDs)
	{
		if (textIDs == null || all_texts == null || textIDs.size() == 0)
		{
			return null;
		}
		int idBuff[][] = new int[textIDs.size()][2];
		for (int i = 0; i < textIDs.size(); i++)
		{
			idBuff[i][0] = i;
			idBuff[i][1] =  textIDs.elementAt(i);
		}
		String texts[]=new String[textIDs.size()];
		c2d.lang.math.C2D_Math.orderArrayMin(idBuff, 1);
		int idBuffPoint = 0;
		String fileName = C2D_Consts.STR_RES + C2D_Consts.STR_C2D + c2dManager.resName + C2D_Consts.STR_TXT;
		DataInputStream dIn = C2D_IOUtil.getDataInputStream(fileName);
		String text = null;
		if (dIn != null)
		{
			try
			{
				short count = 0;
				count = C2D_IOUtil.readShort(count, dIn); // 字段数目
				short skip = 0;
				for (int i = 0; i < count; i++)
				{
					if (idBuff[idBuffPoint][1] != i)
					{
						skip = C2D_IOUtil.readShort(skip, dIn);
						dIn.skip(skip);
					}
					else
					{
						text = C2D_IOUtil.readString(text, dIn);
						while (true)
						{
							texts[idBuffPoint]=text;
							idBuffPoint++;
							if (idBuffPoint >= texts.length || idBuff[idBuffPoint][1] != i)
							{
								break;
							}
						}
						if (idBuffPoint >= texts.length)
						{
							break;
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (dIn != null)
					{
						dIn.close();
						dIn = null;
					}
				}
				catch (Exception ex)
				{
				}
			}
		}
		return texts;
	}

	/**
	 * 根据释放字符内容，但不释放字符入口.
	 */
	public void releaseContent()
	{
		if (all_texts != null)
		{
			for (int i = 0; i < all_texts.length; i++)
			{
				all_texts[i] = null;
			}
		}
	}

	/**
	 * 释放资源.
	 */
	public void onRelease()
	{
		releaseContent();
		all_texts = null;
		c2dManager=null;
	}
}
