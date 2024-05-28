package com.example.AdminInvoice.ReportFilter;

import com.example.AdminInvoice.Entity.InvoiceDto;
import com.example.AdminInvoice.Repository.InvoiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DateFilter {
    @Autowired
    private InvoiceDao invoiceDao;


    public List<InvoiceDto> getInvoicesForLast7Days() {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -7); // Subtract 7 days from today
        Date startDate = calendar.getTime();

        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForLast30Days() {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, -30); // Subtract 30 days from today
        Date startDate = calendar.getTime();

        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForToday() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate,endDate);
    }

    public List<InvoiceDto> getInvoicesForThisWeek() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForThisMonth() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForThisQuarter() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int currentMonth = calendar.get(Calendar.MONTH);
        int startMonthOfQuarter = currentMonth / 3 * 3;
        calendar.set(Calendar.MONTH, startMonthOfQuarter);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForThisYear() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForYesterday() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForPreviousWeek() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForPreviousMonth() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForPreviousQuarter() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int currentMonth = calendar.get(Calendar.MONTH);
        int startMonthOfPreviousQuarter = (currentMonth / 3 - 1) * 3;
        calendar.set(Calendar.MONTH, startMonthOfPreviousQuarter);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<InvoiceDto> getInvoicesForPreviousYear() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }


    public List<InvoiceDto> getInvoicesBetweenDates(Date startDate, Date endDate) {
        return invoiceDao.findByInvoiceDateBetween(startDate, endDate);
    }


    public long calculateDaysPastDue(Date dueDate) {
        // Get today's date
        Date currentDate = new Date();

        // Calculate the difference in milliseconds between the due date and today's date
        long differenceMillis = currentDate.getTime() - dueDate.getTime();

        // Convert milliseconds to days
        long daysPastDue = differenceMillis / (1000 * 60 * 60 * 24);

        // Return the number of days past due
        return daysPastDue;
    }

}
