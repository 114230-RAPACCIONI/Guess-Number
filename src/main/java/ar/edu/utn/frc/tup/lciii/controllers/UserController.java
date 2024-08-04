package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/guess-number/users")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Create a new user with the given user DTO.
     *
     * @param userDto The DTO containing user details.
     * @return ResponseEntity containing the created UserDto.
     */
    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto.getUserName(), userDto.getEmail());
        UserDto userDtoCreated = modelMapper.map(user, UserDto.class);
        return ResponseEntity.ok(userDtoCreated);
    }

    /**
     * Create a new match for the user with the given user ID and match creation DTO.
     *
     * @param userId The ID of the user.
     * @param createUserMatchDto The DTO containing match creation details.
     * @return ResponseEntity containing the created MatchDto.
     */
    @PostMapping("/{userId}/matches")
    public ResponseEntity<MatchDto> createUserMatch(@PathVariable Long userId,
                                                    @RequestBody CreateUserMatchDto createUserMatchDto) {
        Match match = userService.createUserMatch(userId, createUserMatchDto.getDifficulty());
        MatchDto matchDto = modelMapper.map(match, MatchDto.class);
        return ResponseEntity.ok(matchDto);
    }

    /**
     * Play a match for the user with the given user ID, match ID, and play match DTO.
     *
     * @param userId The ID of the user.
     * @param matchId The ID of the match.
     * @param playUserMatchDto The DTO containing the number to play.
     * @return ResponseEntity containing the result of the match play in RoundMatchDto.
     */
    @PostMapping("/{userId}/matches/{matchId}")
    public ResponseEntity<RoundMatchDto> playUserMatch(@PathVariable Long userId,
                                                       @PathVariable Long matchId,
                                                       @RequestBody PlayUserMatchDto playUserMatchDto) {

        RoundMatch roundMatch = userService.playUserMatch(userId, matchId, playUserMatchDto.getNumber());
        MatchDto matchDto = modelMapper.map(roundMatch.getMatch(), MatchDto.class);
        RoundMatchDto roundMatchDto = modelMapper.map(roundMatch, RoundMatchDto.class);
        roundMatchDto.setMatchDto(matchDto);
        return ResponseEntity.ok(roundMatchDto);
    }
}
