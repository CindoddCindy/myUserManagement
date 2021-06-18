package com.myusermanagement.tryusermanagement.user.service;

import com.google.common.base.Strings;
import com.myusermanagement.tryusermanagement.user.dto.PermissionDto;
import com.myusermanagement.tryusermanagement.user.entities.Permission;
import com.myusermanagement.tryusermanagement.user.exception.InvalidPermissionDataException;
import com.myusermanagement.tryusermanagement.user.exception.PermissionInUseException;
import com.myusermanagement.tryusermanagement.user.exception.PermissionNotFoundException;
import com.myusermanagement.tryusermanagement.user.repository.PermissionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PermissionService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    public Iterable<Permission> getPermissionList() {
        return permissionsRepository.findAll();
    }

    public Permission getPermissionByKey(String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }
        Optional<Permission> userOpt = permissionsRepository.findByPermission(key);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new PermissionNotFoundException(String.format("Permission not found for permission key = %s", key));
    }

    @Transactional
    public Permission createPermission(PermissionDto permissionDto) {
        if (permissionDto == null) {
            throw new InvalidPermissionDataException("Permission data cannot be null or empty");
        }

        String permissionKey = permissionDto.getPermission();

        if (Strings.isNullOrEmpty(permissionKey)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }

        // check permission
        Optional<Permission> permissionOpt = permissionsRepository.findByPermission(permissionKey);
        if (permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission %s already existing with the same key with Id = %s",
                    permissionKey, permissionOpt.get().getId()));
        }

        Permission permission = new Permission();
        permission.setPermission(permissionKey);

       // permission.setNote(permissionDTO.getNote());
        permission.setEnabled(permissionDto.isEnabled());

        Permission createdPermission = permissionsRepository.save(permission);

        log.info(String.format("Created permission %s with Id = %s", permissionKey, createdPermission.getId()));
        return createdPermission;
    }

    @Transactional
    public Permission updatePermission(PermissionDto permissionDto) {
        if (permissionDto == null) {
            throw new InvalidPermissionDataException("Permission data cannot be null");
        }

        Long permissionId = permissionDto.getId();
        Optional<Permission> permissionOpt = permissionsRepository.findById(permissionId);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("The permission with the id = %s has not been found",
                    permissionId));
        }

        Permission permission = permissionOpt.get();
        String permissionKey = permissionDto.getPermission();

        // check if exists a different configuration with the same permissionKey
        Optional<Permission> permissionByKeyOpt = permissionsRepository.findByPermission(permissionKey);
        if (permissionByKeyOpt.isPresent()) {
            Permission permission1 = permissionByKeyOpt.get();
            if (!permission1.getId().equals(permission.getId())) {
                throw new InvalidPermissionDataException(String.format("Exists already a permission with the key %s." +
                        " Use another key", permissionKey));
            }
        }

        // update permission
        permission.setPermission(permissionDto.getPermission());
        permission.setEnabled(permissionDto.isEnabled());
        //permission.setNote(permissionDTO.getNote());

        Permission updatedPermission = permissionsRepository.save(permission);
        log.info(String.format("Permission with id = %s has been updated.", permission.getId()));

        return updatedPermission;
    }

    @Transactional
    public void deletePermissionByKey(String permissionKey) {
        if (Strings.isNullOrEmpty(permissionKey)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }

        // check permission
        Optional<Permission> permissionOpt = permissionsRepository.findByPermission(permissionKey);
        if (!permissionOpt.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission %s not found", permissionKey));
        }

        Permission permission = permissionOpt.get();

        // check usage
        Long rowsFound = permissionsRepository.countPermissionUsage(permission.getId());
        if (rowsFound > 0) {
            // permission cannot be deleted
            throw new PermissionInUseException(String.format("The permission with key %s is in used (%s configuration rows)",
                    permissionKey, rowsFound));
        }

        permissionsRepository.delete(permission);

        log.info(String.format("Deleted permission with key %s", permission.getPermission()));
    }

}
