package com.plg.shiro.entity.Vo;

import java.util.List;

import com.plg.shiro.entity.OmQuestion;
import com.plg.shiro.entity.OmUploadImg;

public class OmQuestionVo extends OmQuestion{
	private String answerResult;
	
	private String answerId;
	
    private Integer markScore;

    private String markText;
    
    private List<OmUploadImg> imgList;

	public String getAnswerResult() {
		return answerResult;
	}

	public void setAnswerResult(String answerResult) {
		this.answerResult = answerResult;
	}

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public Integer getMarkScore() {
		return markScore;
	}

	public void setMarkScore(Integer markScore) {
		this.markScore = markScore;
	}

	public String getMarkText() {
		return markText;
	}

	public void setMarkText(String markText) {
		this.markText = markText;
	}

	public List<OmUploadImg> getImgList() {
		return imgList;
	}

	public void setImgList(List<OmUploadImg> imgList) {
		this.imgList = imgList;
	}
}