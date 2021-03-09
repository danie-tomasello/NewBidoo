package com.innovat.asta.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "CATEGORY")
public class Category {

	@Id
    @Column(name = "ID", length = 50, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "NAME", length = 50, unique = true)
    @NotNull
    private String name;
	
	@ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Product> productList;
	
	public Category() {}
	public Category(String name) {
		this.name=name;
	}
}
