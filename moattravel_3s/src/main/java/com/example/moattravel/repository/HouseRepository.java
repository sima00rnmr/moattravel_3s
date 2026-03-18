package com.example.moattravel.repository;

import java.util.List;

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

public interface HouseRepository extends JpaRepository<House, Integer> {
	//findBy○○○Like()というメソッドで
	//SQLのLIKE句と同様のクエリを実行できる

	public Page<House> findByNameLike(String keyword, Pageable pageable);

	public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword,
			Pageable pageable);

	public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);

	public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);

	public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

	public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

	public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);

	public Page<House> findAllByOrderByPriceAsc(Pageable pageable);

	public List<House> findTop10ByOrderByCreatedAtDesc();

	public Page<House> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);

	public Page<House> findByAddressLike(String area, Pageable pageable);

	public Page<House> findByPriceLessThanEqual(Integer price, Pageable pageable);
}