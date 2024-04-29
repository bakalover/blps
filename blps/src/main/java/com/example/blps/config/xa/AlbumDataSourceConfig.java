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
        entityManagerFactoryRef = "albumDataSourceConfiguration",
        transactionManagerRef = "bakaloverTransactionManager",
        basePackages = {"com.example.blps.repo"}
)
public class AlbumDataSourceConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public AlbumDataSourceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean(name = "albumEntityManagerFactoryBuilder")
    @Primary
    public EntityManagerFactoryBuilder albumEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null
        );
    }

    @Bean(name = "albumDataSourceConfiguration")
    @Primary
    public LocalContainerEntityManagerFactoryBean albumEntityManager(
            @Qualifier("albumEntityManagerFactoryBuilder") EntityManagerFactoryBuilder albumEntityManagerFactoryBuilder,
            @Qualifier("albumDataSource") DataSource postgresDataSource
    ) {
        return albumEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.blps.repo.entity")
                .persistenceUnit("postgres")
                .properties(jpaProperties.getProperties())
                .jta(true)
                .build();
    }

    @Bean("albumDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.album")
    public DataSourceProperties albumDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("albumDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.album")
    public DataSource albumDataSource(@Qualifier("albumDataSourceProperties") DataSourceProperties albumDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(albumDataSourceProperties.getUrl());
        ds.setUser(albumDataSourceProperties.getUsername());
        ds.setPassword(albumDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setMaxPoolSize(15);
        xaDataSource.setUniqueResourceName("xa_album");
        return xaDataSource;
    }

}