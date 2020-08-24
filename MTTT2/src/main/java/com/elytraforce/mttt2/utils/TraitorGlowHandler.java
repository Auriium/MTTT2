package main.java.com.elytraforce.mttt2.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;

import main.java.com.elytraforce.mttt2.Main;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityMetadata;

public class TraitorGlowHandler {
	//DO NOT MAKE THIS STATIC
	private Main mainClass;
	private ProtocolManager pm;
	
	public TraitorGlowHandler(Main main) {
		this.mainClass = main;
		this.pm = mainClass.getProtocolManager();
		
	}
	
	
	//Local man that does not understand packets attempts packets
	@SuppressWarnings("unchecked")
	
	public void setGlowing(Player glowingPlayer, Player sendPacket, boolean enable) {
	       
        PacketContainer packet = mainClass.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, glowingPlayer.getEntityId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        Serializer serializer = Registry.get(Byte.class);
        watcher.setEntity(glowingPlayer);
        Byte value;
        
        if (enable == false) {
        	value = 0x0;
        } else {
        	value = 0x40;
        }
        
        watcher.setObject(0, serializer, value);
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
       
        try {
            mainClass.getProtocolManager().sendServerPacket(sendPacket, packet);
        } catch (InvocationTargetException e) {
        	e.printStackTrace();
            System.out.println("Something went wrong with GlowingAPI");
        }
           
    }
}
