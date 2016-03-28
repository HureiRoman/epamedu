package edu.epam.sms;

public class Test {
		 
	    public static void main(String[] args) {
	 
	        SMSCSender sd= new SMSCSender("slim50", "a133e6cd12c1a0899f275bc6058a0ce9", "utf-8", true);
	 
	        sd.sendSms("380634243164", "Дмитрик привіт", 1, "", "", 0, "", "");
	        sd.getSmsCost("380634243164", "Вы успешно зарегистрированы!", 0, 0, "", "");
	        sd.getBalance();
	    }
	 
	
}
