package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoundMatchDto {
    private MatchDto matchDto;

    private String response;
}

