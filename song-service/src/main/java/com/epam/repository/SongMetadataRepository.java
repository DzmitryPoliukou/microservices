package com.epam.repository;

import com.epam.model.SongMetadata;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongMetadataRepository extends JpaRepository<SongMetadata, Integer> {

  List<SongMetadata> findByIdIn(List<Integer> resourceIds);
}
