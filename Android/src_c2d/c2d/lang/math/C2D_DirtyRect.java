package c2d.lang.math;

import c2d.lang.math.type.C2D_RectS;

/**
 * 脏矩形类，将一块大的矩形模型（m_modelWidth*m_modelHeight），
 * 分割成多块相同尺寸(m_cellW*m_cellH)的标记单元，接下来，在动画
 * 变化过程中，向标记单元标记脏掉的区域，当所有动画播放完毕，对标记
 * 单元进行联合处理，产生不重叠的一系列脏矩形结果单元，最后，让这些
 * 脏矩形结果单元中的控件进行重绘处理。
 * @author AndrewFan
 *
 */
public class C2D_DirtyRect
{
	/** 脏矩形模型宽度 */
	private int m_modelWidth=-1;
	/** 脏矩形模型高度 */
	private int m_modelHeight=-1;
	/** 标记单元尺寸宽度 */
	protected int m_cellW;
	/** 标记单元尺寸高度 */
	protected int m_cellH;
	/** 横向标记单元个数 */
	protected int m_NumX;
	/** 纵向标记单元个数 */
	protected int m_NumY;
	/** 标记单元 */
	protected boolean[][] m_cells;
	/** 计算结果单元缓冲，存放每个脏矩形结果 */
	private C2D_RectS[] m_resultsBuffer;
	/** 计算结果单元，最终发现的脏矩形区域的引用存放在此. */
	private C2D_Array m_results=new C2D_Array();
	/**
	 * 构造函数
	 * @param width 	  模型总宽度
	 * @param height	  模型总高度
	 * @param cellWidth	  标记单元宽度
	 * @param cellHeight 标记单元高度
	 */
	public C2D_DirtyRect(int width,int height,int cellWidth,int cellHeight)
	{
		setModel(width, height, cellWidth, cellHeight);
	}
	/**
	 * 设置模型，包括模型尺寸以及单元尺寸
	 * @param width 	  模型总宽度
	 * @param height	  模型总高度
	 * @param cellWidth	  标记单元宽度
	 * @param cellHeight 标记单元高度
	 * @return 是否进行了设置
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
	 * 重置脏矩形标记单元
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
	 * 搜索左上角下标到右下角下标之间的所有标记单元并且试图联合.
	 */
	public C2D_Array connectCells()
	{
		return connectCells(0, 0, m_NumX-1, m_NumY-1);
	}
	/**
	 * 搜索下标(drX0、drY0)到下标(drX1、drY1)之间的所有标记单元并且试图联合.
	 * 
	 * @param drX0
	 *            int 左上角X坐标
	 * @param drY0
	 *            int 左上角Y坐标
	 * @param drX1
	 *            int 右下角X坐标
	 * @param drY1
	 *            int 右下角Y坐标
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
				// 寻找以i、j为左上角的最大矩形
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
					// 开始横向查找
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
				// 加入到脏矩形列表
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
				// 去掉当前大矩形内所有单元的脏标志
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
	 * 接受脏掉的区域，这个函数负责将脏矩形规格化，即分散到预设的方格内标记。.
	 * 
	 * @param x
	 *            int 左上角X坐标
	 * @param y
	 *            int 左上角Y坐标
	 * @param width
	 *            int 脏矩形宽度
	 * @param height
	 *            int 脏矩形高度
	 * @return boolean 返回报告区域是否位于当前缓冲内，位于缓冲外部则不需要更新。
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
