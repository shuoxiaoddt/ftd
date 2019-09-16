package com.xs.middle.compent.util;

import com.thoughtworks.xstream.XStream;
import com.xs.middle.compent.ftdmiddle.unittest.MyDateConverter;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class XMLUtil {

    private static final String DATEFORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将xml文件内容读取到javaBean中
     *
     * @param resource 资源路劲
     * @param clazz    javaBean类型
     * @param <T>      返回对应javaBean类型
     * @return
     */
    public static <T> T xmlToObject(String resource, Class<T> clazz) {
        return xmlToObject(resource, DATEFORMATTER, clazz);
    }

    /**
     * 将xml文件内容读取到javaBean中,可自定义Date类型的格式化
     *
     * @param resource      资源路劲
     * @param defaultFormat Date类型的格式化
     * @param clazz         javaBean类型
     * @param <T>           返回对应javaBean类型
     * @return
     */
    public static <T> T xmlToObject(String resource, String defaultFormat, Class<T> clazz) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            XStream xStream = newXStream(defaultFormat, clazz);
            T t = (T) xStream.fromXML(inputStream);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> xmlToList(String resource, Class<T> clazz) {
        return xmlToList(resource, DATEFORMATTER, clazz);
    }

    public static <T> List<T> xmlToList(String resource, String defaultFormat, Class<T> clazz) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            XStream xStream = newXStream(defaultFormat, clazz);
            List<T> tList = (List<T>) xStream.fromXML(inputStream);
            return tList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将javaBean转换成xml格式的字符串类型
     *
     * @param pojo javaBean
     * @return
     */
    public static <T> String objectToString(T pojo) {
        return objectToString(pojo, DATEFORMATTER);
    }

    public static <T> String objectToCamelCaseString(T pojo, String ...excludeFields){
        boolean haveSkipField = false;
        Set<String> excludeFieldSet = null;
        if(excludeFields != null){
            haveSkipField = true;
            excludeFieldSet = Arrays.stream(excludeFields).collect(Collectors.toSet());
        }

        String xmlString = objectToString(pojo);
        int xmlStringLength = xmlString.length();
        StringBuilder sb = new StringBuilder(xmlStringLength + (xmlStringLength >> 1));
        String[] lines = xmlString.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (StringUtils.isEmpty(line)) {
                continue;
            }
            if(line.startsWith("<?xml")){
                sb.append(line);
                continue;
            }

            boolean isFindSign = (line.lastIndexOf("<") > 0);
            boolean isContainSign = line.contains("</");
            // 对象字段节点
            if(isFindSign && isContainSign){
                boolean finalHaveSkipField = haveSkipField;
                Set<String> finalExcludeFieldSet = excludeFieldSet;
                entryFieldNameCamelCase(sb, line, fieldName -> !finalHaveSkipField ? false : finalExcludeFieldSet.contains(fieldName));
                continue;
            }

            // 表字段节点
            entrySimpleClassNameCamelCase(sb, line, isContainSign);
        }

        return sb.toString();
    }

    /**
     * 驼峰映射
     * @param src
     * @return
     */
    public static String camelCase(String src) {
        int length = src.length();
        char[] chars = new char[length];
        src.getChars(0, length, chars,0);

        StringBuilder sb = new StringBuilder(length + 6).append(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            char ch = chars[i];
            if (Character.isLowerCase(ch)) {
                sb.append(ch);
                continue;
            }
            sb.append("_").append(ch);
        }

        return sb.toString().toLowerCase();
    }

    /**
     * 将javaBean转换成xml格式的字符串类型,可自定义Date类型的格式化
     *
     * @param pojo          javaBean
     * @param defaultFormat Date类型的格式化
     * @return
     */
    public static <T> String objectToString(T pojo, String defaultFormat) {
        if (Objects.isNull(pojo)) {
            return null;
        }
        XStream xStream = newXStream(defaultFormat, pojo.getClass());
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xmlString += xStream.toXML(pojo);
        return xmlString;
    }

    private static XStream newXStream(String defaultFormat, Class... clazz) {
        XStream xStream = new XStream();
        xStream.registerConverter(new MyDateConverter(defaultFormat));
        if (clazz.length > 0) {
            for (Class clazz1 : clazz) {
                xStream.alias(clazz1.getName(), clazz1);
            }
        }
        return xStream;
    }

    /**
     * 实体字段名转换成表中字段
     * @param sb
     * @param line
     * @param isExcelude
     */
    private static void entryFieldNameCamelCase(StringBuilder sb, String line, Predicate<String> isExcelude) {
        int indexOfSign = line.indexOf(">");
        String fieldName = line.substring(1, indexOfSign);

        if (isExcelude.test(fieldName)) {
            sb.append(line);
            return;
        }

        String value = line.substring(indexOfSign + 1, line.length() - 3 - fieldName.length());
        String camelCase = camelCase(fieldName);
        sb.append("<").append(camelCase).append(">").append(value).append("</").append(camelCase).append(">");
    }

    /**
     * 实体简单类名转换成表名
     * @param sb
     * @param line
     * @param isContainSign
     */
    private static void entrySimpleClassNameCamelCase(StringBuilder sb, String line, boolean isContainSign) {
        int indexOfDotSign = line.lastIndexOf(".");
        String objName;
        if(indexOfDotSign < 0){
            objName = line.substring(1, line.length() - 1);
            sb.append("<");
        }else {
            objName = line.substring(indexOfDotSign + 1, line.length() - 1);
            sb = !isContainSign
                    // 对象起始节点
                    ? sb.append("<")
                    // 对象结束节点
                    : sb.append("</");
        }

        sb.append(camelCase(objName)).append(">");
    }


}
