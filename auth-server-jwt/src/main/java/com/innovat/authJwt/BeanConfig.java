package com.innovat.authJwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class BeanConfig {

	@Bean
	public HazelcastInstance getHazelcast() {
		return Hazelcast.newHazelcastInstance();
	}
}
