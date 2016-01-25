package c2d.frame.com.text;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Image;

/**
 * 动态宽度的图片文本的配置类(Picture Text Configuration of Dynamic Width )
 * 这个配置信息需要从C2D图片字生成工具中导出，信息包括一个C2D_Image图片、
 * 一个映射字符串，以及相关的绘制信息，这些信息包括指定了从这图片上绘制的起点坐标
 * (charX,charY)，指定单个字符的宽度(CharW)和整体高度(CharH)，显示字
 * 符之间的间隔(Gap)等。
 * @author AndrewFan
 *
 */
public class C2D_PTC_DynW extends C2D_PTC
{
	/** 字符宽度列表 */
	private short m_charWs[];
	/** 最大字符宽度*/
	private short m_charWMax;
	/**
	 * 获得对应字符的宽度
	 * @param id 字符ID
	 * @return 宽度
	 */
	public int getCharW(int id)
	{
		if(id<0||m_charWs==null||id>=m_charWs.length)
		{
			return 0;
		}
		return m_charWs[id];
	}
	/**
	 * 获得最大的字符宽度
	 * @return 最大的字符宽度
	 */
	public int getCharWMax()
	{
		return m_charWMax;
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
	 * 从".picf"的图片字库文件加载
	 * @param fileName 位于fnts目录下的文件,不含路径和后缀名
	 */
	public void loadFromPTLib(String fileName)
	{
		loadFromPTLib(fileName,C2D_Consts.STR_Fonts);
	}
	/**
	 * 从".picf"的图片字库文件加载
	 * @param fileName 位于指定文件夹下的文件,不含路径和后缀名
	 * @param folderName 指定文件夹下名称
	 */
	public void loadFromPTLib(String fileName,String folderName)
	{
		String filePath=C2D_Consts.STR_RES+folderName
				+fileName+C2D_Consts.STR_PIC_FNT;
		DataInputStream dis = C2D_IOUtil.getDataInputStream(filePath);
		if(dis==null)
		{
			return;
		}
		try
		{
			m_charTable = C2D_IOUtil.readString(m_charTable, dis);
			m_charH=C2D_IOUtil.readShort((short)m_charH, dis);
			m_gapX=C2D_IOUtil.readShort((short)m_gapX, dis);
			m_gapY=C2D_IOUtil.readShort((short)m_gapY, dis);
			String imgName=null;
			imgName =  C2D_IOUtil.readString(imgName, dis);
			int len=m_charTable.length();
			m_charPos=new short[len*2];
			m_charWMax=0;
			m_charWs=new short[len];
			short dataT=0;
			for (int i = 0; i < len; i++)
			{
				m_charPos[i<<1]=C2D_IOUtil.readShort(dataT, dis);
				m_charPos[(i<<1)+1]=C2D_IOUtil.readShort(dataT, dis);
				m_charWs[i]=C2D_IOUtil.readShort(dataT, dis);
				if(m_charWMax<m_charWs[i])
				{
					m_charWMax=m_charWs[i];
				}
			}
			if(imgName==null)
			{
				return;
			}
			m_image=C2D_Image.createImage(fileName+C2D_Consts.STR_IMG_PNG, folderName);
			m_image.transHadler(this);;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
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
