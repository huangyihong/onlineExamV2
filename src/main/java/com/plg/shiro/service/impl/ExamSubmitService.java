package com.plg.shiro.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plg.shiro.dao.OmExamAnswerMapper;
import com.plg.shiro.dao.OmExamSubmitMapper;
import com.plg.shiro.dao.OmExamSubmitVoMapper;
import com.plg.shiro.dao.OmUserMapper;
import com.plg.shiro.entity.OmExamAnswer;
import com.plg.shiro.entity.OmExamAnswerExample;
import com.plg.shiro.entity.OmExamPlan;
import com.plg.shiro.entity.OmExamPlanVo;
import com.plg.shiro.entity.OmExamSubmit;
import com.plg.shiro.entity.OmExamSubmitExample;
import com.plg.shiro.entity.OmExamUser;
import com.plg.shiro.entity.OmPaper;
import com.plg.shiro.entity.OmQuestion;
import com.plg.shiro.entity.OmUser;
import com.plg.shiro.entity.OmUserExample;
import com.plg.shiro.entity.Vo.OmExamSubmitVo;
import com.plg.shiro.entity.Vo.OmExamSubmitVoExample;
import com.plg.shiro.entity.Vo.OmQuestionVo;
import com.plg.shiro.service.IExamAnswerService;
import com.plg.shiro.service.IExamPlanService;
import com.plg.shiro.service.IExamSubmitService;
import com.plg.shiro.service.IExamUserService;
import com.plg.shiro.service.IPaperService;
import com.plg.shiro.service.IQuestionService;
import com.plg.shiro.service.IUserService;
import com.plg.shiro.util.DateUtil;
import com.plg.shiro.util.UUIDUtil;
import com.plg.shiro.util.dwz.LayuiPage;

@Service
public class ExamSubmitService implements IExamSubmitService {
	private static final Logger logger = LoggerFactory.getLogger(ExamSubmitService.class);

	@Resource
	private OmExamSubmitMapper omExamSubmitMapper;
	
	@Resource
	private OmExamSubmitVoMapper omExamSubmitVoMapper;
	
	@Resource
	private IExamUserService examUserService;
	
	@Resource
	private IExamAnswerService answerService;
	
	@Resource
	private IQuestionService questionService;
	
	@Resource
	private IExamPlanService planService;
	
	@Resource
	private OmExamAnswerMapper omExamAnswerMapper;
	
	@Resource
	private IUserService omUserService;
	
	@Resource
	private IPaperService paperService;

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private OmUserMapper omUserMapper;
	
	@Override
	public void insertExamSubmit(OmExamSubmit record) {
		omExamSubmitMapper.insert(record);
	}

	@Override
	public int updateByPrimaryKeySelective(OmExamSubmit record) {
		return omExamSubmitMapper.updateByPrimaryKeySelective(record);
	}
	
	@Override
	public OmExamSubmit selectByUserId(String planId, String paperId, String userId) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		if(StringUtils.isNoneBlank(paperId)){
			criteria.andPaperIdEqualTo(paperId);
		}
		criteria.andUserIdEqualTo(userId);
		List<OmExamSubmit> list = omExamSubmitMapper.selectByExample(example);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OmExamSubmit> findPageList(HttpServletRequest request, LayuiPage page) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		example.setLimitPageSize(page.getLimit());
		example.setLimitStart(page.limitStart());
		example.setOrderByClause("START_TIME desc");
		page.setTotalCount(omExamSubmitMapper.countByExample(example));
		return omExamSubmitMapper.selectByExample(example);
	}
	
	@Override
	public List<OmExamSubmitVo> findVoPageList(HttpServletRequest request, LayuiPage page) {
		OmExamSubmitVoExample example = new OmExamSubmitVoExample();
		OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
		String planId = request.getParameter("planId");
		if(StringUtils.isNotBlank(planId)){
			criteria.andPlanIdEqualTo(planId);
		}
		String planName = request.getParameter("planName");
		if(StringUtils.isNotBlank(planName)){
			criteria.andPlanNameLike("%"+planName+"%");
		}
		String paperName = request.getParameter("paperName");
		if(StringUtils.isNotBlank(paperName)){
			criteria.andPaperNameLike("%"+paperName+"%");
		}
		String realName = request.getParameter("realName");
		if(StringUtils.isNotBlank(realName)){
			criteria.andRealNameLike("%"+realName+"%");
		}
		String planType = request.getParameter("planType");
		if(StringUtils.isNotBlank(planType)){
			criteria.andPlanTypeEqualTo(planType);
		}
		example.setLimitPageSize(page.getLimit());
		example.setLimitStart(page.limitStart());
		example.setOrderByClause("START_TIME desc");
		page.setTotalCount(omExamSubmitVoMapper.countByExample(example));
		List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
		for(OmExamSubmitVo bean:list) {
			setBeanValue(bean);
		}
		return list;
		//return omExamSubmitVoMapper.selectByExample(example);
	}
	
	@Override
	public OmExamSubmitVo selectVoByUserId(String planId, String paperId, String userId) {
		OmExamSubmitVoExample example = new OmExamSubmitVoExample();
		OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andPaperIdEqualTo(paperId);
		criteria.andUserIdEqualTo(userId);
		List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
		for(OmExamSubmitVo bean:list) {
			setBeanValue(bean);
		}
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OmExamSubmitVo> findUserPageList(HttpServletRequest request, LayuiPage page, OmUser user,String examUserType,String status) {
		//获取当前用户被授权的考试安排1或者阅卷安排2
		List<OmExamUser> listExam = examUserService.selectUserExam(user, examUserType);
		List<String> planIdList = new ArrayList<String>();
		for(OmExamUser examUser:listExam){
			planIdList.add(examUser.getPlanId());
		}
		if(planIdList.size()>0){
			OmExamSubmitVoExample example = new OmExamSubmitVoExample();
			OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
			criteria.andPlanIdIn(planIdList);
			if(StringUtils.isNotBlank(status)){
				criteria.andStatusEqualTo(status);
			}
			if("1".equals(examUserType)){//回顾我的试卷
				criteria.andUserIdEqualTo(user.getUserId());
			}
			String planId = request.getParameter("planId");
			if(StringUtils.isNotBlank(planId)){
				criteria.andPlanIdEqualTo(planId);
			}
			String planName = request.getParameter("planName");
			if(StringUtils.isNotBlank(planName)){
				criteria.andPlanNameLike("%"+planName+"%");
			}
			String paperName = request.getParameter("paperName");
			if(StringUtils.isNotBlank(paperName)){
				criteria.andPaperNameLike("%"+paperName+"%");
			}
			String realName = request.getParameter("realName");
			if(StringUtils.isNotBlank(realName)){
				criteria.andRealNameLike("%"+realName+"%");
			}
			String planType = request.getParameter("planType");
			if(StringUtils.isNotBlank(planType)){
				criteria.andPlanTypeEqualTo(planType);
			}
			example.setLimitPageSize(page.getLimit());
			example.setLimitStart(page.limitStart());
			example.setOrderByClause("START_TIME desc");
			page.setTotalCount(omExamSubmitVoMapper.countByExample(example));
			
			List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
			for(OmExamSubmitVo bean:list) {
				setBeanValue(bean);
			}
			return list;
		}else{
			return null;
		}
		
	}

	private void setBeanValue(OmExamSubmitVo bean) {
		//查询plan_name
		OmExamPlan planBean = planService.selectByPrimaryKey(bean.getPlanId());
		if(planBean!=null) {
			bean.setPlanName(planBean.getPlanName());;
		}
		//查询real_name
		OmUser userBean = omUserService.selectByPrimaryKey(bean.getUserId());
		if(userBean!=null) {
			bean.setRealName(userBean.getRealName());
		}
		//查询user_answer_count
		OmExamAnswerExample exampleAnswer = new OmExamAnswerExample();
		OmExamAnswerExample.Criteria criteria1 = exampleAnswer.createCriteria();
		criteria1.andUserIdEqualTo(bean.getUserId());
		criteria1.andPlanIdEqualTo(bean.getPlanId());
		criteria1.andPaperIdEqualTo(bean.getPaperId());
		criteria1.andAnswerResultIsNotNull();
		List<OmExamAnswer> listAnswer = omExamAnswerMapper.selectByExample(exampleAnswer);
		bean.setUserAnswerCount(listAnswer.size());
	}

	@Override
	public List<OmExamSubmit> findListByPlanId(String planId, String paperId, String status) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andPaperIdEqualTo(paperId);
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
		return omExamSubmitMapper.selectByExample(example);
	}

	@Override
	public OmExamSubmit selectByPrimaryKey(String submitId) {
		return omExamSubmitMapper.selectByPrimaryKey(submitId);
	}
	
	@Override
	public List<OmExamSubmitVo> findUserPageList(HttpServletRequest request, LayuiPage page, String status) {
		OmExamSubmitVoExample example = new OmExamSubmitVoExample();
		OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
		queryCommon(request, status, criteria);
		example.setLimitPageSize(page.getLimit());
		example.setLimitStart(page.limitStart());
		page.setTotalCount(omExamSubmitVoMapper.countByExample(example));
		if("3,4".equals(status)){//发布成绩页面
			example.setOrderByClause("status,START_TIME desc");
		}
		if("1,2".equals(status)){//监控页面
			example.setOrderByClause("user_answer_count desc,START_TIME desc");
		}
		List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
		for(OmExamSubmitVo bean:list) {
			setBeanValue(bean);
		}
		return list;
		//return omExamSubmitVoMapper.selectByExample(example);
		
	}

	@Override
	public void updateBatch(String ids, String status) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria c = example.createCriteria();
        c.andSubmitIdIn(Arrays.asList(ids.split(",")));
        List<OmExamSubmit> list = omExamSubmitMapper.selectByExample(example);
        for(OmExamSubmit bean:list){
        	bean.setStatus(status);
	        if("4".equals(bean.getStatus())){//成绩发布
				bean.setPublishTime(DateUtil.dateParse(new Date(),""));
			}
	        omExamSubmitMapper.updateByPrimaryKeySelective(bean);
        }
	}
	
	@Override
	public void deleteBatch(String ids) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria c = example.createCriteria();
        c.andSubmitIdIn(Arrays.asList(ids.split(",")));
        List<OmExamSubmit> list = omExamSubmitMapper.selectByExample(example);
        for(OmExamSubmit bean:list){
        	//获取答题记录
        	OmExamAnswerExample exampleAnswer = new OmExamAnswerExample();
    		OmExamAnswerExample.Criteria criteria1 = exampleAnswer.createCriteria();
    		criteria1.andUserIdEqualTo(bean.getUserId());
    		criteria1.andPlanIdEqualTo(bean.getPlanId());
    		criteria1.andPaperIdEqualTo(bean.getPaperId());
    		omExamAnswerMapper.deleteByExample(exampleAnswer);
    		omExamSubmitMapper.deleteByPrimaryKey(bean.getSubmitId());
        }
	}

	@Override
	public List<OmExamSubmitVo> getList(HttpServletRequest request,String status) {
		OmExamSubmitVoExample example = new OmExamSubmitVoExample();
		OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
		queryCommon(request, status, criteria);
		example.setOrderByClause("START_TIME desc");
		if("3,4".equals(status)){//发布成绩页面
			example.setOrderByClause("status,START_TIME desc");
		}
		List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
		for(OmExamSubmitVo bean:list) {
			setBeanValue(bean);
		}
		return list;
		//return omExamSubmitVoMapper.selectByExample(example);
	}

	private void queryCommon(HttpServletRequest request, String status, OmExamSubmitVoExample.Criteria criteria) {
		String submitId = request.getParameter("submitId");
		if(StringUtils.isNotBlank(submitId)){
			criteria.andSubmitIdIn(Arrays.asList(submitId.split(",")));
		}
		String paperId = request.getParameter("paperId");
		if(StringUtils.isNotBlank(paperId)){
			criteria.andPaperIdEqualTo(paperId);
		}
		String planId = request.getParameter("planId");
		if(StringUtils.isNotBlank(planId)){
			criteria.andPlanIdEqualTo(planId);
		}
		String planName = request.getParameter("planName");
		if(StringUtils.isNotBlank(planName)){
			criteria.andPlanNameLike("%"+planName+"%");
		}
		String paperName = request.getParameter("paperName");
		if(StringUtils.isNotBlank(paperName)){
			criteria.andPaperNameLike("%"+paperName+"%");
		}
		String realName = request.getParameter("realName");
		if(StringUtils.isNotBlank(realName)){
			criteria.andRealNameLike("%"+realName+"%");
		}
		String planType = request.getParameter("planType");
		if(StringUtils.isNotBlank(planType)){
			criteria.andPlanTypeEqualTo(planType);
		}
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
	}
	
	@Override
	public List<OmExamSubmitVo> findVoUserList(HttpServletRequest request, String status) {
		OmExamSubmitVoExample example = new OmExamSubmitVoExample();
		OmExamSubmitVoExample.Criteria criteria = example.createCriteria();
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
		String planId = request.getParameter("planId");
		if(StringUtils.isNotBlank(planId)){
			criteria.andPlanIdEqualTo(planId);
		}
		String planName = request.getParameter("planName");
		if(StringUtils.isNotBlank(planName)){
			criteria.andPlanNameLike("%"+planName+"%");
		}
		String paperName = request.getParameter("paperName");
		if(StringUtils.isNotBlank(paperName)){
			criteria.andPaperNameLike("%"+paperName+"%");
		}
		String realName = request.getParameter("realName");
		if(StringUtils.isNotBlank(realName)){
			criteria.andRealNameLike("%"+realName+"%");
		}
		example.setOrderByClause("START_TIME desc");
		if("3,4".equals(status)){//发布成绩页面
			example.setOrderByClause("status,START_TIME desc");
		}
		List<OmExamSubmitVo> list =  omExamSubmitVoMapper.selectByExample(example);
		for(OmExamSubmitVo bean:list) {
			setBeanValue(bean);
		}
		return list;
		//return omExamSubmitVoMapper.selectByExample(example);
	}
	
	@Override
	public Map<String, Object> sqlQueryScoreNumMaxMin(String planId, String status,String groupId) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select IFNULL(max(t.total_score),0) as max_score,IFNULL(min(t.total_score),0) as min_score,ROUND(IFNULL(avg(t.total_score),0)) as avg_score, "
				+ "count(1) as user_num, IFNULL(sum(CASE when total_score>=90 then 1 else 0 end),0) AS perfect_num,\n" + 
				"IFNULL(sum(CASE when total_score>=70 and total_score<90 then 1 else 0 end),0)  AS good_num,\n" + 
				"IFNULL(sum(CASE when total_score>=60 and total_score<70 then 1 else 0 end),0) AS pass_num,\n" + 
				"IFNULL(sum(CASE when total_score<60 then 1 else 0 end),0) AS nopass_num ");
		sqlBuffer.append("from om_exam_submit t where 1=1 ");
		sqlBuffer.append("and plan_id = '"+planId+"' ");
		sqlBuffer.append("and status = '"+status+"' ");
		if(StringUtils.isNotBlank(groupId)) {
			sqlBuffer.append("and user_id in (select user_id from om_user t where t.group_id='"+groupId+"' ) ");
		}
		Map<String, Object> map = jdbcTemplate.queryForMap(sqlBuffer.toString());
		return map;
	}

	@Override
	public OmExamSubmit autoMarkTotalScore(OmExamPlan plan, OmPaper paper, OmExamSubmit bean) {
		String answerUserId = bean.getUserId();
		String planId = plan.getPlanId();
		String paperId = plan.getPaperId();
		//答案
		List<OmExamAnswer> answerList = answerService.selectByUserPaperID(answerUserId, planId, paperId);
		Map<String,OmExamAnswer> answerMap = new HashMap<String,OmExamAnswer>();
		for(OmExamAnswer answer:answerList ){
			answerMap.put(answer.getQuestionId(), answer);
		}
		List<OmQuestion> questionList = new ArrayList<OmQuestion>();
		if("1".equals(paper.getAddMode())){//人工
			questionList = questionService.selectPaperQuestion(paper.getPaperId());
		}else{
			//随机试卷
			questionList = questionService.selectUserPaperQuestion(paper.getPaperId(),answerUserId);
		}
		int totalScore=0;
		for(OmQuestion questionBean:questionList){
			String questionType = questionBean.getQuestionType();
		    OmQuestionVo questionVo = new OmQuestionVo();
		    BeanUtils.copyProperties(questionBean,questionVo);
		    if(answerMap.get(questionBean.getQuestionId())!=null){
		    	questionVo.setAnswerResult(answerMap.get(questionBean.getQuestionId()).getAnswerResult());
		    	questionVo.setAnswerId(answerMap.get(questionBean.getQuestionId()).getAnswerId());
		    	questionVo.setMarkScore(answerMap.get(questionBean.getQuestionId()).getMarkScore());
		    	questionVo.setMarkText(answerMap.get(questionBean.getQuestionId()).getMarkText());
		    }
		    if(StringUtils.isNotBlank(questionVo.getAnswerResult())&&questionVo.getAnswerResult().equals(questionVo.getRightResult())){
		    	totalScore +=questionVo.getQuestionScore();
		    }
		}
		//自动阅卷
		bean.setTotalScore(totalScore);
		bean.setMarkTime(DateUtil.dateParse(new Date(),""));
		bean.setMarkUser("系统自动阅卷");
		bean.setPublishTime(DateUtil.dateParse(new Date(),""));
		bean.setStatus("4");
		return bean;
	}

	@Override
	@Transactional
	public void saveExamSubmit(OmExamSubmit bean) {
		Subject currentUser = SecurityUtils.getSubject();
		OmUser omUser = (OmUser) SecurityUtils.getSubject().getSession().getAttribute("om_user");
		if("1".equals(bean.getStatus())){//开始答题
			bean.setSubmitId(UUIDUtil.getUUID());
			bean.setUserId(omUser.getUserId());
			bean.setStartTime(DateUtil.dateParse(new Date(),""));
			bean.setCreateTime(DateUtil.dateParse(new Date(),""));
			OmExamPlan plan = planService.selectByPrimaryKey(bean.getPlanId());
			if(plan!=null) {
				bean.setPlanType(plan.getPlanType());
			}
			this.insertExamSubmit(bean);
		}else if("2".equals(bean.getStatus())){//提交答题
			bean.setUserId(omUser.getUserId());
			bean.setSubmitTime(DateUtil.dateParse(new Date(),""));
			//判断是否为自动阅卷
			OmExamPlan plan = planService.selectByPrimaryKey(bean.getPlanId());
			OmPaper paper = paperService.selectByPrimaryKey(plan.getPaperId());
			String autoMarkFlag = plan.getAutoMarkFlag();//是否自动阅卷
			if("1".equals(autoMarkFlag)){//自动阅卷
				bean = this.autoMarkTotalScore(plan, paper, bean);
			}
			this.updateByPrimaryKeySelective(bean);
		}else if("3".equals(bean.getStatus())){//阅卷完成
			bean.setMarkTime(DateUtil.dateParse(new Date(),""));
			bean.setMarkUserId(omUser.getUserId());
			bean.setMarkUser(omUser.getRealName());
			this.updateByPrimaryKeySelective(bean);
		}else if("4".equals(bean.getStatus())){//成绩发布
			OmExamSubmit oldBean = this.selectByPrimaryKey(bean.getSubmitId());
			if("2".equals(oldBean.getStatus())){//同时阅卷完成并发布成绩的
				bean.setMarkTime(DateUtil.dateParse(new Date(),""));
				bean.setMarkUserId(omUser.getUserId());
				bean.setMarkUser(omUser.getRealName());
			}
			bean.setPublishTime(DateUtil.dateParse(new Date(),""));
			this.updateByPrimaryKeySelective(bean);
		}
	}

	@Override
	@Transactional
	public void saveMarkExam(OmExamSubmit bean, List<OmExamAnswer> omExamAnswerList) {
		this.saveExamSubmit(bean);
		answerService.updateBatch(omExamAnswerList);
	}

	@Override
	@Transactional
	public void editResultExam(OmExamSubmit bean, List<OmExamAnswer> omExamAnswerList) {
		this.updateByPrimaryKeySelective(bean);
		answerService.updateBatch(omExamAnswerList);
	}

	@Override
	public void deleteExamSubmit(String userId, String planId, String paperId) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria c = example.createCriteria();
		c.andUserIdEqualTo(userId);
		c.andPlanIdEqualTo(planId);
		c.andPaperIdEqualTo(paperId);
		this.omExamSubmitMapper.deleteByExample(example);
	}

	@Override
	public int getCountByPlanId(String planId, String paperId, String status) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andPaperIdEqualTo(paperId);
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
		return (int) omExamSubmitMapper.countByExample(example);
	}

	@Override
	public int getNopassNum(String planId, String paperId, String status, int totalScore) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andPaperIdEqualTo(paperId);
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
		criteria.andTotalScoreLessThan(totalScore);
		return (int) omExamSubmitMapper.countByExample(example);
	}

	@Override
	public List<OmExamSubmit> getNopassList(String planId, String paperId, String status, int totalScore) {
		OmExamSubmitExample example = new OmExamSubmitExample();
		OmExamSubmitExample.Criteria criteria = example.createCriteria();
		criteria.andPlanIdEqualTo(planId);
		criteria.andPaperIdEqualTo(paperId);
		if(StringUtils.isNotBlank(status)){
			criteria.andStatusIn(Arrays.asList(status.split(",")));
		}
		criteria.andTotalScoreLessThan(totalScore);
		return omExamSubmitMapper.selectByExample(example);
	}

	@Override
	public List<OmUser> getMakeupExamUserList(String planId) {
		OmExamPlanVo planVo = planService.selectVoByPrimaryKey(planId);
		if(planVo!=null) {
			int passingScore = planVo.getPassingScore();//及格分数
  			int paperScore = planVo.getPaperScore();//总分数
  			String passingType = planVo.getPassingType();
  			if("2".equals(passingType)){//百分比
  				 passingScore =Math.round((paperScore*passingScore)/100);
  			}
  			List<OmExamSubmit> listSubmit = this.getNopassList(planVo.getPlanId(), planVo.getPaperId(), "4", passingScore);
  			List<String> userIdList = new ArrayList<String>();
  			for(OmExamSubmit bean:listSubmit) {
  				userIdList.add(bean.getUserId());
  			}
  			if(userIdList.size()>0) {
  				OmUserExample example = new OmUserExample();
  				OmUserExample.Criteria c = example.createCriteria();
  		        c.andUserIdIn(userIdList);
  		        List<OmUser> userList = omUserMapper.selectByExample(example);
  		        return userList;
  			}
		}
		return null;
	}
	
}
