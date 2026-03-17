package com.example.moattravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moattravel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	//ロール名でロールを検索するmethod
	public Role findByName(String name);

}
