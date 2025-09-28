package com.epam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class SongMetadataOutputDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("artist")
  private String artist;

  @JsonProperty("album")
  private String album;

  @JsonProperty("duration")
  private String duration;

  @JsonProperty("year")
  private String year;
}
