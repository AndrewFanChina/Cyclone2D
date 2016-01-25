package c2d.frame.com.list.scroll;

public interface C2D_Scrollable
{
	/** 滚动方式-页面滚动，只在当前页滚动，当光标到达页面起始或者结尾，再进行溢出滚动将会使控件丢失光标，当隐藏的条目需要显示时，必须通过切换成下一页面  */
	public static final int Scrool_Page=0;
	/** 滚动方式-条目滚动，只在能滚动的条目上滚动，当光标到达页面起始或者结尾，再进行溢出滚动将会自动更新页面内条目显示 ，但是不会进行首尾循环 */
	public static final int Scrool_Item=1;
	/** 滚动方式-条目滚动，只在能滚动的条目上滚动，当光标到达页面起始或者结尾，再进行溢出滚动将会自动更新页面内条目显示 ，会进行首尾循环 */
	public static final int Scrool_Loop=2;
	/**
	 * 向前滚动
	 * @return 是否执行了滚动
	 */
	boolean scrollFront();
	/**
	 * 向回滚动
	 * @return 是否执行了滚动
	 */
	boolean scrollBack();
	/**
	 * 获得当前滚动位置,设全部滚动距离L，滚动位置处于[0,L-1]区间
	 * @return 当前滚动位置
	 */
	int getCurrentScroll();
	/**
	 * 获得全部行数
	 * @return 全部全部行数
	 */
	int getTotalRow();
	/**
	 * 获得全部可以滚动的行号，文本只能进行首行滚动，
	 * 因此在如果文本行数<=页面容纳行数时，不可以进行
	 * 滚动，返回值皆为0。
	 * 当文本行数>页面容纳行数时，可以滚动行号=
	 * 文本行数-页面容纳行数。滚动光标可以到达滚动行号的
	 * 末尾。
	 * @return 可以滚动的行号
	 */
	int getTotalScroll();	
	/**
	 * 重置光标位置
	 */
	void resetScroll();
	/**
	 * 设置当前滚动位置
	 * @param scroll 滚动位置
	 */
	public void setCurrentScroll(int scroll);
}
