package edu.epam.role;

import edu.epam.annotations.Column;

public class User extends CommonUser {
	@Column("key_used")
	protected boolean isKeyUsed;

	public boolean getIsKeyUsed() {
		return isKeyUsed;
	}

	public void setIsUsed(boolean isUsed) {
		this.isKeyUsed = isUsed;
	}

	public void setIsKeyActive(String isKeyUsed) {
		this.isKeyUsed = Boolean.valueOf(isKeyUsed);
	}

}
