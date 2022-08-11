package com.continuing.development.probonorest.dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProbonoDataSourceMongoDbAtlas {
    private MongoClient mongoClient;
    private static final MongoClientSettings SETTINGS;
    static {
        SETTINGS = MongoClientSettings.builder()
                .build();
    }

    public ProbonoDataSourceMongoDbAtlas() {}

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void createConnection() {
        mongoClient = MongoClients.create(SETTINGS);
    }

    public void destroyConnection() {
        mongoClient.close();
    }
}
