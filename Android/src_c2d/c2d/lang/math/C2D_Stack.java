package c2d.lang.math;


/**
 * ջ�ṹ
 * @author AndrewFan
 *
 */
public class C2D_Stack
{
	private C2D_SortableArray m_stack=new C2D_SortableArray();//�����ڲ����ݴ洢
	
	//ѹջ����
	public void push(C2D_Order obj)
	{
		m_stack.addElement(obj);
	}
	//��������
	public Object pop()
	{
		int last=m_stack.size()-1;
		if(last<0)
		{
			return null;
		}
		Object obj=m_stack.elementAt(last);
		m_stack.removeElementAt(last);
		return obj;
	}
	//�鿴ջ��ֵ
	public Object peek()
	{
		int last=m_stack.size()-1;
		if(last<0)
		{
			return null;
		}
		return m_stack.elementAt(last);
	}
	//���ջ�Ƿ�Ϊ��
	public boolean isEmpty()
	{
		return m_stack.size()==0;
	}
	/**
	 * ����ջ��ǰ��С
	 * @return ջ��ǰ��С
	 */
	public int getSize()
	{
		return m_stack.m_length;
	}
	public void clear()
	{
		if(m_stack!=null)
		{
			m_stack.clear();	
		}
	}
}
