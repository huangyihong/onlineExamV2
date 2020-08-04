package com.plg.shiro.controller.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.plg.shiro.entity.OmRole;
import com.plg.shiro.entity.OmUploadImg;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.entity.OmUserGroup;
import com.plg.shiro.entity.Vo.OmExamSubmitExportVo;
import com.plg.shiro.entity.Vo.OmExamSubmitVo;
import com.plg.shiro.service.IRoleService;
import com.plg.shiro.service.IUserGroupService;
import com.plg.shiro.service.IUserRoleService;
import com.plg.shiro.service.IUserService;
import com.plg.shiro.util.DateUtil;
import com.plg.shiro.util.DownloadUtils;
import com.plg.shiro.util.ExcelUtils;
import com.plg.shiro.util.FileUtils;
import com.plg.shiro.util.Md5;
import com.plg.shiro.util.UUIDUtil;
import com.plg.shiro.util.dwz.AjaxObject;
import com.plg.shiro.util.dwz.LayuiPage;

import jxl.Workbook;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 用户模块
 *
 */
@Controller
@RequestMapping({ "/admin/system/omUser" })
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private IUserService service;
	
	@Resource
	private IUserGroupService groupService;
	
	private static final String USER_PATH = "admin/system/omUser/";
	
	/**
	 * 用户管理列表页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/list" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String list(HttpServletRequest request) {
		return USER_PATH+"list";
	}
	
	@RequestMapping(value = { "/studentList" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String studentList(HttpServletRequest request) {
		return USER_PATH+"studentList";
	}
	
	//获取列表数据
  	@RequestMapping("getList")
    @ResponseBody
  	public AjaxObject getList(HttpServletRequest request,LayuiPage page) throws Exception {
  	  	AjaxObject result = AjaxObject.newOk("success");
  			try {
  				List<OmUser> list = service.findPageList(request, page);
  				result.setData(list);
  				result.setTotal(page.getTotalCount());
  			} catch(Exception e) {
  				return AjaxObject.newError(e.getMessage());
  			}
  			return result;
  	}
  	
  	 //新增修改页面
  	@RequestMapping(value = { "/create" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
  	public String create(HttpServletRequest request) {
  		String userId = request.getParameter("userId");
  		OmUser bean = new OmUser();
		if(StringUtils.isNotBlank(userId)){
			bean = service.selectByPrimaryKey(userId);
			if(bean!=null){
				//获取图片
				List<OmUploadImg> imgList = service.selectUserImgByUserId(userId);
				request.setAttribute("imgList", imgList);
			}
    	}else {
    		bean.setUserType(request.getParameter("userType"));
    	}
		request.setAttribute("bean", bean);
    	request.setAttribute("fntype", request.getParameter("fntype"));
    	request.setAttribute("userType", request.getParameter("userType"));
    	List<OmUserGroup> groupList =groupService.selectAll();
    	request.setAttribute("groupList", groupList);
    	return USER_PATH+"create";
  	}

    //保存修改操作
    @RequestMapping("save")
    @ResponseBody
	public AjaxObject save(HttpServletRequest request,OmUser bean){
    	AjaxObject result = AjaxObject.newOk("success");
		try {
			String fntype = request.getParameter("fntype");
			String imgId = request.getParameter("imgId");
			String imgSrc = request.getParameter("imgSrc");
			if ("update".equals(fntype)) {
				bean.setUpdateTime(DateUtil.dateParse(new Date(),""));
				service.updateByPrimaryKeySelective(bean,imgId,imgSrc);
			}else{
				bean.setUserId(UUIDUtil.getUUID());
				bean.setCreateTime(DateUtil.dateParse(new Date(),""));
				bean.setPassword(Md5.getMD5ofStrByLowerCase(String.valueOf(bean.getPassword())));
				service.insert(bean,imgId,imgSrc);
			}
		} catch (Exception e) {
			logger.error("保存修改用户信息："  , e);
			return AjaxObject.newError(e.getMessage());
		}
		return result;
	}
    
    //批量删除操作
    @RequestMapping("del")
    @ResponseBody
	public AjaxObject del(HttpServletRequest request,String ids){
    	AjaxObject result = AjaxObject.newOk("success");
		try {
			service.deleteBatch(ids);
		} catch (Exception e) {
			logger.error("删除用户信息："  , e);
			return AjaxObject.newError(e.getMessage());
		}
		return result;
	}
    
    //用户名是否重复
    @RequestMapping(value = { "/isExistName" })
   	@ResponseBody
   	public AjaxObject isExistName(String userName,HttpServletRequest request){
    	OmUser bean= service.selectByUserName(userName);
    	boolean flag = false;
    	if(bean!=null){
    		flag = true;
    	}
   		return AjaxObject.newOk("操作成功",flag);
   	}
    
    /**
	 * 用户角色授权页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/assignRole" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String assignRole(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return USER_PATH+"assignRole";
	}
	
	//获取所有用户
    @RequestMapping(value = { "/getAllUserList" })
   	@ResponseBody
   	public AjaxObject getAllRoleList(HttpServletRequest request){
    	List<OmUser> list = service.selectAll();
   		return AjaxObject.newOk("操作成功",list);
   	}
    
    /**
	 * 修改密码页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/changePassword" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String changePassword(HttpServletRequest request) {
		return USER_PATH+"changePassword";
	}
	
	//保存修改密码
    @RequestMapping(value = { "/saveChangePassword" })
   	@ResponseBody
   	public AjaxObject saveChangePassword(HttpServletRequest request, String password, String newPassword, String rPassword, String userId){
    	AjaxObject result = AjaxObject.newOk("success");
		try {
			Subject currentUser = SecurityUtils.getSubject();
			OmUser omUser = (OmUser) SecurityUtils.getSubject().getSession().getAttribute("om_user");
			if (StringUtils.isNotBlank(userId)) {
				omUser= service.selectByPrimaryKey(userId);
			}
			//判断旧密码是否正确
			String oldPassword = Md5.getMD5ofStrByLowerCase(String.valueOf(password));
			if(!omUser.getPassword().equals(oldPassword)){
				return AjaxObject.newError("当前密码不正确！");
			}
			//更新密码
			omUser.setPassword(Md5.getMD5ofStrByLowerCase(String.valueOf(newPassword)));
			service.updateByPrimaryKeySelective(omUser);
		} catch (Exception e) {
			logger.error("保存修改用户密码信息："  , e);
			return AjaxObject.newError(e.getMessage());
		}
		return result;
   	}
    
    //重置密码
    @RequestMapping(value = { "/resetPassword" })
   	@ResponseBody
   	public AjaxObject resetPassword(HttpServletRequest request, String userId){
    	AjaxObject result = AjaxObject.newOk("success");
		try {
			if (StringUtils.isNotBlank(userId)) {
				OmUser omUser = service.selectByPrimaryKey(userId);
				//重置密码
				omUser.setPassword(Md5.getMD5ofStrByLowerCase(String.valueOf("123456")));
				service.updateByPrimaryKeySelective(omUser);
			}else{
				return AjaxObject.newError("参数错误！");
			}
		} catch (Exception e) {
			logger.error("重置用户密码信息："  , e);
			return AjaxObject.newError(e.getMessage());
		}
		return result;
   	}
    
    /**
	 * 个人中心页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/userInfo" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String userInfo(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		OmUser bean = (OmUser) SecurityUtils.getSubject().getSession().getAttribute("om_user");
		if(bean!=null){
			bean= service.selectByPrimaryKey(bean.getUserId()); 
			//获取图片
			List<OmUploadImg> imgList = service.selectUserImgByUserId(bean.getUserId());
			request.setAttribute("imgList", imgList);
		}
		request.setAttribute("bean", bean);
		List<OmUserGroup> groupList =groupService.selectAll();
    	request.setAttribute("groupList", groupList);
		return USER_PATH+"userInfo";
	}
	
	/**
	 * 个人中心页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/userInfoImg" })
   	@ResponseBody
   	public AjaxObject userInfoImg(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		OmUser bean = (OmUser) SecurityUtils.getSubject().getSession().getAttribute("om_user");
		if(bean!=null){
			//获取图片
			List<OmUploadImg> imgList = service.selectUserImgByUserId(bean.getUserId());
			if(imgList.size()>0) {
				return AjaxObject.newOk("操作成功",imgList.get(0).getImgSrc());
			}
		}
    	return AjaxObject.newError("获取图片失败");
	}
	
	//excel导入
	@RequestMapping(value = { "/importUser" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String toImportFile(HttpServletRequest request) {
		String userType = request.getParameter("userType");
		request.setAttribute("userType", userType);
	  	return USER_PATH+"importUser";
	}

    @RequestMapping("import")
    @ResponseBody
    public AjaxObject importFile(@RequestParam MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
        AjaxObject result = AjaxObject.newOk("success");
        try {
        	// 判断文件名是否为空
    		if (file == null){
    			 return AjaxObject.newError("上传文件为空");
    		}
    		// 获取文件名
    		String name = file.getOriginalFilename();
    		// 判断文件大小、即名称
    		long size = file.getSize();
    		if (name == null || ("").equals(name) && size == 0){
    			return AjaxObject.newError("上传文件内容为空");
    		}
    		result = service.readExcel(request,file);
    		
        } catch(Exception e) {
            return AjaxObject.newError(e.getMessage());
        }
        return result;
    }

	/**
	 * 模板下载
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/downloadTemplate")
   	@SuppressWarnings("deprecation")
   	@ResponseBody
   	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response)throws Exception {
		String filePath = "/template/用户信息模板.xls";
		String fileName = "用户信息模板.xls";
		InputStream in = null;  
        try {  
          in =  this.getClass().getResourceAsStream(filePath);  
          DownloadUtils.downloadExcel(response,in,fileName);
        } catch (FileNotFoundException e) {  
        }  
    }
	
	@RequestMapping("uploadImg")
    @ResponseBody
    public AjaxObject uploadImg(@RequestParam MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
        AjaxObject result = AjaxObject.newOk("success");
        String prefix="";
	    String imgId="";
	    OutputStream out = null;
        try {
        	// 判断文件名是否为空
    		if (file == null){
    			 return AjaxObject.newError("上传文件为空");
    		}
    		// 获取文件名
    		String originalName = file.getOriginalFilename();
    		prefix=originalName.substring(originalName.lastIndexOf(".")+1);
    		imgId =UUIDUtil.getUUID();
    		String year = DateUtil.date2String(new Date(), "yyyy");
    		// 判断文件大小、即名称
    		long size = file.getSize();
    		if (originalName == null || ("").equals(originalName) && size == 0){
    			return AjaxObject.newError("上传文件内容为空");
    		}
    		String filepath = request.getSession().getServletContext().getRealPath("/upUserfile")+"/"+year+"/"+ imgId + "." + prefix;
	        File files=new File(filepath);
  
            if(!files.getParentFile().exists()){
                files.getParentFile().mkdirs();
            }
	        file.transferTo(files);
 
	        Map<String,Object> img=new HashMap<>();

		    img.put("src","/upUserfile/"+year+"/"+ imgId + "." + prefix);
		    img.put("imgId", imgId);
		    result.setData(img);
        } catch(Exception e) {
            return AjaxObject.newError(e.getMessage());
        }finally{
	        try {
	            if(out!=null){
	                out.close();
	            }
	        } catch (IOException e) {
	        }
	    }
        return result;
    }
	
	//img导入
	@RequestMapping(value = { "/importUserImg" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String toImportUserImg(HttpServletRequest request) {
	  	return USER_PATH+"importUserImg";
	}
	
	@RequestMapping("uploadBatchUserImg")
    @ResponseBody
    public AjaxObject uploadImg1(@RequestParam MultipartFile file,HttpServletRequest request, HttpServletResponse response) {
        AjaxObject result = AjaxObject.newOk("success");
        try {
        	MultipartFile zipFile = file;
        	if (zipFile == null){
   			 	return AjaxObject.newError("请上传压缩包");
        	}
        	String filename = zipFile.getOriginalFilename();                    //上传的包名
            InputStream srcInputStream = null;                                    //上传的源文件流
            
            try {
                srcInputStream = zipFile.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String path = request.getSession().getServletContext().getRealPath("/tempfile");
        	File tempFile = FileUtils.saveTempFile(srcInputStream, filename,path);
        	if (tempFile == null){
   			 	return AjaxObject.newError("请上传压缩包");
        	}
        	if(filename.matches(".*\\.zip")){                //是zip压缩文件
        		result= service.importUserImg(tempFile,request);
            }
        	srcInputStream.close();
        	FileUtils.deleteTempFile(tempFile);
        	
        } catch(Exception e) {
            return AjaxObject.newError(e.getMessage());
        }
        return result;
    }
	
	//打印准考证
	@RequestMapping(value = { "/print" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String toPrint(HttpServletRequest request) {
		OmUser bean = (OmUser) SecurityUtils.getSubject().getSession().getAttribute("om_user");
		if(bean!=null){
			bean= service.selectByPrimaryKey(bean.getUserId()); 
			//获取图片
			List<OmUploadImg> imgList = service.selectUserImgByUserId(bean.getUserId());
			if(imgList.size()>0) {
				request.setAttribute("imgSrc", imgList.get(0).getImgSrc());
			}
		}
		request.setAttribute("bean", bean);
	  	return USER_PATH+"print";
	}
	
	@RequestMapping("/exportUser")
	@SuppressWarnings("deprecation")
	@ResponseBody
	public void exportGrade(HttpServletRequest request,HttpServletResponse response)throws Exception {
		String file = "用户信息.xls";
		response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(file, "UTF-8") );
		String error = null;
		String selectSql = null; 
		try {
			List<OmUser> list = service.findList(request);
			WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
			if(list==null||list.size()==0){
				WritableSheet sheet = wwb.createSheet("暂无符合条件记录",0);
				WritableCellFormat wc = new WritableCellFormat();
			    wc.setAlignment(Alignment.CENTRE); // 设置居中
			    sheet.addCell(new Label(0, 0, "暂无符合条件记录",wc));
			    wwb.write();
				wwb.close();
				return;
			}
			service.exportUser(response, list); 
		} catch (Exception e) {
			error = e.getMessage();
			throw e;
		} finally {

		}
	}

}
