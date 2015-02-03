package com.InfinityRaider.AgriCraft.container;

import com.InfinityRaider.AgriCraft.reference.Names;
import com.InfinityRaider.AgriCraft.tileentity.TileEntitySeedStorage;
import com.InfinityRaider.AgriCraft.utility.LogHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ContainerSeedStorage extends ContainerAgricraft {
    //one hash map to quickly find the correct slot based on a stack and another based on the slot id
    public HashMap<ItemSeeds, HashMap<Integer, ArrayList<SlotSeedStorage>>> entries;
    public HashMap<Integer, SlotSeedStorage> seedSlots;
    public TileEntitySeedStorage te;
    private int lastSlotId;

    public ContainerSeedStorage(InventoryPlayer inventory, TileEntitySeedStorage te) {
        super(inventory, 82, 94);
        this.lastSlotId = this.PLAYER_INVENTORY_SIZE -1;
        this.te = te;
        this.entries = new HashMap<ItemSeeds, HashMap<Integer, ArrayList<SlotSeedStorage>>>();
        this.seedSlots = new HashMap<Integer, SlotSeedStorage>();
        for(ItemStack seedStack:te.getInventory()) {
            if(seedStack!=null && seedStack.getItem()!=null) {
                this.addSeedToStorage(seedStack);
            }
        }
    }

    //tries to add a stack to the storage, return true on success
    public boolean addSeedToStorage(ItemStack stack) {
        boolean success = false;
        ItemSeeds seed = (ItemSeeds) stack.getItem();
        //There is a value for this seed
        if(this.entries.get(seed) != null) {
            HashMap<Integer, ArrayList<SlotSeedStorage>> metaMap = this.entries.get(seed);
            //There is a value for this meta
            if(metaMap.get(stack.getItemDamage())!=null && metaMap.get(stack.getItemDamage()).size()>0) {
                for(SlotSeedStorage slot:metaMap.get(stack.getItemDamage())) {
                    if(slot!=null && slot.getStack()!=null) {
                        ItemStack stackInSlot = slot.getStack();
                        if(stackInSlot!=null && stackInSlot.getItem()!=null && stack.hasTagCompound() && ItemStack.areItemStackTagsEqual(stackInSlot, stack)) {
                            slot.putStack(stack);
                            success = true;
                            break;
                        }
                    }
                }
            }
            //There is no value for this meta yet
            else {
                //create new array list for this seed & meta
                ArrayList<SlotSeedStorage> newList = new ArrayList<SlotSeedStorage>();
                newList.add(this.getNewSeedSlot(stack));
                metaMap.put(stack.getItemDamage(), newList);
                success = true;
            }
        }
        //There is not yet a value for this seed
        else {
            //create new array list for this seed & meta
            ArrayList<SlotSeedStorage> newList = new ArrayList<SlotSeedStorage>();
            newList.add(this.getNewSeedSlot(stack));
            //create new hash map for this seed
            HashMap<Integer, ArrayList<SlotSeedStorage>> newMetaMap = new HashMap<Integer, ArrayList<SlotSeedStorage>>();
            newMetaMap.put(stack.getItemDamage(), newList);
            this.entries.put(seed, newMetaMap);
            success = true;
        }
        return success;
    }

    public SlotSeedStorage getNewSeedSlot(ItemStack stack) {
        this.lastSlotId = this.lastSlotId+1;
        SlotSeedStorage newSlot = new SlotSeedStorage(this, this.te, this.lastSlotId, 0, 0, stack);
        this.seedSlots.put(lastSlotId, newSlot);
        return newSlot;
    }

    public ArrayList<SlotSeedStorage> getEntries() {
        ArrayList<SlotSeedStorage> slots = new ArrayList<SlotSeedStorage>();
        slots.addAll(this.seedSlots.values());
        return slots;
    }

    /**
     * returns a list if itemStacks, for each slot.
     */
    @Override
    public List getInventory() {
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < this.inventorySlots.size(); ++i) {
            arraylist.add(((Slot)this.inventorySlots.get(i)).getStack());
        }
        for(SlotSeedStorage slot:this.seedSlots.values())
        arraylist.add(slot.getStack());
        return arraylist;
    }

    @Override
    public Slot getSlotFromInventory(IInventory inventory, int slotIndex) {
        Slot slot = this.seedSlots.get(slotIndex);
        if(slot!=null && slot instanceof SlotSeedStorage) {
            if (slot.isSlotInInventory(inventory, slotIndex)) {
                return slot;
            }
        }
        return super.getSlotFromInventory(inventory, slotIndex);
    }

    @Override
    public Slot getSlot(int id) {
        if(id<this.PLAYER_INVENTORY_SIZE) {
            return (Slot) this.inventorySlots.get(id);
        }
        else {
            return this.seedSlots.get(id);
        }
    }

    /**
     * places item stacks in the first x slots, x being itemstack.length
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void putStacksInSlots(ItemStack[] stackArray) {
        for (int i=0;i<Math.min(stackArray.length,this.PLAYER_INVENTORY_SIZE);++i) {
            this.getSlot(i).putStack(stackArray[i]);
        }
        for(int i=this.PLAYER_INVENTORY_SIZE;i<stackArray.length;i++) {
            this.addSeedToStorage(stackArray[i]);
        }
    }

    public void setActiveEntries(ItemStack stack) {
        if(stack!=null && stack.getItem()!=null) {
            ItemSeeds seed = (ItemSeeds) stack.getItem();
            int seedMeta = stack.getItemDamage();
            ArrayList<SlotSeedStorage> activeEntries = this.entries.get(seed).get(seedMeta);
            if (activeEntries != null) {
                int xOffset = 82;
                int yOffset = 8;
                for (int i = 0; i < activeEntries.size(); i++) {
                    SlotSeedStorage slot = activeEntries.get(i);
                    slot.set(xOffset + 16 * i, yOffset, this.PLAYER_INVENTORY_SIZE+i);
                    this.inventorySlots.add(slot);
                    this.inventoryItemStacks.add(slot.getStack());
                }
            }
        }
    }

    public void clearActiveEntries() {
        for(int i=this.inventoryItemStacks.size()-1;i>=this.PLAYER_INVENTORY_SIZE;i--) {
            ((SlotSeedStorage) this.inventorySlots.get(i)).reset();
            this.inventorySlots.remove(i);
            this.inventoryItemStacks.remove(i);
        }
    }

    //checks if the player can drag a stack over this slot to split it
    public boolean canDragIntoSlot(Slot slot)
    {
        return !(slot instanceof SlotSeedStorage);
    }

    //this gets called when a player shift clicks a stack into the inventory
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlot) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(clickedSlot);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            //try to move item from the container into the player's inventory
            if (clickedSlot>=this.PLAYER_INVENTORY_SIZE) {
                if (!this.mergeItemStack(itemstack1, 0, 36, false)) {
                    return null;
                }
            }
            else {
                //try to move item from the player's inventory into the container
                if(itemstack1.getItem()!=null) {
                    if(itemstack1.getItem() instanceof ItemSeeds && itemstack1.hasTagCompound() && itemstack1.stackTagCompound.hasKey(Names.NBT.analyzed) && itemstack1.stackTagCompound.getBoolean(Names.NBT.analyzed)) {
                        if (this.addSeedToStorage(itemstack1)) {
                            itemstack1.stackSize=0;
                        }
                        else {
                            return null;
                        }
                    }
                }
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
                if (clickedSlot>=this.PLAYER_INVENTORY_SIZE) {slot = null;}
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startSlot, int endSlot, boolean iterateBackwards) {
        boolean flag = false;
        int k = iterateBackwards?endSlot - 1:startSlot;
        Slot currentSlot;
        ItemStack currentStack;
        while (stack.stackSize > 0 && (!iterateBackwards && k < endSlot || iterateBackwards && k >= startSlot)) {
            currentSlot = (Slot)this.inventorySlots.get(k);
            currentStack = currentSlot.getStack();
            if (currentStack != null && currentStack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == currentStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, currentStack)) {
                int l = currentStack.stackSize + stack.stackSize;
                if (l <= stack.getMaxStackSize()) {
                    stack.stackSize = 0;
                    currentStack.stackSize = l;
                    currentSlot.onSlotChanged();
                    flag = true;
                }
                else if (currentStack.stackSize < stack.getMaxStackSize()) {
                    stack.stackSize -= stack.getMaxStackSize() - currentStack.stackSize;
                    currentStack.stackSize = stack.getMaxStackSize();
                    currentSlot.onSlotChanged();
                    flag = true;
                }
            }
            k = iterateBackwards?k-1:k+1;
        }
        if (stack.stackSize > 0) {
            k = iterateBackwards?endSlot-1:startSlot;
            while (!iterateBackwards && k < endSlot || iterateBackwards && k >= startSlot) {
                currentSlot = (Slot)this.inventorySlots.get(k);
                currentStack = currentSlot.getStack();
                if (currentStack == null) {
                    currentSlot.putStack(stack.copy());
                    currentSlot.onSlotChanged();
                    stack.stackSize = 0;
                    flag = true;
                    break;
                }
                k = iterateBackwards?k-1:k+1;
            }
        }
        return flag;
    }

    //par1: slotIndex
    //par2: 0 = LMB, 1 = RMB, 2 = MMB
    //par3: 1 = shift, 3 = MMB
    @Override
    public ItemStack slotClick(int slotIndex, int mouseButton, int shiftHeld, EntityPlayer player) {
        LogHelper.debug("Slot CLicked: par1 = "+slotIndex+", par2 = "+mouseButton+", par3 = "+shiftHeld);
        if(slotIndex>=this.PLAYER_INVENTORY_SIZE) {
            SlotSeedStorage slot = (SlotSeedStorage) this.getSlot(slotIndex);
        }
        return super.slotClick(slotIndex, mouseButton, shiftHeld, player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        this.te.setInventory(this.entries);
        InventoryPlayer inventoryplayer = player.inventory;
        if (inventoryplayer.getItemStack() != null) {
            player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
            inventoryplayer.setItemStack(null);
        }
    }
}