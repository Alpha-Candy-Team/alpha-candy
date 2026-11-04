package com.alphacandy.item;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.api.storage.PokemonStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

/** Restores the Advanced Alpha active mark after Cobblemon finishes loading player storage. */
public final class AdvancedAlphaMarkPersistence {

    private AdvancedAlphaMarkPersistence() {
    }

    /** Registers a post-sync repair for the player's party and PC Pokémon. */
    public static void register() {
        CobblemonEvents.DATA_SYNCHRONIZED.subscribe(AdvancedAlphaMarkPersistence::restorePlayerMarks);
    }

    private static void restorePlayerMarks(ServerPlayer player) {
        var storage = Cobblemon.INSTANCE.getStorage();
        PlayerPartyStore party = storage.getParty(player);
        restoreStore(party);

        for (PCStore pc : storage.getPCs(player.getUUID(), player.registryAccess())) {
            restoreStore(pc);
        }
    }

    private static void restoreStore(PokemonStore<?> store) {
        for (Pokemon pokemon : store) {
            restoreMark(pokemon);
        }
    }

    private static void restoreMark(Pokemon pokemon) {
        if (pokemon == null
                || !pokemon.isAlpha()
                || !pokemon.getPersistentData().getBoolean(AlphaAdvancedCandyItem.ACTIVE)) {
            return;
        }

        Mark advancedAlphaMark = Marks.getByIdentifier(
                AlphaAdvancedCandyItem.ADVANCED_ALPHA_MARK_ID
        );
        if (advancedAlphaMark == null) {
            return;
        }

        Set<Mark> marks = new HashSet<>(pokemon.getMarks());
        if (marks.add(advancedAlphaMark)) {
            pokemon.setMarks(marks);
        }

        if (!advancedAlphaMark.equals(pokemon.getActiveMark())) {
            pokemon.setActiveMark(advancedAlphaMark);
        }
    }
}
