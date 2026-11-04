package com.alphacandy.cooking;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;

import java.util.List;

/**
 * EN: Seasoning processor for cooking recipes producing the chance-based Alpha Candy.
 * ES: Procesador de condimentos para recetas de cocina que producen el Caramelo Alfa con probabilidad.
 */
public final class AlphaEssenceSeasoningProcessor implements SeasoningProcessor {

    // EN: Cobblemon recipes share this stateless processor instance. / ES: Las recetas de Cobblemon comparten esta instancia sin estado.
    public static final AlphaEssenceSeasoningProcessor INSTANCE = new AlphaEssenceSeasoningProcessor();

    /** EN: Identifier matching the Cobblemon seasoning recipe definition. / ES: Identificador que coincide con la receta de condimentos de Cobblemon. */
    private static final String TYPE = "alphacandy:essence_chance";

    /** EN: Custom NBT tag key storing the counted essence level. / ES: Clave NBT personalizada que guarda el nivel de esencia contabilizado. */
    private static final String ESSENCE_LEVEL = "alphacandy:essence_level";

    /** EN: Prevents duplicate processor instances. / ES: Evita crear instancias duplicadas del procesador. */
    private AlphaEssenceSeasoningProcessor() {
    }

    @Override
    /** EN: Returns the ID referenced by the alpha candy recipe. / ES: Devuelve el ID usado por la receta de caramelo Alfa. */
    public String getType() {
        return TYPE;
    }

    @Override
    /** EN: Counts occupied Alpha Essence seasoning stacks, caps the level at three, and stores it on the result. / ES: Cuenta los espacios ocupados por esencia Alfa, limita el nivel a tres y lo guarda en el resultado. */
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        int essenceLevel = 0;

        // EN: Count seasoning stacks up to the maximum cap of 3. / ES: Cuenta espacios de condimento hasta el límite máximo de 3.
        for (ItemStack stack : seasoning) {
            if (stack.is(AlphaCandyMod.alpha_essence.get())) {
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
    /** EN: Marks Alpha Essence as consumable seasoning. / ES: Indica que la esencia Alfa se consume como condimento. */
    public boolean consumesItem(ItemStack seasoning) {
        return seasoning.is(AlphaCandyMod.alpha_essence.get());
    }
}
