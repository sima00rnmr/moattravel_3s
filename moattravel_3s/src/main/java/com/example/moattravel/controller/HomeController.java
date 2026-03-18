package com.example.moattravel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.moattravel.entity.House;
import com.example.moattravel.repository.HouseRepository;

@Controller//これはコントローラクラスとして使う　という宣言
public class HomeController{
	private final HouseRepository houseRepository;
	
	public HomeController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}
	
	@GetMapping("/") //Getメソッドをindexメソッドに紐づけるよ！の意味
	/*@GetMapping("/")アノテーションをつけた場合、
	 * 今回の場合、アプリのトップページに（GETメソッドで）
	 * アクセスされたときにそのメソッドが実行される
	 * */
	public String index(Model model) {
		
		List<House> newHouses =houseRepository.findTop10ByOrderByCreatedAtDesc();
		model.addAttribute("newHouses",newHouses);
		
		//呼び出すビューの名前をreturnで返す（htmlは省略する）
		//src/main/resources/templatesフォルダ内の"index"が呼び出されている
		return "index";
	}
	
}
