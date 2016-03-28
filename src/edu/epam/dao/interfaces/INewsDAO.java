package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.NewsItem;

public interface INewsDAO {
	
	List<NewsItem> getAllNews() throws SQLException;
	List<NewsItem> getActiveNews() throws SQLException;
	Integer addNewItemToDB(NewsItem item);
	boolean setNewsItemArchived(Integer newsItemId, Boolean needArchive) throws SQLException;
	NewsItem getNewsItemById(Integer newsItemId);
	boolean updateNewsItemById(NewsItem newsItem, Integer id);
	boolean deleteNewsItem(Integer newsItemId) throws SQLException;
	
}
