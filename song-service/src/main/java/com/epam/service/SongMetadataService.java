package com.epam.service;

import com.epam.dto.SongMetadataInputDto;
import com.epam.dto.SongMetadataOutputDto;
import com.epam.exception.BadRequestException;
import com.epam.exception.ConflictException;
import com.epam.exception.NotFoundException;
import com.epam.mapper.SongMetadataMapper;
import com.epam.model.SongMetadata;
import com.epam.repository.SongMetadataRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SongMetadataService {

  private final SongMetadataRepository songMetadataRepository;

  public Map<String, Integer> saveMetadata(SongMetadataInputDto songMetadataInputDto) {
    if (songMetadataRepository.findById(songMetadataInputDto.getId()).isPresent()) {
      throw new ConflictException("Metadata for this ID =" + songMetadataInputDto.getId() +"  already exists");
    }
    SongMetadata songMetadata = SongMetadataMapper.toEntity(songMetadataInputDto);
    SongMetadata savedSong = songMetadataRepository.save(songMetadata);
    return Map.of("id", savedSong.getId());
  }

  public SongMetadataOutputDto findSongMetadata(Integer id) {
    return songMetadataRepository.findById(id)
        .map(SongMetadataMapper::toDto)
        .orElseThrow(() -> new NotFoundException("Song metadata not found with id: " + id));
  }

  public List<Integer> deleteSongMetadata(String idsCsv) {
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
    List<SongMetadata> existingResources = songMetadataRepository.findByIdIn(ids);
    List<Integer> existingIds = existingResources.stream()
        .map(SongMetadata::getId)
        .toList();

    songMetadataRepository.deleteAllById(existingIds);
    return existingIds;
  }
}
