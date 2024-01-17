package com.example.batch.steps;

import com.example.batch.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PostReader implements ItemReader<Post> {

  private final String url;
  private final RestTemplate restTemplate;
  private Integer nextPost = 0;
  private List<Post> postList;


  public PostReader(String url, RestTemplate restTemplate) {
    this.url = url;
    this.restTemplate = restTemplate;
  }


  @Override
  public Post read() throws Exception {
    Post post = null;

    if (postList == null) {
      postList = fetchPosts();
    }

    if (nextPost < postList.size()) {
      post = postList.get(nextPost);
      log.info("reading post id " + post.getId().toString());
      nextPost++;
    } else {
      nextPost = 0;
      postList = null;
    }
    return post;
  }

  public List<Post> fetchPosts() {
    var summary = restTemplate.getForEntity(url, Post[].class);
    log.info("Getting summary data from Reonomy API: " + summary.getBody().length);
    return Arrays.stream(summary.getBody()).toList();
  }

}
