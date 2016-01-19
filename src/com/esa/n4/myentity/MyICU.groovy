package com.esa.n4.myentity

import groovy.xml.MarkupBuilder

class MyICU {
    String unit_id
    String load_list_id
    String cmdy_id
    String ex_bl_id



    public String createICUXmlString() {
        def snxBillStringWriter = new StringWriter()
        def snx = new MarkupBuilder(snxBillStringWriter)
        String tag1 = 'UnitFlexString02'
        String tag2 = 'CommodityRef'
        String tag3 = 'UnitFlexString04'
        snx.'icu'() {
            'units'() {
                    'unit-identity'('id': unit_id, 'type': 'CONTAINERIZED')
            }
            'properties'() {
                if(load_list_id !=null && load_list_id != ""){
                    'property'('tag': tag1, value: load_list_id)
                }

                if (cmdy_id != null && cmdy_id != "") {
                    'property'('tag': tag2, value: cmdy_id)
                }

                if (ex_bl_id !=null && ex_bl_id != ""){
                    'property'('tag': tag3, value: ex_bl_id)
                }
            }
        }
        return snxBillStringWriter.toString()

    }


}
