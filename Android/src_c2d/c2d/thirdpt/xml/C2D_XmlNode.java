package c2d.thirdpt.xml;

import c2d.lang.io.C2D_StringReader;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;
import c2d.lang.math.C2D_Stack;
import c2d.lang.util.debug.C2D_Debug;
import c2d.thirdpt.xml.kxml.C2D_Xml;
import c2d.thirdpt.xml.kxml.parser.C2D_ParseEvent;
import c2d.thirdpt.xml.kxml.parser.C2D_XmlParser;
/**
 * XML�ڵ���
 * @author AndrewFan
 *
 */
public class C2D_XmlNode implements C2D_Order
{
	private String m_name;//�ڵ�����
	private C2D_Array m_atts;//����
	private C2D_Array m_sons;//�ӽڵ��б�
	public C2D_XmlNode(String name)
	{
		m_name=name;
	}
	/**
	 * ��ȡ����
	 * @return ��ǰ�ڵ�����
	 */
	public String getName()
	{
		return m_name;
	}
	/**
	 * �������� 
	 * @param name ��ǰ�ڵ�����
	 */
	public void setName(String name)
	{
		this.m_name = name;
	}
	/**
	 * ��������
	 * @param name ��������
	 * @param value ����ֵ
	 * @return ����ӵ����Զ���
	 */
	public C2D_XmlAtt addAtt(String name,String value)
	{
		if(m_atts==null)
		{
			m_atts=new C2D_Array();
		}
		C2D_XmlAtt att = getAtt(name);
		if(att==null)
		{
			att=new C2D_XmlAtt(name);
			m_atts.addElement(att);
		}
		att.addValue(value);
		return att;
	}
	/**
	 * ���ָ�����Զ�Ӧ��ֵ�б���
	 * @param key ��������
	 * @return ����
	 */
	public int getAtt_Len(String key)
	{
		C2D_XmlAtt att = getAtt(key);
		if(att==null)
		{
			return 0;
		}
		return att.getValueLen();
	}
	
	/**
	 * Ѱ�����Զ���
	 * @param name
	 * @return
	 */
	public C2D_XmlAtt getAtt(String name)
	{
		if(m_atts==null||name==null)
		{
			return null;
		}
		for (int i = 0; i < m_atts.size(); i++)
		{
			Object o = m_atts.elementAt(i);
			if(o!=null && o instanceof C2D_XmlAtt)
			{
				C2D_XmlAtt att=(C2D_XmlAtt)o;
				if(name.equals(att.m_name))
				{
					return att;
				}
			}
		}
		return null;
	}
	
	/**
	 * ��������
	 * @param name ��������
	 * @param retValue ����ֵ
	 * @return ����ӵĽڵ�
	 */
	public C2D_XmlNode addSon(String name)
	{
		if(m_sons==null)
		{
			m_sons=new C2D_Array();
		}
		C2D_XmlNode son = getSon(name);
		if(son==null)
		{
			son=new C2D_XmlNode(name);
			m_sons.addElement(son);
		}
		return son;
	}
	/**
	 * ���ָ�����Զ�Ӧ����ֵ
	 * @param name ��������
	 * @return ����ֵ
	 */
	public C2D_XmlNode getSon(String name)
	{
		if(m_sons==null||name==null)
		{
			return null;
		}
		for (int i = 0; i < m_sons.size(); i++)
		{
			Object o =m_sons.elementAt(i);
			if(o!=null && o instanceof C2D_XmlNode)
			{
				C2D_XmlNode node=(C2D_XmlNode)o;
				if(name.equals(node.m_name))
				{
					return node;
				}
			}
		}
		return null;
	}
	/**
	 * �����������
	 */
	public void clear()
	{
		if(m_atts!=null)
		{
			for (int i = 0; i < m_atts.size(); i++)
			{
				Object o =m_atts.elementAt(i);
				if(o!=null && o instanceof C2D_XmlAtt)
				{
					C2D_XmlAtt att=(C2D_XmlAtt)o;
					att.clear();
				}
			}
			m_atts.clear();
		}
		if(m_sons!=null)
		{
			for (int i = 0; i < m_sons.size(); i++)
			{
				Object o =m_sons.elementAt(i);
				if(o!=null && o instanceof C2D_XmlNode)
				{
					C2D_XmlNode node=(C2D_XmlNode)o;
					node.clear();
				}
			}
			m_sons.clear();
		}
	}

	/**
	 * ��������
	 * 
	 * @param content
	 * @return �����õĸ��ڵ����
	 */
	public static C2D_XmlNode parseXML(String content,String rootName)
	{
		if(rootName==null||content==null)
		{
			return null;
		}
		C2D_XmlNode m_rootNode=new C2D_XmlNode(rootName);
		C2D_XmlParser parser = null;
		C2D_StringReader sr=null;
		
		boolean parsing = true;//�ж��Ƿ񵽴����һ��document��ǩ
		try
		{
			
			sr = new C2D_StringReader(content);
			parser = new C2D_XmlParser(sr);
			String eleName=null;
			String eleValue=null;
			boolean inContent=false;
			int readElementStep=2;
			C2D_XmlNode currentNode=m_rootNode;
			C2D_Stack nodeStack=new C2D_Stack();
			while (parsing)
			{
				C2D_ParseEvent event = parser.read();
				if(event==null)
				{
					break;
				}
				switch (event.getType())
				{
				case C2D_Xml.START_DOCUMENT://START_DOCUMENT��ǩ
					break;
				case C2D_Xml.COMMENT://COMMENT��ǩ
					break;
				case C2D_Xml.DOCTYPE://DOCTYPE��ǩ
					break;
				case C2D_Xml.ELEMENT://ELEMEN��ǩ
					break;
				case C2D_Xml.END_DOCUMENT://��ĩ��ǩ
					parsing = false;
					break;
				case C2D_Xml.END_TAG://���������ǩ
					if(inContent)
					{
						if(readElementStep==1||readElementStep==0)//�������Ա�ǩ
						{
							currentNode.addAtt(eleName, eleValue);
							readElementStep=2;
						}
						else if(readElementStep==2)//�����ӽڵ��ǩ
						{
							if(nodeStack.getSize()>0)
							{
								currentNode=(C2D_XmlNode)nodeStack.pop();	
							}
						}
						
						eleName=null;
						eleValue=null;
						
						if(nodeStack.getSize()==0 && rootName.equals(event.getName()))
						{
							parsing = false;
						}
					}
					break;
				case C2D_Xml.PROCESSING_INSTRUCTION://����PROCESSING_INSTRUCTION��ǩ
					break;
				case C2D_Xml.START_TAG://����ʼ��ǩ
					if(!inContent)
					{
						if(rootName.equals(event.getName()))
						{
							inContent=true;
						}
					}
					else
					{
						if(readElementStep==0&&eleName!=null)//�����ӽڵ�
						{
							C2D_XmlNode newNode=currentNode.addSon(eleName);
							nodeStack.push(currentNode);
							currentNode=newNode;	
						}
						//�����±�ǩ
						eleName=event.getName();
						eleValue=null;
						readElementStep=0;
					}
					break;
				case C2D_Xml.TEXT://����TEXT��ǩ
					if(readElementStep==0)
					{
						eleValue=event.getText();
						readElementStep=1;
					}
					break;
				case C2D_Xml.WHITESPACE://����WHITESPACE��ǩ
					break;
				}
			}
		}
		catch (Exception e)
		{
			if(e!=null)
			{
				C2D_Debug.log(e.getMessage());
			}
		}
		finally
		{
			if(sr!=null)
			{
				sr.close();
			}
		}
		parser=null;
		sr=null;
		return m_rootNode;
	}
	public int getOrderValue(int orderType)
	{
		if(m_name==null||m_name.length()==0)
		{
			return 0;
		}
		return m_name.length();
	}
}
