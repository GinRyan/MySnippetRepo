/*    */ package android.support.v4.os;
/*    */ 
/*    */ import android.os.Parcel;
import android.os.Parcelable;
/*    */ 
/*    */ class ParcelableCompatCreatorHoneycombMR2<T>
/*    */   implements Parcelable.Creator<T>
/*    */ {//ParcelableCompatCreatorHoneycombMR2(callbacks);
/*    */   private final ParcelableCompatCreatorCallbacks<T> mCallbacks;
/*    */ 
/*    */   public ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> callbacks)
/*    */   {
/* 32 */     this.mCallbacks = callbacks;
/*    */   }
/*    */ 
/*    */   public T createFromParcel(Parcel in) {
/* 36 */     return this.mCallbacks.createFromParcel(in, null);
/*    */   }
/*    */ 
/*    */   public T createFromParcel(Parcel in, ClassLoader loader) {
/* 40 */     return this.mCallbacks.createFromParcel(in, loader);
/*    */   }
/*    */ 
/*    */   public T[] newArray(int size) {
/* 44 */     return this.mCallbacks.newArray(size);
/*    */   }
/*    */ }
