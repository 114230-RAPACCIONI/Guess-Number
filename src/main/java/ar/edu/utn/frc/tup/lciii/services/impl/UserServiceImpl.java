package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.MatchDifficulty;
import ar.edu.utn.frc.tup.lciii.models.RoundMatch;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.UserRepository;
import ar.edu.utn.frc.tup.lciii.services.MatchService;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MatchService matchService;

    /**
     * Create a new user with the specified username and email.
     *
     * @param userName The username of the new user.
     * @param email The email of the new user.
     * @return The created User object.
     * @throws EntityNotFoundException If the email already exists.
     */
    @Override
    public User createUser(String userName, String email) {
        Optional<UserEntity> userEntityOptional = userRepository.getByEmail(email);
        if (userEntityOptional.isPresent()) {
            throw new EntityNotFoundException("Email already exist!");
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(userName);
            userEntity.setEmail(email);
            UserEntity userEntitySaved = userRepository.save(userEntity);
            return modelMapper.map(userEntitySaved, User.class);
        }
    }

    /**
     * Create a new match for the user with the specified ID and match difficulty.
     *
     * @param userId The ID of the user.
     * @param matchDifficulty The difficulty level of the match.
     * @return The created Match object.
     * @throws EntityNotFoundException If the user is not found.
     */
    @Override
    public Match createUserMatch(Long userId, MatchDifficulty matchDifficulty) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new EntityNotFoundException();
        } else {
            User user = modelMapper.map(userEntityOptional.get(), User.class);
            return matchService.createMatch(user, matchDifficulty);
        }
    }

    /**
     * Play a match for the user with the specified user ID and match ID, using the provided number.
     *
     * @param userId The ID of the user.
     * @param matchId The ID of the match.
     * @param numberToPlay The number to play in the match.
     * @return The RoundMatch object representing the result of the play.
     */
    @Override
    public RoundMatch playUserMatch(Long userId, Long matchId, Integer numberToPlay) {
        Match match = matchService.getMatchById(matchId);
        if (!match.getUser().getId().equals(userId)) {
            throw new EntityNotFoundException();
        } else {
            return matchService.playMatch(match, numberToPlay);
        }
    }
}
