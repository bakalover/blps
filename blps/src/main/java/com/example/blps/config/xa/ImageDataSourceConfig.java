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
        entityManagerFactoryRef = "imageDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager"
)
public class ImageDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public ImageDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "imageEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder imageEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "imageDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean imageEntityManager(
            @Qualifier("imageEntityManagerFactoryBuilder") EntityManagerFactoryBuilder imageEntityManagerFactoryBuilder,
            @Qualifier("imageDataSource") DataSource postgresDataSource
    ) {
        return imageEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("imageDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.image")
    public DataSourceProperties imageDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("imageDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.image")
    public DataSource imageDataSource(@Qualifier("imageDataSourceProperties") DataSourceProperties imageDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(imageDataSourceProperties.getUrl());
        ds.setUser(imageDataSourceProperties.getUsername());
        ds.setPassword(imageDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_image");
        return xaDataSource;
    }

}