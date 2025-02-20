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
@RequestMapping("newAPI/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<List<ResponseFamilyDTO>> getFamiliesForUser() {
        return ResponseEntity.ok(familyService.getFamiliesForUser());
    }

    @GetMapping({"{id}"})
    public ResponseEntity<ResponseFamilyDTO> getFamilyByName(@PathVariable Long id) {
        return ResponseEntity.ok(familyService.getFamilyByID(id));
    }

    @PostMapping
    public ResponseEntity<ResponseFamilyDTO> createFamily(@RequestBody RequestFamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.createFamily(familyDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUsers(@Valid @RequestBody RequestFamilyDTO familyDTO) {
        familyService.deleteUsersFromFamily(familyDTO);
        return ResponseEntity.ok().build();
    }

}
