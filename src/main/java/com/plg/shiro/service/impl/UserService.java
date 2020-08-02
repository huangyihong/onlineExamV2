package com.plg.shiro.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plg.shiro.dao.OmUploadImgMapper;
import com.plg.shiro.dao.OmUserMapper;
import com.plg.shiro.entity.OmRole;
import com.plg.shiro.entity.OmUploadImg;
import com.plg.shiro.entity.OmUploadImgExample;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.entity.OmUserExample;
import com.plg.shiro.entity.OmUserGroup;
import com.plg.shiro.service.IRoleService;
import com.plg.shiro.service.IUserGroupService;
import com.plg.shiro.service.IUserRoleService;
import com.plg.shiro.service.IUserService;
import com.plg.shiro.util.DateUtil;
import com.plg.shiro.util.ExcelUtils;
import com.plg.shiro.util.Md5;
import com.plg.shiro.util.UUIDUtil;
import com.plg.shiro.util.dwz.AjaxObject;
import com.plg.shiro.util.dwz.LayuiPage;

@Service
public class UserService implements IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Resource
	private OmUserMapper omUserMapper;
	
	@Resource
	private OmUploadImgMapper omUploadImgMapper;
	
	@Resource
	private IUserGroupService groupService;
	
	@Resource
	private IRoleService roleService;
	
	@Resource
	private IUserRoleService userRoleService;

	@Override
	public int deleteByPrimaryKey(String userId) {
		logger.info("删除用户:{}", userId);
		return omUserMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(OmUser record,String imgId,String imgSrc) {
		logger.info("新增用户：{}", record.getUserId());
		saveUserImg(record.getUserId(), imgId, imgSrc);
		//默认分配角色
		assignUserRole(record.getUserType(),record);
		return omUserMapper.insert(record);
	}
	
	@Override
	public int insert(OmUser record) {
		logger.info("新增用户：{}", record.getUserId());
		return omUserMapper.insert(record);
	}

	@Override
	public OmUser selectByPrimaryKey(String userId) {
		logger.info("查询用户：{}", userId);
		return omUserMapper.selectByPrimaryKey(userId);
	}
	
	@Override
	public OmUser selectByUserName(String userName) {
		logger.info("通过用户名查询用户，用户名userName：{}", userName);
		OmUserExample example = new OmUserExample();
		OmUserExample.Criteria criteria = example.createCriteria();
		criteria.andUserNameEqualTo(userName);
		List<OmUser> list = omUserMapper.selectByExample(example);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OmUser> selectAll() {
		logger.info("查询所有用户");
		OmUserExample example = new OmUserExample();
		OmUserExample.Criteria criteria = example.createCriteria();
		example.setOrderByClause("USER_NAME");
		return omUserMapper.selectByExample(example);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OmUser record) {
		logger.info("更新用户：{}", record.getUserId());
		return omUserMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OmUser record,String imgId,String imgSrc) {
		logger.info("更新用户：{}", record.getUserId());
		deleteUserImg(record.getUserId());
		saveUserImg(record.getUserId(), imgId, imgSrc);
		return omUserMapper.updateByPrimaryKeySelective(record);
	}
	
	private void saveUserImg(String userId, String imgId, String imgSrc) {
		//新增图片
		if(StringUtils.isNoneBlank(imgId)) {
			OmUploadImg imgBean = new OmUploadImg();
			imgBean.setId(imgId);
			imgBean.setImgSrc(imgSrc);
			imgBean.setRelationId(userId);
			imgBean.setType("user");
			omUploadImgMapper.insert(imgBean);
		}
	}
	
	private void deleteUserImg(String userId) {
		//删除图片
		OmUploadImgExample example = new OmUploadImgExample();
		OmUploadImgExample.Criteria criteria = example.createCriteria();
		criteria.andRelationIdEqualTo(userId);
		omUploadImgMapper.deleteByExample(example);
	}

	@Override
	public List<OmUser> findPageList(HttpServletRequest request, LayuiPage page) {
		OmUserExample example = new OmUserExample();
		OmUserExample.Criteria criteria = example.createCriteria();
		String userName = request.getParameter("userName");
		if(StringUtils.isNoneBlank(userName)){
			criteria.andUserNameLike("%"+userName+"%");
		}
		String realName = request.getParameter("realName");
		if(StringUtils.isNoneBlank(realName)){
			criteria.andRealNameLike("%"+realName+"%");
		}
		String userType = request.getParameter("userType");
		if(StringUtils.isNoneBlank(userType)){
			criteria.andUserTypeEqualTo(userType);
		}
		String unit = request.getParameter("unit");
		if(StringUtils.isNoneBlank(unit)){
			criteria.andUnitLike("%"+unit+"%");
		}
		String phone = request.getParameter("phone");
		if(StringUtils.isNoneBlank(phone)){
			criteria.andPhoneEqualTo(phone);
		}
		String groupName = request.getParameter("groupName");
		if(StringUtils.isNoneBlank(groupName)){
			criteria.andGroupNameLike("%"+groupName+"%");
		}
		example.setLimitPageSize(page.getLimit());
		example.setLimitStart(page.limitStart());
		page.setTotalCount(omUserMapper.countByExample(example));
		example.setOrderByClause("USER_NAME");
		return omUserMapper.selectByExample(example);
	}

	@Override
	public void deleteBatch(String ids) {
		OmUserExample example = new OmUserExample();
		OmUserExample.Criteria c = example.createCriteria();
        c.andUserIdIn(Arrays.asList(ids.split(",")));
        omUserMapper.deleteByExample(example);
	}

	@Override
	public List<OmUploadImg> selectUserImgByUserId(String userId) {
		OmUploadImgExample example = new OmUploadImgExample();
		OmUploadImgExample.Criteria criteria = example.createCriteria();
		criteria.andRelationIdEqualTo(userId);
		return omUploadImgMapper.selectByExample(example);
	}

	@Override
	public AjaxObject readExcel(HttpServletRequest request, MultipartFile file) throws Exception {
		String[] mappingFields = {"userId","realName","idType","userName","sex","phone","unit","groupName"};
		List<OmUser> list = ExcelUtils.readSheet(OmUser.class, mappingFields, 0, 1, file.getInputStream());
		if(list.size()==0){
			return AjaxObject.newError("上传文件内容为空");
		}
		return readExcel(request,list);
	}
	
	private AjaxObject readExcel(HttpServletRequest request,List<OmUser> list) throws Exception, IOException {
		String message = "";
		String userType = request.getParameter("userType");
		List<OmUser> addList = new ArrayList<OmUser>();
		for(int i=0;i<list.size();i++){
			boolean flag = true;
			OmUser bean=list.get(i);
			if(StringUtils.isBlank(bean.getRealName())){
				message +="第"+(i+2)+"行数据姓名为空\n";
				flag = false;
			}
			if(StringUtils.isBlank(bean.getIdType())){
				message +="第"+(i+2)+"行数据证件类型为空\n";
				flag = false;
			}
			if(StringUtils.isBlank(bean.getUserName())){
				message +="第"+(i+2)+"行数据用户名(证件号码)为空\n";
				flag = false;
			}
			if(StringUtils.isBlank(bean.getGroupName())){
				message +="第"+(i+2)+"行数据考试类别（用户分组）为空\n";
				flag = false;
			}
			if(!flag) {
		    	continue;
		    }
			OmUser oldUser = this.selectByUserName(bean.getUserName());
			if(oldUser!=null){
				message +="第"+(i+2)+"行数据["+bean.getUserName()+"]用户名(证件号码)已存在于库中\n";
				flag = false;
			}
			if(!flag) {
		    	continue;
		    }
			addList.add(bean);
		}
		if(StringUtils.isNotBlank(message)){
			message +="请核对数据后进行导入。";
			return AjaxObject.newError(message);
		}else{
			if(addList.size()>0) {
				Map<String,String> idTypeMap = this.idTypeMap();
				Map<String,String> sexMap = this.sexMap();
				for(OmUser bean:addList) {
					bean.setGroupName(bean.getGroupName().replace("道路运输企业", ""));
					OmUserGroup group= groupService.selectByGroupName(bean.getGroupName());
					if(group==null){
						group = new OmUserGroup();
						group.setGroupId(UUIDUtil.getUUID());
						group.setGroupName(bean.getGroupName());
						groupService.insert(group);
					}
					bean.setGroupId(group.getGroupId());
					
					if(StringUtils.isNotBlank(bean.getIdType())) {
						bean.setIdType(idTypeMap.get(bean.getIdType()));
					}
					
					if(StringUtils.isNotBlank(bean.getSex())) {
						bean.setSex(sexMap.get(bean.getSex()));
					}
					
					bean.setUserType(userType);
					bean.setUserId(UUIDUtil.getUUID());
					bean.setStatus((byte)1);
					bean.setPassword(Md5.getMD5ofStrByLowerCase(String.valueOf("123456")));
					bean.setCreateTime(DateUtil.dateParse(new Date(),""));
					this.insert(bean);
					
					//分配角色
					assignUserRole(userType, bean);
				}
				
			}
			message += "导入成功，共导入"+addList.size()+"条数据。";
			return AjaxObject.newOk(message);
		}
	}

	private void assignUserRole(String userType, OmUser bean) {
		String roleCode = "admin";
		if("1".equals(userType)) {//管理员
			roleCode = "admin";
		}else if("2".equals(userType)) {//教官 老师
			roleCode = "teacher_role";
		}else if("3".equals(userType)) {//学员
			roleCode = "student_role";
		}
		//根据角色编码获取角色id
		OmRole roleBean = roleService.selectByRoleCode(roleCode);
		if(roleBean==null) {
			List<OmRole> roleList = roleService.selectAll();
			if(roleList.size()>0) {
				roleBean = roleList.get(0);
			}
		}
		if(roleBean!=null) {
			userRoleService.saveUserRole(bean.getUserId(),roleBean.getRoleId());
		}
	}
	
	private Map<String, String> idTypeMap() {
		Map<String,String> idTypeMap = new HashMap<String,String>();
		idTypeMap.put("身份证", "1");
		idTypeMap.put("港澳居民居住证", "2");
		idTypeMap.put("港澳居民来往内地通行证", "3");
		idTypeMap.put("台湾居民居住证", "4");
		idTypeMap.put("台湾居民来往大陆通行证", "5");
		idTypeMap.put("护照", "6");
		idTypeMap.put("其他", "9");
		return idTypeMap;
	}
	
	private Map<String, String> sexMap() {
		Map<String,String> sexMap = new HashMap<String,String>();
		sexMap.put("男", "1");
		sexMap.put("女", "2");
		sexMap.put("未知", "9");
		return sexMap;
	}

	@Override
	public AjaxObject importUserImg(File tempFile,HttpServletRequest request) throws Exception{
		String message = "";
		boolean flag = true;
		List<OmUploadImg> userImgList = new ArrayList<OmUploadImg>();
		try{
        	//设置格式 防止压缩包中文乱码
        	ZipFile zip = new ZipFile(new File(tempFile.getAbsolutePath()), Charset.forName("GBK"));//解决中文文件夹乱码
        	for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();){
        		ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                	 String entryName = entry.getName();
                	 if (entryName != null) {
	                	String[] url = entryName.split("\\.");
	                	if(url.length>1) {
	                		String[] split = url[0].split("/");
	                        String userName = split[split.length - 1];
		                	OmUser userBean = this.selectByUserName(userName);
		        			if(userBean==null){
		        				message +=userName+"用户名(证件号码)在库中不存在<br>";
		        				flag = false;
		        			}
		        			if(!flag) {
		        		    	continue;
		        		    }
		                	// 获取文件名
		             		String prefix=entryName.substring(entryName.lastIndexOf(".")+1);
		             		String imgId =UUIDUtil.getUUID();
		             		String year = DateUtil.date2String(new Date(), "yyyy");
		             		String filepath = request.getSession().getServletContext().getRealPath("/upUserfile")+"/"+year+"/"+ imgId + "." + prefix;
		         	        File files=new File(filepath);
		           
		         	        if(!files.getParentFile().exists()){
		         	        	files.getParentFile().mkdirs();
		         	        }
		         	        
		         	        InputStream in = zip.getInputStream(entry);
		         	        FileOutputStream out = new FileOutputStream(filepath);
		         	        byte[] buf1 = new byte[1024];
		         	        int len;
		         	        while ((len = in.read(buf1)) > 0) {
		         	            out.write(buf1, 0, len);
		         	        }
		         	        in.close();
		         	        out.close();
		         	        
		         	        //保存关联记录表
		         	        OmUploadImg imgBean = new OmUploadImg();
			       			imgBean.setId(imgId);
			       			imgBean.setImgSrc("/upUserfile/"+year+"/"+ imgId + "." + prefix);
			       			imgBean.setRelationId(userBean.getUserId());
			       			imgBean.setType("user");
			       			userImgList.add(imgBean);
	                	}
                	} 
                }
                
        	}
        	zip.close();
        }catch(IOException e){
            e.printStackTrace();
        }
		
		if(StringUtils.isNotBlank(message)){
			message +="请核对照片名称后进行导入。";
			return AjaxObject.newError(message);
		}else{
			if(userImgList.size()>0) {
				for(OmUploadImg imgBean:userImgList) {
					deleteUserImg(imgBean.getRelationId());
					omUploadImgMapper.insert(imgBean);
				}
			}
			message += "导入成功，共导入"+userImgList.size()+"张图片。";
			return AjaxObject.newOk(message);
		}
	}
}
