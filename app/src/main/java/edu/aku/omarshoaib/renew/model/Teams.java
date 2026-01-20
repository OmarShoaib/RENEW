package edu.aku.omarshoaib.renew.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Teams {

    public static String TABLE_NAME = "Teams";

    @PrimaryKey
    @NonNull
    @SerializedName("team_id")
    private String teamId;

    @SerializedName("team_name")
    private String teamName;

    public String getTeamId() {
        return teamId.trim();
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName.trim();
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @NonNull
    @Override
    public String toString() {
        return teamId.trim()+"-"+teamName;
    }
}
