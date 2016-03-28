package edu.epam.persistance;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class HashUtils {
	
	static final Logger LOGGER = Logger.getLogger(HashUtils.class);
	
	private HashUtils(){
	}
	
	public static String getMD5(String password, String email_sault){
		LOGGER.info("Hash password pas 1 : " + password);
		//System.out.println("Hash password pas 1 : " + password);
		String hash = null;

		//System.out.println("mail normal = " + email_sault);
		hash = DigestUtils.md5Hex(password + email_sault);

		LOGGER.info("Hash password pas 2 : " + hash);
		return hash;
	}
	
	public static boolean passIsCorrect(String pass_income, String pass_inDB, String email_sault){
/*		System.out.println("input " + pass_inDB);
		System.out.println("output " + getMD5(pass_income, email_sault));*/
		return getMD5(pass_income, email_sault).equals(pass_inDB);
	}
	
	
	
	
	
}
