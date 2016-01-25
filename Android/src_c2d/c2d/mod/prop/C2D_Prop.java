package c2d.mod.prop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;
import c2d.mod.C2D_Consts;


/**
 * ����ʵ����
 *
 */
public class C2D_Prop extends C2D_Object
{
	
	/** The style. */
	C2D_PropStyle style = null;// ʵ�������������Ը�ʽ
	
	/** The v properties. */
	private Vector vProperties;// ʵ��������
	
	/** The name. */
	public String name = null;
	
	/** The original id. */
	private int originalID = -1;// ԭʼID�����ڱ����¡���ID��ʧ

	/**
	 * Instantiates a new prop instance.
	 *
	 * @param styleT the style t
	 */
	public C2D_Prop(C2D_PropStyle styleT)
	{
		style = styleT;
	}

	// ��ñ���ID
	/**
	 * Gets the iD.
	 *
	 * @return the iD
	 */
	public int getID()
	{
		int id = -1;
		if (style != null)
		{
			id = style.vPropInstances.indexOf(this);
		}
		if (id < 0)
		{
			id = originalID;
		}
		return id;
	}

	// Actor��ָͬ��ͬһ������ʱ�����ܻ�������⡣
	/**
	 * Clone me.
	 *
	 * @return the prop instance
	 */
	public C2D_Prop cloneMe()
	{
		C2D_Prop newInstance = new C2D_Prop(style);
		newInstance.name = name + "";
		newInstance.vProperties = new Vector();
		newInstance.originalID = getID();
		int intData = 0;
		String strData = null;
		for (int i = 0; i < newInstance.style.byteStyle.length; i++)
		{
			int formatI = newInstance.style.byteStyle[i];
			if (formatI >= C2D_Consts.PARAM_PROP)
			{
				formatI = C2D_Consts.PARAM_PROP;
			}
			switch (formatI)
			{
			case C2D_Consts.PARAM_INT:
			case C2D_Consts.PARAM_INT_VAR:
			case C2D_Consts.PARAM_STR_VAR:
			case C2D_Consts.PARAM_INT_ID:
			intData = ((Integer) (vProperties.elementAt(i))).intValue();
			newInstance.vProperties.addElement(new Integer(intData));
			break;
			case C2D_Consts.PARAM_STR:
			strData = ((String) (vProperties.elementAt(i))) + "";
			newInstance.vProperties.addElement(strData);
			break;
			case C2D_Consts.PARAM_PROP:
			C2D_Prop pInstance = (C2D_Prop) (vProperties.elementAt(i));
			if (pInstance != null)
			{
				// ȡ��PRO�Ŀ�¡
				newInstance.vProperties.addElement(pInstance.cloneMe());
			}
			else
			{
				newInstance.vProperties.addElement(null);
			}
			break;
			}
		}
		return newInstance;
	}

	/**
	 * Read object.
	 *
	 * @param dataIn the data in
	 * @throws Exception the exception
	 */
	public void readObject(DataInputStream dataIn) throws Exception
	{
		name = C2D_IOUtil.readString(name, dataIn);
		vProperties = new Vector();
		readParamsByFormats(vProperties, dataIn, style.byteStyle);
	}

	/**
	 * Write object.
	 *
	 * @param dos the dos
	 * @throws Exception the exception
	 */
	public void writeObject(DataOutputStream dos) throws Exception
	{
		C2D_IOUtil.writeString(name, dos);
		if (vProperties == null || dos == null || style.byteStyle == null)
		{
			return;
		}
		int intData = 0;
		String strData = null;
		// short id=0;
		for (int i = 0; i < style.byteStyle.length; i++)
		{
			int formatI = style.byteStyle[i];
			if (formatI >= C2D_Consts.PARAM_PROP)
			{
				// id=(short)(formatI-PARAM_PROP-1);
				formatI = C2D_Consts.PARAM_PROP;
			}
			switch (formatI)
			{
			case C2D_Consts.PARAM_INT:
			case C2D_Consts.PARAM_INT_VAR:
			case C2D_Consts.PARAM_STR_VAR:
			case C2D_Consts.PARAM_INT_ID:
			intData = ((Integer) vProperties.elementAt(i)).intValue();
			C2D_IOUtil.writeInt(intData, dos);
			break;
			case C2D_Consts.PARAM_STR:
			strData = (String) vProperties.elementAt(i);
			C2D_IOUtil.writeString(strData, dos);
			break;
			case C2D_Consts.PARAM_PROP:
			C2D_Prop pInstance = (C2D_Prop) vProperties.elementAt(i);
			if (pInstance == null)
			{
				intData = -1;
			}
			else
			{
				intData = pInstance.getID();
			}
			C2D_IOUtil.writeInt(intData, dos);
			break;
			}
		}
	}

	/**
	 * Read params by formats.
	 *
	 * @param vector the vector
	 * @param s the s
	 * @param format the format
	 * @throws Exception the exception
	 * @todo ���ݸ�ʽ���������
	 */
	private void readParamsByFormats(Vector vector, DataInputStream s, byte format[]) throws Exception
	{
		if (vector == null || s == null || format == null)
		{
			return;
		}
		int intData = 0;
		String strData = null;
		short id = 0;
		for (int i = 0; i < format.length; i++)
		{
			int formatI = format[i];
			if (formatI >= C2D_Consts.PARAM_PROP)
			{
				id = (short) (formatI - C2D_Consts.PARAM_PROP - 1);
				formatI = C2D_Consts.PARAM_PROP;
			}
			switch (formatI)
			{
			case C2D_Consts.PARAM_INT:
			case C2D_Consts.PARAM_INT_VAR:
			case C2D_Consts.PARAM_STR_VAR:
			case C2D_Consts.PARAM_INT_ID:
			intData = C2D_IOUtil.readInt(intData, s);
			vector.addElement(new Integer(intData));
			break;
			case C2D_Consts.PARAM_STR:
			strData = C2D_IOUtil.readString(strData, s);
			vector.addElement(strData);
			break;
			case C2D_Consts.PARAM_PROP:// �������һ��PropInstance����id��������ID��intdata����ʵ��ID����λ
			intData = C2D_IOUtil.readInt(intData, s);
			C2D_Prop pInstance = style.propertiesManager.getProp(id, intData);
			vector.addElement(pInstance);
			break;
			}
		}
	}

	/**
	 * ��������
	 *
	 * @param pId the id
	 * @param newValue the new value
	 * @return true, if successful
	 */
	public boolean setProperty(int pId, int newValue)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return false;
		}
		if (style.byteStyle[pId] != C2D_Consts.PARAM_INT && style.byteStyle[pId] != C2D_Consts.PARAM_INT_ID)
		{
			return false;
		}
		vProperties.removeElementAt(pId);
		Integer valueObj = new Integer(newValue);
		vProperties.insertElementAt(valueObj, pId);
		return true;
	}

	/**
	 * Sets the property.
	 *
	 * @param pId the id
	 * @param newValue the new value
	 * @return true, if successful
	 */
	public boolean setProperty(int pId, String newValue)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return false;
		}
		if (style.byteStyle[pId] != C2D_Consts.PARAM_STR && style.byteStyle[pId] != C2D_Consts.PARAM_STR_VAR)
		{
			return false;
		}
		vProperties.removeElementAt(pId);
		vProperties.insertElementAt(newValue, pId);
		return true;
	}

	/**
	 * ��������
	 *
	 * @param pId the id
	 * @param prop the prop
	 * @return true, if successful
	 */
	public boolean setProperty(int pId, C2D_Prop prop)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return false;
		}
		if (style.byteStyle[pId] < C2D_Consts.PARAM_PROP)
		{
			return false;
		}
		vProperties.setElementAt(prop, pId);
		return true;
	}

	/**
	 * ���INT����,��������õ�ֵ
	 *
	 * @param pId the id
	 * @return the int property
	 */
	public int getInt(int pId)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return -1;
		}
		if (style.byteStyle[pId] != C2D_Consts.PARAM_INT && style.byteStyle[pId] != C2D_Consts.PARAM_INT_ID)
		{
			return -1;
		}
		Integer objValue = (Integer) vProperties.elementAt(pId);
		return objValue.intValue();
	}

	/**
	 * Gets the string property.
	 *
	 * @param pId the id
	 * @return the string property
	 */
	public String getString(int pId)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return null;
		}
		if (style.byteStyle[pId] != C2D_Consts.PARAM_STR)
		{
			return null;
		}
		String stringValue = (String) vProperties.elementAt(pId);
		return stringValue;
	}

	/**
	 * Gets the instance property.
	 *
	 * @param pId the id
	 * @return the instance property
	 */
	public C2D_Prop getProp(int pId)
	{
		if (vProperties == null || pId < 0 || pId >= vProperties.size() || style == null)
		{
			return null;
		}
		if (style.byteStyle[pId] < C2D_Consts.PARAM_PROP)
		{
			return null;
		}
		C2D_Prop instanceValue = (C2D_Prop) vProperties.elementAt(pId);
		return instanceValue;
	}

	/**
	 *  ������Գ���
	 *
	 * @return the property len
	 * 
	 */
	public int getLen()
	{
		if (vProperties == null)
		{
			return -1;
		}
		return vProperties.size();
	}

	/**
	 * Release res.
	 *
	 * @todo ��ʾ����
	 */
	/*
	public void showProperty()
	{
	if (vProperties == null || vProperties.size() == 0)
	{
		MiscUtil.println("no property");
		return;
	}
	MiscUtil.println("-------------- property list --------------");
	for (int i = 0; i < vProperties.size(); i++)
	{
		Object obj = vProperties.elementAt(i);
		if (obj instanceof Integer)
		{
			MiscUtil.println("PROP[" + i + "]=>[int]:" + ((Integer) obj).intValue());
		}
		if (obj instanceof String)
		{
			MiscUtil.println("PROP[" + i + "]=>[str]:" + (String) obj);
		}
		if (obj instanceof PropInstance)
		{
			MiscUtil.println("PROP[" + i + "]=>[prop]:" + ((PropInstance) obj).name);
		}

	}

	}
	*/
	/** ж����Դ */
	public void onRelease()
	{
		style = null;
		if (vProperties != null)
		{
			vProperties.removeAllElements();
			vProperties = null;
		}
		name = null;
	}
}
