package cf.brforgers.forged.modules.moarrecipes;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

//import net.minecraftforge.common.CHESTGenHooks;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.registry.GameRegistry;

public class MoarRecipes extends SimplerModule
{
	public static void addShapeless()
	{
        GameRegistry.addShapelessRecipe(new ItemStack(ICE, 2), PACKED_ICE);
        GameRegistry.addShapelessRecipe(new ItemStack(DISPENSER, 1), DROPPER, BOW);
        GameRegistry.addShapelessRecipe(new ItemStack(DROPPER, 1), Item.getItemFromBlock(DISPENSER).setContainerItem(BOW));
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), OAK_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), SPRUCE_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), BIRCH_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), JUNGLE_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), ACACIA_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(STICK, 2), DARK_OAK_FENCE);
        GameRegistry.addShapelessRecipe(new ItemStack(ENDER_PEARL, 1), ENDER_EYE.setContainerItem(BLAZE_POWDER));
        GameRegistry.addShapelessRecipe(new ItemStack(PLANKS, 3), CRAFTING_TABLE);
        GameRegistry.addShapelessRecipe(new ItemStack(PLANKS, 7), CHEST);
        GameRegistry.addShapelessRecipe(new ItemStack(PLANKS, 4), BOAT);
        GameRegistry.addShapelessRecipe(new ItemStack(EXPERIENCE_BOTTLE, 1), GLASS_BOTTLE, EMERALD);
        GameRegistry.addShapelessRecipe(new ItemStack(GLOWSTONE, 1), REDSTONE, BLAZE_POWDER);
        //GameRegistry.addShapelessRecipe(new ItemStack(obsidian, 1), WATER_BUCKET.setContainerItem(BUCKET),lava_BUCKET.setContainerItem(BUCKET));
        //GameRegistry.addShapelessRecipe(new ItemStack(hardened_clay) , STAINED_hardened_clay);

		//1.1
        GameRegistry.addShapelessRecipe(new ItemStack(IRON_INGOT, 3), IRON_BARS, IRON_BARS, IRON_BARS, IRON_BARS, IRON_BARS, IRON_BARS, IRON_BARS, IRON_BARS);
        GameRegistry.addShapelessRecipe(new ItemStack(OBSIDIAN, 8), Item.getItemFromBlock(ENDER_CHEST).setContainerItem(ENDER_EYE));
        GameRegistry.addShapelessRecipe(new ItemStack(GLASS), GLASS_PANE, GLASS_PANE, GLASS_PANE, GLASS_PANE, GLASS_PANE, GLASS_PANE, GLASS_PANE, GLASS_PANE);
        GameRegistry.addShapelessRecipe(new ItemStack(IRON_INGOT, 5), MINECART);
        GameRegistry.addShapelessRecipe(new ItemStack(CHEST), CHEST_MINECART.setContainerItem(MINECART));
        GameRegistry.addShapelessRecipe(new ItemStack(FURNACE), FURNACE_MINECART.setContainerItem(MINECART));
        GameRegistry.addShapelessRecipe(new ItemStack(HOPPER), HOPPER_MINECART.setContainerItem(MINECART));
        GameRegistry.addShapelessRecipe(new ItemStack(TNT), TNT_MINECART.setContainerItem(MINECART));
        GameRegistry.addShapelessRecipe(new ItemStack(GOLD_INGOT, 8), GOLDEN_APPLE.setContainerItem(APPLE));
        GameRegistry.addShapelessRecipe(new ItemStack(GOLD_NUGGET, 6), GOLDEN_CARROT.setContainerItem(CARROT));
        GameRegistry.addShapelessRecipe(new ItemStack(GOLD_INGOT, 4), CLOCK.setContainerItem(REDSTONE));
        GameRegistry.addShapelessRecipe(new ItemStack(IRON_INGOT, 2), COMPASS.setContainerItem(REDSTONE));
        GameRegistry.addShapelessRecipe(new ItemStack(EMERALD), EXPERIENCE_BOTTLE.setContainerItem(GLASS_BOTTLE));
        GameRegistry.addShapelessRecipe(new ItemStack(STRING, 5), WEB, WEB, WEB, WEB);
        GameRegistry.addShapelessRecipe(new ItemStack(POTATO, 8), POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, POISONOUS_POTATO, MILK_BUCKET.setContainerItem(BUCKET));

		for (int i = 0; i < 16; i++) {
            ItemStack output = new ItemStack(STAINED_GLASS, 3, i), input = new ItemStack(STAINED_GLASS_PANE, 1, i);
            GameRegistry.addShapelessRecipe(output, input, input, input, input, input, input, input, input);
		}

        GameRegistry.addShapelessRecipe(new ItemStack(GOLDEN_APPLE, 1, 1), GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_APPLE);
    }

	public static void addShaped()
	{
        GameRegistry.addShapedRecipe(new ItemStack(WATER_BUCKET, 1),
                " C ",
				"CBC",
				" C ",
                'C', CACTUS,
                'B', BUCKET
        );

        GameRegistry.addShapedRecipe(new ItemStack(WEB, 4),
                "S S",
				" S ",
				"S S",
                'S', STRING
        );

        GameRegistry.addShapedRecipe(new ItemStack(PACKED_ICE, 2),
                "II",
				"II",
                'I', ICE
        );

        GameRegistry.addShapedRecipe(new ItemStack(PLANKS, 1),
                "SS",
				"SS",
                'S', STICK
        );

        GameRegistry.addShapedRecipe(new ItemStack(HOPPER, 1),
                "S S",
				"SCS",
				" S ",
                'S', COBBLESTONE,
                'C', CHEST
        );

        GameRegistry.addShapedRecipe(new ItemStack(HOPPER, 1),
                "S S",
				"SCS",
				" S ",
                'S', STONE,
                'C', CHEST
        );

		//1.1
        GameRegistry.addShapedRecipe(new ItemStack(SADDLE, 1),
                "R I",
				"LIL",
                'I', IRON_INGOT,
                'R', LEAD,
                'L', LEATHER
        );

        GameRegistry.addShapedRecipe(new ItemStack(HARDENED_CLAY, 8),
                "CCC",
				"CBC",
				"CCC",
                'C', STAINED_HARDENED_CLAY,
                'B', WATER_BUCKET.setContainerItem(BUCKET)
        );
	}
	
	public static void addFurnace()
	{
        GameRegistry.addSmelting(new ItemStack(ROTTEN_FLESH), new ItemStack(LEATHER), 1);
    }

    @Override
    public String name() {
        return "MoarRecipes";
    }

    @Override
    public ForgedEventState loadAt() {
        return ForgedEventState.INITIALIZATION;
    }

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
        logger.info("Adding FURNACEs Recipes...");
        addFurnace();
        //}
        //if(CFG.FOUNDATION.MOARRECIPES.DUNGEON_LOOT)
        //{
//			logger.info("Adding Dungeon Loot...");
//			addDungeonLoot();
        //}

        logger.info("Sucessfully Loaded!");

        String name = (event.side.isClient()) ? " " + Minecraft.getMinecraft().getSession().getUsername() + " " : " ";
        logger.info("Thanks" + name + "for Playing with MoarRecipes!");
    }

//	public static void addDungeonLoot()
//	{
//		WeightedRandomCHESTContent[] loots =
//			new WeightedRandomCHESTContent[]{
//				new WeightedRandomCHESTContent(new ItemStack(dragon_egg), 1, 1, 1),
//				new WeightedRandomCHESTContent(new ItemStack(nether_star), 1, 1, 1),
//				new WeightedRandomCHESTContent(new ItemStack(clock), 1, 1, 25),
//				new WeightedRandomCHESTContent(new ItemStack(compass), 1, 1, 25),
//				new WeightedRandomCHESTContent(new ItemStack(emerald), 1, 5, 5),
//				new WeightedRandomCHESTContent(new ItemStack(slime_ball, 3), 1, 5, 10),
//				new WeightedRandomCHESTContent(new ItemStack(beacon), 1, 1, 1),
//
//				//1.1
//				new WeightedRandomCHESTContent(new ItemStack(IRON_pickaxe, 1, RandomUtils.nextInt(10, 200)), 1, 1, 10),
//				new WeightedRandomCHESTContent(new ItemStack(experience_bottle), 1, 10, 10),
//			};
//		String[] CHESTLoots =
//			new String[] {
//				CHESTGenHooks.BONUS_CHEST,
//				CHESTGenHooks.DUNGEON_CHEST,
//				CHESTGenHooks.MINESHAFT_CORRIDOR,
//				CHESTGenHooks.PYRAMID_DESERT_CHEST,
//				CHESTGenHooks.PYRAMID_JUNGLE_CHEST,
//				CHESTGenHooks.PYRAMID_JUNGLE_DISPENSER,
//				CHESTGenHooks.STRONGHOLD_CORRIDOR,
//				CHESTGenHooks.STRONGHOLD_CROSSING,
//				CHESTGenHooks.STRONGHOLD_LIBRARY,
//				CHESTGenHooks.VILLAGE_BLACKSMITH
//			};
//
//		for (String CHESTLoot: CHESTLoots)
//		{
//			CHESTGenHooks hook = CHESTGenHooks.getInfo(CHESTLoot);
//			for (WeightedRandomCHESTContent loot: loots)
//			{
//				hook.addItem(loot);
//			}
//		}
//	}
}
