package com.yunat.ccms.auth.login.taobao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaobaoShopRepository extends JpaRepository<TaobaoShop, String> {

}
