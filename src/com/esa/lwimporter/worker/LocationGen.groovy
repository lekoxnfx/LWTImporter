package com.esa.lwimporter.worker

import com.esa.n4.myentity.MyUnit

class LocationGen {
	def bay
	def lane
	def tier
	static def init_bay = 01
	static def init_lane = 00
	static def init_tier = 02
	static def tier_below_max = 16
	static def tier_above_max = 98
	static def lane_max = 12
	
	LocationGen() {
		bay = init_bay
		lane = init_lane
		tier = init_tier
	}
	def PosToStr(def _bay,def _lane,def _tier){
		def pos = ''
		pos = pos + '0' + _bay.toString()
		if (_lane < 10){
			pos = pos + '0' + _lane.toString()
		}
		else{
			pos = pos + _lane.toString()
		}
		if (_tier < 10){
			pos = pos + '0' + _tier.toString()
		}
		else{
			pos = pos + _tier.toString()
		}
		
	}

	def GenLocation(def _type){
		int size = Integer.parseInt(_type)
		if(size>=4000){
			if(bay == 03){
				
			}
			bay = 02
			def pos = PosToStr(bay,lane,tier)
			if(lane < lane_max){
				lane = lane + 1
			}
			if(lane == lane_max){
				lane = init_lane
				
				if(tier < tier_below_max){
					tier = tier + 2
				}
				else if(tier == tier_below_max){
					tier = 82
				}
				else if(tier <= tier_above_max ){
					tier = tier + 2
				}
				else {
					
				}
			}
			return pos
		}
		else{
			def pos = ''
			if(bay == 01){
				pos  = PosToStr(bay,lane,tier)
				bay = 03
				return pos
			}
			if(bay == 03){
				pos =  PosToStr(bay,lane,tier)
				bay = 01
				if(lane < lane_max){
					lane = lane + 1
				}
				if(lane == lane_max){
					lane = init_lane
					
					if(tier < tier_below_max){
						tier = tier + 2
					}
					else if(tier == tier_below_max){
						tier = 82
					}
					else if(tier < tier_above_max ){
						tier = tier + 2
					}
					else {
						return 'vessel is full!'
					}
				}
				return pos
			}
			if(bay == 02){
				bay = 01
				pos = PosToStr(bay,lane,tier)
				bay = 03
				return pos
			}
		}
	}
	static def CreateLocation(List<MyUnit> l_u){
		List l_res = new LinkedList<MyUnit>()
		LocationGen loc = new LocationGen()
		l_u.each {u->
			println '箱号:'+u.unit_id+' 类型：'+ u.unit_type
			int size = Integer.parseInt(u.unit_type)
			if(size<4000){
				l_res.add(u)
			}
			else{
				l_res.add(0,u)
			}
			
		}
		l_res.each {
			MyUnit u = it
			u.unit_slot = loc.GenLocation(u.unit_type)
		}
//		println 'kkk:'+l_res.size()
		return l_res
	}
}

