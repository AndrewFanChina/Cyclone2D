package c2d.frame.com.text;

import c2d.plat.gfx.C2D_Image;

/**
 * 等宽的图片文本的配置类(Picture Text Configuration of Average Width )
 * 你需要指定一个图片C2D_Image和一个映射字符串，指定从这图片上绘制的
 * 起点坐标(charX,charY)，指定单个字符的宽度(CharW)和整体高度(CharH)，
 * 显示字符之间的间隔(Gap)，最少显示的位数(LeastNum)。这些参数可以单独指定，也
 * 可以使用setParameters一起设置。
 * 注意与图片数字不同，这里的字符换的映射图可以来自多行，当一行不足以
 * 包含所有的文本时，读取到图片的尾部，将从下一行(textX,textY+=CharH)继续
 * 读取。
 * @author AndrewFan
 *
 */
public class C2D_PTC_AvgW extends C2D_PTC
{
	/** 单个字符宽度 */
	private int m_charW;
	/**
	 * 获得对应字符的宽度
	 * @param id 字符ID
	 * @return 宽度
	 */
	public int getCharW(int id)
	{
		return m_charW;
	}
	/**
	 * 获得最大的字符宽度
	 * @return 最大的字符宽度
	 */
	public int getCharWMax()
	{
		return m_charW;
	}
	/**
	 * 获得对应字符所在的位置X坐标
	 * @param id 字符ID
	 * @return 所在的位置X坐标
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
	 * 获得对应字符所在的位置Y坐标
	 * @param id 字符ID
	 * @return 所在的位置Y坐标
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
	 * 获得用于绘制字符的图片
	 * @param id 字符图片id
	 * @return 字符图片
	 */
	public C2D_Image getImage(int id)
	{
		return m_image;
	}
	/**
	 * 设置图片字的所有参数
	 * @param image    字符图片
	 * @param textX    字符序列位于图片中的X坐标
	 * @param textY    字符序列位于图片中的Y坐标
	 * @param charW    单字符宽度
	 * @param charH    单字符高度
	 * @param gapX     字符横向间距
	 * @param gapY     字符纵向间距
	 * @param charTable 映射字符串
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
	 * 更新每个字符所在的位置
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
