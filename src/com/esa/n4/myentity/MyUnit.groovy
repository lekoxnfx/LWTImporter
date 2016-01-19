package com.esa.n4.myentity

import groovy.xml.MarkupBuilder

class MyUnit {
    def unit_id                    // unit ID号
    def unit_transit_state        // unit 状态()
    def unit_category            // unit 进出口类别(IMPORT, EXPORT)
    def unit_freight_kind        // unit 箱货类型(空箱, 重箱, 散货)
    def unit_line                // line-operator
    def unit_weight                // 箱货重量
    def unit_iso_group                // 集装箱
    def unit_type                // 集装箱iso_type
    def unit_slot                //箱位
    def unit_vessel_visit        // 进(出)口航期

    def unit_seal_no            //unit 封号
    def unit_pol                //装货港
    def unit_pod_1                //中转港
    def unit_pod_2                //卸货港

    def unit_cmdy_id           //货物代码
    def unit_damage                //是否破损箱
    def unit_transfer            //是否中转箱

    def dummyVV = "LWZZ"

    MyUnit() {

    }

    // 生成多元素SNX
    static def createMultiSNX(List<MyUnit> _UnitList) {
        def snxUnitString = new StringWriter()
        def snxUnits = new MarkupBuilder(snxUnitString)
        snxUnits.'argo:snx'('xmlns:argo': 'http://www.navis.com/argo', 'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance', 'xsi:schemaLocation': 'http://www.navis.com/argo snx.xsd') {
            _UnitList.each {
                MyUnit u = it
                'unit'('id': u.unit_id, 'transit-state': u.unit_transit_state, 'category': u.unit_category, 'freight-kind': u.unit_freight_kind, 'line': u.unit_line)
                        {
                            'equipment'(
                                    'eqid': u.unit_id,
                                    'class': 'CTR',
                                    'role': "PRIMARY",
                                    'type': u.unit_type,
                                    //'tare-kg':"1900.0",
                                    //'height-mm':"2800",
                                    //'strength-code':"AA",
                                    //'material':"ALUMINUM",
                                    'owner': u.unit_line,
                                    'operator': u.unit_line,
                                    'iso-group': u.unit_iso_group,
                            )
                                    {
                                        if (u.unit_damage) {
                                            'damages'
                                                    {
                                                        'damage'('type': 'DEFACE',
                                                                'component': 'BACK',
                                                                'severity': 'MAJOR'
                                                        )
                                                    }
                                        }
                                    }
                            'position'('loc-type': 'VESSEL', 'location': u.unit_vessel_visit, 'slot': u.unit_slot)
                            switch (u.unit_category) {
                                case 'TRANSSHIP':
                                    ('routing'('pod-1': u.unit_pod_1, 'pod-2': u.unit_pod_2, 'pol': u.unit_pol)
                                            {
                                                'carrier'('direction': 'IB', 'qualifier': 'DECLARED', 'mode': 'VESSEL', 'id': u.unit_vessel_visit)
                                                'carrier'('direction': 'IB', 'qualifier': 'ACTUAL', 'mode': 'VESSEL', 'id': u.unit_vessel_visit)
                                                'carrier'('direction': 'OB', 'qualifier': 'DECLARED', 'mode': 'VESSEL', 'id': u.dummyVV)
                                                'carrier'('direction': 'OB', 'qualifier': 'ACTUAL', 'mode': 'VESSEL', 'id': u.dummyVV)
                                            })
                                    break

                                case 'IMPORT':
                                    ('routing'('pod-1': u.unit_pod_1, 'pod-2': u.unit_pod_2, 'pol': u.unit_pol)
                                            {
                                                'carrier'('direction': 'IB', 'qualifier': 'DECLARED', 'mode': 'VESSEL', 'id': u.unit_vessel_visit)
                                                'carrier'('direction': 'IB', 'qualifier': 'ACTUAL', 'mode': 'VESSEL', 'id': u.unit_vessel_visit)
                                                'carrier'('direction': 'OB', 'qualifier': 'DECLARED', 'mode': 'TRUCK', 'id': 'GEN_TRUCK')
                                                'carrier'('direction': 'OB', 'qualifier': 'ACTUAL', 'mode': 'TRUCK', 'id': 'GEN_TRUCK')
                                            })
                                    break

                                default:
                                    ('routing'('pod-1': u.unit_pod_1, 'pod-2': u.unit_pod_2, 'pol': u.unit_pol) {

                                    })

                            }


                            'contents'('commodity-id': u.unit_cmdy_id, 'weight-kg': u.unit_weight)
                            'seals'('seal-1': u.unit_seal_no)
                            /*
                            'non-move-history'(){
                                'event'('id':"EVENT01",'note':"snx",'time-event-applied':"2013-04-21T12:12:58",'user-id':"snx:admin",'is-billable':"N")
                            }
                            */
                            'unit-flex'('unit-flex-11': u.unit_transfer)
                        }
            }

        }
        return snxUnitString.toString()
    }
}
