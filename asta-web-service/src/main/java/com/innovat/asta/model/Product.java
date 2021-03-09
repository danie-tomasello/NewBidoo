package com.innovat.asta.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "PRODUCT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

	@Id
    @Column(name = "ID", length = 50, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "NAME", length = 50)
    @NotNull
    private String name;
	
	@Column(name = "BRAND", length = 50)
    @NotNull
    private String brand;		
	
	@Column(name = "PRICE")
    @NotNull
    private double price;
	
	@OneToMany(mappedBy = "product",cascade= {CascadeType.ALL})
	@JsonBackReference
	private List<Asta> aste;
	
	@OneToMany(mappedBy = "product",cascade= {CascadeType.ALL})
	@JsonBackReference
	private List<ImageModel> imageList;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade= {CascadeType.REMOVE})
    @JoinTable(
            name = "PRODUCT_CATERGORY",
            joinColumns = {@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")})
    private List<Category> categories;

	public Product() {}
	
	public Product(Long id, String name, String brand, double price, List<Asta> aste, List<ImageModel> imageList,
			List<Category> categories) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.aste = aste;
		this.imageList = imageList;
		this.categories = categories;
	}


	
	
}
