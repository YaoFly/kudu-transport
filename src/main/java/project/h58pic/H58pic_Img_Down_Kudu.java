package project.h58pic;

import kudu.KuduClientJava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toolsUtil.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static toolsUtil.CheckUtil.checkNumber;

public class H58pic_Img_Down_Kudu extends KuduClientJava<H58pic_Img> {
    protected static final Logger LOG = LoggerFactory.getLogger(H58pic_Img_Down_Kudu.class);

    @Override
    public void testInsert(String tableName) {
        String s2 = "43200073\t25745722\t2018-05-02 18:53:53\n";
        H58pic_Img h58pic = dataProcess(s2);
        if (h58pic == null) {
            System.out.println("insert failed");
            return;
        }
        h58pic.setLine_number(1);
        insertRowDataByDefault(h58pic, tableName);
    }

    @Override
    public H58pic_Img dataProcess(String originalStr) {
        H58pic_Img h58pic_img = new H58pic_Img();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String[] str = originalStr.split("\t");
        if (checkNumber(str[0]) && checkNumber(str[1])) {
            h58pic_img.setQt_uid(Integer.valueOf(str[0]));
            h58pic_img.setImg_id(str[1]);
            Date date = new Date();
            date.setTime(Long.parseLong(str[2])*1000);
            h58pic_img.setDate_time(Integer.valueOf(sdf.format(date)));
        } else {
            return null;
        }
        return h58pic_img;
    }
}
