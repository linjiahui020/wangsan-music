package com.jh.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: LinJH
 * @Date: 2020/11/3 10:20
 * @Version:0.0.1
 */
public class RegularExpressionUtils {

    //邮箱
    public static final String EMAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$" ;

    //电话号码
    public static final String PHONE_PATTERN = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9]|199)\\d{8}$";

    //帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)
    public static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    //密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)
    public static final String PASSWORD_PATTERN = "^[a-zA-Z]\\w{5,17}$";


    public static boolean check(String pattern,String input){
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        return matcher.matches();
    }
}
