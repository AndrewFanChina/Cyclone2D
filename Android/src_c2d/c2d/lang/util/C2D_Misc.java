package c2d.lang.util;

import java.util.Hashtable;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
/**
 * �Ӻ�С���ܹ�����
 * @author AndrewFan
 *
 */
public class C2D_Misc
{

	/**
	 * ��ȡ�ļ����ĺ�׺��
	 * 
	 * @param imageName
	 * @return �޺�׺�ļ���+��׺��
	 */
	public static String[] getSubffix(String imageName)
	{
		if (imageName == null)
		{
			return null;
		}
		int lastIndex = imageName.lastIndexOf('.');
		if (lastIndex <= 0)
		{
			return new String[]
			{ imageName, "" };
		}
		String prefix = imageName.substring(0, lastIndex);
		String subfix = imageName.substring(lastIndex, imageName.length());
		return new String[]
		{ prefix, subfix };
	}


	/**
	 * ��һ���ַ����У�����ָ���Ĳ�ͬ���Էָ��������Լ�ֵ�ָ������зָ��Ͷ�ȡ֮�󣬴����һ����ϣ����
	 * 
	 * @param content
	 *            ָ�����ַ���
	 * @param sp
	 *            ��ͬ���Լ�ָ���
	 * @param skv
	 *            �������ݵļ�ֵ�ָ���
	 * @param hashTable
	 *            ��ŷ������ݵĹ�ϣ�����ǰ����չ�ϣ��
	 */
	public static void readProperty(String content, char sp, char skv, Hashtable hashTable)
	{
		if (hashTable != null)
		{
			hashTable.clear();
		}
		if (hashTable == null || content == null)
		{
			return;
		}
		String strList[] = splitString(content, sp);
		if (strList != null)
		{
			for (int i = 0; i < strList.length; i++)
			{
				String p = strList[i];
				if (p != null)
				{
					String pKV[] = splitString(p, skv);
					if (pKV != null && pKV.length >= 2 && pKV[0] != null && pKV[1] != null)
					{
						hashTable.put(pKV[0], pKV[1]);
					}
				}
			}
		}
	}

	/**
	 * ʹ��ָ�����ַ�����һ���ַ����Ĳ������ݣ� ԭ���ǣ����ؿ��м�İ����ַ������������ֳɼ������֣����������ؿ�ǰ�ĸ�������
	 * 
	 * @param content
	 *            ���������ַ���
	 * @param conChar
	 *            ���ڻ������ַ�
	 * @return ��������ַ���
	 */
	public static String confuseString(String content, char conChar)
	{
		if (content == null || content.length() == 0)
		{
			return content;
		}
		int len = content.length();
		int len2 = C2D_Math.max(len / 2, 1);
		int len1 = C2D_Math.min((len - len2) / 2, len - len2);
		int len3 = len - len1 - len2;
		StringBuffer newContent = new StringBuffer();
		newContent.append(content.substring(0, len1));
		if (len2 > 0)
		{
			for (int i = 0; i < len2; i++)
			{
				newContent.append(conChar);
			}
		}
		if (len3 > 0)
		{
			newContent.append(content.substring(len1 + len2, len1 + len2 + len3));
		}
		return newContent.toString();
	}
	private static C2D_Array strArray = new C2D_Array(0);

	/**
	 * �ָ��ַ���.
	 * 
	 * @param str
	 *            String ���ָ���ַ���
	 * @param separ
	 *            char �ָ��ַ���һ��ʹ��'|'
	 * @return String[] �ָ�õ��ַ�������
	 */
	public static String[] splitString(String str, char separ)
	{
		int curIndex = 0;
		int preIndex = 0;
		int strCount = str.length();
		strArray.removeAllElements();
		boolean head = true;
		do
		{
			if (curIndex >= strCount)
			{
				if (preIndex != curIndex)
				{
					strArray.addElement(str.substring(preIndex, curIndex));
				}
				break;
			}
			char c = str.charAt(curIndex);
			// ����
			if (c == separ)
			{
				if (head)
				{
					strArray.addElement(str.substring(0, curIndex));
					head = false;

				}
				else
				{
					strArray.addElement(str.substring(preIndex, curIndex));
				}
				preIndex = ++curIndex;

			}
			else
			{
				curIndex++;
			}
		}
		while (true);
		int size = strArray.size();
		String[] divStr = new String[size];
		for (int i = 0; i < size; i++)
		{
			divStr[i] = (String) strArray.elementAt(i);
		}
		strArray.removeAllElements();
		return divStr;
	}

	/**
	 * ���ַ��е�һ�����滻��ָ���ַ�
	 * 
	 * @param content
	 *            the string
	 * @param oldPart
	 *            the old part
	 * @param newPart
	 *            the new part
	 * @return the string
	 */
	public static String replace(String content, String oldPart, String newPart)
	{
		if (content == null || oldPart == null || newPart == null || oldPart.equals(newPart))
		{
			return content;
		}
		int searchP = 0;
		while (true)
		{
			int begin = content.indexOf(oldPart, searchP);
			if (begin < 0)
			{
				break;
			}
			int end = begin + oldPart.length();
			String c1 = content.substring(0, begin);
			String c2 = content.substring(end, content.length());
			content = c1 + newPart + c2;
			searchP = begin + newPart.length();
		}

		return content;
	}

	/**
	 * ��ȡָ���ַ�����ռ�Ŀ�ȣ�ÿ�������ַ�ռ��2��Ӣ���ַ�ռ��1
	 * 
	 * @param s
	 * @return
	 */
	public static int getStringLen(String s)
	{
		if (s == null)
		{
			return 0;
		}
		int w = 0;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c >= '!' && c < '~')
			{
				w++;
			}
			else
			{
				w += 2;
			}
		}
		return w;
	}

	public static boolean isEmptyStr(String str)
	{
		return str==null || str.length()==0;
	}

}
