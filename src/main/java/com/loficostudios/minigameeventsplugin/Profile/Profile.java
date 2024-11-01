/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version SoulBoundSMPCore
 * @since 6/12/2024
 */

package com.loficostudios.minigameeventsplugin.Profile;

import com.loficostudios.melodyapi.file.impl.YamlFile;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Profile {

	@Getter
	private int wins;
	@Getter
	private int deaths;
	@Getter
	private int kills;

	private final YamlFile playerFile;
	private final FileConfiguration playerConfig;

	@Getter
	private final UUID uuid;

	@Getter
	private boolean isOptedOut;


	public void optOutOfGame() {
		isOptedOut = true;
	}

	public Profile(UUID uuid) {
		this.uuid = uuid;


		this.playerFile = new YamlFile("players/" + uuid + ".yml", AetherLabsPlugin.getInstance());
		this.playerConfig = playerFile.getConfig();

		this.wins = playerConfig.getInt("wins");
		this.deaths = playerConfig.getInt("deaths");
		this.kills = playerConfig.getInt("kills");

		playerFile.save();
	}

	public void saveData() {
		playerConfig.set("uuid", uuid.toString());

		playerConfig.set("kills", kills);
		playerConfig.set("wins", wins);
		playerConfig.set("deaths", deaths);

		playerFile.save();
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}


	public void addDeath() {
		deaths++;
		saveData();
	}

	public void addWin() {
		wins++;
		saveData();
	}

	public void addKill() {
		kills++;
		saveData();
	}


}
