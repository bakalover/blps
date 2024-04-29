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
        entityManagerFactoryRef = "complaintDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"
)
public class ComplaintDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public ComplaintDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "complaintEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder complaintEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "complaintDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean complaintEntityManager(
            @Qualifier("complaintEntityManagerFactoryBuilder") EntityManagerFactoryBuilder complaintEntityManagerFactoryBuilder,
            @Qualifier("complaintDataSource") DataSource postgresDataSource
    ) {
        return complaintEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("complaintDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.complaint")
    public DataSourceProperties complaintDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("complaintDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.complaint")
    public DataSource complaintDataSource(@Qualifier("complaintDataSourceProperties") DataSourceProperties complaintDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(complaintDataSourceProperties.getUrl());
        ds.setUser(complaintDataSourceProperties.getUsername());
        ds.setPassword(complaintDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_complaint");
        return xaDataSource;
    }

}