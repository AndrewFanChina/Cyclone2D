package c2d.lang.io;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 用于处理编码转换的类
 * @author AndrewFan
 *
 */
public class C2D_Encoder
{

	/**
	 * 将以GBK编码的字符串转换成以ISO-8859-1编码的格式字符串
	 * 
	 * @param content
	 *            要转换的内容
	 * @return 转换后的字符串
	 */
	public static String GBK2ISO(String content)
	{
		if (content == null)
		{
			return null;
		}
		String result = null;
		try
		{
			byte bytes[] = content.getBytes("GBK");
			result = new String(bytes, "ISO-8859-1");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将以ISO-8859-1编码的字符串转换成以GBK编码的格式字符串
	 * 
	 * @param content
	 *            要转换的内容
	 * @return 转换后的字符串
	 */
	public static String ISO2GBK(String content)
	{
		if (content == null)
		{
			return null;
		}
		String result = null;
		try
		{
			byte bytes[] = content.getBytes("ISO-8859-1");
			result = new String(bytes, "GBK");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将字符串先以指定的编码转换成二进制数据
	 * 
	 * @param content
	 *            字符串内容
	 * @param encode
	 *            编码
	 * @return 编码后的数据
	 */
	public static byte[] encodeData(String content, String encode)
	{
		if (content == null || encode == null)
		{
			return null;
		}
		byte bytes[] = null;
		try
		{
			bytes = content.getBytes(encode);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 对字符串进行编码转换
	 * 
	 * @param content
	 *            内容
	 * @param encodeFrom
	 *            源字符编码
	 * @param encodeDest
	 *            转换成编码
	 * @return 指定编码的字符串
	 */
	public static String encodeString(String content, String encodeFrom, String encodeDest)
	{
		if (content == null || encodeFrom == null || encodeDest == null)
		{
			return null;
		}
		String result = null;
		try
		{
			byte bytes[] = content.getBytes(encodeFrom);
			result = new String(bytes, encodeDest);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 对字符串进行编码转换
	 * 
	 * @param content
	 *            内容
	 * @param encodeDest
	 *            转换成编码
	 * @return 指定编码的字符串
	 */
	public static String encodeString(String content,  String encodeDest)
	{
		if (content == null || encodeDest == null)
		{
			return null;
		}
		String result = null;
		try
		{
			byte bytes[] = content.getBytes(encodeDest);
			result = new String(bytes);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 对字符串进行解码
	 * 
	 * @param content
	 *            字节内容
	 * @param encode
	 *            字符编码
	 * @return 指定编码的字符串
	 */
	public static String decodeString(byte content[], String encode)
	{
		if (content == null || encode == null)
		{
			return null;
		}
		String result = null;
		try
		{
			result = new String(content, encode);
		}
		catch (Exception e)
		{
			C2D_Debug.log("Error:" + e.getMessage());
		}
		return result;
	}

	/**
	 * 对字符串进行解码
	 * 
	 * @param content
	 *            字节内容
	 * @param encodeList
	 *            字符编码列表，如果一个编码正确，则立刻返回结果
	 * @return 指定编码的字符串
	 */
	public static String decodeString(byte content[], String encodeList[])
	{
		if (content == null || encodeList == null)
		{
			return null;
		}
		String result = null;
		for (int i = 0; i < encodeList.length; i++)
		{
			String encode = encodeList[i];
			if (encode == null)
			{
				continue;
			}
			try
			{
				result = new String(content, encode);
				break;
			}
			catch (Exception e)
			{
				C2D_Debug.logException(e);
			}
		}
		return result;
	}
	/**
	 * 解释字符数组的unicode编码字符，合成字符串返回
	 * 
	 * @param in
	 *            输入的字符数组
	 * @return 合成的字符串
	 */
	public static String decodeUnicode(char[] in)
	{
		int off = 0;
		char c;
		char[] out = new char[in.length];
		int outLen = 0;
		while (off < in.length)
		{
			c = in[off++];
			if (c == '\\')
			{
				if (in.length > off)
				{ // 是否有下一个字符
					c = in[off++]; // 取出下一个字符
				}
				else
				{
					out[outLen++] = '\\'; // 末字符为'\'，返回
					break;
				}
				if (c == 'u')
				{ // 如果是"\\u"
					int value = 0;
					if (in.length > off + 4)
					{ // 判断"\\u"后边是否有四个字符
						boolean isUnicode = true;
						for (int i = 0; i < 4; i++)
						{ // 遍历四个字符
							c = in[off++];
							switch (c)
							{
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + c - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + c - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + c - 'A';
								break;
							default:
								isUnicode = false; // 判断是否为unicode码
							}
						}
						if (isUnicode)
						{ // 是unicode码转换为字符
							out[outLen++] = (char) value;
						}
						else
						{ // 不是unicode码把"\\uXXXX"填入返回值
							off = off - 4;
							out[outLen++] = '\\';
							out[outLen++] = 'u';
							out[outLen++] = in[off++];
						}
					}
					else
					{ // 不够四个字符则把"\\u"放入返回结果并继续
						out[outLen++] = '\\';
						out[outLen++] = 'u';
						continue;
					}
				}
				else
				{
					switch (c)
					{ // 判断"\\"后边是否接特殊字符，回车，tab一类的
					case 't':
						c = '\t';
						out[outLen++] = c;
						break;
					case 'r':
						c = '\r';
						out[outLen++] = c;
						break;
					case 'n':
						c = '\n';
						out[outLen++] = c;
						break;
					case 'f':
						c = '\f';
						out[outLen++] = c;
						break;
					default:
						out[outLen++] = '\\';
						out[outLen++] = c;
						break;
					}
				}
			}
			else
			{
				out[outLen++] = (char) c;
			}
		}
		String s = null;
		try
		{
			s = new String(out, 0, outLen);
			s.trim();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return s;

	}
}
