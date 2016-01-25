package c2d.mod.script;;

/**
 * ����ʱջ 
 */
class C2D_RuntimeStack
{
    
    /** ջԪ������ */
    C2D_Value[] pElmnts;
    
    /** ջԪ����Ŀ. */
    int iSize;

    /** ջ��ָ��. */
    int iTopIndex;
    
    /** ��ǰջ���ָ��. */
    int iFrameIndex; 

    /** ��ջ���ڵ������߳�. */
    private C2D_Thread parent = null;
    
    /**
     * ��������ʱջ
     *
     * @param parentT ��ջ���ڵ������߳�
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
     * PopFrame () ��ջ�е���N����Ԫ.
     *
     * @param iSize int ��ջ�е���N����Ԫ
     */
    void PopFrame(int iSize)
    {
        iTopIndex -= iSize;
        int idBegin = iTopIndex;
        //�ַ������
        for (int i = 0; i < iSize; i++)
        {
            parent.releaseValueString(pElmnts[idBegin + i]);
        }
    }

    /**
     * GetStackValue () ��ȡָ����ջԪ�ص�ֵ.
     *
     * @param iIndex int ָ����ջԪ��ID
     * @return Value ���ص�ջԪ��ֵ
     */
    C2D_Value GetStackValue(int iIndex)
    {

        return pElmnts[C2D_VM.ResolveStackIndex(iIndex)];
    }

    /**
     * SetStackValue () ����ָ����ջԪ�ص�ֵ.
     *
     * @param iIndex int ָ����ջԪ��ID
     * @param Val Value �趨��ջԪ��ֵ
     */
    void SetStackValue(int iIndex, C2D_Value Val)
    {
        //�ַ������
        int id =  C2D_VM.ResolveStackIndex(iIndex);
        parent.releaseValueString(pElmnts[id]);
        //���¸�ֵ
        pElmnts[id] = Val;
    }

    /**
     * Push () ��ջ��ѹ��һ����Ԫ.
     *
     * @param Val Value
     */
    void Push(C2D_Value Val)
    {

        //�ַ������
        parent.releaseValueString(pElmnts[iTopIndex]);

        //��Ԫ�ط���ջ��
        pElmnts[iTopIndex] = Val;

        // ����ջ��ָ��

        ++iTopIndex;
    }

    /**
     * 
     * ����ջ����Ԫ.
     */

    void Pop()
    {
        //����ջ������
        --iTopIndex;

        //��ȡջ����Ԫ

        C2D_Value Val = pElmnts[iTopIndex];
        parent.releaseValueString(Val);
    }

    /**
     * Pop () ����ջ����Ԫ.
     *
     * @return Value
     */
    C2D_Value Peek()
    {
        if (iTopIndex <= 0)
        {
            return null;
        }
        //��ȡջ����Ԫ
        C2D_Value Val = pElmnts[iTopIndex-1];

        return Val;
    }

    /**
     * PushFrame () ѹ��һ��ָ����С��ջ���.
     *
     * @param iSize int
     */
    void PushFrame(int iSize)
    {
        //����ջ��ָ��
        iTopIndex += iSize;

        //�ƶ�ջ���ָ��

        iFrameIndex = iTopIndex;
    }

    /**
     * �ͷ���Դ
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
