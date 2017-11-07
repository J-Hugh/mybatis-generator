package autocode.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * J2EE Framework Description: 处理、缓冲反射方法，在两个类之间进行值复制 
 * </p>
 * 
 * @author Zhang lelong
 * @version 1.0
 */
public class ReflectUtil {

    /**
     * 从source对象复制值到object对象
     * 
     * 
     * @param source 源对象
     * @param object 目标对象
     * 
     */
    public static void copyValue(Object source, Object object) {
        if (source == null || object == null)
            return;

        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field objectField = null;
            try {
                objectField = object.getClass().getDeclaredField(sourceField.getName());
            } catch (Exception e) {
                continue;
            }

            Object value = null;
            // 得到源对象字段的
            try {
                Method method = getGetMethod(source.getClass(), sourceField.getName());
                value = method.invoke(source, null);
            } catch (Exception e) {
                try {
                    sourceField.setAccessible(true);
                    value = sourceField.get(source);
                } catch (Exception ignore) {
                }
            }

            // 设置目标对象的
            try {
                Method method = getSetMethod(object.getClass(), objectField.getName());
                Object[] values = new Object[1];
                values[0] = value;
                method.invoke(object, values);
            } catch (Exception e) {
                try {
                    objectField.set(object, value);
                } catch (Exception ignore) {
                }
            }
        }
    }

    /**
     * 根据字段名得到get方法
     * 
     * @param clazz     Class
     * @param fieldName 字段名称
     * @return
     * 
     * @throws NoSuchMethodException
     */
    public static Method getGetMethod(Class clazz, String fieldName) throws NoSuchMethodException {
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return clazz.getMethod(methodName, null);
    }

    /**
     * 根据字段名得到set方法
     * 
     * @param clazz
     * @param fieldName 字段名称
     * @return
     * 
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     */
    public static Method getSetMethod(Class clazz, String fieldName) throws NoSuchMethodException,
            NoSuchFieldException {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class[] clazzz = new Class[1];
        clazzz[0] = clazz.getDeclaredField(fieldName).getType();
        return clazz.getMethod(methodName, clazzz);
    }
}
