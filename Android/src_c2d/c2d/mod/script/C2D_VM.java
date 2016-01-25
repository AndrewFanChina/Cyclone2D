package c2d.mod.script;

import c2d.lang.math.C2D_Math;
import c2d.lang.util.debug.C2D_Debug;

/**
 * C2DSVM类
 */
public class C2D_VM
{
	// ---- 通用常量 -----------------------------------------------------------
	/** The Constant DEF_STACK_SIZE. */
	static final int DEF_STACK_SIZE = 128; // 默认堆栈大小
	// ---- 成员变量 -----------------------------------------------------------
	/** The current thread. */
	private static C2D_Thread currentThread; //脚本
	// ---- 指令编码操作码定义 ---------------------------------------------------
	/** The Constant INSTR_MOV. */
	private static final int INSTR_MOV = 0;
	/** The Constant INSTR_ADD. */
	private static final int INSTR_ADD = 1;
	/** The Constant INSTR_SUB. */
	private static final int INSTR_SUB = 2;
	/** The Constant INSTR_MUL. */
	private static final int INSTR_MUL = 3;
	/** The Constant INSTR_DIV. */
	private static final int INSTR_DIV = 4;
	/** The Constant INSTR_MOD. */
	private static final int INSTR_MOD = 5;
	/** The Constant INSTR_EXP. */
	private static final int INSTR_EXP = 6;
	/** The Constant INSTR_NEG. */
	private static final int INSTR_NEG = 7;
	/** The Constant INSTR_INC. */
	private static final int INSTR_INC = 8;
	/** The Constant INSTR_DEC. */
	private static final int INSTR_DEC = 9;
	/** The Constant INSTR_AND. */
	private static final int INSTR_AND = 10;
	/** The Constant INSTR_OR. */
	private static final int INSTR_OR = 11;
	/** The Constant INSTR_XOR. */
	private static final int INSTR_XOR = 12;
	/** The Constant INSTR_NOT. */
	private static final int INSTR_NOT = 13;
	/** The Constant INSTR_SHL. */
	private static final int INSTR_SHL = 14;
	/** The Constant INSTR_SHR. */
	private static final int INSTR_SHR = 15;
	/** The Constant INSTR_CONCAT. */
	private static final int INSTR_CONCAT = 16;
	/** The Constant INSTR_GETCHAR. */
	private static final int INSTR_GETCHAR = 17;
	/** The Constant INSTR_SETCHAR. */
	private static final int INSTR_SETCHAR = 18;
	/** The Constant INSTR_JMP. */
	private static final int INSTR_JMP = 19;
	/** The Constant INSTR_JE. */
	private static final int INSTR_JE = 20;
	/** The Constant INSTR_JNE. */
	private static final int INSTR_JNE = 21;
	/** The Constant INSTR_JG. */
	private static final int INSTR_JG = 22;
	/** The Constant INSTR_JL. */
	private static final int INSTR_JL = 23;
	/** The Constant INSTR_JGE. */
	private static final int INSTR_JGE = 24;
	/** The Constant INSTR_JLE. */
	private static final int INSTR_JLE = 25;
	/** The Constant INSTR_PUSH. */
	private static final int INSTR_PUSH = 26;
	/** The Constant INSTR_POP. */
	private static final int INSTR_POP = 27;
	/** The Constant INSTR_CALL. */
	private static final int INSTR_CALL = 28;
	/** The Constant INSTR_RET. */
	private static final int INSTR_RET = 29;
	/** The Constant INSTR_CALLHOST. */
	private static final int INSTR_CALLHOST = 30;
	/** The Constant INSTR_PAUSE. */
	private static final int INSTR_PAUSE = 31;
	/** The Constant INSTR_EXIT. */
	private static final int INSTR_EXIT = 32;
	/** The Constant INSTR_PRINT. */
	private static final int INSTR_PRINT = 33;
	// ---- 指令助记符 -------------------------------------------------------------
	//这个数组用来在指令执行的时候输出对应的助记符
	/** The Constant instroMnemonics. */
	static final String[] instroMnemonics = new String[]
	{
			"Mov", "Add", "Sub", "Mul", "Div", "Mod", "Exp", "Neg", "Inc", "Dec", "And", "Or", "XOr", "Not", "ShL", "ShR", "Concat", "GetChar", "SetChar", "Jmp", "JE", "JNE",
			"JG", "JL", "JGE", "JLE", "Push", "Pop", "Call", "Ret", "CallHost", "Pause", "Exit", "Print"
	};
	// ---- 操作数类型 ------------------------------------------------------
	/** The Constant OP_TYPE_NULL. */
	static final int OP_TYPE_NULL = -1; // 未定义的类型
	/** The Constant OP_TYPE_INT. */
	static final int OP_TYPE_INT = 0; // 整型
	//#if Platform=="Android"
	/** The Constant OP_TYPE_FLOAT. */
	static final int OP_TYPE_FLOAT = 1; // 浮点型
	//#endif
	/** The Constant OP_TYPE_STRING_INDEX. */
	static final int OP_TYPE_STRING_INDEX = 2; // 字符型
	/** The Constant OP_TYPE_ABS_STACK_INDEX. */
	static final int OP_TYPE_ABS_STACK_INDEX = 3; // 绝对堆栈索引(变量或者使用整型字面量索引的数组元素)
	/** The Constant OP_TYPE_REL_STACK_INDEX. */
	static final int OP_TYPE_REL_STACK_INDEX = 4; // 相对堆栈索引(使用变量索引的数组)
	/** The Constant OP_TYPE_INSTR_INDEX. */
	static final int OP_TYPE_INSTR_INDEX = 5; // 指令索引(用于跳转目标)
	/** The Constant OP_TYPE_FUNC_INDEX. */
	static final int OP_TYPE_FUNC_INDEX = 6; // 函数索引(用于Call指令中)
	/** The Constant OP_TYPE_HOST_API_CALL_INDEX. */
	static final int OP_TYPE_HOST_API_CALL_INDEX = 7; // 主应用程序API调用索引(用于CallHost指令中)
	/** The Constant OP_TYPE_REG. */
	static final int OP_TYPE_REG = 8; // 寄存器，在我们这里就是_RetVal
	/** The Constant OP_TYPE_RET_ASYN. */
	static final int OP_TYPE_RET_ASYN = 9; // 异步退出标志
	/** The Constant OP_TYPE_RET_SYN. */
	static final int OP_TYPE_RET_SYN = 10; // 同步退出标志

	// ---- 成员函数 -----------------------------------------------------------------------------
	/**
	 * ResolveStackIndex () 获得绝对栈索引，正数和零直接返回，负数则是相对当前的栈框架，因此需要加上当前栈框架索引后返回.
	 * 
	 * @param iIndex
	 *            int
	 * @return int
	 */
	static int ResolveStackIndex(int iIndex)
	{
		return (iIndex < 0 ? iIndex += currentThread.Stack.iFrameIndex : iIndex);
	}

	/**
	 * RunScript () 执行当前载入的脚本，直到有按键输入或者脚本退出.
	 * 
	 * @param thread
	 *            C2DSThread
	 */
	public static final void C2DS_RunScript(C2D_Thread thread)
	{
		//设置当前线程
		currentThread = thread;
		if (currentThread == null || !currentThread.isRunning)
		{
			return;
		}
		// 当前脚本是否被暂停?
		if (currentThread.pausedFrame > 0)
			
		{
			currentThread.pausedFrame--;
			if (currentThread.pausedFrame > 0)
			{
				return;
			}
		}
		//循环退出标志
		boolean iExitLoop = false;
		//开始脚本循环
		while (currentThread.isRunning && currentThread.pausedFrame == 0)
		{
			//准备一份指令指针的拷贝用作后面的比较
			int iCurrInstr = currentThread.iCurrInstr;
			if (iCurrInstr < 0 || iCurrInstr >= currentThread.scriptData.pInstrs.length)
			{
				print("代码行无效\n");
				currentThread.isRunning = false;
				iExitLoop = true;
				return;
			}
			//得到当前操作码
			int iOpcode = currentThread.scriptData.pInstrs[iCurrInstr].iOpcode;
			//打印指令信息
			printDebugInfor("\t");
			if (iOpcode < 10)
			{
				printDebugInfor(" " + iOpcode);
			}
			else
			{
				printDebugInfor("" + iOpcode);
			}
			printDebugInfor(" " + instroMnemonics[iOpcode] + " ");
			//根据当前的指令指针执行操作码，只要脚本没有暂停
			switch (iOpcode)
			{
			// ---- 二进制操作
			// 赋值
			case INSTR_MOV:
				// 数学运算
			case INSTR_ADD:
			case INSTR_SUB:
			case INSTR_MUL:
			case INSTR_DIV:
			case INSTR_MOD:
			case INSTR_EXP:
				// 位运算
			case INSTR_AND:
			case INSTR_OR:
			case INSTR_XOR:
			case INSTR_SHL:
			case INSTR_SHR:
			{
				//获得目标操作数的本地副本(索引0)
				C2D_Value Dest = ResolveOpValue(0);
				//获得源操作数的本地副本(索引1)
				C2D_Value Source = ResolveOpValue(1);
				//根据指令，执行操作
				switch (iOpcode)
				{
				//赋值
				case INSTR_MOV:
					// 如果两个栈元素相同，则跳过/*??*/
					if (ResolveOpPntr(0) == ResolveOpPntr(1))
					{
						break;
					}
					// 拷贝原栈元素到目标栈元素
					CopyValue(Dest, Source);
					break;
				//加法
				case INSTR_ADD:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData += ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF += sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				//减法
				case INSTR_SUB:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData -= ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF -= sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				//乘法
				case INSTR_MUL:
					//#if Platform=="Android"
					if ((Source.iType == OP_TYPE_FLOAT || Source.iType == OP_TYPE_INT) && (Dest.iType == OP_TYPE_FLOAT || Dest.iType == OP_TYPE_INT))
					{
						//#endif
						if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
						{
							Dest.iData *= ResolveOpAsInt(1);
						}
						//#if Platform=="Android"
						else
						{
							if (Dest.iType == OP_TYPE_FLOAT && Source.iType == OP_TYPE_INT)
							{
								float destF = ResolveOpAsFloat(0);
								int sourceI = ResolveOpAsInt(1);
								float value = sourceI * destF;
								Dest.iData = C2D_Math.floatToIntBits(value);
							}
							else if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_FLOAT)
							{
								int destI = ResolveOpAsInt(0);
								float sourceF = ResolveOpAsFloat(1);
								float value = sourceF * destI;
								Dest.iData = C2D_Math.floatToIntBits(value);
							}
							else if (Dest.iType == OP_TYPE_FLOAT && Source.iType == OP_TYPE_FLOAT)
							{
								float destF = ResolveOpAsFloat(0);
								float sourceF = ResolveOpAsFloat(1);
								float value = sourceF * destF;
								Dest.iData = C2D_Math.floatToIntBits(destF);
							}
						}
					}
					//#endif
					else
					{
						print("error: can't use MUL between these type operators[" + Source.iType + "," + Dest.iType + "]");
						currentThread.isRunning = false;
						iExitLoop = true;
						break;
					}
					break;
				//除法
				case INSTR_DIV:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData /= ResolveOpAsInt(1);
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						float sourceF = ResolveOpAsFloat(1);
						destF /= sourceF;
						Dest.iData += C2D_Math.floatToIntBits(destF);
					}
					//#endif
					break;
				// 取模
				case INSTR_MOD:
					// 取模只能用于整型
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData %= ResolveOpAsInt(1);
					}
					break;
				// 取幂(暂不支持)
				case INSTR_EXP:
					//                                if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					//                                {
					//                                    Dest.iData = (int)MathUtil.Pow(Dest.iData, ResolveOpAsInt(1));
					//                                }
					//                                else
					//                                {
					//                                    float destF = ResolveOpAsFloat(0);
					//                                    float sourceF = ResolveOpAsFloat(1);
					//                                    destF = (float)MathUtil.Pow(destF, sourceF);
					//                                    Dest.iData += MathUtil.floatToInt(destF);
					//                                }
					break;
				// 与
				case INSTR_AND:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData &= ResolveOpAsInt(1);
					}
					break;
				// 或
				case INSTR_OR:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData |= ResolveOpAsInt(1);
					}
					break;
				//异或
				case INSTR_XOR:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData ^= ResolveOpAsInt(1);
					}
					break;
				// 左移
				case INSTR_SHL:
					if (Dest.iType == OP_TYPE_INT && Source.iType == OP_TYPE_INT)
					{
						Dest.iData <<= ResolveOpAsInt(1);
					}
					break;
				// 右移
				case INSTR_SHR:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData >>= ResolveOpAsInt(1);
					}
					break;
				}
				//打印操作数
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				break;
			}
			// ---- 一元运算
			case INSTR_NEG:
			case INSTR_NOT:
			case INSTR_INC:
			case INSTR_DEC:
			{
				// 获得操作数类型
				int iDestStoreType = GetOpType(0);
				// 获得目标操作数
				C2D_Value Dest = ResolveOpValue(0);
				switch (iOpcode)
				{
				// 取负
				case INSTR_NEG:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData = -Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(-destF);
					}
					//#endif
					break;
				// 取反
				case INSTR_NOT:
					if (Dest.iType == OP_TYPE_INT)
					{
						Dest.iData = ~Dest.iData;
					}
					break;
				// 自增
				case INSTR_INC:
					if (Dest.iType == OP_TYPE_INT)
					{
						++Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(destF + 1);
					}
					//#endif
					break;
				// 自减
				case INSTR_DEC:
					if (Dest.iType == OP_TYPE_INT)
					{
						--Dest.iData;
					}
					//#if Platform=="Android"
					else
					{
						float destF = ResolveOpAsFloat(0);
						Dest.iData = C2D_Math.floatToIntBits(destF - 1);
					}
					//#endif
					break;
				}
				//打印操作数
				PrintOpIndir(0);
				break;
			}
			// ---- 字符处理
			case INSTR_CONCAT:
			{
				// 获得操作数
				C2D_Value Dest = ResolveOpValue(0);
				//获得字符串拷贝
				String pstrSourceString = ResolveOpAsString(1);
				//如果目标值类型不是字符串，则什么也不做
				if (Dest.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				String newString = ResolveOpAsString(0) + pstrSourceString;
				//如果是动态字符串，删除原先的文字
				currentThread.releaseValueString(Dest);
				int key = currentThread.addString(newString); //添加新的文字
				Dest.setAsDynamicString(key);
				//打印操作数
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				break;
			}
			case INSTR_GETCHAR:
			{
				//获得操作数
				C2D_Value Dest = ResolveOpValue(0);
				//如果目标值类型不是字符串，则什么也不做
				if (Dest.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				//解析源字符串,
				String pstrSourceString = ResolveOpAsString(1);
				//解析截取索引
				int iSourceIndex = ResolveOpAsInt(2);
				String newString = pstrSourceString.charAt(iSourceIndex) + "";
				currentThread.releaseValueString(Dest);
				int key = currentThread.addString(newString); //添加新的文字
				Dest.setAsDynamicString(key);
				//打印操作数
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				break;
			}
			case INSTR_SETCHAR:
			{
				// 获得目标索引
				int iDestIndex = ResolveOpAsInt(1);
				// 验证类型
				C2D_Value value = ResolveOpValue(0);
				if (value.iType != OP_TYPE_STRING_INDEX)
				{
					break;
				}
				// 获得源字符串
				String pstrSourceString = ResolveOpAsString(2);
				//设置
				int key = value.iData;
				if (value.iOffsetIndex < 0)
				{
					String oldString = ResolveOpAsString(0);
					String newString = oldString.substring(0, iDestIndex) + pstrSourceString.charAt(0) + oldString.substring(iDestIndex + 1, oldString.length() - (iDestIndex + 1));
					currentThread.setString(key, newString);
				}
				else
				{
					print("error: can't modify static string");
					currentThread.isRunning = false;
					iExitLoop = true;
				}
				//打印操作数
				PrintOpIndir(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				break;
			}
			// ---- 条件分支
			case INSTR_JMP:
			{
				//打印操作数
				PrintOpValue(0);
				int iTargetIndex = ResolveOpAsInstrIndex(0);
				currentThread.iCurrInstr = iTargetIndex;
				break;
			}
			case INSTR_JE:
			case INSTR_JNE:
			case INSTR_JG:
			case INSTR_JL:
			case INSTR_JGE:
			case INSTR_JLE:
			{
				//获取两个操作数
				C2D_Value Op0 = ResolveOpValue(0);
				C2D_Value Op1 = ResolveOpValue(1);
				//获取目标指令索引
				int iTargetIndex = ResolveOpAsInstrIndex(2);
				//跳转判断
				boolean iJump = false;
				//#if Platform=="Android"
				double d0, d1;
				//#else
//@								int d0 = 0, d1 = 0;
				//#endif
				switch (iOpcode)
				{
				//如果相等的话跳转
				case INSTR_JE:
				{
					switch (Op0.iType)
					{
					case OP_TYPE_INT:
						if (Op0.iData == Op1.iData)
						{
							iJump = true;
						}
						break;
					//#if Platform=="Android"
					case OP_TYPE_FLOAT:
						if (Op0.iData == Op1.iData)
						{
							iJump = true;
						}
						break;
					//#endif
					case OP_TYPE_STRING_INDEX:
						String string0 = (String) currentThread.getString(Op0);
						String string1 = (String) currentThread.getString(Op1);
						if (string0.equals(string1))
						{
							iJump = true;
						}
						break;
					}
					break;
				}
				//如果不相等跳转
				case INSTR_JNE:
				{
					switch (Op0.iType)
					{
					case OP_TYPE_INT:
						if (Op0.iData != Op1.iData)
						{
							iJump = true;
						}
						break;
					//#if Platform=="Android"
					case OP_TYPE_FLOAT:
						if (Op0.iData != Op1.iData)
						{
							iJump = true;
						}
						break;
					//#endif
					case OP_TYPE_STRING_INDEX:
						String string0 = (String) currentThread.getString(Op0);
						String string1 = (String) currentThread.getString(Op1);
						if (!string0.equals(string1))
						{
							iJump = true;
						}
						break;
					}
					break;
				}
				//如果大于跳转
				case INSTR_JG:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 > d1)
					{
						iJump = true;
					}
					break;
				// 如果小于跳转
				case INSTR_JL:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 < d1)
					{
						iJump = true;
					}
					break;
				// 大于等于跳转
				case INSTR_JGE:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 >= d1)
					{
						iJump = true;
					}
					break;
				// 小于等于跳转
				case INSTR_JLE:
					if (Op0.iType == OP_TYPE_INT)
					{
						d0 = Op0.iData;
					}
					//#if Platform=="Android"
					else
					{
						d0 = ResolveOpAsFloat(0);
					}
					//#endif
					if (Op1.iType == OP_TYPE_INT)
					{
						d1 = Op1.iData;
					}
					//#if Platform=="Android"
					else
					{
						d1 = ResolveOpAsFloat(1);
					}
					//#endif
					if (d0 <= d1)
					{
						iJump = true;
					}
					break;
				}
				//打印操作数
				PrintOpValue(0);
				printDebugInfor(", ");
				PrintOpValue(1);
				printDebugInfor(", ");
				PrintOpValue(2);
				printDebugInfor(" ");
				//如果决定跳转
				if (iJump)
				{
					currentThread.iCurrInstr = iTargetIndex;
					printDebugInfor("(True)");
				}
				else
				{
					printDebugInfor("(False)");
				}
				break;
			}
			// ---- 栈操作接口
			case INSTR_PUSH:
			{
				// 获得操作数
				C2D_Value Source = ResolveOpValue(0);
				C2D_Value Dest = new C2D_Value();
				CopyValue(Dest, Source);
				//将操作数压栈
				currentThread.Stack.Push(Dest);
				//打印操作数
				PrintOpValue(0);
				break;
			}
			case INSTR_POP:
			{
				//弹出栈顶元素到指定操作数
				C2D_Value dest = ResolveOpPntr(0);
				C2D_Value src = currentThread.Stack.Peek();
				CopyValue(dest, src);
				currentThread.Stack.Pop();
				//打印目标
				PrintOpIndir(0);
				break;
			}
			// ---- 函数调用接口
			case INSTR_CALL:
			{
				//根据第一个操作数获得函数
				int iFuncIndex = ResolveOpAsFuncIndex(0);
				//立刻将指令指针移至函数调用后的位置
				++currentThread.iCurrInstr;
				//调用函数
				CallFunc(iFuncIndex);
				break;
			}
			case INSTR_RET:
			{
				//从当前栈顶元素中获得当前函数索引，使用它获得相应的函数结构
				C2D_Value FuncIndex = currentThread.Stack.Peek();
				currentThread.Stack.Pop();
				//检查是否有异步退出标志
				if (FuncIndex.iType == OP_TYPE_RET_ASYN)
				{
					iExitLoop = true;
				}
				//检查是否有同步退出标志/*这里不应该停止线程*/
				//if (FuncIndex.iType == OP_TYPE_RET_SYN)
				//{
				//    currentThread.iIsRunning = false;
				//}
				// 获得调用前函数的索引
				C2D_Func CurrFunc = GetFunc(FuncIndex.iData);
				int iFrameIndex = FuncIndex.iOffsetIndex;
				//从栈中读取返回地址结构，它位于当前局部数据块的下方一个单元
				C2D_Value ReturnAddr = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - (CurrFunc.iLocalDataSize + 1));
				//弹出栈框架，包括返回地址一起
				currentThread.Stack.PopFrame(CurrFunc.iStackFrameSize);
				//恢复原函数的栈框架索引
				currentThread.Stack.iFrameIndex = iFrameIndex;
				//跳转到返回地址
				currentThread.iCurrInstr = ReturnAddr.iData;
				//打印返回地址
				printDebugInfor("" + ReturnAddr.iData);
				/*检查是否应该停止线程，如果线程不是从main函数启动，而是根本就没有main函数，应用程序使用
				 同步或者异步调用的方式启动了线程的话，而且到此栈应该为空的话，说明并非从main函数启动，
				 我们可以停止这个线程的活动了。如果从main函数启动的话，应该等到Exit指令来停止线程活动。*/
				if (currentThread.Stack.iFrameIndex == 0)
				{
					currentThread.isRunning = false;
					iExitLoop = true;
				}
				break;
			}
			case INSTR_CALLHOST:
			{
				//操作数0保存了API函数ID
				C2D_Value HostAPICall = ResolveOpValue(0);
				int iHostAPICallIndex = HostAPICall.iData;
				if (currentThread.getExcutor().m_funHandler != null)
				{
					currentThread.getExcutor().m_funHandler.handleFunction(currentThread, iHostAPICallIndex);
				}
				C2D_ScriptManager scriptManager = currentThread.getExcutor().m_scripManager;
				//弹出压栈的Host参数
				if (iHostAPICallIndex >= 0 && iHostAPICallIndex < scriptManager.g_HostsLen.length)
				{
					for (int i = 0; i < scriptManager.g_HostsLen[iHostAPICallIndex]; i++)
					{
						currentThread.Stack.Pop();
					}
				}
				else
				{
					print("-error host address-");
					return;
				}
				String sw = currentThread.getSwitchTo();
				if (sw != null)
				{
					currentThread.getExcutor().switchToThread(sw);
				}
				break;
			}
			// ---- 其它
			//        case INSTR_PAUSE:
			//        {
			//          // 如果已经暂停，什么也不做
			//
			//          if (currentThread.iIsPaused)
			//          {
			//            break;
			//          }
			//
			//          //获得暂停时长
			//
			//          int iPauseDuration = ResolveOpAsInt(0);
			//
			//          //决定恢复时间
			//
			//          currentThread.iPauseEndTime = System.currentTimeMillis() +
			//              iPauseDuration;
			//
			//          //暂停脚本
			//
			//          currentThread.iIsPaused = true;
			//
			//          //打印暂停时长
			//
			//          PrintOpValue(0);
			//          break;
			//        }
			case INSTR_EXIT:
				//从操作数0获取退出码
				C2D_Value ExitCode = ResolveOpValue(0);
				int iExitCode = ExitCode.iData;
				//退出执行循环
				iExitLoop = true;
				//打印退出码
				PrintOpValue(0);
				//只有main函数的结尾，才会遇到exit指令，这时候应该停止线程运行
				currentThread.isRunning = false;
				break;
			case INSTR_PRINT:
				//从操作数0获取打印信息
				String infor = ResolveOpAsString(0);
				printDebugInfor(infor);
				//打印信息
				PrintOpValue(0);
				break;
			}
			printDebugInfor("\n");
			//            currentThread.displayStringTable(false);
			//如果当前指令指针没有被指令改变，则增加1
			if (iCurrInstr == currentThread.iCurrInstr)
			{
				++currentThread.iCurrInstr;
			}
			//退出执行循环判断
			if (iExitLoop)
			{
				break;
			}
		}
		currentThread = null;
	}

	/**
	 * CopyValue () 拷贝一个Vlaue结构到另外一个，加入字符计算.
	 * 
	 * @param pDest
	 *            Value
	 * @param Source
	 *            Value
	 */
	private static void CopyValue(C2D_Value pDest, C2D_Value Source)
	{
		//先删除原先的字符串(如果有的话)
		currentThread.releaseValueString(pDest);
		//拷贝数值
		pDest.iType = Source.iType;
		pDest.iData = Source.iData;
		pDest.iOffsetIndex = Source.iOffsetIndex;
		//创建新的字符串(如果需要的话)
		if (pDest.iType == OP_TYPE_STRING_INDEX)
		{
			String sNew = currentThread.getString(pDest) + "";
			int keyNew = currentThread.addString(sNew);
			pDest.setAsDynamicString(keyNew);
		}
	}

	/**
	 * CoereceValueToInt () 将一个数值强制转换成整型值.
	 * 
	 * @param Val
	 *            Value
	 * @return int
	 */
	static int CoerceValueToInt(C2D_Value Val)
	{
		// 根据类型处理
		switch (Val.iType)
		{
		// 整型直接返回
		case OP_TYPE_INT:
			return Val.iData;
			// 浮点型
			//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float fFloat = CoerceValueToFloat(Val);
			return (int) fFloat;
			//#endif
			// 字符串
		case OP_TYPE_STRING_INDEX:
			return C2D_Math.stringToInt((String) currentThread.getString(Val));
			//其它均非法
		default:
			return 0;
		}
	}

	//#if Platform=="Android"
	/**
	 * CoereceValueToFloat () 强制转换一个Value结构成浮点数据.
	 * 
	 * @param Val
	 *            Value
	 * @return float
	 */
	static float CoerceValueToFloat(C2D_Value Val)
	{
		// 根据类型解析
		switch (Val.iType)
		{
		//整型，使用强制转换
		case OP_TYPE_INT:
			return (float) Val.iData;
			//浮点型，使用格式转换
		case OP_TYPE_FLOAT:
			return C2D_Math.intBitsToFloat(Val.iData);
			//字符型
		case OP_TYPE_STRING_INDEX:
			return (float) C2D_Math.stringToFloat((String) currentThread.getString(Val));
			//其它都非法
		default:
			return 0;
		}
	}

	//#endif
	/**
	 * CoereceValueToString () 强制转换一个Value结构为字符串.
	 * 
	 * @param Val
	 *            Value
	 * @return String
	 */
	static String CoerceValueToString(C2D_Value Val)
	{
		// 根据Vlaue当前的数据类型
		switch (Val.iType)
		{
		case OP_TYPE_INT:
			return Val.iData + "";
			//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float f = C2D_Math.intBitsToFloat(Val.iData);
			return f + "";
			//#endif
		case OP_TYPE_STRING_INDEX:
			return (String) currentThread.getString(Val);
		default:
			return null;
		}
	}

	/**
	 * GetOpType () 获取当前指令的指定操作数的操作数类型.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int GetOpType(int iOpIndex)
	{
		//获取当前指令
		int iCurrInstr = currentThread.iCurrInstr;
		//获取操作数类型
		return currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex].iType;
	}

	/**
	 * ResolveOpStackIndex () 分析操作数的站索引，不论是绝对还是相对.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpStackIndex(int iOpIndex)
	{
		// 得到当前指令
		int iCurrInstr = currentThread.iCurrInstr;
		// 得到操作数类型
		C2D_Value OpValue = currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex];
		//根据类型分析栈索引
		switch (OpValue.iType)
		{
		//绝对栈索引
		case OP_TYPE_ABS_STACK_INDEX:
			return OpValue.iData;
			//相对栈索引，分析它
		case OP_TYPE_REL_STACK_INDEX:
		{
			// 首先是基地址
			int iBaseIndex = OpValue.iData;
			// 再获得变量索引
			int iOffsetIndex = OpValue.iOffsetIndex;
			// 获得变量值
			C2D_Value StackValue = currentThread.Stack.GetStackValue(iOffsetIndex);
			// 现在，在基地址上增加当前变量值来获得绝对地址，注意使用减号
			return iBaseIndex - StackValue.iData;
		}
		// 其他情况返回0，不过我们不应该碰到这种情况
		default:
			return 0;
		}
	}

	/**
	 * ResolveOpValue () 解析操作数，返回相关的数值结构.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return Value
	 */
	private static C2D_Value ResolveOpValue(int iOpIndex)
	{
		// 获得当前指令
		int iCurrInstr = currentThread.iCurrInstr;
		// 获取操作数
		if (iCurrInstr < 0 || iCurrInstr >= currentThread.scriptData.pInstrs.length || iOpIndex < 0 || iOpIndex >= currentThread.scriptData.pInstrs[iCurrInstr].pOpList.length)
		{
			C2D_Debug.log("error");
		}
		C2D_Value OpValue = currentThread.scriptData.pInstrs[iCurrInstr].pOpList[iOpIndex];
		//根据操作数类型，决定返回值
		switch (OpValue.iType)
		{
		// 栈索引
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			//分析索引，使用它返回本栈元素指向的相应的栈元素
			int iAbsIndex = ResolveOpStackIndex(iOpIndex);
			return currentThread.Stack.GetStackValue(iAbsIndex);
		}
		// 寄存器
		case OP_TYPE_REG:
			return currentThread._RetVal;
			// 其它情况都可以当前栈元素
		default:
			return OpValue;
		}
	}

	/**
	 * ResolveOpType () 返回当前指令指定操作数的操作数类型 resolved type.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	protected static int ResolveOpType(int iOpIndex)
	{
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		return OpValue.iType;
	}

	/**
	 * ResolveOpAsInt () 将操作数值强制解析成整型数据.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsInt(int iOpIndex)
	{
		//获得操作数
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//强制转换成整型数据
		int iInt = CoerceValueToInt(OpValue);
		return iInt;
	}

	//#if Platform=="Android"
	/**
	 * ResolveOpAsFloat () 将操作数值强制解析成浮点型数据.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return float
	 */
	private static float ResolveOpAsFloat(int iOpIndex)
	{
		//获得操作数
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//强制转换成浮点数据
		float fFloat = CoerceValueToFloat(OpValue);
		return fFloat;
	}

	//#endif
	/**
	 * ResolveOpAsString () 解析并强制装换操作数成字符串.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return String
	 */
	private static String ResolveOpAsString(int iOpIndex)
	{
		//获得操作数
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//强制转换成字符串后返回
		String pstrString = CoerceValueToString(OpValue);
		return pstrString;
	}

	/**
	 * ResolveOpAsInstrIndex () 强制转换操作数为指令索引.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsInstrIndex(int iOpIndex)
	{
		//获得数值
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		//返回数值
		return OpValue.iData;
	}

	/**
	 * ResolveOpAsFuncIndex () 强制转换操作数为函数ID.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return int
	 */
	private static int ResolveOpAsFuncIndex(int iOpIndex)
	{
		// 获取操作数
		C2D_Value OpValue = ResolveOpValue(iOpIndex);
		// 返回函数ID
		return OpValue.iData;
	}

	/**
	 * ResolveOpPntr () 分析操作数并返回一个指向它的结构的指针，只允许是寄存器或者栈地址类型.
	 * 
	 * @param iOpIndex
	 *            int
	 * @return Value
	 */
	private static C2D_Value ResolveOpPntr(int iOpIndex)
	{
		//获得操作数类型
		int iIndirMethod = GetOpType(iOpIndex);
		// 根据类型返回指针
		switch (iIndirMethod)
		{
		//寄存器
		case OP_TYPE_REG:
			return currentThread._RetVal;
			//栈索引
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			int iStackIndex = ResolveOpStackIndex(iOpIndex);
			return currentThread.Stack.pElmnts[ResolveStackIndex(iStackIndex)];
		}
		}
		// Return null for anything else
		return null;
	}

	/**
	 * GetFunc () Returns the function corresponding to the specified index.
	 * 
	 * @param iIndex
	 *            int
	 * @return Func
	 */
	private static C2D_Func GetFunc(int iIndex)
	{
		return currentThread.scriptData.pFuncTable[iIndex];
	}

	/**
	 * CallFunc () 函数调用.
	 * 
	 * @param iIndex
	 *            int
	 */
	private static void CallFunc(int iIndex)
	{
		C2D_Func DestFunc = GetFunc(iIndex);
		//保存栈框架索引
		int iFrameIndex = currentThread.Stack.iFrameIndex;
		//将返回地址，即当前指令指针压栈
		C2D_Value ReturnAddr = new C2D_Value();
		ReturnAddr.iData = currentThread.iCurrInstr;
		currentThread.Stack.Push(ReturnAddr);
		//压入局部数据空间+1的大小(多余的1作为返回函数索引)
		currentThread.Stack.PushFrame(DestFunc.iLocalDataSize + 1);
		//将返回函数索引和原栈框架索引存放在栈顶元素中
		C2D_Value FuncIndex = new C2D_Value();
		FuncIndex.iData = iIndex;
		FuncIndex.iOffsetIndex = iFrameIndex;
		currentThread.Stack.SetStackValue(currentThread.Stack.iTopIndex - 1, FuncIndex);
		//跳转到指定的函数入口
		currentThread.iCurrInstr = DestFunc.iEntryPoint;
	}

	/**
	 * 根据函数名返回函数ID.
	 * 
	 * @param callThread
	 * @param pstrName
	 *            String
	 * @return int
	 */
	static int GetFuncIndexByName(C2D_Thread callThread, String pstrName)
	{
		if(callThread!=null)
		{
			//遍历查找
			for (int iFuncIndex = 0; iFuncIndex < callThread.scriptData.pFuncTable.length; ++iFuncIndex)
			{
				if (pstrName.equals(callThread.scriptData.pFuncTable[iFuncIndex].iFunctionName))
				{
					return iFuncIndex;
				}
			}
		}
		//没有找到返回-1
		return -1;
	}

	/**
	 * 从主应用程序调用一个脚本函数，异步调用，立刻给予时间片运行，直到其返回.
	 * 
	 * @param callThread
	 *            int
	 * @param pstrName
	 *            String
	 */
	public static void C2DS_CallScriptFunc(C2D_Thread callThread, String pstrName)
	{
		//保存VM的当前状态
		C2D_Thread prevThread = currentThread;
		currentThread = callThread;
		//根据名字获得函数ID
		int iFuncIndex = GetFuncIndexByName(callThread,pstrName);
		//确定函数存在
		if (iFuncIndex == -1)
		{
			return;
		}
		//调用函数
		CallFunc(iFuncIndex);
		//设置异步退出标志
		C2D_Value StackBase = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - 1);
		StackBase.iType = OP_TYPE_RET_ASYN;
		//SetStackValue(g_iCurrThread, currentThread.Stack.iTopIndex - 1, StackBase);
		callThread.isRunning = true;
		//允许脚本代码不中断运行直到其返回
		C2DS_RunScript(callThread);
		//---- 处理函数返回
		//恢复VM状态
		currentThread = prevThread;
	}

	/**
	 * 从主应用程序唤起一个脚本函数，意思是调用与脚本以同步方式执行
	 * (此函数相当于在指定脚本线程中当前执行位置强行插入一个函数调用，函数此时并不立刻执行，
	 * 只是设置好函数入口点等信息，在其线程获得时间片后才执行和返回。注意这种方式可能带来的问题。).
	 * 
	 * @param pstrName
	 *            String
	 */
	public static void C2DS_InvokeScriptFunc(C2D_Thread callThread, String pstrName)
	{
		//根据函数名获得函数ID
		int iFuncIndex = GetFuncIndexByName(callThread,pstrName);
		//确定函数存在
		if (iFuncIndex == -1)
		{
			return;
		}
		callThread.isRunning = true;
		//调用函数
		CallFunc(iFuncIndex);
		//设置同步退出表
		C2D_Value StackBase = currentThread.Stack.GetStackValue(currentThread.Stack.iTopIndex - 1);
		StackBase.iType = OP_TYPE_RET_SYN;
		//SetStackValue(g_iCurrThread, currentThread.Stack.iTopIndex - 1, StackBase);
	}

	/**
	 * PrintOpIndir () 打印栈地址.
	 * 
	 * @param iOpIndex
	 *            int
	 */
	private static void PrintOpIndir(int iOpIndex)
	{
		//获取操作数类型
		int iIndirMethod = GetOpType(iOpIndex);
		//打印
		switch (iIndirMethod)
		{
		//寄存器
		case OP_TYPE_REG:
			printDebugInfor("_RetVal");
			break;
		//栈索引
		case OP_TYPE_ABS_STACK_INDEX:
		case OP_TYPE_REL_STACK_INDEX:
		{
			int iStackIndex = ResolveOpStackIndex(iOpIndex);
			printDebugInfor("[ " + iStackIndex + " ]");
			break;
		}
		}
	}

	/**
	 * PrintOpValue () 打印操作数的值.
	 * 
	 * @param iOpIndex
	 *            int
	 */
	private static void PrintOpValue(int iOpIndex)
	{
		// 获得操作数
		C2D_Value Op = ResolveOpValue(iOpIndex);
		// 打印
		switch (Op.iType)
		{
		case OP_TYPE_NULL:
			printDebugInfor("Null");
			break;
		case OP_TYPE_INT:
			printDebugInfor("" + Op.iData);
			break;
		//#if Platform=="Android"
		case OP_TYPE_FLOAT:
			float f = ResolveOpAsFloat(iOpIndex);
			printDebugInfor("" + f);
			break;
		//#endif
		case OP_TYPE_STRING_INDEX:
			printDebugInfor("\"" + ResolveOpAsString(iOpIndex) + "\"");
			break;
		case OP_TYPE_INSTR_INDEX:
			printDebugInfor("" + Op.iData);
			break;
		case OP_TYPE_HOST_API_CALL_INDEX:
		{
			printDebugInfor("call host " + iOpIndex);
			break;
		}
		}
	}

	//向控制台输出信息
	/**
	 * Prints the.
	 * 
	 * @param s
	 *            the s
	 */
	static void print(String s)
	{
		//    MiscUtil.println(s);
	}

	//输出debug信息
	/**
	 * Prints the debug infor.
	 * 
	 * @param s
	 *            the s
	 */
	private static void printDebugInfor(String s)
	{
		//    C2D_MiscUtil.log(s);
	}
}
