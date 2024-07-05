package com.prohitman.dragonsdungeons.core.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.IExtendedReach;
import com.prohitman.dragonsdungeons.core.network.DDPacketHandler;
import com.prohitman.dragonsdungeons.core.network.MessageExtendedReachAttack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DragonsDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            DDPacketHandler.init();
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(InputEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getMainHandItem().getItem() instanceof IExtendedReach) {

                if (((IExtendedReach) player.getMainHandItem().getItem()).getReach() > 5.0f) {
                    Options keys = Minecraft.getInstance().options;
                    if (keys.keyAttack.consumeClick()) {
                        extendAttackReach(player);
                    }
                }
            }
        }
    }

    private static void extendAttackReach(LocalPlayer thePlayer) {
        //if (!thePlayer.isRowingBoat()) {// maybe player.isPassenger()
        ItemStack itemstack = thePlayer.getMainHandItem();
        if (itemstack.getItem() instanceof IExtendedReach ieri) {
            float reach = ieri.getReach();
            getMouseOverExtended2(reach);
            if (!thePlayer.isHandsBusy()) {
                var inputEvent = ForgeHooksClient.onClickInput(0, Minecraft.getInstance().options.keyAttack, InteractionHand.MAIN_HAND);
                if (!inputEvent.isCanceled()) {
                    switch (Minecraft.getInstance().hitResult.getType()) {
                        case ENTITY: {
                            DDPacketHandler.HANDLER.sendToServer(new MessageExtendedReachAttack(((EntityHitResult) Minecraft.getInstance().hitResult).getEntity().getId()));

                            if (!thePlayer.isSpectator()) {
                                Minecraft.getInstance().gameMode.attack(thePlayer, ((EntityHitResult) Minecraft.getInstance().hitResult).getEntity());
                                thePlayer.resetAttackStrengthTicker();
                            }
                            break;
                        }
                        case BLOCK:
                            // Only extends attack reach, not breaking reach
                            BlockHitResult blockraytraceresult = (BlockHitResult) Minecraft
                                    .getInstance().hitResult;
                            BlockPos blockpos = blockraytraceresult.getBlockPos();
                            if (!Minecraft.getInstance().level
                                    .isEmptyBlock(blockpos) /* && thePlayer.pos.getDistanceSq(blockpos) <= 25 */) {
                                Minecraft.getInstance().gameMode.startDestroyBlock(blockpos,
                                        blockraytraceresult.getDirection());
                                break;
                            }

                        case MISS:

						/*if (Minecraft.getInstance().gameMode.hasMissTime()) {
							//Minecraft.getInstance().missTime = 10;
						}*/

                            thePlayer.resetAttackStrengthTicker();
                            ForgeHooks.onEmptyLeftClick(thePlayer);
                    }
                    if (inputEvent.shouldSwingHand())
                        thePlayer.swing(InteractionHand.MAIN_HAND);

                }
            }
        }
    }

    //From GameRenderer
    public static void getMouseOverExtended2(float dist) {
        Entity entity = Minecraft.getInstance().getCameraEntity();

        if (entity != null) {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().getProfiler().push("pick");
                double d0 = dist;
                Minecraft.getInstance().hitResult = entity.pick(d0, 1.0F, false);
                Vec3 Vector3d = entity.getEyePosition(1.0F);
                boolean flag = false;
                double d1 = d0;

                {
                    if (d0 > 3.0D) {
                        flag = true;
                    }
                }

                d1 = Minecraft.getInstance().hitResult.getLocation().distanceToSqr(Vector3d);

                d1 = d1 * d1;

                Vec3 Vector3d1 = entity.getViewVector(1.0F);
                Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * d0, Vector3d1.y * d0, Vector3d1.z * d0);
                AABB axisalignedbb = entity.getBoundingBox().expandTowards(Vector3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityraytraceresult = ProjectileUtil.getEntityHitResult(entity, Vector3d, Vector3d2,
                        axisalignedbb, (Entity) -> !Entity.isSpectator() && Entity.isPickable(), d1);
                if (entityraytraceresult != null) {
                    Entity pointedEntity = entityraytraceresult.getEntity();
                    Vec3 Vector3d3 = entityraytraceresult.getLocation();
                    double d2 = Vector3d.distanceToSqr(Vector3d3);

                    if (pointedEntity != null && flag && d2 > d1) {
                        Minecraft.getInstance().hitResult = BlockHitResult.miss(Vector3d3,
                                Direction.getNearest(Vector3d1.x, Vector3d1.y, Vector3d1.z), BlockPos.containing(Vector3d3));
                    }

                    else if (pointedEntity != null && (d2 < d1 || Minecraft.getInstance().hitResult == null)) {
                        Minecraft.getInstance().hitResult = entityraytraceresult;

                        if (pointedEntity instanceof LivingEntity || pointedEntity instanceof ItemFrame) {
                            Minecraft.getInstance().crosshairPickEntity = pointedEntity;
                        }
                    }
                }

                Minecraft.getInstance().getProfiler().pop();
            }
        }
    }
}
