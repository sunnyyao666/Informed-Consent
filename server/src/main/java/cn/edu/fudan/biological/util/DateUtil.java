package cn.edu.fudan.biological.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: biological
 * @description: 日期工具
 * @author: Shen Zhengyu
 * @create: 2021-03-14 18:47
 **/
public class DateUtil {
    /**
     * @Description: 将类似2021.1.1的字符串转为日期
     * @Param: [dateStr]
     * @return: java.util.Date
     * @Author: Shen Zhengyu
     * @Date: 2021/3/14
     */

    public static Date StringToDate(String dateStr) {
        dateStr.replace(".", "-");
        DateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
