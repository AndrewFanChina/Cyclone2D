package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 可以排序的动态整形数字数组
 * 
 * @author AndrewFan
 * 
 */
public class C2D_SortableArrayL
{
	private int m_capacity;// 容量(初始值是10)
	public long m_datas[];// 内部数组
	private int m_length;// 存放的元素个数

	public C2D_SortableArrayL()
	{
		m_capacity = 10;
		m_datas = new long[m_capacity];
	}

	public C2D_SortableArrayL(int capacity)
	{
		m_capacity = capacity;
		m_datas = new long[m_capacity];
	}

	/**
	 * 克隆
	 */
	public C2D_SortableArrayL cloneSelf()
	{
		C2D_SortableArrayL newInstance = new C2D_SortableArrayL(m_capacity);
		newInstance.m_length = m_length;
		System.arraycopy(m_datas, 0, newInstance.m_datas, 0, m_datas.length);
		return newInstance;
	}

	/**
	 * 冒泡排序(从小到大)
	 */
	public void bubbleSort()
	{
		if (m_datas == null)
		{
			return;
		}
		long currentMin, dataJ;
		for (int i = 0; i < m_length; i++)
		{
			currentMin = m_datas[i];
			for (int j = i + 1; j < m_length; j++)
			{
				dataJ = m_datas[j];
				if (currentMin > dataJ)
				{
					m_datas[j] = currentMin;
					m_datas[i] = dataJ;
					currentMin = dataJ;
				}
			}
		}
	}

	/**
	 * 插入排序(从小到大)
	 */
	public void insertSort()
	{
		long dataI, dataC;
		int left, right, center, newCenter;
		long dataIHash, dataCHash;
		for (int i = 1; i < m_length; i++)
		{
			dataI = m_datas[i];
			dataIHash = dataI;
			// 先查找插入的位置
			left = 0;
			right = i;
			center = (left + right) / 2;
			while (true)
			{
				dataC = m_datas[center];
				dataCHash = dataC;
				if (dataCHash > dataIHash)
				{
					right = center;
					newCenter = (left + right) / 2;
					if (newCenter == right || newCenter <= left)
					{
						break;
					}
					center = newCenter;
				}
				else if (dataCHash < dataIHash)
				{
					left = center;
					newCenter = (left + right) / 2;
					if (newCenter == left || newCenter >= right)
					{
						center = right;
						break;
					}
					center = newCenter;
				}
				else
				{
					break;
				}
			}
			if (center != i)
			{
				System.arraycopy(m_datas, center, m_datas, center + 1, i - center);
				// 插入当前数值
				m_datas[center] = dataI;
			}
		}
	}

	/**
	 * 归并排序(从小到大)
	 */
	public void mergeSort()
	{ // provides workspace
		long[] workSpace = new long[m_length];
		recMergeSort(workSpace, 0, m_length - 1);
	}

	private void recMergeSort(long[] workSpace, int lowerBound, int upperBound)
	{
		if (lowerBound == upperBound) // if range is 1,
		{
			return; // no use sorting
		}
		else
		{ // find midpoint
			int mid = (lowerBound + upperBound) / 2;
			// sort low half
			recMergeSort(workSpace, lowerBound, mid);
			// sort high half
			recMergeSort(workSpace, mid + 1, upperBound);
			// merge them
			merge(workSpace, lowerBound, mid + 1, upperBound);
		} // end else
	} // end recMergeSort()

	private void merge(long[] workSpace, int lowPtr, int highPtr, int upperBound)
	{
		int j = 0; // workspace index
		int lowerBound = lowPtr;
		int mid = highPtr - 1;
		int n = upperBound - lowerBound + 1; // # of items

		while (lowPtr <= mid && highPtr <= upperBound)
		{
			if (m_datas[lowPtr] < m_datas[highPtr])
			{
				workSpace[j++] = m_datas[lowPtr++];
			}
			else
			{
				workSpace[j++] = m_datas[highPtr++];
			}
		}
		while (lowPtr <= mid)
		{
			workSpace[j++] = m_datas[lowPtr++];
		}
		while (highPtr <= upperBound)
		{
			workSpace[j++] = m_datas[highPtr++];
		}
		System.arraycopy(workSpace, 0, m_datas, lowerBound, n);
	}

	/**
	 * 希尔排序(从小到大)
	 */
	public void shellSort()
	{
		int h = 1; // find initial value of h
		while (h <= m_length / 3)
		{
			h = h * 3 + 1; // (1, 4, 13, 40, 121, ...)
		}

		long temp;

		while (h > 0) // 变换跨度，进行多轮插入排序
		{

			for (int iCirc = 0; iCirc < h; iCirc++)// 进行一组跨度为h的h次插入排序
			{

				for (int iSort = iCirc + h; iSort < m_length; iSort += h)// 进行第iCirc次插入排序
				{
					temp = m_datas[iSort];
					int iPos = iSort;
					while (iPos >= h && temp < m_datas[iPos - h])
					{
						m_datas[iPos] = m_datas[iPos - h];
						iPos -= h;
					}
					m_datas[iPos] = temp;
				}
			}
			h = (h - 1) / 3; // decrease h
		} // end while(h>0)
	} // end shellSort()

	/**
	 * 快速排序(从小到大)
	 */
	public void quickSort1()
	{
		recQuickSort1(0, m_length - 1);
	}

	// --------------------------------------------------------------
	private void recQuickSort1(int left, int right)
	{
		if (right - left <= 0) // if size <= 1,
			return; // already sorted
		else
		// size is 2 or larger
		{
			long pivot = m_datas[right]; // rightmost item
											// partition range
			int partition = partitionIt1(left, right, pivot);
			recQuickSort1(left, partition - 1); // sort left side
			recQuickSort1(partition + 1, right); // sort right side
		}
	} // end recQuickSort()

	private int partitionIt1(int left, int right, long pivot)
	{
		int leftPtr = left - 1; // left (after ++)
		int rightPtr = right; // right-1 (after --)
		long pivotCode = pivot;
		while (true)
		{ // find bigger item
			while (m_datas[++leftPtr] < pivotCode)
				; // (nop)
					// find smaller item
			while (rightPtr > 0 && m_datas[--rightPtr] > pivotCode)
				; // (nop)

			if (leftPtr >= rightPtr) // if pointers cross,
				break; // partition done
			else
				// not crossed, so
				swap(leftPtr, rightPtr); // swap elements
		} // end while(true)
		swap(leftPtr, right); // restore pivot
		return leftPtr; // return pivot location
	} // end partitionIt()

	private void swap(int dex1, int dex2) // swap two elements
	{
		long temp = m_datas[dex1]; // A into temp
		m_datas[dex1] = m_datas[dex2]; // B into A
		m_datas[dex2] = temp; // temp into B
	}

	/**
	 * 快速排序2(从小到大)
	 */
	public void quickSort2()
	{
		recQuickSort2(0, m_length - 1);
	}

	private void recQuickSort2(int left, int right)
	{
		int size = right - left + 1;
		if (size <= 3) // manual sort if small
			manualSort(left, right);
		else
		// quicksort if large
		{
			long median = medianOf3(left, right);
			int partition = partitionIt2(left, right, median);
			recQuickSort2(left, partition - 1);
			recQuickSort2(partition + 1, right);
		}
	}

	private long medianOf3(int left, int right)
	{
		int center = (left + right) / 2;
		// order left & center
		if (m_datas[left] > m_datas[center])
			swap(left, center);
		// order left & right
		if (m_datas[left] > m_datas[right])
			swap(left, right);
		// order center & right
		if (m_datas[center] > m_datas[right])
			swap(center, right);

		swap(center, right - 1); // put pivot on right
		return m_datas[right - 1]; // return median value
	} // end medianOf3()

	private int partitionIt2(int left, int right, long pivot)
	{
		int leftPtr = left; // right of first elem
		int rightPtr = right - 1; // left of pivot
		long pivotCode = pivot;

		while (true)
		{
			while (m_datas[++leftPtr] < pivotCode)
				// find bigger
				; // (nop)
			while (m_datas[--rightPtr] > pivotCode)
				// find smaller
				; // (nop)
			if (leftPtr >= rightPtr) // if pointers cross,
				break; // partition done
			else
				// not crossed, so
				swap(leftPtr, rightPtr); // swap elements
		} // end while(true)
		swap(leftPtr, right - 1); // restore pivot
		return leftPtr; // return pivot location
	} // end partitionIt()

	private void manualSort(int left, int right)
	{
		int size = right - left + 1;
		if (size <= 1)
			return; // no sort necessary
		if (size == 2)
		{ // 2-sort left and right
			if (m_datas[left] > m_datas[right])
				swap(left, right);
			return;
		}
		else
		// size is 3
		{ // 3-sort left, center, & right
			if (m_datas[left] > m_datas[right - 1])
				swap(left, right - 1); // left, center
			if (m_datas[left] > m_datas[right])
				swap(left, right); // left, right
			if (m_datas[right - 1] > m_datas[right])
				swap(right - 1, right); // center, right
		}
	} // end manualSort()

	/**
	 * 快速排序3(从小到大)
	 */
	public void quickSort3()
	{
		recQuickSort3(0, m_length - 1);
		// insertionSort(0, nElems-1); // the other option
	}

	// --------------------------------------------------------------
	private void recQuickSort3(int left, int right)
	{
		int size = right - left + 1;
		if (size < 10) // insertion sort if small
			insertionSort(left, right);
		else
		// quicksort if large
		{
			long median = medianOf3(left, right);
			int partition = partitionIt2(left, right, median);
			recQuickSort3(left, partition - 1);
			recQuickSort3(partition + 1, right);
		}
	} // end recQuickSort()

	private void insertionSort(int left, int right)
	{
		int in, out;
		// sorted on left of out
		for (out = left + 1; out <= right; out++)
		{
			long temp = m_datas[out]; // remove marked item
			in = out; // start shifts at out
						// until one is smaller,
			while (in > left && m_datas[in - 1] >= temp)
			{
				m_datas[in] = m_datas[in - 1]; // shift item to right
				--in; // go left one position
			}
			m_datas[in] = temp; // insert marked item
		} // end for
	} // end insertionSort()

	/**
	 * 增加元素
	 * 
	 * @param number
	 */
	public void add(int number)
	{
		increaseCapacity();
		// 赋值
		m_datas[m_length] = number;
		m_length++;
	}

	/**
	 * 插入元素
	 * 
	 * @param index
	 *            插入的位置
	 * @param number
	 *            插入的对象
	 * @return 返回是否成功插入
	 */
	public boolean insertElementAt(int index, int number)
	{
		if (index < 0)
		{
			return false;
		}
		if (index >= m_length)
		{
			add(number);
			return true;
		}
		increaseCapacity();
		// 向后拷贝
		// for(int i=length;i>index;i--)
		// {
		// datas[i]=datas[i-1];
		// }
		System.arraycopy(m_datas, index, m_datas, index + 1, m_length - index);

		m_datas[index] = number;

		m_length++;
		return true;
	}

	/**
	 * 增长容量
	 */
	private void increaseCapacity()
	{
		if (m_length >= m_capacity)
		{
			int newCapacity = m_capacity * 2;
			long datasNew[] = new long[newCapacity];
			System.arraycopy(m_datas, 0, datasNew, 0, m_length);
			m_datas = datasNew;
			m_capacity = newCapacity;
		}
	}

	/**
	 * 清空对象数组
	 */
	public void clear()
	{
		for (int i = 0; i < m_length; i++)
		{
			m_datas[i] = 0;
		}
		m_length = 0;
	}

	/**
	 * 是否包含某个对象
	 * 
	 * @param number
	 *            包含的对象
	 * @return 是否包含
	 */
	public boolean contains(int number)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i] == number)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 取位于某个下标的对象
	 * 
	 * @param index
	 *            对象下标
	 * @return 对应的对象
	 */
	public long elementAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return -1;
		}
		return m_datas[index];
	}

	/**
	 * 判断某个值位于当前Vector中的什么位置
	 * 
	 * @param number
	 *            要判断的对象
	 * @return 对象在当前数组中的位置
	 */
	public int indexOf(int number)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i] == number)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 判断当前Vector是否为空
	 * 
	 * @return Vector是否为空
	 */
	public boolean isEmpty()
	{
		return m_length == 0;
	}

	/**
	 * 移除指定位置的对象，后续对象会依次上移
	 * 
	 * @param index
	 * @return 是否成功移除
	 */
	public boolean removeAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return false;
		}

		for (int i = index; i <= m_length - 2; i++)
		{
			m_datas[i] = m_datas[i + 1];
		}
		m_length--;
		return true;
	}

	/**
	 * 删除某个对象
	 * 
	 * @param number
	 *            要删除的对象
	 * @return 是否成功删除
	 */
	public boolean remove(int number)
	{
		int index = indexOf(number);
		if (index < 0)
		{
			return false;
		}
		return removeAt(index);
	}

	/**
	 * 删除尾部对象
	 * 
	 * @return 是否成功删除
	 */
	public boolean removeEnd()
	{
		if (m_length <= 0)
		{
			return false;
		}
		m_datas[m_length - 1] = 0;
		m_length--;
		return true;
	}

	/**
	 * 在index位置重新设置对象
	 * 
	 * @param index
	 *            指定的下标
	 * @param number
	 *            重新设置的对象
	 * @return 是否成功设置
	 */
	public boolean set(int index, int number)
	{
		if (index < 0 || index >= m_length)
		{
			return false;
		}
		m_datas[index] = number;
		return true;
	}

	/**
	 * 获取大小
	 * 
	 * @return 大小
	 */
	public int size()
	{
		return m_length;
	}

	/**
	 * 显示此数组，每个对象之间以逗号分隔
	 */
	public void show()
	{
		for (int i = 0; i < m_length; i++)
		{
			long number = m_datas[i];
			C2D_Debug.logChunk(number + ",");
		}
		C2D_Debug.log("");
	}

	/**
	 * 显示此数组，每个对象一行
	 */
	public void showByLines()
	{
		for (int i = 0; i < m_length; i++)
		{
			long number = m_datas[i];
			C2D_Debug.log("" + number);
		}
	}

	/**
	 * 检查是否排序
	 */
	public void checkSortInfor()
	{
		if (isSorted())
		{
			C2D_Debug.log("Array is sorted!");
		}
		else
		{
			C2D_Debug.log("Array is not sorted!");
		}
	}

	/**
	 * 检测是否经过排序
	 * 
	 * @return 是否经过排序
	 */
	public boolean isSorted()
	{
		if (m_length <= 1)
		{
			return true;
		}
		if (m_datas[0] > m_datas[1])
		{
			int i = 1;
			while (i < m_length)
			{
				if (m_datas[i] > m_datas[i - 1])
				{
					return false;
				}
				i++;
			}
		}
		else
		{
			int i = 1;
			while (i < m_length)
			{
				if (m_datas[i] < m_datas[i - 1])
				{
					return false;
				}
				i++;
			}
		}
		return true;
	}
}
