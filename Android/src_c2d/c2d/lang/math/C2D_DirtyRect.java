package c2d.lang.math;

import c2d.lang.math.type.C2D_RectS;

/**
 * ������࣬��һ���ľ���ģ�ͣ�m_modelWidth*m_modelHeight����
 * �ָ�ɶ����ͬ�ߴ�(m_cellW*m_cellH)�ı�ǵ�Ԫ�����������ڶ���
 * �仯�����У����ǵ�Ԫ�����������򣬵����ж���������ϣ��Ա��
 * ��Ԫ�������ϴ����������ص���һϵ������ν����Ԫ���������Щ
 * ����ν����Ԫ�еĿؼ������ػ洦��
 * @author AndrewFan
 *
 */
public class C2D_DirtyRect
{
	/** �����ģ�Ϳ�� */
	private int m_modelWidth=-1;
	/** �����ģ�͸߶� */
	private int m_modelHeight=-1;
	/** ��ǵ�Ԫ�ߴ��� */
	protected int m_cellW;
	/** ��ǵ�Ԫ�ߴ�߶� */
	protected int m_cellH;
	/** �����ǵ�Ԫ���� */
	protected int m_NumX;
	/** �����ǵ�Ԫ���� */
	protected int m_NumY;
	/** ��ǵ�Ԫ */
	protected boolean[][] m_cells;
	/** ��������Ԫ���壬���ÿ������ν�� */
	private C2D_RectS[] m_resultsBuffer;
	/** ��������Ԫ�����շ��ֵ��������������ô���ڴ�. */
	private C2D_Array m_results=new C2D_Array();
	/**
	 * ���캯��
	 * @param width 	  ģ���ܿ��
	 * @param height	  ģ���ܸ߶�
	 * @param cellWidth	  ��ǵ�Ԫ���
	 * @param cellHeight ��ǵ�Ԫ�߶�
	 */
	public C2D_DirtyRect(int width,int height,int cellWidth,int cellHeight)
	{
		setModel(width, height, cellWidth, cellHeight);
	}
	/**
	 * ����ģ�ͣ�����ģ�ͳߴ��Լ���Ԫ�ߴ�
	 * @param width 	  ģ���ܿ��
	 * @param height	  ģ���ܸ߶�
	 * @param cellWidth	  ��ǵ�Ԫ���
	 * @param cellHeight ��ǵ�Ԫ�߶�
	 * @return �Ƿ����������
	 */
	public boolean setModel(int width,int height,int cellWidth,int cellHeight)
	{
		if(width<0||height<0||cellWidth<2||cellHeight<2)
		{
			return false;
		}
		if(width!=m_modelWidth||height!=m_modelHeight||cellWidth!=cellHeight||cellHeight!=m_modelHeight)
		{
			m_modelWidth=width;
			m_modelHeight=height;

			m_cellW=cellWidth;
			m_cellH=cellHeight;
			int numX = (m_modelWidth + m_cellW - 1) / m_cellW;
			int numY = (m_modelHeight + m_cellH - 1) / m_cellH;
			if(numX!=m_NumX||numY!=m_NumY||m_cells==null)
			{
				m_NumX=numX;
				m_NumY=numY;
				m_cells = new boolean[m_NumX][m_NumY];
			}
			resetDirtyCells();
			if(m_resultsBuffer==null)
			{
				m_resultsBuffer=new C2D_RectS[40];
				for(int i=0;i<m_resultsBuffer.length;i++)
				{
					m_resultsBuffer[i]=new C2D_RectS();
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * ��������α�ǵ�Ԫ
	 */
	public void resetDirtyCells()
	{
		if (m_cells != null)
		{
			for (int i = 0; i < m_cells.length; i++)
			{
				for (int j = 0; j < m_cells[i].length; j++)
				{
					m_cells[i][j] = false;
				}
			}
		}
	}
	/**
	 * �������Ͻ��±굽���½��±�֮������б�ǵ�Ԫ������ͼ����.
	 */
	public C2D_Array connectCells()
	{
		return connectCells(0, 0, m_NumX-1, m_NumY-1);
	}
	/**
	 * �����±�(drX0��drY0)���±�(drX1��drY1)֮������б�ǵ�Ԫ������ͼ����.
	 * 
	 * @param drX0
	 *            int ���Ͻ�X����
	 * @param drY0
	 *            int ���Ͻ�Y����
	 * @param drX1
	 *            int ���½�X����
	 * @param drY1
	 *            int ���½�Y����
	 */
	public C2D_Array connectCells(int drX0, int drY0, int drX1, int drY1)
	{
		m_results.removeAllElements();
		drX0 = c2d.lang.math.C2D_Math.limitNumber(drX0, 0, m_NumX - 1);
		drX1 = c2d.lang.math.C2D_Math.limitNumber(drX1, 0, m_NumX - 1);
		drY0 = c2d.lang.math.C2D_Math.limitNumber(drY0, 0, m_NumY - 1);
		drY1 = c2d.lang.math.C2D_Math.limitNumber(drY1, 0, m_NumY - 1);
		int i, j, k, m;
		int xs, ys, xe, ye ;
		int xStart,xEnd,yStart,yEnd;
		for (j = drY0; j <= drY1; j++)
		{

			for (i = drX0; i <= drX1; i++)
			{
				// Ѱ����i��jΪ���Ͻǵ�������
				if (!(m_cells[i][j]))
				{
					continue;
				}
				xStart = i;
				xEnd = i;
				yStart = j;
				yEnd = j;
				boolean firstLine = true;
				while (true)
				{
					// ��ʼ�������
					xStart = i - 1;
					while (xStart + 1 <= drX1
							&& m_cells[xStart + 1][yStart])
					{
						xStart++;
					}
					if (firstLine)
					{
						xEnd = xStart;
						firstLine = false;
					}
					else if (xStart < xEnd)
					{
						yEnd = yStart - 1;
						break;
					}
					if (yStart + 1 <= drY1)
					{
						yStart++;
					}
					else
					{
						yEnd = yStart;
						break;
					}
				}
				// ���뵽������б�
				int resultLen = m_results.size();
				if ( resultLen>= m_resultsBuffer.length)
				{
					C2D_RectS []t_DirtyRects=new C2D_RectS[resultLen*2];
					for(int tI=0;tI<t_DirtyRects.length;tI++)
					{
						t_DirtyRects[tI]=new C2D_RectS();
					}
					System.arraycopy(m_resultsBuffer, 0, t_DirtyRects, 0, resultLen);
					for(int tI=0;tI<m_resultsBuffer.length;tI++)
					{
						m_resultsBuffer[tI]=null;
					}
					m_resultsBuffer=null;
					m_resultsBuffer=t_DirtyRects;
				}
				xs = i * m_cellW;
				ys = j * m_cellH;
				xe = (xEnd + 1) * m_cellW;
				ye = (yEnd + 1) * m_cellW;
				m_resultsBuffer[resultLen].m_x = (short)(xs);
				m_resultsBuffer[resultLen].m_y = (short)(ys);
				m_resultsBuffer[resultLen].m_width = (short)(xe-xs);
				m_resultsBuffer[resultLen].m_height = (short)(ye-ys);
				m_results.addElement(m_resultsBuffer[resultLen]);
				// ȥ����ǰ����������е�Ԫ�����־
				for (m = j; m <= yEnd; m++)
				{
					for (k = i; k <= xEnd; k++)
					{
						m_cells[k][m] = false;
					}
				}
			}
		}
		return m_results;
	}

	/**
	 * ����������������������������ι�񻯣�����ɢ��Ԥ��ķ����ڱ�ǡ�.
	 * 
	 * @param x
	 *            int ���Ͻ�X����
	 * @param y
	 *            int ���Ͻ�Y����
	 * @param width
	 *            int ����ο��
	 * @param height
	 *            int ����θ߶�
	 * @return boolean ���ر��������Ƿ�λ�ڵ�ǰ�����ڣ�λ�ڻ����ⲿ����Ҫ���¡�
	 */
	public boolean receiveRegion(int x, int y, int width, int height)
	{
		int xr=x+width;
		int yb=y+height;
		if (xr < 0 || x > m_modelWidth || y > m_modelHeight || yb < 0 || width<=0 || height<=0)
		{
			return false;
		}
		int startX, endX, startY, endY;
		startX = (int) C2D_Math.max(0, x);
		endX = (int) C2D_Math.min(m_modelWidth, xr);
		startY = (int) C2D_Math.max(0, y);
		endY = (int) C2D_Math.min(m_modelHeight, yb);
		startX/=m_cellW;
		endX= endX/m_cellW;
		startY/=m_cellH;
		endY= endY/m_cellH;
		
		startX=C2D_Math.limitNumber(startX, 0, m_NumX-1);
		endX=C2D_Math.limitNumber(endX, 0, m_NumX-1);
		startY=C2D_Math.limitNumber(startY, 0, m_NumY-1);
		endY=C2D_Math.limitNumber(endY, 0, m_NumY-1);
		for (int i = startX; i <= endX; i++)
		{
			for (int j = startY; j <= endY; j++)
			{
				m_cells[i][j] = true;
			}
		}
		return true;
	}
}
