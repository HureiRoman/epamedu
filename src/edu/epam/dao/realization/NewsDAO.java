package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.INewsDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.NewsItem;

public class NewsDAO implements INewsDAO {
	private static NewsDAO instance;
	private static final String SQL_GET_ALL_NEWS = "SELECT * FROM news n  ";
	private static final String SQL_GET_ACTIVE_NEWS = "SELECT * FROM news n WHERE n.isArchived = 'false' ORDER BY message_date DESC";
	private static final String SQL_INSERT_NEWSITEM_TO_DB = "INSERT INTO news (title,content,description,message_date) VALUES (?,?,?,?);  ";
	private static final String SQL_SET_NEWS_ITEM_ARCHIVED = "UPDATE news n SET n.isArchived= ? WHERE n.id=? ; ";
	private static final String SQL_GET_NEWS_ITEM_BY_ID = "SELECT * FROM news n  WHERE n.id = ?";
	private static final String SQL_UPDATE_NEWSITEM_BY_ID = "UPDATE news n SET  n.title=?,n.content=?,n.description=?,n.message_date=?  WHERE n.id = ?;";
	private static final String SQL_DELETE_NEWS = "DELETE FROM news WHERE id=?"; 
	
	@Override
	public List<NewsItem> getAllNews() throws SQLException {

		List<NewsItem> resultList = null;
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();

		PreparedStatement pStatement = connect
				.prepareStatement(SQL_GET_ALL_NEWS);
		ResultSet resultSet = pStatement.executeQuery();
		try {
			resultList = Transformer.getListOfInstances(resultSet,NewsItem.class, NewsItem.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			   cm.freeConnection(connect);
		 }

		ConnectionManager.getInstance().freeConnection(connect);
		return resultList;
	}

	private NewsDAO() {

	}

	public static NewsDAO getInstance() {
		if (instance == null) {
			instance = new NewsDAO();
		}
		return instance;
	}

	@Override
	public Integer addNewItemToDB(NewsItem item) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		PreparedStatement pStatement;
		int updated = 0;
		Integer newsItemId = null;
		try {
			pStatement = connect.prepareStatement(SQL_INSERT_NEWSITEM_TO_DB,PreparedStatement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, item.getTitle());
			pStatement.setString(2,item.getContent());
			pStatement.setString(3, item.getDescription());
			pStatement.setTimestamp(4,  new Timestamp(Calendar.getInstance().getTimeInMillis()));
			updated = pStatement.executeUpdate();
			if(updated>0){
				ResultSet generatedKeys = pStatement.getGeneratedKeys();
				if(generatedKeys.next()){
					newsItemId  = generatedKeys.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return newsItemId;
	}

	@Override
	public List<NewsItem> getActiveNews() throws SQLException {

		List<NewsItem> resultList = null;
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();

		PreparedStatement pStatement = connect
				.prepareStatement(SQL_GET_ACTIVE_NEWS);
		ResultSet resultSet = pStatement.executeQuery();
		try {
			resultList = Transformer.getListOfInstances(resultSet,NewsItem.class, NewsItem.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			   cm.freeConnection(connect);
		 }
		return resultList;
	}
	@Override
	public boolean setNewsItemArchived(Integer newsItemId, Boolean needArchive) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		int rowsUpdated = 0;
		PreparedStatement pStatement = null;
		try {
			pStatement = connect
					.prepareStatement(SQL_SET_NEWS_ITEM_ARCHIVED);
			pStatement.setString(1, needArchive.toString());
			pStatement.setInt(2, newsItemId);
			rowsUpdated = pStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return rowsUpdated>0;
	}
	@Override
	public NewsItem getNewsItemById(Integer newsItemId) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		PreparedStatement pStatement = null;
		 NewsItem resultNewsItem =null;
		try {
			pStatement = connect
					.prepareStatement(SQL_GET_NEWS_ITEM_BY_ID);
			pStatement.setInt(1, newsItemId);
			ResultSet resultSetOfNewsItem = pStatement.executeQuery();
			resultNewsItem = Transformer.getInstance(resultSetOfNewsItem, NewsItem.class,NewsItem.class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return resultNewsItem;
	}

	@Override
	public boolean updateNewsItemById(NewsItem item, Integer id) {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		PreparedStatement pStatement;
		int updated = 0;
		try {
			pStatement = connect.prepareStatement(SQL_UPDATE_NEWSITEM_BY_ID);
			pStatement.setString(1, item.getTitle());
			pStatement.setString(2,item.getContent());
			pStatement.setString(3, item.getDescription());
			pStatement.setTimestamp(4,  new Timestamp(Calendar.getInstance().getTimeInMillis()));
			pStatement.setInt(5,id);
			
			updated = pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return updated>0;
	}
	
	@Override
	public boolean deleteNewsItem(Integer newsItemId) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection connect = cm.getConnection();
		int rowsUpdated = 0;
		PreparedStatement pStatement = null;
		try {
			pStatement = connect
					.prepareStatement(SQL_DELETE_NEWS);
			pStatement.setInt(1, newsItemId);
			rowsUpdated = pStatement.executeUpdate();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			   cm.freeConnection(connect);
		 }
		return rowsUpdated>0;
	}


}
