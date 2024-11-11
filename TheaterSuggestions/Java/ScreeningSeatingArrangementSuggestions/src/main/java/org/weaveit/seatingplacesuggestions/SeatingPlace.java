package org.weaveit.seatingplacesuggestions;

public record SeatingPlace(
        String rowName,
        int number,
        PricingCategory pricingCategory,
        SeatingPlaceAvailability seatingPlaceAvailability
) {
    public boolean isAvailable() {
        return seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE;
    }

    public boolean matchCategory(PricingCategory pricingCategory) {
        return this.pricingCategory == pricingCategory || pricingCategory == PricingCategory.IGNORED;
    }

    public SeatingPlace allocate() {
        if (seatingPlaceAvailability == SeatingPlaceAvailability.AVAILABLE)
            return new SeatingPlace(
                    this.rowName,
                    this.number,
                    this.pricingCategory,
                    SeatingPlaceAvailability.ALLOCATED
            );
        else {
            return this;
        }
    }

    @Override
    public String toString() {
        return rowName + number;
    }
}
