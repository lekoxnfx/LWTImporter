package com.esa.n4.conn

import com.esa.config.N4Config
import groovy.sql.Sql

public class N4DBAdapter {
	
	def DB
	def USER
	def PASSWORD
	def DRIVER
	Sql SQL
	
	N4DBAdapter(){		
		DB = 'jdbc:oracle:thin:@'+N4Config.N4_DB_IP+':'+N4Config.N4_DB_PORT+':' + N4Config.N4_DB_SID
		USER = N4Config.N4_DB_USERNAME
		PASSWORD = N4Config.N4_DB_PASSWORD
		DRIVER = 'oracle.jdbc.driver.OracleDriver'
		SQL = Sql.newInstance(DB, USER, PASSWORD, DRIVER)		
	}
	void closeConnection(){
		SQL.getConnection().close()
	}
}
