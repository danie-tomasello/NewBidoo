package com.innovat.userservice.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;
import lombok.ToString;



@Data
@Entity
@Table(name = "USERS")
@ToString
public class User implements Serializable{


    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3518018508482697232L;

	@Id 
	@GeneratedValue
	@Column(name = "USERID", length = 50, unique = true)
	private long id;
	
	@Column(name = "USERNAME", length = 50, unique = true)
    @NotNull(message="{NotNull.User.username.Validation}")
    @Size(min = 4, max = 50, message="{Size.User.username.Validation}")
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull(message="{NotNull.User.password.Validation}")
    @Size(min = 4, max = 100, message="{Size.User.password.Validation}")
    private String password;

    @Column(name = "ENABLED")
    @NotNull
    private Boolean enabled;
    
    @Column(name = "EMAIL", length = 50, unique = true)
    @NotNull(message="{NotNull.User.email.Validation}")
    @Size(min = 4, max = 50 ,message="{Size.User.email.Validation}")
    private String email;
    
    @Column(name = "PHONE_NUMBER", length = 15, unique = true)
    @NotNull(message="{NotNull.User.phoneNumber.Validation}")
    @Size(min = 4, max = 15, message="{Size.User.phoneNumber.Validation}")
    private String phoneNumber;
    
    @Column(name = "VERIFICATION_CODE", length = 64)    
    private String verification;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATION_DATE")
    private Date dataCreaz;
    
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_AUTHORITIES",
            joinColumns = {@JoinColumn(name = "USER_USERNAME", referencedColumnName = "USERNAME")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;



}