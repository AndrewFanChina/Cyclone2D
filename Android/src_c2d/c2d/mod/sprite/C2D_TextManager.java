package c2d.mod.sprite;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_SortableArrayI;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;
import c2d.mod.C2D_FrameManager;


/**
 * �ı�������
 */
public class C2D_TextManager extends C2D_Object
{
	
	/** C2D����������. */
	private C2D_FrameManager c2dManager;

	/**
	 * �ı����������캯��s
	 *
	 * @param c2dManagerT C2D����������
	 * @param readAllTxt boolean �Ƿ��ڳ�ʼ��ʱ��һ���Զ�ȡ�����ı�
	 */
	public C2D_TextManager(C2D_FrameManager c2dManagerT, boolean readAllTxt)
	{
		c2dManager = c2dManagerT;
		loadTextsEntries(readAllTxt);
	}

	/** �ı�����. */
	private String all_texts[] = null;

	/**
	 * ��ʼ���ֶ����.
	 *
	 * @param readAllTxt boolean �Ƿ�һ���Զ�ȡ�����ı�
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
					count = C2D_IOUtil.readShort(count, dIn); // �ֶ���Ŀ
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
	 * ����ID�����ַ������������趨�Ƿ�洢�ڴ������У��洢�Ļ���
	 * ��һ�λ�ȡ����Ҫ�ٴδ��ⲿ�ļ���ȡ������ֱ�Ӵ��ڴ��ȡ��.
	 *
	 * @param id int �ı�ID
	 * @param save boolean �Ƿ�洢
	 * @return String ���ػ�ȡ���ı�
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
				count = C2D_IOUtil.readShort(count, dIn); // �ֶ���Ŀ
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
	 * ����ID������ȡһϵ���ַ��������أ������textIDs�к����ַ�ID����ЩID�����ó��ַ����󷵻�
	 * @param textIDs
	 * @return �ַ�������
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
				count = C2D_IOUtil.readShort(count, dIn); // �ֶ���Ŀ
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
	 * �����ͷ��ַ����ݣ������ͷ��ַ����.
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
	 * �ͷ���Դ.
	 */
	public void onRelease()
	{
		releaseContent();
		all_texts = null;
		c2dManager=null;
	}
}
