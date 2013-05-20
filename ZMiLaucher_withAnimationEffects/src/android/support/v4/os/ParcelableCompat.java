/*    */ package android.support.v4.os;
/*    */ 
/*    */ import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
/*    */ 
/*    */ public class ParcelableCompat
/*    */ {
/*    */   public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> callbacks)
/*    */   {
/* 25 */     if (Build.VERSION.SDK_INT >= 13) {
/* 26 */       ParcelableCompatCreatorHoneycombMR2Stub.instantiate(callbacks);
/*    */     }
/* 28 */     return new CompatCreator<T>(callbacks);
/*    */   }
/*    */   static class CompatCreator<T> implements Parcelable.Creator<T> {
/*    */     final ParcelableCompatCreatorCallbacks<T> mCallbacks;
/*    */ 
/*    */     public CompatCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
/* 35 */       this.mCallbacks = callbacks;
/*    */     }
/*    */ 
/*    */     public T createFromParcel(Parcel source)
/*    */     {
/* 40 */       return this.mCallbacks.createFromParcel(source, null);
/*    */     }
/*    */ 
/*    */     public T[] newArray(int size)
/*    */     {
/* 45 */       return this.mCallbacks.newArray(size);
/*    */     }
/*    */   }
/*    */ }
