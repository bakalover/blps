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
        entityManagerFactoryRef = "roleDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"
)
public class RoleDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public RoleDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "roleEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder roleEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "roleDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean roleEntityManager(
            @Qualifier("roleEntityManagerFactoryBuilder") EntityManagerFactoryBuilder roleEntityManagerFactoryBuilder,
            @Qualifier("roleDataSource") DataSource postgresDataSource
    ) {
        return roleEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("roleDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.role")
    public DataSourceProperties roleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("roleDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.role")
    public DataSource roleDataSource(@Qualifier("roleDataSourceProperties") DataSourceProperties roleDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(roleDataSourceProperties.getUrl());
        ds.setUser(roleDataSourceProperties.getUsername());
        ds.setPassword(roleDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_role");
        return xaDataSource;
    }

}