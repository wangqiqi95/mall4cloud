package com.mall4j.cloud.common.util;

import com.mall4j.cloud.common.exception.LuckException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
/**
 * @Date 2022年3月24日, 0024 09:28
 * @Created by eury
 */
public class PlaceholderResolver {

    /**
     * 默认前缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认后缀占位符
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * 默认单例解析器
     */
    private static PlaceholderResolver defaultResolver = new PlaceholderResolver();

    /**
     * 占位符前缀
     */
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;

    /**
     * 占位符后缀
     */
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;


    private PlaceholderResolver(){}

    private PlaceholderResolver(String placeholderPrefix, String placeholderSuffix) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }

    /**
     * 获取默认的占位符解析器，即占位符前缀为"${", 后缀为"}"
     * @return
     */
    public static PlaceholderResolver getDefaultResolver() {
        return defaultResolver;
    }

    public static PlaceholderResolver getResolver(String placeholderPrefix, String placeholderSuffix) {
        return new PlaceholderResolver(placeholderPrefix, placeholderSuffix);
    }

    /**
     * 获取字符串中的变量
     * @param content
     * @return
     */
    public List<String> getVariables(String content) {
        List<String> variables = new ArrayList<>();
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return variables;
        }
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix,start);
            String substring = result.substring(start, end+1);
            variables.add(substring);
            start = result.indexOf(this.placeholderPrefix, end + 1);
        }
        return variables;
    }

    /**
     * 校验字符串是否包含变量
     * @param content
     * @param variable
     * @return
     */
    public boolean checkVariables(String content, String variable) {
        boolean check =false;
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            throw new LuckException("文案中必须包含变数:"+variable);
        }
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix,start);
            String substring = result.substring(start, end+1);
            if(substring.equals(variable)) {
                check = true;
                break;
            }
            start = result.indexOf(this.placeholderPrefix, end + 1);
        }
        if(!check) {
//            throw new LuckException("文案中必须包含变数:" + variable);
            return false;
        } else {
            return check;
        }

    }


    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values   按照模板占位符索引位置设置对应的值
     * @return
     */
    public String resolve(String content, String... values) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        //值索引
        int valueIndex = 0;
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix);
            String replaceContent = values[valueIndex++];
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    public static void main(String[] args) {
//        String s = "导购${staffName} 欢迎${userName} 加入门店${storeName} 组织简称${orgName} ";
        String s = "${userName}${storeName}${orgName}";
        String s2 = "导购${导购姓名} 欢迎${客户昵称} 加入门店${门店名称} 组织简称${门店简称} ";
        System.out.println(s);
        System.out.println(s2);
        PlaceholderResolver defaultResolver = PlaceholderResolver.getDefaultResolver();
//        String resolve = defaultResolver.resolve(s, "1", "好奇会员");
        List<String> resolve = defaultResolver.getVariables(s);
        System.out.println(resolve);

        Map<String,Object> keyContent1=new LinkedHashMap<>();
//        keyContent1.put("staffName","导购1号");
        keyContent1.put("userName","客户1号");
        keyContent1.put("storeName","门店1号");
        keyContent1.put("orgName","门店简称1号");
        String resolve1=defaultResolver.resolveByMap(s,keyContent1);
        System.out.println(resolve1);

        System.out.println(s2.replace("导购姓名","staffName")
                .replace("客户昵称","userName")
                .replace("门店名称","storeName")
                .replace("门店简称","orgName"));


        System.out.println(defaultResolver.checkVariables(s,"${staffName}"));
    }

    /**
     * 解析带有指定占位符的模板字符串，默认占位符为前缀：${  后缀：}<br/><br/>
     * 如：template = category:${}:product:${}<br/>
     * values = {"1", "2"}<br/>
     * 返回 category:1:product:2<br/>
     *
     * @param content 要解析的带有占位符的模板字符串
     * @param values   按照模板占位符索引位置设置对应的值
     * @return
     */
    public String resolve(String content, Object[] values) {
        return resolve(content, Stream.of(values).map(String::valueOf).toArray(String[]::new));
    }

    /**
     * 根据替换规则来替换指定模板中的占位符值
     * @param content  要解析的字符串
     * @param rule  解析规则回调
     * @return
     */
    public String resolveByRule(String content, Function<String, String> rule) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        }
        StringBuilder result = new StringBuilder(content);
        while (start != -1) {
            int end = result.indexOf(this.placeholderSuffix, start);
            //获取占位符属性值，如${id}, 即获取id
            String placeholder = result.substring(start + this.placeholderPrefix.length(), end);
            //替换整个占位符内容，即将${id}值替换为替换规则回调中的内容
            String replaceContent = placeholder.trim().isEmpty() ? "" : rule.apply(placeholder);
            result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            start = result.indexOf(this.placeholderPrefix, start + replaceContent.length());
        }
        return result.toString();
    }

    /**
     * 替换模板中占位符内容，占位符的内容即为map key对应的值，key为占位符中的内容。<br/><br/>
     * 如：content = product:${id}:detail:${did}<br/>
     *    valueMap = id -> 1; pid -> 2<br/>
     *    经过解析返回 product:1:detail:2<br/>
     *
     * @param content  模板内容。
     * @param valueMap 值映射
     * @return   替换完成后的字符串。
     */
    public String resolveByMap(String content, final Map<String, Object> valueMap) {
        return resolveByRule(content, placeholderValue -> String.valueOf(valueMap.get(placeholderValue)));
    }

    /**
     * 根据properties文件替换占位符内容
     * @param content
     * @param properties
     * @return
     */
    public String resolveByProperties(String content, final Properties properties) {
        return resolveByRule(content, placeholderValue -> properties.getProperty(placeholderValue));
    }
}
