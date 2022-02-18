package com.itbank.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.itbank.admin.AdminDTO;
import com.itbank.component.Hash;
import com.itbank.member.MemberDTO;
import com.itbank.service.MailService;
import com.itbank.service.MemberService;

@RestController
public class AjaxMemberController {

	@Autowired private MailService mailService;
	@Autowired private MemberService memberService;

	@Autowired private Hash hash;
	
		@GetMapping("/mailto/{email}/")
		public HashMap<String, String> mailto(@PathVariable String email, HttpSession session) throws IOException{
			System.out.println("인증번호 받은 이메일 : " +  email);
			
			String authNumber = mailService.getAuthNumber();
			System.out.println("인증 번호 : " + authNumber);
			
			String hashNumber = hash.getHash(authNumber);
			System.out.println("인증 해시값 : " + hashNumber);
			
			session.setAttribute("hashNumber", hashNumber);
			
			String account = null;
			
			String filePath = session.getServletContext().getRealPath("/WEB-INF/data/account.dat");
			File f = new File(filePath);
			
			if(f.exists() == false) {
				System.out.println("메일 전송에 필요한 계정 정보가 없습니다");
				return null;
			}
			
			Scanner sc = new Scanner(f);
			
			while(sc.hasNext()) {
				account = sc.nextLine();
			}
			sc.close();
			
			MemberDTO dto = memberService.findByEmail(email);
			
			HashMap<String, String> ret = new HashMap<String, String>();
			
			if(dto != null ) {
				ret.put("status","FAIL");
				ret.put("message","중복되는 이메일입니다.");
			}
			
			else {
				String result = mailService.sendMail(email, authNumber, account);
				
				if(result.equals(authNumber)) {
					ret.put("status", "OK");
					ret.put("message", "인증번호가 발송되었습니다");
				}
			}
			return ret;
		}
		
		
		@GetMapping("/ajaxAuth/{userNumber}")
		public HashMap<String, String> getAuthResult(@PathVariable String userNumber, HttpSession session){
			
			String sessionHash = (String)session.getAttribute("hashNumber");
			String userHash = hash.getHash(userNumber);
			
			// 이렇게 hash 값으로 입력값과 인증번호를 비교한다
			boolean flag = userHash.equals(sessionHash);
			
			HashMap<String, String> ret = new HashMap<String, String>();
			ret.put("status", flag ? "OK" : "Fail");
			ret.put("message", flag ? "인증이 완료되었습니다" : "인증번호를 다시 확인해주세요");
			
			return ret;
		}
		
		// 비밀번호 이메일로 찾기
		@GetMapping("/mailtoFindPw/{email}/")
		public HashMap<String, String> mailtoFindPw(@PathVariable String email, HttpSession session) throws IOException{
			
			System.out.println("인증번호 받은 이메일 : " +  email);
			
			String authNumber = mailService.getAuthNumber();
			
			System.out.println("인증 번호 : " + authNumber);
			
			String hashNumber2 = hash.getHash(authNumber);
			System.out.println("인증 해시값 : " + hashNumber2);
			
			session.setAttribute("hashNumber2", hashNumber2);
			
			String account = null;
			
			String filePath = session.getServletContext().getRealPath("/WEB-INF/data/account.dat");
			File f = new File(filePath);
			
			if(f.exists() == false) {
				System.out.println("메일 전송에 필요한 계정 정보가 없습니다");
				return null;
			}
			
			Scanner sc = new Scanner(f);
			
			while(sc.hasNext()) {
				account = sc.nextLine();
			}
			sc.close();
			
			String result = mailService.pwChkMail(email, authNumber, account);
			result = hash.getHash(result);
			System.out.println("인증코드 : " + result);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("email", email);
			map.put("userpw", result);
			System.out.println(map);
			
			int row = memberService.updateFindPw(map);
			System.out.println("업데이트 결과 : " + row);
			
			HashMap<String, String> ret = new HashMap<String, String>();
			ret.put("status", row == 1 ? "OK" : "Fail");
			ret.put("message", row == 1 ? "이메일이 전송되었습니다" : "이메일을 다시 확인해주세요");
			System.out.println(ret);
			
			return ret;
		}
		
		@GetMapping("/mailtoFindId/{email}/")
		public HashMap<String, String> mailtoFindId(@PathVariable String email, HttpSession session) throws IOException{
			
			System.out.println("인증번호 받은 이메일 : " +  email);
			
			String account = null;
			
			String filePath = session.getServletContext().getRealPath("/WEB-INF/data/account.dat");
			File f = new File(filePath);
			
			if(f.exists() == false) {
				System.out.println("메일 전송에 필요한 계정 정보가 없습니다");
				return null;
			}
			
			Scanner sc = new Scanner(f);
			
			while(sc.hasNext()) {
				account = sc.nextLine();
			}
			sc.close();
			
			MemberDTO dto = memberService.getMemberFindId(email);
			String id = dto.getUserid();
					
			String result = mailService.idChkMail(email, id, account);
			System.out.println("아이디 : " + result);
			
			HashMap<String, String> ret = new HashMap<String, String>();
			ret.put("status", result != null ? "OK" : "Fail");
			ret.put("message", result != null ? "이메일이 전송되었습니다" : "이메일을 다시 확인해주세요");
			System.out.println(ret);
			
			return ret;
		}
		
		@GetMapping("/mailChk/{email}/")
		public HashMap<String, String> mailChk(@PathVariable String email){
			HashMap<String, String> map = new HashMap<String, String>();
			
			MemberDTO dto = memberService.mailChk(email);
			map.put("status", dto != null ? "OK" : "Fail");
			
			return map;
		}
		
		@GetMapping("/ajaxIdChk/{userid}")
		public HashMap<String, String> idCheck(@PathVariable String userid){
			HashMap<String, String> map = new HashMap<String, String>();
			
			MemberDTO dto = memberService.getMember(userid);
			
			map.put("msg", dto == null ? "사용가능한 ID입니다" : "이미 사용중인 ID입니다");
			map.put("color", dto == null ? "blue" : "red");
			map.put("focus", dto == null ? "userpw" : "userid");
			
			return map;
		}
		
		@GetMapping("/ajaxPhoneChk/{phone}")
		public HashMap<String, String> phoneCheck(@PathVariable String phone){
			HashMap<String, String> map = new HashMap<String, String>();
			
			MemberDTO dto = memberService.getMemberByPhone(phone);
			
			map.put("msg", dto == null ? "사용가능한 전화번호입니다" : "이미 사용중인 전화번호입니다");
			map.put("color", dto == null ? "blue" : "red");
			return map;
		}
		
		@GetMapping("/ajaxIdChk2/{userid}")
		public HashMap<String, String> idCheck2(@PathVariable String userid){
			HashMap<String, String> map = new HashMap<String, String>();
			
			AdminDTO dto = memberService.idChkAdmin(userid);
			
			map.put("msg", dto == null ? "사용가능한 ID입니다" : "이미 사용중인 ID입니다");
			map.put("color", dto == null ? "blue" : "red");
			map.put("focus", dto == null ? "userpw" : "userid");
			
			return map;
		}
		
		// 예외처리
		@ExceptionHandler(NullPointerException.class)
		public ModelAndView exHam(NullPointerException e) {
			ModelAndView mav = new ModelAndView("alert");
			mav.addObject("msg", "등록되지 않은 이메일입니다, 확인해주세요");
			System.out.println("등록되지 않은 이메일입니다, 확인해주세요");
			return mav;
		}
		
}
