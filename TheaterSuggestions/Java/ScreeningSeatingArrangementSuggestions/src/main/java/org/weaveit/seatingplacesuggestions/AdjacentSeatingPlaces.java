package org.weaveit.seatingplacesuggestions;

import java.util.Comparator;
import java.util.List;

public record AdjacentSeatingPlaces(
        List<SeatingPlaceDistanceToCenter> suggestedSeats
) {
    public double calculateDistance() {
        var list = suggestedSeats.stream().sorted(Comparator.comparingDouble(SeatingPlaceDistanceToCenter::distanceToCenter)).toList();
        return list.get(0).distanceToCenter();
    }

    public boolean areAdjacents() {
        if (suggestedSeats == null || suggestedSeats.size() <= 1) {
            return true;
        }

        List<Integer> numbers = suggestedSeats.stream()
                .map(s -> s.seatingPlace().number())
                .sorted()
                .toList();

        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i + 1) - numbers.get(i) != 1) {
                return false;
            }
        }
        return true;
    }
}
