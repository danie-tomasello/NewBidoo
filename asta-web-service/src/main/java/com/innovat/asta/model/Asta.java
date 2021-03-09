package com.innovat.asta.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ASTA")
public class Asta {

	@Id
    @Column(name = "ID", length = 50, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.PERSIST)
	@JoinColumn(name="PRODUCT_ID",nullable=false)
    private Product product;

	public Asta() {}
	
	public Asta(Long id, Product product) {
		super();
		this.id = id;
		this.product = product;
	}

	
}
