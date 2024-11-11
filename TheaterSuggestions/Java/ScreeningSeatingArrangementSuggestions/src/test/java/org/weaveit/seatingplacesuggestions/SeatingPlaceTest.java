package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class SeatingPlaceTest {

    @Test
    public void Be_a_Value_Type() {
        SeatingPlace firstInstance = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace secondInstance = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);

        // Two different instances with same values should be equals
        assertThat(secondInstance).isEqualTo(firstInstance);

        // Should not mutate existing instance
        secondInstance.allocate();
        assertThat(secondInstance).isEqualTo(firstInstance);
    }
}