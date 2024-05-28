package com.example.AdminInvoice.Entity;

import com.example.AdminInvoice.InvoiceSubclass.DashboardReportAbove45Day;
import com.example.AdminInvoice.InvoiceSubclass.DashboardReportCurrentDay;
import com.example.AdminInvoice.InvoiceSubclass.DashboardReportOverdueDay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardReport {
    @Id
    private String id;
    private DashboardReportCurrentDay currentDays;
    private DashboardReportOverdueDay overdueDays;
    private DashboardReportAbove45Day above45Days;
    private Double totalReceivables;
}
