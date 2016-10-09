package com.github.drizzleme.bo;

public class UserRolePermission {
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private String permissionBitOperator;

    private Integer parentUid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPermissionBitOperator() {
        return permissionBitOperator;
    }

    public void setPermissionBitOperator(String permissionBitOperator) {
        this.permissionBitOperator = permissionBitOperator == null ? null : permissionBitOperator.trim();
    }

    public Integer getParentUid() {
        return parentUid;
    }

    public void setParentUid(Integer parentUid) {
        this.parentUid = parentUid;
    }
}