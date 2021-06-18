package com.myusermanagement.tryusermanagement.user.entities;

import com.myusermanagement.tryusermanagement.user.exception.InvalidAdminLevelException;

public enum AdminLevel {


    //MALE(1), FEMALE(2);
    BANK_BRANCH_MANAGER(1),

    FINANCIAL_PLANING(2),

     AUTO_REMARKETING_MANAGER(3),

    CARD_OPERATION_MANAGER(4),

    SENIOR_BUSSINES_DEVELOPMENT(5),

    COMMERCIAL_LENDING_DIRECTOR(6);



    private int adminLevel;

    AdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }

    public int getAdminLevel() {
        return adminLevel;
    }

    public static AdminLevel getValidAdminLevel(String adminLevelName) {
        AdminLevel adminLevel;
        try {
            adminLevel = AdminLevel.valueOf(adminLevelName);
        } catch(IllegalArgumentException ex) {
            throw new InvalidAdminLevelException(String.format("Invalid admin level string %s. Are supported only: Six Type Admin level strings", adminLevelName));
        }
        return adminLevel;
    }
}
