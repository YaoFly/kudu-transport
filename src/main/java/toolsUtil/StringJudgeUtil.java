/**
 * Copyright (C), 2015-2018, 韩创科技有限公司
 * FileName: StringJudgeUtil
 * Author:   kevin
 * Date:     2018/3/21 11:25
 * Description: 字符串判断
 */
package toolsUtil;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class StringJudgeUtil {

    public static void main(String[] args) {

    }

    /**
     *  这里是判断String字符串是否为整数
     * @param str
     * @return true/false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     *  这里是判断String字符串是否为short类型的request_status
     * @param str
     * @return true/false
     */
    public static boolean isHTTPShort(String str) {
        Pattern pattern = Pattern.compile("^[\\d]{1,3}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 这里是为了使Double类型数字保留3位小数的
     * @param doubleString 输入的小数
     * @return
     */
    public static String doubleFormat(String doubleString) {
        double d = -1.0;
        d = Double.parseDouble(doubleString);
        BigDecimal bg = new BigDecimal(d);
        d = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(d);
    }

}
