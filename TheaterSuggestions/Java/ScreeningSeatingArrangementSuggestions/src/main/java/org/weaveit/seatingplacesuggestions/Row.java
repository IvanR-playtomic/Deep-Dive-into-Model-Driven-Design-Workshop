package org.weaveit.seatingplacesuggestions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record Row(
        String name,
        List<SeatingPlace> seatingPlaces
) {
    private double middle() {
        return (double) seatingPlaces().size() / 2 + 0.5;
    }

    public SeatingOptionIsSuggested suggestSeatingOption(int partyRequested, PricingCategory pricingCategory) {

        var seatAllocation = new SeatingOptionIsSuggested(partyRequested, pricingCategory);

        var seatList = offerSeatsNearerTheMiddleOfTheRow(pricingCategory, partyRequested);
        seatAllocation.addSeats(seatList);
        if (seatAllocation.matchExpectation()) {
            return seatAllocation;
        }
        else
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

    // Deep Modeling: probing the code should start with a prototype.
    public List<SeatingPlace> offerSeatsNearerTheMiddleOfTheRow(PricingCategory pricingCategory, int partyRequested) {
        var seatingPlacesWithDistance = this.seatingPlaces().stream()
                .filter(SeatingPlace::isAvailable)
                .filter(place -> place.matchCategory(pricingCategory))
                .map(place -> new SeatingPlaceDistanceToCenter(place, Math.abs(place.number() - this.middle())))
                .toList();

        var listOfParties = splitList(seatingPlacesWithDistance, partyRequested)
                .stream()
                .map(AdjacentSeatingPlaces::new)
                .filter(AdjacentSeatingPlaces::areAdjacents)
                .sorted(Comparator.comparingDouble(AdjacentSeatingPlaces::calculateDistance))
                .toList();

        if (listOfParties.isEmpty()) return List.of();

        return listOfParties.get(0).suggestedSeats()
                .stream()
                .map(SeatingPlaceDistanceToCenter::seatingPlace)
                .toList();
    }

    public static <T> List<List<T>> splitList(List<T> list, int chunkSize) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(
                    i,
                    Math.min(list.size(), i + chunkSize)));
        }
        return chunks;
    }
}
