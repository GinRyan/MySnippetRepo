package hong.specialEffects.wight;


import hong.specialEffects.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NinePointLineView extends View {

	Paint linePaint = new Paint();

	Paint whiteLinePaint = new Paint();

	Paint textPaint = new Paint();

	// 鐢变簬涓や釜鍥剧墖閮芥槸姝ｆ柟褰紝鎵�互鑾峰彇涓�釜闀垮害灏辫浜�	
	Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lock);
	int defaultBitmapRadius = defaultBitmap.getWidth() / 2;

	// 鍒濆鍖栬閫変腑鍥剧墖鐨勭洿寰勩�鍗婂緞
	Bitmap selectedBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.indicator_lock_area);
	int selectedBitmapDiameter = selectedBitmap.getWidth();
	int selectedBitmapRadius = selectedBitmapDiameter / 2;

	// 瀹氫箟濂�涓偣鐨勬暟缁�	
	PointInfo[] points = new PointInfo[9];

	// 鐩稿簲ACTION_DOWN鐨勯偅涓偣
	PointInfo startPoint = null;

	// 灞忓箷鐨勫楂�	
	int width, height;

	// 褰揂CTION_MOVE鏃惰幏鍙栫殑X锛孻鍧愭爣
	int moveX, moveY;

	// 鏄惁鍙戠敓ACTION_UP
	boolean isUp = false;

	// 鏈�粓鐢熸垚鐨勭敤鎴烽攣搴忓垪
	StringBuffer lockString = new StringBuffer();

	public NinePointLineView(Context context) {
		super(context);
		this.setBackgroundColor(Color.WHITE);
		initPaint();
	}

	public NinePointLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(Color.WHITE);
		initPaint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 鍒濆鍖栧睆骞曞ぇ灏�		
		width = getWidth();
		height = getHeight();
		if (width != 0 && height != 0) {
			initPoints(points);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	private int startX = 0, startY = 0;

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawText("鐢ㄦ埛鐨勬粦鍔ㄩ『搴忥細" + lockString, 0, 40, textPaint);

		if (moveX != 0 && moveY != 0 && startX != 0 && startY != 0) {
			// 缁樺埗褰撳墠娲诲姩鐨勭嚎娈�			drawLine(canvas, startX, startY, moveX, moveY);
		}

		drawNinePoint(canvas);

		super.onDraw(canvas);
	}

	// 璁颁綇锛岃繖涓狣OWN鍜孧OVE銆乁P鏄垚瀵圭殑锛屽鏋滄病浠嶶P閲婃斁锛屽氨涓嶄細鍐嶈幏寰桪OWN锛�	// 鑰岃幏寰桪OWN鏃讹紝涓�畾瑕佺‘璁ゆ秷璐硅浜嬩欢锛屽惁鍒橫OVE鍜孶P涓嶄細琚繖涓猇iew鐨刼nTouchEvent鎺ユ敹
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean flag = true;

		if (isUp) {// 濡傛灉宸叉粦瀹岋紝閲嶇疆姣忎釜鐐圭殑灞炴�鍜宭ockString

			finishDraw();

			// 褰揢P鍚庯紝瑕佽繑鍥瀎alse锛屾妸浜嬩欢閲婃斁缁欑郴缁燂紝鍚﹀垯鏃犳硶鑾峰緱Down浜嬩欢
			flag = false;

		} else {// 娌℃粦瀹岋紝鍒欑户缁粯鍒�
			handlingEvent(event);

			// 杩欓噷瑕佽繑鍥瀟rue锛屼唬琛ㄨView娑堣�姝や簨浠讹紝鍚﹀垯涓嶄細鏀跺埌MOVE鍜孶P浜嬩欢
			flag = true;

		}
		return flag;
	}

	private void handlingEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			moveX = (int) event.getX();
			moveY = (int) event.getY();
			for (PointInfo temp : points) {
				if (temp.isInMyPlace(moveX, moveY) && temp.isNotSelected()) {
					temp.setSelected(true);
					startX = temp.getCenterX();
					startY = temp.getCenterY();
					int len = lockString.length();
					if (len != 0) {
						int preId = lockString.charAt(len - 1) - 48;
						points[preId].setNextId(temp.getId());
					}
					lockString.append(temp.getId());
					break;
				}
			}

			invalidate(0, height - width, width, height);
			break;

		case MotionEvent.ACTION_DOWN:
			int downX = (int) event.getX();
			int downY = (int) event.getY();
			for (PointInfo temp : points) {
				if (temp.isInMyPlace(downX, downY)) {
					temp.setSelected(true);
					startPoint = temp;
					startX = temp.getCenterX();
					startY = temp.getCenterY();
					lockString.append(temp.getId());
					break;
				}
			}
			invalidate(0, height - width, width, height);
			break;

		case MotionEvent.ACTION_UP:
			startX = startY = moveX = moveY = 0;
			isUp = true;
			invalidate();
			break;
		default:
			break;
		}
	}

	private void finishDraw() {
		for (PointInfo temp : points) {
			temp.setSelected(false);
			temp.setNextId(temp.getId());
		}
		lockString.delete(0, lockString.length());
		isUp = false;
		invalidate();
	}

	private void initPoints(PointInfo[] points) {

		int len = points.length;

		int seletedSpacing = (width - selectedBitmapDiameter * 3) / 4;

		// 琚�鎷╂椂鏄剧ず鍥剧墖鐨勫乏涓婅鍧愭爣
		int seletedX = seletedSpacing;
		int seletedY = height - width + seletedSpacing;

		// 娌¤閫夋椂鍥剧墖鐨勫乏涓婅鍧愭爣
		int defaultX = seletedX + selectedBitmapRadius - defaultBitmapRadius;
		int defaultY = seletedY + selectedBitmapRadius - defaultBitmapRadius;

		// 缁樺埗濂芥瘡涓偣
		for (int i = 0; i < len; i++) {
			if (i == 3 || i == 6) {
				seletedX = seletedSpacing;
				seletedY += selectedBitmapDiameter + seletedSpacing;

				defaultX = seletedX + selectedBitmapRadius
						- defaultBitmapRadius;
				defaultY += selectedBitmapDiameter + seletedSpacing;

			}
			points[i] = new PointInfo(i, defaultX, defaultY, seletedX, seletedY);

			seletedX += selectedBitmapDiameter + seletedSpacing;
			defaultX += selectedBitmapDiameter + seletedSpacing;

		}
	}

	private void initPaint() {
		initLinePaint(linePaint);
		initTextPaint(textPaint);
		initWhiteLinePaint(whiteLinePaint);
	}

	/**
	 * 鍒濆鍖栨枃鏈敾绗�	 * @param paint
	 */
	private void initTextPaint(Paint paint) {
		textPaint.setTextSize(30);
		textPaint.setAntiAlias(true);
		textPaint.setTypeface(Typeface.MONOSPACE);
	}

	/**
	 * 鍒濆鍖栭粦绾跨敾绗�	 * 
	 * @param paint
	 */
	private void initLinePaint(Paint paint) {
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(defaultBitmap.getWidth());
		paint.setAntiAlias(true);
		paint.setStrokeCap(Cap.ROUND);
	}

	/**
	 * 鍒濆鍖栫櫧绾跨敾绗�	 * 
	 * @param paint
	 */
	private void initWhiteLinePaint(Paint paint) {
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(defaultBitmap.getWidth() - 5);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Cap.ROUND);

	}

	/**
	 * 缁樺埗宸插畬鎴愮殑閮ㄥ垎
	 * 
	 * @param canvas
	 */
	private void drawNinePoint(Canvas canvas) {

		if (startPoint != null) {
			drawEachLine(canvas, startPoint);
		}

		// 缁樺埗姣忎釜鐐圭殑鍥剧墖
		for(PointInfo pointInfo : points) {
			
			if (pointInfo.isSelected()) {// 缁樺埗澶у湀
				canvas.drawBitmap(selectedBitmap, pointInfo.getSeletedX(),pointInfo.getSeletedY(), null);
			}
			// 缁樺埗鐐�			
			canvas.drawBitmap(defaultBitmap, pointInfo.getDefaultX(),pointInfo.getDefaultY(), null);
		}

	}

	/**
	 * 閫掑綊缁樺埗姣忎袱涓偣涔嬮棿鐨勭嚎娈�	 * 
	 * @param canvas
	 * @param point
	 */
	private void drawEachLine(Canvas canvas, PointInfo point) {
		if (point.hasNextId()) {
			int n = point.getNextId();
			drawLine(canvas, point.getCenterX(), point.getCenterY(),
					points[n].getCenterX(), points[n].getCenterY());
			// 閫掑綊
			drawEachLine(canvas, points[n]);
		}
	}

	/**
	 * 鍏堢粯鍒堕粦绾匡紝鍐嶅湪涓婇潰缁樺埗鐧界嚎锛岃揪鍒伴粦杈圭櫧绾跨殑鏁堟灉
	 * 
	 * @param canvas
	 * @param startX
	 * @param startY
	 * @param stopX
	 * @param stopY
	 */
	private void drawLine(Canvas canvas, float startX, float startY,
			float stopX, float stopY) {
		canvas.drawLine(startX, startY, stopX, stopY, linePaint);
		canvas.drawLine(startX, startY, stopX, stopY, whiteLinePaint);
	}

	/**
	 * 鐢ㄦ潵琛ㄧず涓�釜鐐�	 * 
	 * @author zkwlx
	 * 
	 */
	private class PointInfo {

		// 涓�釜鐐圭殑ID
		private int id;

		// 褰撳墠鐐规墍鎸囧悜鐨勪笅涓�釜鐐圭殑ID锛屽綋娌℃湁鏃朵负鑷繁ID
		private int nextId;

		// 鏄惁琚�涓�		
		private boolean selected;

		// 榛樿鏃跺浘鐗囩殑宸︿笂瑙扻鍧愭爣
		private int defaultX;

		// 榛樿鏃跺浘鐗囩殑宸︿笂瑙扽鍧愭爣
		private int defaultY;

		// 琚�涓椂鍥剧墖鐨勫乏涓婅X鍧愭爣
		private int seletedX;

		// 琚�涓椂鍥剧墖鐨勫乏涓婅Y鍧愭爣
		private int seletedY;

		public PointInfo(int id, int defaultX, int defaultY, int seletedX,
				int seletedY) {
			this.id = id;
			this.nextId = id;
			this.defaultX = defaultX;
			this.defaultY = defaultY;
			this.seletedX = seletedX;
			this.seletedY = seletedY;
		}

		public boolean isSelected() {
			return selected;
		}

		public boolean isNotSelected() {
			return !isSelected();
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public int getId() {
			return id;
		}

		public int getDefaultX() {
			return defaultX;
		}

		public int getDefaultY() {
			return defaultY;
		}

		public int getSeletedX() {
			return seletedX;
		}

		public int getSeletedY() {
			return seletedY;
		}

		public int getCenterX() {
			return seletedX + selectedBitmapRadius;
		}

		public int getCenterY() {
			return seletedY + selectedBitmapRadius;
		}

		public boolean hasNextId() {
			return nextId != id;
		}

		public int getNextId() {
			return nextId;
		}

		public void setNextId(int nextId) {
			this.nextId = nextId;
		}

		/**
		 * 鍧愭爣(x,y)鏄惁鍦ㄥ綋鍓嶇偣鐨勮寖鍥村唴
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		public boolean isInMyPlace(int x, int y) {
			boolean inX = x > seletedX
					&& x < (seletedX + selectedBitmapDiameter);
			boolean inY = y > seletedY
					&& y < (seletedY + selectedBitmapDiameter);

			return (inX && inY);
		}

	}

}
