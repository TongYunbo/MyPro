package com.tzcpa.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;
import tk.mybatis.mapper.autoconfigure.MapperAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.ResourceLoader;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;

import java.util.List;

/**
 * @ClassName MapperNoWarn
 * @Description
 * @Author wangzhangju
 * @Date 2019/7/12 13:04
 * @Version 6.0
 **/
@Configuration
public class MapperNoWarn extends MapperAutoConfiguration {
    public MapperNoWarn(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
         }

}
