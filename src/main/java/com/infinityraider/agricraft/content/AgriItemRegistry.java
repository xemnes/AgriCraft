package com.infinityraider.agricraft.content;

import com.infinityraider.agricraft.AgriCraft;
import com.infinityraider.agricraft.content.core.*;
import com.infinityraider.agricraft.content.decoration.ItemGrate;
import com.infinityraider.agricraft.content.irrigation.*;
import com.infinityraider.agricraft.content.tools.*;
import com.infinityraider.agricraft.reference.Names;

public class AgriItemRegistry {

    private static final AgriItemRegistry INSTANCE = new AgriItemRegistry();

    public static AgriItemRegistry getInstance() {
        return INSTANCE;
    }

    public final ItemDebugger debugger;

    public final ItemCropSticks crop_sticks_wood;
    public final ItemCropSticks crop_sticks_iron;
    public final ItemCropSticks crop_sticks_obsidian;

    public final ItemSeedAnalyzer seed_analyzer;
    public final ItemJournal journal;

    public final ItemDynamicAgriSeed seed;

    public final ItemIrrigationTank tank;
    public final ItemIrrigationChannel channel;
    public final ItemIrrigationChannelHollow channel_hollow;
    public final ItemSprinkler sprinkler;
    public final ItemChannelValve valve;

    public final ItemClipper clipper;
    public final ItemMagnifyingGlass magnifying_glass;
    public final ItemRake rake_wood;
    public final ItemRake rake_iron;
    public final ItemTrowel trowel;
    public final ItemSeedBag seed_bag;

    public final ItemAgriNugget nugget_coal;
    public final ItemAgriNugget nugget_diamond;
    public final ItemAgriNugget nugget_emerald;
    public final ItemAgriNugget nugget_quartz;

    public final ItemGrate grate;

    @SuppressWarnings("deprecation")
    private AgriItemRegistry() {
        this.debugger = new ItemDebugger();

        this.crop_sticks_wood = new ItemCropSticks(CropStickVariant.WOOD);
        this.crop_sticks_iron = new ItemCropSticks(CropStickVariant.IRON);
        this.crop_sticks_obsidian = new ItemCropSticks(CropStickVariant.OBSIDIAN);

        this.seed_analyzer = new ItemSeedAnalyzer();
        this.journal = new ItemJournal();

        this.seed = new ItemDynamicAgriSeed();

        this.tank = new ItemIrrigationTank();
        this.channel = new ItemIrrigationChannel();
        this.channel_hollow = new ItemIrrigationChannelHollow();
        this.sprinkler = new ItemSprinkler();
        this.valve = new ItemChannelValve();

        this.clipper = new ItemClipper();
        this.magnifying_glass = new ItemMagnifyingGlass();
        this.rake_wood = new ItemRake(ItemRake.WOOD_LOGIC);
        this.rake_iron = new ItemRake(ItemRake.IRON_LOGIC);
        this.trowel = new ItemTrowel();
        this.seed_bag = new ItemSeedBag();

        this.nugget_coal = AgriCraft.instance.getConfig().enableCoalNugget()
                ? new ItemAgriNugget.Burnable(Names.Nuggets.COAL)
                : null;
        this.nugget_diamond = AgriCraft.instance.getConfig().enableDiamondNugget()
                ? new ItemAgriNugget(Names.Nuggets.DIAMOND)
                : null;
        this.nugget_emerald = AgriCraft.instance.getConfig().enableEmeraldNugget()
                ? new ItemAgriNugget(Names.Nuggets.EMERALD)
                : null;
        this.nugget_quartz = AgriCraft.instance.getConfig().enableQuartzNugget()
                ? new ItemAgriNugget(Names.Nuggets.QUARTZ)
                : null;

        this.grate = new ItemGrate();
    }
}
