package com.plg.shiro.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plg.shiro.dao.OmExamPlanMapper;
import com.plg.shiro.dao.OmExamUserMapper;
import com.plg.shiro.entity.OmExamPlan;
import com.plg.shiro.entity.OmExamUser;
import com.plg.shiro.entity.OmExamUserExample;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.entity.OmUserGroup;
import com.plg.shiro.service.IExamUserService;
import com.plg.shiro.service.IUserService;
import com.plg.shiro.util.DateUtil;
import com.plg.shiro.util.ExcelTool;
import com.plg.shiro.util.ExcelUtils;
import com.plg.shiro.util.ExcelXUtils;
import com.plg.shiro.util.Md5;
import com.plg.shiro.util.UUIDUtil;
import com.plg.shiro.util.dwz.AjaxObject;

@Service
public class ExamUserService implements IExamUserService {
	private static final Logger logger = LoggerFactory.getLogger(ExamUserService.class);

	@Resource
	private OmExamUserMapper omExamUserMapper;
	
	@Resource
	private OmExamPlanMapper omExamPlanMapper;
	
	@Resource
	private IUserService userService;

	@Override
	public void insertExamUser(OmExamUser bean, String userIds, String groupIds) {
		//删除原来的记录
		deleteExample(bean);
		
		//插入用户
		if(StringUtils.isNotBlank(userIds)){
			List<String> userId_arr = Arrays.asList(userIds.split(","));
			for(String userId:userId_arr){
				bean.setExamUserId(UUIDUtil.getUUID());
				bean.setUserId(userId);
				bean.setGroupId("");
				bean.setCreateTime(DateUtil.dateParse(new Date(),""));
				omExamUserMapper.insertSelective(bean);
			}
		}
		//插入用户分组
		if(StringUtils.isNotBlank(groupIds)){
			List<String> groupId_arr = Arrays.asList(groupIds.split(","));
			for(String groupId:groupId_arr){
				bean.setExamUserId(UUIDUtil.getUUID());
				bean.setGroupId(groupId);
				bean.setUserId("");
				bean.setCreateTime(DateUtil.dateParse(new Date(),""));
				omExamUserMapper.insertSelective(bean);
			}
		}
		
		
	}

	private void deleteExample(OmExamUser bean) {
		OmExamUserExample example = new OmExamUserExample();
		OmExamUserExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(bean.getPlanId());
		criteria.andPaperIdEqualTo(bean.getPaperId());
		criteria.andExamUserTypeEqualTo(bean.getExamUserType());
		omExamUserMapper.deleteByExample(example);
	}

	@Override
	public List<OmExamUser> selectAll(String planId, String examUserType) {
		OmExamUserExample example = new OmExamUserExample();
		OmExamUserExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andExamUserTypeEqualTo(examUserType);
		example.setOrderByClause("CAST(SEAT_NUM as SIGNED)");
		return omExamUserMapper.selectByExample(example);
	}
	
	/**
	 * 获取用户授权的考试或者阅卷
	 */
	@Override
	public List<OmExamUser> selectUserExam(OmUser user, String examUserType) {
		OmExamUserExample example = new OmExamUserExample();
		OmExamUserExample.Criteria criteria = example.createCriteria();
		criteria.andExamUserTypeEqualTo(examUserType);
		criteria.andUserIdEqualTo(user.getUserId());
		if(StringUtils.isNotBlank(user.getGroupId())){
			OmExamUserExample.Criteria criteria2 = example.createCriteria();
			criteria2.andExamUserTypeEqualTo(examUserType);
			criteria2.andGroupIdEqualTo(user.getGroupId());
			example.or(criteria2);
		}
		return omExamUserMapper.selectByExample(example);
	}
	
	@Override
	public AjaxObject readExcel(HttpServletRequest request, MultipartFile file) throws Exception {
		String[] mappingFields = {"realName","userName","unit"};
		List<OmUser> list = new ArrayList<OmUser>();
		String name = file.getOriginalFilename();
		if (name.endsWith(ExcelTool.POINT+ExcelTool.OFFICE_EXCEL_2010_POSTFIX)) {
			list = ExcelXUtils.readSheet(OmUser.class, mappingFields, 0, 1, file.getInputStream());
		}else {
			list = ExcelUtils.readSheet(OmUser.class, mappingFields, 0, 1, file.getInputStream());
		}
		/*if(list.size()==0){
			return AjaxObject.newError("上传文件内容为空");
		}*/
		return readExcel(request,list);
	}
	
	private AjaxObject readExcel(HttpServletRequest request,List<OmUser> list) throws Exception, IOException {
		String message = "";
		String planId = request.getParameter("planId");
		OmExamPlan plan =  omExamPlanMapper.selectByPrimaryKey(planId);
		if(plan==null) {
			return AjaxObject.newError("考试安排planId参数异常");
		}
		String planTime = "";
		if(plan.getPlanTime()!=null) {
			planTime = DateUtil.date2String(new Date(),"yyyyMMddHHmm");
		}else {
			planTime = DateUtil.date2String(DateUtil.string2Date(plan.getPlanTime(),"yyyyMMddHHmm"),"yyyyMMddHHmm");
		}
		List<OmExamUser> addList = new ArrayList<OmExamUser>();
		Map<String,String> seatNumMap = new HashMap<String,String>();
		for(int i=0;i<list.size();i++){
			boolean flag = true;
			OmUser bean=list.get(i);
			if(StringUtils.isBlank(bean.getRealName())){
				message +="第"+(i+2)+"行数据姓名为空 ";
				flag = false;
			}
			if(StringUtils.isBlank(bean.getUserName())){
				message +="第"+(i+2)+"行数据证件号码(用户名)为空 ";
				flag = false;
			}
			if(StringUtils.isBlank(bean.getUnit())){
				message +="第"+(i+2)+"行数据座位号为空 ";
				flag = false;
			}else {
				if(StringUtils.isNotBlank(seatNumMap.get(bean.getUnit()))) {
					message +="第"+(i+2)+"行数据座位号在excel重复 ";
					flag = false;
				}
				try {
					Integer.parseInt(bean.getUnit());
				}catch(Exception e){
					message +="第"+(i+2)+"行数据座位号格式不正确，请填数字 ";
					flag = false;
				}
			}
			
			if(!flag) {
				message +="<br>";
		    	continue;
		    }
			OmUser oldUser = userService.selectByUserName(bean.getUserName());
			if(oldUser==null){
				message +="第"+(i+2)+"行数据["+bean.getUserName()+"]用户名(证件号码)不存在于库中 ";
				flag = false;
			}
			if(!flag) {
				message +="<br>";
		    	continue;
		    }
			OmExamUser examUser = new OmExamUser();
			examUser.setExamUserId(UUIDUtil.getUUID());
			examUser.setUserId(oldUser.getUserId());
			examUser.setGroupId("");
			examUser.setCreateTime(DateUtil.dateParse(new Date(),""));
			examUser.setSeatNum(bean.getUnit());
			examUser.setPlanId(plan.getPlanId());
			examUser.setPaperId(plan.getPaperId());
			examUser.setExamUserType("1");
			
			//生成准考证号
			examUser.setExamNum(planTime+(int)((Math.random()*9+1)*1000)+autoGenericCode(examUser.getSeatNum(),3));
			addList.add(examUser);
			seatNumMap.put(examUser.getSeatNum(), examUser.getSeatNum());
		}
		if(StringUtils.isNotBlank(message)){
			message +="请核对数据后进行导入。";
			return AjaxObject.newError(message);
		}else{
			//先删除以前授权的
			OmExamUser delBean = new OmExamUser();
			delBean.setPlanId(plan.getPlanId());
			delBean.setPaperId(plan.getPaperId());
			delBean.setExamUserType("1");
			this.deleteExample(delBean);
			if(addList.size()>0) {
				//插入
				for(OmExamUser examUser:addList) {
					omExamUserMapper.insertSelective(examUser);
				}
			}
			message += "导入成功，共导入"+addList.size()+"条数据。";
			return AjaxObject.newOk(message);
		}
	}
	
	
	/**
               * 不够位数的在前面补0，保留num的长度位数字
     * @param code
     * @return
     */
    private String autoGenericCode(String code, int num) {
        String result = "";
        if(code.length()>num) {
        	num = code.length();
        }
        result = String.format("%0" + num + "d", Integer.parseInt(code) + 1);
        return result;
    }

    @Override
	public void exportAssignUser(HttpServletResponse response, List<OmExamUser> list) throws Exception {
		String[] titles= new String[]{"姓名","证件号码(用户名)","座位号"};
		String[] mappingFields = {"groupId","userId","seatNum"};
		int i=1;
		for(OmExamUser bean:list) {
			//获取用户信息
			OmUser userBean = userService.selectByPrimaryKey(bean.getUserId());
			bean.setGroupId(userBean.getRealName());
			bean.setUserId(userBean.getUserName());
		}
		ExcelUtils.writeOneSheet(list, titles, mappingFields, "数据",response.getOutputStream());
	}
}
