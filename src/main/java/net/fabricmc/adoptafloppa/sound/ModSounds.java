package net.fabricmc.adoptafloppa.sound;

import net.fabricmc.adoptafloppa.AdoptAFloppa;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds{
    public static final Identifier BOOM_ID = new Identifier(AdoptAFloppa.MOD_ID, "boom");
    public static SoundEvent BOOM_EVENT = new SoundEvent(BOOM_ID);
    public static final Identifier YIPPEE_ID = new Identifier(AdoptAFloppa.MOD_ID, "yippee");
    public static SoundEvent YIPPEE_EVENT = new SoundEvent(YIPPEE_ID);
    public static final Identifier FART_ID = new Identifier(AdoptAFloppa.MOD_ID, "fart");
    public static SoundEvent FART_EVENT = new SoundEvent(FART_ID);
    public static final Identifier AUGH_ID = new Identifier(AdoptAFloppa.MOD_ID, "augh");
    public static SoundEvent AUGH_EVENT = new SoundEvent(AUGH_ID);
    public static final Identifier AMOGUS_ID = new Identifier(AdoptAFloppa.MOD_ID, "amogilive");
    public static SoundEvent AMOGUS_EVENT = new SoundEvent(AMOGUS_ID);
    public static final Identifier GHIBLI_ID = new Identifier(AdoptAFloppa.MOD_ID, "ghibli");
    public static SoundEvent GHIBLI_EVENT = new SoundEvent(GHIBLI_ID);
    public static final Identifier RAPTURE_ID = new Identifier(AdoptAFloppa.MOD_ID, "rapture");
    public static SoundEvent RAPTURE_EVENT = new SoundEvent(RAPTURE_ID);
    public static final Identifier HORN_ID = new Identifier(AdoptAFloppa.MOD_ID, "horn");
    public static SoundEvent HORN_EVENT = new SoundEvent(HORN_ID);
    public static final Identifier THIS_SHIT_AINT_WORKING_ID = new Identifier(AdoptAFloppa.MOD_ID, "this_shit_aint_working");
    public static SoundEvent THIS_SHIT_AINT_WORKING_EVENT = new SoundEvent(THIS_SHIT_AINT_WORKING_ID);
    public static final Identifier SPACE_CORE_ID = new Identifier(AdoptAFloppa.MOD_ID, "space_core");
    public static SoundEvent SPACE_CORE_EVENT = new SoundEvent(SPACE_CORE_ID);
    public static final Identifier RYAN_GOSLING_ID = new Identifier(AdoptAFloppa.MOD_ID, "ryan_gosling");
    public static SoundEvent RYAN_GOSLING_EVENT = new SoundEvent(RYAN_GOSLING_ID);
    public static final Identifier RIZZ_ID = new Identifier(AdoptAFloppa.MOD_ID, "rizz");
    public static SoundEvent RIZZ_EVENT = new SoundEvent(RIZZ_ID);
    public static final Identifier SPLAT_ID = new Identifier(AdoptAFloppa.MOD_ID, "splat");
    public static SoundEvent SPLAT_EVENT = new SoundEvent(SPLAT_ID);
    public static final Identifier BWOOF_ID = new Identifier(AdoptAFloppa.MOD_ID, "bwoof");
    public static SoundEvent BWOOF_EVENT = new SoundEvent(BWOOF_ID);

    public static void registerSounds() {
        Registry.register(Registry.SOUND_EVENT, BOOM_ID, BOOM_EVENT);
        Registry.register(Registry.SOUND_EVENT, YIPPEE_ID, YIPPEE_EVENT);
        Registry.register(Registry.SOUND_EVENT, FART_ID, FART_EVENT);
        Registry.register(Registry.SOUND_EVENT, AUGH_ID, AUGH_EVENT);
        Registry.register(Registry.SOUND_EVENT, AMOGUS_ID, AMOGUS_EVENT);
        Registry.register(Registry.SOUND_EVENT, GHIBLI_ID, GHIBLI_EVENT);
        Registry.register(Registry.SOUND_EVENT, RAPTURE_ID, RAPTURE_EVENT);
        Registry.register(Registry.SOUND_EVENT, HORN_ID, HORN_EVENT);
        Registry.register(Registry.SOUND_EVENT, THIS_SHIT_AINT_WORKING_ID, THIS_SHIT_AINT_WORKING_EVENT);
        Registry.register(Registry.SOUND_EVENT, SPACE_CORE_ID, SPACE_CORE_EVENT);
        Registry.register(Registry.SOUND_EVENT, RYAN_GOSLING_ID, RYAN_GOSLING_EVENT);
        Registry.register(Registry.SOUND_EVENT, RIZZ_ID, RIZZ_EVENT);
        Registry.register(Registry.SOUND_EVENT, SPLAT_ID, SPLAT_EVENT);
        Registry.register(Registry.SOUND_EVENT, BWOOF_ID, BWOOF_EVENT);
    }
}
