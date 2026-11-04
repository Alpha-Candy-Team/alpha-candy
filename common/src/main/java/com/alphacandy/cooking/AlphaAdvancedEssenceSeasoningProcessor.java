package com.alphacandy.cooking;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;

import java.util.List;

/**
 * EN: Seasoning processor for cooking recipes producing the guaranteed Advanced Alpha Candy.
 * ES: Procesador de condimentos para recetas de cocina que producen el Caramelo Alfa Avanzado garantizado.
 */
public final class AlphaAdvancedEssenceSeasoningProcessor implements SeasoningProcessor {

    // EN: Shared stateless processor registered with Cobblemon. / ES: Procesador sin estado compartido y registrado en Cobblemon.
    public static final AlphaAdvancedEssenceSeasoningProcessor INSTANCE = new AlphaAdvancedEssenceSeasoningProcessor();

    /** EN: Identifier matching the Cobblemon seasoning recipe definition. / ES: Identificador que coincide con la receta de condimentos de Cobblemon. */
    private static final String TYPE = "alphacandy:advanced_essence";

    /** EN: Custom NBT tag key storing the applied essence level. / ES: Clave NBT personalizada que guarda el nivel de esencia aplicado. */
    private static final String ESSENCE_LEVEL = "alphacandy:advanced_essence_level";

    /** EN: Restricts construction to the singleton. / ES: Restringe la creación a la instancia única. */
    private AlphaAdvancedEssenceSeasoningProcessor() {
    }

    @Override
    /** EN: Returns this processor's recipe identifier. / ES: Devuelve el identificador de receta del procesador. */
    public String getType() {
        return TYPE;
    }

    @Override
    /** EN: Sets the advanced candy essence data to its fixed maximum value. / ES: Establece los datos de esencia del caramelo avanzado en su máximo fijo. */
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        // EN: Advanced recipes encode their guaranteed level directly in the output without extra iteration.
        // ES: Las recetas avanzadas fijan el nivel garantizado directamente en el resultado sin iteración innecesaria.
        CompoundTag tag = new CompoundTag();
        tag.putInt(ESSENCE_LEVEL, 3);

        result.set(
                DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.of(tag)
        );
    }

    @Override
    /** EN: Consumes Alpha Essence when it is used as seasoning. / ES: Consume la esencia Alfa cuando se usa como condimento. */
    public boolean consumesItem(ItemStack seasoning) {
        return seasoning.is(AlphaCandyMod.alpha_essence.get());
    }
}
