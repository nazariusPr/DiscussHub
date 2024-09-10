package pet_project.DiscussHub.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
  @Value("${aws.access-key}")
  private String awsAccessKey;

  @Value("${aws.secret-key}")
  private String awsSecretKey;

  @Bean
  public AmazonS3 s3() {
    BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.US_EAST_1)
        .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
        .build();
  }
}
