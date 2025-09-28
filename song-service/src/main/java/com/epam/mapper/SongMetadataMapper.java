package com.epam.mapper;

import com.epam.dto.SongMetadataInputDto;
import com.epam.dto.SongMetadataOutputDto;
import com.epam.model.SongMetadata;

public class SongMetadataMapper {

  public static SongMetadata toEntity(SongMetadataInputDto request) {
    SongMetadata song = new SongMetadata();
    song.setId(request.getId());
    song.setTitle(request.getName());
    song.setArtist(request.getArtist());
    song.setAlbum(request.getAlbum());
    song.setDuration(request.getDuration());
    song.setYear(request.getYear());
    return song;
  }

  public static SongMetadataOutputDto toDto(SongMetadata song) {
    return new SongMetadataOutputDto(
        song.getId(),
        song.getTitle(),
        song.getArtist(),
        song.getAlbum(),
        song.getDuration(),
        song.getYear()
    );
  }

}
