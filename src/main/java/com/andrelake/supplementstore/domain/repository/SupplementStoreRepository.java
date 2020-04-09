package com.andrelake.supplementstore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andrelake.supplementstore.domain.model.SupplementStore;

@Repository
public interface SupplementStoreRepository extends JpaRepository<SupplementStore, Long>{

}
