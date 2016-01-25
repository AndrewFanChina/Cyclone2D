package c2d.mod.script;;

/**
 * 运行时栈 
 */
class C2D_RuntimeStack
{
    
    /** 栈元素数组 */
    C2D_Value[] pElmnts;
    
    /** 栈元素数目. */
    int iSize;

    /** 栈顶指针. */
    int iTopIndex;
    
    /** 当前栈框架指针. */
    int iFrameIndex; 

    /** 本栈基于的运行线程. */
    private C2D_Thread parent = null;
    
    /**
     * 构造运行时栈
     *
     * @param parentT 本栈基于的运行线程
     */
    public C2D_RuntimeStack(C2D_Thread parentT)
    {
        parent = parentT;
        pElmnts = new C2D_Value[parent.scriptData.stackSize];
        for (int i = 0; i < pElmnts.length; i++)
        {
          pElmnts[i] = new C2D_Value();
        }
    }

    /**
     * PopFrame () 从栈中弹出N个单元.
     *
     * @param iSize int 从栈中弹出N个单元
     */
    void PopFrame(int iSize)
    {
        iTopIndex -= iSize;
        int idBegin = iTopIndex;
        //字符串检查
        for (int i = 0; i < iSize; i++)
        {
            parent.releaseValueString(pElmnts[idBegin + i]);
        }
    }

    /**
     * GetStackValue () 获取指定的栈元素的值.
     *
     * @param iIndex int 指定的栈元素ID
     * @return Value 返回的栈元素值
     */
    C2D_Value GetStackValue(int iIndex)
    {

        return pElmnts[C2D_VM.ResolveStackIndex(iIndex)];
    }

    /**
     * SetStackValue () 设置指定的栈元素的值.
     *
     * @param iIndex int 指定的栈元素ID
     * @param Val Value 设定的栈元素值
     */
    void SetStackValue(int iIndex, C2D_Value Val)
    {
        //字符串检查
        int id =  C2D_VM.ResolveStackIndex(iIndex);
        parent.releaseValueString(pElmnts[id]);
        //重新赋值
        pElmnts[id] = Val;
    }

    /**
     * Push () 向栈中压入一个单元.
     *
     * @param Val Value
     */
    void Push(C2D_Value Val)
    {

        //字符串检查
        parent.releaseValueString(pElmnts[iTopIndex]);

        //将元素放入栈顶
        pElmnts[iTopIndex] = Val;

        // 增加栈顶指针

        ++iTopIndex;
    }

    /**
     * 
     * 弹出栈顶单元.
     */

    void Pop()
    {
        //减少栈顶索引
        --iTopIndex;

        //读取栈顶单元

        C2D_Value Val = pElmnts[iTopIndex];
        parent.releaseValueString(Val);
    }

    /**
     * Pop () 弹出栈顶单元.
     *
     * @return Value
     */
    C2D_Value Peek()
    {
        if (iTopIndex <= 0)
        {
            return null;
        }
        //读取栈顶单元
        C2D_Value Val = pElmnts[iTopIndex-1];

        return Val;
    }

    /**
     * PushFrame () 压入一个指定大小的栈框架.
     *
     * @param iSize int
     */
    void PushFrame(int iSize)
    {
        //增加栈顶指针
        iTopIndex += iSize;

        //移动栈框架指针

        iFrameIndex = iTopIndex;
    }

    /**
     * 释放资源
     */
    void ReleaseRes()
    {
        if (pElmnts != null)
        {
            for (int i = 0; i < pElmnts.length; i++)
            {
                pElmnts[i] = null;
            }
            pElmnts = null;
        }
    }
    }
