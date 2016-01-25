package pay.model.record;

import c2d.app.C2D_App;
import android.content.Context;


/**
 * ���ؼ�¼�࣬����¼д�뵽���أ�����Android��˵��д�������ļ��У�����J2me��˵��д��RMS��
 */
public class C2D_LocalRecord
{
	private static final String PrefixName = "rd_";
	private static final String SubfixName = ".bin";

	/**
	 * ������Ϸ��¼���ϣ�����Ϸ��¼����ת�����ַ������浽��������
	 * @param context ������
	 * @param record ��¼����
	 * @return �Ƿ�ɹ����棬ֻҪ��һ������ʧ�ܾͻ᷵��ʧ��
	 */
	public static boolean saveRecordSet(Context context,C2D_RecordSet record)
	{
		if (record == null)
		{
			return false;
		}
		boolean success = true;
		for (int i = 0; i < record.size(); i++)
		{
			C2D_RecordItem recordItem = record.getRecordByID(i);
			if (recordItem != null)
			{
				if (!saveRecord(context,recordItem))
				{
					success = false;
				}
			}
		}
		return success;
	}

	/**
	 * ������Ϸ��¼���ϣ����Զ���ȡ�Ѿ�����������Ϸ��¼���ϡ�
	 * @param context ������
	 * @return ��ȡ���ļ�¼����
	 */
	public static C2D_RecordSet loadRecordSet(Context context)
	{
		C2D_RecordSet rcordSet = new C2D_RecordSet();
		for (int i = 1; i <= 6; i++)
		{
			C2D_RecordItem record = new C2D_RecordItem(i);
			String urlI = PrefixName + i + SubfixName;
			String data = readLocal(context,urlI);
			if (data != null && record.serializeIn(data))
			{
				rcordSet.addRecord(record);
			}
		}
		return rcordSet;
	}

	/**
	 * ɾ����Ϸ��¼���ϣ���������Ϸ��¼�����е�ÿ����¼IDɾ����Ӧ�ļ�¼
	 * @param context
	 * 		    ������
	 * @return ��ɾ��������
	 */
	public static int removeRecordSet(Context context,C2D_RecordSet record)
	{
		if (record == null||context==null)
		{
			return 0;
		}
		int count = 0;
		for (int i = 0; i < record.size(); i++)
		{
			C2D_RecordItem recordItem = record.getRecordByID(i);
			if (recordItem != null)
			{
				if (removeRecord(context,recordItem))
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * ���浥����Ϸ��¼��
	 * @param context
	 * 			    ������
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ�����
	 */
	public static boolean saveRecord(Context context,C2D_RecordItem record)
	{
		if (record == null||context==null)
		{
			return false;
		}
		String urlI = PrefixName + record.getRecordID() + SubfixName;
		String data = record.serializeOut();
		return writeLocal(context,urlI, data);
	}
	/**
	 * ���浥����Ϸ��¼��
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ�����
	 */
	public static boolean saveRecord(C2D_RecordItem record)
	{
		return saveRecord(C2D_App.getApp(),record);
	}
	/**
	 * ���ص�����Ϸ��¼��������Ϸ��¼ID��ȡ��Ӧ�ļ�¼��
	 * @param context
	 * 			  ������
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ���ȡ
	 */
	public static boolean loadRecord(Context context,C2D_RecordItem record)
	{
		if (record == null)
		{
			return false;
		}
		String urlI = PrefixName + record.getRecordID() + SubfixName;
		String data = readLocal(context,urlI);
		if (data != null && record.serializeIn(data))
		{
			return true;
		}
		return false;
	}
	/**
	 * ���ص�����Ϸ��¼��������Ϸ��¼ID��ȡ��Ӧ�ļ�¼��
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ���ȡ
	 */
	public static boolean loadRecord(C2D_RecordItem record)
	{
		return loadRecord(C2D_App.getApp(),record);
	}
	/**
	 * ɾ��������Ϸ��¼��

	 * @param context
	 * 		  ������
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public static boolean removeRecord(Context context,C2D_RecordItem record)
	{
		if (record == null)
		{
			return false;
		}
		String urlI = PrefixName + record.getRecordID() + SubfixName;
		return removeLocal(context,urlI);
	}
	/**
	 * ɾ��������Ϸ��¼��
	 * @param record
	 *            �����¼
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public static boolean removeRecord(C2D_RecordItem record)
	{
		return removeRecord(C2D_App.getApp(),record);
	}
	/**
	 * ��ȡ��¼�����ؼ�¼�ַ���
	 * @param context
	 * 			  ������
	 * @param rName
	 *            ��¼����
	 * @return String ��ȡ���ļ�¼�ַ���
	 */
	private static final String readLocal(Context context,String rName)
	{
		if(context==null)
		{
			return null;
		}
		java.io.FileInputStream in = null;
		String strValue = null;
		try
		{
			byte byteData[] = null;
			in = context.openFileInput(rName);
			if (in != null)
			{
				int len = in.available();
				byteData = new byte[len];
				in.read(byteData);
			}
			if (byteData != null)
			{
				strValue = new String(byteData);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
					in = null;
				}
				catch (Exception e)
				{
				}
			}
		}
		return strValue;
	}

	/**
	 * ����¼д����¼������
	 * @param context
	 * 			  ������
	 * @param rName
	 *            ��¼����
	 * @param str
	 *            ��¼����
	 * @return �Ƿ�ɹ�д��
	 */
	public static final boolean writeLocal(Context context,String rName, String str)
	{
		if (str == null || str.length() == 0||context==null)
		{
			return false;
		}
		byte data[] = str.getBytes();
		java.io.FileOutputStream out = null;
		boolean success = false;
		try
		{
			out = context.openFileOutput(rName,  android.content.Context.MODE_PRIVATE);
			if (out != null)
			{
				out.write(data);
			}
			success = true;
		}
		catch (Exception ex)
		{
			// #debug
			ex.printStackTrace();
			// #enddebug
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
					out = null;
				}
				catch (Exception e)
				{
				}
			}
		}
		return success;
	}

	/**
	 * ����¼�Ƿ����
	 * @param context ������
	 * @param rName
	 *            ��¼����
	 * @return �����Ƿ����
	 */
	private static final boolean testLocal(Context context,String rName)
	{
		if(context==null)
		{
			return false;
		}
		int data = 0;
		try
		{
			java.io.FileInputStream in = context.openFileInput(rName);
			if (in != null)
			{
				data = in.available();
			}
		}
		catch (Exception e)
		{
			data=-1;
		}
		return data >= 0;
		
	}

	/**
	 * ɾ����¼
	 * 
	 * @param context
	 * 			  ������
	 * @param rName
	 *            ��¼����
	 * @return �Ƿ�ɹ�ɾ��
	 */
	private static final boolean removeLocal(Context context,String rName)
	{
		if(context==null)
		{
			return false;
		}
		try
		{
			if (!testLocal(context,rName))
			{
				return false;
			}
			context.deleteFile(rName);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
