package com.esa.n4.conn;

import com.esa.config.N4Config;

public class MyN4Operator extends N4Operator {

	MyN4Operator(){
		
		Operator_Id = N4Config.N4_OPERATOR_ID
		Complex_Id = N4Config.N4_COMPLEX_ID
		Facility_Id = N4Config.N4_FACILITY_ID
		Yard_Id = N4Config.N4_YARD_ID
		
		USERNAME = N4Config.N4_USERNAME
		PASSWORD = N4Config.N4_PASSOWRD
		ARGO_SERVICE_URL = "http://" + N4Config.N4_IP + ":" + N4Config.N4_PORT + "/apex/services/argoservice"
		
		initRequest()
	}
}
