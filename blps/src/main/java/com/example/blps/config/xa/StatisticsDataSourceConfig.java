package com.example.blps.config.xa;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "statisticsDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"
)
public class StatisticsDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public StatisticsDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "statisticsEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder statisticsEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "statisticsDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean statisticsEntityManager(
            @Qualifier("statisticsEntityManagerFactoryBuilder") EntityManagerFactoryBuilder statisticsEntityManagerFactoryBuilder,
            @Qualifier("statisticsDataSource") DataSource postgresDataSource
    ) {
        return statisticsEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("statisticsDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.statistics")
    public DataSourceProperties statisticsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("statisticsDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.statistics")
    public DataSource statisticsDataSource(@Qualifier("statisticsDataSourceProperties") DataSourceProperties statisticsDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(statisticsDataSourceProperties.getUrl());
        ds.setUser(statisticsDataSourceProperties.getUsername());
        ds.setPassword(statisticsDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_statistics");
        return xaDataSource;
    }

}