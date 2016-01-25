package c2d.lang.util.debug;

import android.util.Log;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.mod.C2D_Consts;

/**
 * �����࣬���ڹ������̨�����bug������صĹ���
 */
public class C2D_Debug
{
	/** ��ӡ��Ϣ����������--Ĭ�� */
	public static final int LOG_Def = 1 << 0;
	/** ��ӡ��Ϣ����������--��Ϣ */
	public static final int LOG_Warn = 1 << 1;
	/** ��ӡ��Ϣ����������--���� */
	public static final int LOG_Debug = 1 << 2;
	/** ��ӡ��Ϣ����������--�쳣 */
	public static final int LOG_Exp = 1 << 3;
	/** ��ӡ��Ϣ����������--���� */
	public static final int LOG_Err = 1 << 4;
	/** ��ӡ��Ϣ����������--ϵͳ */
	public static final int LOG_C2D = 1 << 5;
	/** ��ǰ��ӡ��Ϣ����������*/
	private static int LogMask = LOG_Def|LOG_Warn|LOG_Debug|LOG_Exp|LOG_Err|LOG_C2D;
	/** ��ǰ�Ŀ���̨����������ڣ����ͬʱ���������Ϣ*/
	private static C2D_Console m_console;
	/**
	 * ���ô�ӡ��Ϣ���������룬����������LOG_Def�� LOG_Debug��LOG_Err��LOG_C2D��������ϡ�
	 * 
	 * @param mask ��Ϣ��������
	 */
	public static void setLogMask(int mask)
	{
		LogMask = mask;
	}

	/**
	 * ����һ������̨�������ʱ��ͬʱ��Ӧ����̨
	 * 
	 * @param console
	 */
	public static void setConsole(C2D_Console console)
	{
		m_console = console;
	}
	/**
	 * �����̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	private static void doLog(String s)
	{
		if (c2d.lang.io.C2D_ServerLogger.isConnected())
		{
			c2d.lang.io.C2D_ServerLogger.log(s);
		}
		Log.i(C2D_Consts.tag, s);
		if (m_console != null)
		{
			m_console.onLog(s);
		}
	}

	/**
	 * �����̨������ݣ������ϲ����л���
	 * 
	 * @param s
	 *            �������
	 */
	private static void doLogChunck(String s)
	{
		if (c2d.lang.io.C2D_ServerLogger.isConnected())
		{
			c2d.lang.io.C2D_ServerLogger.log(s);
		}
		Log.i(C2D_Consts.tag, s);
		if (m_console != null)
		{
			m_console.onLogChunk(s);
		}
	}
	
	/**
	 * ��Ĭ�ϵ����������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void log(String s)
	{
		if((LogMask&LOG_Def)!=0)
		{
			doLog(s);
		}
	}

	/**
	 * ��Ĭ�ϵ����������̨������ݣ������ϲ����л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logChunk(String s)
	{
		if((LogMask&LOG_Def)!=0)
		{
			doLogChunck(s);
		}
	}

	/**
	 * �Ծ�������������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logWarn(String logValue)
	{
		if((LogMask&LOG_Warn)!=0)
		{
			doLog("[Warn]" + " " + logValue);
		}
	}
	
	/**
	 * �Ծ�������������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logWarnChunck(String logValue)
	{
		if((LogMask&LOG_Warn)!=0)
		{
			doLogChunck("[Warn]" + " " + logValue);
		}
	}

	/**
	 * ����Ϣ�����������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logDebug(String logValue)
	{
		if((LogMask&LOG_Debug)!=0)
		{
			doLog("[Debug]" + " " + logValue);
		}
	}

	/**
	 * ����Ϣ�����������̨������ݣ������ϲ����л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logDebugChunck(String logValue)
	{
		if((LogMask&LOG_Debug)!=0)
		{
			doLogChunck("[Debug]" + " " + logValue);
		}
	}
	
	/**
	 * �Դ�������������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logErr(String logValue)
	{
		if((LogMask&LOG_Err)!=0)
		{
			doLog("[Error]" + " " + logValue);
		}
	}

	/**
	 * �Դ�������������̨������ݣ������ϲ����л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logErrChunck(String logValue)
	{
		if((LogMask&LOG_Err)!=0)
		{
			doLogChunck("[Error]" + " " + logValue);
		}
	}
	
	/**
	 * ��ϵͳ�����������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logC2D(String logValue)
	{
		if((LogMask&LOG_C2D)!=0)
		{
			doLog("[C2D]" + " " + logValue);
		}
	}

	/**
	 * ��ϵͳ�����������̨������ݣ������ϲ����л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logC2DChunck(String logValue)
	{
		if((LogMask&LOG_C2D)!=0)
		{
			doLogChunck("[C2D]" + " " + logValue);
		}
	}
	/**
	 * ���쳣�����������̨������ݣ������Ͻ��л���
	 * 
	 * @param s
	 *            �������
	 */
	public static void logException(Exception logValue)
	{
		if((LogMask&LOG_Exp)!=0)
		{
			if(logValue!=null)
			{
				doLog("[Exp]" + " " + logValue.getMessage());	
			}
			else
			{
				doLog("[Exp]" + " null");	
			}
		}
	}

	private static C2D_Array m_keys = new C2D_Array();
	private static C2D_Array m_values = new C2D_Array();

	/**
	 * ������log�б��ȴ���ӡ
	 * 
	 * @param key
	 *            ��������
	 * @param value
	 *            ������ֵ��ֵ
	 */
	public static void addInfor2List(String key, String value)
	{
		m_keys.addElement(key);
		m_values.addElement(value);
	}

	/**
	 * ������log�б��ȴ���ӡ
	 * 
	 * @param key
	 *            ��������
	 * @param value
	 *            ������ֵ��ֵ
	 */
	public static void addInfor2List(String key, int value)
	{
		m_keys.addElement(key);
		m_values.addElement("" + value);
	}

	/**
	 * ���log�б�
	 */
	public static void clearInforList()
	{
		m_keys.clear();
		m_values.clear();
	}

	/**
	 * ��ӡlog�б�
	 * 
	 * @param line
	 *            Ԥ�ȴ�ӡ��һ����Ϣ
	 */
	public static void logInforList(String line)
	{
		if (line != null)
		{
			log(line);
		}
		String lineKeyTotal = "";
		String lineValueTotal = "";
		int size = C2D_Math.min(m_keys.size(), m_values.size());
		for (int i = 0; i < size; i++)
		{
			String key = (String) m_keys.elementAt(i);
			String value = (String) m_values.elementAt(i);
			String lineKey = "";
			String lineValue = "";
			lineKey += key;
			lineValue += value;
			lineKeyTotal += lineKey + "\t";
			lineValueTotal += lineValue + "\t";
		}
		log(lineKeyTotal);
		log(lineValueTotal);
	}

	/** ������󣬲���ת�����ҳ�� 
	 * @param errInfor ������Ϣ
	 */
	public static void reportError(String errInfor)
	{
		
	}
}
