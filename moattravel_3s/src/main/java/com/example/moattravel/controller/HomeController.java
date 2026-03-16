package com.example.moattravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//これはコントローラクラスとして使う　という宣言
public class HomeController{
	@GetMapping("/") //Getメソッドをindexメソッドに紐づけるよ！の意味
	/*@GetMapping("/")アノテーションをつけた場合、
	 * 今回の場合、アプリのトップページに（GETメソッドで）
	 * アクセスされたときにそのメソッドが実行される
	 * */
	public String index() {
		
		//呼び出すビューの名前をreturnで返す（htmlは省略する）
		//src/main/resources/templatesフォルダ内の"index"が呼び出されている
		return "index";
	}
	
}
