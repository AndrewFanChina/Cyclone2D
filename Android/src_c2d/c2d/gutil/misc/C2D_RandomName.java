package c2d.gutil.misc;

import c2d.lang.io.C2D_Encoder;
import c2d.lang.io.C2D_IOUtil;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Math;
import c2d.lang.obj.C2D_Object;
import c2d.lang.util.C2D_Misc;
import c2d.mod.C2D_Consts;

/**
 * ���ȡ����
 * 
 * @author AndrewFan
 * 
 */
public class C2D_RandomName extends C2D_Object
{
	// �����б�
	private C2D_Array m_familyName;
	// ���������б�
	private C2D_Array m_maleName;
	// Ů�������б�
	private C2D_Array m_femaleName;
	/**
	 * ����ȡ����
	 * 
	 */
	public C2D_RandomName()
	{
		loadData();
	}

	/**
	 * ��ȡ��һ���������������Ա�
	 * 
	 * @return �������
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
	 * ��ȡ��һ�����������ָ���Ա�����:true��Ů��:false
	 * 
	 * @return �������
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
	 * ��ȡ��count������������Ҵ���һ�����鷵��
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
	 * ���ı����������ȡһ���ı�����
	 * 
	 * @param texts
	 *            �ı�����
	 * @return ���ص��ı�
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
	 * ������������
	 */
	private void loadData()
	{
		String folder = C2D_Consts.STR_RES + C2D_Consts.STR_Fonts;
		// ����
		m_familyName = new C2D_Array();
		readText(m_familyName, folder + "name_family.txt");
		// ��������
		m_maleName = new C2D_Array();
		readText(m_maleName, folder + "name_male.txt");
		// Ů������
		m_femaleName = new C2D_Array();
		readText(m_femaleName, folder + "name_female.txt");

	}

	/**
	 * ���ļ�·����ȡ���е��ı������붯̬����󷵻�
	 * 
	 * @param texts
	 *            �ı�����
	 * @param filePath
	 *            �ļ�·��
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
