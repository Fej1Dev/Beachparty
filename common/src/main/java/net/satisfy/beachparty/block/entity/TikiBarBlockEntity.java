package net.satisfy.beachparty.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.beachparty.client.gui.handler.TikiBarGuiHandler;
import net.satisfy.beachparty.recipe.TikiBarRecipe;
import net.satisfy.beachparty.registry.EntityTypeRegistry;
import net.satisfy.beachparty.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public class TikiBarBlockEntity extends BlockEntity implements WorldlyContainer, MenuProvider {
    private static final int[] SLOTS_FOR_SIDE = new int[]{2};
    private static final int[] SLOTS_FOR_UP = new int[]{1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0};
    private NonNullList<ItemStack> inventory;
    public static final int CAPACITY = 3;
    private static final int OUTPUT_SLOT = 0;
    private int shakingTime = 0;
    private int totalShakingTime;
    protected float experience;

    private final ContainerData propertyDelegate = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> TikiBarBlockEntity.this.shakingTime;
                case 1 -> TikiBarBlockEntity.this.totalShakingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> TikiBarBlockEntity.this.shakingTime = value;
                case 1 -> TikiBarBlockEntity.this.totalShakingTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public TikiBarBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.TIKI_BAR_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
        this.shakingTime = nbt.getShort("ShakingTime");
        this.experience = nbt.getFloat("Experience");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putFloat("Experience", this.experience);
        nbt.putShort("ShakingTime", (short) this.shakingTime);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, TikiBarBlockEntity blockEntity) {
        if (world.isClientSide) return;

        boolean dirty = false;
        TikiBarRecipe recipe = world.getRecipeManager()
                .getRecipeFor(RecipeRegistry.TIKI_BAR_RECIPE_TYPE.get(), blockEntity, world)
                .orElse(null);
        RegistryAccess access = world.registryAccess();

        if (blockEntity.canCraft(recipe, access)) {
            blockEntity.shakingTime++;

            if (blockEntity.shakingTime >= blockEntity.totalShakingTime) {
                blockEntity.shakingTime = 0;
                blockEntity.craft(recipe, access);
                dirty = true;
            }
        } else {
            blockEntity.shakingTime = 0;
        }

        if (dirty) {
            blockEntity.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private boolean canCraft(@Nullable TikiBarRecipe recipe, RegistryAccess access) {
        if (recipe == null) return false;

        ItemStack recipeResultItem = recipe.getResultItem(access);
        if (recipeResultItem.isEmpty() || areInputsEmpty()) return false;

        ItemStack outputSlotItem = getItem(OUTPUT_SLOT);
        if (outputSlotItem.isEmpty()) return true;

        return ItemStack.isSameItem(outputSlotItem, recipeResultItem) &&
                outputSlotItem.getCount() + recipeResultItem.getCount() <= outputSlotItem.getMaxStackSize();
    }

    private boolean areInputsEmpty() {
        int emptyStacks = 0;
        for (int i = 1; i <= 2; i++) {
            if (this.getItem(i).isEmpty()) emptyStacks++;
        }
        return emptyStacks == 2;
    }

    private void craft(TikiBarRecipe recipe, RegistryAccess access) {
        if (!canCraft(recipe, access)) {
            return;
        }
        ItemStack recipeOutput = recipe.getResultItem(access).copy();
        ItemStack outputSlotStack = getItem(OUTPUT_SLOT);

        if (outputSlotStack.isEmpty()) {
            setItem(OUTPUT_SLOT, recipeOutput);
        } else if (ItemStack.isSameItem(outputSlotStack, recipeOutput)) {
            outputSlotStack.grow(recipeOutput.getCount());
            if (outputSlotStack.getCount() > outputSlotStack.getMaxStackSize()) {
                outputSlotStack.setCount(outputSlotStack.getMaxStackSize());
            }
        }

        consumeIngredients(recipe);
    }

    private void consumeIngredients(TikiBarRecipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            for (int i = 1; i <= 2; i++) {
                ItemStack slotItem = this.getItem(i);
                if (ingredient.test(slotItem)) {
                    ItemStack remainder = getRemainderItem(slotItem);
                    slotItem.shrink(1);
                    if (slotItem.isEmpty() && !remainder.isEmpty()) {
                        this.setItem(i, remainder);
                    } else if (slotItem.isEmpty()) {
                        this.setItem(i, ItemStack.EMPTY);
                    }
                    break;
                }
            }
        }
    }

    private ItemStack getRemainderItem(ItemStack stack) {
        if (stack.getItem().hasCraftingRemainingItem()) {
            return new ItemStack(Objects.requireNonNull(stack.getItem().getCraftingRemainingItem()));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side.equals(Direction.UP)) {
            return SLOTS_FOR_UP;
        } else if (side.equals(Direction.DOWN)) {
            return SLOTS_FOR_DOWN;
        } else return SLOTS_FOR_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return CAPACITY;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(Predicate.isEqual(ItemStack.EMPTY));
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return this.inventory.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(this.inventory, i, j);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return this.inventory.remove(i);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        final ItemStack stackInSlot = this.inventory.get(slot);
        boolean dirty = !stack.isEmpty() && ItemStack.isSameItem(stack, stackInSlot) && ItemStack.matches(stack, stackInSlot);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == 1 || slot == 2) {
            if (!dirty) {
                this.totalShakingTime = 50;
                this.shakingTime = 0;
                setChanged();
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null) return false;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr(
                    (double) this.worldPosition.getX() + 0.5,
                    (double) this.worldPosition.getY() + 0.5,
                    (double) this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new TikiBarGuiHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }
}
