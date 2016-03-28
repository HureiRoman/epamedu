package edu.epam.service;

import java.sql.SQLException;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Advertisement;

public class AdvertisementService {

	public static List<Advertisement> getListOfAdvertisement()
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().getListOfAdvertisement();

	}
	
	public static List<Advertisement> getListOfAdvertisementForGroup(Integer idGroup)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().getListOfAdvertisementsForGroup(idGroup);

	}
	
	public static List<Advertisement> getListOfAdvertisementByTeacher(Integer idTeacher)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().getListOfAdvertisementsByTeacher(idTeacher);

	}

	public static Advertisement getAdvertisementById(Integer id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().gettAdvertisementById(id);

	}

	public static Integer createNewAdvertisement(Advertisement advertisement)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().createAdvertisement(advertisement);
	}

	public static boolean updateAdvertisement(Advertisement advertisement)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().updateAdvertisement(advertisement);

	}

	public static boolean setAdvertisementArchived(Integer id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getAdvertisementDAO().deleteAdvertisement(id);

	}

	
	
	

}
