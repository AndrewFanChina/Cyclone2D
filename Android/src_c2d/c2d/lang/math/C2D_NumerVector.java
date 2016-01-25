package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ��̬��������
 * @author AndrewFan
 *
 */
public class C2D_NumerVector
{
	private int capacity = 10;// ����(��ʼֵ��10)
	private int datas[] = new int[capacity];// �ڲ�����
	private int length = 0;// ��ŵ�Ԫ�ظ���
	/**
	 * ��¡
	 */
	public C2D_NumerVector cloneSelf()
	{
		C2D_NumerVector newInstance = new C2D_NumerVector();
		newInstance.capacity = capacity;
		newInstance.length = length;
		newInstance.datas = new int[datas.length];
		System.arraycopy(datas, 0, newInstance.datas, 0, datas.length);
		return newInstance;
	}

	/**
	 * ð������(��С����)
	 */
	public void bubbleSort()
	{
		if (datas == null)
		{
			return;
		}
		int currentMin, dataJ;
		for (int i = 0; i < length; i++)
		{
			currentMin = datas[i];
			for (int j = i + 1; j < length; j++)
			{
				dataJ = datas[j];
				if (currentMin > dataJ)
				{
					datas[j] = currentMin;
					datas[i] = dataJ;
					currentMin = dataJ;
				}
			}
		}
	}

	/**
	 * ��������(��С����)
	 */
	public void insertSort()
	{
		int dataI, dataC;
		int left, right, center, newCenter;
		int dataIHash, dataCHash;
		for (int i = 1; i < length; i++)
		{
			dataI = datas[i];
			dataIHash = dataI;
			// �Ȳ��Ҳ����λ��
			left = 0;
			right = i;
			center = (left + right) / 2;
			while (true)
			{
				dataC = datas[center];
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
				System.arraycopy(datas, center, datas, center + 1, i - center);
				// ���뵱ǰ��ֵ
				datas[center] = dataI;
			}
		}
	}

	/**
	 * �鲢����(��С����)
	 */
	public void mergeSort()
	{ // provides workspace
		int[] workSpace = new int[length];
		recMergeSort(workSpace, 0, length - 1);
	}

	private void recMergeSort(int[] workSpace, int lowerBound, int upperBound)
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

	private void merge(int[] workSpace, int lowPtr, int highPtr, int upperBound)
	{
		int j = 0; // workspace index
		int lowerBound = lowPtr;
		int mid = highPtr - 1;
		int n = upperBound - lowerBound + 1; // # of items

		while (lowPtr <= mid && highPtr <= upperBound)
		{
			if (datas[lowPtr] < datas[highPtr])
			{
				workSpace[j++] = datas[lowPtr++];
			}
			else
			{
				workSpace[j++] = datas[highPtr++];
			}
		}
		while (lowPtr <= mid)
		{
			workSpace[j++] = datas[lowPtr++];
		}
		while (highPtr <= upperBound)
		{
			workSpace[j++] = datas[highPtr++];
		}
		System.arraycopy(workSpace, 0, datas, lowerBound, n);
	}

	/**
	 * ϣ������(��С����)
	 */
	public void shellSort()
	{
		int h = 1; // find initial value of h
		while (h <= length / 3)
		{
			h = h * 3 + 1; // (1, 4, 13, 40, 121, ...)
		}

		int temp;

		while (h > 0) // �任��ȣ����ж��ֲ�������
		{

			for (int iCirc = 0; iCirc < h; iCirc++)// ����һ����Ϊh��h�β�������
			{

				for (int iSort = iCirc + h; iSort < length; iSort += h)// ���е�iCirc�β�������
				{
					temp = datas[iSort];
					int iPos = iSort;
					while (iPos >= h && temp < datas[iPos - h])
					{
						datas[iPos] = datas[iPos - h];
						iPos -= h;
					}
					datas[iPos] = temp;
				}
			}
			h = (h - 1) / 3; // decrease h
		} // end while(h>0)
	} // end shellSort()
	/**
	 * ��������(��С����)
	 */
	public void quickSort1()
	{
		recQuickSort1(0, length - 1);
	}

	// --------------------------------------------------------------
	private void recQuickSort1(int left, int right)
	{
		if (right - left <= 0) // if size <= 1,
			return; // already sorted
		else
		// size is 2 or larger
		{
			int pivot = datas[right]; // rightmost item
											// partition range
			int partition = partitionIt1(left, right, pivot);
			recQuickSort1(left, partition - 1); // sort left side
			recQuickSort1(partition + 1, right); // sort right side
		}
	} // end recQuickSort()
	private int partitionIt1(int left, int right, int pivot)
	{
		int leftPtr = left - 1; // left (after ++)
		int rightPtr = right; // right-1 (after --)
		int pivotCode = pivot;
		while (true)
		{ // find bigger item
			while (datas[++leftPtr] < pivotCode)
				; // (nop)
					// find smaller item
			while (rightPtr > 0 && datas[--rightPtr] > pivotCode)
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
		int temp = datas[dex1]; // A into temp
		datas[dex1] = datas[dex2]; // B into A
		datas[dex2] = temp; // temp into B
	}
	/**
	 * ��������2(��С����)
	 */
	public void quickSort2()
	{
		recQuickSort2(0, length - 1);
	}

	private void recQuickSort2(int left, int right)
	{
		int size = right - left + 1;
		if (size <= 3) // manual sort if small
			manualSort(left, right);
		else
		// quicksort if large
		{
			int median = medianOf3(left, right);
			int partition = partitionIt2(left, right, median);
			recQuickSort2(left, partition - 1);
			recQuickSort2(partition + 1, right);
		}
	} 
	private int medianOf3(int left, int right)
	{
		int center = (left + right) / 2;
		// order left & center
		if (datas[left] > datas[center])
			swap(left, center);
		// order left & right
		if (datas[left] > datas[right])
			swap(left, right);
		// order center & right
		if (datas[center] > datas[right])
			swap(center, right);

		swap(center, right - 1); // put pivot on right
		return datas[right - 1]; // return median value
	} // end medianOf3()

	private int partitionIt2(int left, int right, int pivot)
	{
		int leftPtr = left; // right of first elem
		int rightPtr = right - 1; // left of pivot
		int pivotCode = pivot;

		while (true)
		{
			while (datas[++leftPtr] < pivotCode)
				// find bigger
				; // (nop)
			while (datas[--rightPtr] > pivotCode)
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
			if (datas[left] > datas[right])
				swap(left, right);
			return;
		}
		else
		// size is 3
		{ // 3-sort left, center, & right
			if (datas[left] > datas[right - 1])
				swap(left, right - 1); // left, center
			if (datas[left] > datas[right])
				swap(left, right); // left, right
			if (datas[right - 1] > datas[right])
				swap(right - 1, right); // center, right
		}
	} // end manualSort()
	/**
	 * ��������3(��С����)
	 */
	public void quickSort3()
	{
		recQuickSort3(0, length - 1);
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
			int median = medianOf3(left, right);
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
			int temp = datas[out]; // remove marked item
			in = out; // start shifts at out
						// until one is smaller,
			while (in > left && datas[in - 1] >= temp)
			{
				datas[in] = datas[in - 1]; // shift item to right
				--in; // go left one position
			}
			datas[in] = temp; // insert marked item
		} // end for
	} // end insertionSort()

	/**
	 * ����Ԫ��
	 * @param number
	 */
	public void add(int number)
	{
		increaseCapacity();
		// ��ֵ
		datas[length] = number;
		length++;
	}

	/**
	 * ����Ԫ��
	 * @param index �����λ��
	 * @param number ����Ķ���
	 * @return �����Ƿ�ɹ�����
	 */
	public boolean insertElementAt(int index, int number)
	{
		if (index < 0)
		{
			return false;
		}
		if (index >= length)
		{
			add(number);
			return true;
		}
		increaseCapacity();
		// ��󿽱�
		// for(int i=length;i>index;i--)
		// {
		// datas[i]=datas[i-1];
		// }
		System.arraycopy(datas, index, datas, index + 1, length - index);

		datas[index] = number;

		length++;
		return true;
	}
	/**
	 * ��������
	 */
	private void increaseCapacity()
	{
		if (length >= capacity)
		{
			int newCapacity = capacity * 2;
			int datasNew[] = new int[newCapacity];
			System.arraycopy(datas, 0, datasNew, 0, length);
			datas = datasNew;
			capacity = newCapacity;
		}
	}

	/**
	 * ��ն�������
	 */
	public void clear()
	{
		for (int i = 0; i < length; i++)
		{
			datas[i] = 0;
		}
		length = 0;
	}

	/**
	 * �Ƿ����ĳ������
	 * @param number  �����Ķ���
	 * @return �Ƿ����
	 */
	public boolean contains(int number)
	{
		for (int i = 0; i < length; i++)
		{
			if (datas[i] == number)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ȡλ��ĳ���±�Ķ���
	 * @param index �����±�
	 * @return ��Ӧ�Ķ���
	 */
	public int elementAt(int index)
	{
		if (index < 0 || index >= length)
		{
			return -1;
		}
		return datas[index];
	}

	/**
	 * �ж�ĳ��ֵλ�ڵ�ǰVector�е�ʲôλ��
	 * @param number Ҫ�жϵĶ���
	 * @return �����ڵ�ǰ�����е�λ��
	 */
	public int indexOf(int number)
	{
		for (int i = 0; i < length; i++)
		{
			if (datas[i] == number)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * �жϵ�ǰVector�Ƿ�Ϊ��
	 * @return Vector�Ƿ�Ϊ��
	 */
	public boolean isEmpty()
	{
		return length == 0;
	}

	/**
	 * �Ƴ�ָ��λ�õĶ��󣬺����������������
	 * @param index
	 * @return �Ƿ�ɹ��Ƴ�
	 */
	public boolean removeAt(int index)
	{
		if (index < 0 || index >= length)
		{
			return false;
		}

		for (int i = index; i <= length - 2; i++)
		{
			datas[i] = datas[i + 1];
		}
		length--;
		return true;
	}

	/**
	 *  ɾ��ĳ������
	 * @param number Ҫɾ���Ķ���
	 * @return �Ƿ�ɹ�ɾ��
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
	 * ��indexλ���������ö���
	 * @param index ָ�����±�
	 * @param number �������õĶ���
	 * @return �Ƿ�ɹ�����
	 */
	public boolean set(int index, int number)
	{
		if (index < 0 || index >= length)
		{
			return false;
		}
		datas[index] = number;
		return true;
	}

	/**
	 * ��ȡ��С
	 * @return ��С
	 */
	public int size()
	{
		return length;
	}

	/**
	 * ��ʾ�����飬ÿ������֮���Զ��ŷָ�
	 */
	public void show()
	{
		for (int i = 0; i < length; i++)
		{
			int number = datas[i];
			C2D_Debug.logChunk(number + ",");
		}
		C2D_Debug.log("");
	}

	/**
	 * ��ʾ�����飬ÿ������һ��
	 */
	public void showByLines()
	{
		for (int i = 0; i < length; i++)
		{
			int number = datas[i];
			C2D_Debug.logDebug(""+number);
		}
	}

	/**
	 * ����Ƿ�����
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
	 * ����Ƿ񾭹�����
	 * @return �Ƿ񾭹�����
	 */
	public boolean isSorted()
	{
		if (length <= 1)
		{
			return true;
		}
		if (datas[0] > datas[1])
		{
			int i = 1;
			while (i < length)
			{
				if (datas[i] > datas[i - 1])
				{
					return false;
				}
				i++;
			}
		}
		else
		{
			int i = 1;
			while (i < length)
			{
				if (datas[i] < datas[i - 1])
				{
					return false;
				}
				i++;
			}
		}
		return true;
	}
}
