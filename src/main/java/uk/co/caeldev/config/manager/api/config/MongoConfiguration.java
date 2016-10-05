package uk.co.caeldev.config.manager.api.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.caeldev.spring.moprhia.*;
import uk.co.caeldev.spring.moprhia.MongoSettings;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSpringMorphia
public class MongoConfiguration {}