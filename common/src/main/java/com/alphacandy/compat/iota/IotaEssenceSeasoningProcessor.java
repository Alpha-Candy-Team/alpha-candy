package com.alphacandy.compat.iota;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * EN: Seasoning processor for cooking recipes producing the chance-based Iota Candy.
 * ES: Procesador de condimentos para recetas de cocina que producen el Caramelo Iota con probabilidad.
 */
public final class IotaEssenceSeasoningProcessor implements SeasoningProcessor {

    // EN: Stateless processor instance shared by all Iota candy recipes. / ES: Instancia sin estado compartida por las recetas de caramelos Iota.
    public static final IotaEssenceSeasoningProcessor INSTANCE =
            new IotaEssenceSeasoningProcessor();

    /** EN: Identifier matching the Cobblemon seasoning recipe definition. / ES: Identificador que coincide con la receta de condimentos de Cobblemon. */
    private static final String TYPE = "alphacandy:iota_essence_chance";

    /** EN: Custom NBT tag key storing the counted essence level. / ES: Clave NBT personalizada que guarda el nivel de esencia contabilizado. */
    private static final String ESSENCE_LEVEL = "alphacandy:iota_essence_level";

    /** EN: Restricts construction to the singleton instance. / ES: Restringe la creación a la instancia única. */
    private IotaEssenceSeasoningProcessor() {
    }

    @Override
    /** EN: Returns the recipe processor identifier. / ES: Devuelve el identificador del procesador de receta. */
    public String getType() {
        return TYPE;
    }

    @Override
    /** EN: Counts matching seasoning slots, caps the value at three, and saves it on the result. / ES: Cuenta los espacios con esencia, limita el valor a tres y lo guarda en el resultado. */
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        int essenceLevel = 0;

        // EN: Count seasoning stacks up to the maximum cap of 3. / ES: Cuenta espacios de condimento hasta el límite máximo de 3.
        for (ItemStack stack : seasoning) {
            if (stack.is(AlphaCandyMod.IOTA_ESSENCE.get())) {
                essenceLevel++;
                if (essenceLevel >= 3) {
                    break;
                }
            }
        }

        CompoundTag tag = new CompoundTag();
        tag.putInt(ESSENCE_LEVEL, essenceLevel);

        result.set(
                DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.of(tag)
        );
    }

    @Override
    /** EN: Marks Iota Essence as consumed seasoning. / ES: Marca la esencia Iota como condimento consumible. */
    public boolean consumesItem(ItemStack seasoning) {
        return seasoning.is(AlphaCandyMod.IOTA_ESSENCE.get());
    }
}
