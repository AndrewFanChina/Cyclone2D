package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 二叉节点类
 * 
 * @author AndrewFan
 * 
 */
public class C2D_BinaryNode
{
	/**
	 * 节点名称
	 */
	public String name;
	/**
	 * 键，用来标记节点的位置权值
	 */
	public int m_key; 
	/**
	 * 节点数据
	 */
	public Object m_dData;
	/**
	 * 左子节点
	 */
	public C2D_BinaryNode m_leftChild;
	/**
	 * 右子节点
	 */
	public C2D_BinaryNode m_rightChild;
	/**
	 * 显示节点
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