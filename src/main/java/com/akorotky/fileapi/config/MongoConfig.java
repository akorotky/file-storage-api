package com.akorotky.fileapi.config;

import com.akorotky.fileapi.constants.DocumentMimeType;
import com.akorotky.fileapi.constants.EnumToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String gridFsDatabaseName;

    @Autowired
    private @Lazy MappingMongoConverter mappingMongoConverter;

    @Bean
    public GridFsTemplate gridFsTemplate() {
        mappingMongoConverter.setCustomConversions(mongoCustomConversions());
        mappingMongoConverter.afterPropertiesSet();
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new EnumToStringConverter<DocumentMimeType>());
        converters.add(new DocumentMimeType.StringToEnumConverter());
        return new MongoCustomConversions(converters);
    }

    @Override
    protected @NonNull String getDatabaseName() {
        return gridFsDatabaseName;
    }
}
