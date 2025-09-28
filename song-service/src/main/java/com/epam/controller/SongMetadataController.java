package com.epam.controller;

import com.epam.dto.SongMetadataInputDto;
import com.epam.dto.SongMetadataOutputDto;
import com.epam.service.SongMetadataService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/songs")
public class SongMetadataController {

  private final SongMetadataService songMetadataService;

  @PostMapping
  public ResponseEntity<Map<String, Integer>> createSongMetadata(@Valid @RequestBody SongMetadataInputDto songMetadataInputDto) {
    Map<String, Integer> response = songMetadataService.saveMetadata(songMetadataInputDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SongMetadataOutputDto> findSongMetadata(@PathVariable("id") Integer id) {
    SongMetadataOutputDto songMetadata = songMetadataService.findSongMetadata(id);
    return ResponseEntity.ok(songMetadata);
  }

  @DeleteMapping
  public ResponseEntity<Map<String, List<Integer>>> deleteSongMetadata(@RequestParam("id") String idsCsv) {
    List<Integer> removedIds = songMetadataService.deleteSongMetadata(idsCsv);
    Map<String, List<Integer>> response = new HashMap<>();
    response.put("ids", removedIds);
    return ResponseEntity.ok(response);
  }
}
