package c2d.mod.script;

/**
 * 脚本方法处理器
 */
public interface C2D_ScriptFunctionHandler
{
    
    /**
     * 执行方法处理
     *
     * @param 当前线程
     * @param 执行的函数ID
     * @return 是否挂起当前脚本，如果挂起的话，下次执行不会跳过当前语句。
     */
    boolean handleFunction(C2D_Thread currentThread,int functionID);
}
