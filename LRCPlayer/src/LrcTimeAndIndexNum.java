/**
 * 用于保存歌词时间与歌词容器的下标索引
 */
public class LrcTimeAndIndexNum {
	/**
	 * 该行歌词的显示时间
	 * 
	 * @uml.property name="lrcTime"
	 */
	private float lrcTime;

	/**
	 * 该行歌词行索引
	 * 
	 * @uml.property name="lrcIndex"
	 */
	private int lrcIndex;

	/**
	 * 默认的构造器
	 */
	public LrcTimeAndIndexNum() {

	}

	/**
	 * 带参数的构造器，设置时间标签和对应的歌词行索引
	 * 
	 * @param lrcTime
	 *            时间标签
	 * @param lrcIndex
	 *            歌词行索引
	 */
	public LrcTimeAndIndexNum(float lrcTime, int lrcIndex) {
		this.lrcTime = lrcTime;
		this.lrcIndex = lrcIndex;
		
	}

	/**
	 * 返回歌词应该出现的时间
	 * 
	 * @return lrcTime 出现时间
	 * @uml.property name="lrcTime"
	 */
	public float getLrcTime() {
		return lrcTime;
	}

	/**
	 * 返回歌词内容行索引
	 * 
	 * @return 歌词内容行索引
	 * @uml.property name="lrcIndex"
	 */
	public int getLrcIndex() {
		return lrcIndex;
	}
}