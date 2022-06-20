package com.atguigu.dao;
import com.atguigu.pojo.Permission;
import org.springframework.stereotype.Repository;
import java.util.Set;

public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
