package com.inspirelegacyventures.gathersocial.service;

import com.inspirelegacyventures.gathersocial.dto.UpdateDynamicPreferencesRequest;
import com.inspirelegacyventures.gathersocial.model.ActivityType;
import com.inspirelegacyventures.gathersocial.model.DynamicPreferences;
import com.inspirelegacyventures.gathersocial.model.User;
import com.inspirelegacyventures.gathersocial.repository.DynamicPreferencesRepository;
import com.inspirelegacyventures.gathersocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DynamicPreferencesRepository dynamicPreferencesRepository;

    public User createUser(User user) throws DataIntegrityViolationException {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUserHomeLocation(Long id, String location) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setHomeLocation(location);
                    return userRepository.save(user);
                });
    }

    public Optional<User> updateUserCurrentLocation(Long id, String location) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setCurrentLocation(location);
                    return userRepository.save(user);
                });
    }

    public Optional<User> updateUserPreferences(Long id, String activityType, String preferences) {
        return userRepository.findById(id)
                .map(user -> {
                    user.getActivityPreferences().put(ActivityType.valueOf(activityType.toUpperCase()), preferences);
                    return userRepository.save(user);
                });
    }

    public User updateDynamicPreferences(Long userId, UpdateDynamicPreferencesRequest request) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            DynamicPreferences preference = new DynamicPreferences();
            preference.setActivityType(request.getActivityType());
            preference.setCuisinePreferences(request.getCuisinePreferences());
            preference.setPriceRange(request.getPriceRange());

            user.addDynamicPreference(preference);
            dynamicPreferencesRepository.save(preference);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
