import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

/**
 * 对象序列化存储
 * 
 * @author Liang
 * 
 * @param <T>保存某种类型的对象
 */
public class KVHelper<T> {

	Context mContext;
	SharedPreferences mSharedPreferences;

	public KVHelper(Context context) {
		this(context, "Entities");
	}

	public KVHelper(Context context, String sharedname) {
		this.mContext = context;
		mSharedPreferences = context.getSharedPreferences(sharedname, Context.MODE_PRIVATE);
	}

	/**
	 * 存储对象
	 * 
	 * @param tag
	 *            对象tag
	 * @param obj
	 *            序列化对象体
	 */
	public void put(String tag, T obj) {
		if (obj instanceof Serializable) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(obj);
				String encoded = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));

				Editor editor = mSharedPreferences.edit();
				editor.putString(tag, encoded);
				editor.commit();
				Logger.d(this, "完成" + tag + "对象的存储");

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("待存储的对象必须实现Serializable接口，实现了List和Map等接口的类的实例默认已经实现了Serializable");
		}

	}

	/**
	 * 取出对象
	 * 
	 * @param tag
	 *            对象名
	 * @return 返回对象体
	 */
	@SuppressWarnings("unchecked")
	public T get(String tag) {
		String initial = mSharedPreferences.getString(tag, "");
		if (initial.equals("")) {
			return null;
		}
		byte[] base64bytes = Base64.decode(initial.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64bytes);
		T t = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			t = (T) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	public void remove(String tag) {
		Editor editor = mSharedPreferences.edit();
		editor.remove(tag);
		editor.commit();
	}
}
