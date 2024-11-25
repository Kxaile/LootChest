package fr.black_eyes.lootchest.commands.commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.util.BlockIterator;

import fr.black_eyes.lootchest.Constants;
import fr.black_eyes.lootchest.Lootchest;
import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.Mat;
import fr.black_eyes.lootchest.LootChestUtils;
import fr.black_eyes.lootchest.commands.ArgType;
import fr.black_eyes.lootchest.commands.SubCommand;
import fr.black_eyes.lootchest.ui.UiHandler;
import fr.black_eyes.simpleJavaPlugin.Utils;

public class CreateCommand extends SubCommand {
	
	private final UiHandler uiHandler;
	public CreateCommand(UiHandler uiHandler) {
		super("create", Arrays.asList(ArgType.STRING));
		setPlayerRequired(true);

		this.uiHandler = uiHandler;
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
    Player player = sender instanceof Player ? (Player) sender : null;
    Block chest;
    String chestName = args[1];

    if (player != null) {
        BlockIterator iter = new BlockIterator(player, 10);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        chest = lastBlock;

        if (!Mat.isALootChestBlock(chest)) {
            // Create a chest at the player's location
            Location playerLocation = player.getLocation();
            playerLocation.getBlock().setType(Material.CHEST);
            chest = playerLocation.getBlock();
            Utils.msg(sender, "Chest was not found, creating a new chest at your location.", " ", " ");
        }
    } else {
        // If the sender is not a player, create a chest at (0, 200, 0)
        Location consoleLocation = new Location(Bukkit.getWorld("world"), 0, 200, 0); // Adjust "world" if necessary
        Block consoleBlock = consoleLocation.getBlock();
        consoleBlock.setType(Material.CHEST);
        chest = consoleBlock;
        Utils.msg(sender, "Chest was not found, creating a new chest at (0, 200, 0).", " ", " ");
    }

    if (LootChestUtils.isEmpty(((InventoryHolder) chest.getState()).getInventory())) {
        Utils.msg(sender, "chestIsEmpty", " ", " ");
    } else if (Main.getInstance().getLootChest().containsKey(chestName)) {
        Utils.msg(sender, "chestAlreadyExists", Constants.cheststr, chestName);
    } else if (LootChestUtils.isLootChest(chest.getLocation()) != null) {
        Utils.msg(sender, "blockIsAlreadyLootchest", Constants.cheststr, chestName);
    } else {
        Lootchest newChest = new Lootchest(chest, chestName);
        Main.getInstance().getLootChest().put(chestName, newChest);
        newChest.spawn(true);
        newChest.updateData();
        Utils.msg(sender, "chestSuccessfullySaved", Constants.cheststr, chestName);

        // If the sender is a player, open the UI
        if (player != null) {
            uiHandler.openUi(player, UiHandler.UiType.MAIN, newChest);
        }
    }
}
}
