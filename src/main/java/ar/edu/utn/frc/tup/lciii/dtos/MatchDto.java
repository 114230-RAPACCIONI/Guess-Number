package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.models.MatchDifficulty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchDto {

    private Long id;

    private MatchDifficulty matchDifficulty;

    private Integer remainingTries;
}
