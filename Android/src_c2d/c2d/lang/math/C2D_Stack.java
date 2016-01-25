package c2d.lang.math;


/**
 * 栈结构
 * @author AndrewFan
 *
 */
public class C2D_Stack
{
	private C2D_SortableArray m_stack=new C2D_SortableArray();//建立内部数据存储
	
	//压栈操作
	public void push(C2D_Order obj)
	{
		m_stack.addElement(obj);
	}
	//弹出操作
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
	//查看栈顶值
	public Object peek()
	{
		int last=m_stack.size()-1;
		if(last<0)
		{
			return null;
		}
		return m_stack.elementAt(last);
	}
	//检测栈是否为空
	public boolean isEmpty()
	{
		return m_stack.size()==0;
	}
	/**
	 * 返回栈当前大小
	 * @return 栈当前大小
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
