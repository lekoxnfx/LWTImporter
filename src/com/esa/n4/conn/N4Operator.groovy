package com.esa.n4.conn

import com.navis.argo.webservice.types.v1_0.GenericInvokeResponseWsType
import com.navis.argo.webservice.types.v1_0.QueryResultType
import com.navis.argo.webservice.types.v1_0.ResponseType
import com.navis.argo.webservice.types.v1_0.ScopeCoordinateIdsWsType
import com.navis.www.services.argoservice.ArgoServiceLocator
import com.navis.www.services.argoservice.ArgoServicePort

import javax.xml.rpc.Stub

class N4Operator {
	static String OK = "0"
	static String INFO = "1"
	static String WARNINGS = "2"
	static String ERRORS = "3"

	public String ARGO_SERVICE_URL
	public String USERNAME = ""
	public String PASSWORD = ""

	public String Operator_Id
	public String Complex_Id
	public String Facility_Id
	public String Yard_Id

	ScopeCoordinateIdsWsType scope
	ArgoServiceLocator service
	ArgoServicePort port

	public String STATUS=""
	public String PAYLOAD=""
	public String[] RESULTS=null
	N4Operator(){

	}
	// 初始化
	def initRequest()
	{
		// ָ指定的 Operator/Complex/Facility/Yard : WZCT/WZT/LWT/LWT
		this.scope = new ScopeCoordinateIdsWsType()
		this.scope.setOperatorId(this.Operator_Id)
		this.scope.setComplexId(this.Complex_Id)
		this.scope.setFacilityId(this.Facility_Id)
		this.scope.setYardId(this.Yard_Id)

		// 确定Web服务主机
		this.service = new ArgoServiceLocator()
		this.port = this.service.getArgoServicePort(new URL(this.ARGO_SERVICE_URL))
		Stub stub = (Stub) this.port

		// 指定用户名和密码
		stub._setProperty(Stub.USERNAME_PROPERTY, this.USERNAME)
		stub._setProperty(Stub.PASSWORD_PROPERTY, this.PASSWORD)
	}

	// 发送带XML的请求，返回执行结果状态
	def sendRequestWithXml(String _xmlString) {
		println _xmlString
		// 发送请求
		GenericInvokeResponseWsType response = this.port.genericInvoke(this.scope, _xmlString)
		this.PAYLOAD = response.getResponsePayLoad()
		// 解析API响应
		ResponseType commonResponse = response.getCommonResponse()
		// 获取执行状态
		this.STATUS = commonResponse.getStatus()

		if(commonResponse.getQueryResults()==null){
			this.RESULTS = new String[0]
		}
		else{
			this.RESULTS = new String[commonResponse.getQueryResults().length]
			commonResponse.getQueryResults().eachWithIndex { QueryResultType resultType, int i ->
				this.RESULTS[i] = resultType.getResult()
			}
		}
		println this.PAYLOAD
		println this.STATUS
		println this.RESULTS

	}
}


