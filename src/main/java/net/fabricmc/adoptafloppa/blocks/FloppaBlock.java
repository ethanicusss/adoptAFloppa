package net.fabricmc.adoptafloppa.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Wearable;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class FloppaBlock extends HorizontalFacingBlock implements Wearable {

    public static final DirectionProperty FACING;

    public FloppaBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }
    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}
