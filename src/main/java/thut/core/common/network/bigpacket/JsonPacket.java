package thut.core.common.network.bigpacket;

import java.io.UnsupportedEncodingException;

import net.minecraft.network.FriendlyByteBuf;
import thut.api.util.JsonUtil;

public abstract class JsonPacket extends BigPacket
{

    public JsonPacket()
    {
        super();
    }

    public JsonPacket(final FriendlyByteBuf buffer)
    {
        super(buffer);
    }

    public JsonPacket(Object o)
    {
        super();
        String json = JsonUtil.gson.toJson(o);
        try
        {
            this.setData(json.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}
