package uk.co.maboughey.mcnsanotes.type;

import uk.co.maboughey.mcnsanotes.database.DBuuid;

import java.util.Date;
import java.util.UUID;

public class Stat {
    public boolean changed = false;
    public String name = new String("");
    public Date dateJoined = new Date();
    public long timeOnServer = 0;
    public long numJoins = 0;
    public long numKicks = 0;
    public long blocksBroken = 0;
    public long blocksPlaced = 0;
    public long modRequests = 0;
    public UUID uuid;
    public long numDeaths = 0;

    public long loginTime;

    public Stat(UUID uuid) {
        loginTime = System.currentTimeMillis() / 1000;
        this.uuid = uuid;
    }
    public Stat() {}

    public String getName() {
        return DBuuid.getUsername(this.uuid);
    }
    public String getTimeOnServer()
    {
        long currentPlayTime = 0L;
        if (this.loginTime != 0)
        {
            long currenttime = System.currentTimeMillis() / 1000;
            currentPlayTime = this.timeOnServer + (currenttime - this.loginTime);
        }
        else
        {
            currentPlayTime = this.timeOnServer;
        }
        long days = Math.floorDiv(currentPlayTime, 86400);
        long hours = Math.floorDiv(currentPlayTime - (days * 86400) , 3600);
        long minutes = Math.floorDiv(currentPlayTime - ((hours * 3600) + (days * 86400)) , 60);
        long seconds = currentPlayTime - ((minutes * 60) + (hours * 3600) + (days * 86400));
        String string = days + " Days " + hours + " Hours " + minutes + " Minutes " + seconds + " Seconds";
        return string;
    }
}
