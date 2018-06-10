package project.h58pic;

import kudu.KuduClientJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolsUtil.FileUtil;
import toolsUtil.TimeUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static toolsUtil.CheckUtil.checkNumber;

public class H58pic_Kudu extends KuduClientJava<H58pic> {
    protected static final Logger LOG = LoggerFactory.getLogger(H58pic_Kudu.class);

    @Override
    public void testInsert(String tableName){
        String s2 ="realip:119.123.239.244 slbip:183.3.255.28 - - [23/Apr/2018:03:02:09 +0800] \"POST /index.php?m=zfbPay&a=wxNotify HTTP/1.1\" 200 47 \"http://www.58pic.com/index.php?m=sponsor&a=indexnew&f=4&t=3\" \"Mozilla/5.0 (Linux; U; Android 7.0; zh-cn; SM-G9350 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.3 Mobile Safari/537.36\" \"119.123.239.244\"\"-\" 0.015 0.060 \"1524422804\" \"2\" \"42792728\" \"1524423728\" \"af68493df68ff5e64af4ad403b03a5c5\"\n";
        H58pic h58pic = dataProcess(s2);
        h58pic.setLine_number(1);
        insertRowDataByDefault(h58pic, tableName);
    }

    @Override
    public H58pic dataProcess(String originalStr) {
        H58pic h58pic = new H58pic();
        //([^\s]*)\s([^\s]*)\s-\s([^\s]*)\s(\[[^\[\]]+\])\s\"((?:[^"]|\")+|-)\"\s(\d{3})\s(\d+|-)\s\"((?:[^"])+|-)\"\s\"((?:[^"])+|-)\"\s\"(\d+\.\d+\.\d+\.\d+\,\s\d+\.\d+\.\d+\.\d+|\d+\.\d+\.\d+\.\d+|-)\"\"((?:[^"])+|-)\"\s(\d*\.\d*|\-)\s(\d*\.\d*|\-)\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"(\d*|-)\"\s\"([a-zA-Z0-9]*|-)\"
//        String regex = "([^\\s]*)\\s([^\\s]*)\\s-\\s([^\\s]*)\\s(\\[[^\\[\\]]+\\])\\s\\\"((?:[^\"]|\\\")+|-)\\\"\\s(\\d{3})\\s(\\d+|-)\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\\"\\\"((?:[^\"])+|-)\\\"\\s(\\d*\\.\\d*|\\-)\\s(\\d*\\.\\d*|\\-)\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"([a-zA-Z0-9]*|-)\\\"";
        String regex = "realip:(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\sslbip:(\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\s-\\s([^\\s]*)\\s(\\[[^\\[\\]]+\\])\\s\\\"([^\\s]*)\\s([^\\s]*)\\s([^\\s]*)\\\"\\s(\\d{3})\\s(\\d+|-)\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"((?:[^\"])+|-)\\\"\\s\\\"(\\d+\\.\\d+\\.\\d+\\.\\d+\\,\\s\\d+\\.\\d+\\.\\d+\\.\\d+|\\d+\\.\\d+\\.\\d+\\.\\d+|-)\\\"\\\"((?:[^\"])+|-)\\\"\\s(\\d*\\.\\d*|\\-)\\s(\\d*\\.\\d*|\\-)\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"(\\d*|-)\\\"\\s\\\"([a-zA-Z0-9]*|-)\\\"";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(originalStr);
        if (m.find()) {
            h58pic.setDate_time(TimeUtil.timeString2daytime(m.group(4)));
            h58pic.setHttp_x_forwarded_for(m.group(10));
            h58pic.setRemote_addr(m.group(2));
            h58pic.setRemote_user(m.group(3));
            h58pic.setTime_local(TimeUtil.timeString2unixtime(m.group(4)));
            h58pic.setRequest_method(m.group(5));
            h58pic.setRequest_path(m.group(6));
            h58pic.setRequest_version(m.group(7));
            h58pic.setStatus(Short.valueOf(m.group(8)));
            h58pic.setBody_bytes_sent(checkNumber(m.group(9))?Integer.valueOf(m.group(9)):0);
            h58pic.setHttp_referer(m.group(10));
            h58pic.setHttp_user_agent(m.group(11));
            h58pic.setUpstream_cache_status(m.group(13));
            h58pic.setUpstream_response_time(checkNumber(m.group(14))?Double.valueOf(m.group(14)):0D);
            h58pic.setRequest_time(checkNumber(m.group(15))?Double.valueOf(m.group(15)):0D);
            h58pic.setQt_createtime(checkNumber(m.group(16))?Integer.valueOf(m.group(16)):0);
            h58pic.setQt_type(m.group(17));
            h58pic.setQt_uid(checkNumber(m.group(18))?Integer.valueOf(m.group(18)):0);
            h58pic.setQt_utime(checkNumber(m.group(19))?Integer.valueOf(m.group(19)):0);
            h58pic.setQt_visitor_id(m.group(20));
        }else{
            LOG.debug(originalStr);
        }
        return h58pic;
    }
}
