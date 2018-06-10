/**
 * Copyright (C), 2015-2018, 韩创科技有限公司
 * FileName: TimeUtil
 * Author:   kevin
 * Date:     2018/3/21 11:10
 * Description: 时间转换函数工具
 */
package toolsUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtil {

    public static void main(String[] args) {

    }

    /**
     * 此类是为了增加时间daytime格式
     * @param timeString
     * @return
     */
    public static Integer timeString2daytime(String timeString){
        SimpleDateFormat sdf = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss Z]", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String daytime = new String();
        try {
            daytime = sdf2.format(sdf.parse(timeString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.valueOf(daytime);
    }

    /**
     * 这里是为了修改时间格式，将原格式修改成long类型时间格式
     * @param timeString
     * @return
     */
    public static long timeString2unixtime(String timeString){
        SimpleDateFormat sdf = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss Z]", Locale.ENGLISH);
        long timelong = 0;
        try {
            timelong = sdf.parse(timeString).getTime() ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timelong;
    }


}
