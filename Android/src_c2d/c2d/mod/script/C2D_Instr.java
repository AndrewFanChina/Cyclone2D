package c2d.mod.script;


/**
 * C2D指令类
 */
class C2D_Instr
{
    
    /** 操作码. */
    public int iOpcode;
    
    /** 操作数个数. */
    public int iOpCount; 
    
    /** 操作数列表. */
    public C2D_Value[] pOpList; 
    
    /** 操作码助符记，【后面可以去掉】. */
    public String sOpcode;      

    /**
     * 释放资源
     */
    public void ReleaseRes()
    {
        if (pOpList != null)
        {
            for (int i = 0; i < pOpList.length; i++)
            {
                pOpList[i] = null;
            }
            pOpList = null;
        }
    }
}
