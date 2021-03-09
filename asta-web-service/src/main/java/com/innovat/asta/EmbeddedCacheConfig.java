package com.innovat.asta;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;

@Configuration
@EnableCaching
public class EmbeddedCacheConfig {

	@Bean
	  Config config() {
	    Config config = new Config();

	    MapConfig mapConfig = new MapConfig();
	    mapConfig.setTimeToLiveSeconds(300);
	    config.getMapConfigs().put("aste", mapConfig);
	    config.getMapConfigs().put("products", mapConfig);

	    return config;
	  }
}
