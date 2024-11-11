package org.weaveit.seatingplacesuggestions;

import java.util.List;

public record Row(
        String name,
        List<SeatingPlace> seatingPlaces
) {

    public SeatingOptionIsSuggested suggestSeatingOption(int partyRequested, PricingCategory pricingCategory) {

        var seatAllocation = new SeatingOptionIsSuggested(partyRequested, pricingCategory);

        for (var seat : seatingPlaces) {
            if (seat.isAvailable() && seat.matchCategory(pricingCategory)) {
                seatAllocation.addSeat(seat);

                if (seatAllocation.matchExpectation())
                    return seatAllocation;

            }
        }
        return new SeatingOptionIsNotAvailable(partyRequested, pricingCategory);
    }

    public Row allocate(SeatingPlace seatingPlace) {
        var updatedSeatingPlaces = seatingPlaces.stream().map(place -> allocatedOrSame(place, seatingPlace)).toList();
        return new Row(name, updatedSeatingPlaces);
    }

    private SeatingPlace allocatedOrSame(SeatingPlace currentSeatingPlace, SeatingPlace updatedSeatingPlace) {
        if (currentSeatingPlace.number() == updatedSeatingPlace.number() && currentSeatingPlace.rowName().equals(updatedSeatingPlace.rowName())) {
            return currentSeatingPlace.allocate();
        } else {
            return currentSeatingPlace;
        }
    }
}
