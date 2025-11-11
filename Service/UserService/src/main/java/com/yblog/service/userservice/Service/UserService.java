package com.yblog.service.userservice.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yblog.service.userservice.Entity.DTO.UserDTO;

public interface UserService{

    public IPage<UserDTO> getUsers(int pageNum, int size);

    public UserDTO getUserById(String uid);

    public boolean updateUser(UserDTO userDTO);

    public boolean deleteUser(String uid);

    public boolean addUser(UserDTO userDTO);
}
