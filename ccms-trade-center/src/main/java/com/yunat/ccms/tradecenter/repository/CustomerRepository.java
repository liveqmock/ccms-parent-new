package com.yunat.ccms.tradecenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yunat.ccms.tradecenter.domain.CustomerDomain;

public interface CustomerRepository extends JpaRepository<CustomerDomain, String>{

}
