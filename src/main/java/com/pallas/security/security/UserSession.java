package com.pallas.security.security;

import com.pallas.security.models.UserSessionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 17-November-2022
 * @email : talehji@gmail.com
 */

public class UserSession {
    public static Map<String, Long> userId = new HashMap<>();
    public static List<UserSessionData> data = new ArrayList<>();

    public static UserSessionData getDataByToken(String token){
        List<UserSessionData> collect = data.stream().filter(d -> d.getToken().equals(token)).collect(Collectors.toList());

        if (collect.size() > 0) {
            return collect.get(0);
        }
        return null;
    }

    public static void removeDataByUserId(long idUser){
        data.removeIf(userSessionData -> userSessionData.getUserId() == idUser);
    }

}
