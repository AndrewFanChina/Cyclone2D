package c2d.mod.script;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 脚本执行者类
 */
public class C2D_ScriptExcutor
{
  /** 父框架 */
  public C2D_ScriptManager m_scripManager;
  /** 线程列表*/
  private Hashtable m_Threads;
  /**当前线程*/
  private C2D_Thread m_currentThread;
  /** 函数执行器 */
  C2D_ScriptFunctionHandler m_funHandler;
  
  /**
   * 初始化新的函数执行者
   *
   * @param scripManagerT 脚本管理器
   */
  public C2D_ScriptExcutor(C2D_ScriptManager scripManagerT)
  {
    m_scripManager=scripManagerT;
    m_Threads=new Hashtable();
  }
  /**
   * 执行线程
   */
  public void updateThread()
  {
    if(m_currentThread==null)
    {
      return;
    }
    C2D_VM.C2DS_RunScript(m_currentThread);
  }
  /**
   * 加载脚本线程，如果已经加载过，则直接返回
   *
   * @param fileName 脚本文件名称
   * @return 返回 加载好的脚本线程
   */
  public C2D_Thread loadThread(String fileName)
  {
    if(fileName==null)
    {
      return null;
    }
    C2D_Thread thread = (C2D_Thread)m_Threads.get(fileName);
    if(thread!=null)
    {
    	return thread;
    }
    C2D_ScriptData scriptData = m_scripManager.useScriptData(fileName);
    if(scriptData==null)
    {
      return null;
    }
    thread=new C2D_Thread(this,scriptData);
    m_Threads.put(fileName, thread);
    return thread;
  }
  /**
   * 将当前线程切换到指定线程，如果线程未加载，将自动加载
   *
   * @param dtName 目标线程名称
   * @return 是否切换成功
   */
  public boolean switchToThread(String dtName)
  {
    if(dtName ==null)
    {
      return false;
    }
    C2D_Thread dtThread=loadThread(dtName);
    if(dtThread!=null&&!dtThread.equals(m_currentThread))
    {
    	m_currentThread=dtThread;
    	return true;
    }
    return false;
  }
  /**
   * 获取当前线程
   *
   * @return 当前线程
   */
  public C2D_Thread getCurrentThread()
  {
    return m_currentThread;
  }
  /**
   * 删除线程
   *
   * @param destThread the dest thread
   * @return 是否成功删除
   */
  public boolean removeThread(String dtName)
  {
    if(dtName==null)
    {
      return false;
    }
    C2D_Thread thread = (C2D_Thread)m_Threads.get(dtName);
    if(thread!=null)
    {
        if(thread.equals(m_currentThread))
        {
        	m_currentThread=null;
        }
    	thread.doRelease();
    	m_Threads.remove(dtName);
    	return true;
    }
    return true;
  }
  /**
   * 设置函数处理器
   *
   * @param functionHandlerT 函数处理器
   */
  public void setFunctionHandler(C2D_ScriptFunctionHandler functionHandlerT)
  {
    m_funHandler=functionHandlerT;
  }
  /**
   * 释放资源
   */
  public boolean doRelease()
  {
    m_scripManager=null;
    if(m_Threads!=null)
    {
     Enumeration enumer = m_Threads.elements();
     while(enumer.hasMoreElements())
     {
    	 C2D_Thread thread = (C2D_Thread)enumer.nextElement();
    	 if(thread!=null)
    	 {
    		 thread.doRelease();
    	 }
     }
     m_Threads.clear();
     m_Threads=null;
    }
    m_currentThread=null;
    return true;
  }
}
