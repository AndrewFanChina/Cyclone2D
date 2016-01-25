package c2d.mod.script;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * �ű�ִ������
 */
public class C2D_ScriptExcutor
{
  /** ����� */
  public C2D_ScriptManager m_scripManager;
  /** �߳��б�*/
  private Hashtable m_Threads;
  /**��ǰ�߳�*/
  private C2D_Thread m_currentThread;
  /** ����ִ���� */
  C2D_ScriptFunctionHandler m_funHandler;
  
  /**
   * ��ʼ���µĺ���ִ����
   *
   * @param scripManagerT �ű�������
   */
  public C2D_ScriptExcutor(C2D_ScriptManager scripManagerT)
  {
    m_scripManager=scripManagerT;
    m_Threads=new Hashtable();
  }
  /**
   * ִ���߳�
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
   * ���ؽű��̣߳�����Ѿ����ع�����ֱ�ӷ���
   *
   * @param fileName �ű��ļ�����
   * @return ���� ���غõĽű��߳�
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
   * ����ǰ�߳��л���ָ���̣߳�����߳�δ���أ����Զ�����
   *
   * @param dtName Ŀ���߳�����
   * @return �Ƿ��л��ɹ�
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
   * ��ȡ��ǰ�߳�
   *
   * @return ��ǰ�߳�
   */
  public C2D_Thread getCurrentThread()
  {
    return m_currentThread;
  }
  /**
   * ɾ���߳�
   *
   * @param destThread the dest thread
   * @return �Ƿ�ɹ�ɾ��
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
   * ���ú���������
   *
   * @param functionHandlerT ����������
   */
  public void setFunctionHandler(C2D_ScriptFunctionHandler functionHandlerT)
  {
    m_funHandler=functionHandlerT;
  }
  /**
   * �ͷ���Դ
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
