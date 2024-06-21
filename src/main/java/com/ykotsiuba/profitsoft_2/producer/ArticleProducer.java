package com.ykotsiuba.profitsoft_2.producer;

import com.ykotsiuba.profitsoft_2.dto.EmailMessageDto;
import com.ykotsiuba.profitsoft_2.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdminOperations;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ArticleProducer {

    @Value("${kafka.topic.article}")
    private String articleTopic;

    private final KafkaOperations<String, EmailMessageDto> operations;

    public void sendReport(Article article) {
        EmailMessageDto data = EmailMessageDto.builder()
                .to(article.getAuthor().getEmail())
                .content(String.format("Congratulations you have published article %s.", article.getTitle()))
                .subject("New article")
                .build();
        operations.send(articleTopic, UUID.randomUUID().toString(), data);
    }
}
