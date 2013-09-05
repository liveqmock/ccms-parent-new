package com.yunat.ccms.dashboard.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.yunat.ccms.dashboard.model.Notice;


/**
 *  
 *  Repository： 仅仅是一个标识，表明任何继承它的均为仓库接口类，方便Spring自动扫描识别 
	CrudRepository： 继承Repository，实现了一组CRUD相关的方法 
	PagingAndSortingRepository： 继承CrudRepository，实现了一组分页排序相关的方法 
	JpaRepository： 继承PagingAndSortingRepository，实现一组JPA规范相关的方法 
	JpaSpecificationExecutor： 比较特殊，不属于Repository体系，实现一组JPA Criteria查询相关的方法 
 *  
 *   example:
 *   public interface UserRepository extends JpaRepository<User, Long> {
 *   @Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname") 
     User findByLastnameOrFirstname(@Param("lastname") String lastname, 
                                 @Param("firstname") String firstname); 
     } 
 *  
 *  优先选择 Repository 接口
 *  其他接口能做到的在 Repository 中也能做到
 *  只是 Repository 需要显示声明需要的方法，而其他则可能已经提供了相关的方法，不需要再显式声明
 *  @author yinwei
 *
 */
public interface  NoticeRepository extends Repository<Notice, Long>  {
	
	 List<Notice> findAll();
	 
	 Notice  save(Notice  notice);
	
}
