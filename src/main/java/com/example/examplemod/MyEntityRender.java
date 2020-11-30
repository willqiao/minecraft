package com.example.examplemod;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class MyEntityRender extends MobRenderer<MyEntity, MyEntityModel>
{
    public MyEntityRender(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn, new MyEntityModel(), 0.9f);
    }

    @Override
    public ResourceLocation getEntityTexture(MyEntity entity)
    {
        return new ResourceLocation("examplemod", "textures/entity/myentity.png");
    }
}
