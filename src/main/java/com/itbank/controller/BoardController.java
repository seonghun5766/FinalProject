package com.itbank.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itbank.component.Paging;
import com.itbank.member.MemberDTO;
import com.itbank.model.NoticeDTO;
import com.itbank.model.QnaBoardDTO;
import com.itbank.model.ReplyDTO;
import com.itbank.service.BoardService;
import com.itbank.service.FileService;
import com.itbank.service.NoticeService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired private BoardService bs;
	@Autowired private FileService fs;
	@Autowired private NoticeService ns;
	@Autowired private Paging paging;
	
	
	@GetMapping("/news")
	public ModelAndView news(int page, @RequestParam(required = false) String search) {
		ModelAndView mav = new ModelAndView("board/news");
		
		if(page == 0) {
			page = 1;
		}
		int offset = (page-1) * 5;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("offset", offset);
		
		
		List<HashMap<String, Object>> list = ns.searchList(map);
		List<HashMap<String, Object>> topList = ns.topList();
		
		int total = ns.searchTotal(search);
		
		int pageCount = (total / 5);
		pageCount = total % 5 == 0 ? pageCount : pageCount + 1 ;

		int section = paging.section(page);		
		int begin = paging.begin(section);
		int end = paging.end(pageCount);
		boolean prev = paging.prev(section);
		boolean next = paging.next(pageCount, end);
		

		
		System.out.println("pageCount : " + pageCount);
		System.out.println("section : " + section);
		System.out.println("begin : " + begin);
		System.out.println("end : " + end);
		System.out.println("total :" + total);
		System.out.println("prev : " + prev);
		System.out.println("next : " + next);
		System.out.println(list);
		
		mav.addObject("pageCount", pageCount);
		mav.addObject("section", section);
		mav.addObject("begin", begin);
		mav.addObject("end", end);
		mav.addObject("total", total);
		mav.addObject("prev", prev);
		mav.addObject("next", next);
		mav.addObject("list", list);
		mav.addObject("topList", topList);
		return mav;
	}
	
	@PostMapping("/news")
	public ModelAndView news(@RequestParam String search, @RequestParam int page) {
		ModelAndView mav = new ModelAndView();
		
		int total = ns.searchTotal(search);
		
		if(page == 0) {
			page = 1;
		}
		
		int offset = (page-1) / 5;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("offset", offset);
		
		List<HashMap<String, Object>> list = ns.searchList(map);
		
		int pageCount = (total / 5);
		pageCount = total % 5 == 0 ? pageCount : pageCount + 1 ;

		int section = paging.section(page);		
		int begin = paging.begin(section);
		int end = paging.end(pageCount);
		boolean prev = paging.prev(section);
		boolean next = paging.next(pageCount, end);
		
		mav.addObject("list", list);
		mav.addObject("pageCount", pageCount);
		mav.addObject("section", section);
		mav.addObject("begin", begin);
		mav.addObject("end", end);
		mav.addObject("total", total);
		mav.addObject("prev", prev);
		mav.addObject("next", next);
		return mav;
	}
	
	@GetMapping("/newsDetail/{seq}")
	public ModelAndView newsDetail(@PathVariable int seq) {
		ModelAndView mav = new ModelAndView("board/newsDetail");
		
		NoticeDTO dto = bs.getNews(seq);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("seq", seq);
		map.put("cnt", dto.getCnt());
		
		NoticeDTO dto1 = bs.getNews(seq);
		
		mav.addObject("dto", dto1);
		
		return mav;
	}
	
	
	@GetMapping("/newsWrite")
	public ModelAndView newsWrite() {
		ModelAndView mav = new ModelAndView();
		
		return mav;
	}
	
	@PostMapping("/newsWrite")
	public ModelAndView newsWrite(NoticeDTO dto) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		int row = fs.upload(dto);
		
		if(row == 1) {
			mav.setViewName("alert");
			mav.addObject("msg", "작성 성공!!");
			mav.addObject("url", "board/news?page=1");
		}
		else {
			mav.setViewName("alert");
			mav.addObject("msg", "작성 실패!!");
		}
		
		return mav;
	}
	
	@GetMapping("/question")
	public ModelAndView question(int page, @RequestParam(required = false) String result) {
		ModelAndView mav = new ModelAndView();
		
		if(page == 0) {
			page = 1;
		}

		int total = bs.qnaCount(result);
		
		int pageCount = (total / 10);
		pageCount = total % 10 == 0 ? pageCount : pageCount + 1 ;

		int offset = (page-1) * 10;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("result", result);
		
		List<HashMap<String, Object>> qlist = bs.qnaList2(map);
		
		int section = paging.section(page);		
		int begin = paging.begin(section);
		int end = paging.end(pageCount);
		boolean prev = paging.prev(section);
		boolean next = paging.next(pageCount, end);
		
		System.out.println("page : " + page);
		System.out.println("total : " + total);
		System.out.println("pageCount : " + pageCount);
		System.out.println("offset : " + offset);
		System.out.println("section : " + section);
		System.out.println("begin : " + begin);
		System.out.println("end : " + end);
		System.out.println("prev : " + prev);
		System.out.println("next : " + next);
		
		
	
		
		mav.addObject("list", qlist);
		mav.addObject("section", section);
		mav.addObject("begin", begin);
		mav.addObject("end", end);
		mav.addObject("prev", prev);
		mav.addObject("next", next);
		return mav;
	}
	
	@PostMapping("/question")
	public ModelAndView postQuestion(int page, @RequestParam String writer) {
		ModelAndView mav = new ModelAndView();
		
		if(page == 0) {
			page = 1;
		}
		int total = bs.userCount(writer);
		

		int pageCount = (total / 10);
		pageCount = total % 10 == 0 ? pageCount : pageCount + 1 ;

		int offset = (page-1) * 10;
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("writer", writer);
		
		List<QnaBoardDTO> userQnaList = bs.userQnaList(map);
		
		int section = paging.section(page);		
		int begin = paging.begin(section);
		int end = paging.end(pageCount);
		boolean prev = paging.prev(section);
		boolean next = paging.next(pageCount, end);
		
		System.out.println("page : " + page);
		System.out.println("total : " + total);
		System.out.println("pageCount : " + pageCount);
		System.out.println("offset : " + offset);
		System.out.println("section : " + section);
		System.out.println("begin : " + begin);
		System.out.println("end : " + end);
		System.out.println("prev : " + prev);
		System.out.println("next : " + next);
		
		mav.addObject("list", userQnaList);
		mav.addObject("section", section);
		mav.addObject("begin", begin);
		mav.addObject("end", end);
		mav.addObject("prev", prev);
		mav.addObject("next", next);
		return mav;
		
		
	}
	
	@GetMapping("/questionDetail/{seq}")
	public ModelAndView questionDetail(@PathVariable int seq) {
		ModelAndView mav = new ModelAndView("board/questionDetail");
		QnaBoardDTO que = bs.getQna(seq);
		
		ReplyDTO rep = bs.getReply(seq);
		
		mav.addObject("que", que);
		
		if(rep != null) {
			mav.addObject("rep", rep);	
		}
		else {
			mav.addObject("rep", "댓글 작성 전 입니다");
		}
		
		
		
		return mav;
	}
	
	
	// qnaWrite get
	@GetMapping("/qnaWrite")
	public ModelAndView qnaWrite(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		MemberDTO dto =  (MemberDTO)session.getAttribute("login");
		mav.addObject("login", dto);
		
		return mav;
	}
	
	@PostMapping("/qnaWrite")
	public ModelAndView qnaWrite(HttpServletRequest request, QnaBoardDTO dto) {
		ModelAndView mav = new ModelAndView();
		
		String ipAdd = request.getRemoteAddr();
		dto.setIpAddress(ipAdd);
		
		int row = bs.qnaInsert(dto);
		
		System.out.println(row);
		
		if(row == 1) {
			mav.setViewName("alert");
			mav.addObject("msg", "작성완료");
			mav.addObject("url", "board/question?page=1");
		}
		else {
			mav.setViewName("alert");
			mav.addObject("msg", "작성실패");
			mav.addObject("url", "qnaWrite");
		}
		
		return mav;
	}
	
	// news 삭제
	@GetMapping("/newsDelete/{seq}")
	public ModelAndView newsDelete(@PathVariable int seq) {
		ModelAndView mav = new ModelAndView("alert");
		
		int row = ns.deleteNews(seq);
		System.out.println(row);
		
		if(row == 1) {
			mav.addObject("msg", seq + "번 게시글이 삭제되었습니다");
			mav.addObject("url", "board/news?page=1");
		}
		else {
			mav.addObject("msg", seq + "번  게시글 삭제에 실패하였습니다");
		}
		return mav;
	}
	
	// news 수정
	
	@GetMapping("/newsModify/{seq}")
	public ModelAndView newsModify(@PathVariable int seq) {
		ModelAndView mav = new ModelAndView("board/newsModify");
			
		NoticeDTO dto = bs.getNews(seq);
			
		mav.addObject("dto", dto);
			
		return mav;
	}
		
	@PostMapping("/newsModify/{seq}")
	public ModelAndView newsModify(NoticeDTO dto)  throws Exception {
		ModelAndView mav = new ModelAndView();
			
		int row = fs.uploadModify(dto);
			
		if(row == 1) {
			mav.setViewName("alert");
			mav.addObject("msg", "게시글 수정을 성공하였습니다");
		}
			
		else {
			mav.setViewName("alert");
			mav.addObject("msg", "게시글 수정에 실패하였습니다");
		}
		return mav;
	}
	
	
	
}