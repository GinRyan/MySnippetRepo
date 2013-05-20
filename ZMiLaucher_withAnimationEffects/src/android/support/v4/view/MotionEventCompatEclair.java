/*    */ package android.support.v4.view;
/*    */ 
/*    */ import android.view.MotionEvent;
/*    */ 
/*    */ class MotionEventCompatEclair
/*    */ {
/*    */   public static int findPointerIndex(MotionEvent event, int pointerId)
/*    */   {
/* 26 */     return event.findPointerIndex(pointerId);
/*    */   }
/*    */   public static int getPointerId(MotionEvent event, int pointerIndex) {
/* 29 */     return event.getPointerId(pointerIndex);
/*    */   }
/*    */   public static float getX(MotionEvent event, int pointerIndex) {
/* 32 */     return event.getX(pointerIndex);
/*    */   }
/*    */   public static float getY(MotionEvent event, int pointerIndex) {
/* 35 */     return event.getY(pointerIndex);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\android-support-v4.jar
 * Qualified Name:     android.support.v4.view.MotionEventCompatEclair
 * JD-Core Version:    0.6.0
 */