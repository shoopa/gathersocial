package com.inspirelegacyventures.gathersocial.controller;

import com.inspirelegacyventures.gathersocial.exception.EntityAlreadyExistsException;
import com.inspirelegacyventures.gathersocial.model.Vote;
import com.inspirelegacyventures.gathersocial.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @GetMapping
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vote getVote(@PathVariable Long id) {
        return voteRepository.findById(id).orElse(null);
    }


    @PostMapping
    public Vote createVote(@RequestBody Vote vote) {
        if (vote.getId() != null && voteRepository.existsById(vote.getId())) {
            throw new EntityAlreadyExistsException("Vote with id " + vote.getId() + " already exists");
        }
        return voteRepository.save(vote);
    }
}