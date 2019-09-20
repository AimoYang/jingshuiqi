package com.jingshuiqi.service;

import java.util.List;

import com.jingshuiqi.bean.Category;
import com.jingshuiqi.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingshuiqi.bean.JsonResult;
import com.jingshuiqi.dao.CategoryMapper;

@Service
public class SortService {
	
	@Autowired
	private CategoryMapper categoryMapper;

	public JsonResult findOneSortInfo() {
		List<Category> oneSort = categoryMapper.findOneSortInfo();
		return ResultUtil.success(oneSort);
	}

}
