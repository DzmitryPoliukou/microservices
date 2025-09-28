package com.epam.repository;

import com.epam.model.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {

  List<Resource> findByIdIn(List<Integer> ids);

}
