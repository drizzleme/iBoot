package com.github.drizzleme.cfg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created with iBoot
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/10/8
 */
@Configuration
@ConditionalOnClass({EnableTransactionManagement.class /**, EntityManager.class*/})
@AutoConfigureAfter(MutiDataSourceCfg.class)
@MapperScan(basePackages = {"com.github.drizzleme.mapper"})
public class MybatisConfiguration implements EnvironmentAware{
    private static Log logger = LogFactory.getLog(MybatisConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    @Autowired
    DataSource dataSource;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment,"mybatis.");
    }


    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            if(null != propertyResolver
                    .getProperty("typeAliasesPackage"))
                sessionFactory.setTypeAliasesPackage(propertyResolver
                        .getProperty("typeAliasesPackage"));
            sessionFactory
                    .setMapperLocations(new PathMatchingResourcePatternResolver()
                            .getResources(propertyResolver
                                    .getProperty("mapperLocations")));
            if(null != propertyResolver
                    .getProperty("configLocation"))
                sessionFactory
                        .setConfigLocation(new DefaultResourceLoader()
                                .getResource(propertyResolver
                                        .getProperty("configLocation")));

            return sessionFactory.getObject();
        } catch (Exception e) {
            logger.error("Could not confiure mybatis session factory", e);
            throw  new RuntimeException(e);
        }
    }
    @Bean
    @ConditionalOnMissingBean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
