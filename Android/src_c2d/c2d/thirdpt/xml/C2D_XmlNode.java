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
 * XML节点类
 * @author AndrewFan
 *
 */
public class C2D_XmlNode implements C2D_Order
{
	private String m_name;//节点名称
	private C2D_Array m_atts;//属性
	private C2D_Array m_sons;//子节点列表
	public C2D_XmlNode(String name)
	{
		m_name=name;
	}
	/**
	 * 获取名称
	 * @return 当前节点名称
	 */
	public String getName()
	{
		return m_name;
	}
	/**
	 * 设置名称 
	 * @param name 当前节点名称
	 */
	public void setName(String name)
	{
		this.m_name = name;
	}
	/**
	 * 增加属性
	 * @param name 属性名称
	 * @param value 属性值
	 * @return 新添加的属性对象
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
	 * 获得指定属性对应的值列表长度
	 * @param key 属性名称
	 * @return 长度
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
	 * 寻找属性对象
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
	 * 增加属性
	 * @param name 属性名称
	 * @param retValue 属性值
	 * @return 新添加的节点
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
	 * 获得指定属性对应的首值
	 * @param name 属性名称
	 * @return 属性值
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
	 * 清除所有内容
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
	 * 解析内容
	 * 
	 * @param content
	 * @return 解析好的根节点对象
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
		
		boolean parsing = true;//判断是否到达最后一个document标签
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
				case C2D_Xml.START_DOCUMENT://START_DOCUMENT标签
					break;
				case C2D_Xml.COMMENT://COMMENT标签
					break;
				case C2D_Xml.DOCTYPE://DOCTYPE标签
					break;
				case C2D_Xml.ELEMENT://ELEMEN标签
					break;
				case C2D_Xml.END_DOCUMENT://最末标签
					parsing = false;
					break;
				case C2D_Xml.END_TAG://处理结束标签
					if(inContent)
					{
						if(readElementStep==1||readElementStep==0)//结束属性标签
						{
							currentNode.addAtt(eleName, eleValue);
							readElementStep=2;
						}
						else if(readElementStep==2)//结束子节点标签
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
				case C2D_Xml.PROCESSING_INSTRUCTION://处理PROCESSING_INSTRUCTION标签
					break;
				case C2D_Xml.START_TAG://处理开始标签
					if(!inContent)
					{
						if(rootName.equals(event.getName()))
						{
							inContent=true;
						}
					}
					else
					{
						if(readElementStep==0&&eleName!=null)//开启子节点
						{
							C2D_XmlNode newNode=currentNode.addSon(eleName);
							nodeStack.push(currentNode);
							currentNode=newNode;	
						}
						//开启新标签
						eleName=event.getName();
						eleValue=null;
						readElementStep=0;
					}
					break;
				case C2D_Xml.TEXT://处理TEXT标签
					if(readElementStep==0)
					{
						eleValue=event.getText();
						readElementStep=1;
					}
					break;
				case C2D_Xml.WHITESPACE://处理WHITESPACE标签
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
