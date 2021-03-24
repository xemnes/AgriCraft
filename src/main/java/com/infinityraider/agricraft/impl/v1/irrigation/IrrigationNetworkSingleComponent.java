package com.infinityraider.agricraft.impl.v1.irrigation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.infinityraider.agricraft.api.v1.irrigation.IAgriIrrigationComponent;
import com.infinityraider.agricraft.api.v1.irrigation.IAgriIrrigationConnection;
import com.infinityraider.agricraft.api.v1.irrigation.IAgriIrrigationNetwork;
import com.infinityraider.agricraft.api.v1.irrigation.IAgriIrrigationNode;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class IrrigationNetworkSingleComponent implements IAgriIrrigationNetwork {
    private final IAgriIrrigationComponent component;
    private final Direction direction;
    private final IAgriIrrigationNode node;

    private final Set<IAgriIrrigationNode> nodes;
    private final Map<IAgriIrrigationNode, Set<IAgriIrrigationConnection>> connections;

    public IrrigationNetworkSingleComponent(IAgriIrrigationComponent component, @Nullable Direction direction, IAgriIrrigationNode node) {
        this.component = component;
        this.direction = direction;
        this.node = node;
        this.nodes = ImmutableSet.of(this.getNode());
        this.connections = new ImmutableMap.Builder<IAgriIrrigationNode, Set<IAgriIrrigationConnection>>().put(this.getNode(), ImmutableSet.of()).build();
    }

    public IAgriIrrigationComponent getComponent() {
        return this.component;
    }

    @Nullable
    public Direction getDirection() {
        return this.direction;
    }

    public IAgriIrrigationNode getNode() {
        return this.node;
    }

    @Nullable
    @Override
    public World getWorld() {
        return this.getComponent().getTile().getWorld();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Nonnull
    @Override
    public Set<IAgriIrrigationNode> nodes() {
        return this.nodes;
    }

    @Nonnull
    @Override
    public Map<IAgriIrrigationNode, Set<IAgriIrrigationConnection>> connections() {
        return this.connections;
    }

    @Override
    public int capacity() {
        return this.getNode().getFluidCapacity();
    }

    @Override
    public double fluidHeight() {
        return this.getNode().getFluidHeight();
    }

    @Override
    public int contents() {
        return this.getNode().getFluidContents();
    }

    @Override
    public void setContents(int value) {
        this.getNode().setFluidContents(value);

    }

    @Nonnull
    @Override
    public FluidStack contentAsFluidStack() {
        return this.contents() <= 0 ? FluidStack.EMPTY : new FluidStack(Fluids.WATER, this.contents());
    }
}