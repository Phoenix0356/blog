package com.phoenix.blog.enumeration;

import com.phoenix.blog.exceptions.clientException.EnumTypeNotFoundException;
import lombok.Getter;

@Getter
public enum Role {
    //未登录游客
    VISITOR("VISITOR",0),
    //已登录游客
    MEMBER("MEMBER",1),
    //文章发布者
    WRITER("WRITER",2),
    //管理员
    ADMIN("ADMIN",3);

    private final String name;
    private final int level;

    Role(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public static int getLevel(String name) {
        for (Role role : Role.values()) {
            if (role.getName().equals(name)) {
                return role.getLevel();
            }
        }
        throw new EnumTypeNotFoundException();
    }
}
