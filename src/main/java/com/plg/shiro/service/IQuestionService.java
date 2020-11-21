package com.plg.shiro.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.plg.shiro.entity.OmQuestion;
import com.plg.shiro.entity.OmUploadImg;
import com.plg.shiro.util.dwz.LayuiPage;

public interface IQuestionService {
    int deleteByPrimaryKey(String questionId);

    int insert(OmQuestion record);
    
    int insert(OmQuestion record,String imgIds,String imgSrcs);

    OmQuestion selectByPrimaryKey(String questionId);
    
    List<OmQuestion> selectAll();

    int updateByPrimaryKeySelective(OmQuestion record);
    
    int updateByPrimaryKeySelective(OmQuestion record,String imgIds,String imgSrcs);
    
    List<OmQuestion> findPageList(HttpServletRequest request, LayuiPage page);

	void deleteBatch(String ids);
	
	List<OmQuestion> selectAuto(String questionType,String courseId,int count);
	
	List<OmQuestion> selectPaperQuestion(String paperId);
	
	List<OmQuestion> selectQuestionByIds(String ids);

	List<OmQuestion> selectUserPaperQuestion(String paperId, String answerUserId);

	List<OmUploadImg> selectQuestionImgByQuestionId(List<String> questionList);
	
	long countQuestionImgByQuestionId(List<String> questionList);

	Map<String, List<OmUploadImg>> getQuestionImgMap(List<OmQuestion> questionList);
}
