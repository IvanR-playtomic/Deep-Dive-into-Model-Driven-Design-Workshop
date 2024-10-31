package com.baasie.SeatsSuggestionsAcceptanceTests;

import com.baasie.ExternalDependencies.auditoriumlayoutrepository.AuditoriumDto;
import com.baasie.ExternalDependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import com.baasie.ExternalDependencies.auditoriumlayoutrepository.SeatDto;
import com.baasie.ExternalDependencies.reservationsprovider.ReservationsProvider;
import com.baasie.ExternalDependencies.reservationsprovider.ReservedSeatsDto;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;

class AuditoriumSeatingAdapter {

    private final ReservationsProvider reservedSeatsRepository;
    private final AuditoriumLayoutRepository auditoriumLayoutRepository;


    public AuditoriumSeatingAdapter(AuditoriumLayoutRepository auditoriumLayoutRepository, ReservationsProvider reservationsProvider) {
        this.auditoriumLayoutRepository = auditoriumLayoutRepository;
        this.reservedSeatsRepository = reservationsProvider;
    }

    public AuditoriumSeating getAuditoriumSeating(String showId) {
        return adapt(auditoriumLayoutRepository.getAuditoriumSeatingFor(showId),
                reservedSeatsRepository.getReservedSeats(showId));

    }

    private AuditoriumSeating adapt(AuditoriumDto auditoriumDto, ReservedSeatsDto reservedSeatsDto) {

        Map<String, Row> rows = new HashMap<>();

        for (Map.Entry<String, ImmutableList<SeatDto>> entry : auditoriumDto.getRows().entrySet()) {
            entry.getValue().forEach(seatDto -> {
                String rowName = extractRowName(seatDto.getName());
                int number = extractNumber(seatDto.getName());
                PricingCategory pricingCategory = convertCategory(seatDto.getCategory());

                boolean isReserved = reservedSeatsDto.getReservedSeats().contains(seatDto.getName());

                if (!rows.containsKey(rowName)) {
                    rows.put(rowName, new Row());
                }

                rows.get(rowName).seats().add(new Seat(rowName, number, pricingCategory,
                        isReserved ? SeatAvailability.Reserved : SeatAvailability.Available));
            });
        }

        return new AuditoriumSeating(rows);
    }

    private static PricingCategory convertCategory(int seatDtoCategory) {
        return PricingCategory.valueOf(seatDtoCategory);
    }

    private static int extractNumber(String name) {
        return Integer.parseUnsignedInt(name.substring(1));
    }

    private static String extractRowName(String name) {
        return Character.toString(name.charAt(0));
    }
}

