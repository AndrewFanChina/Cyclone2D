package c2d.mod.script;

/**
 * �ű�����������
 */
public interface C2D_ScriptFunctionHandler
{
    
    /**
     * ִ�з�������
     *
     * @param ��ǰ�߳�
     * @param ִ�еĺ���ID
     * @return �Ƿ����ǰ�ű����������Ļ����´�ִ�в���������ǰ��䡣
     */
    boolean handleFunction(C2D_Thread currentThread,int functionID);
}
