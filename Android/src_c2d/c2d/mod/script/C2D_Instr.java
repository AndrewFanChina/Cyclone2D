package c2d.mod.script;


/**
 * C2Dָ����
 */
class C2D_Instr
{
    
    /** ������. */
    public int iOpcode;
    
    /** ����������. */
    public int iOpCount; 
    
    /** �������б�. */
    public C2D_Value[] pOpList; 
    
    /** �����������ǣ����������ȥ����. */
    public String sOpcode;      

    /**
     * �ͷ���Դ
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
