package com.andrelake.supplementstore.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class SupplementStore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "national_id", nullable = false)
	private String nationalId;
	
	@CreationTimestamp
	@Column(name = "registration_date", nullable = false, columnDefinition = "datetime")
	private LocalDateTime registrationDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "supStore")
	private List<Payment> payments = new ArrayList<>();
	
	@OneToMany(mappedBy = "supStore")
	private List<Customer> customers = new ArrayList<>(); 
}
