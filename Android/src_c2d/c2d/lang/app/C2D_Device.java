package c2d.lang.app;

import java.util.Calendar;
import java.util.TimeZone;
/**
 * �豸�����
 */
public class C2D_Device implements C2D_Keys
{
	
	private C2D_Device()
	{
	}

	/**
	 * ����ҳ�����.
	 *
	 * @param url String �����������ַ
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
	 * ��ȡ��ǰ���� year+month+date��ʽ.
	 *
	 * @return String
	 */
	public static String getDate()
	{
		TimeZone t = TimeZone.getDefault();// ��ȡʱ�� ����Ĭ��
		Calendar c = Calendar.getInstance(t);// ����Calendar ����
		String strYear;
		String strMonth;
		String strDate;
		strYear = String.valueOf(c.get(Calendar.YEAR)); // ��ȡ��ǰ��ʱ�䲢תΪ�ַ�����
		strMonth = String.valueOf(c.get(Calendar.MONTH) + 1); // ��ȡ��ǰ��ʱ�䲢תΪ�ַ�����
		strDate = String.valueOf(c.get(Calendar.DATE)); // ��ȡ��ǰ��ʱ�䲢תΪ�ַ�����
		if (c.get(Calendar.MONTH) + 1 < 10)
		{
			strMonth = "0" + (c.get(Calendar.MONTH) + 1);
		}
		if (c.get(Calendar.DATE) < 10)
		{
			strDate = "0" + c.get(Calendar.DATE);
		}
		// String time = c.get( Calendar.HOUR ) + ":" + c.get( Calendar.MINUTE ) + ":"+ c.get( Calendar.SECOND );//��ǰʱ��ʱ �� ��
		String time = strYear + strMonth + strDate;
		return time;
	}


	// =====================����� =========================================
	/** �ֻ��𶯿��أ����Ϊfalse���������𶯣�����startShake����ִ����Ч. */
	public static boolean shakeAllowed = true;

	/** The last shake time delay. */
	private static long lastShakeTimeDelay = 0;// �ϴ���ʱ���ӳ�
	
	/** The last shake time begin. */
	private static long lastShakeTimeBegin = 0;// �ϴ��𶯿�ʼʱ��

	/**
	 * �����ֻ���.
	 *
	 * @param milliseconds int ��ʱ������λ����
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
	 * ֹͣ�ֻ���.
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
	 * �鿴��ǰ�ֻ��Ƿ�������.
	 *
	 * @return boolean
	 */
	private static boolean inShaking()
	{
		return System.currentTimeMillis() < (lastShakeTimeDelay + lastShakeTimeBegin);
	}



	/** �������� */
	private static final int keys[] = 
	{ 
		-1, -4, -2, -3, -5, -6, -7, 35, 42, 48,
		49, 50, 51, 52, 53, 54, 55, 56, 57, 0,
		-8,-31,-32,-11, 999
	};
	
	/**
	 * ����ԭʼ��ֵ����ȡ�����ֵ(��λID)
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
	 * ����ԭʼ��ֵ
	 *
	 * @param keyID ��λID
	 * @param keydata ԭʼ��ֵ
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
	 * ��ȡԭʼ��ֵ
	 *
	 * @param keyID ��λID
	 * @return ��ֵ
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
