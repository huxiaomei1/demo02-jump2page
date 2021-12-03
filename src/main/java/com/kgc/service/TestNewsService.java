package com.kgc.service;

import java.util.List;

import com.kgc.pojo.News;
import com.kgc.service.impl.NewsServiceImpl;

public class TestNewsService {
	public static void main(String[] args) {
		NewsService newsService = new NewsServiceImpl();
		List<News> list = newsService.getPageNewsList(2, 3);
		for(News news : list ){
			System.out.println(news.getId()+"\t"+news.getAuthor()+"\t"+news.getTitle());
		}
	}
}
