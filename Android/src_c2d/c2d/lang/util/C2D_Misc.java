package c2d.lang.util;

import java.util.Hashtable;

import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
/**
 * 杂合小功能工具类
 * @author AndrewFan
 *
 */
public class C2D_Misc
{

	/**
	 * 获取文件名的后缀名
	 * 
	 * @param imageName
	 * @return 无后缀文件名+后缀名
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
	 * 从一行字符串中，根据指定的不同属性分隔符，属性键值分隔符进行分隔和读取之后，存放在一个哈希表中
	 * 
	 * @param content
	 *            指定的字符串
	 * @param sp
	 *            不同属性间分隔符
	 * @param skv
	 *            属性内容的键值分隔符
	 * @param hashTable
	 *            存放返回内容的哈希表，存放前会清空哈希表
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
	 * 使用指定的字符混淆一个字符串的部分内容， 原则是：隐藏靠中间的半数字符串，如果不足分成几个部分，则优先隐藏靠前的更多内容
	 * 
	 * @param content
	 *            被混淆的字符串
	 * @param conChar
	 *            用于混淆的字符
	 * @return 混淆后的字符串
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
	 * 分割字符串.
	 * 
	 * @param str
	 *            String 待分割的字符串
	 * @param separ
	 *            char 分割字符，一般使用'|'
	 * @return String[] 分割好的字符串数组
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
			// 换行
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
	 * 将字符中的一部分替换成指定字符
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
	 * 获取指定字符串所占的宽度，每个中文字符占用2，英文字符占用1
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
