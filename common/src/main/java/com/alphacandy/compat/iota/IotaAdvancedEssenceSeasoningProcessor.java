package com.alphacandy.compat.iota;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import java.util.List;

/**
 * EN: Seasoning processor for cooking recipes producing the guaranteed Advanced Iota Candy.
 * ES: Procesador de condimentos para recetas de cocina que producen el Caramelo Iota Avanzado garantizado.
 */
public final class IotaAdvancedEssenceSeasoningProcessor implements SeasoningProcessor {

    // EN: Shared processor for advanced recipes with a fixed essence level. / ES: Procesador compartido para recetas avanzadas de nivel fijo.
    public static final IotaAdvancedEssenceSeasoningProcessor INSTANCE =
            new IotaAdvancedEssenceSeasoningProcessor();

    /** EN: Identifier matching the Cobblemon seasoning recipe definition. / ES: Identificador que coincide con la receta de condimentos de Cobblemon. */
    private static final String TYPE = "alphacandy:iota_advanced_essence";

    /** EN: Custom NBT tag key storing the applied essence level. / ES: Clave NBT personalizada que guarda el nivel de esencia aplicado. */
    private static final String ESSENCE_LEVEL = "alphacandy:iota_essence_level";

    /** EN: Prevents duplicate processor instances. / ES: Evita instancias duplicadas del procesador. */
    private IotaAdvancedEssenceSeasoningProcessor() {}

    @Override
    /** EN: Returns the ID referenced by advanced Iota recipe data. / ES: Devuelve el ID usado por las recetas Iota avanzadas. */
    public String getType() {
        return TYPE;
    }

    @Override
    /** EN: Writes the guaranteed level three value to the output item. / ES: Escribe el nivel tres garantizado en el objeto resultante. */
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(ESSENCE_LEVEL, 3);
        result.set(DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.of(tag));
    }

    @Override
    /** EN: Consumes Iota Essence if its registered supplier is available. / ES: Consume esencia Iota si está disponible el proveedor registrado. */
    public boolean consumesItem(ItemStack seasoning) {
        return AlphaCandyMod.IOTA_ESSENCE != null
                && seasoning.is(AlphaCandyMod.IOTA_ESSENCE.get());
    }
}
