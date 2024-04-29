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
        entityManagerFactoryRef = "commentDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"

)
public class CommentDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public CommentDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "commentEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder commentEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "commentDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean commentEntityManager(
            @Qualifier("commentEntityManagerFactoryBuilder") EntityManagerFactoryBuilder commentEntityManagerFactoryBuilder,
            @Qualifier("commentDataSource") DataSource postgresDataSource
    ) {
        return commentEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("commentDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.comment")
    public DataSourceProperties commentDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("commentDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.comment")
    public DataSource commentDataSource(@Qualifier("commentDataSourceProperties") DataSourceProperties commentDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(commentDataSourceProperties.getUrl());
        ds.setUser(commentDataSourceProperties.getUsername());
        ds.setPassword(commentDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_comment");
        return xaDataSource;
    }

}