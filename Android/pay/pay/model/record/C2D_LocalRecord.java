package pay.model.record;

import c2d.app.C2D_App;
import android.content.Context;


/**
 * 本地记录类，将记录写入到本地，对于Android来说，写到本地文件中，对于J2me来说，写到RMS中
 */
public class C2D_LocalRecord
{
	private static final String PrefixName = "rd_";
	private static final String SubfixName = ".bin";

	/**
	 * 保存游戏记录集合，将游戏记录集合转换成字符串保存到服务器。
	 * @param context 上下文
	 * @param record 记录集合
	 * @return 是否成功保存，只要有一个保存失败就会返回失败
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
	 * 加载游戏记录集合，将自动获取已经被创建的游戏记录集合。
	 * @param context 上下文
	 * @return 读取到的记录集合
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
	 * 删除游戏记录集合，将根据游戏记录集合中的每条记录ID删除响应的记录
	 * @param context
	 * 		    上下文
	 * @return 被删除的条数
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
	 * 保存单项游戏记录。
	 * @param context
	 * 			    上下文
	 * @param record
	 *            单项记录
	 * @return 是否成功保存
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
	 * 保存单项游戏记录。
	 * @param record
	 *            单项记录
	 * @return 是否成功保存
	 */
	public static boolean saveRecord(C2D_RecordItem record)
	{
		return saveRecord(C2D_App.getApp(),record);
	}
	/**
	 * 加载单项游戏记录，根据游戏记录ID获取相应的记录。
	 * @param context
	 * 			  上下文
	 * @param record
	 *            单项记录
	 * @return 是否成功获取
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
	 * 加载单项游戏记录，根据游戏记录ID获取相应的记录。
	 * @param record
	 *            单项记录
	 * @return 是否成功获取
	 */
	public static boolean loadRecord(C2D_RecordItem record)
	{
		return loadRecord(C2D_App.getApp(),record);
	}
	/**
	 * 删除单项游戏记录。

	 * @param context
	 * 		  上下文
	 * @param record
	 *            单项记录
	 * @return 是否成功删除
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
	 * 删除单项游戏记录。
	 * @param record
	 *            单项记录
	 * @return 是否成功删除
	 */
	public static boolean removeRecord(C2D_RecordItem record)
	{
		return removeRecord(C2D_App.getApp(),record);
	}
	/**
	 * 读取记录，返回记录字符串
	 * @param context
	 * 			  上下文
	 * @param rName
	 *            记录名称
	 * @return String 读取到的记录字符串
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
	 * 将记录写出记录到本地
	 * @param context
	 * 			  上下文
	 * @param rName
	 *            记录名称
	 * @param str
	 *            记录数据
	 * @return 是否成功写入
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
	 * 检查记录是否存在
	 * @param context 上下文
	 * @param rName
	 *            记录名称
	 * @return 返回是否存在
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
	 * 删除记录
	 * 
	 * @param context
	 * 			  上下文
	 * @param rName
	 *            记录名称
	 * @return 是否成功删除
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
