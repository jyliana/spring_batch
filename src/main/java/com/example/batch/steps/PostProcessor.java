package com.example.batch.steps;

import com.example.batch.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PostProcessor implements ItemProcessor<Post, Post> {

  @Override
  public Post process(Post post) throws Exception {
    log.info("processing post id: " + post.getId());
    return processReonomyProperties(post);
  }

  private Post processReonomyProperties(Post post) {
    post.setTitle("from user id " + post.getUserId() + ": " + post.getTitle());
    return post;
  }
}
