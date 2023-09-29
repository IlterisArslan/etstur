package com.ets.repository;

import com.ets.entity.EtsFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EtsFileRepository extends CrudRepository<EtsFile, Long> {
}
