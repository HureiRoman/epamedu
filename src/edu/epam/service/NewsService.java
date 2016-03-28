package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.NewsItem;

public class NewsService {

	public static List<NewsItem> getAllNews() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.getAllNews();
	}
	
	public static List<NewsItem> getActiveNews() throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.getActiveNews();
	}
	
	public static Integer addNewsItemToDB(NewsItem newsItem){
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.addNewItemToDB(newsItem);
	}

	public static boolean setNewsItemArchived(Integer newsItemId,
			Boolean needArchive) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.setNewsItemArchived(newsItemId,needArchive);
		
	}

	public static NewsItem getNewsItemById(Integer newsItemId) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.getNewsItemById(newsItemId);
		
	}

	public static boolean updateNewsItemById(NewsItem newsItem, Integer id) {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO()
				.updateNewsItemById(newsItem,id);
	}
	
	public static boolean deleteNewsItem(Integer newsItemId) throws SQLException{
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getNewsDAO().deleteNewsItem(newsItemId);
	}
}
