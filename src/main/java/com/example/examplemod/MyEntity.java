package com.example.examplemod;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class MyEntity extends MonsterEntity {
    protected MyEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_()
            .createMutableAttribute(Attributes.MAX_HEALTH, 20)
            .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10)
            .createMutableAttribute(Attributes.ATTACK_SPEED, 10)
            .createMutableAttribute(Attributes.ARMOR, 10)
            .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 1));
    }
}
