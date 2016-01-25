package c2d.lang.app;

import java.util.Calendar;
import java.util.TimeZone;
/**
 * 设备相关类
 */
public class C2D_Device implements C2D_Keys
{
	
	private C2D_Device()
	{
	}

	/**
	 * 打开网页浏览器.
	 *
	 * @param url String 即将浏览的网址
	 */
	public static void openBrowser(String url)
	{
		if (C2D_App.getApp() == null)
		{
			return;
		}
		try
		{
			//...
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前日期 year+month+date形式.
	 *
	 * @return String
	 */
	public static String getDate()
	{
		TimeZone t = TimeZone.getDefault();// 获取时区 设置默认
		Calendar c = Calendar.getInstance(t);// 创建Calendar 对象
		String strYear;
		String strMonth;
		String strDate;
		strYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年时间并转为字符串型
		strMonth = String.valueOf(c.get(Calendar.MONTH) + 1); // 获取当前月时间并转为字符串型
		strDate = String.valueOf(c.get(Calendar.DATE)); // 获取当前日时间并转为字符串型
		if (c.get(Calendar.MONTH) + 1 < 10)
		{
			strMonth = "0" + (c.get(Calendar.MONTH) + 1);
		}
		if (c.get(Calendar.DATE) < 10)
		{
			strDate = "0" + c.get(Calendar.DATE);
		}
		// String time = c.get( Calendar.HOUR ) + ":" + c.get( Calendar.MINUTE ) + ":"+ c.get( Calendar.SECOND );//当前时间时 分 秒
		String time = strYear + strMonth + strDate;
		return time;
	}


	// =====================震动相关 =========================================
	/** 手机震动开关，如果为false，不允许震动，即让startShake调用执行无效. */
	public static boolean shakeAllowed = true;

	/** The last shake time delay. */
	private static long lastShakeTimeDelay = 0;// 上次震动时间延迟
	
	/** The last shake time begin. */
	private static long lastShakeTimeBegin = 0;// 上次震动开始时间

	/**
	 * 开启手机震动.
	 *
	 * @param milliseconds int 震动时长，单位毫秒
	 */
	public static final void startShake(final int milliseconds)
	{
		if (shakeAllowed && !inShaking())
		{
			try
			{
				lastShakeTimeBegin = System.currentTimeMillis();
				lastShakeTimeDelay = milliseconds;
				//...
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 停止手机震动.
	 */
	public static final void stopShake()
	{
		if (shakeAllowed)
		{
			try
			{
				//...
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查看当前手机是否正在震动.
	 *
	 * @return boolean
	 */
	private static boolean inShaking()
	{
		return System.currentTimeMillis() < (lastShakeTimeDelay + lastShakeTimeBegin);
	}



	/** 按键常量 */
	private static final int keys[] = 
	{ 
		-1, -4, -2, -3, -5, -6, -7, 35, 42, 48,
		49, 50, 51, 52, 53, 54, 55, 56, 57, 0,
		-8,-31,-32,-11, 999
	};
	
	/**
	 * 根据原始键值，获取虚拟键值(键位ID)
	 *
	 * @param keyIn the key in
	 * @return the virtual key
	 */
	public static final int getVirtualKey(int keyIn)
	{
		int keyOut = key_empty;
		for (int i = 0; i < keys.length; i++)
		{
			if (keys[i] == keyIn)
			{
				keyOut = i;
				break;
			}
		}
		return keyOut;
	}

	/**
	 * 设置原始键值
	 *
	 * @param keyID 键位ID
	 * @param keydata 原始键值
	 */
	public static final void setKeyValue(int keyID, int keydata)
	{
		if (keyID < 0 || keyID > keys.length)
		{
			return;
		}
		keys[keyID] = keydata;
	}

	/**
	 * 获取原始键值
	 *
	 * @param keyID 键位ID
	 * @return 键值
	 */
	public static final int getKeyValue(int keyID)
	{
		if (keyID < 0 || keyID > keys.length)
		{
			return 0;
		}
		return keys[keyID];
	}
}
