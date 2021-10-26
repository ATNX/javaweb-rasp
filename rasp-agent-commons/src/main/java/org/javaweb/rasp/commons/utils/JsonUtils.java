package org.javaweb.rasp.commons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by yz on 2017/2/20.
 *
 * @author yz
 */
public class JsonUtils {

	private static final JsonObjectTypeAdapter TYPE_ADAPTER = new JsonObjectTypeAdapter();

	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().
			registerTypeAdapter(List.class, TYPE_ADAPTER).
			registerTypeAdapter(Map.class, TYPE_ADAPTER).
			create();

	public static String toJson(Object src) {
		return GSON.toJson(src);
	}

	private static <T> T fromJson(Object object, Type typeOfT) {
		String json = null;

		if (object instanceof String) {
			json = (String) object;
		} else {
			json = toJson(object);
		}

		return GSON.fromJson(json, typeOfT);
	}

	public static Map<String, Object> toJsonMap(Object object) {
		if (object != null) {
			return fromJson(object, new TypeToken<Map<String, Object>>() {
			}.getType());
		}

		return new HashMap<String, Object>();
	}

	public static Set<Map<String, Object>> toJsonSetMap(Object object) {
		if (object != null) {
			return fromJson(object, new TypeToken<Set<Map<String, Object>>>() {
			}.getType());
		}

		return new HashSet<Map<String, Object>>();
	}

	public static Object toJSONObject(String json) {
		return GSON.fromJson(json, Object.class);
	}

	public static List<Map<String, Object>> toJsonArrayMap(Object object) {
		if (object != null) {
			return fromJson(object, new TypeToken<List<Map<String, Object>>>() {
			}.getType());
		}

		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * 适配JSON序列化数据类型:https://stackoverflow.com/questions/36508323/how-can-i-prevent-gson-from-converting-integers-to-doubles
	 */
	public static final class JsonObjectTypeAdapter extends TypeAdapter<Object> {

		private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();

			switch (token) {
				case BEGIN_ARRAY:
					List<Object> list = new ArrayList<Object>();
					in.beginArray();

					while (in.hasNext()) {
						list.add(read(in));
					}

					in.endArray();
					return list;

				case BEGIN_OBJECT:
					Map<String, Object> map = new LinkedTreeMap<String, Object>();
					in.beginObject();

					while (in.hasNext()) {
						map.put(in.nextName(), read(in));
					}

					in.endObject();
					return map;

				case STRING:
					return in.nextString();

				case NUMBER:
					Number num = in.nextDouble();

					if (Math.ceil(num.doubleValue()) == num.longValue())
						return num.longValue();
					else {
						return num.doubleValue();
					}

				case BOOLEAN:
					return in.nextBoolean();

				case NULL:
					in.nextNull();
					return null;

				default:
					throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			if (value == null) {
				out.nullValue();

				return;
			}

			delegate.write(out, value);
		}

	}

}
