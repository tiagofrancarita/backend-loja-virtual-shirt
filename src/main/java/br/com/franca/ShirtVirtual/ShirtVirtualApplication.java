package br.com.franca.ShirtVirtual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.concurrent.Executor;


@SpringBootApplication(scanBasePackages = {"br.com.franca.ShirtVirtual.controller"})
@EnableScheduling
@EnableAsync
@EntityScan(basePackages = "br.com.franca.ShirtVirtual.model")
@ComponentScan(basePackages = {"br.*"})
@EnableJpaRepositories(basePackages = {"br.com.franca.ShirtVirtual.repository"})
@EnableTransactionManagement
public class ShirtVirtualApplication implements AsyncConfigurer, WebMvcConfigurer {

	private static Logger logger = LoggerFactory.getLogger(ShirtVirtualApplication.class);

	public static void main(String[] args) {

		logger.info("Iniciando a loja virtual, aguarde.");
		SpringApplication.run(ShirtVirtualApplication.class, args);
		logger.info("Loja virtual iniciada com sucesso.");
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

	@Bean
	public ViewResolver viewResolver() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("classpath:templates/");
		viewResolver.setSuffix(".html");

		return viewResolver;
	}

	@Override
	@Bean
	public Executor getAsyncExecutor(){

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Assyncrono Thread");
		executor.initialize();
		return executor;
	}
}