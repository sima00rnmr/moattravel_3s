package com.example.moattravel.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moattravel.entity.House;



/*JpaRepositoryインターフェースを継承すると…
 * findAll()：テーブル内のすべてのエンティティを取得
 * getReferenceById(id)：指定したidのエンティティを取得
 * save(エンティティ)：指定したエンティティを保存または更新
 * delete(エンティティ)：指定したエンティティを削除する
 * deleteById(id)：指定したidのエンティティを削除
 * 
 * 等の機能が利用可能になる
 * */

public interface HouseRepository extends JpaRepository<House,Integer>{
	//findBy○○○Like()というメソッドで
	//SQLのLIKE句と同様のクエリを実行できる
	public Page<House>findByNameLike(String keyword,Pageable pageable);
	
}