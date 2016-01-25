package c2d.lang.math;

/**
 * ����
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
	// �ڶ���β�����뵥Ԫ
	public boolean insert(Object j)
	{
		if (!m_allowCover && m_nItems + 1 > m_maxSize)
		{
			return false;
		}
		// β�����ƴ���
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

	// ��ͷ���Ƴ���Ԫ
	public Object remove()
	{
		Object temp = m_queArray[m_front++];
		// ͷ�����ƴ���
		if (m_front == m_maxSize)
		{
			m_front = 0;
		}
		m_nItems--;
		return temp;
	}

	// ���ͷ����Ԫ
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
