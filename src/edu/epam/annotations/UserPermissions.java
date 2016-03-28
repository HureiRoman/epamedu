package edu.epam.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edu.epam.constants.RoleType;
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermissions {
	RoleType[] value();
}
