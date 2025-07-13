package dev.ehyeon.hydrangea.common.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing

@Configuration
@EnableMongoAuditing
class MongoDbAuditingConfiguration
