package org.weaveit.seatingplacesuggestions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record AuditoriumSeatingArrangement(
        Map<String, Row> rows
) {
    public SeatingOptionIsSuggested suggestSeatingOptionFor(int partyRequested, PricingCategory pricingCategory) {
        for (Row row : rows.values()) {
            var seatingOptionSuggested = row.suggestSeatingOption(partyRequested, pricingCategory);

            if (seatingOptionSuggested.matchExpectation()) {
                return seatingOptionSuggested;
            }
        }

        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public AuditoriumSeatingArrangement allocate(List<SeatingPlace> seatingPlaces) {
        Map<String, Row> newVersionOfRows = new HashMap<>(rows);

        seatingPlaces.forEach(updatedSeat ->
                newVersionOfRows.computeIfPresent(updatedSeat.rowName(),
                        (rowName, formerRow) -> formerRow.allocate(updatedSeat))
        );

        return new AuditoriumSeatingArrangement(newVersionOfRows);
    }
}
