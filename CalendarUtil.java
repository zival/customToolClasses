package org.tongwoo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *  calendarUtil version 1.2 modify content:
 *      1、skipLatelyTime method 取最近一个整点时间
 *      2、add limit method
 *
* */
public class CalendarUtil {
    private final SimpleDateFormat YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat normalYMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");
    private final SimpleDateFormat YMDH = new SimpleDateFormat("yyyy-MM-dd HH");
    private final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat HMS = new SimpleDateFormat("HH:mm:ss");
    private Calendar calendar;

    public CalendarUtil(){
        this.calendar = Calendar.getInstance();
    }

    /*
        base sub and add operation contain a series of year,month,day,hour,minute,second
     */
    public CalendarUtil subOrAddMonth(Date setTime, int changeMonth){
        calendar.setTime(setTime);
        calendar.add(Calendar.MONTH, changeMonth);
        return this;
    }

    public CalendarUtil subOrAddDay(Date setTime, int changeDay){
        calendar.setTime(setTime);
        calendar.add(Calendar.DATE, changeDay);
        return this;
    }

    public CalendarUtil subOrAddHour(Date setTime, int changeHour){
        calendar.setTime(setTime);
        calendar.add(Calendar.HOUR_OF_DAY, changeHour);        //HOUR_OF_DAY 24小时制    HOUR 12小时制
        return this;
    }

    public CalendarUtil subOrAddMinute(Date setTime, int changeMinute){
        calendar.setTime(setTime);
        calendar.add(Calendar.MINUTE, changeMinute);        //HOUR_OF_DAY 24小时制    HOUR 12小时制
        return this;
    }

    public CalendarUtil skipHourWithToday(int scheduleHour) {
        calendar.set(Calendar.HOUR_OF_DAY, scheduleHour);
        return this;
    }

    public CalendarUtil skipMinuteWithToday(int scheduleMinute) {
        calendar.set(Calendar.MINUTE, scheduleMinute);
        return this;
    }

    public CalendarUtil skipDayWithMonth(int scheduleDay) {
        calendar.set(Calendar.DATE, scheduleDay);
        return this;
    }

    public CalendarUtil skipLatelyTime(int modulo) {
        int remainder = Integer.valueOf(this.getHour())%modulo;
        if(remainder!=0)
            this.subOrAddHour(this.getTime(), 1);
        String time = this.getYMDH(true);
        this.setTime(this.parseDate("YMDHMS", time));
        return this;
    }

    /*
        operation for date time
    */
    public CalendarUtil setTime(Date setTime){
        calendar.setTime(setTime);
        return this;
    }

    public Date getTime(){
        return calendar.getTime();
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public Calendar cloneThisUtilCalendar(){
        return (Calendar) calendar.clone();
    }


    /*
        format date return string
     */
    public String getYMDHMS(){
        return YMDHMS.format(calendar.getTime());
    }

    public String getYMDH(boolean isSupplement){
        if(isSupplement) return YMDH.format(calendar.getTime())+":00:00";
        return YMD.format(calendar.getTime());
    }

    public String getYMD(boolean isSupplement){
        if(isSupplement) return YMD.format(calendar.getTime())+" 00:00:00";
        return YMD.format(calendar.getTime());
    }

    public String getYear(){
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getMonth(){
        return String.valueOf(calendar.get(Calendar.MONTH) + 1);
    }

    public String getDay(){
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String getHour(){
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    public String getMinute(){
        return String.valueOf(calendar.get(Calendar.MINUTE));
    }

    public String getMaxDayOfMonth(){
        return String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    public Date parseDate(String sdf, String dateStr){
        Date date = null;
        try {
            if(sdf.equals("YMDHMS")) date = YMDHMS.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
