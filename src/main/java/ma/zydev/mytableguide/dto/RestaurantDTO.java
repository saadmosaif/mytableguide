package ma.zydev.mytableguide.dto;

import lombok.*;
import ma.zydev.mytableguide.domain.Restaurant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String openingHours;

    public static RestaurantDTO fromEntity(Restaurant r) {
        return RestaurantDTO.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .phone(r.getPhone())
                .email(r.getEmail())
                .openingHours(r.getOpeningHours())
                .build();
    }
}
