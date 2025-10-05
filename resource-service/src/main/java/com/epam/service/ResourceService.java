package com.epam.service;

import com.epam.exception.BadRequestException;
import com.epam.exception.NotFoundException;
import com.epam.model.Resource;
import com.epam.repository.ResourceRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceService {

  private final ResourceRepository resourceRepository;
  private final RestTemplate restTemplate;

  public Integer uploadFile(byte[] mp3File) {
    Resource fileEntity = new Resource();
    fileEntity.setFile(mp3File);
    Resource savedFile = resourceRepository.save(fileEntity);

    Map<String, Object> metadata;
    try {
      metadata = retrieveMetadata(mp3File, savedFile.getId());
    } catch (IOException | TikaException | SAXException e) {
      throw new BadRequestException(e.getMessage());
    }
    saveSongMetadata(metadata);

    return savedFile.getId();
  }

  public byte[] getFile(Integer id) {
    if (id == null || id <= 0) {
      throw new BadRequestException("Resource ID cannot be null or negative. Given: " + id);
    }
    return resourceRepository.findById(id).map(Resource::getFile).orElseThrow(() ->
        new NotFoundException("File not found with id: " + id));
  }

  public List<Integer> deleteResources(String idsCsv) {
    if (idsCsv.length() > 200) {
      throw new BadRequestException("CSV string length must be less than 200 characters, current length: " + idsCsv.length());
    }

    List<Integer> ids;
    try {
      ids = Arrays.stream(idsCsv.split(","))
          .map(String::trim)
          .map(Integer::parseInt)
          .toList();
    } catch (NumberFormatException e) {
      throw new BadRequestException("Invalid ID format in CSV. All IDs must be integers: " + e.getMessage());
    }

    List<Resource> existingResources = resourceRepository.findByIdIn(ids);
    List<Integer> existingIds = existingResources.stream()
        .map(Resource::getId)
        .toList();

    resourceRepository.deleteAllById(existingIds);
    deleteSongMetadata(existingIds);
    return existingIds;
  }

  private Map<String, Object> retrieveMetadata(byte[] file, Integer resourceId) throws IOException, TikaException, SAXException {
    ContentHandler handler = new BodyContentHandler();
    Metadata metadata = new Metadata();
    ParseContext parseCtx = new ParseContext();

    try (InputStream input = new ByteArrayInputStream(file)) {
      Mp3Parser parser = new Mp3Parser();
      parser.parse(input, handler, metadata, parseCtx);
    }

    return prepareMetadata(metadata, resourceId);
  }

  private Map<String, Object> saveSongMetadata(Map<String, Object> songMetadata) {
    return restTemplate.postForObject("http://song-service/songs", songMetadata, Map.class);
  }

  private void deleteSongMetadata(List<Integer> resourceIds) {
    if (resourceIds != null && !resourceIds.isEmpty()) {
      String idsParam = String.join(",", resourceIds.stream().map(Object::toString).toList());
      restTemplate.delete("http://song-service/songs?id={ids}", Map.of("ids", idsParam));
    }
  }

  private Map<String, Object> prepareMetadata(Metadata metadata, Integer resourceId) {
    HashMap<String, Object> preparedMetadata = new HashMap<>();
    preparedMetadata.put("name", metadata.get("dc:title"));
    preparedMetadata.put("id", resourceId);
    preparedMetadata.put("artist", metadata.get("xmpDM:artist"));
    preparedMetadata.put("album", metadata.get("xmpDM:album"));

    // Format duration as mm:ss with leading zeros
    String formattedDuration = "00:00";
    String rawDuration = metadata.get("xmpDM:duration");
    if (rawDuration != null && !rawDuration.isEmpty()) {
      try {
        double durationSeconds = Double.parseDouble(rawDuration);
        int minutes = (int) (durationSeconds / 60);
        int seconds = (int) (durationSeconds % 60);
        formattedDuration = String.format("%02d:%02d", minutes, seconds);
      } catch (NumberFormatException e) {
        log.warn("Failed to parse duration value: {}", rawDuration);
      }
    }
    preparedMetadata.put("duration", formattedDuration);

    preparedMetadata.put("year", metadata.get("xmpDM:releaseDate"));

    return preparedMetadata;
  }
}
