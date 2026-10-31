package com.marcos.Fintech.controllers;

import com.marcos.Fintech.dto.UserCreateDTO;
import com.marcos.Fintech.dto.UserResponseDTO;
import com.marcos.Fintech.dto.UserUpdateDTO;
import com.marcos.Fintech.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Users",
        description = "Operations related to users"
)
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // GET /users
    @Operation(
            summary = "List all users",
            description = "Returns all users registered in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of users returned successfully"
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {

        List<UserResponseDTO> user = userService.findAll();

        return ResponseEntity.ok(user);
    }


    // GET /users/{id}
    @Operation(
            summary = "Find a user by id",
            description = "Returns the user corresponding to the provided id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findOne(

            @Parameter(
                    description = "User id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        UserResponseDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }


    // GET /users/document/{documentNumber}
    @Operation(
            summary = "Find a user by document number",
            description = "Returns a user using their document number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<UserResponseDTO> findByDocumentNumber(

            @Parameter(
                    description = "User document number",
                    example = "30123456"
            )
            @PathVariable String documentNumber

    ) {
        UserResponseDTO user =
                userService.findByDocumentNumber(documentNumber);

        return ResponseEntity.ok(user);
    }


    // POST /users
    @Operation(
            summary = "Create a user",
            description = "Creates a new user in the system"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid user data"
    )
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody UserCreateDTO dto
    ) {
        UserResponseDTO user = userService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }


    // PUT /users/{id}
    @Operation(
            summary = "Update a user",
            description = "Updates the data of an existing user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid user data"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(

            @Parameter(
                    description = "User id",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody UserUpdateDTO dto

    ) {
        UserResponseDTO user =
                userService.update(id, dto);

        return ResponseEntity.ok(user);
    }


    // PUT /users/{id}/activate
    @Operation(
            summary = "Activate a user",
            description = "Activates an existing user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User activated successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponseDTO> activate(

            @Parameter(
                    description = "User id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        UserResponseDTO user =
                userService.activate(id);

        return ResponseEntity.ok(user);
    }


    // PUT /users/{id}/deactivate
    @Operation(
            summary = "Deactivate a user",
            description = "Deactivates a user if none of their accounts have a non-zero balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User deactivated successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "User cannot be deactivated because one or more accounts have a non-zero balance"
    )
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivate(

            @Parameter(
                    description = "User id",
                    example = "1"
            )
            @PathVariable Long id

    ) {
        UserResponseDTO user =
                userService.deactivate(id);

        return ResponseEntity.ok(user);
    }
}