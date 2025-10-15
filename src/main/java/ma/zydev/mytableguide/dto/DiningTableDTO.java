package ma.zydev.mytableguide.dto;

import lombok.*;
import ma.zydev.mytableguide.domain.DiningTable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiningTableDTO {
    private Long id;
    private String label;
    private int capacity;
    private String restaurantName;

    public static DiningTableDTO fromEntity(DiningTable table) {
        return DiningTableDTO.builder()
                .id(table.getId())
                .label(table.getLabel())
                .capacity(table.getCapacity())
                .restaurantName(table.getRestaurant().getName())
                .build();
    }
}
