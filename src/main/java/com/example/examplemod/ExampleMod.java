package com.example.examplemod;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraftforge.registries.IForgeRegistry;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod
{
    // Directly reference a log4j logger.
    static final String MODID = "examplemod";
    private static final Logger LOGGER = LogManager.getLogger();

    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    static final DeferredRegister ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    static RegistryObject<EntityType<MyEntity>> myentity;

    public static void main(String[] args) {


        Block block = new Block(Block.Properties.create(Material.GLASS)) {

        };

        new ExampleMod(){
            @Override
            public void onServerStarting(FMLServerStartingEvent event) {
                super.onServerStarting(event);
            }
        };
    }
    RegistryObject<Block> myblock;
    public ExampleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(new RegistryEvents());


        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        RegistryObject<Item> myitem = ITEMS.register("myitem", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

        myblock = BLOCKS.register("myblock", () -> new Block(
            Block.Properties.create(Material.GLASS)
                .hardnessAndResistance(3f, 10f)
                .setLightLevel(input-> 1)
                 .hardnessAndResistance(10f)
                .sound(SoundType.GLASS)));


        ITEMS.register("myblock", () -> new BlockItem(myblock.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));

        myentity = ENTITIES.register("myentity", () -> EntityType.Builder
                              .create(MyEntity::new, EntityClassification.MONSTER)
                              .size(2.5f, 2.5f)
                              .build(new ResourceLocation(MODID, "myentity").toString()));

    }

    private void setup(final FMLCommonSetupEvent event)
    {

        event.enqueueWork(()->{
            System.out.println("qiao event = " + event);

            RenderTypeLookup.setRenderLayer(myblock.get(), RenderType.getTranslucent());

            GlobalEntityTypeAttributes.put(myentity.get(), MyEntity.setCustomAttributes().create());
            RenderingRegistry.registerEntityRenderingHandler(myentity.get(), MyEntityRender::new);
        });

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public class RegistryEvents {
        @SubscribeEvent
        public void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public void onSpawn( PlayerEvent.PlayerLoggedInEvent event) {

            System.out.println("qiao event = " + event);
            //relocate position
//            Thread thread = new Thread(() -> {
//                while (true) {
//                    try {
//                        Thread.sleep(60000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("qiao event 2 = " + event);
//                    event.getPlayer().setPositionAndUpdate((int) (300.0 * Math.random()), 1, (int) (300.0 * Math.random()));
//                }
//            });
//            thread.start();

//            for (int i = 100; i < 200; i ++) {
//                Entity entityByID = event.getPlayer().getEntityWorld().getEntityByID(i);
//                boolean b = event.getPlayer().getEntityWorld().addEntity(entityByID);
//                System.out.println("qiao b = " + b);
//            }

            //drop item based on ID.
            for (int i = 110; i < 150; i ++) {
                event.getPlayer().entityDropItem(new ItemStack(Item.getItemById(i),i));
            }
            //find registry
            IForgeRegistry<Item> registry = GameRegistry.findRegistry(Item.class);
            System.out.println("qiao registry.getKeys() = " + registry.getKeys());
            Item egg = registry.getValue(new ResourceLocation("minecraft:egg"));
            ItemStack itemStack = new ItemStack(egg, 34);
            itemStack.setDisplayName(new StringTextComponent(TextFormatting.GOLD + "This is good"));
            //Drop item
            event.getPlayer().entityDropItem(itemStack);

            //Add item to Inventory
            event.getPlayer().addItemStackToInventory(new ItemStack(Item.getItemById(130), 19));

            event.getPlayer().sendMessage(new StringTextComponent("/summon minecraft:cat"), event.getPlayer().getUniqueID());
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public void onDrops( LivingDropsEvent event) {
            System.out.println("qiao event = " + event);
            LivingEntity entity = event.getEntityLiving();
            for (int i = 300; i < 400; i++) {
                ItemEntity itemEntity = new ItemEntity(entity.getEntityWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), new ItemStack(Item.getItemById(i), 32));
                event.getDrops().add(itemEntity);
            }
        }
    }
}
