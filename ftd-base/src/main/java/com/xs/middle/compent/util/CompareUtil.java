package com.xs.middle.compent.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


public class CompareUtil {

    /**
     * 日期格式化形式
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     *
     * @param obj1              进行属性比较的对象1
     * @param obj2              进行属性比较的对象2
     * @param ingnoreCompareArr 选择不要比较的属性数组
     */
    public static Map<Object, Object> compare(Object obj1, Object obj2, String... ingnoreCompareArr) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return compare(obj1, obj2, false, ingnoreCompareArr);
    }

    public static Map<Object, Object> compare(Object obj1, Object obj2, boolean convertToString, String... ingnoreCompareArr) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Map<Object, Object> result = new HashMap<>();
        if (!check(obj1, obj2)) {
            result.put(obj1, obj2);
        }
        List<String> ignoreList = ingnoreCompareArr != null && ingnoreCompareArr.length > 0 ? Arrays.asList(ingnoreCompareArr) : Lists.newArrayList();
        if (!compareObj(obj1, obj2, convertToString, ignoreList)) {
            result.put(obj1, obj2);
        }
        if (result.size() > 0) {
            for (Map.Entry<Object, Object> entry : result.entrySet()) {
                System.out.println("--------------------------------");
                System.out.println("expectedRecord:" + JSONObject.toJSONString(entry.getKey()));
                System.out.println("actualRecord:" + JSONObject.toJSONString(entry.getValue()));
                System.out.println("--------------------------------");
            }
            throw new RuntimeException();
        }
        return result;
    }

    private static boolean compareObj(Object obj1, Object obj2, List<String> ignoreList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return compareObj(obj1, obj2, false, ignoreList);
    }

    private static boolean compareObj(Object obj1, Object obj2, boolean convertToString, List<String> ignoreList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (convertToString) {
            if (isBaseType(obj1)) {
                obj1 = getStringValue(obj1);
            }

            if (isBaseType(obj2)) {
                obj2 = getStringValue(obj2);
            }
        }

        if (!check(obj1, obj2)) {
            return false;
        }
        if (obj1 == null) {
            return true;
        } else if (obj1.getClass().isPrimitive()
                || obj1 instanceof String
                || obj1 instanceof Integer
                || obj1 instanceof BigDecimal
                || obj1 instanceof Date
                || obj1 instanceof Boolean
        ) {
            return Objects.equals(obj1, obj2);
        } else if (obj1 instanceof Collection) {
            obj1 = sort((Collection)obj1);
            obj2 = sort((Collection)obj2);
            List obj1List = (List) obj1;
            List obj2List = (List) obj2;
            if (obj1List.size() != obj2List.size()) {
                return false;
            } else {
                for (int i = 0; i < obj1List.size(); i++) {
                    if (compareObj(obj1List.get(i), obj2List.get(i), convertToString, ignoreList)) {
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        } else if (obj1 instanceof Map) {
            Map<Object, Object> obj1Map = (Map<Object, Object>) obj1;
            Map<Object, Object> obj2Map = (Map<Object, Object>) obj2;
            if (obj1Map.size() != obj2Map.size()) {
                return false;
            } else {
                for (Map.Entry<Object, Object> entry : obj1Map.entrySet()) {
                    Object rs = obj2Map.get(entry.getKey());
                    if (rs == null) {
                        return false;
                    } else {
                        if (compareObj(entry.getValue(), rs, convertToString, ignoreList)) {
                            continue;
                        } else {
                            return false;
                        }
                    }
                }
                return true;
            }
        } else {
            return compare(obj1, obj2, ignoreList);
        }
    }

    /**
     * 判断对象是否为Integer、BigDecimal、Date等MySQL基本数据类型（为了后续转为字符串）
     *
     * @param obj 目标对象
     */
    private static boolean isBaseType(Object obj) {
        boolean flag = false;
        if (obj instanceof Integer
                || obj instanceof BigDecimal
                || obj instanceof Date) {
            flag = true;
        }
        return flag;
    }

    /**
     * 将对象转为字符串格式，如为日期类型（或【yyyy-MM-dd HH:mm:ss】格式的字符串），获取其时间毫秒数，转换为字符串格式
     *
     * @param obj
     * @return
     */
    private static String getStringValue(Object obj) {

        if (obj instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            return format.format((Date) obj);
        }

        return obj.toString();
    }

    /**
     * 对集合对象进行排序
     *
     * @param col 排序前的集合对象
     * @return 排序后的集合对象
     */
    private static Collection sort(Collection col) {
        Map<Integer, Object> map = new HashMap<>();
        List<Integer> hashCodes = new ArrayList<>();
        for (Object o : col) {
            Integer code = o.hashCode();
            map.put(code, o);
            hashCodes.add(code);
        }

        Object[] ints =  hashCodes.toArray();
        Arrays.sort(ints);

        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < ints.length; i++) {
            objects.add(map.get(ints[i]));
        }
        return objects;
    }

    private static boolean compare(Object obj1, Object obj2, List<String> ignoreList) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (!check(obj1, obj2)) {
            return false;
        }
        // 获取object的属性描述
        PropertyDescriptor[] pds = Introspector.getBeanInfo(obj1.getClass(), Object.class).getPropertyDescriptors();
        // 这里就是所有的属性了
        for (PropertyDescriptor pd : pds) {
            // 属性名
            String name = pd.getName();
            // 如果当前属性选择进行比较，跳到下一次循环
            if (ignoreList != null && !ignoreList.contains(name)) {
                // 在obj1上调用get方法等同于获得obj1的属性值
                Object obj1Value = pd.getReadMethod().invoke(obj1);
                // 在obj2上调用get方法等同于获得obj2的属性值
                Object obj2Value = pd.getReadMethod().invoke(obj2);
                obj1Value = obj1Value instanceof Timestamp ? new Date(((Timestamp) obj1Value).getTime()) : obj1Value;
                obj2Value = obj2Value instanceof Timestamp ? new Date(((Timestamp) obj2Value).getTime()) : obj2Value;
                if (!check(obj1Value, obj2Value)) {
                    return false;
                } else {
                    if (compareObj(obj1Value, obj2Value, ignoreList)) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean check(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        } else if (obj1 != null && obj2 != null) {
            return obj1.getClass() == obj2.getClass();
        } else {
            return false;
        }
    }
}
