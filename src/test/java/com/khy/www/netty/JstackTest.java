package com.khy.www.netty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JstackTest {
    public static void main(String[] args) throws IOException {

        /*Thread thread = new Thread(new Worker());

        thread.start();*/

        /*String s = "{\"name\":\"小民\",\"age\":20,\"birthday\":844099200000,\"email\":\"xiaomin@sina.com\"}";
        Map<String, Object> auditModelList = new ObjectMapper().readValue(s, Map.class);
        System.out.println(auditModelList);
*/
//        System.out.println(BigDecimal.valueOf(Double.parseDouble("1554")));

        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2018-06-01 11:11:11", df);
        System.out.println(localDateTime);*/

//        System.out.println(String.format("客户申请失败 %s 。原因：%s", "a","b"));;
        LocalDateTime approveTime = GetTime(Long.parseLong("1528771222816"));
        System.out.println(approveTime);

    }

    /**
     * 获取时间字符串
     *
     * @param timeStamp
     * @return
     */
    public static LocalDateTime GetTime(Long timeStamp)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        Date date = c.getTime();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dft.format(date.getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return LocalDateTime.parse(time, formatter);
    }

    static class Worker implements Runnable {

        @Override

        public void run() {

            while (true) {

                System.out.println("Thread Name:" + Thread.currentThread().getName());

            }

        }

    }
}
