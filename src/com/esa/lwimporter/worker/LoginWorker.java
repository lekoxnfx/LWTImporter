package com.esa.lwimporter.worker;

import com.esa.n4.conn.MyN4Operator;

import javax.swing.*;

public class LoginWorker extends SwingWorker<Object,Object> {

	@Override
	protected Object doInBackground() throws Exception {
		// TODO Auto-generated method stub
		boolean login_success = false;
		String xml = "<groovy class-name=\"N4Login\" class-location=\"database\">"
				+ "</groovy>";

		try{
			MyN4Operator n4o = new MyN4Operator();
			n4o.sendRequestWithXml(xml);
			String[] results = n4o.RESULTS;
			if(results[0].equals("login success")){
				login_success = true;
			}
		}catch(Exception e){
			e.printStackTrace();
			login_success = false;
		}
		return login_success;
	}

}
