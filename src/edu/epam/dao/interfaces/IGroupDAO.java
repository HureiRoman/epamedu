package edu.epam.dao.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.epam.model.Direction;
import edu.epam.model.Group;

public interface IGroupDAO {
	List<Group> getListOfGroupForTeacher(Integer teacher_id)
			throws SQLException;

	List<Direction> getListOfDirectionsForTeacher(Integer teacher_id)
			throws SQLException;

	boolean createGroup(Group group) throws SQLException;

	 Group getGroupById(Integer groupId) throws SQLException;

	List<Group> getListOfGroupsForDirection(Integer directionId) throws SQLException;

	public List<Group> getListOfActiveGroupsForDirection(Integer directionId) throws SQLException;

	boolean setGroupActive(Integer groupId, Boolean active);

	boolean updateGroup(Group group);

	boolean updateGroupStaff(Integer groupId, ArrayList<Integer> teachersIds);
}
