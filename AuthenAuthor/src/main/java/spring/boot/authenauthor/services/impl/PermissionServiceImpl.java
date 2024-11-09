package spring.boot.authenauthor.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Service;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.repositories.PermissionRepository;
import spring.boot.authenauthor.services.IPermissionService;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer, ReactiveCrudRepository<Permission,Integer>> implements IPermissionService {
    //
//

    @Autowired
    private PermissionRepository permissionRepository;
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository); // Truyền repository vào lớp cha
    }

}
