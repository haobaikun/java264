package cn.itcast.dao;

import java.util.List;

import cn.itcast.pojo.Book;

public interface BookDao {

	public List<Book> findBookList();
}
