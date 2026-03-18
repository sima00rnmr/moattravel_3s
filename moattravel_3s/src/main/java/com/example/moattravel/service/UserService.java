package com.example.moattravel.service;
/*パスワードはハッシュ化してからセットする
 * ハッシュ化…？
 * 平文（そのまま）で保存するのは危険なので
 * 一定のルールに基づき、データを別の値に変換することを指す
 * 作成したロールはROLE_GENERAL（会員）とROLE_ADMIN（管理者）だが
 * Formから登録できるのは会員のみと想定している（ここから登録した人は全員会員扱い）
 * 
 * 
 * 
 * */

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.moattravel.entity.Role;
import com.example.moattravel.entity.User;
import com.example.moattravel.form.SignupForm;
import com.example.moattravel.form.UserEditForm;
import com.example.moattravel.repository.RoleRepository;
import com.example.moattravel.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public User create(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");

		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setPostalCode(signupForm.getPostalCode());
		user.setAddress(signupForm.getAddress());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setRole(role);
		user.setEnabled(false);

		return userRepository.save(user);

	}

	@Transactional
	public void update(UserEditForm userEditForm) {
		User user = userRepository.getReferenceById(userEditForm.getId());

		user.setName(userEditForm.getName());
		user.setFurigana(userEditForm.getFurigana());
		user.setPostalCode(userEditForm.getPostalCode());
		user.setAddress(userEditForm.getAddress());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setEmail(userEditForm.getEmail());

		userRepository.save(user);

	}

	/*以降…　登録時にメールアドレスが既に登録済みかどうか
	 *をチェックする機能をつける
	 * */

	//メールアドレスが登録済みかどうかをチェックする
	public boolean isEmailRegistered(String email) {
		//もし、Userのアドレス（このアドレスを持っているユーザーがいるかどうか）をみつけることが出来たら…nullで返す（アドレスの重複を許容しない）
		User user = userRepository.findByEmail(email);
		return user != null;

	}

	//パスワードとパスワード（確認用）の入力値が一致するかどうかチェックする
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}

	//ユーザーを有効にする
	/*認証に成功した際に実行するメソッド
	 * 
	 * */
	@Transactional
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);

	}

	//メールアドレスが変更されたかどうかチェックする
	public boolean isEmailChanged(UserEditForm userEditForm) {
		User currentUser = userRepository.getReferenceById(userEditForm.getId());
		return !userEditForm.getEmail().equals(currentUser.getEmail());
	}

}