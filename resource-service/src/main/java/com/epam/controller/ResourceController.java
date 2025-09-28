package com.epam.controller;

import com.epam.service.ResourceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/resources")
public class ResourceController {

  private final ResourceService resourceService;

  @PostMapping(consumes = {MediaType.APPLICATION_OCTET_STREAM_VALUE, "audio/mpeg"}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Long>> uploadFile(@RequestBody byte[] mp3File) {
    Integer uploadedFileId = resourceService.uploadFile(mp3File);
    return ResponseEntity.ok(Map.of("id", uploadedFileId.longValue()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<byte[]> getSongFile(@PathVariable("id") Integer id) {
    byte[] file = resourceService.getFile(id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("audio/mpeg"))
        .body(file);
  }

  @DeleteMapping
  public ResponseEntity<Map<String, List<Integer>>> deleteResources(@RequestParam("id") String idsCsv) {
    List<Integer> deletedIds = resourceService.deleteResources(idsCsv);
    Map<String, List<Integer>> response = new HashMap<>();
    response.put("ids", deletedIds);
    return ResponseEntity.ok(response);
  }
}
