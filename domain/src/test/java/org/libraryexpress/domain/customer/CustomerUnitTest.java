package org.libraryexpress.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.valueobject.Email;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
@DisplayName("Customer Entity - Unit Test")
public class CustomerUnitTest {

    @Test
    @DisplayName("Should register a customer when no ID is provided.")
    void shouldRegisterCustomer_whenNoIdIsProvided() {

        Customer customer = new Customer.Builder()
                .setName("John Doe")
                .setEmail("j.doe@test.com")
                .build();

        assertNotNull(customer.getId());
        assertFalse(customer.getId().isBlank());
        assertEquals("John Doe", customer.getName());
        assertEquals(new Email("j.doe@test.com"), customer.getEmail());
    }

    @Test
    @DisplayName("Should register a customer when ID is provided")
    void shouldRegisterCustomer_whenIdIsProvided() {

        String customId = "custom-uuid-123";

        Customer customer = new Customer.Builder()
                .setId(customId)
                .setName("John Doe")
                .setEmail("j.doe@test.com")
                .build();

        assertNotNull(customer.getId());
        assertFalse(customer.getId().isBlank());
        assertEquals(customId, customer.getId());
    }

    @Test
    @DisplayName("Should consider two customers equal when they have the same ID")
    void shouldConsiderTwoCustomersEqual_whenTheyHaveTheSameId() {

        String customId = "custom-uuid-123";

        Customer firstCustomer = new Customer.Builder()
                .setId(customId)
                .setName("John Doe")
                .setEmail("j.doe@test.com")
                .build();

        Customer secondCustomer = new Customer.Builder()
                .setId(customId)
                .setName("Jane Doe")
                .setEmail("jane.28@test.com")
                .build();

        assertEquals(firstCustomer, secondCustomer);
        assertEquals(firstCustomer.hashCode(), secondCustomer.hashCode());
    }

    @Test
    @DisplayName("Should sort customers by name when the order is alphabetical.")
    void shouldSortCustomersByName_WhenOrderIsAlphabetical() {

        Customer firstCustomer = new Customer.Builder()
                .setName("Samantha Smith")
                .setEmail("s.s@test.com")
                .build();

        Customer secondCustomer = new Customer.Builder()
                .setName("David Parker")
                .setEmail("dp.25@test.com")
                .build();

        assertFalse(firstCustomer.compareTo(secondCustomer) < 0); // Samantha comes after David (D...S)
        assertFalse(secondCustomer.compareTo(firstCustomer) > 0); // Samantha comes after David (D...S)
    }
}
