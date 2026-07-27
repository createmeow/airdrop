package xaero.hud.minimap.radar.icon.creator.entity;

import net.minecraft.world.entity.LivingEntity;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/entity/LivingEntityPoseResetter.class */
public class LivingEntityPoseResetter {
    private float yRotO;
    private float xRotO;
    private float yHeadRotO;
    private float speedO;
    private float oAttackAnim;
    private float yBodyRotO;
    private float yRot;
    private float xRot;
    private float yHeadRot;
    private float speed;
    private float attackAnim;
    private float yBodyRot;
    private int tickCount;

    public void storeAndReset(LivingEntity livingEntity) {
        this.yRotO = livingEntity.yRotO;
        this.xRotO = livingEntity.xRotO;
        this.yHeadRotO = livingEntity.yHeadRotO;
        this.speedO = livingEntity.walkAnimation.speed(0.0f);
        this.oAttackAnim = livingEntity.oAttackAnim;
        this.yBodyRotO = livingEntity.yBodyRotO;
        this.yRot = livingEntity.getYRot();
        this.xRot = livingEntity.getXRot();
        this.yHeadRot = livingEntity.yHeadRot;
        this.speed = livingEntity.walkAnimation.speed();
        this.attackAnim = livingEntity.attackAnim;
        this.yBodyRot = livingEntity.yBodyRot;
        this.tickCount = livingEntity.tickCount;
        livingEntity.walkAnimation.setSpeed(0.0f);
        livingEntity.yRotO = 0.0f;
        livingEntity.xRotO = 0.0f;
        livingEntity.yHeadRotO = 0.0f;
        livingEntity.walkAnimation.update(0.0f, 0.0f);
        livingEntity.oAttackAnim = 0.0f;
        livingEntity.yBodyRotO = 0.0f;
        livingEntity.setYRot(0.0f);
        livingEntity.setXRot(0.0f);
        livingEntity.yHeadRot = 0.0f;
        livingEntity.attackAnim = 0.0f;
        livingEntity.yBodyRot = 0.0f;
        livingEntity.tickCount = 10;
    }

    public void restore(LivingEntity livingEntity) {
        livingEntity.walkAnimation.setSpeed(0.0f);
        livingEntity.walkAnimation.update(-this.speedO, 1.0f);
        livingEntity.walkAnimation.setSpeed(this.speedO);
        livingEntity.walkAnimation.update(0.0f, 0.0f);
        livingEntity.walkAnimation.setSpeed(this.speed);
        livingEntity.yRotO = this.yRotO;
        livingEntity.xRotO = this.xRotO;
        livingEntity.yHeadRotO = this.yHeadRotO;
        livingEntity.oAttackAnim = this.oAttackAnim;
        livingEntity.yBodyRotO = this.yBodyRotO;
        livingEntity.setYRot(this.yRot);
        livingEntity.setXRot(this.xRot);
        livingEntity.yHeadRot = this.yHeadRot;
        livingEntity.attackAnim = this.attackAnim;
        livingEntity.yBodyRot = this.yBodyRot;
        livingEntity.tickCount = this.tickCount;
    }
}
