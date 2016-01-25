package c2d.lang.util;

import java.util.Calendar;
import java.util.Date;

import c2d.lang.math.C2D_Math;

/**
 * ϵͳ�࣬���ڸ�ϵͳ�ڴ棬ϵͳʱ�䣬�̵߳���صĹ������
 * @author AndrewFan
 *
 */
public class C2D_System
{

	/**
	 * ��ȡʣ���ڴ�
	 * 
	 * @return ʣ���ڴ�
	 */
	public static int getMemLeft()
	{
		return (int) (Runtime.getRuntime().freeMemory() / 1024);
	}
	/**
	 * ��õ�ǰ������ʽ������ʱ��
	 * @return ��ǰʱ��
	 */
	public static long getTimeNow()
	{
		long time=System.currentTimeMillis();
		return time/1024;
	}

	/**
	 * ������"2013-01-26"�ĸ�ʽ������ڶ���
	 * 
	 * @param s
	 * @return
	 */
	public static Date getDate(String s)
	{
		if (s == null)
		{
			return null;
		}
		String data[] = C2D_Misc.splitString(s, '-');
		if (data.length < 3)
		{
			return null;
		}
		Calendar c = Calendar.getInstance();
		try
		{
			c.set(Calendar.YEAR, Integer.parseInt(data[0].trim()));
			c.set(Calendar.MONTH, Integer.parseInt(data[1].trim()) - 1);
			c.set(Calendar.DATE, Integer.parseInt(data[2].trim()));
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
		}
		catch (Exception e)
		{
			return null;
		}
		return c.getTime();
	}

	/**
	 * ������"2013-01-26 12:20:02"�ĸ�ʽ�������ʱ�����
	 * 
	 * @param s
	 * @return
	 */
	public static Date getDateAndTime(String s)
	{
		if (s == null)
		{
			return null;
		}
		String dataF[] = C2D_Misc.splitString(s, ' ');
		if (dataF == null || (dataF != null && dataF.length < 2))
		{
			return null;
		}
		String[] data1 = null;
		String[] data2 = null;
		data1 = C2D_Misc.splitString(dataF[0], '-');
		data2 = C2D_Misc.splitString(dataF[1], ':');
		if (data1 == null || data2 == null || (data1 != null && data1.length < 3)
				|| (data2 != null && data2.length < 3))
		{
			return null;
		}
		Calendar c = Calendar.getInstance();
		try
		{
			c.set(Calendar.YEAR, Integer.parseInt(data1[0].trim()));
			c.set(Calendar.MONTH, Integer.parseInt(data1[1].trim()) - 1);
			c.set(Calendar.DATE, Integer.parseInt(data1[2].trim()));
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data2[0].trim()));
			c.set(Calendar.MINUTE, Integer.parseInt(data2[1].trim()));
			c.set(Calendar.SECOND, Integer.parseInt(data2[2].trim()));
		}
		catch (Exception e)
		{
			return null;
		}
		return c.getTime();
	}

	/**
	 * ������"20130126122002"�ĸ�ʽ�������ʱ�����(2013��01��26��12ʱ20��02��)
	 * 
	 * @param s
	 * @return
	 */
	public static Date getTimeToDate(String s)
	{
		if (s == null || s.length() < 14)
		{
			return null;
		}

		String s_year = s.substring(0, 4);
		String s_month = s.substring(4, 6);
		String s_day = s.substring(6, 8);
		String s_hours = s.substring(8, 10);
		String s_minutes = s.substring(10, 12);
		String s_seconds = s.substring(12, 14);

		Calendar c = Calendar.getInstance();
		try
		{
			c.set(Calendar.YEAR, Integer.parseInt(s_year.trim()));
			c.set(Calendar.MONTH, Integer.parseInt(s_month.trim()) - 1);
			c.set(Calendar.DATE, Integer.parseInt(s_day.trim()));
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(s_hours.trim()));
			c.set(Calendar.MINUTE, Integer.parseInt(s_minutes.trim()));
			c.set(Calendar.SECOND, Integer.parseInt(s_seconds.trim()));
		}
		catch (Exception e)
		{
			return null;
		}
		return c.getTime();
	}
	/**
	 * ������"123456"�ĸ�ʽ��UTC��׼ʱ�䣩�������ʱ�����
	 * 
	 * @param s
	 * @return
	 */
	public static Date getTimeToDate(long timeStampL)
	{
		if(timeStampL<0)
		{
			return null;
		}
		return new Date(timeStampL);
	}
	/**
	 * ������ת�����������֣�2013-9-1=>20130901
	 * 
	 * @param date
	 *            ����
	 * @return ��������
	 */
	public static int dateToDayNum(Date date)
	{
		if (date == null)
		{
			return -1;
		}
		Calendar c = Calendar.getInstance();
		int dayNum = -1;
		try
		{
			c.setTime(date);
			int year = c.get(Calendar.YEAR);
			int moth = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DATE);
			String all = "" + year;
			all += (moth < 10 ? "0" : "") + moth;
			all += (day < 10 ? "0" : "") + day;
			dayNum = C2D_Math.stringToInt(all);
		}
		catch (Exception e)
		{
		}
		return dayNum;
	}

//	/**
//	 * ��ȡϵͳ����
//	 * 
//	 * @param paramKey
//	 *            ��������
//	 * @return ��Ӧ�Ĳ���ֵ
//	 */
//	public static String getAppParam(String paramKey)
//	{
//		C2D_App app = C2D_App.getApp();
//		if (app == null || paramKey == null)
//		{
//			return null;
//		}
//		return app.getAppProperty(paramKey);
//	}

	public static final int timeArray[] = new int[3];

	/**
	 * ��ʱ��ת���ɡ��֡��롢���롿��������ʽ
	 * 
	 * @param time ��ת����ʱ��
	 * @param minisecondLen ��ʾ���볤�ȣ�Ĭ��Ϊ3λ����������1����2��ʾ�����Գ���
	 * @return the time to array
	 */
	public static final int[] getTimeToArray(long time, int minisecondLen)
	{
		if (time <= 0)
		{
			timeArray[0] = timeArray[1] = timeArray[2] = 0;
			return timeArray;
		}
		int minite, second, miniSecond;
		miniSecond = (int) (time % 1000);
		time -= miniSecond;
		time /= 1000;
		second = (int) (time % 60);
		time -= second;
		time /= 60;
		minite = (int) (time);
		timeArray[0] = minite;
		timeArray[1] = second;
		timeArray[2] = miniSecond;
		if (minisecondLen == 2)
		{
			timeArray[2] /= 10;
		}
		if (minisecondLen == 1)
		{
			timeArray[2] /= 100;
		}
		return timeArray;
	}
	/**
	 * �����߳�
	 * 
	 * @param time
	 *            ʱ��(ms)
	 */
	public static void sleepThread(int time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (Exception e)
		{
		}
	}
}
