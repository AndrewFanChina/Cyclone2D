package c2d.lang.math.type;

import c2d.lang.util.debug.C2D_Debug;
import android.view.MotionEvent;

public class C2D_TouchData
{
	/** 触点状态，标记第n个触点是否处于按下状态 */
	public boolean m_touchStates[] = new boolean[5];
	/** 触点原始坐标 */
	public C2D_PointF m_touchPoints[] = new C2D_PointF[]
	{ new C2D_PointF(), new C2D_PointF(), new C2D_PointF(), new C2D_PointF(), new C2D_PointF() };
	/** 上一次触点事件 */
	public int m_lastAction = -1;
	/** 按下的触点个数 */
	public int m_touchCount;
	/** 是否当前触点全部失效 */
	private boolean m_invalid = false;
	/** 新数据到达次数 */
	private int m_dataReachTime = 0;

	/**
	 * 从另外一个手势数据对象拷贝数据
	 * 
	 * @param data
	 *            另外一个手势数据对象
	 */
	private void copyFrom(C2D_TouchData data)
	{
		m_lastAction = data.m_lastAction;
		m_touchCount = data.m_touchCount;
		for (int i = 0; i < m_touchStates.length; i++)
		{
			m_touchStates[i] = data.m_touchStates[i];
		}
		for (int i = 0; i < m_touchPoints.length; i++)
		{
			m_touchPoints[i].setValue(data.m_touchPoints[i].m_x, data.m_touchPoints[i].m_y);
		}
	}

	/**
	 * 激活触点信息或者使其失效，如果失效，将直到接收到新的触点按下事件，才会将其重新激活
	 * 
	 * @param invalid
	 */
	public void setInvalid(boolean invalid)
	{
		if (invalid == m_invalid)
		{
			return;
		}
		m_invalid = invalid;
		if (m_invalid)
		{
			m_lastAction = -1;
			m_touchCount = 0;
			for (int i = 0; i < m_touchStates.length; i++)
			{
				m_touchStates[i] = false;
			}
		}
	}

	/**
	 * 标记获取到新的数据
	 */
	public void incNewDataTime()
	{
		m_dataReachTime++;
	}

	public void updateFrom(C2D_TouchData m_newTouchData)
	{
		if (!m_invalid && m_newTouchData.m_dataReachTime > 0)
		{
			int dataRechTime = m_newTouchData.m_dataReachTime;
			copyFrom(m_newTouchData);
			m_newTouchData.m_dataReachTime -= dataRechTime;
			// printInf("cur touch");
		}
	}

	public boolean isPressedAction()
	{
		return m_lastAction == MotionEvent.ACTION_DOWN;
	}

	public void printInf(String flag)
	{
		String inf = flag + "invalid:" + m_invalid + " count:" + m_touchCount;
		for (int i = 0; i < m_touchPoints.length; i++)
		{
			if (m_touchStates[i])
			{
				inf += " [" + i + "](" + m_touchPoints[i].m_x + "," + m_touchPoints[i].m_y + ")";
			}
		}
		C2D_Debug.log(inf);
	}

	public int getFirstTouchID()
	{
		for (int i = 0; i < m_touchPoints.length; i++)
		{
			if (m_touchStates[i])
			{
				return i;
			}
		}
		return -1;
	}

	public C2D_PointF getTouchPoint(int id)
	{
		if(id>=0&&id<m_touchPoints.length&&m_touchStates[id])
		{
			return m_touchPoints[id];
		}
		return null;
	}
}
