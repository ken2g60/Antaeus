package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Bill
import io.pleo.antaeus.models.Invoice
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors
import kotlin.math.log

class BillingService(private val paymentProvider: PaymentProvider, private val dal: AntaeusDal) {
    // TODO - Add code e.g. here
    // setup the schedule every month
    // method fetch all invoices
    // check for duplicates of bills
    // technically iterate over the invoice and store
    fun fetchAll(): List<Bill> {
        return dal.fetchAllBills()
    }

    fun generateMonthlyBills() {
        val invoices = InvoiceService(dal).fetchAll()
        val billsForMonth = getBills().stream().map(Bill::referenceId).collect(Collectors.toList())
        val invoicesWithoutBills = invoices.filter { !billsForMonth.contains(it.id) }
        invoicesWithoutBills.forEach {
            val bill = createBill(it)
            saveBill(bill)
        }
    }

    private fun createBill(data: Invoice): Bill {
        return Bill(data.id, data.customerId, data.id, data.status.name, "", data.amount.currency.name, getCurrentMonthInValue(), "")
    }

    private fun saveBill(bill: Bill) {
        dal.createBill(bill)
    }

    private fun getBills(): List<Bill> {
        return dal.fetchBills(getCurrentMonthInValue())
    }

    private fun getCurrentMonthInValue(): Int{
        return  LocalDate.now().monthValue
    }
}
