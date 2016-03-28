package edu.epam.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.constants.EFactoryType;
import edu.epam.factory.AbstractDAOFactory;
import edu.epam.model.Direction;
import edu.epam.model.Group;

public class GroupService {
	public static List<Group> getListOfGroupForTeacher(Integer teacher_id)
			throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO()
				.getListOfGroupForTeacher(teacher_id);
	}

	public static List<Direction> getListOfDirectionsForTeacher(
			Integer teacher_id) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO()
				.getListOfDirectionsForTeacher(teacher_id);
	}

	public static boolean createGroup(Group group) throws SQLException {
		boolean res = AbstractDAOFactory.getFactory(EFactoryType.MySQL)
				.getGroupDAO().createGroup(group);
		return res;
	}

	
	public static Group getGroupById(Integer groupId) throws SQLException{
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().getGroupById(groupId);
	 }

	public static List<Group> getListOfGroupsForDirection(Integer directionId) throws SQLException {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().getListOfGroupsForDirection(directionId);
	}

	public static List<Group> getListOfActiveGroupsForDirection(Integer directionId) throws SQLException {
		return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().getListOfActiveGroupsForDirection(directionId);
	}

	public static boolean setGroupActive(Integer groupId, Boolean active) {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().setGroupActive(groupId,active);

	}

	public static boolean updateGroup(Group group) {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().updateGroup(group);

	}

	public static boolean updateGroupStaff(Integer groupId,ArrayList<Integer> teachersIds) {
		 return AbstractDAOFactory.getFactory(EFactoryType.MySQL).getGroupDAO().updateGroupStaff(groupId,teachersIds);
		
	}
}
