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
    public int iType = -1;                        // 数值类型
    
    /** The i data. */
    public int iData = -1;                        // 数值
    
    /** The i offset index. */
    public int iOffsetIndex = -1;                 // 偏移索引，如果是字符串索引变量，这个值用正数来表示是否是静态字符串
    /**
     * 设置为动态字符串
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
