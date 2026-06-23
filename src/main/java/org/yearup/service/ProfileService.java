package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Product;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.security.Principal;
import java.util.List;

@Service
public class ProfileService
{
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository)
    {
        this.profileRepository = profileRepository;
    }

    public Profile create(Profile profile)
    {
        return profileRepository.save(profile);
    }

    public Profile getById(int id){
       return profileRepository.findById(id).orElse(null);
    }

    public Profile update(int userId, Profile profile) {

        Profile existing = profileRepository.findById(userId).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setFirstName(profile.getFirstName());
        existing.setLastName(profile.getLastName());
        existing.setPhone(profile.getPhone());
        existing.setEmail(profile.getEmail());
        existing.setAddress(profile.getAddress());
        existing.setCity(profile.getCity());
        existing.setState(profile.getState());
        existing.setZip(profile.getZip());

        return profileRepository.save(existing);
    }
}
