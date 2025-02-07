package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.controlleradvice.Errors;
import org.example.dto.request.RequestFamilyDTO;
import org.example.dto.request.RequestUserDTO;
import org.example.dto.response.ResponseFamilyDTO;
import org.example.dto.response.ResponseUserDto;
import org.example.entity.Family;
import org.example.entity.User;
import org.example.exception.extend.FamilyNameException;
import org.example.exception.extend.FamilyNotFoundException;
import org.example.exception.extend.UserNotFoundException;
import org.example.mapper.FamilyMapper;
import org.example.mapper.UserMapper;
import org.example.repo.FamilyRepo;
import org.example.repo.UserRepo;
import org.example.sequrity.service.UserContext;
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
    private final UserMapper userMapper;


    public List<ResponseFamilyDTO> getFamiliesForUser() {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        String username = responseUserDto.getUsername();
        User user = userRepo.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username" + username + " not found"));
        List<Family> families = userRepo.findAllFamiliesByUser(user);
        return families.stream()
                .map(familyMapper::toResponseFamilyDTO)
                .collect(Collectors.toList());

    }

    public ResponseFamilyDTO getFamilyByName(String name) {
        Family family = familyRepo.findByNameIgnoreCase(name).orElseThrow(() -> new FamilyNotFoundException("Family with name" + name + " isn't found"));
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
    public ResponseFamilyDTO addUsersToFamily(RequestFamilyDTO requestFamilyDTO) {
        Family family = getFamilyForService(requestFamilyDTO);
        List<RequestUserDTO> requestUsers = requestFamilyDTO.getUsers();
        List<User> users = getUsersForService(requestUsers);

        users.forEach(user -> {
            family.getUsers().add(user);
        });
        familyRepo.save(family);
        return familyMapper.toResponseFamilyDTO(family);
    }

    @Transactional
    public void deleteUsersFromFamily(RequestFamilyDTO requestFamilyDTO) {
        Family family = getFamilyForService(requestFamilyDTO);
        List<RequestUserDTO> requestUsers = requestFamilyDTO.getUsers();
        List<User> users = getUsersForService(requestUsers);

        users.forEach(user -> {
            family.getUsers().remove(user);
            user.getFamilies().remove(family);
            userRepo.save(user);
        });

        if (family.getUsers().isEmpty()) {
            familyRepo.delete(family);
        } else {
            familyRepo.save(family);
        }
    }

    private Family getFamilyForService(RequestFamilyDTO requestFamilyDTO) {
        String name = requestFamilyDTO.getName();
        return familyRepo.findByNameIgnoreCase(name).orElseThrow(() -> new FamilyNotFoundException("Family with name" + name + " isn't found"));
    }

    private List<User> getUsersForService(List<RequestUserDTO> requestUsers) {
        List<User> users = new ArrayList<>();
        requestUsers.forEach(
                requestUserDTO -> {
                    users.add(userRepo.findUserByUsername(requestUserDTO.getUsername()).orElseThrow(() ->
                            new UserNotFoundException("User with username " + requestUserDTO.getUsername() + " is not found")));
                }
        );
        return users;
    }

}
