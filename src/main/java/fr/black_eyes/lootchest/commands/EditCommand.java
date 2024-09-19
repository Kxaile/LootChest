package fr.black_eyes.lootchest.commands;

import fr.black_eyes.lootchest.Constants;
import fr.black_eyes.lootchest.Main;
import fr.black_eyes.lootchest.Utils;
import fr.black_eyes.lootchest.ui.UiHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EditCommand extends SubCommand {
	
	private final UiHandler uiHandler;

	public EditCommand(UiHandler uiHandler) {
		super("edit", Arrays.asList(ArgType.LOOTCHEST));
		setPlayerRequired(true);
		this.uiHandler = uiHandler;
	}
	
	@Override
	protected void onCommand(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String chestName = args[1];
		uiHandler.openUi(player, UiHandler.UiType.MAIN, Main.getInstance().getLootChest().get(chestName));
	}
}
