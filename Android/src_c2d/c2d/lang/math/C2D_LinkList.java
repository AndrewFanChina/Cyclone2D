package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 链表
 * @author AndrewFan
 *
 */
public class C2D_LinkList
{
	private C2D_LinkNode m_firstNode;// 链表头节点
	private C2D_LinkNode m_lastNode; // 链表尾节点
	private int m_length = 0;

	// 获取链表的长度
	public int size()
	{
		return m_length;
	}

	// 获取元素
	public Object getElementAt(int index)
	{
		C2D_LinkNode node = getNode(index);
		if (node != null)
		{
			return node.getData();
		}
		return null;
	}

	// 增加元素
	public void add(Object object)
	{
		if (m_firstNode == null)
		{
			m_firstNode = new C2D_LinkNode(object);
			m_lastNode = m_firstNode;
			m_length = 1;
			return;
		}
		// 增加到链表结尾
		C2D_LinkNode endNode = getLast();
		C2D_LinkNode nextNodeNew = new C2D_LinkNode(object);
		endNode.setNext(nextNodeNew);
		m_lastNode = nextNodeNew;
		m_length++;
	}

	// 清空
	public void clear()
	{
		m_firstNode = null;
		m_lastNode = null;
		m_length = 0;
	}

	// 插入元素
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
		// 代表应该插入头节点
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

	// 查找链表结尾
	public C2D_LinkNode getLast()
	{
		return m_lastNode;
	}

	// 显示自己
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

	// 获取节点
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
	private Object obj;// 节点数据
	private C2D_LinkNode nextNode;// 后一个节点
	// private DS_LinkNode preNode;//前一个节点

	public C2D_LinkNode(Object obj)
	{
		this.obj = obj;
	}

	// 获取后一个节点
	public C2D_LinkNode getNext()
	{
		return nextNode;
	}

	// 设置后一个节点
	public void setNext(C2D_LinkNode nextNode)
	{
		this.nextNode = nextNode;
	}

	// //获取前一个节点
	// public DS_LinkNode getPre()
	// {
	// return preNode;
	// }
	// //设置前一个节点
	// public void setPre(DS_LinkNode nextNode)
	// {
	// this.preNode=nextNode;
	// }
	// 获取数据
	public Object getData()
	{
		return obj;
	}

}
