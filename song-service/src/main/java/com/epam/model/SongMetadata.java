package com.epam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "songs")
@Data
public class SongMetadata {

  @Id
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "artist")
  private String artist;

  @Column(name = "album")
  private String album;

  @Column(name = "duration")
  private String duration; // Format: mm:ss

  @Column(name = "year")
  private String year;
}
