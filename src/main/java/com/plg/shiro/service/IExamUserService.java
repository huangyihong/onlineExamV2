package com.plg.shiro.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.plg.shiro.entity.OmExamUser;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.util.dwz.AjaxObject;

public interface IExamUserService {

	void insertExamUser(OmExamUser bean, String userIds, String groupIds);

	List<OmExamUser> selectAll(String planId, String examUserType);

	public List<OmExamUser> selectUserExam(OmUser user, String examUserType);
	
	AjaxObject readExcel(HttpServletRequest request, MultipartFile file) throws Exception ;
	
	void exportAssignUser(HttpServletResponse response, List<OmExamUser> list) throws Exception;
}
