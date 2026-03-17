package com.example.moattravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.moattravel.entity.House;
import com.example.moattravel.form.HouseEditForm;
import com.example.moattravel.form.HouseRegisterForm;
import com.example.moattravel.repository.HouseRepository;
import com.example.moattravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
	private final HouseRepository houseRepository;
	private final HouseService houseService;

	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
		this.houseRepository = houseRepository;
		this.houseService = houseService;

	}

	@GetMapping
	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {

		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) {
			//キーワード検索して該当するものがあったらこれを表示してね
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			//無かった場合は全部表示で良いよ
			housePage = houseRepository.findAll(pageable);
		}

		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);

		return "admin/houses/index";
	}

	@GetMapping("{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);

		model.addAttribute("house", house);

		return "admin/houses/show";
	}

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm());
		return "admin/houses/register";

	}

	@PostMapping("/create")
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin/houses/register";
		}
		houseService.create(houseRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");

		return "redirect:/admin/houses";
	}

	@GetMapping("/{id}/edit")
	/*これは編集画面を表示するための処理
	 * */

	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		String imageName = house.getImageName();
		HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(),
				house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(),
				house.getPhoneNumber());

		model.addAttribute("imageName", imageName);
		model.addAttribute("houseEditForm", houseEditForm);

		return "admin/houses/edit";
	}

	@PostMapping("/{id}/update")
	/*フォーム送信（更新・登録など）を処理する
	 * 
	 * 更新処理時（今回はFormなのでこのイメージ）
	 * HTML → Form → Controller → DB（idで更新）
	 *<input th:field="*{name}">
	 *このhtml上の記載によって入力値がHouseEditFormに
	 *自動で入るようになる
	 *だから、htmlの入力値→HouseEditFormに入る
	 * */
	public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		//エラーの場合はフォーム画面に留まって
		if (bindingResult.hasErrors()) {

			return "admin/houses/edit";
		}
		//OKな場合は更新して、編集完了のメッセージと共に一覧のページにアクセスしてね
		houseService.update(houseEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");

		return "redirect:/admin/houses";
	}
	/*更新と再度アクセス、何が違うの…？
	 * return 画面名　とredirect:～
	 * return 画面名→画面遷移…の処理だと、更新処理はPOSTで実行
	 * …再度処理を行っちゃう（二度更新掛けられちゃう？）
	 * redirect:～はただ読み込むだけ、なので更新は行わない
	 * 二重で処理を行われちゃうのを防止している
	 * 
	 * なので、基本更新処理（追加・削除も）の後はredirect:を使うのがセオリー
	 * 
	 * 二重送信…　決済するときに決済処理が二重で行われちゃったりしたら大変だよね
	 * 
	 * */
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id")Integer id,RedirectAttributes redirectAttributes) {
		houseRepository.deleteById(id);
		//民宿削除したら一覧ページにアクセスしてね
		redirectAttributes.addFlashAttribute("successMessage","民宿を削除しました");
		//redirect:　このURLにもう一度リクエストを送っての意味。
		return "redirect:/admin/houses";
	}
}
