package com.example.batch.steps;

import com.example.batch.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class PostWriter implements ItemWriter<Post> {

  @Override
  public void write(Chunk<? extends Post> chunk) throws Exception {
    // Implement logic to write data to an Excel file
    // Use a library like Apache POI to create Excel workbook and sheet
    // Write the processed data to the Excel file
    chunk.getItems().forEach(c -> log.info("Writing post id:" + c.getId()));
  }

}