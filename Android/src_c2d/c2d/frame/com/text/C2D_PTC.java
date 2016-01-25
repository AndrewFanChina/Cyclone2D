package c2d.frame.com.text;

import c2d.lang.obj.C2D_Object;
import c2d.plat.gfx.C2D_Image;
import c2d.plat.gfx.C2D_ImageClip;

/**
 * ͼƬ�ı��������� ����Ҫָ��һ��ͼƬC2D_Image��һ��ӳ���ַ�����ָ������ͼƬ�ϻ��Ƶ�
 * �������(charX,charY)��ָ�������ַ��Ŀ��(CharW)������߶�(CharH)��
 * ��ʾ�ַ�֮��ļ��(Gap)��������ʾ��λ��(LeastNum)����Щ�������Ե���ָ����Ҳ ����ʹ��setParametersһ�����á�
 * ע����ͼƬ���ֲ�ͬ��������ַ�����ӳ��ͼ�������Զ��У���һ�в�����
 * �������е��ı�ʱ����ȡ��ͼƬ��β����������һ��(textX,textY+=CharH)���� ��ȡ��
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_PTC extends C2D_Object
{
	/** ���ڻ��Ƶ�ͼƬ */
	protected C2D_Image m_image;
	/** ӳ���ַ��� */
	protected String m_charTable;
	/** �ַ�����λ��ͼƬ�е�X���� */
	protected int m_TextX;
	/** �ַ�����λ��ͼƬ�е�Y���� */
	protected int m_TextY;
	/** �����ַ��߶� */
	public int m_charH;
	/** �ַ�֮��ĺ����� */
	public int m_gapX;
	/** �ַ�֮��������� */
	protected int m_gapY;
	/** ͼƬ�ı�ÿ���ַ���λ��,���� = ӳ���ַ�������*2,(x,y) */
	protected short m_charPos[];
	/** ͼƬ����Ƭ�� */
	protected C2D_ImageClip m_chars[];

	/**
	 * ��ȡ�ַ�����λ��ͼƬ�е�X����
	 * 
	 * @return X����
	 */
	public int getTextX()
	{
		return m_TextX;
	}

	/**
	 * ��ȡ�ַ�����λ��ͼƬ�е�Y����
	 * 
	 * @return Y����
	 */
	public int getTextY()
	{
		return m_TextY;
	}

	/**
	 * ��ȡ�����ַ����
	 * 
	 * @return �ַ��߶�
	 */
	public int getCharH()
	{
		return m_charH;
	}

	/**
	 * ��ȡ�ַ�֮��ĺ�����
	 * 
	 * @return ���
	 */
	public int getGapX()
	{
		return m_gapX;
	}

	/**
	 * ��ȡ�ַ�֮���������
	 * 
	 * @return ���
	 */
	public int getGapY()
	{
		return m_gapY;
	}

	/**
	 * �����ַ�֮���������
	 * 
	 * @param gapX
	 *            �ַ�֮��ĺ�����
	 */
	public void setGapX(int gapX)
	{
		m_gapX = gapX;
	}

	/**
	 * �����ַ�֮���������
	 * 
	 * @param gapY
	 *            �ַ�֮���������
	 */
	public void setGapY(int gapY)
	{
		m_gapY = gapY;
	}

	/**
	 * ��ȡ��ǰ��ӳ���ַ���
	 * 
	 * @return ӳ���ַ���
	 */
	public String getChars()
	{
		return m_charTable;
	}

	/**
	 * ��ȡӳ���ַ�������
	 * 
	 * @return ӳ���ַ�������
	 */
	public int getCharCount()
	{
		if (m_charTable == null)
		{
			return 0;
		}
		return m_charTable.length();
	}

	/**
	 * ��ö�Ӧ�ַ��Ŀ��
	 * 
	 * @param id
	 *            �ַ�ID
	 * @return ���
	 */
	public abstract int getCharW(int id);

	/**
	 * ��������ַ����
	 * 
	 * @return �����ַ����
	 */
	public abstract int getCharWMax();

	/**
	 * ��ö�Ӧ�ַ����ڵ�λ��X����
	 * 
	 * @param id
	 *            �ַ�ID
	 * @return ���ڵ�λ��X����
	 */
	public abstract int getCharX(int id);

	/**
	 * ��ö�Ӧ�ַ����ڵ�λ��Y����
	 * 
	 * @param id
	 *            �ַ�ID
	 * @return ���ڵ�λ��Y����
	 */
	public abstract int getCharY(int id);

	/**
	 * ������ڻ����ַ���ͼƬ
	 * 
	 * @param id
	 *            �ַ�ͼƬid
	 * @return �ַ�ͼƬ
	 */
	public abstract C2D_Image getImage(int id);

	/**
	 * ����ÿ���ַ����ڵ�λ��
	 */
	protected void updatePositions()
	{
		if (m_chars != null)
		{
			for (int i = 0; i < m_chars.length; i++)
			{
				if (m_chars[i] != null)
				{
					m_chars[i].doRelease(this);
					m_chars[i] = null;
				}
			}
			m_chars = null;
		}
		m_chars = new C2D_ImageClip[m_charTable.length()];
		short x = (short) m_TextX;
		short y = (short) m_TextY;
		for (int i = 0; i < m_chars.length; i++)
		{
			m_chars[i] = new C2D_ImageClip(m_image);
			m_chars[i].transHadler(this);
			m_chars[i].setContentRect(x, y, getCharW(i), m_charH);
			m_chars[i].setShowSize(getCharW(i), m_charH);
			x += getCharW(i);
			if (x + getCharW(i) - 1 >= m_image.pixelWidth())
			{
				x = (short) m_TextX;
				y += m_charH;
			}
		}
	}

	/**
	 * ��õ����ַ�����ͼ�п�
	 * 
	 * @param id
	 *            �ַ�ID
	 * @return �����ַ�
	 */
	public C2D_ImageClip getChar(int id)
	{
		if (m_chars != null && id >= 0 && id < m_chars.length)
		{
			return m_chars[id];
		}
		return null;
	}

	public void onRelease()
	{
		m_charTable = null;
		if (m_image != null)
		{
			m_image.doRelease();
			m_image = null;
		}

		if (m_chars != null)
		{
			for (int i = 0; i < m_chars.length; i++)
			{
				if (m_chars[i] != null)
				{
					m_chars[i].doRelease(this);
					m_chars[i] = null;
				}
			}
			m_chars = null;
		}

	}
}