package com.yunat.ccms.dashboard.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.dashboard.model.Notice;
import com.yunat.ccms.dashboard.repository.NoticeRepository;
import com.yunat.ccms.dashboard.tool.Page;
import com.yunat.ccms.dashboard.tool.TestJdbcPagination;

@Service
public class NoticeService {
    
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	TestJdbcPagination  testJdbcPagination;
	
	
	public List<Notice> noticeInfo(){
		return  noticeRepository.findAll();
	}
	
	
	public Page<Notice>   testPage() throws SQLException{
		return  testJdbcPagination.getNotices(1, 2, "admin");
	}
}
