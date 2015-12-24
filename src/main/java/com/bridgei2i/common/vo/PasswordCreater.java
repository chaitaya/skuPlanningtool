package com.bridgei2i.common.vo;

import com.bi2i.login.EncryptService;

public class PasswordCreater {
	private final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EncryptService obj = EncryptService.getInstance();
		PasswordCreater media = new PasswordCreater();
		for(int i = 0; i < 2; i++)
		{
			
			System.out.println("Orig:   "+media.getPublishRandomKey() + "       ------>Enc:    " +obj.encrypt(media .getPublishRandomKey()) );
			
		}
		System.out.println(obj.decrypt("WHkWtTgA6yA="));
		/*System.out.println(obj.decrypt("cyrdAxY+RUg="));
		System.out.println(obj.decrypt("HK1s+Z41N3Y="));
		System.out.println(obj.decrypt("hjkaZ8ohM4s="));
		System.out.println(obj.decrypt("qpcXa83o818="));
		System.out.println(obj.decrypt("jyQipAedtYE="));
		System.out.println(obj.decrypt("WGYHxr0GkaI="));
		System.out.println(obj.decrypt("gi/2VPXjZic="));
		System.out.println(obj.decrypt("1OlxonNC5Ss="));
		System.out.println(obj.decrypt("G13UtiWU9RY="));*/
		


	}
		
	   public String getPublishRandomKey()
	    {
	        StringBuffer sb = new StringBuffer();
	        for(int i = 0; i < 5; i++)
	        {
	            int ndx = (int)(Math.random() * (double)ALPHA_NUM.length());
	            sb.append(ALPHA_NUM.charAt(ndx));
	        }
	 
	        return sb.toString();
	    }

}
