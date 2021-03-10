package com.innovat.userservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U>
{

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    
    @Column(name = "last_modified_by")
    private U lastModifiedBy;
    
    @Column(name = "VERIFICATION_CODE", length = 64)    
    private String verification;
    
    public void setLastModifiedBy(U username) {
    	this.lastModifiedBy=username;
    }
    
    public void setVerification(String verification) {
    	this.verification=verification;
    }
    public String getVerification() {
    	return verification;
    }
    
}
