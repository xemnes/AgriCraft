package com.infinityraider.agricraft.api.v1.soil;

import com.infinityraider.agricraft.api.v1.misc.IAgriRegisterable;

import java.util.Collection;
import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;

/**
 * Class for interacting with AgriCraft soil definitions.
 */
public interface IAgriSoil extends IAgriRegisterable<IAgriSoil> {

    @Nonnull
    @Override
    String getId();

    @Nonnull
    String getName();

    @Nonnull
    Collection<BlockState> getVariants();

    default boolean isVariant(@Nonnull BlockState state) {
        return this.getVariants().contains(state);
    }

}
