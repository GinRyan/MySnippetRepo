package com.hitfm.improve;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.widget.Toast;

/**
 * @author Himi
 */
public class SurfaceView_Cube extends SurfaceView implements Callback, Runnable {
	//标记程序是否初始化
	private boolean init = false;
	//图片大小，pix
	private final static int mTileSize = 36;
	//敌方图片数量
	private final static int mEnemyCount = 26;
	//sprite运动状态
	private final static int STOP = 0;
	private final static int LEFT = -1;
	private final static int RIGTH = 1;
	//程序运行状态
	private final static int END = 0;
	private final static int RUN = 1;
	private final static int PAUSE = 2;
	private int mstate = END;
	//系统变量
	private SurfaceHolder mSurfaceHolder;
	private Thread mMapThread;
	private Canvas mCanvas;
	private Paint mPaint;
	private Bitmap[] bitmaps;
	//系统参数，屏幕宽高，图片行列数量和偏移
	private int mScreenW, mScreenH;
	private int mXTile, mYTile;
	private int mXOffset, mYOffset;
	//存储图片所以，列(行)
	private ArrayList<int[]> mTileGrid;
	//第一张图片索引
	private int imgIndex = 0x7f020000;
	//随机产生敌机
	private Random random = new Random();
	//sprite
	private Sprite mSprite;
	//存储子弹参数
	private List<Point> mBullet = new ArrayList<Point>();
	//sprite和bullet图片索引
	private final static int mSpriteIndex = 27;
	private final static int mBulletIndex = 27;
	//表示此坐标无图片
	private final static int NOTHING = 0;
	//线控按键索引
	private final static int KEY_FIRE = KeyEvent.KEYCODE_HEADSETHOOK;
	private final static int KEY_LEFT = KeyEvent.KEYCODE_MEDIA_PREVIOUS;
	private final static int KEY_RIGHT = KeyEvent.KEYCODE_MEDIA_NEXT;
	//触发长按事件的时间
	private final static int LONG_PRESS_TIME = 500;
	//标记屏幕更新时间
	private long lastTime = 0;
	//现在时间
	private long now = 0;
	//sprite的更新间隔
	private int mDelay = 10;
	//子弹和敌机的更新间隔
	private int mEnemyAndBulletDelay=mDelay * 150;

	public SurfaceView_Cube(Context context) {
		super(context);
		mMapThread = new Thread(this);
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		mPaint = new Paint();
		//反锯齿
		mPaint.setAntiAlias(true);
		//屏幕常亮
		this.setKeepScreenOn(true);
	}

	@Override
	public void startAnimation(Animation animation) {
		super.startAnimation(animation);
	}

	/**
	 * 屏幕初始化时做的准备
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		initScreenPrams();
		loadTiles();
		initSprite();
		Toast.makeText(getContext(), R.string.start, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 初始化sprite，在屏幕最后一行的中间位置
	 */
	private void initSprite() {
		mSprite = new Sprite((mXTile - 1) / 2, mYTile - 1);
		mSprite.setDirection(STOP);
	}

	/**
	 * 加载所有图片，0-25为敌机，26为sprite
	 */
	private void loadTiles() {
		bitmaps = new Bitmap[mEnemyCount + 1];
		for (int i = 0; i < mEnemyCount + 1; i++) {
			bitmaps[i] = BitmapFactory.decodeResource(this.getResources(), imgIndex++);
			bitmaps[i] = resizeImage(bitmaps[i], mTileSize, mTileSize);
		}
	}

	/**
	 * 初始化屏幕参数
	 */
	private void initScreenPrams() {
		mScreenW = this.getWidth();
		mScreenH = this.getHeight();
		mXTile = mScreenW / mTileSize;
		mYTile = mScreenH / mTileSize;
		mXOffset = (mScreenW % mTileSize) / 2;
		mYOffset = (mScreenH % mTileSize) / 2;
		initTileGrid();
	}

	/**
	 * 初始化屏幕图片索引矩阵
	 */
	private void initTileGrid() {
		int[] tempXTile;
		mTileGrid = new ArrayList<int[]>();
		for (int i = 0; i < mYTile; i++) {
			tempXTile = new int[mXTile];
			mTileGrid.add(tempXTile);
		}
	}

	public void run() {
		while (true) {
			if (mstate == END || mstate == PAUSE) {
				continue;
			}
			try {
				mCanvas = mSurfaceHolder.lockCanvas();
				mCanvas.drawColor(Color.WHITE);
				updateSprite();
				updateEnemyAndBullet();
				drawAll();
				Thread.sleep(mDelay);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (mCanvas != null) {
					mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				}
			}
		}
	}

	/**
	 * 绘制屏幕
	 */
	private void drawAll() {
		int[] row;
		for (int i = 0; i < mYTile; i++) {
			row = mTileGrid.get(i);
			for (int j = 0; j < mXTile; j++) {
				if (row[j] != NOTHING) {
					drawBitmap(row[j], j, i, mPaint);
				}
			}
		}
	}

	/**
	 * 清除所有子弹
	 */
	private void clearBullet() {
		int x, y;
		for (int i = 0; i < mBullet.size(); i++) {
			x = mBullet.get(i).x;
			y = mBullet.get(i).y;
			if (y == mYTile - 1) {
				continue;
			}
			mTileGrid.get(y)[x] = NOTHING;
		}
	}

	/**
	 * 更新所有子弹位置
	 */
	private void updateBullet() {
		int x, y;
		int[] removeIndex = new int[mBullet.size()];
		if (!mBullet.isEmpty()) {
			for (int i = 0; i < mBullet.size(); i++) {
				x = mBullet.get(i).x;
				y = mBullet.get(i).y;
				// 子弹到达最顶部
				if (y == 0) {
					removeIndex[i] = 1;
				}
				// 子弹已经遇到障碍物
				else if (mTileGrid.get(y)[x] != NOTHING && mTileGrid.get(y)[x] != mBulletIndex) {
					removeIndex[i] = 1;
					mTileGrid.get(y)[x] = NOTHING;
				}
				// 子弹即将遇到障碍物
				else if (mTileGrid.get(y - 1)[x] != NOTHING && mTileGrid.get(y - 1)[x] != mBulletIndex) {
					removeIndex[i] = 1;
					mTileGrid.get(y - 1)[x] = NOTHING;
				}
				// 将子弹上移
				else if (mTileGrid.get(y - 1)[x] == NOTHING) {
					mBullet.set(i, new Point(x, y - 1));
					mTileGrid.get(y - 1)[x] = mBulletIndex;
				}
			}
			//将子弹从索引中移除
			for (int i = removeIndex.length - 1; i >= 0; i--) {
				if (removeIndex[i] == 1) {
					mBullet.remove(i);
				}
			}
		}

	}

	/**
	 * 更新sprite位置
	 */
	private void updateSprite() {
		int oldX = mSprite.getX();
		int newX = oldX + mSprite.getDirection();
		if (newX < 0 || newX >= mXTile) {
			newX = oldX;
		}
		int y = mYTile - 1;
		mTileGrid.get(y)[oldX] = NOTHING;
		mSprite.setXY(newX, y);
		mTileGrid.get(mTileGrid.size() - 1)[newX] = mSpriteIndex;
		mSprite.setDirection(STOP);
	}

	/**
	 * 在满足更新间隔的要求下更新敌机和子弹位置
	 */
	private void updateEnemyAndBullet() {
		now = System.currentTimeMillis();
		if ((now - lastTime) > (mEnemyAndBulletDelay)) {
			lastTime = System.currentTimeMillis();
			clearBullet();
			updateEnemy();
			updateBullet();
		}
	}

	/**
	 * 更新enemy，生成第一行，插入最前面，第二行到倒数第二行依次向下移动，再移除倒数第二行
	 */
	private void updateEnemy() {
		int randomX, randomEnemyType;
		int[] newFirstRow = new int[mXTile];
		for (int i = 0; i < mXTile / 4;) {
			randomX = random.nextInt(mXTile);
			randomEnemyType = random.nextInt(mEnemyCount + 1);
			if (newFirstRow[randomX] == NOTHING) {
				newFirstRow[randomX] = randomEnemyType;
				i++;
			}
		}
		mTileGrid.add(0, newFirstRow);
		mTileGrid.remove(mTileGrid.size() - 2);

	}

	/**
	 * 绘制bitmap
	 * @param bitmapIndex 图片索引
	 * @param x x轴坐标
	 * @param y y轴坐标
	 * @param paint 画笔
	 */
	private void drawBitmap(int bitmapIndex, int x, int y, Paint paint) {
		if (bitmapIndex >= 1 && bitmapIndex <= mEnemyCount + 1) {
			mCanvas.drawBitmap(bitmaps[bitmapIndex - 1], mXOffset + x * mTileSize, mYOffset + y * mTileSize, paint);
		}

	}

	/**
	 * 重置图片大小
	 * @param bitmap 需要被重置的图片
	 * @param w 新的宽度
	 * @param h 新的高度
	 * @return 重置后的图片
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//第一次按键，初始化线程
		if (!init) {
			mMapThread.start();
		}
		//设置按键方向
		setDirection(keyCode);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if ((event.getEventTime() - event.getDownTime()) > LONG_PRESS_TIME) {
			return this.onKeyLongPress(keyCode, event);
		}
		return true;
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 * 设置按键方向
	 * @param keyCode
	 */
	private void setDirection(int keyCode) {
		if (!init) {
			init = true;
			mstate = RUN;
		}
		switch (keyCode) {
		case KEY_LEFT:
			mSprite.setDirection(LEFT);
			break;
		case KEY_RIGHT:
			mSprite.setDirection(RIGTH);
			break;
		case KEY_FIRE:
			int xBullet = mSprite.getX();
			int yBullet = mSprite.getY();
			mBullet.add(new Point(xBullet, yBullet));
			mTileGrid.get(yBullet)[xBullet] = mBulletIndex;
			break;
		default:
			break;
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}
