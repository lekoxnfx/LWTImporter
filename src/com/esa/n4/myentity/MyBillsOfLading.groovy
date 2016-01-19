package com.esa.n4.myentity

import groovy.xml.MarkupBuilder

class MyBillsOfLading {
	def bill_nbr
	def bill_category
	def bill_line_operator
	def bill_carrier_visit
	def bill_port_of_load
	def bill_port_of_discharge
	def bill_port_of_discharge_2
	
	// 该bill包含的unit
	List<String> bill_units
	
	// 构造函数
	
	def Bill(){
		
	}
	
	static def createMultiSNX(List<MyBillsOfLading> billList)
	{
		// 生成多元素SNX
		def snxBillStringWriter = new StringWriter()
		def snx = new MarkupBuilder(snxBillStringWriter)
		snx.'argo:snx'('xmlns:argo':'http://www.navis.com/argo', 'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance', 'xsi:schemaLocation':'http://www.navis.com/argo snx.xsd') {
			billList.each {
				def b = it
				if(b.bill_port_of_load != '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 != ''){
					'bill-of-lading'(
						'nbr':b.bill_nbr,
						'category':b.bill_category,
						'line':b.bill_line_operator,
						'carrier-visit':b.bill_carrier_visit,
						'pol':b.bill_port_of_load,
						'pod-1':b.bill_port_of_discharge,
						'pod-2':b.bill_port_of_discharge_2
						)
					{
						b.bill_units.each {
							'goods-bl'('unit-id':it)
						}
					}
				}
				if(b.bill_port_of_load != '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 == ''){
					'bill-of-lading'(
						'nbr':b.bill_nbr,
						'category':b.bill_category,
						'line':b.bill_line_operator,
						'carrier-visit':b.bill_carrier_visit,
						'pol':b.bill_port_of_load,
						'pod-1':b.bill_port_of_discharge
						)
					{
						b.bill_units.each {
							'goods-bl'('unit-id':it)
						}
					}
				}
				if(b.bill_port_of_load != '' && b.bill_port_of_discharge == '' && b.bill_port_of_discharge_2 == ''){
					'bill-of-lading'(
						'nbr':b.bill_nbr,
						'category':b.bill_category,
						'line':b.bill_line_operator,
						'carrier-visit':b.bill_carrier_visit,
						'pol':b.bill_port_of_load,
						)
					{
						b.bill_units.each {
							'goods-bl'('unit-id':it)
						}
					}
				}
				if(b.bill_port_of_load == '' && b.bill_port_of_discharge == '' && b.bill_port_of_discharge_2 == ''){
					'bill-of-lading'(
						'nbr':b.bill_nbr,
						'category':b.bill_category,
						'line':b.bill_line_operator,
						'carrier-visit':b.bill_carrier_visit,
						)
					{
						b.bill_units.each {
							'goods-bl'('unit-id':it)
						}
					}
				}
				//出口箱
				if(b.bill_port_of_load == '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 == ''){
					'bill-of-lading'(
							'nbr':b.bill_nbr,
							'category':b.bill_category,
							'line':b.bill_line_operator,
							'carrier-visit':b.bill_carrier_visit,
							'pod-1':b.bill_port_of_discharge,
					)
							{
								b.bill_units.each {
									'goods-bl'('unit-id':it)
								}
							}
				}
				if(b.bill_port_of_load == '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 != ''){
					'bill-of-lading'(
							'nbr':b.bill_nbr,
							'category':b.bill_category,
							'line':b.bill_line_operator,
							'carrier-visit':b.bill_carrier_visit,
							'pod-1':b.bill_port_of_discharge,
							'pod-2':b.bill_port_of_discharge_2
					)
							{
								b.bill_units.each {
									'goods-bl'('unit-id':it)
								}
							}
				}
				if(b.bill_port_of_load != '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 == ''){
					'bill-of-lading'(
							'nbr':b.bill_nbr,
							'category':b.bill_category,
							'line':b.bill_line_operator,
							'carrier-visit':b.bill_carrier_visit,
							'pol':b.bill_port_of_load,
							'pod-1':b.bill_port_of_discharge,
					)
							{
								b.bill_units.each {
									'goods-bl'('unit-id':it)
								}
							}
				}
				if(b.bill_port_of_load != '' && b.bill_port_of_discharge != '' && b.bill_port_of_discharge_2 != ''){
					'bill-of-lading'(
							'nbr':b.bill_nbr,
							'category':b.bill_category,
							'line':b.bill_line_operator,
							'carrier-visit':b.bill_carrier_visit,
							'pol':b.bill_port_of_load,
							'pod-1':b.bill_port_of_discharge,
							'pod-2':b.bill_port_of_discharge_2
					)
							{
								b.bill_units.each {
									'goods-bl'('unit-id':it)
								}
							}
				}
			}
		}
		return snxBillStringWriter.toString()
	}
}
