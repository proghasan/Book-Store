package com.nextprogrammers.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextprogrammers.model.Book;

public class BookDAO {
	private String jdbcUrl;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	
	public BookDAO(String jdbcUrl, String jdbcUsername, String jdbcPassword)
	{
		this.jdbcUrl = jdbcUrl;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	protected void connect() throws SQLException
	{
		if(jdbcConnection == null || jdbcConnection.isClosed() )
		{
			try {
				Class.forName("com.mysql.jdbc.Drive");
			}catch(ClassNotFoundException e) {
				throw new SQLException(e);
			}
			
			jdbcConnection = DriverManager.getConnection(jdbcUrl,jdbcUsername,jdbcPassword);
		}
	}
	
	protected void disconnect() throws SQLException
	{
		if(jdbcConnection != null || !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public boolean InsertBook(Book book) throws SQLException
	{
		String sql = "inbsert into book(title, author, price) values(?,?,?)";
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, book.getTitle());
		statement.setString(2, book.getAuthor());
		statement.setFloat(3, book.getPrice());
		
		boolean rowInsert = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowInsert;
	}
	
	public List<Book> ListAllBooks() throws SQLException
	{
		List<Book> listBook = new ArrayList<>();
		String sql = "select * from book";
		connect();
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		while(resultSet.next()) {
			int id = resultSet.getInt("book_id");
			String title = resultSet.getString("title");
			String author = resultSet.getString("author");
			float price = resultSet.getFloat("price");
			
			Book book = new Book (id, title, author, price);
			listBook.add(book);
			disconnect();
		}
		return listBook;
		
		
	}
}
