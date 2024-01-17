package com.example.batch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Generated;

@Data
@Entity
public class Post {

  @Id
  @Generated
  private Integer id;
  private Integer userId;
  private String title;
  private String body;
}
