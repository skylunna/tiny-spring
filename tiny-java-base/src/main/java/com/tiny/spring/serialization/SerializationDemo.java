package com.tiny.spring.serialization;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Copyright (c) 2026 skyluna. All Rights Reserved.
 *
 * @Author: skylunna
 * @Date: 2026/4/27 16:20
 * @Description: SerializationDemo 类功能描述
 */
@Slf4j
public class SerializationDemo {

    private static final String FILE_PATH = "user.dat";

    public static void main(String[] args) {
        User original = new User(1L, "张三", "123");
        System.out.println("原始对象: " + original);

        // 序列化
        serialize(original);

        // 反序列化
        User deserialized = deserialize();
        System.out.println("反序列化对象: " + deserialized);

    }

    private static void serialize(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(user);
            System.out.println("✅ 序列化成功");
        } catch (IOException e) {
            log.info("❌ 序列化失败: {}", e.getMessage());
        }
    }

    private static User deserialize() {
        // 反序列化逻辑
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("❌ 反序列化失败: {}", e.getMessage());
            return null;
        }
    }
}