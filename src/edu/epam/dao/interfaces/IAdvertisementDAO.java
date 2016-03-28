package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import edu.epam.model.Advertisement;

public interface IAdvertisementDAO {
	Advertisement getTheNewestAdvertisement() throws SQLException;

	List<Advertisement> getListOfAdvertisement() throws SQLException;

	Integer createAdvertisement(Advertisement advertisement)
			throws SQLException;

	boolean updateAdvertisement(Advertisement advertisement)
			throws SQLException;

	boolean deleteAdvertisement(int id) throws SQLException;

	Advertisement gettAdvertisementById(Integer id) throws SQLException;


	List<Advertisement> getListOfAdvertisementsForGroup(int idGroup)
			throws SQLException;
	
	List<Advertisement> getListOfAdvertisementsByTeacher(int idTeacher)
			throws SQLException;

}
