package com.game.jeffrey.towerdefence2.BFTD;

/**
 * Created by Zonde on 14-12-2016.
 */

public abstract class GenericWorker
{
    enum workerType
    {
        HighDamage,
        Splash,
        Fast,
        Sniper
    }

    private int damage;
    private int stamina;
    private int attackSpeed;
    private int level;
    private int range;

    private static int COST = 10;

    private workerType type;
    public GenericWorker(int damage, int stamina, int attackSpeed, int level, int range)
    {
        this.damage = damage;
        this.stamina = stamina;
        this.attackSpeed = attackSpeed;
        this.level = level;
        this.range = range;
    }

    public void upgrade(int cost)
    {
        if(cost == level*COST)
        {
            level++;
            this.damage = (int)(damage * 1.5);

            if(this.type == workerType.Fast)
            {
                this.attackSpeed = (int)(attackSpeed*1.2);
            }
            if(this.type == workerType.HighDamage)
            {
                this.damage = (int)(damage*1.2);
            }
            if(this.type == workerType.Sniper)
            {
                this.range = (int)(range*1.2);
            }
            //TODO: splash
        }
    }

    public int getDamage()
    {
        return damage;
    }


    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public int getStamina()
    {
        return stamina;
    }

    public void setStamina(int stamina)
    {
        this.stamina = stamina;
    }

    public int getAttackSpeed()
    {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed)
    {
        this.attackSpeed = attackSpeed;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }
}
