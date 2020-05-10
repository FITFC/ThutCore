package thut.core.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import thut.core.common.ThutCore;

public class EntityUpdate extends NBTPacket
{
    public static final PacketAssembly<EntityUpdate> ASSEMBLER = PacketAssembly.registerAssembler(EntityUpdate.class,
            EntityUpdate::new, ThutCore.packets);

    public static void sendEntityUpdate(final Entity entity)
    {
        if (entity.getEntityWorld().isRemote)
        {
            ThutCore.LOGGER.error("Packet sent on wrong side!", new IllegalArgumentException());
            return;
        }
        final CompoundNBT tag = new CompoundNBT();
        tag.putInt("id", entity.getEntityId());
        final CompoundNBT mobtag = new CompoundNBT();
        entity.writeWithoutTypeId(mobtag);
        tag.put("tag", mobtag);
        final EntityUpdate message = new EntityUpdate(tag);
        EntityUpdate.ASSEMBLER.sendToTracking(message, entity);
    }

    public EntityUpdate()
    {
        super();
    }

    public EntityUpdate(final CompoundNBT tag)
    {
        super(tag);
    }

    public EntityUpdate(final PacketBuffer buffer)
    {
        super(buffer);
    }

    @Override
    public void write(final PacketBuffer buffer)
    {
        buffer.writeCompoundTag(this.getTag());
    }

    @Override
    protected void onCompleteClient()
    {
        final PlayerEntity player = ThutCore.proxy.getPlayer();
        final int id = this.getTag().getInt("id");
        final Entity mob = player.getEntityWorld().getEntityByID(id);
        if (mob != null) mob.read(this.getTag().getCompound("tag"));
    }
}
