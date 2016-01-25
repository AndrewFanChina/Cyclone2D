package c2d.frame.com.text;

import java.io.DataInputStream;

import c2d.lang.io.C2D_IOUtil;
import c2d.mod.C2D_Consts;
import c2d.plat.gfx.C2D_Image;

/**
 * ��̬��ȵ�ͼƬ�ı���������(Picture Text Configuration of Dynamic Width )
 * ���������Ϣ��Ҫ��C2DͼƬ�����ɹ����е�������Ϣ����һ��C2D_ImageͼƬ��
 * һ��ӳ���ַ������Լ���صĻ�����Ϣ����Щ��Ϣ����ָ���˴���ͼƬ�ϻ��Ƶ��������
 * (charX,charY)��ָ�������ַ��Ŀ��(CharW)������߶�(CharH)����ʾ��
 * ��֮��ļ��(Gap)�ȡ�
 * @author AndrewFan
 *
 */
public class C2D_PTC_DynW extends C2D_PTC
{
	/** �ַ�����б� */
	private short m_charWs[];
	/** ����ַ����*/
	private short m_charWMax;
	/**
	 * ��ö�Ӧ�ַ��Ŀ��
	 * @param id �ַ�ID
	 * @return ���
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
	 * ��������ַ����
	 * @return �����ַ����
	 */
	public int getCharWMax()
	{
		return m_charWMax;
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
	 * ��".picf"��ͼƬ�ֿ��ļ�����
	 * @param fileName λ��fntsĿ¼�µ��ļ�,����·���ͺ�׺��
	 */
	public void loadFromPTLib(String fileName)
	{
		loadFromPTLib(fileName,C2D_Consts.STR_Fonts);
	}
	/**
	 * ��".picf"��ͼƬ�ֿ��ļ�����
	 * @param fileName λ��ָ���ļ����µ��ļ�,����·���ͺ�׺��
	 * @param folderName ָ���ļ���������
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
