package c2d.lang.io;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ���ڴ������ת������
 * @author AndrewFan
 *
 */
public class C2D_Encoder
{

	/**
	 * ����GBK������ַ���ת������ISO-8859-1����ĸ�ʽ�ַ���
	 * 
	 * @param content
	 *            Ҫת��������
	 * @return ת������ַ���
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
	 * ����ISO-8859-1������ַ���ת������GBK����ĸ�ʽ�ַ���
	 * 
	 * @param content
	 *            Ҫת��������
	 * @return ת������ַ���
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
	 * ���ַ�������ָ���ı���ת���ɶ���������
	 * 
	 * @param content
	 *            �ַ�������
	 * @param encode
	 *            ����
	 * @return ����������
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
	 * ���ַ������б���ת��
	 * 
	 * @param content
	 *            ����
	 * @param encodeFrom
	 *            Դ�ַ�����
	 * @param encodeDest
	 *            ת���ɱ���
	 * @return ָ��������ַ���
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
	 * ���ַ������б���ת��
	 * 
	 * @param content
	 *            ����
	 * @param encodeDest
	 *            ת���ɱ���
	 * @return ָ��������ַ���
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
	 * ���ַ������н���
	 * 
	 * @param content
	 *            �ֽ�����
	 * @param encode
	 *            �ַ�����
	 * @return ָ��������ַ���
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
	 * ���ַ������н���
	 * 
	 * @param content
	 *            �ֽ�����
	 * @param encodeList
	 *            �ַ������б����һ��������ȷ�������̷��ؽ��
	 * @return ָ��������ַ���
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
	 * �����ַ������unicode�����ַ����ϳ��ַ�������
	 * 
	 * @param in
	 *            ������ַ�����
	 * @return �ϳɵ��ַ���
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
				{ // �Ƿ�����һ���ַ�
					c = in[off++]; // ȡ����һ���ַ�
				}
				else
				{
					out[outLen++] = '\\'; // ĩ�ַ�Ϊ'\'������
					break;
				}
				if (c == 'u')
				{ // �����"\\u"
					int value = 0;
					if (in.length > off + 4)
					{ // �ж�"\\u"����Ƿ����ĸ��ַ�
						boolean isUnicode = true;
						for (int i = 0; i < 4; i++)
						{ // �����ĸ��ַ�
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
								isUnicode = false; // �ж��Ƿ�Ϊunicode��
							}
						}
						if (isUnicode)
						{ // ��unicode��ת��Ϊ�ַ�
							out[outLen++] = (char) value;
						}
						else
						{ // ����unicode���"\\uXXXX"���뷵��ֵ
							off = off - 4;
							out[outLen++] = '\\';
							out[outLen++] = 'u';
							out[outLen++] = in[off++];
						}
					}
					else
					{ // �����ĸ��ַ����"\\u"���뷵�ؽ��������
						out[outLen++] = '\\';
						out[outLen++] = 'u';
						continue;
					}
				}
				else
				{
					switch (c)
					{ // �ж�"\\"����Ƿ�������ַ����س���tabһ���
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
