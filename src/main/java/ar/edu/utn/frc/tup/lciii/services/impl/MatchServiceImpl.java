package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.entities.MatchEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.repositories.MatchRepository;
import ar.edu.utn.frc.tup.lciii.services.MatchService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Random random = new Random();

    /**
     * Create a new match for the given user with the specified match difficulty.
     *
     * @param user The user for whom the match is being created.
     * @param matchDifficulty The difficulty level of the match.
     * @return The created Match object.
     */
    @Override
    public Match createMatch(User user, MatchDifficulty matchDifficulty) {
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setUserEntity(modelMapper.map(user, UserEntity.class));
        matchEntity.setMatchDifficulty(matchDifficulty);
        switch (matchDifficulty) {
            case HARD -> matchEntity.setRemainingTries(5);
            case MEDIUM -> matchEntity.setRemainingTries(8);
            case EASY -> matchEntity.setRemainingTries(10);
        }
        matchEntity.setNumberToGuess(random.nextInt(100));
        matchEntity.setMatchStatus(MatchStatus.PLAYING);
        matchEntity.setCreatedAt(LocalDateTime.now());
        matchEntity.setUpdateAt(LocalDateTime.now());
        MatchEntity savedMatchEntity = matchRepository.save(matchEntity);
        return modelMapper.map(savedMatchEntity, Match.class);
    }

    /**
     * Get the match by its ID.
     *
     * @param matchId The ID of the match.
     * @return The Match object.
     * @throws EntityNotFoundException If the match is not found.
     */
    @Override
    public Match getMatchById(Long matchId) {
        Optional<MatchEntity> matchEntityOptional = matchRepository.findById(matchId);
        if (matchEntityOptional.isEmpty()) {
            throw new EntityNotFoundException();
        } else {
            return modelMapper.map(matchEntityOptional.get(), Match.class);
        }
    }

    /**
     * Play the match with the given number.
     *
     * @param match The match to be played.
     * @param number The number to play in the match.
     * @return The RoundMatch object representing the result of the play.
     */
    @Override
    public RoundMatch playMatch(Match match, Integer number) {
        RoundMatch roundMatch = new RoundMatch();
        roundMatch.setMatch(match);
        if (match.getMatchStatus().equals(MatchStatus.FINISH)) {
            throw new EntityNotFoundException("The match is finished");
        }
        if (match.getNumberToGuess().equals(number)) {
            // TODO: calcular score
            // TODO: dar respuesta
            match.setMatchStatus(MatchStatus.FINISH);
            roundMatch.setResponse("GANO");
        } else {
            match.setRemainingTries(match.getRemainingTries() - 1);
            if (match.getRemainingTries().equals(0)) {
                match.setMatchStatus(MatchStatus.FINISH);
                roundMatch.setResponse("PERDIO");
            } else {
                if(number > match.getNumberToGuess()) {
                    roundMatch.setResponse("MENOR");
                } else {
                    roundMatch.setResponse("MAYOR");
                }
            }
        }
        UserEntity userEntity = modelMapper.map(match.getUser(), UserEntity.class);
        MatchEntity matchEntity = modelMapper.map(match, MatchEntity.class);
        matchEntity.setUserEntity(userEntity);
        matchEntity.setCreatedAt(LocalDateTime.now());
        matchEntity.setUpdateAt(LocalDateTime.now());
        matchRepository.save(matchEntity);
        return roundMatch;
    }
}
