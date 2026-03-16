package com.example.moattravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity//これはエンティティクラスとして使う
@Table(name = "houses")//housesって名前のテーブルにこれを作るよ
@Data//以下はデータとして使う（ゲッターやセッターなどを自動生成）
public class House{
	@Id//こいつを主キーにするよ（指定）
	
	/*@GeneratedValueアノテーションをつけて
	 * strategy = GenerationType.IDENTITYを指定すると
	 * テーブル内のAUTO_INCREMENTを指定したカラムを利用して
	 * idの値を自分で指定しなくても、自動採番されるようになる
	 * （勝手にIDが割り振られる）
	 * 
	 * */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="Id")
	private Integer id;
	
	@Column(name ="name")
	private String name;
	
	@Column(name ="image_name")
	private String imageName;
	
	@Column(name ="description")
	private String description;

	@Column(name ="price")
	private Integer price;
	
	@Column(name ="capacity")
	private Integer capacity;
	
	@Column(name ="postal_code")
	private String postalCode;
	
	@Column(name ="address")
	private String address;
	
	@Column(name ="phone_number")
	private String phoneNumber;
	
	@Column(name ="created_at",insertable = false, updatable = false)
	private Timestamp createdAt;
	
	@Column(name ="updated_at",insertable = false, updatable = false)
	private Timestamp updatedAt;
	
	
}



