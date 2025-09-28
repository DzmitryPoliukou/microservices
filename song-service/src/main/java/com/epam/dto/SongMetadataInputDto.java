package com.epam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SongMetadataInputDto {

  @NotNull(message = "id is required")
  @Positive(message = "id must be a positive number")
  private Integer id;

  @NotBlank(message = "name is required")
  @Size(min = 1, max = 100, message = "name must be 1-100 characters")
  private String name;

  @NotBlank(message = "artist is required")
  @Size(min = 1, max = 100, message = "artist must be 1-100 characters")
  private String artist;

  @NotBlank(message = "album is required")
  @Size(min = 1, max = 100, message = "album must be 1-100 characters")
  private String album;

  @NotBlank(message = "duration is required")
  @Pattern(regexp = "^([0-9]{2}):([0-5][0-9])$", message = "Duration must be in mm:ss format with leading zeros where seconds must be between 00-59")
  private String duration;

  @NotBlank(message = "year is required")
  @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be between 1900 and 2099 without leading zeros")
  private String year;
}
