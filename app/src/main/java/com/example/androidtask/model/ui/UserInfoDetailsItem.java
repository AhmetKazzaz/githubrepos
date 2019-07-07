package com.example.androidtask.model.ui;

import com.example.androidtask.model.Owner;

public class UserInfoDetailsItem extends UserInfoBaseItem {

    public Owner owner;

    public UserInfoDetailsItem(Owner owner) {
        this.owner = owner;
    }

    @Override
    public UserInfoItemType type() {
        return UserInfoItemType.details;
    }
}
