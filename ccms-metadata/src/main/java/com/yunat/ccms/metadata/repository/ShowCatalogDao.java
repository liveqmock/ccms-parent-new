package com.yunat.ccms.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.metadata.pojo.Catalog;

public interface ShowCatalogDao extends JpaRepository<Catalog, Long> {

}
