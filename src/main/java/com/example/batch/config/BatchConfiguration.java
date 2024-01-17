package com.example.batch.config;

import com.example.batch.Post;
import com.example.batch.steps.PostProcessor;
import com.example.batch.steps.PostReader;
import com.example.batch.steps.PostWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Configuration class for setting up Spring Batch processing for Reonomy data.
 * Defines a job with a single step that involves reading data from a REST API,
 * processing it, and writing the results using Spring Batch components.
 *
 * @author Andri Osta
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class BatchConfiguration {

  @Bean
  public static BeanDefinitionRegistryPostProcessor jobRegistryBeanPostProcessorRemover() {
    return registry -> registry.removeBeanDefinition("jobRegistryBeanPostProcessor");
  }

  @Bean
  public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new JobBuilder("postsJob", jobRepository)
            .incrementer(new RunIdIncrementer())
            .start(chunkStep(jobRepository, transactionManager))
            .build();
  }

  public Step chunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("postReaderStep", jobRepository).<Post, Post>
                    chunk(25, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();

  }

  @Bean
  @StepScope
  public PostReader reader() {
    return new PostReader("https://jsonplaceholder.typicode.com/posts", new RestTemplate());
  }

  @StepScope
  @Bean
  public PostWriter writer() {
    return new PostWriter();
  }

  @StepScope
  @Bean
  public CompositeItemProcessor<Post, Post> processor() {
    var processor = new CompositeItemProcessor<Post, Post>();
    processor.setDelegates(List.of(new PostProcessor()));
    return processor;
  }
}


