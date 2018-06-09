package com.sys.utils;

/**
 * @author wp
 * @date 2018/4/27 10:23
 */
public class LevelUtils {

    public static String getLevel(int level) {

        String s;
        switch (level) {

            case 1:
                s = "学生";
                break;
            case 2:
                s = "教师";
                break;
            case 3:
                s = "专业领导";
                break;
            case 4:
                s = "学院领导";
                break;
            case 5:
                s = "审核人员";
                break;
            case 9:
                s = "管理员";
                break;
            default:
                s = "未知";
        }
        return s;
    }

    public static Integer transLevel(String level) {

        Integer s = 0;
        switch (level) {

            case "学生":
                s = 1;
                break;
            case "教师":
                s = 2;
                break;
            case "专业领导":
                s = 3;
                break;
            case "学院领导":
                s = 4;
                break;
            case "审核人员":
                s = 5;
                break;
            case "管理员":
                s = 6;
                break;
            default:

        }
        return s;
    }
}
