/*     */ package android.support.v4.view;
/*     */ 
/*     */ import android.os.Build;
import android.view.MotionEvent;
/*     */ 
/*     */ public class MotionEventCompat
/*     */ {
/*     */   static final MotionEventVersionImpl IMPL;
/*     */   public static final int ACTION_MASK = 255;
/*     */   public static final int ACTION_POINTER_DOWN = 5;
/*     */   public static final int ACTION_POINTER_UP = 6;
/*     */   public static final int ACTION_HOVER_MOVE = 7;
/*     */   public static final int ACTION_SCROLL = 8;
/*     */   public static final int ACTION_POINTER_INDEX_MASK = 65280;
/*     */   public static final int ACTION_POINTER_INDEX_SHIFT = 8;
/*     */ 
/*     */   public static int getActionMasked(MotionEvent event)
/*     */   {
/* 133 */     return event.getAction() & 0xFF;
/*     */   }
/*     */ 
/*     */   public static int getActionIndex(MotionEvent event)
/*     */   {
/* 141 */     return (event.getAction() & 0xFF00) >> 8;
/*     */   }
/*     */ 
/*     */   public static int findPointerIndex(MotionEvent event, int pointerId)
/*     */   {
/* 151 */     return IMPL.findPointerIndex(event, pointerId);
/*     */   }
/*     */ 
/*     */   public static int getPointerId(MotionEvent event, int pointerIndex)
/*     */   {
/* 160 */     return IMPL.getPointerId(event, pointerIndex);
/*     */   }
/*     */ 
/*     */   public static float getX(MotionEvent event, int pointerIndex)
/*     */   {
/* 169 */     return IMPL.getX(event, pointerIndex);
/*     */   }
/*     */ 
/*     */   public static float getY(MotionEvent event, int pointerIndex)
/*     */   {
/* 178 */     return IMPL.getY(event, pointerIndex);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  84 */     if (Build.VERSION.SDK_INT >= 5)
/*  85 */       IMPL = new EclairMotionEventVersionImpl();
/*     */     else
/*  87 */       IMPL = new BaseMotionEventVersionImpl();
/*     */   }
/*     */ 
/*     */   static class EclairMotionEventVersionImpl
/*     */     implements MotionEventCompat.MotionEventVersionImpl
/*     */   {
/*     */     public int findPointerIndex(MotionEvent event, int pointerId)
/*     */     {
/*  63 */       return MotionEventCompatEclair.findPointerIndex(event, pointerId);
/*     */     }
/*     */ 
/*     */     public int getPointerId(MotionEvent event, int pointerIndex) {
/*  67 */       return MotionEventCompatEclair.getPointerId(event, pointerIndex);
/*     */     }
/*     */ 
/*     */     public float getX(MotionEvent event, int pointerIndex) {
/*  71 */       return MotionEventCompatEclair.getX(event, pointerIndex);
/*     */     }
/*     */ 
/*     */     public float getY(MotionEvent event, int pointerIndex) {
/*  75 */       return MotionEventCompatEclair.getY(event, pointerIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class BaseMotionEventVersionImpl
/*     */     implements MotionEventCompat.MotionEventVersionImpl
/*     */   {
/*     */     public int findPointerIndex(MotionEvent event, int pointerId)
/*     */     {
/*  41 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getPointerId(MotionEvent event, int pointerIndex) {
/*  45 */       throw new IndexOutOfBoundsException("Pre-Eclair does not support pointers");
/*     */     }
/*     */ 
/*     */     public float getX(MotionEvent event, int pointerIndex) {
/*  49 */       throw new IndexOutOfBoundsException("Pre-Eclair does not support pointers");
/*     */     }
/*     */ 
/*     */     public float getY(MotionEvent event, int pointerIndex) {
/*  53 */       throw new IndexOutOfBoundsException("Pre-Eclair does not support pointers");
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface MotionEventVersionImpl
/*     */   {
/*     */     public abstract int findPointerIndex(MotionEvent paramMotionEvent, int paramInt);
/*     */ 
/*     */     public abstract int getPointerId(MotionEvent paramMotionEvent, int paramInt);
/*     */ 
/*     */     public abstract float getX(MotionEvent paramMotionEvent, int paramInt);
/*     */ 
/*     */     public abstract float getY(MotionEvent paramMotionEvent, int paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Users\Administrator\Desktop\android-support-v4.jar
 * Qualified Name:     android.support.v4.view.MotionEventCompat
 * JD-Core Version:    0.6.0
 */