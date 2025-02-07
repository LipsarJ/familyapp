package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.RequestFamilyDTO;
import org.example.dto.response.ResponseFamilyDTO;
import org.example.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<List<ResponseFamilyDTO>> getFamiliesForUser() {
        return ResponseEntity.ok(familyService.getFamiliesForUser());
    }

    @GetMapping({"name"})
    public ResponseEntity<ResponseFamilyDTO> getFamilyByName(@RequestParam String name) {
        return ResponseEntity.ok(familyService.getFamilyByName(name));
    }

    @PostMapping("create")
    public ResponseEntity<ResponseFamilyDTO> createFamily(@Valid @RequestBody RequestFamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.createFamily(familyDTO));
    }

    @PutMapping("add")
    public ResponseEntity<ResponseFamilyDTO> addUserToFamily(@Valid @RequestBody RequestFamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.addUsersToFamily(familyDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@Valid @RequestBody RequestFamilyDTO familyDTO) {
        familyService.deleteUsersFromFamily(familyDTO);
        return ResponseEntity.ok().build();
    }

}
