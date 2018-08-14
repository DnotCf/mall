package com.mmall.util;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {

        //设置序列化规则 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转换timestamps格式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        //取消空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        //设置日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        //设置反序列化 忽略不存在对应属性的字段
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String obj2String(T t) {

        if (t == null) {
            return null;
        }

        try {
            return t instanceof String ? (String) t : objectMapper.writeValueAsString(t);
        } catch (IOException e) {
            log.error("Object to String errro value:{}", t, e);
            return null;
        }
    }

    //格式发的json对象
    public static <T> String obj2StringPretty(T t) {

        if (t == null) {
            return null;
        }

        try {
            return t instanceof String ? (String) t : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (IOException e) {
            log.error("Object to String errro value:{}", t, e);
            return null;
        }
    }


    public static <T> T string2obj(String value, TypeReference<T> typeReference) {

        if (StringUtils.isBlank(value) || typeReference == null) {

            return null;
        }

        try {
            return (T)(typeReference.getType().equals(String.class) ? value : objectMapper.readValue(value, typeReference));
        } catch (IOException e) {
            log.error("String to Object errro params:{},type:{}", value, typeReference, e);
            return null;
        }
    }



}
