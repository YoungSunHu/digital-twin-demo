package com.gs.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.PostgreDialect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: LiangJingXing
 * @Date: 2019/8/21 19:38
 * @Decription: MybatisPlus 配置分页 性能分析
 */
@Configuration
@MapperScan("com.gs.dao.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor().setDialectClazz("com.baomidou.mybatisplus.extension.plugins.pagination.dialects.PostgreDialect");
    }

}