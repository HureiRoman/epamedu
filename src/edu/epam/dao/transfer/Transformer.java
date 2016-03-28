package edu.epam.dao.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.epam.annotations.Column;

public class Transformer {
	public static <B extends T, T> B getInstance(ResultSet resultSet,
			Class<B> instantiateClass, Class<? extends T>... classes)
			throws Exception {
		B instance = null;
		Map<String, Object> map = new HashMap<>();
		instance = instantiateClass.getConstructor().newInstance();
		if(resultSet.next()){
			for (Class<? extends T> tClass : classes) {
				Field[] fields = tClass.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Column.class)) {
						Column annotation = field.getAnnotation(Column.class);
						String columnName = annotation.value();
						Object obj = resultSet.getObject(columnName);
						map.put(field.getName().toLowerCase(), obj);

					}
				}
			}
			Method[] methods = instantiateClass.getMethods();
			for (Method method : methods) {
				if (method.getName().substring(0, 3).equals("set")) {
					String fieldName = method.getName().substring(3);
					Object value = map.get(fieldName.toLowerCase());
					if (null != value) {
						method.invoke(instance, value);
					}

				}
			}
			return instance;
		}
		return null;
	}

	@SafeVarargs
	public static <B extends T, T> List<B> getListOfInstances(
			ResultSet resultSet, Class<B> instantiateClass,
			Class<? extends T>... classes) throws Exception {

		List<B> list = new ArrayList<>();
		while (resultSet.next()) {
			resultSet.previous();
			B instance = getInstance(resultSet, instantiateClass, classes);
			list.add(instance);
		}
		return list;
	}
	
	
	
//	public static <B extends T, T> B universalGetInstance(ResultSet resultSet, Connection conn,
//			Class<B> instantiateClass, Class<? extends T>... classes)
//			throws Exception {
//		B instance = null;
//		conn.setAutoCommit(false);
//		try{
//			Map<String, Object> map = new HashMap<>();
//			instance = instantiateClass.getConstructor().newInstance();
//			if(resultSet.next()){
//				List<Field> list_of_fields = null;
//				//Find field name and get Object from result Set and put it to map
//				for (Class<? extends T> tClass : classes) {
//					if(list_of_fields == null && tClass.getAnnotation(InnerObject.class)!= null){
//						list_of_fields = new ArrayList<>();
//					}
//					Field[] fields = tClass.getDeclaredFields();
//					for (Field field : fields) {
//						if (field.isAnnotationPresent(Column.class)) {
//							Column annotation = field.getAnnotation(Column.class);
//							String columnName = annotation.value();
//							Object obj = resultSet.getObject(columnName);
//							map.put(field.getName().toLowerCase(), obj);
//							
//						}else if(field.isAnnotationPresent(InnerObject.class)){
//							list_of_fields.add(field);
//							
//						}
//					}
//				}
//				
//				//Initializing fields
//				Method[] methods = instantiateClass.getMethods();
//				for (Method method : methods) {
//					if (method.getName().substring(0, 3).equals("set")) {
//						String fieldName = method.getName().substring(3);
//						Object value = map.get(fieldName.toLowerCase());
//						if (null != value) {
//							method.invoke(instance, value);
//						}
//	
//					}
//				}
//				if(list_of_fields != null){
//					for(Field innerField : list_of_fields){
//						
//						InnerObject annotation = innerField.getAnnotation(InnerObject.class);
//						String tableName = annotation.tableName();
//						String thisObjectKey = annotation.thisObjectKey();
//						String wrapperObjectField = annotation.wrapperObjectField();
//						
//						Field key_field = instance.getClass().getField(wrapperObjectField);
//						if(key_field != null){
//							Object key_value = key_field.get(instance);
//							
//							final String query = "SELECT * FROM "+ tableName +" WHERE "+thisObjectKey + " = " + key_value;
//							System.out.println("QUERY = " + query);
//							try(PreparedStatement statement = conn.prepareStatement(query)){
//								ResultSet innerResultSet = statement.executeQuery();
////								Object innerInstance = innerField.getClass().getConstructor().newInstance();
//								List<Class<?>>listOfSuperClasses = new ArrayList<>();
//								Class<?> C = innerField.getType();
//								while (C != null) {
//								  listOfSuperClasses.add(C);
//								  System.out.println(C.getName());
//								  C = C.getSuperclass();
//								}
//								getInstance(innerResultSet, innerField.getType(), listOfSuperClasses);
//							
//							
//							}
//									
//						}else{
//							System.out.println("key value == null!!!");
//						}
//					}
//				}
//				
//				conn.commit();
//				conn.setAutoCommit(true);
//				
//				return instance;
//			}
//		}catch(Exception e ){
//			conn.rollback();
//			conn.setAutoCommit(true);
//			throw (Exception)(new Exception().getCause());
//		}
//		return null;
//	}

}
