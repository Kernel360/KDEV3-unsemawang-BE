// package com.palbang.unsemawang.oauth2.controller;
//
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// public class LoginTestController {
// 	@GetMapping("/")
// 	@ResponseBody
// 	public String mainPage() {
// 		return "index.html";
// 	}
//
//     /*
//     @GetMapping("/login/oauth2/code/google")
//     public String OAuthTest(@RequestParam("code") String code) {
//         //위에 주소로 받는거 어떻게 검증하냐????????
//         System.out.println("코드(code):"+code);
//         return "google";
//     }*/
//
// 	@GetMapping("/login")
// 	public String loginPage() {
// 		return "login2";
// 	}
//
// 	@GetMapping("/join")
// 	public String join() {
// 		return "join";
// 	}
//
// 	@GetMapping("favicon.ico")
// 	public void favicon() {
// 		// 빈 응답 처리
// 	}
// }
