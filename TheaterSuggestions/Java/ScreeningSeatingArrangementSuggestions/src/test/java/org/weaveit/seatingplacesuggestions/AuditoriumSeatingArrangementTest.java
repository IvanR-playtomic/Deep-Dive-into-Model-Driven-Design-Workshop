package org.weaveit.seatingplacesuggestions;

import org.junit.jupiter.api.Test;
import org.weaveit.externaldependencies.auditoriumlayoutrepository.AuditoriumLayoutRepository;
import org.weaveit.externaldependencies.reservationsprovider.ReservationsProvider;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AuditoriumSeatingArrangementTest {

    @Test
    public void be_a_Value_Type() throws IOException {
        var auditoriumSeatingArrangements =
                new AuditoriumSeatingArrangements(new AuditoriumLayoutRepository(), new ReservationsProvider());

        String showIdWithoutReservationYet = "18";
        var auditoriumSeatingArrangementFirstInstance =
                auditoriumSeatingArrangements.findByShowId(showIdWithoutReservationYet);
        var auditoriumSeatingArrangementSecondInstance =
                auditoriumSeatingArrangements.findByShowId(showIdWithoutReservationYet);

        // Two different instances with same values should be equals
        assertThat(auditoriumSeatingArrangementSecondInstance).isEqualTo(auditoriumSeatingArrangementFirstInstance);

        // Should not mutate existing instance
        auditoriumSeatingArrangementSecondInstance.rows().values().iterator().next().seatingPlaces().iterator().next().allocate();
        assertThat(auditoriumSeatingArrangementSecondInstance).isEqualTo(auditoriumSeatingArrangementFirstInstance);
    }
}