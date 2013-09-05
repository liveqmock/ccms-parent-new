package com.yunat.ccms.tradecenter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yunat.ccms.tradecenter.domain.AffairsHandleDomain;

/**
 * User: weilin.li
 * Date: 13-7-29
 * Time: 下午2:33
 */
public interface AffairsHandleRepository extends JpaRepository<AffairsHandleDomain, Long>{

    @Query("select a from AffairsHandleDomain a where a.affairsId = :affairsId order by a.created asc")
    List<AffairsHandleDomain> getByAffairsId(@Param("affairsId")Long affairsId);

    @Query("select a.affairsId, count(a) as size from AffairsHandleDomain a where a.affairsId in (:affairsIds) group by a.affairsId")
    List<Object> countGroupByAffairsId(@Param("affairsIds")List<Long> affairsIds);
}
