package c2d.mod.prop;

import java.io.DataInputStream;
import java.util.Vector;

import c2d.lang.io.C2D_IOUtil;
import c2d.lang.obj.C2D_Object;


/**
 * ���Է���
 */
public class C2D_PropStyle extends C2D_Object
{
	
	/** The properties manager. */
	C2D_PropManager propertiesManager;

	/**
	 * Instantiates a new prop instance style.
	 *
	 * @param propertiesManagerT the properties manager t
	 */
	public C2D_PropStyle(C2D_PropManager propertiesManagerT)
	{
		propertiesManager = propertiesManagerT;
	}

	/** The byte style. */
	public byte byteStyle[] = null; // ���Ը�ʽ
	
	/** The v prop instances. */
	public Vector vPropInstances = null;

	/**
	 * Read object init.
	 *
	 * @param s the s
	 * @throws Exception the exception
	 */
	public void readObjectInit(DataInputStream s) throws Exception
	{
		// �������Ը�ʽ
		short len = 0;
		byte byteData = 0;
		len = C2D_IOUtil.readShort(len, s);
		byteStyle = new byte[len];
		for (int i = 0; i < byteStyle.length; i++)
		{
			byteStyle[i] = C2D_IOUtil.readByte(byteData, s);
		}
		// ����ʵ������
		len = C2D_IOUtil.readShort(len, s);
		vPropInstances = new Vector();
		for (int i = 0; i < len; i++)
		{
			C2D_Prop propertyInstance = new C2D_Prop(this);
			vPropInstances.addElement(propertyInstance);
		}
	}

	/**
	 * Read object.
	 *
	 * @param s the s
	 * @throws Exception the exception
	 */
	public void readObject(DataInputStream s) throws Exception
	{
		for (int i = 0; i < vPropInstances.size(); i++)
		{
			C2D_Prop propertyInstance = (C2D_Prop) vPropInstances.elementAt(i);
			propertyInstance.readObject(s);
		}
	}

	/**
	 * ж����Դ
	 *
	 */
	public void onRelease()
	{
		byteStyle = null;
		if (vPropInstances != null)
		{
			for (int i = 0; i < vPropInstances.size(); i++)
			{
				C2D_Prop property = (C2D_Prop) (vPropInstances.elementAt(i));
				property.doRelease();
			}
		}
	}
}
