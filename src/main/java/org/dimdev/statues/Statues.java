package org.dimdev.statues;

import java.io.File;

import javax.swing.ImageIcon;

import org.dimdev.statues.shared.containers.ContainerStatue;
import org.dimdev.statues.shared.gui.GuiSculpt;
import org.dimdev.statues.shared.gui.GuiStatue;
import org.dimdev.statues.shared.network.Packets;
import org.dimdev.statues.proxy.Proxy;
import org.dimdev.statues.shared.tileentities.TileEntityStatue;
import org.dimdev.utilities.DummyContainer;
import org.dimdev.utilities.GuiHandler;
import org.dimdev.utilities.packets.PacketHandler;
//import PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "statues", name = "Statues", version = "2.1.4")
public class Statues {
    static Configuration config;

    @Instance("Statues")
    public static Statues instance;

    @SidedProxy(clientSide = "ProxyClient", serverSide = "Proxy")
    public static Proxy proxy;

    public static PacketHandler packet;

    public static ImageIcon slotHand;

    public static GuiHandler guiStatue;
    public static GuiHandler guiSculpt;

    public static String skinServerLocation;

    public static boolean debugImages;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        config = new Configuration(configFile);
        config.load();

        GameRegistry.register(statue, new ResourceLocation("statues.statue"));
        GameRegistry.register(hammer, new ResourceLocation("statues.marteau"));
        GameRegistry.register(itemPalette, new ResourceLocation("statues.item.palette"));
        // GameRegistry.registerItem(hammer, "statues.marteau");
        // GameRegistry.registerItem(itemPalette, "statues.item.palette");

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        skinServerLocation = config.get("general", "skin server location", "http://skins.minecraft.net/MinecraftSkins/", "Download skins from this path.").getString();
        debugImages = config.get("general", "debug skins", false, "Save generated skins to files.").getBoolean(false);

        packet = new PacketHandler("statues", new Packets(), new Packets());

        /*
         * LanguageRegistry.addName(statue, "Statue"); LanguageRegistry.addName(showcase, "Showcase"); LanguageRegistry.addName(itemShowcase, "Showcase"); LanguageRegistry.addName(hammer, "Hammer"); LanguageRegistry.addName(itemPalette, "Palette");
         */

        GameRegistry.registerTileEntity(TileEntityStatue.class, new ResourceLocation("statues:tile_statue"));

        GameRegistry.addShapedRecipe(new ItemStack(hammer, 1), " I ", " SI", "S  ", 'S', Items.STICK, 'I', Items.IRON_INGOT);
        GameRegistry.addShapedRecipe(new ItemStack(itemPalette, 1), "GB", "RW", 'W', Blocks.PLANKS, 'R', new ItemStack(Items.DYE, 1, 1), 'G', new ItemStack(Items.DYE, 1, 2), 'B', new ItemStack(Items.DYE, 1, 4));

        // TODO
        // MinecraftForge.setBlockHarvestLevel(showcase, "axe", 0);

        guiStatue = new GuiHandler("statue") {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(new BlockPos(new Vec3i(x, y, z)));

                if (!(tileEntity instanceof TileEntityStatue))
                    return null;

                TileEntityStatue e = (TileEntityStatue) tileEntity;

                return new ContainerStatue(player.inventory, e);
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(new BlockPos(new Vec3i(x, y, z)));

                if (!(tileEntity instanceof TileEntityStatue))
                    return null;

                TileEntityStatue e = (TileEntityStatue) tileEntity;

                return new GuiStatue(player.inventory, e, world, x, y, z);
            }
        };

        guiSculpt = new GuiHandler("sculpt") {
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                return new DummyContainer();
            }

            @Override
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                int face = MathHelper.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;

                return new GuiSculpt(world, x, y, z, player, face);
            }
        };

        GuiHandler.register(this);

        // TODO
        // PacketHandler.register(this);

        proxy.init();

        config.save();
    }

    public static boolean canSculpt(Block block, World world, int x, int y, int z) {

        if (block == null)
            return false;
        if (block.equals(Blocks.BEDROCK))
            return false;
        IBlockState baseState = block.getBlockState().getBaseState();
        if (block.getMaterial(baseState) == Material.CIRCUITS)
            return false;
        if (block.getMaterial(baseState) == Material.FIRE)
            return false;
        if (block.getMaterial(baseState) == Material.LAVA)
            return false;
        if (block.getMaterial(baseState) == Material.WATER)
            return false;
        if (world.getTileEntity(new BlockPos(new Vec3i(x, y, z))) != null)
            return false;

        return true;
    }
}
