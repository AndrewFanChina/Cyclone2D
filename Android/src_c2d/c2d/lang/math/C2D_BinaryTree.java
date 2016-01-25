package c2d.lang.math;

import java.util.Stack;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 二叉树
 * 
 * @author AndrewFan
 * 
 */
public class C2D_BinaryTree
{
	private C2D_BinaryNode m_root; // first node of tree

	/**
	 * 二叉树
	 */
	public C2D_BinaryTree() // constructor
	{
		m_root = null;
	}

	/**
	 * 根据指定的键返回节点
	 * 
	 * @param key
	 *            键
	 * @return 节点
	 */
	public C2D_BinaryNode find(int key) // find node with given key
	{ // (assumes non-empty tree)
		C2D_BinaryNode current = m_root; // start at root
		while (current.m_key != key) // while no match,
		{
			if (key < current.m_key) // go left?
				current = current.m_leftChild;
			else
				// or go right?
				current = current.m_rightChild;
			if (current == null) // if no child,
				return null; // didn't find it
		}
		return current; // found it
	} // end find()

	/**
	 * 根据指定ID插入数据
	 * 
	 * @param id
	 *            指定ID
	 * @param data
	 *            要插入的数据
	 */
	public void insert(int id, Object data)
	{
		C2D_BinaryNode newNode = new C2D_BinaryNode(); // make new node
		newNode.m_key = id; // insert data
		newNode.m_dData = data;
		if (m_root == null) // no node in root
		{
			m_root = newNode;

		}
		else
		// root occupied
		{
			C2D_BinaryNode current = m_root; // start at root
			C2D_BinaryNode parent;
			while (true) // (exits internally)
			{
				parent = current;
				if (id < current.m_key) // go left?
				{
					current = current.m_leftChild;
					if (current == null) // if end of the line,
					{ // insert on left
						parent.m_leftChild = newNode;
						return;
					}
				} // end if go left
				else
				// or go right?
				{
					current = current.m_rightChild;
					if (current == null) // if end of the line
					{ // insert on right
						parent.m_rightChild = newNode;
						return;
					}
				} // end else go right
			} // end while
		} // end else not root
	} // end insert()
		// -------------------------------------------------------------

	/**
	 * 根据某个键删除对应的节点
	 * 
	 * @param key 对应节点的键
	 * @return 是否成功删除
	 */
	public boolean delete(int key)
	{
		C2D_BinaryNode current = m_root;
		C2D_BinaryNode parent = m_root;
		boolean isLeftChild = true;

		while (current.m_key != key) // 查找指定的键
		{
			parent = current;
			if (key < current.m_key) // go left?
			{
				isLeftChild = true;
				current = current.m_leftChild;
			}
			else
			{
				isLeftChild = false;
				current = current.m_rightChild;
			}
			if (current == null)
			{
				return false; // 没有找到
			}
		}
		// 如果没有子节点，直接删除掉
		if (current.m_leftChild == null && current.m_rightChild == null)
		{
			if (current == m_root)
			{
				m_root = null;
			}
			else if (isLeftChild)
			{
				parent.m_leftChild = null;
			}
			else
			{
				parent.m_rightChild = null;// from parent
			}
		}
		// 如果没有右节点，使用左子树去替换
		else if (current.m_rightChild == null)
		{
			if (current == m_root)
			{
				m_root = current.m_leftChild;
			}
			else if (isLeftChild)
			{
				parent.m_leftChild = current.m_leftChild;
			}
			else
			{
				parent.m_rightChild = current.m_leftChild;
			}
		}
		// 如果没有左节点，使用右子树去替换
		else if (current.m_leftChild == null)
		{
			if (current == m_root)
			{
				m_root = current.m_rightChild;
			}
			else if (isLeftChild)
			{
				parent.m_leftChild = current.m_rightChild;
			}
			else
			{
				parent.m_rightChild = current.m_rightChild;
			}
		}
		else
		// 两个子节点都存在，使用中序后继去替换
		{
			// 查找当前节点的中序后继(找到时已经调整了其右子节点关系)
			C2D_BinaryNode successor = getSuccessor(current);

			// 连接父节点到继任节点
			if (current == m_root)
			{
				m_root = successor;
			}
			else if (isLeftChild)
			{
				parent.m_leftChild = successor;
			}
			else
			{
				parent.m_rightChild = successor;
			}

			// 将继任节点的左节点连接到当前的左节点
			successor.m_leftChild = current.m_leftChild;
		}
		return true; // success
	}

	/**
	 * 找到即将删除的节点的后继节点 返回删除当前节点后大于此节点的最接近的节点，即先转到右子节点，然后一直找到它的左值节点末端。
	 * 找到节点后，会将其提升到删除节点的位置，调整其右子节点的关系，左子节点以及父节点关系尚未调整
	 * 
	 * @param delNode
	 *            即将删除的节点
	 * @return
	 */
	private C2D_BinaryNode getSuccessor(C2D_BinaryNode delNode)
	{
		C2D_BinaryNode successorParent = delNode;
		C2D_BinaryNode successor = delNode;
		C2D_BinaryNode current = delNode.m_rightChild; // go to right child
		while (current != null) // until no more
		{ // left children,
			successorParent = successor;
			successor = current;
			current = current.m_leftChild; // go to left child
		}
		// if successor not
		if (successor != delNode.m_rightChild)
		{ // make connections
			successorParent.m_leftChild = successor.m_rightChild;
			successor.m_rightChild = delNode.m_rightChild;
		}
		return successor;
	}

	/**
	 * 遍历二叉树
	 * 
	 * @param traverseType
	 *            遍历顺序，即左子节点、右子节点和本节点（中）的遍历顺序， 1代表做中->左->右，2代表左->中->右，3代表右->左->中
	 */
	public void traverse(int traverseType)
	{
		switch (traverseType)
		{
		case 1:
			C2D_Debug.logChunk("\nPreorder traversal: ");
			preOrder(m_root);
			break;
		case 2:
			C2D_Debug.logChunk("\nInorder traversal:  ");
			inOrder(m_root);
			break;
		case 3:
			C2D_Debug.logChunk("\nPostorder traversal: ");
			postOrder(m_root);
			break;
		}
		C2D_Debug.log("");
	}

	private void preOrder(C2D_BinaryNode localRoot)
	{
		if (localRoot != null)
		{
			C2D_Debug.logChunk(localRoot.m_key + " ");
			preOrder(localRoot.m_leftChild);
			preOrder(localRoot.m_rightChild);
		}
	}

	private void inOrder(C2D_BinaryNode localRoot)
	{
		if (localRoot != null)
		{
			inOrder(localRoot.m_leftChild);
			C2D_Debug.logChunk(localRoot.m_key + " ");
			inOrder(localRoot.m_rightChild);
		}
	}

	private void postOrder(C2D_BinaryNode localRoot)
	{
		if (localRoot != null)
		{
			postOrder(localRoot.m_leftChild);
			postOrder(localRoot.m_rightChild);
			C2D_Debug.logChunk(localRoot.m_key + " ");
		}
	}

	/**
	 * 显示二叉树
	 */
	public void displayTree()
	{
		Stack globalStack = new Stack();
		globalStack.push(m_root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		C2D_Debug.log("......................................................");
		while (isRowEmpty == false)
		{
			Stack localStack = new Stack();
			isRowEmpty = true;

			for (int j = 0; j < nBlanks; j++)
				C2D_Debug.logChunk(" ");

			while (globalStack.isEmpty() == false)
			{
				C2D_BinaryNode temp = (C2D_BinaryNode) globalStack.pop();
				if (temp != null)
				{
					localStack.push(temp.m_leftChild);
					localStack.push(temp.m_rightChild);

					if (temp.m_leftChild != null || temp.m_rightChild != null)
						isRowEmpty = false;
				}
				else
				{
					localStack.push(null);
					localStack.push(null);
				}
				for (int j = 0; j < nBlanks * 2 - 2; j++)
				{
					C2D_Debug.logChunk(" ");
				}
			} // end while globalStack not empty
			C2D_Debug.log("");
			nBlanks /= 2;
			while (localStack.isEmpty() == false)
			{
				globalStack.push(localStack.pop());
			}
		} // end while isRowEmpty is false
		C2D_Debug.log("......................................................");
	}
}