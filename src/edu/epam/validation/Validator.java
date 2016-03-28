package edu.epam.validation;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.epam.annotations.RegexCheck;

public class Validator {
	
	
	public static <T> boolean isRegexValid(T object) throws IllegalArgumentException, IllegalAccessException{
		Class objClass = object.getClass();
		while(true) {
			Field[] fields = objClass.getDeclaredFields();
			for (Field field : fields) {
				//System.out.println(field.getName());
				if (field.isAnnotationPresent(RegexCheck.class)) {
					RegexCheck annotation = field.getAnnotation(RegexCheck.class);
					Pattern p = Pattern.compile(annotation.regex());
					field.setAccessible(true);
					Matcher m = p.matcher(field.get(object).toString());

					if (m.matches() == false) {
						return false;
					}
				}
			}
			objClass = objClass.getSuperclass();
			if(objClass == null) {
				break;
			}
		}
		return true;
	}

	
}

/* fname, lname, pname: "Карпенко-Карий", "Д'Артаньян"
 * "^([А-ЯA-ZЇІЄ])(([а-яa-zїіє]{0,16})([-–'])?([A-ZА-Яа-яa-zїіє]{1,16}))+$"
 * password (пароль від 4 до 30 будь-яких символів крім <>)
 * "^.[^<>]{4,30}$"
 * Objective, skills, education, additional info:
 * "^.[^<>]{5,250}$"
 * phone, ex: 0637253827
 * "^0[1-9][0-9]{8}$"
 * email (перевірити на крапку в кінці)
 * "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$"
 * englishLevel
 * "^[A-C][1-2]$" - englishLevel
 * birth??
 * 
 * 
 * */
