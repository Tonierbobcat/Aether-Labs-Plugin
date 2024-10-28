/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version SoulBoundSMPCore
 * @since 6/12/2024
 */

package com.loficostudios.minigameeventsplugin.Profile;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class Profile {

	@Getter
	private int wins;
	@Getter
	private int deaths;
	@Getter
	private int kills;

	@Getter
	private final UUID uuid;

	public Profile(UUID uuid) {
		this.uuid = uuid;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}


	public void addDeath() {
		deaths++;
	}

	public void addWin() {
		wins++;
	}

	public void addKill() {
		kills++;
	}


}
