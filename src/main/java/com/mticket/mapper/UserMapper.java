package com.mticket.mapper;

import com.mticket.base.BaseMapper;
import com.mticket.dto.UserDTO;
import com.mticket.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserMapper implements BaseMapper<User, UserDTO> {
    private final ModelMapper modelMapper;

    @Override
    public UserDTO toDto(User entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    @Override
    public User toEntity(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    @Override
    public List<UserDTO> toDtoList(List<User> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public List<User> toEntityList(List<UserDTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
}
