package c2d.thirdpt.iptv;

import c2d.lang.util.debug.C2D_Debug;


/**
 * 机顶盒判断，根据获取机顶盒部分信息判断该盒子的版本型号等信息。目前可以在以下机顶盒中作出分辨。
 * 浙江电信5款机顶盒部分信息如下：
 *		属性参数名												机顶盒				
 *									创维				UT				HG600			EC1308		B600
 *	totalMemory						2048000			20971136		2097152			10534528	1048576	
 *	microedition.profiles			MIDP-2.0		MIDP-2.1		MIDP-2.1		MIDP-2.1	MIDP-2.0
 *	microedition.configuration		CLDC-1.1		CLDC-1.1		CLDC-1.1		CLDC-1.1	CLDC-1.1
 *	microedition.locale				en-US			en-US			en-US			zh-CN		zh-CN
 *	microedition.platform			j2me			j2me			j2me			j2me		zJar1.3 ZTE B500
 *	microedition.encoding			GB2312			ISO8859_1		GB2312			ISO8859_1	GB2312	
 *	microedition.commports			0,1				COM1			COM1			COM1		0,1
 *	microedition.hostname			localhost		(none)			localhost		(none)		STLinux
 *	microedition.jtwi.version		null			1				1				1			null
 *	microedition.media.version		null			1.1				1.1				1.2			1.1
 *	supports.mixing					null			false			false			false		true
 *	supports.audio.capture			null			true			true			false		false
 *	supports.video.capture			null			false			false			false		false
 *	supports.recording				null			true			true			false		false
 *	audio.encodings					null			encoding=pcm	encoding=pcm	null		null
 *	streamable.contents				null			null			audio/x-wav					null
 *
 *	@author Seven_Q
 *
 */
public class C2D_IptvJudge
{
	/** 默认空白的机顶盒 */
	public final static byte IPTV_NONE = 0;
	/** 中兴B600机顶盒 */
	public final static byte IPTV_ZTE = IPTV_NONE + 1;
	/** 创维SKYWORTH机顶盒 */
	public final static byte IPTV_CW = IPTV_ZTE + 1;
	/** UT机顶盒 */
	public final static byte IPTV_UT = IPTV_CW + 1;
	/** HG600机顶盒 */
	public final static byte IPTV_HG = IPTV_UT + 1;
	/** 华为EC1308机顶盒 */
	public final static byte IPTV_HW = IPTV_HG + 1;
	/** 机顶盒支持的MIDP版本 ,返回格式值为“MIDP-1.0”或“MIDP-2.0” */
	public static String PARA_PROF = "microedition.profiles";
	/** 机顶盒支持的CLDC版本,返回格式值为“CLDC-1.0”或“CLDC-2.0” */
	public static String PARA_CONF = "microedition.configuration";
	/** 机顶盒所在的国家或地区，返回值格式为“en-US” */
	public static String PARA_LOCA = "microedition.locale";
	/** 设备的品牌和型号，Nokia手机的返回值格式为“Nokia6310i/4.42” */
	public static String PARA_PLAT = "microedition.platform";
	/** 机顶盒默认的字符集名称，返回值格式为“ISO-8859-1” */
	public static String PARA_ENCO = "microedition.encoding";
	/** 机顶盒可以使用的串口列表，返回值中各个串口之间使用逗号分隔 */
	public static String PARA_COMM = "microedition.commports";
	/** MIDP2.0定义，代表本地主机名称，需要机顶盒支持。 */
	public static String PARA_HOST = "microedition.hostname";
	/** 机顶盒支持的JTWI版本，值必须是“1.0” */
	public static String PARA_JTWI = "microedition.jtwi.version";
	/** 机顶盒支持的MMAPI版本，如果不支持则返回null */
	public static String PARA_MEDI = "microedition.media.version";
	/** 机顶盒是否支持混音(同时播放多个Player)，返回值为“true”或“false” */
	public static String PARA_MIXI = "supports.mixing";
	/** 机顶盒是否支持声音捕获(录音)，返回值为“true”或“false” */
	public static String PARA_AUDIO = "supports.audio.capture";
	/**  */
	public static String PARA_VIDEO = "supports.video.capture";
	public static String PARA_RECO = "supports.recording";
	public static String PARA_AUEN = "audio.encodings";
	public static String PARA_CONT = "streamable.contents";
	
	private static byte IPTV_CUR=IPTV_NONE;
	/**
	 * 分辨机顶盒型号
	 * @return
	 */
	public static byte DistinguishModel()
	{
		String prop_plat = getParaValue(PARA_PLAT);
		String prop_prof = getParaValue(PARA_PROF);
		String prop_loca = getParaValue(PARA_LOCA);
		String prop_cont = getParaValue(PARA_CONT);
		String prop_enco = getParaValue(PARA_ENCO);
		String prop_auen = getParaValue(PARA_AUEN);
		IPTV_CUR=IPTV_NONE;
		if (prop_plat != null && prop_plat.toLowerCase().indexOf("zte") >=0)
		{
			IPTV_CUR = IPTV_ZTE;
		}
		else if (prop_plat != null && prop_plat.equals("j2me"))
		{
			if (prop_prof != null && prop_prof.equals("MIDP-2.0"))
			{
				IPTV_CUR = IPTV_CW;
			}
			else if (prop_loca != null && prop_loca.equals("zh-CN"))
			{
				IPTV_CUR = IPTV_HW;
			}
			else if (prop_cont != null && prop_cont.equals("audio/x-wav"))
			{
				IPTV_CUR = IPTV_HG;
			}
			else if (prop_enco != null && prop_enco.equals("ISO8859_1") && prop_auen != null
					&& prop_auen.equals("encoding=pcm"))
			{
				IPTV_CUR = IPTV_UT;
			}
		}
		return IPTV_CUR;
	}
	/**
	 * 获得当前机顶盒型号
	 * @return 当前机顶盒型号
	 */
	public static byte getCurIptv()
	{
		return IPTV_CUR;
	}

	public static long getTotalMemory()
	{
		int totalMem = (int)(Runtime.getRuntime().totalMemory());
		return totalMem;
	}

	public static long getTotalMemoryK()
	{
		int totalMem = (int)(Runtime.getRuntime().totalMemory()/1024);
		return totalMem;
	}
	
	/**
	 * 获取机顶盒对应参数的属性值
	 * 
	 * @param para
	 * @return
	 */
	public static String getParaValue(String para)
	{
		String value=null;
		try
		{
			value = System.getProperty(para);
			C2D_Debug.logC2D(para+":"+value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 获得机顶盒型号简称
	 * @return 机顶盒型号简称
	 */
	public static String getIptvModel()
	{
		String prop_plat = getParaValue(PARA_PLAT);
		C2D_Debug.log("Platform:"+prop_plat);
		String res="unkonw";
		switch(IPTV_CUR)
		{
		case IPTV_ZTE:
			res="ZTE";
			break;
		case IPTV_CW:
			res="CW";
			break;
		case IPTV_UT:
			res="UT";
			break;
		case IPTV_HG:
			res="HG";
			break;
		case IPTV_HW:
			res="HW";
			break;
		default:
			res=prop_plat;
		}
		return res;
	}
}
