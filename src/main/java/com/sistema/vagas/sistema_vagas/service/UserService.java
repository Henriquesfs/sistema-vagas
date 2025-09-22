package com.sistema.vagas.sistema_vagas.service;

import com.sistema.vagas.sistema_vagas.model.user.User;
import com.sistema.vagas.sistema_vagas.model.user.UserRequestDTO;
import com.sistema.vagas.sistema_vagas.model.user.UserResponseDTO;
import com.sistema.vagas.sistema_vagas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.username())) {
            throw new RuntimeException("Username já existe");
        }

        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setPassword(userRequestDTO.password());

        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return convertToResponseDTO(user);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getUsername().equals(userRequestDTO.username()) &&
                userRepository.existsByUsername(userRequestDTO.username())) {
            throw new RuntimeException("Username já existe");
        }

        user.setUsername(userRequestDTO.username());
        user.setPassword(userRequestDTO.password());

        User updatedUser = userRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername());
    }
}
