package org.springframework.samples.petclinic;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.github.marschall.hikari.jfr.JfrMetricsTrackerFactory;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Modifies {@link HikariDataSource} to export metrics to JFR using
 * <a href="https://github.com/marschall/hikari-jfr">Hikari JFR</a>. An alternative would
 * be <a href="https://github.com/marschall/micrometer-jfr">Micrometer JFR</a>
 */
@Component
public class HikariDataSourceJfrMetricsBeanPostProcessor implements BeanPostProcessor {

	@Override
	public @Nullable Object postProcessBeforeInitialization(Object bean, String beanName) {
		if (bean instanceof HikariDataSource dataSource) {
			dataSource.setMetricsTrackerFactory(new JfrMetricsTrackerFactory());
		}
		return bean;
	}

}
