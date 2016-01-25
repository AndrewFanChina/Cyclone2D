package c2d.frame.com.text;

import c2d.plat.gfx.C2D_Image;

/**
 * �ȿ��ͼƬ�ı���������(Picture Text Configuration of Average Width )
 * ����Ҫָ��һ��ͼƬC2D_Image��һ��ӳ���ַ�����ָ������ͼƬ�ϻ��Ƶ�
 * �������(charX,charY)��ָ�������ַ��Ŀ��(CharW)������߶�(CharH)��
 * ��ʾ�ַ�֮��ļ��(Gap)��������ʾ��λ��(LeastNum)����Щ�������Ե���ָ����Ҳ
 * ����ʹ��setParametersһ�����á�
 * ע����ͼƬ���ֲ�ͬ��������ַ�����ӳ��ͼ�������Զ��У���һ�в�����
 * �������е��ı�ʱ����ȡ��ͼƬ��β����������һ��(textX,textY+=CharH)����
 * ��ȡ��
 * @author AndrewFan
 *
 */
public class C2D_PTC_AvgW extends C2D_PTC
{
	/** �����ַ���� */
	private int m_charW;
	/**
	 * ��ö�Ӧ�ַ��Ŀ��
	 * @param id �ַ�ID
	 * @return ���
	 */
	public int getCharW(int id)
	{
		return m_charW;
	}
	/**
	 * ��������ַ����
	 * @return �����ַ����
	 */
	public int getCharWMax()
	{
		return m_charW;
	}
	/**
	 * ��ö�Ӧ�ַ����ڵ�λ��X����
	 * @param id �ַ�ID
	 * @return ���ڵ�λ��X����
	 */
	public int getCharX(int id)
	{
		int idT=id<<1;
		if(id<0||m_charPos==null||idT>=m_charPos.length)
		{
			return 0;
		}
		return m_charPos[idT];
	}
	/**
	 * ��ö�Ӧ�ַ����ڵ�λ��Y����
	 * @param id �ַ�ID
	 * @return ���ڵ�λ��Y����
	 */
	public int getCharY(int id)
	{
		int idT=(id<<1)+1;
		if(id<0||m_charPos==null||idT>=m_charPos.length)
		{
			return 0;
		}
		return m_charPos[idT];
	}
	/**
	 * ������ڻ����ַ���ͼƬ
	 * @param id �ַ�ͼƬid
	 * @return �ַ�ͼƬ
	 */
	public C2D_Image getImage(int id)
	{
		return m_image;
	}
	/**
	 * ����ͼƬ�ֵ����в���
	 * @param image    �ַ�ͼƬ
	 * @param textX    �ַ�����λ��ͼƬ�е�X����
	 * @param textY    �ַ�����λ��ͼƬ�е�Y����
	 * @param charW    ���ַ����
	 * @param charH    ���ַ��߶�
	 * @param gapX     �ַ�������
	 * @param gapY     �ַ�������
	 * @param charTable ӳ���ַ���
	 */
	public void setParameters(C2D_Image image,int textX,int textY,
			int charW,int charH,int gapX,int gapY,String charTable)
	{
		if(charW<0 || charH <0|| charTable == null)
		{
			return;
		}
		if(m_image!=null)
		{
			m_image.doRelease(this);
			m_image=null;
		}
		m_image = image;
		m_TextX = textX;
		m_TextY = textY;
		m_charW = charW;
		m_charH = charH;
		m_gapX = gapX;
		m_gapY = gapY;
		m_charTable = charTable;
		updatePositions();
	}
	/**
	 * ����ÿ���ַ����ڵ�λ��
	 */
	protected void updatePositions()
	{
		m_charPos=new short[m_charTable.length()*2];
		short x=(short)m_TextX;
		short y=(short)m_TextY;
		for (int i = 0; i < m_charPos.length; )
		{
			m_charPos[i++]=x;
			m_charPos[i++]=y;
			x+=m_charW;
			if(x+m_charW-1>=m_image.pixelWidth())
			{
				x=(short)m_TextX;
				y+=m_charH;
			}
		}
		super.updatePositions();
	}
	public void onRelease()
	{
		super.onRelease();
		m_charTable =null;
		if(m_image!=null)
		{
			m_image.doRelease(this);
			m_image= null;
		}
		m_charPos=null;
	}
}
