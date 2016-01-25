package c2d.lang.util.debug;

import android.util.Log;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.mod.C2D_Consts;

/**
 * 调试类，用于管理控制台输出、bug调试相关的功能
 */
public class C2D_Debug
{
	/** 打印消息的类型掩码--默认 */
	public static final int LOG_Def = 1 << 0;
	/** 打印消息的类型掩码--信息 */
	public static final int LOG_Warn = 1 << 1;
	/** 打印消息的类型掩码--调试 */
	public static final int LOG_Debug = 1 << 2;
	/** 打印消息的类型掩码--异常 */
	public static final int LOG_Exp = 1 << 3;
	/** 打印消息的类型掩码--错误 */
	public static final int LOG_Err = 1 << 4;
	/** 打印消息的类型掩码--系统 */
	public static final int LOG_C2D = 1 << 5;
	/** 当前打印消息的类型掩码*/
	private static int LogMask = LOG_Def|LOG_Warn|LOG_Debug|LOG_Exp|LOG_Err|LOG_C2D;
	/** 当前的控制台对象，如果存在，则会同时向其输出消息*/
	private static C2D_Console m_console;
	/**
	 * 设置打印消息的类型掩码，参数可以是LOG_Def、 LOG_Debug、LOG_Err、LOG_C2D的任意组合。
	 * 
	 * @param mask 消息类型掩码
	 */
	public static void setLogMask(int mask)
	{
		LogMask = mask;
	}

	/**
	 * 设置一个控制台，在输出时，同时响应控制台
	 * 
	 * @param console
	 */
	public static void setConsole(C2D_Console console)
	{
		m_console = console;
	}
	/**
	 * 向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
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
	 * 向控制台输出内容，输出完毕不进行换行
	 * 
	 * @param s
	 *            输出内容
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
	 * 以默认的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void log(String s)
	{
		if((LogMask&LOG_Def)!=0)
		{
			doLog(s);
		}
	}

	/**
	 * 以默认的类型向控制台输出内容，输出完毕不进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logChunk(String s)
	{
		if((LogMask&LOG_Def)!=0)
		{
			doLogChunck(s);
		}
	}

	/**
	 * 以警告的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logWarn(String logValue)
	{
		if((LogMask&LOG_Warn)!=0)
		{
			doLog("[Warn]" + " " + logValue);
		}
	}
	
	/**
	 * 以警告的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logWarnChunck(String logValue)
	{
		if((LogMask&LOG_Warn)!=0)
		{
			doLogChunck("[Warn]" + " " + logValue);
		}
	}

	/**
	 * 以信息的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logDebug(String logValue)
	{
		if((LogMask&LOG_Debug)!=0)
		{
			doLog("[Debug]" + " " + logValue);
		}
	}

	/**
	 * 以信息的类型向控制台输出内容，输出完毕不进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logDebugChunck(String logValue)
	{
		if((LogMask&LOG_Debug)!=0)
		{
			doLogChunck("[Debug]" + " " + logValue);
		}
	}
	
	/**
	 * 以错误的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logErr(String logValue)
	{
		if((LogMask&LOG_Err)!=0)
		{
			doLog("[Error]" + " " + logValue);
		}
	}

	/**
	 * 以错误的类型向控制台输出内容，输出完毕不进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logErrChunck(String logValue)
	{
		if((LogMask&LOG_Err)!=0)
		{
			doLogChunck("[Error]" + " " + logValue);
		}
	}
	
	/**
	 * 以系统的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logC2D(String logValue)
	{
		if((LogMask&LOG_C2D)!=0)
		{
			doLog("[C2D]" + " " + logValue);
		}
	}

	/**
	 * 以系统的类型向控制台输出内容，输出完毕不进行换行
	 * 
	 * @param s
	 *            输出内容
	 */
	public static void logC2DChunck(String logValue)
	{
		if((LogMask&LOG_C2D)!=0)
		{
			doLogChunck("[C2D]" + " " + logValue);
		}
	}
	/**
	 * 以异常的类型向控制台输出内容，输出完毕进行换行
	 * 
	 * @param s
	 *            输出内容
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
	 * 将加入log列表，等待打印
	 * 
	 * @param key
	 *            属性名称
	 * @param value
	 *            属性数值数值
	 */
	public static void addInfor2List(String key, String value)
	{
		m_keys.addElement(key);
		m_values.addElement(value);
	}

	/**
	 * 将加入log列表，等待打印
	 * 
	 * @param key
	 *            属性名称
	 * @param value
	 *            属性数值数值
	 */
	public static void addInfor2List(String key, int value)
	{
		m_keys.addElement(key);
		m_values.addElement("" + value);
	}

	/**
	 * 清空log列表
	 */
	public static void clearInforList()
	{
		m_keys.clear();
		m_values.clear();
	}

	/**
	 * 打印log列表
	 * 
	 * @param line
	 *            预先打印的一行信息
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

	/** 报告错误，并且转向错误页面 
	 * @param errInfor 错误信息
	 */
	public static void reportError(String errInfor)
	{
		
	}
}
