package c2d.mod.script;

/**
 * <p>Title: </p>
 * 
 * <p>Description: </p>
 * 
 * <p>Copyright: Copyright (c) 2010</p>
 * 
 * <p>Company: </p>.
 *
 * @author not attributable
 * @version 1.0
 */
class C2D_Value
{
    
    /** The i type. */
    public int iType = -1;                        // ��ֵ����
    
    /** The i data. */
    public int iData = -1;                        // ��ֵ
    
    /** The i offset index. */
    public int iOffsetIndex = -1;                 // ƫ��������������ַ����������������ֵ����������ʾ�Ƿ��Ǿ�̬�ַ���
    /**
     * ����Ϊ��̬�ַ���
     *
     * @param index the new as dynamic string
     */
    public void setAsDynamicString(int index)
    {
        iType = C2D_VM.OP_TYPE_STRING_INDEX;
        iData = index;
        iOffsetIndex = -1;
    }
}
