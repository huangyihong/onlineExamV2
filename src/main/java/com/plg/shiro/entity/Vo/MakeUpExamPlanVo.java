package com.plg.shiro.entity.Vo;

import java.util.Date;

import com.plg.shiro.entity.OmExamPlanVo;

public class MakeUpExamPlanVo extends OmExamPlanVo{
    
    private Integer userNum;//考试人数

    private Integer passNum;//及格人数

    private Integer nopassNum;//不及格人数

	public Integer getUserNum() {
		return userNum;
	}

	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}

	public Integer getPassNum() {
		return passNum;
	}

	public void setPassNum(Integer passNum) {
		this.passNum = passNum;
	}

	public Integer getNopassNum() {
		return nopassNum;
	}

	public void setNopassNum(Integer nopassNum) {
		this.nopassNum = nopassNum;
	}


}