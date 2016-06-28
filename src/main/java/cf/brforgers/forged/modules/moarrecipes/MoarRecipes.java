package cf.brforgers.forged.modules.moarrecipes;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
//import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.registry.GameRegistry;

import org.apache.commons.lang3.RandomUtils;

import cf.brforgers.forged.ForgedMod;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.registry.GameRegistry;

public class MoarRecipes extends SimplerModule
{
	@Override public String name() { return "MoarRecipes"; }
	@Override public ForgedEventState loadAt() { return ForgedEventState.INITIALIZATION; }
	
	@Override
	public void load(ForgedEvent event) {
		//if(CFG.FOUNDATION.MOARRECIPES.SHAPED_RECIPES)
		//{
			logger.info("Adding Shapeless Recipes...");
			addShapeless();
		//}
		//if(CFG.FOUNDATION.MOARRECIPES.SHAPELESS_RECIPES)
		//{
			logger.info("Adding Shaped Recipes...");
			addShaped();
		//}
		//if(CFG.FOUNDATION.MOARRECIPES.FURNACE_RECIPES)
		//{
			logger.info("Adding Furnaces Recipes...");
			addFurnace();
		//}
		//if(CFG.FOUNDATION.MOARRECIPES.DUNGEON_LOOT)
		//{
			logger.info("Adding Dungeon Loot...");
			addDungeonLoot();
		//}
		
		logger.info("Sucessfully Loaded!");
		
		String name = (event.side.isClient()) ? " " + Minecraft.getMinecraft().getSession().getUsername() + " " : " ";
		logger.info("Thanks" + name + "for Playing with MoarRecipes!");
	}
	
	public static void addShapeless()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.ice, 2), Blocks.packed_ice);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dispenser, 1) , Blocks.dropper, Items.bow);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dropper, 1) , Item.getItemFromBlock(Blocks.dispenser).setContainerItem(Items.bow));
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.oak_fence);
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.spruce_fence);
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.birch_fence);
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.jungle_fence);
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.acacia_fence);
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.stick,2), Blocks.dark_oak_fence);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.ender_pearl, 1), Items.ender_eye.setContainerItem(Items.blaze_powder));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 3), Blocks.crafting_table);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 7), Blocks.chest);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks, 4), Items.boat);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.experience_bottle, 1), Items.glass_bottle, Items.emerald);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.glowstone_dust, 1), Items.redstone, Items.blaze_powder);
		//GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian, 1), Items.water_bucket.setContainerItem(Items.bucket),Items.lava_bucket.setContainerItem(Items.bucket));
		//GameRegistry.addShapelessRecipe(new ItemStack(Blocks.hardened_clay) , Blocks.stained_hardened_clay);
		
		//1.1
		GameRegistry.addShapelessRecipe(new ItemStack(Items.iron_ingot, 3), Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars, Blocks.iron_bars);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian, 8), Item.getItemFromBlock(Blocks.ender_chest).setContainerItem(Items.ender_eye));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.glass), Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.iron_ingot, 5), Items.minecart);
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.chest), Items.chest_minecart.setContainerItem(Items.minecart));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.furnace), Items.furnace_minecart.setContainerItem(Items.minecart));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.hopper), Items.hopper_minecart.setContainerItem(Items.minecart));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.tnt), Items.tnt_minecart.setContainerItem(Items.minecart));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gold_ingot, 8), Items.golden_apple.setContainerItem(Items.apple));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.gold_block, 8), new ItemStack(Items.golden_apple.setContainerItem(Items.apple),1,1));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gold_nugget, 8), Items.golden_carrot.setContainerItem(Items.carrot));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gold_ingot, 8), Items.clock.setContainerItem(Items.redstone));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.iron_ingot, 8), Items.compass.setContainerItem(Items.redstone));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.emerald), Items.experience_bottle.setContainerItem(Items.glass_bottle));
		GameRegistry.addShapelessRecipe(new ItemStack(Items.string, 5), Blocks.web, Blocks.web, Blocks.web, Blocks.web);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.potato, 8), Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.poisonous_potato, Items.milk_bucket.setContainerItem(Items.bucket));
		
		for (int i = 0; i < 16; i++) {
			ItemStack output = new ItemStack(Blocks.stained_glass,3,i), input = new ItemStack(Blocks.stained_glass_pane,1,i);
			GameRegistry.addShapelessRecipe(output, input, input, input, input, input, input, input, input);
		}
	}
	
	public static void addShaped()
	{
		GameRegistry.addShapedRecipe(new ItemStack(Items.water_bucket, 1),
				" C ",
				"CBC",
				" C ",
				'C', Blocks.cactus,
				'B', Items.bucket
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.web, 4),
				"S S",
				" S ",
				"S S",
				'S', Items.string
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.packed_ice, 2),
				"II",
				"II",
				'I', Blocks.ice
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.planks, 1),
				"SS",
				"SS",
				'S', Items.stick
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.hopper, 1),
				"S S",
				"SCS",
				" S ",
				'S', Blocks.cobblestone,
				'C', Blocks.chest
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.hopper, 1),
				"S S",
				"SCS",
				" S ",
				'S', Blocks.stone,
				'C', Blocks.chest
		);
		
		//1.1
		GameRegistry.addShapedRecipe(new ItemStack(Items.saddle, 1),
				"R I",
				"LIL",
				'I', Items.iron_ingot,
				'R', Items.lead,
				'L', Items.leather
		);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.hardened_clay, 8),
				"CCC",
				"CBC",
				"CCC",
				'C', Blocks.stained_hardened_clay,
				'B', Items.water_bucket.setContainerItem(Items.bucket)
		);
	}
	
	public static void addFurnace()
	{
		GameRegistry.addSmelting(new ItemStack(Items.rotten_flesh), new ItemStack(Items.leather),  1);
	}
	
	public static void addDungeonLoot()
	{
		WeightedRandomChestContent[] loots = 
			new WeightedRandomChestContent[]{
				new WeightedRandomChestContent(new ItemStack(Blocks.dragon_egg), 1, 1, 1),
				new WeightedRandomChestContent(new ItemStack(Items.nether_star), 1, 1, 1),
				new WeightedRandomChestContent(new ItemStack(Items.clock), 1, 1, 25),
				new WeightedRandomChestContent(new ItemStack(Items.compass), 1, 1, 25),
				new WeightedRandomChestContent(new ItemStack(Items.emerald), 1, 5, 5),
				new WeightedRandomChestContent(new ItemStack(Items.slime_ball, 3), 1, 5, 10),
				new WeightedRandomChestContent(new ItemStack(Blocks.beacon), 1, 1, 1),
				
				//1.1
				new WeightedRandomChestContent(new ItemStack(Items.iron_pickaxe, 1, RandomUtils.nextInt(10, 200)), 1, 1, 10),
				new WeightedRandomChestContent(new ItemStack(Items.experience_bottle), 1, 10, 10),
			};
		String[] chestLoots =
			new String[] {
				ChestGenHooks.BONUS_CHEST,
				ChestGenHooks.DUNGEON_CHEST,
				ChestGenHooks.MINESHAFT_CORRIDOR,
				ChestGenHooks.PYRAMID_DESERT_CHEST,
				ChestGenHooks.PYRAMID_JUNGLE_CHEST,
				ChestGenHooks.PYRAMID_JUNGLE_DISPENSER,
				ChestGenHooks.STRONGHOLD_CORRIDOR,
				ChestGenHooks.STRONGHOLD_CROSSING,
				ChestGenHooks.STRONGHOLD_LIBRARY,
				ChestGenHooks.VILLAGE_BLACKSMITH
			};
		
		for (String chestLoot: chestLoots)
		{
			ChestGenHooks hook = ChestGenHooks.getInfo(chestLoot);
			for (WeightedRandomChestContent loot: loots)
			{
				hook.addItem(loot);
			}
		}
	}
}
