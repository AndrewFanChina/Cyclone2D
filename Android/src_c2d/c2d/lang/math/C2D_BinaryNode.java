package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ����ڵ���
 * 
 * @author AndrewFan
 * 
 */
public class C2D_BinaryNode
{
	/**
	 * �ڵ�����
	 */
	public String name;
	/**
	 * ����������ǽڵ��λ��Ȩֵ
	 */
	public int m_key; 
	/**
	 * �ڵ�����
	 */
	public Object m_dData;
	/**
	 * ���ӽڵ�
	 */
	public C2D_BinaryNode m_leftChild;
	/**
	 * ���ӽڵ�
	 */
	public C2D_BinaryNode m_rightChild;
	/**
	 * ��ʾ�ڵ�
	 */
	public void displayNode()
	{
		C2D_Debug.logChunk("{");
		C2D_Debug.logChunk(""+m_key);
		C2D_Debug.logChunk(", ");
		C2D_Debug.logChunk(""+m_dData);
		C2D_Debug.log("} ");
	}
} // end class Node