package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ����
 * @author AndrewFan
 *
 */
public class C2D_LinkList
{
	private C2D_LinkNode m_firstNode;// ����ͷ�ڵ�
	private C2D_LinkNode m_lastNode; // ����β�ڵ�
	private int m_length = 0;

	// ��ȡ����ĳ���
	public int size()
	{
		return m_length;
	}

	// ��ȡԪ��
	public Object getElementAt(int index)
	{
		C2D_LinkNode node = getNode(index);
		if (node != null)
		{
			return node.getData();
		}
		return null;
	}

	// ����Ԫ��
	public void add(Object object)
	{
		if (m_firstNode == null)
		{
			m_firstNode = new C2D_LinkNode(object);
			m_lastNode = m_firstNode;
			m_length = 1;
			return;
		}
		// ���ӵ������β
		C2D_LinkNode endNode = getLast();
		C2D_LinkNode nextNodeNew = new C2D_LinkNode(object);
		endNode.setNext(nextNodeNew);
		m_lastNode = nextNodeNew;
		m_length++;
	}

	// ���
	public void clear()
	{
		m_firstNode = null;
		m_lastNode = null;
		m_length = 0;
	}

	// ����Ԫ��
	public boolean insertElementAt(int index, Object obj)
	{
		if (index < 0)
		{
			return false;
		}
		if (index >= m_length)
		{
			add(obj);
			return true;
		}
		C2D_LinkNode nodeCurrent = getNode(index - 1);
		C2D_LinkNode nextNodeNew = new C2D_LinkNode(obj);
		// ����Ӧ�ò���ͷ�ڵ�
		if (nodeCurrent == null)
		{
			nextNodeNew.setNext(m_firstNode);
			m_firstNode = nextNodeNew;
		}
		else
		{
			C2D_LinkNode nextNodeOld = nodeCurrent.getNext();
			nodeCurrent.setNext(nextNodeNew);
			nextNodeNew.setNext(nextNodeOld);
		}
		m_length++;
		return true;
	}

	// ���������β
	public C2D_LinkNode getLast()
	{
		return m_lastNode;
	}

	// ��ʾ�Լ�
	public void show()
	{
		C2D_LinkNode node = m_firstNode;
		while (node != null)
		{
			C2D_Debug.logChunk(node.getData().toString() + ",");
			node = node.getNext();
		}
		C2D_Debug.log("");
	}

	// ��ȡ�ڵ�
	private C2D_LinkNode getNode(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return null;
		}
		C2D_LinkNode node = m_firstNode;
		int id = 0;
		while (id < index)
		{
			node = node.getNext();
			id++;
		}
		return node;
	}
}

class C2D_LinkNode
{
	private Object obj;// �ڵ�����
	private C2D_LinkNode nextNode;// ��һ���ڵ�
	// private DS_LinkNode preNode;//ǰһ���ڵ�

	public C2D_LinkNode(Object obj)
	{
		this.obj = obj;
	}

	// ��ȡ��һ���ڵ�
	public C2D_LinkNode getNext()
	{
		return nextNode;
	}

	// ���ú�һ���ڵ�
	public void setNext(C2D_LinkNode nextNode)
	{
		this.nextNode = nextNode;
	}

	// //��ȡǰһ���ڵ�
	// public DS_LinkNode getPre()
	// {
	// return preNode;
	// }
	// //����ǰһ���ڵ�
	// public void setPre(DS_LinkNode nextNode)
	// {
	// this.preNode=nextNode;
	// }
	// ��ȡ����
	public Object getData()
	{
		return obj;
	}

}
