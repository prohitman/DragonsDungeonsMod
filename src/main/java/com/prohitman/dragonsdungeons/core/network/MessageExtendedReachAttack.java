package com.prohitman.dragonsdungeons.core.network;

import com.prohitman.dragonsdungeons.common.items.IExtendedReach;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageExtendedReachAttack implements IMessage<MessageExtendedReachAttack> {
    private int entityId;

    public MessageExtendedReachAttack() {

    }

    public MessageExtendedReachAttack(int parEntityId) {
        this.entityId = parEntityId;
    }

    @Override
    public void encode(MessageExtendedReachAttack pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.entityId);
    }

    @Override
    public MessageExtendedReachAttack decode(FriendlyByteBuf buf) {
        return new MessageExtendedReachAttack(buf.readInt());
    }

    @Override
    public void handle(MessageExtendedReachAttack message, Supplier<NetworkEvent.Context> ctx) {
        // DEBUG
        //System.out.println("Message received");
        // Know it will be on the server so make it thread-safe
        final ServerPlayer thePlayer = ctx.get().getSender();
        ctx.get().enqueueWork(() -> {

            Entity theEntity = thePlayer.level().getEntity(message.entityId);
            // DEBUG
            System.out.println("Entity = " + theEntity);

            // Need to ensure that hackers can't cause trick kills,
            // so double check weapon type and reach

            if (thePlayer.getMainHandItem().getItem() instanceof IExtendedReach theExtendedReachWeapon) {
                thePlayer.getMainHandItem().getItem();
                if (theEntity != null) {

                    double distanceSq = thePlayer.distanceToSqr(theEntity);

                    double reachSq = theExtendedReachWeapon.getReach() * theExtendedReachWeapon.getReach();

                    if (!thePlayer.hasLineOfSight(theEntity)) {
                        reachSq /= 4.0d;
                    }

                    if (reachSq >= distanceSq) {
                        if (theEntity instanceof ItemEntity || theEntity instanceof ExperienceOrb
                                || theEntity instanceof AbstractArrow || theEntity == thePlayer) {
                            thePlayer.connection.disconnect(Component.translatable(
                                    "multiplayer.disconnect.invalid_entity_attacked"));
                            // this.serverController.logWarning("Player " + thePlayer.getName() + " tried to
                            // attack an invalid entity");
                        }
                        thePlayer.attack(theEntity);
                    }
                } // no response in this case
            }
        });
        ctx.get().setPacketHandled(true);// no response message
    }
}
