/*    */ package android.support.v4.view;
/*    */ 
/*    */ import android.view.VelocityTracker;
/*    */ 
/*    */ class VelocityTrackerCompatHoneycomb
/*    */ {
/*    */   public static float getXVelocity(VelocityTracker tracker, int pointerId)
/*    */   {
/* 26 */     return tracker.getXVelocity(pointerId);
/*    */   }
/*    */   public static float getYVelocity(VelocityTracker tracker, int pointerId) {
/* 29 */     return tracker.getYVelocity(pointerId);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\android-support-v4.jar
 * Qualified Name:     android.support.v4.view.VelocityTrackerCompatHoneycomb
 * JD-Core Version:    0.6.0
 */