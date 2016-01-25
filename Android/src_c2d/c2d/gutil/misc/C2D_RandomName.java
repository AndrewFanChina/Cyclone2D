package c2d.gutil.misc;

import c2d.lang.io.C2D_Encoder;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.C2D_Misc;
import c2d.mod.C2D_Consts;

/**
 * 随机取名类
 * 
 * @author AndrewFan
 * 
 */
public class C2D_RandomName extends C2D_Object
{
	// 姓氏列表
	private C2D_Array m_familyName;
	// 男性名字列表
	private C2D_Array m_maleName;
	// 女性名字列表
	private C2D_Array m_femaleName;
	/**
	 * 构建取名类
	 * 
	 */
	public C2D_RandomName()
	{
		loadData();
	}

	/**
	 * 获取下一个随机姓名，随机性别
	 * 
	 * @return 随机姓名
	 */
	public String nextName()
	{
		String fullName = "";
		if (m_familyName != null)
		{
			fullName += randomText(m_familyName);
		}
		C2D_Array nameArray=C2D_Math.getRandom(100)<50?m_maleName:m_femaleName;
		if (nameArray != null)
		{
			fullName += randomText(nameArray);
		}
		return fullName;
	}
	/**
	 * 获取下一个随机姓名，指定性别，男性:true，女性:false
	 * 
	 * @return 随机姓名
	 */
	public String nextName(boolean sex)
	{
		String fullName = "";
		if (m_familyName != null)
		{
			fullName += randomText(m_familyName);
		}
		C2D_Array nameArray=sex?m_maleName:m_femaleName;
		if (nameArray != null)
		{
			fullName += randomText(nameArray);
		}
		return fullName;
	}

	/**
	 * 获取个count随机姓名，并且存入一个数组返回
	 * 
	 * @param count
	 */
	public C2D_Array nextNames(int count)
	{
		C2D_Array names = new C2D_Array();
		for (int i = 0; i < count; i++)
		{
			names.addElement(nextName());
		}
		return names;
	}
	
	/**
	 * 从文本数组中随机取一个文本返回
	 * 
	 * @param texts
	 *            文本数组
	 * @return 返回的文本
	 */
	private String randomText(C2D_Array texts)
	{
		if (texts == null)
		{
			return null;
		}
		int count = texts.size();
		int rndID = C2D_Math.getRandom(count);
		return (String) texts.elementAt(rndID);
	}

	/**
	 * 载入名字数据
	 */
	private void loadData()
	{
		String folder = C2D_Consts.STR_RES + C2D_Consts.STR_Fonts;
		// 姓氏
		m_familyName = new C2D_Array();
		readText(m_familyName, folder + "name_family.txt");
		// 男性名字
		m_maleName = new C2D_Array();
		readText(m_maleName, folder + "name_male.txt");
		// 女性名字
		m_femaleName = new C2D_Array();
		readText(m_femaleName, folder + "name_female.txt");

	}

	/**
	 * 从文件路径读取所有的文本，存入动态数组后返回
	 * 
	 * @param texts
	 *            文本数组
	 * @param filePath
	 *            文件路径
	 */
	private void readText(C2D_Array texts, String filePath)
	{
		if(texts==null)
		{
			return;
		}
		byte datas[] = C2D_IOUtil.readRes_Bytes(filePath);
		String content = C2D_Encoder.decodeString(datas, "utf-8");
		if(content!=null)
		{
			String strList[] = C2D_Misc.splitString(content.trim(), '\n');
			int size=strList.length;
			char t=(char)65279;
			String st=t+"";
			for (int i = 0; i < size; i++)
			{
				String str=strList[i];
				if(str!=null)
				{
					str= C2D_Misc.replace(str, "\r", "");
					str= C2D_Misc.replace(str, st, "");
					texts.addElement(str.trim());		
				}
			}
		}

	}

	public void onRelease()
	{
		if (m_familyName != null)
		{
			m_familyName.clear();
			m_familyName = null;
		}
		if (m_maleName != null)
		{
			m_maleName.clear();
			m_maleName = null;
		}
		if (m_femaleName != null)
		{
			m_femaleName.clear();
			m_femaleName = null;
		}
	}

}
