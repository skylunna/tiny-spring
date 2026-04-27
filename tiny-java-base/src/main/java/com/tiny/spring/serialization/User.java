package com.tiny.spring.serialization;

import lombok.*;

import java.io.Serializable;

/**
 * Copyright (c) 2026 skyluna. All Rights Reserved.
 *
 * @Author: skylunna
 * @Date: 2026/4/27 16:19
 * @Description: User 类功能描述
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    // 🔑 强烈建议手动声明，否则 JVM 会根据类结构自动生成
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    // 🔒 不参与序列化
    private transient String password;
    // 静态变量属于类，不参与序列化
    private static String company = "MiniTech";
}