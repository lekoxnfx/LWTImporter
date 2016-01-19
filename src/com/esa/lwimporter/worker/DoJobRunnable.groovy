package com.esa.lwimporter.worker

import com.esa.lwimporter.ui.OutListener
import com.esa.lwimporter.ui.StaticData
import com.esa.n4.conn.MyN4Operator
import com.esa.n4.conn.N4DBAdapter
import com.esa.n4.myentity.MyBillsOfLading
import com.esa.n4.myentity.MyICU
import com.esa.n4.myentity.MyUnit
import groovy.sql.Sql
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook

import java.util.regex.Matcher
import java.util.regex.Pattern

public class DoJobRunnable implements Runnable {

    OutListener outer;

    public void addOutListener(OutListener inOuter) {
        this.outer = inOuter;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        outer.out("\n************************\n*****开始读取！！*****\n************************\n");

        //判断类型
        outer.out("类型是：" + StaticData.TYPE);

        outer.out("开始分析Excel文件!\n");

        switch (StaticData.TYPE) {
            case "进口舱单":
                try {
                    doReadUnit();
                } catch (Exception e) {
                    outer.out(e.toString())
                    e.printStackTrace()
                }
                break;

            case "出口提单":
                try {
                    doReadBL();
                } catch (Exception e) {
                    outer.out(e.toString())
                    e.printStackTrace()
                }
                break;
        }

    }

    public void doReadUnit() {//读取进口舱单
        outer.out("开始导入进口舱单")

        File file = StaticData.READ_FILE;
        //create list to store units
        List list_u = new LinkedList<MyUnit>()

        //create list to store bills
        List list_b = new LinkedList<MyBillsOfLading>()

        int rowNumber
        //生成箱号和提单号
        String filename = file.name[0..-5]

        boolean read_correct = true
        def wb = new HSSFWorkbook(new FileInputStream(file.toString()))
        //workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        //循环sheet

        wb.getSheets().each { sheet ->
            outer.out("打开Sheet：" + sheet.sheetName)
            //循环行
            int lastRowNbr = sheet.getLastRowNum()
            rowNumber = lastRowNbr
            outer.out('一共' + sheet.getLastRowNum() + '行数据')
            sheet.eachWithIndex { row, index ->
                if (index == 0) {//检查标题行
                    outer.out("检查标题行......")
                    if (stripe.call(getCellVal(row.getCell(0))) != '提单号') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(1))) != '箱号') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(2))) != '箱长') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(3))) != '箱型') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(4))) != '箱公司') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(5))) != '封号') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(6))) != '货名') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(7))) != '空重') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(8))) != '重量') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(9))) != '装货港') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(10))) != '卸货港') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(11))) != '中转港') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(12))) != '内外贸') read_correct = false
                    if (stripe.call(getCellVal(row.getCell(13))) != '破损') read_correct = false

                    if (read_correct == false) {
                        outer.out("Excel文件列名错误！应为：提单号 箱号 箱长 箱型 箱公司 封号 货名 空重 重量  装货港 卸货港 中转港 内外贸 破损")
                    }
                }

                if (read_correct && index > 0 && index <= lastRowNbr) {//从第二行到最后一行
                    def cells = row.physicalNumberOfCells//取得列数

                    //定义Unit属性参数
                    def _id
                    def _transit_state            //状态 Inbound
                    def _category             //货物进出口类别
                    def _freight_kind        //空重、散货
                    def _line                    //船公司
                    def _weight                 //重量
                    def _type                //箱类
                    def _size                //箱型
                    def _visit = filename

                    def _bl_number        //提单号
                    def _cmdy_id       //货物种类
                    def _pod_1
                    def _pod_2
                    def _pol
                    def _seal_no
                    def _position

                    def _transfer   //中转信息
                    def _damage     //破损信息

                    //定义读取的参数
                    def e_tidanhao
                    def e_xianghao
                    def e_xiangchang
                    def e_xiangxing
                    def e_xianggongsi
                    def e_fenghao
                    def e_huoming
                    def e_kongzhong
                    def e_zhongliang
                    def e_zhuanghuogang
                    def e_xiehuogang
                    def e_zhongzhuangang
                    def e_neiwaimao
                    def e_damage

                    //读取excel相应信息
                    e_tidanhao = stripe.call(getCellVal(row.getCell(0)))
                    e_xianghao = stripe.call(getCellVal(row.getCell(1))).toUpperCase()
                    e_xiangchang = stripe.call(getCellVal(row.getCell(2)))
                    e_xiangxing = stripe.call(getCellVal(row.getCell(3)))
                    e_xianggongsi = stripe.call(getCellVal(row.getCell(4)))
                    e_fenghao = stripe.call(getCellVal(row.getCell(5)))
                    e_huoming = stripe.call(getCellVal(row.getCell(6)))
                    e_kongzhong = stripe.call(getCellVal(row.getCell(7)))
                    e_zhongliang = stripe.call(getCellVal(row.getCell(8)))
                    e_zhuanghuogang = stripe.call(getCellVal(row.getCell(9)))
                    e_xiehuogang = stripe.call(getCellVal(row.getCell(10)))
                    e_zhongzhuangang = stripe.call(getCellVal(row.getCell(11)))
                    e_neiwaimao = stripe.call(getCellVal(row.getCell(12)))
                    e_damage = stripe.call(getCellVal(row.getCell(13)))

                    if (e_xianghao.length() > 0) {
                        //create unit and store into list
                        outer.out("读取第" + (index + 1) + "行，箱ID：" + e_xianghao)
                        _id = e_xianghao
                        _pol = e_zhuanghuogang
                        _pod_1 = e_zhongzhuangang
                        _pod_2 = e_xiehuogang
                        _cmdy_id = e_huoming
                        _seal_no = e_fenghao
                        _transit_state = 'INBOUND'

                        //判断是否位破损箱
                        _damage = false
                        if (e_damage != null && e_damage.length() > 0) {
                            switch (e_damage) {
                                case '破损':
                                case '是':
                                case '有':
                                case 'PS':
                                case 'ps':
                                    _damage = true
                                    break
                                default:
                                    _damage = false
                                    break
                            }
                        }

                        _transfer = false
                        switch (e_neiwaimao.toUpperCase()) {
                            case 'JK':
                            case '进口':
                            case 'IMPORT':
                                _category = 'IMPORT'
                                break
                            case 'ZZ':
                            case '中转':
                            case 'TRANSSHIP':
                                _category = 'TRANSSHIP'
                                _transfer = true
                                break
                            case 'GJ':
                            case '过境':
                            case 'THROUGH':
                                _category = 'THROUGH'
                                break
                            default:
                                outer.out('进口舱单"内外贸"只能是"进口"，"JK","中转"，"ZZ","过境","GJ"')
                                read_correct = false;
                        }

                        _freight_kind = e_kongzhong
                        switch (_freight_kind) {
                            case 'F':
                                _freight_kind = 'FCL'
                                break
                            case 'E':
                                _freight_kind = 'MTY'
                                break
                            default:
                                _freight_kind = 'FCL'
                        }
                        _line = e_xianggongsi

                        _weight = e_zhongliang
                        println 'xingxinghao=' + e_xiangchang + e_xiangxing
                        _size = conventToN4((e_xiangchang + e_xiangxing).toString())

                        //根据箱长和空中,自动计算重量
                        def _auto_weight = 0
                        boolean is_length20 = ('' + e_xiangchang) == '20'
                        boolean is_empty = (e_kongzhong + '').toUpperCase() == 'E'

                        if (is_length20 && is_empty) {
                            //20空箱
                            _auto_weight = 2300
                        } else if (is_length20 && !is_empty) {
                            //20重箱
                            _auto_weight = 28000 + 2300
                        } else if (!is_length20 && is_empty) {
                            //40空箱
                            _auto_weight = 3800
                        } else if (!is_length20 && !is_empty) {
                            //40重箱
                            _auto_weight = 26000 + 3800
                        }
                        _weight = _auto_weight + ''


                        MyUnit u = new MyUnit()
                        u.unit_id = _id
                        u.unit_transit_state = _transit_state
                        u.unit_category = _category
                        u.unit_freight_kind = _freight_kind
                        u.unit_line = _line
                        u.unit_weight = _weight
                        u.unit_type = _size
                        u.unit_iso_group = ''
                        u.unit_vessel_visit = _visit
                        u.unit_cmdy_id = _cmdy_id
                        u.unit_pol = _pol
                        u.unit_pod_1 = _pod_1
                        u.unit_pod_2 = _pod_2
                        u.unit_seal_no = _seal_no
                        u.unit_transfer = _transfer ? "中转" : ""
                        u.unit_damage = _damage

                        if (u.unit_type == "NotFound") {
                            outer.out("没能转换第" + (index + 1) + "行的箱型，箱号：" + u.unit_id + "，箱类型：" + e_xiangchang + e_xiangxing + "， 将会引起错误。")
                            read_correct = false
                        } else {
                            list_u.add(u)

                            _bl_number = e_tidanhao
                            List<String> units = [_id]

                            //						Bill bi=new Bill(_bl_number, _category, _line, _visit, _pol, _pod, units)
                            if (_freight_kind != 'MTY') {
                                MyBillsOfLading bi = new MyBillsOfLading()
                                bi.bill_nbr = _bl_number
                                bi.bill_category = _category
                                bi.bill_line_operator = _line
                                bi.bill_carrier_visit = _visit
                                bi.bill_port_of_load = _pol
                                bi.bill_port_of_discharge = _pod_1
                                bi.bill_port_of_discharge_2 = _pod_2
                                bi.bill_units = units
                                list_b.add(bi)
                            }
                        }
                    }


                }

            }
        }
        if (read_correct == false) {
            outer.out("读取结束,有错误产生")
        } else {
            outer.out("读取结束,读取箱子数目：" + list_u.size() + "；读取提单有效条目：" + list_b.size())
        }

        if (read_correct) {
            //检查数据
            outer.out("开始检查数据")
            N4DBAdapter n4dba = new N4DBAdapter()
            String sqlvv = "select * from argo_carrier_visit where carrier_mode='VESSEL' and ID='" + filename + "' and phase not in ('60DEPARTED','70CLOSED','80CANCELED')"
            Sql SQL = n4dba.SQL
            boolean vvexist = false
            SQL.eachRow(sqlvv) { r ->
                vvexist = true;
            }
            if (!vvexist) {
                outer.out("船期：" + filename + "不存在,或者已经离开、关闭、取消")
            } else {
                outer.out("船期：" + filename + "存在")
                outer.out("检查箱号是否冲突")

                boolean unitid_correct = true
                HashSet<String> unit_set = new HashSet<String>()
                String sqlu = "select id from inv_unit where VISIT_STATE = '1ACTIVE'"
                SQL.eachRow(sqlu) { r ->
                    unit_set.add(r.'id')
                }
                outer.out("目前有个" + unit_set.size() + "活动的箱子")
                String str = "[A-Z]{4}[0-9]{7}"
                Pattern pattern = Pattern.compile(str)
                list_u.each { u ->
                    String uid = u.unit_id
                    Matcher m = pattern.matcher(uid)
                    boolean res = m.matches()
                    if (!res) {
                        outer.out("箱号" + uid + "不符合简单标准（4位字母加上7位数字）")
                        unitid_correct = false
                    }
                    if (unit_set.contains(uid)) {
                        unitid_correct = false
                        outer.out("箱号" + uid + "在系统中已存在活动记录！")
                    }
                }
                if (!unitid_correct) {
                    outer.out("箱号检查发现错误，停止导入。")

                } else {
                    outer.out("箱号检查完毕！")
                    //指派位置
                    outer.out("为箱子指派船上位置，如果有未识别的箱型，将会报错。")
                    list_u = LocationGen.CreateLocation(list_u)

                    outer.out("生成Unit的xml")
                    String xmlu = MyUnit.createMultiSNX(list_u)
                    outer.out(xmlu)
                    outer.out("\n\n\n")

                    outer.out("生成BillsOfLading的xml")
                    String xmlb = MyBillsOfLading.createMultiSNX(list_b)
                    outer.out(xmlb)
                    outer.out("\n\n\n")

                    MyN4Operator n4o = new MyN4Operator();

                    outer.out("发送Unit信息给N4...")
                    n4o.sendRequestWithXml(xmlu)
                    if (n4o.STATUS.equals(n4o.OK)) {
                        outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(OK)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                        outer.out("\n###############\n###导入成功！###\n###############")
                    }
                    if (n4o.STATUS.equals(n4o.INFO)) {
                        outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(信息：INFO)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                        outer.out("\n###############\n###导入成功！###\n###############")
                    }
                    if (n4o.STATUS.equals(n4o.WARNINGS)) {
                        outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(警告：WARNING)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                        outer.out("\n???????????????\n   导入过程中有警告   \n????????????")
                    }
                    if (n4o.STATUS.equals(n4o.ERRORS)) {
                        outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(错误：ERROR)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                        outer.out("\n！！！！！！！！！！\n   导入失败！有错误   \n！！！！！！！！")
                    }
                    if (!n4o.STATUS.equals(n4o.ERRORS)) {
                        outer.out("发送BillsOfLading信息给N4...")
                        n4o.sendRequestWithXml(xmlb)
                        if (n4o.STATUS.equals(n4o.OK)) {
                            outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(OK)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                            outer.out("\n###############\n###导入成功！###\n###############")
                        }
                        if (n4o.STATUS.equals(n4o.INFO)) {
                            outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(信息：INFO)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                            outer.out("\n###############\n###导入成功！###\n###############")
                        }
                        if (n4o.STATUS.equals(n4o.WARNINGS)) {
                            outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(警告：WARNING)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                            outer.out("\n???????????????\n   导入过程中有警告   \n????????????")
                        }
                        if (n4o.STATUS.equals(n4o.ERRORS)) {
                            outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(错误：ERROR)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                            outer.out("\n！！！！！！！！！！\n   导入失败！有错误   \n！！！！！！！！")
                        }

                    }

                }
            }
        }
    }

    public void doReadBL() {//读取出口提单
        outer.out("开始导入出口提单")

        File file = StaticData.READ_FILE
        //读取文件名
        String filename = file.name[0..-5]

        List<MyICU> myICUList = new ArrayList<MyICU>()

        boolean is_correct = true
        def wb = new HSSFWorkbook(new FileInputStream(file.toString()))
        //workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
        //循环sheet


        wb.getSheets().each { sheet ->
            outer.out("打开Sheet：" + sheet.sheetName)
            //循环行
            int lastRowNbr = sheet.getLastRowNum()
            int rowNumber = lastRowNbr
            outer.out('一共' + sheet.getLastRowNum() + '行数据')
            sheet.eachWithIndex { row, index ->
                if (index == 0) {//检查标题行

                    if (stripe.call(getCellVal(row.getCell(0))) != '箱号') is_correct = false
                    if (stripe.call(getCellVal(row.getCell(1))) != '货名') is_correct = false
                    if (stripe.call(getCellVal(row.getCell(2))) != '装船清单') is_correct = false   //UnitFlexString02
                    if (stripe.call(getCellVal(row.getCell(3))) != '出口提单') is_correct = false   //UnitFlexString04



                    if (is_correct == false) {
                        outer.out("Excel文件列名错误！应为  箱号,货名,装船清单,出口提单 ")
                    }
                }

                if (is_correct && index > 0 && index <= lastRowNbr) {//从第二行到最后一行
                    def cells = row.physicalNumberOfCells//取得列数

                    //定义读取的参数
                    def e_unitID        //箱号
                    def e_cmdyID        //货名
                    def e_billID        //出口提单号
                    def e_loadList      //装船清单

                    //读取excel相应信息
                    e_unitID = stripe.call(getCellVal(row.getCell(0)))
                    e_cmdyID = stripe.call(getCellVal(row.getCell(1)))
                    e_loadList = stripe.call(getCellVal(row.getCell(2)))
                    e_billID = stripe.call(getCellVal(row.getCell(3)))

                    if(e_unitID!=null){
                        MyICU myicu = new MyICU()

                        myicu.unit_id = e_unitID
                        myicu.cmdy_id = e_cmdyID
                        myicu.ex_bl_id = e_billID
                        myicu.load_list_id = e_loadList

                        myICUList.add(myicu)

                    }




                }
            }
//			println 'unit-amount:'+list_u.size()
//			println 'bill-amount:'+list_b.size()

        }
        if (is_correct) {
            outer.out("读取结束,读取箱子数目：" + myICUList.size())

            outer.out("开始检查箱号")
            N4DBAdapter n4dba = new N4DBAdapter()
            Sql SQL = n4dba.SQL

            boolean unitid_correct = true
            HashSet<String> unit_set = new HashSet<String>()
            String sqlu = "select id from inv_unit where VISIT_STATE = '1ACTIVE'"
            SQL.eachRow(sqlu) { r ->
                unit_set.add(r.'id')
            }
            outer.out("目前有个" + unit_set.size() + "活动的出口箱")
            List<MyICU> not_in_yard_ICU = new ArrayList<MyICU>()
            myICUList.each { icu ->
                if (!unit_set.contains(icu.unit_id)) {
                    not_in_yard_ICU.add(icu)
                }
            }

            //删除不在场的箱子
            not_in_yard_ICU.each {icu->
                myICUList.remove(icu)
            }




            MyN4Operator n4o = new MyN4Operator();

            List<MyICU> ok_icus = new ArrayList<MyICU>()
            List<MyICU> error_icus = new ArrayList<MyICU>()
            List<MyICU> warning_icus = new ArrayList<MyICU>()


            myICUList.each { icu ->
                String xml = ""


                outer.out("生成标记出口箱的xml...")


                xml = icu.createICUXmlString()

                outer.out("xml:\n" + xml)
                outer.out("发送xml信息给N4...")

                n4o.sendRequestWithXml(xml)

//                System.out.println(xml)
                if (n4o.STATUS.equals(n4o.OK)) {
                    outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(OK)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                    outer.out("\n###############\n###导入成功！###\n###############")
                    ok_icus.add(icu)
                }
                if (n4o.STATUS.equals(n4o.INFO)) {
                    outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(信息：INFO)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                    outer.out("\n###############\n###导入成功！###\n###############")
                    ok_icus.add(icu)
                }
                if (n4o.STATUS.equals(n4o.WARNINGS)) {
                    outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(警告：WARNING)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                    outer.out("\n???????????????????????????\n   导入过程中有警告   \n????????????????????????")
                    warning_icus.add(icu)
                }
                if (n4o.STATUS.equals(n4o.ERRORS)) {
                    outer.out("返回结果：\n" + "STATUS:" + n4o.STATUS + "(错误：ERROR)" + "\nPAYLOAD:\n" + n4o.PAYLOAD + "\nRESULTS:\n" + n4o.RESULTS)
                    outer.out("\n！！！！！！！！！！！！！！！！！！！！\n   导入失败！有错误   \n！！！！！！！！！！！！！！！！！！")
                    error_icus.add(icu)
                }

                outer.out("---------------------------------")
            }
            String not_yard = ""
            not_in_yard_ICU.each { icu ->
                not_yard += icu.unit_id + "\n"
            }

            String ok_ids = ""
            ok_icus.each { icu->
                ok_ids += icu.unit_id + "\n"
            }
            String warning_ids = ""
            warning_icus.each { icu->
                warning_ids += icu.unit_id + "\n"
            }
            String error_ids = ""
            error_icus.each { icu->
                error_ids += icu.unit_id + "\n"
            }
            outer.out("----------------结果--------------")

            outer.out("成功：" + ok_icus.size() + "\n" +  ok_ids)

            outer.out("成功但有警告：" + warning_icus.size() + "\n" + warning_ids)

            outer.out("失败：" + error_icus.size() + "\n" + error_ids)



            outer.out("不在场" + not_in_yard_ICU.size() + "\n" + not_yard)
            outer.out("---------------------------------")
        } else {
            outer.out("读取出现错误")
        }
    }

    //格式转换
    public String conventToN4(String _Value) {
        switch (_Value) {
            case '20GP':
            case '20DC':
                return '2200'
                break
            case '20RF':
                return '2230'
            case '40GP':
            case '40DC':
                return '4200'
                break
            case '40HQ':
                return '4500'
                break
            case '40HC':
                return '4500'
                break
            case '45GP':
                return '9500'
                break
            default:
                return _Value
        }

    }

    def getCellVal(HSSFCell cell) {
        if (cell == null) return ""
        else {
            def value
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue();
                    break
                case HSSFCell.CELL_TYPE_ERROR:
                    value = cell.getErrorCellValue()
                    break
                case HSSFCell.CELL_TYPE_NUMERIC:
                    value = "" + cell.getNumericCellValue()
                    for (int i = 0; i < value.length(); i++) {
                        if (".".equals(value.charAt(i))) {
                            //有小数点
                        } else {
                            //无小数点，转换为int后转换为String
                            value = "" + ((int) Double.parseDouble(value))
                        }
                    }
                    break
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue()
                    break
                default:
                    value = ""
            }
            return value
        }

    }

    def stripe = {
        it.toString().reverse().stripIndent().reverse().stripIndent()
    }

}
