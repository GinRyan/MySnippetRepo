/*    */ package android.support.v4.view;
/*    */ 
/*    */ import android.os.Build;
import android.view.ViewConfiguration;
/*    */ 
/*    */ public class ViewConfigurationCompat
/*    */ {
/*    */   static final ViewConfigurationVersionImpl IMPL;
/*    */ 
/*    */   public static int getScaledPagingTouchSlop(ViewConfiguration config)
/*    */   {
/* 72 */     return IMPL.getScaledPagingTouchSlop(config);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 57 */     if (Build.VERSION.SDK_INT >= 11)
/* 58 */       IMPL = new FroyoViewConfigurationVersionImpl();
/*    */     else
/* 60 */       IMPL = new BaseViewConfigurationVersionImpl();
/*    */   }
/*    */ 
/*    */   static class FroyoViewConfigurationVersionImpl
/*    */     implements ViewConfigurationCompat.ViewConfigurationVersionImpl
/*    */   {
/*    */     public int getScaledPagingTouchSlop(ViewConfiguration config)
/*    */     {
/* 48 */       return ViewConfigurationCompatFroyo.getScaledPagingTouchSlop(config);
/*    */     }
/*    */   }
/*    */ 
/*    */   static class BaseViewConfigurationVersionImpl
/*    */     implements ViewConfigurationCompat.ViewConfigurationVersionImpl
/*    */   {
/*    */     public int getScaledPagingTouchSlop(ViewConfiguration config)
/*    */     {
/* 38 */       return config.getScaledTouchSlop();
/*    */     }
/*    */   }
/*    */ 
/*    */   static abstract interface ViewConfigurationVersionImpl
/*    */   {
/*    */     public abstract int getScaledPagingTouchSlop(ViewConfiguration paramViewConfiguration);
/*    */   }
/*    */ }

/* Location:           C:\Users\Administrator\Desktop\android-support-v4.jar
 * Qualified Name:     android.support.v4.view.ViewConfigurationCompat
 * JD-Core Version:    0.6.0
 */