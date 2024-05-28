package com.example.AdminInvoice.Service;

import com.example.AdminInvoice.Entity.*;
import com.example.AdminInvoice.InvoiceSubclass.ServiceAccountingCode;
import com.example.AdminInvoice.Payroll.AddedResponse;
import com.example.AdminInvoice.Payroll.DeleteResponse;
import com.example.AdminInvoice.Payroll.UpdateResponse;
import com.example.AdminInvoice.ReportFilter.DateFilter;
import com.example.AdminInvoice.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired private InvoiceDao invoiceDao;
    @Autowired private ServiceDao serviceDao;
    @Autowired private InvoiceTdsTaxDao invoiceTdsTaxDao;
    @Autowired private InvoiceGstTypeDao invoiceGstTypeDao;
    @Autowired private InvoicePaymentTermsDao invoicePaymentTermsDao;
    @Autowired private DateFilter dateFilter;

    @Override
    public AddedResponse<InvoiceDto> createInvoice(InvoiceDto invoice) throws Exception {
        invoice.setInvoiceDate(new Date());
        invoice.setLastModified(new Date());
        double retainerFee = 0.0;
        double serviceTotalAmount = 0.0;
        // Service Calculation
        for (ServiceAccountingCode service : invoice.getServicesList()) {
            Optional<Services> optional = serviceDao.findByserviceAccountingCode(service.getServiceAccountingCode());
            if (optional.isPresent()) {
                Services services = optional.get();
                double serviceAmount = services.getServiceAmount() * service.getServiceQty();
                service.setServiceTotalAmount(serviceAmount);
                serviceTotalAmount += serviceAmount;
                service.setId(services.getId());
            } else {
                throw new NoSuchFieldException("ServiceCode Not Found");
            }
        }
        // Apply discount
        double discountPercentage = invoice.getDiscountPercentage() / 100.0;
        double discountAmount = serviceTotalAmount * discountPercentage;
        double discountedServiceAmount = serviceTotalAmount - discountAmount;
        // Apply GST
        double gstAmount = 0.0;
        Optional<InvoiceGstType> invoiceGstType = invoiceGstTypeDao.findByGstName(invoice.getGstType());
        if (invoiceGstType.isPresent()) {
            InvoiceGstType gstType = invoiceGstType.get();
            double gstPercentage = gstType.getGstPercentage()/100.0;
            gstAmount += discountedServiceAmount * gstPercentage; // Calculate GST amount
            invoice.setGstPercentage(gstAmount);
        } else {
            throw new NoSuchFieldException("GST Type Not Found");
        }
        double totalAmount = discountedServiceAmount + gstAmount;

        // Calculate total TDS amount
        double tdsAmount = 0.0;
        TaxAmount taxAmount = invoice.getTaxAmount();
        if (taxAmount != null) {
            Optional<InvoiceTdsTax> tdsTaxOptional = invoiceTdsTaxDao.findByTaxName(taxAmount.getTds());
            if (tdsTaxOptional.isPresent()) {
                InvoiceTdsTax invoiceTdsTax = tdsTaxOptional.get();
                double taxRate = invoiceTdsTax.getTaxPercentage() / 100.0;
                tdsAmount = totalAmount * taxRate; // Accumulate TDS amount
            } else {
                throw new NoSuchFieldException("TDS Tax Name Not Found");
            }
        }

        // Calculate final retainer amount
        double finalRetainerAmount = totalAmount - tdsAmount;

        // Invoice type specific operations
        if ("retainer".equalsIgnoreCase(invoice.getInvoiceType())) {
            invoice.setRetainerFee(invoice.getRetainerFee());
            double totalRetainerAmount = finalRetainerAmount + invoice.getRetainerFee();
            invoice.setTotalAmount(totalRetainerAmount);
        } else if ("onetime".equalsIgnoreCase(invoice.getInvoiceType()) || "none".equalsIgnoreCase(invoice.getInvoiceType())) {
            invoice.setRetainerFee(retainerFee);
            invoice.setTotalAmount(finalRetainerAmount);
        } else {
            throw new IllegalArgumentException("Invalid invoice type");
        }
        //Payment Terms and dueDate Calculation
        String invoiceType = invoice.getInvoiceType();
        String paymentTerms=invoice.getPaymentTerms();
        if ("custom".equalsIgnoreCase(paymentTerms)) {
            invoice.setDueDate(invoice.getDueDate());
        } else {
            Optional<InvoicePaymentTerms> optionalInvoicePaymentTerms = invoicePaymentTermsDao.findByTermName(paymentTerms);
            if (optionalInvoicePaymentTerms.isPresent()) {
                InvoicePaymentTerms invoicePaymentTerms = optionalInvoicePaymentTerms.get();
                int invoiceDays = invoicePaymentTerms.getTotalDays();
                Date dueDate = calculateDueDate(invoice.getStartDate(), invoiceDays, invoiceType);
                invoice.setDueDate(dueDate);
            } else {
                throw new FileNotFoundException("Payment terms not found for invoice type: " + invoiceType);
            }
        }
        // Save the invoice to the database
        invoice.setTaxAmount(invoice.getTaxAmount());


        invoice.setInvoiceStatus(invoice.getInvoiceStatus());
        InvoiceDto invoiceDto= invoiceDao.save(invoice);
        return new AddedResponse<>(invoiceDto.getId(), "Invoice created successfully", HttpStatus.CREATED);
    }

    private Date calculateDueDate(Date startDate, int invoiceDays, String invoiceType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        switch (invoiceType.toLowerCase()) {
            case "retainer":
                switch (invoiceDays) {
                    case 30 -> calendar.add(Calendar.MONTH, 1);
                    case 90 -> calendar.add(Calendar.MONTH, 3);
                    case 365 -> calendar.add(Calendar.YEAR, 1);
                    default -> throw new IllegalArgumentException("Unsupported payment terms for retainer invoice: " + invoiceDays);
                }
                break;
            case "onetime":
                switch (invoiceDays) {
                    case 30 -> calendar.add(Calendar.MONTH, 1);
                    case 45 -> calendar.add(Calendar.DATE, 45);
                    case 0 -> calendar.add(Calendar.DATE, 0);
                    default -> throw new IllegalArgumentException("Unsupported payment terms for service invoice: " + invoiceDays);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported invoice type: " + invoiceType);
        }
        return calendar.getTime();
    }


    @Override
    public List<InvoiceDto> getAllInvoices() throws Exception {
        return invoiceDao.findAll();
    }

    @Override
    public UpdateResponse updateInvoice(String id, InvoiceDto invoiceDto) throws Exception {
        Optional<InvoiceDto> optional = invoiceDao.findById(id);
        if (optional.isPresent()) {
            InvoiceDto existingInvoice = optional.get();
            // Update invoice date and last modified date
            existingInvoice.setLastModified(new Date());

            // Calculate service total amount
            double serviceTotalAmount = 0.0;
            for (ServiceAccountingCode service : invoiceDto.getServicesList()) {
                Optional<Services> serviceAccountingCode = serviceDao.findByserviceAccountingCode(service.getServiceAccountingCode());
                if (serviceAccountingCode.isPresent()) {
                    Services services = serviceAccountingCode.get();
                    double serviceAmount = services.getServiceAmount() * service.getServiceQty();
                    service.setServiceTotalAmount(serviceAmount);
                    service.setId(services.getId());
                    serviceTotalAmount += serviceAmount;
                } else {
                    throw new NoSuchFieldException("ServiceCode Not Found");
                }
            }

            // Apply discount
            double discountPercentage = invoiceDto.getDiscountPercentage() / 100.0;
            double discountAmount = serviceTotalAmount * discountPercentage;
            double discountedServiceAmount = serviceTotalAmount - discountAmount;

            // Apply GST
            double gstAmount = 0.0;
            Optional<InvoiceGstType> invoiceGstType = invoiceGstTypeDao.findByGstName(invoiceDto.getGstType());
            if (invoiceGstType.isPresent()) {
                InvoiceGstType gstType = invoiceGstType.get();
                double gstPercentage = gstType.getGstPercentage();
                gstAmount = discountedServiceAmount * gstPercentage / 100.0;
                existingInvoice.setGstPercentage(gstPercentage);
                existingInvoice.setGstType(invoiceDto.getGstType());
            } else {
                throw new NoSuchFieldException("GST Type Not Found");
            }

            // Calculate total amount including GST
            double totalAmount = discountedServiceAmount + gstAmount;

            // Calculate total TDS amount
            double tdsAmount = 0.0;
            TaxAmount taxAmount = invoiceDto.getTaxAmount();
            if (taxAmount != null) {
                Optional<InvoiceTdsTax> tdsTaxOptional = invoiceTdsTaxDao.findByTaxName(taxAmount.getTds());
                if (tdsTaxOptional.isPresent()) {
                    InvoiceTdsTax invoiceTdsTax = tdsTaxOptional.get();
                    double taxRate = invoiceTdsTax.getTaxPercentage() / 100.0;
                    tdsAmount = totalAmount * taxRate; // Accumulate TDS amount
                } else {
                    throw new NoSuchFieldException("TDS Tax Name Not Found");
                }
            }

            // Calculate final retainer amount
            double finalRetainerAmount = totalAmount - tdsAmount;

            // Set retainer fee and total amount based on invoice type
            if ("retainer".equalsIgnoreCase(invoiceDto.getInvoiceType())) {
                existingInvoice.setRetainerFee(invoiceDto.getRetainerFee());
                double totalRetainerAmount = finalRetainerAmount + invoiceDto.getRetainerFee();
                existingInvoice.setTotalAmount(totalRetainerAmount);
            } else if ("onetime".equalsIgnoreCase(invoiceDto.getInvoiceType()) || "none".equalsIgnoreCase(invoiceDto.getInvoiceType())) {
                existingInvoice.setRetainerFee(0.0); // Assuming retainerFee is always zero for service or none invoices
                existingInvoice.setTotalAmount(finalRetainerAmount);
            } else {
                throw new IllegalArgumentException("Invalid invoice type");
            }

            // Set payment terms and due date
            String paymentTerms = invoiceDto.getPaymentTerms();
            if ("custom".equalsIgnoreCase(paymentTerms)) {
                existingInvoice.setDueDate(invoiceDto.getDueDate());
            } else {
                Optional<InvoicePaymentTerms> optionalInvoicePaymentTerms = invoicePaymentTermsDao.findByTermName(paymentTerms);
                if (optionalInvoicePaymentTerms.isPresent()) {
                    InvoicePaymentTerms invoicePaymentTerms = optionalInvoicePaymentTerms.get();
                    int invoiceDays = invoicePaymentTerms.getTotalDays();
                    Date dueDate = calculateDueDate(existingInvoice.getStartDate(), invoiceDays, invoiceDto.getInvoiceType());
                    existingInvoice.setDueDate(dueDate);
                } else {
                    throw new FileNotFoundException("Payment terms not found for invoice type: " + invoiceDto.getInvoiceType());
                }
            }

            // Update other invoice details
            existingInvoice.setPaymentTerms(invoiceDto.getPaymentTerms());
            existingInvoice.setInvoiceType(invoiceDto.getInvoiceType());
            existingInvoice.setInvoiceNumber(invoiceDto.getInvoiceNumber());
            existingInvoice.setCustomerName(invoiceDto.getCustomerName());
            existingInvoice.setNotes(invoiceDto.getNotes());
            existingInvoice.setTermsAndConditions(invoiceDto.getTermsAndConditions());
            existingInvoice.setInvoiceStatus(invoiceDto.getInvoiceStatus());
            existingInvoice.setTaxAmount(invoiceDto.getTaxAmount());
            existingInvoice.setDiscountPercentage(invoiceDto.getDiscountPercentage());
            invoiceDao.save(existingInvoice);
            return new UpdateResponse(existingInvoice.getId(), "Invoice updated successfully", HttpStatus.OK);
        } else {
            return new UpdateResponse(id, "Failed to update invoice: Invoice not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public DeleteResponse deleteInvoice(String id) throws Exception {
        Optional<InvoiceDto> optional = invoiceDao.findById(id);
        if (optional.isPresent()) {
            invoiceDao.deleteById(id);
            return new DeleteResponse(id, "Invoice deleted", HttpStatus.OK);
        } else {
            return new DeleteResponse(id, "Invalid ID: Invoice not found", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public InvoiceDto getInvoice(String id) throws Exception {
        Optional<InvoiceDto> optional = invoiceDao.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NoSuchFieldException();
        }
    }


    public List<ARsummaryDueDateFilter> reportARSummary(ReportARSummary reportARSummary) throws Exception {
        String invoiceDateFilter = reportARSummary.getInvoiceDate();
        if (invoiceDateFilter == null || invoiceDateFilter.isEmpty()) {
            throw new IllegalArgumentException("Invoice date filter is empty");
        }
        // Get the list of invoices based on the selected date filter
        List<InvoiceDto> invoices = switch (invoiceDateFilter.toLowerCase()) {
            case "today" -> dateFilter.getInvoicesForToday();
            case "this week" -> dateFilter.getInvoicesForThisWeek();
            case "this month" -> dateFilter.getInvoicesForThisMonth();
            case "this quarter" -> dateFilter.getInvoicesForThisQuarter();
            case "this year" -> dateFilter.getInvoicesForThisYear();
            case "yesterday" -> dateFilter.getInvoicesForYesterday();
            case "previous week" -> dateFilter.getInvoicesForPreviousWeek();
            case "previous month" -> dateFilter.getInvoicesForPreviousMonth();
            case "previous quarter" -> dateFilter.getInvoicesForPreviousQuarter();
            case "previous year" -> dateFilter.getInvoicesForPreviousYear();
            case "last 7 days" -> dateFilter.getInvoicesForLast7Days();
            case "last 30 days" -> dateFilter.getInvoicesForLast30Days();
            case "custom" -> {
                if (reportARSummary.getStartDate() == null || reportARSummary.getEndDate() == null) {
                    throw new IllegalArgumentException("Start date or end date is missing for custom filter");
                }
                yield dateFilter.getInvoicesBetweenDates(reportARSummary.getStartDate(), reportARSummary.getEndDate());
            }
            default -> throw new IllegalArgumentException("Invalid filter: " + reportARSummary.getInvoiceDate());
        };

        // Initialize a map to store summary information for each customer
        Map<String, ARsummaryDueDateFilter> summaryMap = new HashMap<>();

        // Calculate total amount for each aging category and store it in the map
        for (InvoiceDto invoice : invoices) {
            long daysPastDue = dateFilter.calculateDaysPastDue(invoice.getDueDate());

            // Determine the aging category
            String agingCategory;
            if (daysPastDue <= 30) {
                agingCategory = "0 to 30 days";
            } else if (daysPastDue <= 45) {
                agingCategory = "31 to 45 days";
            } else {
                agingCategory = "Above 45 days";
            }
            // Update or create summary filter for the customer
            ARsummaryDueDateFilter summaryFilter = summaryMap.getOrDefault(invoice.getCustomerName(), new ARsummaryDueDateFilter());
            summaryFilter.setCustomerName(invoice.getCustomerName());
            summaryFilter.setId(invoice.getId());
            summaryFilter.setTotal(summaryFilter.getTotal() + invoice.getTotalAmount());
            // Update the total for the respective aging category
            switch (agingCategory) {
                case "0 to 30 days":
                    summaryFilter.setDays0to30(summaryFilter.getDays0to30() + invoice.getTotalAmount());
                    break;
                case "31 to 45 days":
                    summaryFilter.setDays30to45(summaryFilter.getDays30to45() + invoice.getTotalAmount());
                    break;
                case "Above 45 days":
                    summaryFilter.setAbove45(summaryFilter.getAbove45() + invoice.getTotalAmount());
                    break;
            }
            summaryMap.put(invoice.getCustomerName(), summaryFilter);
        }
        return new ArrayList<>(summaryMap.values());
    }

    @Override
    public List<InvoiceDto> reportInvoiceSummary(ReportInvoiceSummary reportInvoiceSummary) throws Exception {
        String invoiceDateFilter = reportInvoiceSummary.getInvoiceDate();
        if (invoiceDateFilter == null || invoiceDateFilter.isEmpty()) {
            throw new IllegalArgumentException("Invoice date filter is empty");
        }
        return switch (invoiceDateFilter.toLowerCase()) {
            case "today" -> dateFilter.getInvoicesForToday();
            case "this week" -> dateFilter.getInvoicesForThisWeek();
            case "this month" -> dateFilter.getInvoicesForThisMonth();
            case "this quarter" -> dateFilter.getInvoicesForThisQuarter();
            case "this year" -> dateFilter.getInvoicesForThisYear();
            case "yesterday" -> dateFilter.getInvoicesForYesterday();
            case "previous week" -> dateFilter.getInvoicesForPreviousWeek();
            case "previous month" -> dateFilter.getInvoicesForPreviousMonth();
            case "previous quarter" -> dateFilter.getInvoicesForPreviousQuarter();
            case "previous year" -> dateFilter.getInvoicesForPreviousYear();
            case "last 7 days" -> dateFilter.getInvoicesForLast7Days();
            case "last 30 days" -> dateFilter.getInvoicesForLast30Days();
            case "custom" -> {
                if (reportInvoiceSummary.getStartDate() == null || reportInvoiceSummary.getEndDate() == null) {
                    throw new IllegalArgumentException("Start date or end date is missing for custom filter");
                }
                yield dateFilter.getInvoicesBetweenDates(reportInvoiceSummary.getStartDate(), reportInvoiceSummary.getEndDate());
            }
            default -> throw new IllegalArgumentException("Invalid filter: " + reportInvoiceSummary.getInvoiceDate());
        };
    }


    @Override
    public List<InvoiceDto> customizeARSummary(CustomizeReportARSummary customizeReportARSummary) throws Exception {
        String invoiceDateFilter = customizeReportARSummary.getInvoiceDate();
        if (invoiceDateFilter != null) {
            List<InvoiceDto> filteredInvoices = switch (invoiceDateFilter.toLowerCase()) {
                case "today" -> dateFilter.getInvoicesForToday();
                case "this week" -> dateFilter.getInvoicesForThisWeek();
                case "this month" -> dateFilter.getInvoicesForThisMonth();
                case "this quarter" -> dateFilter.getInvoicesForThisQuarter();
                case "this year" -> dateFilter.getInvoicesForThisYear();
                case "yesterday" -> dateFilter.getInvoicesForYesterday();
                case "previous week" -> dateFilter.getInvoicesForPreviousWeek();
                case "previous month" -> dateFilter.getInvoicesForPreviousMonth();
                case "previous quarter" -> dateFilter.getInvoicesForPreviousQuarter();
                case "previous year" -> dateFilter.getInvoicesForPreviousYear();
                case "last 7 days" -> dateFilter.getInvoicesForLast7Days();
                case "last 30 days" -> dateFilter.getInvoicesForLast30Days();
                default -> throw new IllegalArgumentException("Invalid filter: " + invoiceDateFilter);
            };

            List<InvoiceDto> resultList = new ArrayList<>(filteredInvoices);

            Date invoiceDueDate = customizeReportARSummary.getAgingBy().getInvoiceDueDate();
            if (invoiceDueDate != null) {
                filterByDueDate(resultList, invoiceDueDate);
            }

            // Apply invoice date filter if available
            Date invoiceDate = customizeReportARSummary.getAgingBy().getDate();
            if (invoiceDate != null) {
                filterByDate(resultList, invoiceDate);
            }

            String customerName = customizeReportARSummary.getCustomerName();
            if (customerName != null) {
                filterByCustomerName(resultList, customerName);

            }
            return resultList; // Return the filtered list
        } else {
            throw new Exception("Invoice date filter is null");
        }
    }

    private void filterByCustomerName(List<InvoiceDto> invoices, String customerName) {
        invoices.removeIf(invoice -> !invoice.getCustomerName().equals(customerName));
    }

    // Method to filter invoices based on invoice due date
    private List<InvoiceDto> filterByDueDate(List<InvoiceDto> invoices, Date invoiceDueDate) {
        invoices.removeIf(invoice -> !invoice.getDueDate().equals(invoiceDueDate));
        return invoices;
    }

    // Method to filter invoices based on invoice date
    private List<InvoiceDto> filterByDate(List<InvoiceDto> invoices, Date invoiceDate) {
        invoices.removeIf(invoice -> !invoice.getInvoiceDate().equals(invoiceDate));
        return invoices;
    }

    @Override
    public List<InvoiceDto> CustomizeInvoiceSummary(CustomizeReportInvoiceSummary customizeReportInvoiceSummary) throws Exception {
        String dateRange = customizeReportInvoiceSummary.getDateRange();
        if (dateRange != null) {
            List<InvoiceDto> filteredInvoices = switch (dateRange.toLowerCase()) {
                case "today" -> dateFilter.getInvoicesForToday();
                case "this week" -> dateFilter.getInvoicesForThisWeek();
                case "this month" -> dateFilter.getInvoicesForThisMonth();
                case "this quarter" -> dateFilter.getInvoicesForThisQuarter();
                case "this year" -> dateFilter.getInvoicesForThisYear();
                case "yesterday" -> dateFilter.getInvoicesForYesterday();
                case "previous week" -> dateFilter.getInvoicesForPreviousWeek();
                case "  month" -> dateFilter.getInvoicesForPreviousMonth();
                case "previous quarter" -> dateFilter.getInvoicesForPreviousQuarter();
                case "previous year" -> dateFilter.getInvoicesForPreviousYear();
                default -> throw new IllegalArgumentException("Invalid filter: " + dateRange);
            };
            List<InvoiceDto> resultLists = new ArrayList<>(filteredInvoices);

            Date invoiceDueDate = customizeReportInvoiceSummary.getRepostsByCustomizeSummary().getInvoiceDueDate();
            if (invoiceDueDate != null) {
                filterDueDate(resultLists, invoiceDueDate);
            }

            // Apply invoice date filter if available
            Date invoiceDate = customizeReportInvoiceSummary.getRepostsByCustomizeSummary().getInvoiceDate();
            if (invoiceDate != null) {
                filterDate(resultLists, invoiceDate);
            }

            String customerName = customizeReportInvoiceSummary.getCustomerName();
            if (customerName != null) {
                filterCustomerName(resultLists, customerName);
            }

            String invoiceStatus=customizeReportInvoiceSummary.getInvoiceStatus();
            if(invoiceStatus!=null){
                filterStatus(resultLists,invoiceStatus);
            }
            return resultLists; // Return the filtered list
        } else {
            throw new Exception("Invoice date filter is null");
        }
    }

    // Method to filter invoices based on customer name
    private void filterCustomerName(List<InvoiceDto> invoices, String customerName) {
        invoices.removeIf(invoice -> !invoice.getCustomerName().equals(customerName));
    }

    // Method to filter invoices based on invoice due date
    private void filterDueDate(List<InvoiceDto> invoices, Date invoiceDueDate) {
        invoices.removeIf(invoice -> !invoice.getDueDate().equals(invoiceDueDate));
    }

    // Method to filter invoices based on invoice date
    private void filterDate(List<InvoiceDto> invoices, Date invoiceDate) {
        invoices.removeIf(invoice -> !invoice.getInvoiceDate().equals(invoiceDate));
    }
    private void filterStatus(List<InvoiceDto> invoices, String invoiceStatus) {
        invoices.removeIf(invoice -> !invoice.getInvoiceStatus().equals(invoiceStatus));
    }

}
