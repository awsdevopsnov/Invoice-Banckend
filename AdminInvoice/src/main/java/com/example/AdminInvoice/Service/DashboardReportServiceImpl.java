package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.DashboardReport;
import com.example.AdminInvoice.Entity.InvoiceDto;
import com.example.AdminInvoice.InvoiceSubclass.DashboardReportAbove45Day;
import com.example.AdminInvoice.InvoiceSubclass.DashboardReportCurrentDay;
import com.example.AdminInvoice.InvoiceSubclass.DashboardReportOverdueDay;
import com.example.AdminInvoice.ReportFilter.DateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DashboardReportServiceImpl implements DashboardReportService{
    @Autowired private DateFilter dateFilter;
    @Autowired private InvoiceService invoiceService;

        @Override
        public DashboardReport DASHBOARD_REPORTS () throws Exception {
            // Get all invoices
            List<InvoiceDto> invoices = invoiceService.getAllInvoices();

            // Initialize variables to hold totals and counts for each category
            double currentDaysTotalAmount = 0.0;
            long currentDaysNoOfCustomers = 0;
            long currentDaysNoOfInvoices = 0;

            double overdueDaysTotalAmount = 0.0;
            long overdueDaysNoOfCustomers = 0;
            long overdueDaysNoOfInvoices = 0;

            double above45DaysTotalAmount = 0.0;
            long above45DaysNoOfCustomers = 0;
            long above45DaysNoOfInvoices = 0;

            double overallTotal = 0.0;

            Set<String> currentDaysCustomers = new HashSet<>();
            Set<String> overdueDaysCustomers = new HashSet<>();
            Set<String> above45DaysCustomers = new HashSet<>();

            // Calculate total amounts for each aging category and overall total
            for (InvoiceDto invoice : invoices) {
                long daysPastDue = dateFilter.calculateDaysPastDue(invoice.getDueDate());

                if (daysPastDue <= 30) {
                    currentDaysTotalAmount += invoice.getTotalAmount();
                    currentDaysNoOfInvoices++;
                    currentDaysCustomers.add(invoice.getCustomerName());
                } else if (daysPastDue <= 45) {
                    overdueDaysTotalAmount += invoice.getTotalAmount();
                    overdueDaysNoOfInvoices++;
                    overdueDaysCustomers.add(invoice.getCustomerName());
                } else {
                    above45DaysTotalAmount += invoice.getTotalAmount();
                    above45DaysNoOfInvoices++;
                    above45DaysCustomers.add(invoice.getCustomerName());
                }
                overallTotal += invoice.getTotalAmount();
            }

            currentDaysNoOfCustomers = currentDaysCustomers.size();
            overdueDaysNoOfCustomers = overdueDaysCustomers.size();
            above45DaysNoOfCustomers = above45DaysCustomers.size();

            // Create and set current day report
            DashboardReportCurrentDay currentDayReport = new DashboardReportCurrentDay();
            currentDayReport.setTotalAmount(currentDaysTotalAmount);
            currentDayReport.setNoOfCustomers(currentDaysNoOfCustomers);
            currentDayReport.setNoOfInvoices(currentDaysNoOfInvoices);

            // Create and set overdue day report
            DashboardReportOverdueDay overdueDayReport = new DashboardReportOverdueDay();
            overdueDayReport.setTotalAmount(overdueDaysTotalAmount);
            overdueDayReport.setNoOfCustomers(overdueDaysNoOfCustomers);
            overdueDayReport.setNoOfInvoices(overdueDaysNoOfInvoices);

            // Create and set above 45 day report
            DashboardReportAbove45Day above45DayReport = new DashboardReportAbove45Day();
            above45DayReport.setTotalAmount(above45DaysTotalAmount);
            above45DayReport.setNoOfCustomers(above45DaysNoOfCustomers);
            above45DayReport.setNoOfInvoices(above45DaysNoOfInvoices);

            // Create a DashboardReport instance and set calculated values
            DashboardReport dashboardReport = new DashboardReport();
            dashboardReport.setId(UUID.randomUUID().toString());
            dashboardReport.setCurrentDays(currentDayReport);
            dashboardReport.setOverdueDays(overdueDayReport);
            dashboardReport.setAbove45Days(above45DayReport);
            dashboardReport.setTotalReceivables(overallTotal);

            return dashboardReport;
        }

    }


