package com.kgc.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kgc.dao.BaseDao;
import com.kgc.dao.NewsDao;
import com.kgc.pojo.News;
import com.kgc.pojo.NewsCategory;

public class NewsDaoImpl extends BaseDao implements NewsDao {
	//��ȡ����������
	public int getTotalCount(){
		int totalCount=0;
		String sql="select count(1) from news_detail";
		Object[] params={};
		ResultSet rs=this.executeSQL(sql, params);
		try {
			while(rs.next()){
				totalCount=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
		return totalCount;
	}
	//��ҳ��ȡ������Ϣ
	public List<News> getPageNewsList(int pageNo,int pageSize){
		List<News> newsList=new ArrayList<News>();
		//select * from tableName where ���� limit ����ǰҳ��-1��*ҳ��������ҳ������
		//limit�÷�����һ������ָ����Ҫ��ʼ�ĵط����ڶ�������ָÿҳ��ʾ���������ݣ�ע�⣺��һҳҪ��0��ʾ��
		String sql="SELECT id,title,author,createDate FROM news_detail ORDER BY createDate DESC LIMIT ?,?";
		pageNo = (pageNo - 1) * pageSize;
		Object[] params={pageNo,pageSize};
		ResultSet rs=this.executeSQL(sql, params);
		try {
			while(rs.next()){
				int id=rs.getInt("id");
				String title=rs.getString("title");
				String author=rs.getString("author");
				Date date=rs.getDate("createDate");
				News newInfo=new News();
				newInfo.setId(id);
				newInfo.setTitle(title);
				newInfo.setAuthor(author);
				newInfo.setCreateDate(new Timestamp(date.getTime()));
			
				newsList.add(newInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
		return newsList;
	}
	
	public News getNewsById(int id) {
		News news = null;
		try {
			String sql = "select * from news_detail where id=?"; 
			Object[] params = {id};
			ResultSet rs = this.executeSQL(sql, params);
			if(rs.next()){
				int categoryId = rs.getInt("categoryId");
				String title = rs.getString("title");
				String author = rs.getString("author");
				String summary = rs.getString("summary");
				String content = rs.getString("content");
				String picPath = rs.getString("picPath");
				Timestamp createDate = rs.getTimestamp("createDate");
				news = new News();
				news.setId(id);
				news.setAuthor(author);
				news.setCategoryId(categoryId);
				news.setContent(content);
				news.setPicPath(picPath);
				news.setSummary(summary);
				news.setTitle(title);
				news.setCreateDate(createDate);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
		return news;
	}
	//��ѯ�ض�������������ŵ���Ŀ
	public int getCountByCategory(NewsCategory category){
		int count = 0;
		String sql ="select count(1) from news_detail where categoryId=?";
		Object[] params = {category.getId()};
		if(this.getConnection()){
			try{
				ResultSet rs  = this.executeSQL(sql, params);
				while(rs.next()){
					count = rs.getInt(1);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}finally{
				this.closeResource();
			}
		}
		return count;
	}
	
	//��������Դ��ȡ������Ϣ
	public void getNewsListByDS() {
		try {
			// 3 ��ȡStatement����ִ��sql���
			String sql = "select * from news_detail";
			Object[] params = {};
			rs = this.executeSQL2(sql, params);
			// 4 ����ִ�н����ResultSet
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				String content = rs.getString("content");
				String author = rs.getString("author");
				Timestamp createDate = rs.getTimestamp("createDate");
				System.out.println(id + "\t" + title + "\t" + summary + "\t"
						+ content + "\t" + author + "\t" + createDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
	}
	
	// ��ѯ������Ϣ
	public  List<News> getNewsList() {
		List<News> list = new ArrayList<News>();
		try {
			// 3 ��ȡStatement����ִ��sql���
			String sql = "select * from news_detail";
			Object[] params = {};
			rs = this.executeSQL(sql, params);
			// 4 ����ִ�н����ResultSet
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String summary = rs.getString("summary");
				String content = rs.getString("content");
				String author = rs.getString("author");
				Timestamp createDate = rs.getTimestamp("createDate");
//				System.out.println(id + "\t" + title + "\t" + summary + "\t"
//						+ content + "\t" + author + "\t" + createDate);
				
				News news = new News();
				news.setId(id);
				news.setTitle(title);
				news.setSummary(summary);
				news.setContent(content);
				news.setAuthor(author);
				news.setCreateDate(createDate);
				
				list.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return list;
	}

	// ����������Ϣ
	public boolean add(News news) {
		boolean flag = false;
		try {
			String sql = "insert into news_detail(categoryId,title,summary,content,createDate,author,picpath) values(?,?,?,?,?,?,?)";
			Object[] params = {news.getCategoryId(),news.getTitle(),news.getSummary(),news.getContent(),news.getCreateDate(),news.getAuthor(),news.getPicPath()};
			int i = this.executeUpdate(sql,params);
			// 4 ����ִ�н��
			if (i > 0) {
				System.out.println("�������ųɹ���");
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
		return flag;
	}

	// �޸����ű���
	public void update(News news) {
		try {
			String sql = "update news_detail set title=? where id=?";
			Object[] params = {news.getTitle(),news.getId()};
			int i = this.executeUpdate(sql,params);
			if (i > 0) {
				System.out.println("�޸����ű���ɹ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}

	}

	// ɾ��������Ϣ
	public void delete(News news) {
		try {
			String sql = "delete from news_detail where id=?";
			Object[] params ={news.getId()};
			int i = this.executeUpdate(sql,params);
			if (i > 0) {
				System.out.println("ɾ�����ųɹ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����ض������������Ϣ
	public void getNewsByTitle(News news) {
		try {
			String sql = "select id,title from news_detail where title like ?";
			Object[] params = {"%"+news.getTitle()+"%"};
			rs = this.executeSQL(sql, params);
			while (rs.next()) {
				int id = rs.getInt("id"); // rs.getInt(1);
				String newsTitle = rs.getString("title");

				System.out.println(id + "\t" + newsTitle);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResource();
		}
	}

	public static void main(String[] args) {
		NewsDao newsDao = new NewsDaoImpl();
		/*News news = new News();
		news.setId(5);
		news.setCategoryId(2);
		news.setTitle("test");
		news.setSummary("test");
		news.setContent("test");
		news.setAuthor("admin");
		news.setCreateDate(new Date());
		newsDao.add(news);*/
		
		/*News news = new News();
		news.setId(5);
		news.setTitle("testnew");
		newsDao.update(news);*/
		
		/*News news = new News();
		news.setTitle("�ι���");
		newsDao.getNewsByTitle(news);*/
		
		News news = new News();
		news.setId(5);
		newsDao.delete(news); 
		newsDao.getNewsList();

	}

}
