/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version SoulBoundSMPCore
 * @since 6/12/2024
 */

package com.loficostudios.minigameeventsplugin.player.profile;

import com.loficostudios.melodyapi.file.impl.YamlFile;
import com.loficostudios.minigameeventsplugin.AetherLabsPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProfile {

	@Getter
	private int wins;
	@Getter
	private int deaths;
	@Getter
	private int kills;

	private final YamlFile file;

	@Getter
	private final UUID uuid;

	@Getter
	private boolean isOptedOut;

	@Getter
	@Setter
	private double money;


	public void optOutOfGame() {
		isOptedOut = true;
	}

	public PlayerProfile(UUID uuid) {
		this.uuid = uuid;


		this.file = new YamlFile("players/" + uuid + ".yml", AetherLabsPlugin.inst());
		var conf = file.getConfig();

		this.wins = conf.getInt("wins");
		this.deaths = conf.getInt("deaths");
		this.kills = conf.getInt("kills");
		this.money = conf.getDouble("money");
		file.save();
	}

	public void saveData() {
		var conf = file.getConfig();
		conf.set("uuid", uuid.toString());

		conf.set("kills", kills);
		conf.set("wins", wins);
		conf.set("deaths", deaths);

		conf.set("money", money);

		file.save();
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
