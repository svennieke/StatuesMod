package com.shynieke.statues.packets;

import com.shynieke.statues.entity.PlayerStatueEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class PlayerStatueSyncMessage {
    private UUID entityUUID;
    private CompoundNBT data;

    private PlayerStatueSyncMessage(PacketBuffer buf) {
        this.entityUUID = buf.readUniqueId();
        this.data = buf.readCompoundTag();
    }

    public PlayerStatueSyncMessage(UUID playerUUID, CompoundNBT tag) {
        this.entityUUID = playerUUID;
        this.data = tag;
    }

    public void encode(PacketBuffer buf) {
        buf.writeUniqueId(entityUUID);
        buf.writeCompoundTag(data);
    }

    public static PlayerStatueSyncMessage decode(final PacketBuffer packetBuffer) {
        return new PlayerStatueSyncMessage(packetBuffer.readUniqueId(), packetBuffer.readCompoundTag());
    }

    public void handle(Supplier<Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isServer()) {
                final ServerPlayerEntity player = ctx.getSender();
                final ServerWorld world = ctx.getSender().getServerWorld();
                Entity entity = world.getEntityByUuid(this.entityUUID);
                if (entity instanceof PlayerStatueEntity && player != null) {
                    PlayerStatueEntity playerStatue = (PlayerStatueEntity)entity;
                    if(!playerStatue.isLocked() || (playerStatue.getLockedBy().equals(player.getUniqueID()))) {
                        CompoundNBT entityTag = playerStatue.writeWithoutTypeId(new CompoundNBT());
                        CompoundNBT entityTagCopy = entityTag.copy();

                        if(!this.data.isEmpty()) {
                            entityTagCopy.merge(this.data);
                            UUID uuid = playerStatue.getUniqueID();
                            playerStatue.read(entityTagCopy);
                            playerStatue.setUniqueId(uuid);
                        }

                        float YOffset = data.getFloat("yOffset");
                        playerStatue.setYOffset(YOffset);

                        boolean lockFlag = data.getBoolean("Locked");
                        if(lockFlag) {
                            if(!playerStatue.isLocked()) {
                                playerStatue.setLockedBy(player.getUniqueID());
                            }
                        } else {
                            if(playerStatue.isLocked()) {
                                playerStatue.setUnlocked();
                            }
                        }

                        boolean noGravityFlag = data.getBoolean("NoGravity");
                        if(noGravityFlag) {
                            playerStatue.setNoGravity(true);
                        } else {
                            playerStatue.setNoGravity(false);
                        }
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
