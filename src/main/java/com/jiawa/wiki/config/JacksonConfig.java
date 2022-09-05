//package com.jiawa.wiki.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//@Configuration
//public class JacksonConfig {  //处理接口返回id与数据库id不一致问题 (雪花算法前后端交互Long类型精度丢失的问题)
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(ObjectMapper.class)
//    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder)
//    {
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//
//        // 全局配置序列化返回 JSON 处理
//        SimpleModule simpleModule = new SimpleModule();
//        //JSON Long ==> String
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//        return objectMapper;
//    }
//}
