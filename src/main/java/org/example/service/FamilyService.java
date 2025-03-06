package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.dto.request.RequestFamilyDTO;
import org.example.dto.request.RequestUserDTO;
import org.example.dto.response.ResponseFamilyDTO;
import org.example.dto.response.ResponseUserDto;
import org.example.entity.Family;
import org.example.entity.Invitation;
import org.example.entity.InvitationStatus;
import org.example.entity.User;
import org.example.exception.extend.FamilyNameException;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.FamilyMapper;
import org.example.messaging.InvitationEvent;
import org.example.repo.FamilyRepo;
import org.example.repo.InvitationRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
import org.example.messaging.FamilyInvitationProducer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final UserRepo userRepo;
    private final UserContext userContext;
    private final FamilyMapper familyMapper;
    private final FamilyRepo familyRepo;
    private final InvitationRepo invitationRepo;
    private final FamilyInvitationProducer invitationProducer;


    public List<ResponseFamilyDTO> getFamiliesForUser() {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        String username = responseUserDto.getUsername();
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username" + username + " not found"));
        List<Family> families = userRepo.findAllFamiliesByUser(user);
        return families.stream()
                .map(familyMapper::toResponseFamilyDTO)
                .collect(Collectors.toList());

    }

    public ResponseFamilyDTO getFamilyByID(Long id) {
        Family family = familyRepo.findById(id).orElseThrow(() -> new FamilyNotFoundException("Family with id" + id + " not found"));
        return familyMapper.toResponseFamilyDTO(family);
    }

    @Transactional
    public ResponseFamilyDTO createFamily(RequestFamilyDTO requestFamilyDTO) {

        ResponseUserDto responseUserDto = userContext.getUserDto();
        String username = responseUserDto.getUsername();
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username" + username + " not found"));

        String name = requestFamilyDTO.getName();
        if (familyRepo.existsByNameIgnoreCase(name)) {
            throw new FamilyNameException("Family's name is taken", Errors.FAMILY_NAME_TAKEN);
        }

        Family family = new Family();
        family.setName(name);
        family.getUsers().add(user);
        user.getFamilies().add(family);
        familyRepo.save(family);
        return familyMapper.toResponseFamilyDTO(family);

    }


    @Transactional
    public void deleteUserFromFamily(Long userID, Long familyID) {
        Family family = familyRepo.findById(familyID).orElseThrow(()-> new FamilyNotFoundException("Family is not found"));
        User user = userRepo.findById(userID).orElseThrow(() -> new UserNotFoundException("User not found"));
        family.getUsers().remove(user);

        if (family.getUsers().isEmpty()) {
            familyRepo.delete(family);
        } else {
            familyRepo.save(family);
        }
    }

}
