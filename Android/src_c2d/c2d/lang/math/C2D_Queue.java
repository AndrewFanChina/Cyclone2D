package c2d.lang.math;

/**
 * 队列
 * @author AndrewFan
 *
 */
public class C2D_Queue
{
	private int m_maxSize;
	private Object[] m_queArray;
	private int m_front;
	private int m_rear;
	private int m_nItems;
	private boolean m_allowCover;

	// --------------------------------------------------------------
	public C2D_Queue(int p_maxSize) // constructor
	{
		this(p_maxSize, false);
	}

	public C2D_Queue(int p_maxSize, boolean p_allowCover) // constructor
	{
		m_maxSize = p_maxSize;
		m_queArray = new Object[m_maxSize];
		m_front = 0;
		m_rear = -1;
		m_nItems = 0;
		m_allowCover = p_allowCover;
	}

	// --------------------------------------------------------------
	// 在队列尾部插入单元
	public boolean insert(Object j)
	{
		if (!m_allowCover && m_nItems + 1 > m_maxSize)
		{
			return false;
		}
		// 尾部回绕处理
		if (m_rear == m_maxSize - 1)
		{
			m_rear = -1;
		}
		m_queArray[++m_rear] = j;

		if (m_nItems < m_maxSize)
		{
			m_nItems++;
		}
		return true;
	}

	// 从头部移除单元
	public Object remove()
	{
		Object temp = m_queArray[m_front++];
		// 头部回绕处理
		if (m_front == m_maxSize)
		{
			m_front = 0;
		}
		m_nItems--;
		return temp;
	}

	// 获得头部单元
	public Object peekFront()
	{
		return m_queArray[m_front];
	}

	public boolean isEmpty() // true if queue is empty
	{
		return (m_nItems == 0);
	}

	public boolean isFull() // true if queue is full
	{
		return (m_nItems >= m_maxSize);
	}

	public int size() // number of items in queue
	{
		return m_nItems;
	}
}
