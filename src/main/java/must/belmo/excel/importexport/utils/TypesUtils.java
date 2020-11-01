package must.belmo.excel.importexport.utils;

import must.belmo.excel.importexport.exception.ExcelExporterException;
import must.belmo.excel.importexport.exception.ExcelImporterDefaultConstructorException;
import must.belmo.excel.importexport.exception.ExcelImporterException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class TypesUtils {
	private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();
	private static final Map<Class<?>, Object> DEFAULT_VALUES_FOR_PRIMITIVE_TYPES = new HashMap<>();
	
	static {
		PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
		PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
		PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
		PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
		PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
		PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
		PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
		PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
		PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
		
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(boolean.class, false);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(byte.class, (byte) 0);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(char.class, (char) 0);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(double.class, 0d);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(float.class, 0f);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(int.class, 0);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(long.class, 0L);
		DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.put(short.class, (short) 0);
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> wrap(Class<T> c) {
		return c.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get(c) : c;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDefaultValue(Class<T> c) {
		return (T) DEFAULT_VALUES_FOR_PRIMITIVE_TYPES.getOrDefault(c, 0);
	}
	
	public static <T> T createInstanceUsingDefaultConstructor(Class<T> cls) throws ExcelImporterException {
		final Constructor<T> constructor;
		try {
			constructor = cls.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new ExcelImporterDefaultConstructorException(cls);
		}
		final T instance;
		try {
			instance = constructor.newInstance();
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new ExcelImporterException("Cannot access the class " + cls.getCanonicalName());
		}
		return instance;
	}
	
	public static <R, T> R getValueFromField(T object, String fieldName) throws ExcelExporterException {
		if (object == null) {
			throw new ExcelExporterException("The object is null");
		}
		try {
			Field declaredField = object.getClass().getDeclaredField(fieldName);
			declaredField.setAccessible(true);
			return (R) declaredField.get(object);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ExcelExporterException("Error accessing the field " + fieldName + " in the class " + object.getClass());
		}
	}
}
