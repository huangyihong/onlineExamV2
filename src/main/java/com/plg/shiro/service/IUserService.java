package com.plg.shiro.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.plg.shiro.entity.OmUploadImg;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.util.dwz.AjaxObject;
import com.plg.shiro.util.dwz.LayuiPage;

/**
 * 用户服务接口
 *
 */
public interface IUserService {

	int deleteByPrimaryKey(String userId);
	
	int insert(OmUser record);

	int insert(OmUser record,String imgId,String imgSrc);

	OmUser selectByPrimaryKey(String userId);
	
	OmUser selectByUserName(String userName);
	
	int updateByPrimaryKeySelective(OmUser record);

	int updateByPrimaryKeySelective(OmUser record,String imgId,String imgSrc);
	
	List<OmUser> selectAll();

	List<OmUser> findPageList(HttpServletRequest request, LayuiPage page);

	void deleteBatch(String ids);

	List<OmUploadImg> selectUserImgByUserId(String userId);

	AjaxObject readExcel(HttpServletRequest request, MultipartFile file) throws Exception ;

	AjaxObject importUserImg(File tempFile,HttpServletRequest request) throws Exception ;
}
