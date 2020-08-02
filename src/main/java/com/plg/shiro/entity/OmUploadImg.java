package com.plg.shiro.entity;

public class OmUploadImg {
    private String id;

    private String relationId;

    private String imgSrc;

    private Integer imgOrder;
    
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Integer getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(Integer imgOrder) {
        this.imgOrder = imgOrder;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}