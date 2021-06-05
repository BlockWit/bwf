package com.blockwit.bwf.repository;

import com.blockwit.bwf.model.chain.Tx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxsRepository extends JpaRepository<Tx, Long> {

    Page<Tx> findAll(Pageable pageable);

}


