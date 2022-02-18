package com.itbank.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itbank.model.NoticeDAO;
import com.itbank.model.NoticeDTO;
import com.itbank.model.QnaBoardDAO;
import com.itbank.model.QnaBoardDTO;
import com.itbank.model.ReplyDAO;
import com.itbank.model.ReplyDTO;

@Service
public class BoardService {

	@Autowired private NoticeDAO dao;
	@Autowired private QnaBoardDAO qdao;
	@Autowired private ReplyDAO rdao;
	
	
	public List<HashMap<String, Object>> getNotice(int offset) {
		return dao.selectNotice(offset);
	}

	public NoticeDTO getNews(int seq) {
		return dao.selectNews(seq);
	}

	public int cntUpdate(HashMap<String, Object> map) {
		return dao.cntUpdate(map);
	}

	public int qnaInsert(QnaBoardDTO dto) {
		return qdao.qnaInsert(dto);
	}

	public List<QnaBoardDTO> qnaList() {
		return qdao.qnaList();
	}

	// 미답변 QnA 가져오기
	public List<QnaBoardDTO> qnaCheck(int offset) {
		return qdao.qnaCheck(offset);
	}

	public int replyInsert(ReplyDTO dto) {
		return rdao.insert(dto);
	}

	public int qnaResult(int seq) {
		return qdao.resultUpdate(seq);
	}

	public int qnaCount(String result) {
		return qdao.qnaCount(result);
	}

	public List<HashMap<String, Object>> qnaList2(HashMap<String, Object> map) {
		return qdao.qnaList2(map);
	}

	public ReplyDTO getReply(int seq) {
		return rdao.getReply(seq);
	}

	public QnaBoardDTO getQna(int seq) {
		return qdao.getQna(seq);
	}

	public int qnaCheckCnt() {	
		return qdao.qnaCnt();
	}

	public int userCount(String writer) {
		return qdao.userCount(writer);
	}
	
	public List<QnaBoardDTO> userQnaList(HashMap<String, Object> map) {
		return qdao.userQnaList(map);
	}

	

}
