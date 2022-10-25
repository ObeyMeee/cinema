package ua.com.andromeda.cinemaspringbootapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private String sessionName;
    private LocalDateTime sessionDate;
    private int totalPrice;
    private int quantity;
}
