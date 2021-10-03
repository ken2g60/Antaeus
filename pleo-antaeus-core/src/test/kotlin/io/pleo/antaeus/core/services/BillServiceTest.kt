package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.BillNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BillServiceTest {
    private val dal = mockk<AntaeusDal> {
        every { fetchBill(404)} returns null
    }

    private val fetchBill = mockk<AntaeusDal> {
        every { fetchBills(9)  }
    }



    // check for multiple creates
    private val createBill = mockk<AntaeusDal> {
        val paymentProvider = object : PaymentProvider {
            override fun charge(invoice: Invoice): Boolean {
              return true
            }
        }

        // billService
        val billingService = BillingService(paymentProvider = paymentProvider, dal = this)
        every { billingService.generateMonthlyBills() }
    }
    // @Test
    // fun `will throw an error with`() {
    //     assertThrows<BillNotFoundException>() {
    //         // createBill.fetchBill(404)
    //         fetchBill
    //     }
    // }
}