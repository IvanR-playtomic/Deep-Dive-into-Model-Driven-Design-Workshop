package org.weaveit.seatingplacesuggestions;

import java.util.Comparator;
import java.util.List;

public record DistanceOfParty(
        List<SeatingPlaceDistanceToCenter> suggestedSeats
) {
    public double calculateDistance() {
        var list = suggestedSeats.stream().sorted(Comparator.comparingDouble(SeatingPlaceDistanceToCenter::distanceToCenter)).toList();
        return list.get(0).distanceToCenter();
    }
}
