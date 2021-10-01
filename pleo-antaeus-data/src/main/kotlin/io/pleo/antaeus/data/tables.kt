/*
    Defines database tables and their schemas.
    To be used by `AntaeusDal`.
 */

package io.pleo.antaeus.data

import org.jetbrains.exposed.sql.Table

object InvoiceTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val currency = varchar("currency", 3)
    val value = decimal("value", 1000, 2)
    val customerId = reference("customer_id", CustomerTable.id)
    val status = text("status")
}

object CustomerTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val currency = varchar("currency", 3)
}

object BillScheduleTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val customerId = reference("customer_id", CustomerTable.id)
    val referenceId = reference("reference_id", InvoiceTable.id)
    val status = text("status")
    val paymentLink = text("payment_link")
    val currency = varchar("currency", 3)
    val targetMonth = integer("target_month")
    val datePaid = varchar("date_paid", 25)
}
