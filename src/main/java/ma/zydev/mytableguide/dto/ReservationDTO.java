package ma.zydev.mytableguide.dto;

import lombok.*;
import ma.zydev.mytableguide.domain.Reservation;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationDTO {
    private Long id;
    private String clientName;
    private String clientEmail;
    private int guests;
    private String status;
    private String reservationTime;
    private String tableLabel;
    private int tableCapacity;
    private String restaurantName;

    public static ReservationDTO fromEntity(Reservation r) {
        return ReservationDTO.builder()
                .id(r.getId())
                .clientName(r.getClientName())
                .clientEmail(r.getClientEmail())
                .guests(r.getGuests())
                .status(r.getStatus().name())
                .reservationTime(r.getReservationTime().toString())
                .tableLabel(r.getTable().getLabel())
                .tableCapacity(r.getTable().getCapacity())
                .restaurantName(r.getRestaurant().getName())
                .build();
    }
}
