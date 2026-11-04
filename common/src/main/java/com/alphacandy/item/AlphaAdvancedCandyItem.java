package com.alphacandy.item;

import com.alphacandy.config.ModConfig;
import com.alphacandy.compat.iota.IotaCompat;
import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.api.pokemon.experience.CandyExperienceSource;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * EN: Guaranteed, deterministic advanced Alpha candy that transforms a Pokémon into an Alpha variant,
 * maximizes friendship, grants a level, and equips the Advanced Alpha mark.
 * ES: Caramelo Alfa avanzado garantizado y determinista que convierte al Pokémon en Alfa,
 * maximiza su amistad, sube un nivel y equipa la marca Alfa Avanzado.
 */
public final class AlphaAdvancedCandyItem extends Item {

    // EN: Persistent flags allow the purifier and Iota integration to undo this candy's changes. / ES: Estas claves permiten al purificador y a Iota deshacer los cambios del caramelo.
    public static final String ACTIVE =
            "alphacandy_advanced_alpha_active";
    public static final String ORIGINAL_SCALE =
            "alphacandy_advanced_alpha_original_scale";

    /** EN: Identifier for the custom Advanced Alpha mark. / ES: Identificador de la marca personalizada de Alfa Avanzado. */
    public static final ResourceLocation ADVANCED_ALPHA_MARK_ID =
            ResourceLocation.parse("alphacandy:mark_advanced_alpha");

    /** EN: Creates the advanced Alpha candy item. / ES: Crea el objeto de caramelo Alfa avanzado. */
    public AlphaAdvancedCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /**
     * EN: Validates server, ownership, species and Alpha state; sets Alpha status and its mark,
     * saves scale for restoration, maximizes friendship, grants one level if possible, and consumes the item.
     * ES: Valida servidor, propietario, especie y estado Alfa; activa Alfa y su marca,
     * guarda la escala, maximiza amistad, sube un nivel si es posible y consume el objeto.
     */
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // EN: Ignore client-side prediction and non-Pokémon targets. / ES: Ignora la predicción del cliente y los objetivos que no sean Pokémon.
        if (!(target instanceof PokemonEntity entity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = entity.getPokemon();

        // EN: Prevent use by players who do not own this Pokémon. / ES: Impide el uso a jugadores que no sean propietarios del Pokémon.
        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // EN: Reject blacklisted species and Pokémon already converted to Alpha. / ES: Rechaza especies bloqueadas y Pokémon que ya son Alfa.
        if (pokemon.isAlpha() || ModConfig.isSpeciesBlacklisted(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: The custom mark must be registered before changing the Pokémon. / ES: La marca personalizada debe estar registrada antes de modificar al Pokémon.
        Mark advancedAlphaMark =
                Marks.getByIdentifier(ADVANCED_ALPHA_MARK_ID);

        if (advancedAlphaMark == null) {
            return InteractionResult.FAIL;
        }

        CompoundTag data = pokemon.getPersistentData();

        pokemon.setAlpha(true);

        // EN: Preserve the natural scale; for Iotas, use the original scale captured by IotaCompat. / ES: Conserva la escala natural; en Iotas usa la escala original de IotaCompat.
        boolean iotaWasActive = IotaCompat.isIota(pokemon);
        float originalScale = iotaWasActive
                ? IotaCompat.getOriginalScale(pokemon)
                : pokemon.getScaleModifier();

        data.putFloat(ORIGINAL_SCALE, originalScale);
        data.putBoolean(ACTIVE, true);

        // EN: Add only the custom mark, select it, and refresh derived aspects. / ES: Añade solo la marca personalizada, la selecciona y actualiza los aspectos derivados.
        Set<Mark> marks = new HashSet<>(pokemon.getMarks());
        if (marks.add(advancedAlphaMark)) {
            pokemon.setMarks(marks);
        }
        pokemon.setActiveMark(advancedAlphaMark);
        pokemon.updateAspects();

        // EN: Maximize friendship and grant one level using Cobblemon candy experience source. / ES: Maximiza la amistad y otorga un nivel mediante la fuente de experiencia de caramelos de Cobblemon.
        pokemon.setFriendship(250, true);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.FAIL;
        }
        if (pokemon.canLevelUpFurther()) {
            pokemon.levelUp(new CandyExperienceSource(serverPlayer, stack));
        }

        // EN: Play dramatic dragon growl audio feedback upon conversion.
        // ES: Reproduce efecto de sonido dramático tras la conversión.
        player.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.ENDER_DRAGON_GROWL,
                SoundSource.NEUTRAL,
                0.8f,
                0.8f
        );

        // EN: Consume one candy outside Creative mode.
        // ES: Consume un caramelo fuera del modo Creativo.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
