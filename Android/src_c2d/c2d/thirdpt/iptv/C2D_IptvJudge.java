package c2d.thirdpt.iptv;

import c2d.lang.util.debug.C2D_Debug;


/**
 * �������жϣ����ݻ�ȡ�����в�����Ϣ�жϸú��ӵİ汾�ͺŵ���Ϣ��Ŀǰ���������»������������ֱ档
 * �㽭����5������в�����Ϣ���£�
 *		���Բ�����												������				
 *									��ά				UT				HG600			EC1308		B600
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
	/** Ĭ�Ͽհ׵Ļ����� */
	public final static byte IPTV_NONE = 0;
	/** ����B600������ */
	public final static byte IPTV_ZTE = IPTV_NONE + 1;
	/** ��άSKYWORTH������ */
	public final static byte IPTV_CW = IPTV_ZTE + 1;
	/** UT������ */
	public final static byte IPTV_UT = IPTV_CW + 1;
	/** HG600������ */
	public final static byte IPTV_HG = IPTV_UT + 1;
	/** ��ΪEC1308������ */
	public final static byte IPTV_HW = IPTV_HG + 1;
	/** ������֧�ֵ�MIDP�汾 ,���ظ�ʽֵΪ��MIDP-1.0����MIDP-2.0�� */
	public static String PARA_PROF = "microedition.profiles";
	/** ������֧�ֵ�CLDC�汾,���ظ�ʽֵΪ��CLDC-1.0����CLDC-2.0�� */
	public static String PARA_CONF = "microedition.configuration";
	/** ���������ڵĹ��һ����������ֵ��ʽΪ��en-US�� */
	public static String PARA_LOCA = "microedition.locale";
	/** �豸��Ʒ�ƺ��ͺţ�Nokia�ֻ��ķ���ֵ��ʽΪ��Nokia6310i/4.42�� */
	public static String PARA_PLAT = "microedition.platform";
	/** ������Ĭ�ϵ��ַ������ƣ�����ֵ��ʽΪ��ISO-8859-1�� */
	public static String PARA_ENCO = "microedition.encoding";
	/** �����п���ʹ�õĴ����б�����ֵ�и�������֮��ʹ�ö��ŷָ� */
	public static String PARA_COMM = "microedition.commports";
	/** MIDP2.0���壬�������������ƣ���Ҫ������֧�֡� */
	public static String PARA_HOST = "microedition.hostname";
	/** ������֧�ֵ�JTWI�汾��ֵ�����ǡ�1.0�� */
	public static String PARA_JTWI = "microedition.jtwi.version";
	/** ������֧�ֵ�MMAPI�汾�������֧���򷵻�null */
	public static String PARA_MEDI = "microedition.media.version";
	/** �������Ƿ�֧�ֻ���(ͬʱ���Ŷ��Player)������ֵΪ��true����false�� */
	public static String PARA_MIXI = "supports.mixing";
	/** �������Ƿ�֧����������(¼��)������ֵΪ��true����false�� */
	public static String PARA_AUDIO = "supports.audio.capture";
	/**  */
	public static String PARA_VIDEO = "supports.video.capture";
	public static String PARA_RECO = "supports.recording";
	public static String PARA_AUEN = "audio.encodings";
	public static String PARA_CONT = "streamable.contents";
	
	private static byte IPTV_CUR=IPTV_NONE;
	/**
	 * �ֱ�������ͺ�
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
	 * ��õ�ǰ�������ͺ�
	 * @return ��ǰ�������ͺ�
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
	 * ��ȡ�����ж�Ӧ����������ֵ
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
	 * ��û������ͺż��
	 * @return �������ͺż��
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
