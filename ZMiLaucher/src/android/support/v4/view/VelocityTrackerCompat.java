/*    */ package android.support.v4.view;
/*    */ 
/*    */ import android.os.Build;
import android.view.VelocityTracker;
/*    */ 
/*    */ public class VelocityTrackerCompat
/*    */ {
/*    */   static final VelocityTrackerVersionImpl IMPL;
/*    */ 
/*    */   public static float getXVelocity(VelocityTracker tracker, int pointerId)
/*    */   {
/* 81 */     return IMPL.getXVelocity(tracker, pointerId);
/*    */   }
/*    */ 
/*    */   public static float getYVelocity(VelocityTracker tracker, int pointerId)
/*    */   {
/* 90 */     return IMPL.getYVelocity(tracker, pointerId);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 66 */     if (Build.VERSION.SDK_INT >= 11)
/* 67 */       IMPL = new HoneycombVelocityTrackerVersionImpl();
/*    */     else
/* 69 */       IMPL = new BaseVelocityTrackerVersionImpl();
/*    */   }
/*    */ 
/*    */   static class HoneycombVelocityTrackerVersionImpl
/*    */     implements VelocityTrackerCompat.VelocityTrackerVersionImpl
/*    */   {
/*    */     public float getXVelocity(VelocityTracker tracker, int pointerId)
/*    */     {
/* 53 */       return VelocityTrackerCompatHoneycomb.getXVelocity(tracker, pointerId);
/*    */     }
/*    */ 
/*    */     public float getYVelocity(VelocityTracker tracker, int pointerId) {
/* 57 */       return VelocityTrackerCompatHoneycomb.getYVelocity(tracker, pointerId);
/*    */     }
/*    */   }
/*    */ 
/*    */   static class BaseVelocityTrackerVersionImpl
/*    */     implements VelocityTrackerCompat.VelocityTrackerVersionImpl
/*    */   {
/*    */     public float getXVelocity(VelocityTracker tracker, int pointerId)
/*    */     {
/* 39 */       return tracker.getXVelocity();
/*    */     }
/*    */ 
/*    */     public float getYVelocity(VelocityTracker tracker, int pointerId) {
/* 43 */       return tracker.getYVelocity();
/*    */     }
/*    */   }
/*    */ 
/*    */   static abstract interface VelocityTrackerVersionImpl
/*    */   {
/*    */     public abstract float getXVelocity(VelocityTracker paramVelocityTracker, int paramInt);
/*    */ 
/*    */     public abstract float getYVelocity(VelocityTracker paramVelocityTracker, int paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\android-support-v4.jar
 * Qualified Name:     android.support.v4.view.VelocityTrackerCompat
 * JD-Core Version:    0.6.0
 */