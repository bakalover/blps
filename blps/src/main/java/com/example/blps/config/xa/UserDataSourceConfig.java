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
        entityManagerFactoryRef = "userDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"
)
public class UserDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public UserDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "userEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder userEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "userDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager(
            @Qualifier("userEntityManagerFactoryBuilder") EntityManagerFactoryBuilder userEntityManagerFactoryBuilder,
            @Qualifier("userDataSource") DataSource postgresDataSource
    ) {
        return userEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("userDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("userDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.user")
    public DataSource userDataSource(@Qualifier("userDataSourceProperties") DataSourceProperties userDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(userDataSourceProperties.getUrl());
        ds.setUser(userDataSourceProperties.getUsername());
        ds.setPassword(userDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_user");
        return xaDataSource;
    }

}