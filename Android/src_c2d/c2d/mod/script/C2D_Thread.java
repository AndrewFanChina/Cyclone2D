package c2d.mod.script;

import java.util.Hashtable;

import c2d.lang.math.C2D_Math;


/**
 * C2DS�߳�
 */
public class C2D_Thread
{
  
  /** ����ʱջ. */
  C2D_RuntimeStack Stack = null; 
  /** ��̬�ַ�����. */
  private Hashtable stringTableDynamic = null; 
  
  /** ��̬�ַ�����ֵIDy. */
  private int stringHushKey = 0;
  
  /** ʹ�õĽű�����. */
  C2D_ScriptData scriptData = null; 
  
  /** ��ǰָ��ָ��. */
  int iCurrInstr; 
  
  /** _RetVal�Ĵ���. */
  C2D_Value _RetVal = new C2D_Value();

  /** ��ǰ�ű��Ƿ���������. */
  boolean isRunning;
  
  /** ��ͣ��Ļָ�֡��. */
  int pausedFrame;

  /** ���̻߳��ڵĽű�ִ����. */
  private C2D_ScriptExcutor scripExcutor;
  
  /** ��Ҫ�л������߳� */
  private String m_switchTo=null;
  
  /**
   * ����C2DS�ű��߳�
   *
   * @param scripExcutorT ���̻߳��ڵĽű�ִ����
   * @param scriptDataT ���߳�ʹ�õĽű�����
   */
  C2D_Thread(C2D_ScriptExcutor scripExcutorT,C2D_ScriptData scriptDataT)
  {
    scripExcutor=scripExcutorT;           //�����
    scriptData=scriptDataT;               //�ű�����
    Stack = new C2D_RuntimeStack(this);    //����ʱջ
    stringTableDynamic = new Hashtable(); //��̬�ַ�����
    initThread();
  }

  /**
   * initScript () ��ʼ���ű�.
   */
  private void initThread()
  {
    int iMainFuncIndex = scriptData.iMainFuncIndex;
    // �����Ҫ�Ļ�����ȡ_Main()��������
    if (scriptData.iIsMainFuncPresent)
    {
      // ������ں�������������ڵ�

      if (scriptData.pFuncTable != null)
      {
        //��� _Main ()��������, ��ȡ����ڵ���Ϊ��ǰִ��ָ������
        iCurrInstr = scriptData.pFuncTable[iMainFuncIndex].iEntryPoint;
        isRunning=true;
      }
    }

    //���ջ

    Stack.iTopIndex = 0;
    Stack.iFrameIndex = 0;

    //����ջԪ�ص�����Ϊ��

    for (int iCurrElmntIndex = 0; iCurrElmntIndex < Stack.iSize;
         ++iCurrElmntIndex)
    {
      Stack.pElmnts[iCurrElmntIndex].iType = C2D_VM.OP_TYPE_NULL;
    }

    //Ϊȫ�ֱ������ɿռ�

    Stack.PushFrame(scriptData.iGlobalDataSize);

    //���_Main()�������ڣ�ѹ������ջ���(����һ�������Ԫ�أ����ڴ洢����������
    //���Ԫ��ͨ��λ��ջ��ܶ��ˣ�Ҳ����Ϊ���Ĵ��ڣ���������������ͨ����-2��ʼ)
    if (scriptData.iIsMainFuncPresent)
    {
      Stack.PushFrame(scriptData.pFuncTable[iMainFuncIndex].iStackFrameSize + 1);
    }
  }

  //�����ַ�������̬�ַ�����
  /**
   * Adds the string.
   *
   * @param s the s
   * @return the int
   */
  int addString(String s)
  {
    int key = stringHushKey;
    stringTableDynamic.put(new Integer(key), s);
    stringHushKey++;
    return key;
  }

  /**
   * ����ַ�
   *
   * @param value the value
   * @return the string
   */
  String getString(C2D_Value value)
  {
    if (value == null || value.iType != C2D_VM.OP_TYPE_STRING_INDEX)
    {
      return null;
    }
    int key = value.iData;
    if (value.iOffsetIndex > 0)
    {
      if (key >= 0 && key < scriptData.stringTableNative.size())
      {
        return (String) scriptData.stringTableNative.elementAt(key);
      }
    }
    else
    {
      Integer iKey = new Integer(key);
      if (stringTableDynamic.containsKey(iKey))
      {
        return (String) stringTableDynamic.get(iKey);
      }
    }
    return null;
  }

  /**
   * �����ַ�
   *
   * @param key the key
   * @param newString the new string
   */
  void setString(int key, String newString)
  {
    Integer iKey = new Integer(key);
    if (!stringTableDynamic.containsKey(iKey))
    {
      return;
    }
    stringTableDynamic.put(iKey, newString);
  }

  /**
   * �ͷ�Valueָ��Ķ�̬�ַ���
   *
   * @param value the value
   */
  void releaseValueString(C2D_Value value)
  {
    if (value == null || value.iType != C2D_VM.OP_TYPE_STRING_INDEX ||
        value.iOffsetIndex > 0)
    {
      return;
    }
    Integer iKey = new Integer(value.iData);
    if (stringTableDynamic.containsKey(iKey))
    {
      stringTableDynamic.remove(iKey);
    }
  }
  
  /**
   * stopScript () ֹͣĳ���߳�ִ��.
   */
  public void stopScript()
  {
    isRunning = false;
  }

  /**
   * setPauseFrame () ��ĳ���߳���ͣһ��ʱ��.
   *
   * @param iDur int
   */
  public void setPauseFrame(int iDur)
  {
    pausedFrame = iDur;
  }


  /**
   * getParamAsInt () ����Ӧ�ó���API��������ָ���Ľű������Ͳ���.
   *
   * @param iParamIndex int
   * @return int
   */
  public int getParamAsInt(int iParamIndex)
  {
    // ��õ�ǰ�Ĳ�����Ԫ

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //ǿ��ת��������

    int iInt = C2D_VM.CoerceValueToInt(Param);

    return iInt;
  }
//#if Platform=="Android"
  /**
   * ����Ӧ�ó���API��������ָ���Ľű��ĸ����Ͳ���.
   *
   * @param iParamIndex int
   * @return float
   */
  public float getParamAsFloat(int iParamIndex)
  {
    // ��õ�ǰ�Ĳ�����Ԫ

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //ǿ��ת���ɸ�����

    float fFloat =  C2D_VM.CoerceValueToFloat(Param);

    return fFloat;
  }
//#endif
  /**
   * ����Ӧ�ó���API��������ָ���Ľű����ַ��Ͳ���.
   *
   * @param iParamIndex int
   * @return String
   */
  public String getParamAsString(int iParamIndex)
  {
    // ��õ�ǰ�Ĳ�����Ԫ

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //ǿ��ת�����ַ���

    String pstrString =  C2D_VM.CoerceValueToString(Param);

    return pstrString;
  }

  /**
   * getReturnValueAsInt () ��ָ���̵߳ķ���ֵ��Ϊ���ͷ���.
   *
   * @return int
   */
  public int getReturnValueAsInt()
  {
    // ���ؼĴ����е�ֵ
    return _RetVal.iData;
  }
//#if Platform=="Android"
  /**
   * getReturnValueAsInt () ��ָ���̵߳ķ���ֵ��Ϊ�����η���.
   *
   * @return float
   */
  public float getReturnValueAsFloat()
  {
    //���ؼĴ����е�ֵ
    return C2D_Math.intBitsToFloat(_RetVal.iData);
  }
//#endif
  /**
   * getReturnValueAsString () ��ָ���̵߳ķ���ֵ��Ϊ�ַ��ͷ���.
   *
   * @return String
   */
  public String getReturnValueAsString()
  {
    //���ؼĴ����е�ֵ
    return (String) getString(_RetVal);
  }
  
  /**
   * passIntParam () ����Ӧ�ó��򴫵�һ�����Ͳ������ű�����.
   *
   * @param iInt int
   */
  public void passIntParam(int iInt)
  {
    //����һ��Value�ṹ����װ����

    C2D_Value Param = new C2D_Value();
    Param.iType = C2D_VM.OP_TYPE_INT;
    Param.iData = iInt;

    //������ѹջ

    Stack.Push(Param);
  }
//#if Platform=="Android"
  /**
   * passFloatParam () ����Ӧ�ó��򴫵�һ������������ű�����.
   *
   * @param fFloat float
   */
  public void passFloatParam(float fFloat)
  {
    //����һ��Value�ṹ����װ����

    C2D_Value Param = new C2D_Value();
    Param.iType = C2D_VM.OP_TYPE_FLOAT;
    Param.iData = C2D_Math.floatToIntBits(fFloat);

    //������ѹջ

    Stack.Push(Param);
  }
//#endif
  /**
   * passStringParam () ����Ӧ�ó��򴫵�һ���ַ��������ű�����.
   *
   * @param pstrString String
   */
  public void passStringParam(String pstrString)
  {
    //����һ��Value�ṹ����װ����
    int key = addString(pstrString);
    C2D_Value Param = new C2D_Value();
    Param.setAsDynamicString(key);
    //������ѹջ
    Stack.Push(Param);
  }

  /**
   * returnFromHost_Int () ����Ӧ�ó���API�����з�������ֵ.
   *
   * @param iInt int
   */
  public void returnFromHost_Int(int iInt)
  {
    //������ֵ�����Ĵ���
    releaseValueString(_RetVal);
    _RetVal.iType = C2D_VM.OP_TYPE_INT;
    _RetVal.iData = iInt;
  }
//#if Platform=="Android"
  /**
   * returnFromHost_Float () ����Ӧ�ó���API�����з��ظ�����.
   *
   * @param fFloat float
   */
  public void returnFromHost_Float(float fFloat)
  {
    //�������������Ĵ���
    releaseValueString(_RetVal);
    _RetVal.iType = C2D_VM.OP_TYPE_FLOAT;
    _RetVal.iData = C2D_Math.floatToIntBits(fFloat);
  }
//#endif
  /**
   * returnFromHost_String () ����Ӧ�ó���API�����з����ַ���.
   *
   * @param pstrString String
   */
  public void returnFromHost_String(String pstrString)
  {
    //���ַ��������Ĵ���
    releaseValueString(_RetVal);
    int key = addString(pstrString);
    _RetVal.setAsDynamicString(key);
  }
  
  /**
   * inRunning () �Ƿ���������.
   *
   * @return true, if is running
   */
  public boolean isRunning()
  {
    return isRunning;
  }
  
  /**
   * isPaused () �Ƿ��������в����ڱ���ͣ״̬.
   *
   * @return true, if is paused
   */
  public boolean isPaused()
  {
    return pausedFrame!=0;
  }
  /**
   * ���ִ����
   * @return ִ����
   */
  public C2D_ScriptExcutor getExcutor()
  {
	  return scripExcutor;
  }
  /**
   * ���ü����л������߳�����
   * @param name �����л������߳�����
   */
  public void setSwitchTo(String name)
  {
	  m_switchTo=name;
  }
  /**
   * ��ü����л������߳�����
   * @return �����л������߳�����
   */
  public String getSwitchTo()
  {
	  return m_switchTo;
  }
  /**
   * �ͷ���Դ.
   */
  void doRelease()
  {
    if (Stack != null)
    {
      Stack.ReleaseRes();
      Stack = null;
    }
    _RetVal = null;
    if(scriptData!=null&&scripExcutor.m_scripManager!=null)
    {
      scripExcutor.m_scripManager.unuseScriptData(scriptData.fileName);
    }
    scriptData=null;
  }

}
