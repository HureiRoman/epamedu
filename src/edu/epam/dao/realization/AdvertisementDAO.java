package edu.epam.dao.realization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import edu.epam.connection.ConnectionManager;
import edu.epam.dao.interfaces.IAdvertisementDAO;
import edu.epam.dao.transfer.Transformer;
import edu.epam.model.Advertisement;
import edu.epam.role.CommonUser;
import edu.epam.role.Teacher;

public class AdvertisementDAO implements IAdvertisementDAO {
	private final String SQL_GET_THE_NEWEST_ADVERTISEMENT = "SELECT * FROM teacher_advertisement WHERE advertisement_date ="
			+ " (SELECT MAX(advertisement_date) FROM teacher_advertisement ) AND is_archived='false';";
	private final String SQL_GET_ADVERTISEMENTS = "SELECT * FROM teacher_advertisement WHERE is_archived='false'";
	private final String SQL_GET_ADVERTISEMENTS_FOR_GROUP = "SELECT * FROM teacher_advertisement WHERE is_archived='false' AND id_group=? ORDER BY advertisement_date DESC ";
	private final String SQL_GET_ADVERTISEMENTS_BY_TEACHER = "SELECT * FROM teacher_advertisement WHERE is_archived='false' AND id_teacher=? ORDER BY advertisement_date desc";
	private final String SQL_GET_ADVERTISEMENT_BY_ID = "SELECT * FROM teacher_advertisement WHERE is_archived='false' AND id=?";
	private final String SQL_CREATE_ADVERTISEMENT = "INSERT INTO teacher_advertisement(title, content, id_group, id_teacher, advertisement_date) VALUES(?, ?, ?, ?, ?) ";
	private final String SQL_UPDATE_ADVERTISEMENT = "UPDATE teacher_advertisement SET title=?, content=? WHERE id=? ";
	private final String SQL_DELETE_ADVERTISEMENT = "UPDATE teacher_advertisement SET is_archived='true' WHERE id=? ";
	private final String SQL_GET_ADVERTISEMENT_OWNER = "SELECT * FROM users WHERE id=?";
	
	private static AdvertisementDAO instance;

	public static AdvertisementDAO getInstance() {

		if (instance == null) {
			instance = new AdvertisementDAO();
		}
		return instance;
	}

	@Override
	public Advertisement getTheNewestAdvertisement() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Advertisement advertisement;
		try (Statement st = conn.createStatement()) {

			ResultSet rs = st.executeQuery(SQL_GET_THE_NEWEST_ADVERTISEMENT);
			advertisement = Transformer.getInstance(rs, Advertisement.class,
					Advertisement.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

		return advertisement;
	}

	@Override
	public Advertisement gettAdvertisementById(Integer id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Advertisement advertisement;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_GET_ADVERTISEMENT_BY_ID);) {
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();
			advertisement = Transformer.getInstance(rs, Advertisement.class,
					Advertisement.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}

		return advertisement;
	}

	@Override
	public List<Advertisement> getListOfAdvertisement() throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Advertisement> listOfAdvertisement;

		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(SQL_GET_ADVERTISEMENTS);
			listOfAdvertisement = Transformer.getListOfInstances(rs,
					Advertisement.class, Advertisement.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return listOfAdvertisement;
	}

	@Override
	public Integer createAdvertisement(Advertisement advertisement)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer key = null;
		try (PreparedStatement st = conn
				.prepareStatement(SQL_CREATE_ADVERTISEMENT);) {
			st.setString(1, advertisement.getTitle());
			st.setString(2, advertisement.getContent());
			st.setInt(3, advertisement.getIdGroup());
			st.setInt(4, advertisement.getIdTeacher());

			st.setTimestamp(5, new Timestamp(Calendar.getInstance()
					.getTimeInMillis()));

			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}
		return key;
	}

	@Override
	public boolean updateAdvertisement(Advertisement advertisement)
			throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer rowsAffected = null;

		try (PreparedStatement st = conn
				.prepareStatement(SQL_UPDATE_ADVERTISEMENT);) {
			st.setString(1, advertisement.getTitle());
			st.setString(2, advertisement.getContent());
			st.setInt(3, advertisement.getId());

			rowsAffected = st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return rowsAffected > 0;
	}

	@Override
	public boolean deleteAdvertisement(int id) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		Integer rowsAffected = null;

		try (PreparedStatement st = conn
				.prepareStatement(SQL_DELETE_ADVERTISEMENT);) {
			st.setInt(1, id);
			rowsAffected = st.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cm.freeConnection(conn);
		}

		return rowsAffected > 0;

	}

	@Override
	public List<Advertisement> getListOfAdvertisementsForGroup(int idGroup) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Advertisement> listOfAdvertisementsForGroup;

		try (PreparedStatement st = conn.prepareStatement(SQL_GET_ADVERTISEMENTS_FOR_GROUP);
				PreparedStatement getTeacherOwner = conn.prepareStatement(SQL_GET_ADVERTISEMENT_OWNER)) {
			conn.setAutoCommit(false);
			st.setInt(1, idGroup);
			ResultSet rs = st.executeQuery();
			listOfAdvertisementsForGroup = Transformer.getListOfInstances(rs,
					Advertisement.class, Advertisement.class);
			if(listOfAdvertisementsForGroup != null){
				for(Advertisement advert : listOfAdvertisementsForGroup){
					getTeacherOwner.setInt(1, advert.getIdTeacher());
					ResultSet teacherResult = getTeacherOwner.executeQuery();
					Teacher advOwner = Transformer.getInstance(teacherResult, Teacher.class, CommonUser.class);
					advert.setAdvertisementOwner(advOwner);
				}
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			conn.rollback();
			conn.setAutoCommit(true);
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return listOfAdvertisementsForGroup;
	}
	
	@Override
	public List<Advertisement> getListOfAdvertisementsByTeacher(int idTeacher) throws SQLException {
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection conn = cm.getConnection();
		List<Advertisement> listOfAdvertisementsByTeacher;

		try (PreparedStatement st = conn.prepareStatement(SQL_GET_ADVERTISEMENTS_BY_TEACHER)) {
			st.setInt(1, idTeacher);
			ResultSet rs = st.executeQuery();
			listOfAdvertisementsByTeacher = Transformer.getListOfInstances(rs,
					Advertisement.class, Advertisement.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			cm.freeConnection(conn);
		}
		return listOfAdvertisementsByTeacher;
	}



}
