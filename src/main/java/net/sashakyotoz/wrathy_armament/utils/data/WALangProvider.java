package net.sashakyotoz.wrathy_armament.utils.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentBlocks;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

public class WALangProvider extends LanguageProvider {
    private static final String NORMAL_CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_,;.?!/\\'";
    private static final String UPSIDE_DOWN_CHARS = " ɐqɔpǝɟbɥıظʞןɯuodbɹsʇnʌʍxʎzⱯᗺƆᗡƎℲ⅁HIſʞꞀWNOԀὉᴚS⟘∩ΛMXʎZ0ƖᄅƐㄣϛ9ㄥ86‾'؛˙¿¡/\\,";
    private final boolean upsideDown;

    public WALangProvider(PackOutput packOutput, String locale, boolean upsideDown) {
        super(packOutput, WrathyArmament.MODID, locale);
        this.upsideDown = upsideDown;
    }

    @Override
    protected void addTranslations() {
        WrathyArmamentItems.ITEMS.getEntries().stream().filter(item -> !(item.get() instanceof BlockItem)).forEach(this::addItem);
        WrathyArmamentEntities.REGISTRY.getEntries().forEach(this::addEntityType);
        WrathyArmamentBlocks.BLOCKS.getEntries().forEach(this::addBlock);
        add("creativetab.wrathy_armament_tab","Wrathly Armament Arsenal");
        add("item.wrathy_armament.material","Material");
        add("subtitles.item.phantom_lancer","Splash of phantoms");
        add("subtitles.item.light_swing","Light sword swing");
        add("subtitles.item.katana_swing","Katana-like swing");
        add("subtitles.item.soul_swing","Soulful sword swing");
        add("enchantment.wrathy_armament.phantom_fury","Phantom Fury");
        add("enchantment.wrathy_armament.nightmare_jumping","Nightmare Jumping");
        add("enchantment.wrathy_armament.phantoquake","Phantoquake");
        add("effect.wrathy_armament.brightness","Brightness");
        add("structure.wrathy_armament.protection","Structure, reinforced by magic, cancels this action");
        add("boss.wrathy_armament.sashakyotoz.appeared","On the fourth sleepless day, nightmare were visualized");
        add("boss.wrathy_armament.johannes_fountain.dying","The sun... I had forgotten how it feels");
        addWeaponsDescriptions();
        addDeathDescriptions();
        addBossDescriptions();
    }
    private void addWeaponsDescriptions(){
        add("item.wrathy_armament.abilities", "- Abilities:");
        add("item.wrathy_armament.abilities.fire", "§6Fire");
        add("item.wrathy_armament.abilities.water", "§9Water");
        add("item.wrathy_armament.abilities.elemental", "§5Elementalis");
        add("item.wrathy_armament.abilities.earth", "§aEarth");
        add("item.wrathy_armament.abilities.air", "§fAir");
        add("item.wrathy_armament.abilities.WRATH_OF_ARTEMIS", "Wrath of artemis");
        add("item.wrathy_armament.abilities.CYCLONE_OF_CHAOS", "Cyclone of chaos");
        add("item.wrathy_armament.abilities.NEMEAN_CRUSH", "Nemean crush");
        add("item.wrathy_armament.phantom_lancer_hint", "- Circular Attack");
        add("item.wrathy_armament.phantom_lancer_hint1", "- Ranged Sweep Attack");
        add("item.wrathy_armament.phantom_lancer_circular_attack","Creates explosion, that hits nearby mobs");
        add("item.wrathy_armament.left_hand","When player holds item in left hand:");
        add("item.wrathy_armament.right_hand","When player holds item in right hand:");
        add("death.attack.wrathy_armament.phantom_shock_attack","Causes splash, that hits mobs within radius of 6 blocks");
        add("item.wrathy_armament.phantom_lancer_sweep_attack","Shoots wave of small phantoms in range of 16 blocks forward to player's view");
        add("item.wrathy_armament.johannes_sword_hint","- Dash");
        add("item.wrathy_armament.johannes_sword_dash","Push player forward to side of its view");
        add("item.wrathy_armament.johannes_sword_hint1","- Spear Attack");
        add("item.wrathy_armament.johannes_sword_spear_attack","Feel power of Johannes to spread spears above ground");
        add("item.wrathy_armament.zenith_hint","- Oo Zenith");
        add("item.wrathy_armament.zenith_ability","When swung, the sword sprites of its component swords fly towards enemies");
        add("item.wrathy_armament.master_sword_hint","- Gale Strike");
        add("item.wrathy_armament.master_sword_attack","Channels the power of the wind, launching the player backward in the direction of their head rotation and leaves explosive wave of air");
        add("item.wrathy_armament.master_sword_hint1","- Alter Time");
        add("item.wrathy_armament.master_sword_alter_time","Turn owner of sword back in time to safer location");
        add("item.wrathy_armament.blade_of_chaos_hint","- Rising Fury");
        add("item.wrathy_armament.blade_of_chaos_attack","Powerful attack, that launches enemies into air and fire them");
        add("item.wrathy_armament.blade_of_chaos_hint1","- God's combo");
        add("item.wrathy_armament.blade_of_chaos_attack1","Feel power of Kratos to manage Nemean crush, Cyclone of chaos and Wrath of artemis");
        add("item.wrathy_armament.frostmourne_hint","- Soulstorm Barrage");
        add("item.wrathy_armament.frostmourne_attack","Summons a torrent of souls that rains down upon the battlefield");
        add("item.wrathy_armament.frostmourne_hint1","- Rising of fallen");
        add("item.wrathy_armament.frostmourne_attack1","Killed undead enemy can arise to be your ally");
        add("item.wrathy_armament.frostmourne_charge","Souls contain: ");
        add("item.wrathy_armament.murasama_hint","- HF-absorption");
        add("item.wrathy_armament.murasama_attack","Each hit of enemy in combo modify speed of sword's owner, stacks to 30%");
        add("item.wrathy_armament.mistsplitter_hint","- Time charger");
        add("item.wrathy_armament.mistsplitter_attack","Time of rest of sword converts in power of next attack");
        add("item.wrathy_armament.half_zatoichi_hint","- Hostage of weapon");
        add("item.wrathy_armament.half_zatoichi_attack","Hitting of enemy awards by regeneration, but switching of weapon takes it away");
        add("item.wrathy_armament.half_zatoichi_charge","Hits charge: ");
        //game of origin
        add("item.wrathy_armament.game.master_sword","-Original from The Legend of Zelda series");
        add("item.wrathy_armament.game.johannes_sword","-Original from Rogue Legacy I");
        add("item.wrathy_armament.game.zenith","-Original from Terraria");
        add("item.wrathy_armament.game.blade_of_chaos","-Original from God of War series");
        add("item.wrathy_armament.game.frostmourne","-Original from World of Warcraft");
        add("item.wrathy_armament.game.murasama","-Original from Metal Gear");
        add("item.wrathy_armament.game.mistsplitter","-Original from Genshin Impact");
        add("item.wrathy_armament.game.half_zatoichi","-Original from Team Fortress 2");
    }
    private void addDeathDescriptions(){
        add("death.attack.wrathy_armament.phantom_shock_message","Overshocked by phantoms");
        add("death.attack.wrathy_armament.blade_of_chaos_message","Burnt to a crisp");
        add("death.attack.wrathy_armament.frostmourne_message","Take out of soul");
        add("death.attack.wrathy_armament.nightmare_jumping","Set to dust");
        add("death.attack.wrathy_armament.johannes_sword_spears","Spear stabbed");
    }
    private void addBossDescriptions(){
        add("boss.wrathy_armament.sashakyotoz","SashaKYotoz, the Phantom Lancer Keeper");
        add("boss.wrathy_armament.lich_king","Lich King, the Frostmourne Keeper");
        add("boss.wrathy_armament.johannes_knight","Johannes, the Traitor");
        add("boss.wrathy_armament.johannes_fountain","Johannes, the Fountain");
    }

    private void addEntityType(RegistryObject<EntityType<?>> entity) {
        String key = entity.getId().getPath();
        super.add("entity.wrathy_armament." + key,convertToName(key));
    }

    @Override
    public void add(String key, String value) {
        if(upsideDown) super.add(key, toUpsideDown(value));
        else super.add(key, value);
    }

    private void addBlock(RegistryObject<Block> block) {
        String key = block.getId().getPath();
        super.add("block.wrathy_armament." + key, convertToName(key));
    }

    private void addItem(RegistryObject<Item> item) {
        String key = item.getId().getPath();
        super.add("item.wrathy_armament." + key, convertToName(key));
    }

    private String convertToName(String key) {
        StringBuilder builder = new StringBuilder(key.substring(0, 1).toUpperCase() + key.substring(1));
        for(int i = 1; i < builder.length(); i++) {
            if(builder.charAt(i) == '_') {
                builder.deleteCharAt(i);
                builder.replace(i, i + 1, " " + Character.toUpperCase(builder.charAt(i)));
            }
        }
        String name = builder.toString();
        return upsideDown ? toUpsideDown(name) : name;
    }

    private static String toUpsideDown(String name) {
        StringBuilder builder = new StringBuilder();

        for(int i = name.length() - 1; i >= 0; i--) {
            if(i > 2 && name.substring(i - 3, i + 1).equals("%1$s")) {
                builder.append(name, i - 3, i + 1);
                i -= 4;
                continue;
            }

            char upsideDown = UPSIDE_DOWN_CHARS.charAt(NORMAL_CHARS.indexOf(name.charAt(i)));
            builder.append(upsideDown);
        }

        return builder.toString();
    }
}
