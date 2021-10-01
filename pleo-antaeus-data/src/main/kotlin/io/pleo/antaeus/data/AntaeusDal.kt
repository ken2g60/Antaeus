/*
    Implements the data access layer (DAL).
    The data access layer generates and executes requests to the database.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.data.BillScheduleTable.autoIncrement
import io.pleo.antaeus.data.BillScheduleTable.primaryKey
import io.pleo.antaeus.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AntaeusDal(private val db: Database) {
    fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    fun fetchBill(id: Int): Bill? {
        return transaction(db){
            BillScheduleTable.select { BillScheduleTable.id.eq(id) }
                    .firstOrNull()
                    ?.toBill()
        }
    }

    fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }

    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }

        return fetchInvoice(id)
    }

    fun createBill(bill: Bill): Bill? {
        val id = transaction(db) {
            BillScheduleTable.insert {
                it[this.customerId] = bill.customerId
                it[this.referenceId] = bill.referenceId
                it[this.status] = bill.status
                it[this.paymentLink] = bill.paymentLink
                it[this.currency] = bill.currency
                it[this.targetMonth] = bill.targetMonth
                it[this.datePaid] = bill.datePaid
            } get BillScheduleTable.id
        }
        return fetchBill(id)
    }

    fun fetchCustomer(id: Int): Customer? {
        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    // fetch all bills
    fun fetchAllBills(): List<Bill> {
        return transaction(db) {
            BillScheduleTable.selectAll().map { it.toBill() }
        }
    }
    // fetch bill
    fun fetchBills(targetMonth: Int ): List<Bill> {
        return transaction(db){
            BillScheduleTable.select { BillScheduleTable.targetMonth.eq(targetMonth) }.map { it.toBill() }
        }
    }


    fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    fun createCustomer(currency: Currency): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
            } get CustomerTable.id
        }

        return fetchCustomer(id)
    }
}
