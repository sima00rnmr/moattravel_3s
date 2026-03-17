package com.example.moattravel.event;


/*① ユーザー登録
*② イベント発行
*③ Listenerが受け取る
*④ UUID生成
*⑤ DBに保存
*⑥ メール送信
*の、流れ…
*/

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.moattravel.entity.User;
import com.example.moattravel.service.VerificationTokenService;
/*
 * @Componentアノテーション…確認する
 * 「このクラス使うよ」と登録
 * */
@Component
public class SignupEventListener{
	private final VerificationTokenService verificationTokenService;  
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService,JavaMailSender mailSender) {
		this.verificationTokenService =verificationTokenService;
		this.javaMailSender =mailSender;	
	}
	/*イベント来たらこのメソッド動かす の意味
	 * 引数はどのイベントを受け取るか指定してる
	 * SignupEventをトリガーにこのメソッドは動きますよってこと
	 * 
	 * 
	 * */
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		User user =signupEvent.getUser();
		//UUID=認証用トークン作る
		String token =UUID.randomUUID().toString();
		verificationTokenService.create(user, token);
		String recipientAddress = user.getEmail();
		String subject= "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl()+"/verify?token=" +token;
		String message="以下のリンクをクリックして会員登録を完了してください。";
		
		
		//SimpleMailMessage…メールの中身を作る
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message+"\n" + confirmationUrl);
		//実際にメールを送信する役
		javaMailSender.send(mailMessage);
	}
	
}