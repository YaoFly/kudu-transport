package project.h58pic;

import kudu.KuduClientJava;
import org.apache.kudu.Schema;
import org.apache.kudu.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolsUtil.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static toolsUtil.CheckUtil.checkNumber;

public class H58pic_Img_Kudu extends KuduClientJava<H58pic_Img> {
    protected static final Logger LOG = LoggerFactory.getLogger(H58pic_Kudu.class);

    @Override
    public void testInsert(String tableName){
        String s2 ="realip:119.123.239.244 slbip:183.3.255.28 - - [23/Apr/2018:03:02:09 +0800] \"POST /index.php?m=zfbPay&a=wxNotify HTTP/1.1\" 200 47 \"http://www.58pic.com/index.php?m=sponsor&a=indexnew&f=4&t=3\" \"Mozilla/5.0 (Linux; U; Android 7.0; zh-cn; SM-G9350 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.3 Mobile Safari/537.36\" \"119.123.239.244\"\"-\" 0.015 0.060 \"1524422804\" \"2\" \"42792728\" \"1524423728\" \"af68493df68ff5e64af4ad403b03a5c5\"\n";
        H58pic_Img h58pic = dataProcess(s2);
        if(h58pic == null){
            System.out.println("insert failed");
            return;
        }
        h58pic.setLine_number(1);
        insertRowDataByDefault(h58pic, tableName);
    }

    @Override
    public H58pic_Img dataProcess(String originalStr) {
        H58pic_Img h58pic_img = new H58pic_Img();
        //([^\s]*)\s([^\s]*)\s-\s([^\s]*)\s(\[[^\[\]]+\])\s\"((?:[^"]|\")+|-)\"\s(\d{3})\s(\d+|-)\s\"((?:[^"])+|-)\"\s\"((?:[^"])+|-)\"\s\"(\d+\.\d+\.\d+\.\d+\,\s\d+\.\d+\.\d+\.\d+|\d+\.\d+\.\d+\.\d+|-)\"\"((?:[^"])+|-)\"\s(\d*\.\d*|\-)\s(\d*\.\d*|\-)\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"([a-zA-Z0-9]*|-)\"
//        String regex = "([^\\s]*)\\s([^\\s]*)\\s-\\s([^\\s]*)\\s(\\[[^\\[\\]]+\\])\\s\\\"((?:[^\"]|\\\")+|-)\\\"\\s(\\d{3})\\s(\\d+|-)\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\\"\\\"((?:[^\"])+|-)\\\"\\s(\\d*\\.\\d*|\\-)\\s(\\d*\\.\\d*|\\-)\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"([a-zA-Z0-9]*|-)\\\"";
        String regex = "realip:(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\sslbip:(\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\s-\\s([^\\s]*)\\s(\\[[^\\[\\]]+\\])\\s\\\"([^\\s]*)\\s([^\\s]*)\\s([^\\s]*)\\\"\\s(\\d{3})\\s(\\d+|-)\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\\"\\\"((?:[^\"])+|-)\\\"\\s(\\d*\\.\\d*|\\-)\\s(\\d*\\.\\d*|\\-)\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"([a-zA-Z0-9]*|-)\\\"";
        String regex_img_filter = "\\/\\S*\\/([0-9]*).html";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalStr);
        if (m.find()) {
            h58pic_img.setDate_time(TimeUtil.timeString2daytime(m.group(4)));
            if(checkNumber(m.group(18))){
                h58pic_img.setQt_uid(Integer.valueOf(m.group(18)));
            }else{
                return null;
            }
            if(m.group(6).contains("tupian")){
                return null;
            }
            Pattern pp = Pattern.compile(regex_img_filter);
            Matcher mm = pp.matcher(m.group(6));
            if(mm.find()&&!mm.group(1).isEmpty()){
                h58pic_img.setImg_id(mm.group(1));
            }else{
                return null;
            }
        }else{
            LOG.debug(originalStr);
            return null;
        }
        return h58pic_img;
    }

    public void scanSpecificColumn(String tableName) {
        List<String> projectColumns = new ArrayList<String>(1);
        projectColumns.add("date_time");
        projectColumns.add("line_number");
        projectColumns.add("qt_uid");
        projectColumns.add("img_id");
        try {
            KuduTable table = this.syncClient.openTable(tableName);
            Schema schema = table.getSchema();
            PartialRow partialRow = schema.newPartialRow();
            partialRow.addInt("date_time", 20180423);
            partialRow.addInt("line_number",0);
            PartialRow partialRow1 = schema.newPartialRow();
            partialRow1.addInt("date_time", 20180423);
            partialRow1.addInt("line_number",10);
            KuduScanner scanner = this.syncClient.newScannerBuilder(table)
                    .setProjectedColumnNames(projectColumns)//指定输出列
                    .lowerBound(partialRow)//指定下限（包含）
                    .exclusiveUpperBound(partialRow1)//指定上限（不包含）
                    .build();
            while (scanner.hasMoreRows()) {
                for (RowResult row : scanner.nextRows()) {
                    System.out.println("打开的表是："+tableName);
                    System.out.println("date_time: " + row.getInt("daytime"));
                    System.out.println("line_number: " + row.getInt("line_number"));
                    System.out.println("qt_uid: " + row.getDouble("qt_uid"));
                    System.out.println("img_id: " + row.getDouble("img_id"));
                    System.out.println("--------------------------------------------------------------");
                }
            }
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }
}
