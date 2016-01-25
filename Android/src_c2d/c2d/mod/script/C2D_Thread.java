package c2d.mod.script;

import java.util.Hashtable;

import c2d.lang.math.C2D_Math;


/**
 * C2DS线程
 */
public class C2D_Thread
{
  
  /** 运行时栈. */
  C2D_RuntimeStack Stack = null; 
  /** 动态字符串表. */
  private Hashtable stringTableDynamic = null; 
  
  /** 动态字符串键值IDy. */
  private int stringHushKey = 0;
  
  /** 使用的脚本数据. */
  C2D_ScriptData scriptData = null; 
  
  /** 当前指令指针. */
  int iCurrInstr; 
  
  /** _RetVal寄存器. */
  C2D_Value _RetVal = new C2D_Value();

  /** 当前脚本是否正在运行. */
  boolean isRunning;
  
  /** 暂停后的恢复帧数. */
  int pausedFrame;

  /** 本线程基于的脚本执行器. */
  private C2D_ScriptExcutor scripExcutor;
  
  /** 将要切换到的线程 */
  private String m_switchTo=null;
  
  /**
   * 构造C2DS脚本线程
   *
   * @param scripExcutorT 本线程基于的脚本执行器
   * @param scriptDataT 本线程使用的脚本数据
   */
  C2D_Thread(C2D_ScriptExcutor scripExcutorT,C2D_ScriptData scriptDataT)
  {
    scripExcutor=scripExcutorT;           //父框架
    scriptData=scriptDataT;               //脚本数据
    Stack = new C2D_RuntimeStack(this);    //运行时栈
    stringTableDynamic = new Hashtable(); //动态字符串表
    initThread();
  }

  /**
   * initScript () 初始化脚本.
   */
  private void initThread()
  {
    int iMainFuncIndex = scriptData.iMainFuncIndex;
    // 如果需要的话，获取_Main()函数索引
    if (scriptData.iIsMainFuncPresent)
    {
      // 如果存在函数表则设置入口点

      if (scriptData.pFuncTable != null)
      {
        //如果 _Main ()函数存在, 获取其入口点作为当前执行指令索引
        iCurrInstr = scriptData.pFuncTable[iMainFuncIndex].iEntryPoint;
        isRunning=true;
      }
    }

    //清除栈

    Stack.iTopIndex = 0;
    Stack.iFrameIndex = 0;

    //设置栈元素的类型为空

    for (int iCurrElmntIndex = 0; iCurrElmntIndex < Stack.iSize;
         ++iCurrElmntIndex)
    {
      Stack.pElmnts[iCurrElmntIndex].iType = C2D_VM.OP_TYPE_NULL;
    }

    //为全局变量生成空间

    Stack.PushFrame(scriptData.iGlobalDataSize);

    //如果_Main()函数存在，压入它的栈框架(加上一个额外的元素，用于存储函数索引，
    //这个元素通常位于栈框架顶端，也是因为它的存在，其它变量的索引通常从-2开始)
    if (scriptData.iIsMainFuncPresent)
    {
      Stack.PushFrame(scriptData.pFuncTable[iMainFuncIndex].iStackFrameSize + 1);
    }
  }

  //增加字符串到动态字符串表
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
   * 获得字符
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
   * 设置字符
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
   * 释放Value指向的动态字符串
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
   * stopScript () 停止某个线程执行.
   */
  public void stopScript()
  {
    isRunning = false;
  }

  /**
   * setPauseFrame () 将某个线程暂停一段时间.
   *
   * @param iDur int
   */
  public void setPauseFrame(int iDur)
  {
    pausedFrame = iDur;
  }


  /**
   * getParamAsInt () 向主应用程序API函数返回指定的脚本的整型参数.
   *
   * @param iParamIndex int
   * @return int
   */
  public int getParamAsInt(int iParamIndex)
  {
    // 获得当前的参数单元

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //强制转换成整型

    int iInt = C2D_VM.CoerceValueToInt(Param);

    return iInt;
  }
//#if Platform=="Android"
  /**
   * 向主应用程序API函数返回指定的脚本的浮点型参数.
   *
   * @param iParamIndex int
   * @return float
   */
  public float getParamAsFloat(int iParamIndex)
  {
    // 获得当前的参数单元

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //强制转换成浮点型

    float fFloat =  C2D_VM.CoerceValueToFloat(Param);

    return fFloat;
  }
//#endif
  /**
   * 向主应用程序API函数返回指定的脚本的字符型参数.
   *
   * @param iParamIndex int
   * @return String
   */
  public String getParamAsString(int iParamIndex)
  {
    // 获得当前的参数单元

    int iTopIndex = Stack.iTopIndex;
    C2D_Value Param = Stack.pElmnts[iTopIndex - (iParamIndex + 1)];

    //强制转换成字符型

    String pstrString =  C2D_VM.CoerceValueToString(Param);

    return pstrString;
  }

  /**
   * getReturnValueAsInt () 将指定线程的返回值作为整型返回.
   *
   * @return int
   */
  public int getReturnValueAsInt()
  {
    // 返回寄存器中的值
    return _RetVal.iData;
  }
//#if Platform=="Android"
  /**
   * getReturnValueAsInt () 将指定线程的返回值作为浮点形返回.
   *
   * @return float
   */
  public float getReturnValueAsFloat()
  {
    //返回寄存器中的值
    return C2D_Math.intBitsToFloat(_RetVal.iData);
  }
//#endif
  /**
   * getReturnValueAsString () 将指定线程的返回值作为字符型返回.
   *
   * @return String
   */
  public String getReturnValueAsString()
  {
    //返回寄存器中的值
    return (String) getString(_RetVal);
  }
  
  /**
   * passIntParam () 从主应用程序传递一个整型参数到脚本函数.
   *
   * @param iInt int
   */
  public void passIntParam(int iInt)
  {
    //创建一个Value结构来包装参数

    C2D_Value Param = new C2D_Value();
    Param.iType = C2D_VM.OP_TYPE_INT;
    Param.iData = iInt;

    //将参数压栈

    Stack.Push(Param);
  }
//#if Platform=="Android"
  /**
   * passFloatParam () 从主应用程序传递一个浮点参数到脚本函数.
   *
   * @param fFloat float
   */
  public void passFloatParam(float fFloat)
  {
    //创建一个Value结构来包装参数

    C2D_Value Param = new C2D_Value();
    Param.iType = C2D_VM.OP_TYPE_FLOAT;
    Param.iData = C2D_Math.floatToIntBits(fFloat);

    //将参数压栈

    Stack.Push(Param);
  }
//#endif
  /**
   * passStringParam () 从主应用程序传递一个字符参数到脚本函数.
   *
   * @param pstrString String
   */
  public void passStringParam(String pstrString)
  {
    //创建一个Value结构来包装参数
    int key = addString(pstrString);
    C2D_Value Param = new C2D_Value();
    Param.setAsDynamicString(key);
    //将参数压栈
    Stack.Push(Param);
  }

  /**
   * returnFromHost_Int () 从主应用程序API函数中返回整型值.
   *
   * @param iInt int
   */
  public void returnFromHost_Int(int iInt)
  {
    //将整型值存放入寄存器
    releaseValueString(_RetVal);
    _RetVal.iType = C2D_VM.OP_TYPE_INT;
    _RetVal.iData = iInt;
  }
//#if Platform=="Android"
  /**
   * returnFromHost_Float () 从主应用程序API函数中返回浮点数.
   *
   * @param fFloat float
   */
  public void returnFromHost_Float(float fFloat)
  {
    //将浮点数存放入寄存器
    releaseValueString(_RetVal);
    _RetVal.iType = C2D_VM.OP_TYPE_FLOAT;
    _RetVal.iData = C2D_Math.floatToIntBits(fFloat);
  }
//#endif
  /**
   * returnFromHost_String () 从主应用程序API函数中返回字符串.
   *
   * @param pstrString String
   */
  public void returnFromHost_String(String pstrString)
  {
    //将字符串存放入寄存器
    releaseValueString(_RetVal);
    int key = addString(pstrString);
    _RetVal.setAsDynamicString(key);
  }
  
  /**
   * inRunning () 是否正在运行.
   *
   * @return true, if is running
   */
  public boolean isRunning()
  {
    return isRunning;
  }
  
  /**
   * isPaused () 是否正在运行并处于被暂停状态.
   *
   * @return true, if is paused
   */
  public boolean isPaused()
  {
    return pausedFrame!=0;
  }
  /**
   * 获得执行器
   * @return 执行器
   */
  public C2D_ScriptExcutor getExcutor()
  {
	  return scripExcutor;
  }
  /**
   * 设置即将切换到的线程名称
   * @param name 即将切换到的线程名称
   */
  public void setSwitchTo(String name)
  {
	  m_switchTo=name;
  }
  /**
   * 获得即将切换到的线程名称
   * @return 即将切换到的线程名称
   */
  public String getSwitchTo()
  {
	  return m_switchTo;
  }
  /**
   * 释放资源.
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
