package com.itbank.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itbank.model.SearchDAO;

@Service
public class SearchService {

	@Autowired private SearchDAO dao;

	public List<HashMap<String, String>> searchList(HashMap<String, String> map) {
		return dao.search(map);
	}

}
