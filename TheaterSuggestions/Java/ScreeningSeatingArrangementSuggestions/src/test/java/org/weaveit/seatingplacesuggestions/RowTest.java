package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class RowTest {
    @Test
    public void Be_a_Value_Type() {
        SeatingPlace firstInstance = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace secondInstance = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);

        var row1 = new Row("A", List.of(firstInstance, secondInstance));
        var row2 = new Row("A", List.of(firstInstance, secondInstance));

        assertThat(row1).isEqualTo(row2);

        row1.seatingPlaces().get(0).allocate();
        assertThat(row1).isEqualTo(row2);
    }

    @Test
    public void offer_seats_from_the_middle_of_the_row_when_the_row_size_is_even_and_party_size_is_greater_than_one() {
        int partySize = 2;

        SeatingPlace a1 = new SeatingPlace("A", 1, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a2 = new SeatingPlace("A", 2, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a3 = new SeatingPlace("A", 3, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a4 = new SeatingPlace("A", 4, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a5 = new SeatingPlace("A", 5, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a6 = new SeatingPlace("A", 6, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a7 = new SeatingPlace("A", 7, PricingCategory.FIRST, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a8 = new SeatingPlace("A", 8, PricingCategory.FIRST, SeatingPlaceAvailability.RESERVED);
        SeatingPlace a9 = new SeatingPlace("A", 9, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);
        SeatingPlace a10 = new SeatingPlace("A", 10, PricingCategory.SECOND, SeatingPlaceAvailability.AVAILABLE);

        Row row = new Row("A", new ArrayList<>(Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)));
        List<SeatingPlace> seatingPlaces = offerSeatsNearerTheMiddleOfTheRow(row).stream().limit(partySize).collect(Collectors.toList());
        assertThat(seatingPlaces).containsExactly(a5, a6);
    }

    // Deep Modeling: probing the code should start with a prototype.
    public List<SeatingPlace> offerSeatsNearerTheMiddleOfTheRow(Row row) {
        var middle = row.seatingPlaces().size() / 2;

        return List.of(getPlace(row, middle), getPlace(row, middle + 1));
    }

    private static SeatingPlace getPlace(Row row, int index) {
        return row.seatingPlaces().stream().filter(place -> place.number() == index).findFirst().get();
    }
}